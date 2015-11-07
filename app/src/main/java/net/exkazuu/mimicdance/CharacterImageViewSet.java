package net.exkazuu.mimicdance;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.common.base.CaseFormat;
import com.google.common.collect.Lists;

import net.exkazuu.mimicdance.interpreter.ActionType;
import net.exkazuu.mimicdance.interpreter.BodyPartType;
import net.exkazuu.mimicdance.interpreter.CharacterType;

import java.util.Collection;

public class CharacterImageViewSet {
    private final CharacterType charaType;
    private final ImageView[] bodyParts;
    private final int[] firstImageIds;
    private final int[] secondImageIds;

    private CharacterImageViewSet(String prefix, CharacterType charaType, Activity activity) {
        this.charaType = charaType;

        BodyPartType[] bodyPartTypes = BodyPartType.values();
        this.bodyParts = new ImageView[bodyPartTypes.length];

        ActionType[] actions = ActionType.values();
        this.firstImageIds = new int[actions.length];
        this.secondImageIds = new int[actions.length];

        initializeImageViews(prefix, activity, bodyPartTypes);
        initializeImageIds(activity, actions);
    }

    private void initializeImageIds(Activity activity, ActionType[] actions) {
        for (int i = 0; i < actions.length; i++) {
            ActionType action = actions[i];
            String prefix = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, charaType.name()) + "_";
            String actionName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, action.name());
            String firstName = prefix + actionName.replace("down", "up") + "2";
            String secondName = prefix + actionName.replace("up", "up3").replace("down", "up1").replace("jump", "basic");

            firstImageIds[i] = activity.getResources().getIdentifier(firstName, "drawable", activity.getPackageName());
            secondImageIds[i] = activity.getResources().getIdentifier(secondName, "drawable", activity.getPackageName());
        }
    }

    private void initializeImageViews(String prefix, Activity activity, BodyPartType[] bodyPartTypes) {
        for (int i = 0; i < bodyPartTypes.length; i++) {
            String name = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, prefix) + bodyPartTypes[i].name();
            Log.i("charaType", charaType.name());
            int id = activity.getResources().getIdentifier(name, "id", activity.getPackageName());
            bodyParts[i] = (ImageView) activity.findViewById(id);
            String drawableName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, charaType.name()+bodyPartTypes[i].name()) + ((i < bodyPartTypes.length - 1) ? "_up1" : "");
            int drawableId = activity.getResources().getIdentifier(drawableName, "drawable", activity.getPackageName());
            bodyParts[i].setImageResource(drawableId);
            bodyParts[i].setVisibility(View.VISIBLE);
        }
    }

    public static CharacterImageViewSet createStudentLeft(Activity activity) {
        return new CharacterImageViewSet(CharacterType.getViewIdPrefixes()[0], CharacterType.getCharacters()[0], activity);
    }

    public static CharacterImageViewSet createStudentRight(Activity activity) {
        return new CharacterImageViewSet(CharacterType.getViewIdPrefixes()[1], CharacterType.getCharacters()[1], activity);
    }

    public static CharacterImageViewSet createTeacherLeft(Activity activity) {
        return new CharacterImageViewSet(CharacterType.getViewIdPrefixes()[2], CharacterType.getCharacters()[2], activity);
    }

    public static CharacterImageViewSet createTeacherRight(Activity activity) {
        return new CharacterImageViewSet(CharacterType.getViewIdPrefixes()[3], CharacterType.getCharacters()[3], activity);
    }

    public void changeToInitialImages() {
        changeToMovedImages(Lists.newArrayList(ActionType.LeftFootDown, ActionType.Jump.LeftHandDown,
            ActionType.RightFootDown, ActionType.RightHandDown));
    }

    public void changeToMovingImages(Collection<ActionType> actions) {
        for (ActionType action : actions) {
            bodyParts[action.toBodyPart().ordinal()]
                .setImageResource(firstImageIds[action.ordinal()]);
        }
        if (actions.contains(ActionType.Jump)) {
            for (int i = 0; i < bodyParts.length - 1; i++) {
                bodyParts[i].setVisibility(View.INVISIBLE);
            }
        }
    }

    public void changeToMovedImages(Collection<ActionType> actions) {
        for (ActionType actionType : actions) {
            bodyParts[actionType.toBodyPart().ordinal()]
                .setImageResource(secondImageIds[actionType.ordinal()]);
        }
        if (actions.contains(ActionType.Jump)) {
            for (int i = 0; i < bodyParts.length - 1; i++) {
                bodyParts[i].setVisibility(View.VISIBLE);
            }
        }
    }

    public void changeToMovingErrorImage() {
        if (charaType == CharacterType.Piyo) {

            getBody().setImageResource(R.drawable.korobu_1);
        } else {
            getBody().setImageResource(R.drawable.alt_korobu_1);
        }
        for (int i = 0; i < bodyParts.length - 1; i++) {
            bodyParts[i].setVisibility(View.INVISIBLE);
        }
    }

    public void changeToMovedErrorImage() {
        if (charaType == CharacterType.Piyo) {

            getBody().setImageResource(R.drawable.korobu_3);
        } else {
            getBody().setImageResource(R.drawable.alt_korobu_3);
        }
    }

    public ImageView getBody() {
        return bodyParts[4];
    }
}
