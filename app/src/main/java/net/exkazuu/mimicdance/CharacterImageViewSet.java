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
    private final int[][] firstImageIds;
    private final int[][] secondImageIds;

    private CharacterImageViewSet(CharacterType charaType, Activity activity) {
        this.charaType = charaType;

        BodyPartType[] bodyPartTypes = BodyPartType.values();
        this.bodyParts = new ImageView[bodyPartTypes.length];

        ActionType[] actions = ActionType.values();
        CharacterType[] characters = CharacterType.values();
        this.firstImageIds = new int[actions.length][characters.length];
        this.secondImageIds = new int[actions.length][characters.length];

        initializeImageViews(charaType, activity, bodyPartTypes);
        initializeImageIds(activity, actions, characters);
    }

    private void initializeImageIds(Activity activity, ActionType[] actions, CharacterType[] characters) {
        for (int i = 0; i < actions.length; i++) {
            ActionType action = actions[i];
            for (int j = 0; j < characters.length; j++) {
                String prefix = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, characters[j].name()) + "_";
                String actionName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, action.name());
                String firstName = prefix + actionName.replace("down", "up") + "2";
                String secondName = prefix + actionName.replace("up", "up3").replace("down", "up1").replace("jump", "basic");

                firstImageIds[i][j] = activity.getResources().getIdentifier(firstName, "drawable", activity.getPackageName());
                secondImageIds[i][j] = activity.getResources().getIdentifier(secondName, "drawable", activity.getPackageName());
            }
        }
    }

    private void initializeImageViews(CharacterType charaType, Activity activity, BodyPartType[] bodyPartTypes) {
        for (int i = 0; i < bodyPartTypes.length; i++) {
            String name = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, charaType.name()) + bodyPartTypes[i].name();
            Log.i("charaType", charaType.name());
            int id = activity.getResources().getIdentifier(name, "id", activity.getPackageName());
            bodyParts[i] = (ImageView) activity.findViewById(id);
            if (i < bodyPartTypes.length - 1) {
                String tname = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, CharacterType.getCurrentType().name().toLowerCase() + bodyPartTypes[i].name());
                String drawableName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, tname) + "_up1";
                int drawableId = activity.getResources().getIdentifier(drawableName, "drawable", activity.getPackageName());
                bodyParts[i].setImageResource(drawableId);
                bodyParts[i].setVisibility(View.VISIBLE);
            }
        }
    }

    public static CharacterImageViewSet createPiyoLeft(Activity activity) {
        return new CharacterImageViewSet(CharacterType.Piyo, activity);
    }

    public static CharacterImageViewSet createPiyoRight(Activity activity) {
        return new CharacterImageViewSet(CharacterType.AltPiyo, activity);
    }

    public static CharacterImageViewSet createCoccoLeft(Activity activity) {
        return new CharacterImageViewSet(CharacterType.Cocco, activity);
    }

    public static CharacterImageViewSet createCoccoRight(Activity activity) {
        return new CharacterImageViewSet(CharacterType.AltCocco, activity);
    }

    public static CharacterImageViewSet createBoLeft(Activity activity) {
        return new CharacterImageViewSet(CharacterType.Bo, activity);
    }

    public static CharacterImageViewSet createBoRight(Activity activity) {
        return new CharacterImageViewSet(CharacterType.AltBo, activity);
    }

    public void changeToInitialImages() {
        changeToMovedImages(Lists.newArrayList(ActionType.LeftFootDown, ActionType.Jump.LeftHandDown,
            ActionType.RightFootDown, ActionType.RightHandDown));
    }

    public void changeToMovingImages(Collection<ActionType> actions) {
        for (ActionType action : actions) {
            bodyParts[action.toBodyPart().ordinal()]
                .setImageResource(firstImageIds[action.ordinal()][CharacterType.getCurrentType().ordinal()]);
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
                .setImageResource(secondImageIds[actionType.ordinal()][CharacterType.getCurrentType().ordinal()]);
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
