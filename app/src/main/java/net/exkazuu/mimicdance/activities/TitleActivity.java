package net.exkazuu.mimicdance.activities;

import android.os.Bundle;
import android.provider.Settings;
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

import im.delight.android.ddp.Meteor;

public class TitleActivity extends BaseActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        uploadData();
    }

    public void startHelpActivity(View view) {
        startHelpActivity(false);
    }

    public void startLessonListActivity(View view) {
        startLessonListActivity(true);
    }

    public void startPreQuestionnaireActivity(View view) {
        startPreQuestionnaireActivity(false);
    }

    public void startPostQuestionnaireActivity(View view) {
        startPostQuestionnaireActivity(false);
    }

    public void freePlay(View view) {
    }

    public void uploadData() {
        try {
            Meteor meteor = new Meteor(this, "ws://mimic-dance-server.herokuapp.com/websocket");
            List<LessonClear> lessonClears = new Select().from(LessonClear.class).orderBy("Created_at").execute();
            for (LessonClear lessonClear : lessonClears) {
                Map<String, Object> values = new HashMap<>();
                values.put("created_at", lessonClear.created_at);
                values.put("examineeId", lessonClear.examineeId);
                values.put("lessonNumber", lessonClear.lessonNumber);
                values.put("seconds", lessonClear.milliseconds / 1000);
                values.put("moveCount", lessonClear.moveCount);
                meteor.insert("PlayLogs", values);
            }

            String androidId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
            List<PreQuestionnaireResult> preQuestionnaireResults = new Select().from(PreQuestionnaireResult.class).orderBy("Created_at").execute();
            for (PreQuestionnaireResult preQuestionnaireResult : preQuestionnaireResults) {
                Map<String, Object> values = new HashMap<>();
                values.put("created_at", preQuestionnaireResult.created_at);
                values.put("examineeId", preQuestionnaireResult.examineeId);
                values.put("androidId", androidId);
                values.put("type", "通常版");
                values.put("sex", preQuestionnaireResult.sex);
                values.put("age", preQuestionnaireResult.age);
                values.put("knowledgeOfProgramming", preQuestionnaireResult.knowledgeOfProgramming);
                values.put("knowledgeOfMimicDance", preQuestionnaireResult.knowledgeOfMimicDance);
                values.put("desireToLearn", preQuestionnaireResult.desireToLearn);
                values.put("fun", preQuestionnaireResult.fun);
                values.put("feasibility", preQuestionnaireResult.feasibility);
                values.put("usefulness", preQuestionnaireResult.usefulness);
                meteor.insert("PreQuestionnaireResults", values);
            }

            List<PostQuestionnaireResult> postQuestionnaireResults = new Select().from(PostQuestionnaireResult.class).orderBy("Created_at").execute();
            for (PostQuestionnaireResult postQuestionnaireResult : postQuestionnaireResults) {
                Map<String, Object> values = new HashMap<>();
                values.put("created_at", postQuestionnaireResult.created_at);
                values.put("examineeId", postQuestionnaireResult.examineeId);
                values.put("gladness", postQuestionnaireResult.gladness);
                values.put("vexation", postQuestionnaireResult.vexation);
                values.put("desireToPlay", postQuestionnaireResult.desireToPlay);
                values.put("additionalPlayTime", postQuestionnaireResult.additionalPlayTime);
                values.put("desireToLearn", postQuestionnaireResult.desireToLearn);
                values.put("fun", postQuestionnaireResult.fun);
                values.put("feasibility", postQuestionnaireResult.feasibility);
                values.put("usefulness", postQuestionnaireResult.usefulness);
                values.put("opinion", postQuestionnaireResult.opinion);
                meteor.insert("PostQuestionnaireResults", values);
            }
            //new Delete().from(LessonClear.class).execute();
            //new Delete().from(PreQuestionnaireResult.class).execute();
            //new Delete().from(PostQuestionnaireResult.class).execute();
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
