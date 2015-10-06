package net.exkazuu.mimicdance.activities;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.activeandroid.query.Select;

import net.exkazuu.mimicdance.R;
import net.exkazuu.mimicdance.models.LessonClear;
import net.exkazuu.mimicdance.models.PostQuestionnaireResult;
import net.exkazuu.mimicdance.models.PreQuestionnaireResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import im.delight.android.ddp.MeteorSingleton;

public class TitleActivity extends BaseActivity {
    int debug = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!MeteorSingleton.isCreated()) {
            MeteorSingleton.createInstance(this, "ws://mimic-dance-server.herokuapp.com/websocket");
        }

        requestWindowFeature(Window.FEATURE_NO_TITLE); // タイトルバー非表示
        setContentView(R.layout.title);

        Button helpButton = (Button) findViewById(R.id.help_button);
        helpButton.setVisibility(View.VISIBLE);
        Button startButton = (Button) findViewById(R.id.start_button);
        startButton.setVisibility(View.VISIBLE);
        Button freeButton = (Button) findViewById(R.id.free_button);
        freeButton.setVisibility(View.GONE);
        Button preButton = (Button) findViewById(R.id.pre_ques_button);
        preButton.setVisibility(View.VISIBLE);
        Button postButton = (Button) findViewById(R.id.post_ques_button);
        postButton.setVisibility(View.VISIBLE);

        Button kumaBottun = (Button) findViewById(R.id.kuma_test);
        kumaBottun.setVisibility(View.VISIBLE);

        uploadData();
    }

    public void debugKuma(View view) {
        byte command[] = new byte[2];
        if (debug == 0) {
            command[1] = 1;
            debug = 1;
        } else {
            command[1] = 0;
            debug = 0;
        }
        ArduinoManager.sendCommand(command);
    }

    public void startHelpActivity(View view) {
        uploadData();
        startHelpActivity(false);
    }

    public void startLessonListActivity(View view) {
        uploadData();
        startLessonListActivity(true);
    }

    public void startPreQuestionnaireActivity(View view) {
        uploadData();
        startPreQuestionnaireActivity(false);
    }

    public void startPostQuestionnaireActivity(View view) {
        uploadData();
        startPostQuestionnaireActivity(false);
    }

    public void freePlay(View view) {
    }

    public void uploadData() {
        MeteorSingleton.getInstance().reconnect();
        List<LessonClear> lessonClears = new Select().from(LessonClear.class).where("Sent = ?", false).orderBy("Created_at").execute();
        List<PreQuestionnaireResult> preQuestionnaireResults = new Select().from(PreQuestionnaireResult.class).where("Sent = ?", false).orderBy("Created_at").execute();
        List<PostQuestionnaireResult> postQuestionnaireResults = new Select().from(PostQuestionnaireResult.class).where("Sent = ?", false).orderBy("Created_at").execute();
        Log.d("upload", "lessonClears: " + lessonClears.size());
        Log.d("upload", "preQuestionnaireResults: " + preQuestionnaireResults.size());
        Log.d("upload", "postQuestionnaireResults: " + postQuestionnaireResults.size());
        try {
            if (!MeteorSingleton.getInstance().isConnected()) {
                return;
            }
            for (LessonClear item : lessonClears) {
                Map<String, Object> values = new HashMap<>();
                values.put("created_at", item.created_at);
                values.put("examineeId", item.examineeId);
                values.put("lessonNumber", item.lessonNumber);
                values.put("seconds", item.milliseconds / 1000);
                values.put("moveCount", item.moveCount);
                MeteorSingleton.getInstance().insert("PlayLogs", values);
                item.sent = true;
                item.save();
            }

            String androidId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
            for (PreQuestionnaireResult item : preQuestionnaireResults) {
                Map<String, Object> values = new HashMap<>();
                values.put("created_at", item.created_at);
                values.put("examineeId", item.examineeId);
                values.put("androidId", androidId);
                values.put("type", "豪華版");
                values.put("sex", item.sex);
                values.put("age", item.age);
                values.put("knowledgeOfProgramming", item.knowledgeOfProgramming);
                values.put("knowledgeOfMimicDance", item.knowledgeOfMimicDance);
                values.put("desireToLearn", item.desireToLearn);
                values.put("fun", item.fun);
                values.put("feasibility", item.feasibility);
                values.put("usefulness", item.usefulness);
                MeteorSingleton.getInstance().insert("PreQuestionnaireResults", values);
                item.sent = true;
                item.save();
            }

            for (PostQuestionnaireResult item : postQuestionnaireResults) {
                Map<String, Object> values = new HashMap<>();
                values.put("created_at", item.created_at);
                values.put("examineeId", item.examineeId);
                values.put("gladness", item.gladness);
                values.put("vexation", item.vexation);
                values.put("desireToPlay", item.desireToPlay);
                values.put("additionalPlayTime", item.additionalPlayTime);
                values.put("desireToLearn", item.desireToLearn);
                values.put("fun", item.fun);
                values.put("feasibility", item.feasibility);
                values.put("usefulness", item.usefulness);
                values.put("opinion", item.opinion);
                MeteorSingleton.getInstance().insert("PostQuestionnaireResults", values);
                item.sent = true;
                item.save();
            }
            Log.d("upload", "uploaded");
        } catch (Exception e) {
        }
    }

    //    private PwmMotorController controller = new PwmMotorController(50000, 50, 1.5, 1.5);
//
//    @Override
//    protected void onPause() {
//        controller.release();
//        super.onPause();
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//            case MotionEvent.ACTION_MOVE:
//                WindowManager wm = getWindowManager();
//                Display disp = wm.getDefaultDisplay();
//                Point size = new Point();
//                disp.getSize(size);
//                double leftPulseMilliseconds = 0.5 + ((double) event.getX() / size.x) * 2;
//                double rightPulseMilliseconds = 0.5 + ((double) event.getY() / size.y) * 2;
//                controller.setLeftPulseMilliseconds(leftPulseMilliseconds);
//                controller.setRightPulseMilliseconds(rightPulseMilliseconds);
//                short[] buffer = controller.generatePwmData();
//                int count = 0;
//                for (short v : buffer) {
//                    if (v == Short.MAX_VALUE) {
//                        count++;
//                    }
//                }
//                ((TextView) findViewById(R.id.txtPulseLength)).setText("Left: " + leftPulseMilliseconds);
//                ((TextView) findViewById(R.id.txtFrequency)).setText("Right: " + rightPulseMilliseconds);
//                ((TextView) findViewById(R.id.txtCheck)).setText("Check: " + count);
//                controller.play();
//                break;
//            case MotionEvent.ACTION_UP:
//                controller.stop();
//                break;
//        }
//        return super.onTouchEvent(event);
//    }
}
