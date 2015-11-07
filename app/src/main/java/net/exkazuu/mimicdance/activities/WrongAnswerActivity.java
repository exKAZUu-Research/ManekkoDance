package net.exkazuu.mimicdance.activities;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.common.base.CaseFormat;

import net.exkazuu.mimicdance.R;
import net.exkazuu.mimicdance.interpreter.CharacterType;

public class WrongAnswerActivity extends BaseActivity {
    AnimationDrawable altPiyoAnimation = null;
    AnimationDrawable piyoAnimation = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // タイトルバー非表示
        setContentView(R.layout.wrong_answer);
        final int lessonNumber = getIntent().getIntExtra("lessonNumber", 1);
        final String piyoCode = getIntent().getStringExtra("piyoCode");

        int diffCount = getIntent().getIntExtra("diffCount", 0);
        TextView diffCountView = (TextView) findViewById(R.id.differenceCount);
        diffCountView.setText(diffCount + "コのまちがいがあるよ");

        ImageView altPiyoView = (ImageView) findViewById(R.id.altPiyo);
        ImageView piyoView = (ImageView) findViewById(R.id.piyo);

        showAltStudentAnimationForWrongAnswer(altPiyoView);
        showStudentAnimationForWrongAnswer(piyoView);

        Button list = (Button) findViewById(R.id.wrong_lesson_list);
        Button again = (Button) findViewById(R.id.try_again);

        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLessonListActivity(true);
            }
        });
        again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCodingActivity(lessonNumber, piyoCode, true);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        altPiyoAnimation.stop();
        piyoAnimation.stop();
        finish();
    }

    private void showAltStudentAnimationForWrongAnswer(View v) {
        altPiyoAnimation = new AnimationDrawable();
        String currentTypeName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, CharacterType.getAltStudent().name());
        showStudentAnimationForWrongAnswer(v, currentTypeName, altPiyoAnimation);
    }

    private void showStudentAnimationForWrongAnswer(View v) {
        piyoAnimation = new AnimationDrawable();
        String currentTypeName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, CharacterType.getStudent().name());
        showStudentAnimationForWrongAnswer(v, currentTypeName, piyoAnimation);
    }

    private void showStudentAnimationForWrongAnswer(View v, String name, AnimationDrawable animation) {
        int korobu1id = getResources().getIdentifier(name + "_korobu1", "drawable", getPackageName());
        int korobu2id = getResources().getIdentifier(name + "_korobu2", "drawable", getPackageName());
        int korobu3id = getResources().getIdentifier(name + "_korobu3", "drawable", getPackageName());

        Drawable frame1 = getResources().getDrawable(korobu1id);
        Drawable frame2 = getResources().getDrawable(korobu2id);
        Drawable frame3 = getResources().getDrawable(korobu3id);

        // 画像をアニメーションのコマとして追加していく
        animation.addFrame(frame1, 1000);
        animation.addFrame(frame2, 700);
        animation.addFrame(frame3, 700);

        startAnimation(v, animation);
    }

    private void startAnimation(View v, AnimationDrawable animation) {
        animation.setOneShot(true);

        // ビューの背景画像にアニメーションを設定
        v.setBackgroundDrawable(animation);

        // アニメーション開始
        animation.start();
    }

}
