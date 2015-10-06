package net.exkazuu.mimicdance.interpreter;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import net.exkazuu.mimicdance.CharacterImageViewSet;
import net.exkazuu.mimicdance.activities.ArduinoManager;
import net.exkazuu.mimicdance.activities.PlugManager;
import net.exkazuu.mimicdance.controller.PwmMotorController;
import net.exkazuu.mimicdance.program.UnrolledProgram;

import java.util.Collection;
import java.util.Set;

public class Interpreter implements Runnable {

    public static final int WAITING_COUNT = 2;
    private final UnrolledProgram program;
    private final CharacterImageViewSet charaViewSet;
    private final TextView textView;
    private final boolean isPiyo;
    private final Pose pose;

    private Set<ActionType> actions;
    private int executionCount;
    private boolean failed;
    private PwmMotorController danboController;
    private String bearCommand;
    private final byte[] command = new byte[2]; //右手、左手の2つ

    public static Interpreter createForPiyo(UnrolledProgram program, CharacterImageViewSet charaViewSet, TextView textView, Context context) {
        return new Interpreter(program, charaViewSet, textView, context, true);
    }

    public static Interpreter createForCocco(UnrolledProgram program, CharacterImageViewSet charaViewSet) {
        return new Interpreter(program, charaViewSet, null, null, false);
    }

    private Interpreter(UnrolledProgram program, CharacterImageViewSet charaViewSet, TextView textView, Context context, boolean isPiyo) {
        this.program = program;
        this.charaViewSet = charaViewSet;
        this.textView = textView;
        this.isPiyo = isPiyo;
        this.pose = new Pose();
        this.bearCommand = "";
    }

    @Override
    public void run() {
        if (executionCount < WAITING_COUNT) {
            charaViewSet.changeToInitialImages();
        } else if (finished()) {
            return;
        } else if (isMoving()) {
            if (isPiyo) {
                highlightLine();
            }

            actions = program.getActionSet(getLineIndex());
            if (!failed && ActionType.validate(actions) && pose.validate(actions)) {
                pose.change(actions);
                charaViewSet.changeToMovingImages(actions);
                if (isPiyo) {
                    handleDanbo();
                    handleBear(actions);
                    handleMiniBear();
                }
            } else {
                failed = true;
                Log.v("tag", "failed");
                charaViewSet.changeToMovingErrorImage();
            }
        } else {
            if (!failed) {
                charaViewSet.changeToMovedImages(actions);
            } else {
                charaViewSet.changeToMovedErrorImage();
            }
        }
        executionCount++;
    }

    public void finish() {
        if (isPiyo) {
            pose.reset();
            handleDanbo();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            if (danboController != null) {
                danboController.release();
            }
        }
    }

    public boolean finished() {
        return getLineIndex() >= program.size();
    }

    public boolean existsError() {
        return failed;
    }

    private int getLineIndex() {
        return (executionCount - WAITING_COUNT) / 2;
    }

    private boolean isMoving() {
        return (executionCount & 1) == 0;
    }

    private void highlightLine() {
        int currentLineIndex = program.getOriginalLineIndex(getLineIndex());
        String[] lines = textView.getText().toString().split("\n");
        textView.getEditableText().clear();
        for (int lineIndex = 0; lineIndex < lines.length; lineIndex++) {
            if (currentLineIndex == lineIndex) {
                textView.append(
                    Html.fromHtml("<font color=#ff0000>" + lines[lineIndex] + "</font>"));
                textView.append("\n");
            } else {
                textView.append(lines[lineIndex] + "\n");
            }
        }
    }

    private void handleDanbo() {
        if (!PlugManager.isPlugged()) {
            return;
        }
        if (danboController == null) {
            danboController = new PwmMotorController(50000, 50);
            danboController.play();
        }
        double left = pose.isLeftHandUp() ? 0.5 : 1.5;
        double right = pose.isRightHandUp() ? 2.5 : 1.5;
        danboController.setPulseMilliseconds(left, right);
    }

    private void handleBear(Collection<ActionType> actions) {
        if (actions.contains(ActionType.LeftHandDown)) {
            bearCommand = bearCommand.replace("lau", "");
        } else if (actions.contains(ActionType.LeftHandUp)) {
            bearCommand += "lau";
        }
        if (actions.contains(ActionType.RightHandDown)) {
            bearCommand = bearCommand.replace("rau", "");
        } else if (actions.contains(ActionType.RightHandUp)) {
            bearCommand += "rau";
        }
        if (actions.contains(ActionType.LeftFootDown)) {
            bearCommand = bearCommand.replace("llu", "");
        } else if (actions.contains(ActionType.LeftFootUp)) {
            bearCommand += "llu";
        }
        if (actions.contains(ActionType.RightFootDown)) {
            bearCommand = bearCommand.replace("rlu", "");
        } else if (actions.contains(ActionType.RightFootUp)) {
            bearCommand += "rlu";
        }
        if (actions.contains(ActionType.Jump)) {
            bearCommand += "jump";
        } else {
            bearCommand = bearCommand.replace("jump", "");
        }
        new BearHandlingTask(bearCommand).execute();
    }

    private void handleMiniBear() {
        if (!ArduinoManager.isPlugged()) {
            return;
        }

        command[1] = (byte) (pose.isLeftHandUp() ? 0x1 : 0x0); //左手(port3)
        command[0] = (byte) (pose.isRightHandUp() ? 0x1 : 0x0); //右手(port2)
        ArduinoManager.sendCommand(command);
    }
}
