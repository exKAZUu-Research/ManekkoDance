package net.exkazuu.mimicdance.activities;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import net.exkazuu.mimicdance.Lessons;
import net.exkazuu.mimicdance.R;
import net.exkazuu.mimicdance.models.LessonClear;

public class CorrectAnswerActivity extends BaseActivity {
    AnimationDrawable coccoAnimation = null;
    AnimationDrawable piyoAnimation = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // タイトルバー非表示
        setContentView(R.layout.correct_answer);

        final int lessonNumber = getIntent().getIntExtra("lessonNumber", 1);
        final String piyoCode = getIntent().getStringExtra("piyoCode");
        ImageView jumpCocco = (ImageView) findViewById(R.id.cocco);
        ImageView jumpPiyo = (ImageView) findViewById(R.id.piyo);

        startCoccoAnimation(this, jumpCocco);
        startPiyoAnimation(this, jumpPiyo);

        Button again = (Button) findViewById(R.id.check_again);
        Button list = (Button) findViewById(R.id.correct_lesson_list);
        Button next = (Button) findViewById(R.id.next_lesson);
        again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startEvaluationActivity(lessonNumber, piyoCode, true);
            }
        });
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLessonListActivity(true);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCoccoActivity(Math.min(lessonNumber + 1, Lessons.getLessonCount()), "", true);
            }
        });

        LessonClear.createAndSave(lessonNumber);
    }

    @Override
    protected void onPause() {
        super.onPause();
        coccoAnimation.stop();
        piyoAnimation.stop();
        finish();
    }

    void startCoccoAnimation(Context con, View v) {
        coccoAnimation = new AnimationDrawable();

        // 画像の読み込み //
        Drawable frame1 = con.getResources().getDrawable(R.drawable.cocco_jump1);
        Drawable frame2 = con.getResources().getDrawable(R.drawable.cocco_jump2);

        // 画像をアニメーションのコマとして追加していく
        coccoAnimation.addFrame(frame1, 500);
        coccoAnimation.addFrame(frame2, 500);

        // 繰り返し設定
        coccoAnimation.setOneShot(false);

        // ビューの背景画像にアニメーションを設定
        v.setBackgroundDrawable(coccoAnimation);

        // アニメーション開始
        coccoAnimation.start();
    }

    void startPiyoAnimation(Context con, View v) {
        piyoAnimation = new AnimationDrawable();

        // 画像の読み込み
        Drawable frame1 = con.getResources().getDrawable(R.drawable.piyo_jump1);
        Drawable frame2 = con.getResources().getDrawable(R.drawable.piyo_jump2);

        // 画像をアニメーションのコマとして追加していく
        piyoAnimation.addFrame(frame1, 500);
        piyoAnimation.addFrame(frame2, 500);

        // 繰り返し設定
        piyoAnimation.setOneShot(false);

        // ビューの背景画像にアニメーションを設定
        v.setBackgroundDrawable(piyoAnimation);

        // アニメーション開始
        piyoAnimation.start();
    }
}
