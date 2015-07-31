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
        freeButton.setVisibility(View.VISIBLE);
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
            String androidId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
            for (LessonClear lessonClear : lessonClears) {
                Map<String, Object> values = new HashMap<>();
                values.put("androidId", androidId);
                values.put("created_at", lessonClear.created_at);
                values.put("lessonNumber", lessonClear.lessonNumber);
                values.put("seconds", lessonClear.milliseconds / 1000);
                values.put("moveCount", lessonClear.moveCount);
                meteor.insert("play-log", values);
            }
            //new Delete().from(LessonClear.class).execute();
            Log.d("upload", "count: " + lessonClears.size());
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
