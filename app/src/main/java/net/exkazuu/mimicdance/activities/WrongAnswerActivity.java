package net.exkazuu.mimicdance.activities;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.exkazuu.mimicdance.R;

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

        if (getIntent().getBooleanExtra("almostCorrect", false)) {
            showAltPiyoAnimationForAlmostCorrect(this, altPiyoView);
            showPiyoAnimationForAlmostCorrect(this, piyoView);
            LinearLayout wrongBackground = (LinearLayout) findViewById(R.id.wrong_background);
            wrongBackground.setBackgroundColor(0xFF67E47E);
            TextView wrongTitle = (TextView) findViewById(R.id.wrong_title);
            wrongTitle.setText("おしい！！！");
        } else {
            showAltPiyoAnimationForWrongAnswer(this, altPiyoView);
            showPiyoAnimationForWrongAnswer(this, piyoView);
        }

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

    void showAltPiyoAnimationForWrongAnswer(Context con, View v) {
        altPiyoAnimation = new AnimationDrawable();

        // 画像の読み込み
        Drawable frame1 = con.getResources().getDrawable(R.drawable.alt_korobu_1);
        Drawable frame2 = con.getResources().getDrawable(R.drawable.alt_korobu_2);
        Drawable frame3 = con.getResources().getDrawable(R.drawable.alt_korobu_3);

        // 画像をアニメーションのコマとして追加していく
        altPiyoAnimation.addFrame(frame1, 1000);
        altPiyoAnimation.addFrame(frame2, 700);
        altPiyoAnimation.addFrame(frame3, 700);

        altPiyoAnimation.setOneShot(true);

        // ビューの背景画像にアニメーションを設定
        v.setBackgroundDrawable(altPiyoAnimation);

        // アニメーション開始
        altPiyoAnimation.start();
    }

    void showPiyoAnimationForWrongAnswer(Context con, View v) {
        piyoAnimation = new AnimationDrawable();

        // 画像の読み込み
        Drawable frame1 = con.getResources().getDrawable(R.drawable.korobu_1);
        Drawable frame2 = con.getResources().getDrawable(R.drawable.korobu_2);
        Drawable frame3 = con.getResources().getDrawable(R.drawable.korobu_3);

        // 画像をアニメーションのコマとして追加していく
        piyoAnimation.addFrame(frame1, 1000);
        piyoAnimation.addFrame(frame2, 700);
        piyoAnimation.addFrame(frame3, 700);

        piyoAnimation.setOneShot(true);

        // ビューの背景画像にアニメーションを設定
        v.setBackgroundDrawable(piyoAnimation);

        // アニメーション開始
        piyoAnimation.start();
    }

    void showAltPiyoAnimationForAlmostCorrect(Context con, View v) {
        altPiyoAnimation = new AnimationDrawable();

        // 画像の読み込み //
        Drawable frame1 = con.getResources().getDrawable(R.drawable.alt_korobu_3);
        Drawable frame2 = con.getResources().getDrawable(R.drawable.alt_korobu_1);
        Drawable frame3 = con.getResources().getDrawable(R.drawable.alt_piyo_stand);
        Drawable frame4 = con.getResources().getDrawable(R.drawable.alt_piyo_raising_hand);

        // 画像をアニメーションのコマとして追加していく
        altPiyoAnimation.addFrame(frame1, 1500);
        altPiyoAnimation.addFrame(frame2, 700);
        altPiyoAnimation.addFrame(frame3, 700);
        altPiyoAnimation.addFrame(frame4, 700);

        altPiyoAnimation.setOneShot(true);

        // ビューの背景画像にアニメーションを設定
        v.setBackgroundDrawable(altPiyoAnimation);

        // アニメーション開始
        altPiyoAnimation.start();
    }

    void showPiyoAnimationForAlmostCorrect(Context con, View v) {
        piyoAnimation = new AnimationDrawable();

        // 画像の読み込み //
        Drawable frame1 = con.getResources().getDrawable(R.drawable.korobu_3);
        Drawable frame2 = con.getResources().getDrawable(R.drawable.korobu_1);
        Drawable frame3 = con.getResources().getDrawable(R.drawable.piyo_stand);
        Drawable frame4 = con.getResources().getDrawable(R.drawable.piyo_raising_hand);

        // 画像をアニメーションのコマとして追加していく
        piyoAnimation.addFrame(frame1, 1500);
        piyoAnimation.addFrame(frame2, 700);
        piyoAnimation.addFrame(frame3, 700);
        piyoAnimation.addFrame(frame4, 700);

        piyoAnimation.setOneShot(true);

        // ビューの背景画像にアニメーションを設定
        v.setBackgroundDrawable(piyoAnimation);

        // アニメーション開始
        piyoAnimation.start();
    }
}
