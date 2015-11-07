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

        if (getIntent().getBooleanExtra("almostCorrect", false)) {
            showAltStudentAnimationForAlmostCorrect(this, altPiyoView);
            showStudentAnimationForAlmostCorrect(this, piyoView);
            LinearLayout wrongBackground = (LinearLayout) findViewById(R.id.wrong_background);
            wrongBackground.setBackgroundColor(0xFF67E47E);
            TextView wrongTitle = (TextView) findViewById(R.id.wrong_title);
            wrongTitle.setText("おしい！！！");
        } else {
            showAltStudentAnimationForWrongAnswer(altPiyoView);
            showStudentAnimationForWrongAnswer(piyoView);
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

    private void showAltStudentAnimationForWrongAnswer(View v) {
        altPiyoAnimation = new AnimationDrawable();
        String currentTypeName = CharacterType.getCharacters()[1].name().toLowerCase();
        showStudentAnimationForWrongAnswer(v, currentTypeName, altPiyoAnimation);
    }

    private void showStudentAnimationForWrongAnswer(View v) {
        piyoAnimation = new AnimationDrawable();
        String currentTypeName = CharacterType.getCharacters()[0].name().toLowerCase();
        showStudentAnimationForWrongAnswer(v, currentTypeName, piyoAnimation);
    }

    private void showStudentAnimationForWrongAnswer(View v, String name, AnimationDrawable animation) {
        int korobu1id = getResources().getIdentifier(name + "_korobu_1", "drawable", getPackageName());
        int korobu2id = getResources().getIdentifier(name + "_korobu_2", "drawable", getPackageName());
        int korobu3id = getResources().getIdentifier(name + "_korobu_3", "drawable", getPackageName());

        Drawable frame1 = getResources().getDrawable(korobu1id);
        Drawable frame2 = getResources().getDrawable(korobu2id);
        Drawable frame3 = getResources().getDrawable(korobu3id);

        // 画像をアニメーションのコマとして追加していく
        animation.addFrame(frame1, 1000);
        animation.addFrame(frame2, 700);
        animation.addFrame(frame3, 700);

        animation.setOneShot(true);

        // ビューの背景画像にアニメーションを設定
        v.setBackgroundDrawable(animation);

        // アニメーション開始
        animation.start();
    }

    private void showAltStudentAnimationForAlmostCorrect(Context con, View v) {
        altPiyoAnimation = new AnimationDrawable();

        // 画像の読み込み //
        String name = CharacterType.getCharacters()[1].name().toLowerCase();
        int korobu3id = getResources().getIdentifier(name + "_korobu_3", "drawable", getPackageName());
        int korobu1id = getResources().getIdentifier(name + "_korobu_1", "drawable", getPackageName());
        int standid = getResources().getIdentifier(name + "_stand", "drawable", getPackageName());
        int raisingHandid = getResources().getIdentifier(name + "_raising_hand", "drawable", getPackageName());

        Drawable frame1 = con.getResources().getDrawable(korobu3id);
        Drawable frame2 = con.getResources().getDrawable(korobu1id);
        Drawable frame3 = con.getResources().getDrawable(standid);
        Drawable frame4 = con.getResources().getDrawable(raisingHandid);

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

    private void showStudentAnimationForAlmostCorrect(Context con, View v) {
        piyoAnimation = new AnimationDrawable();

        // 画像の読み込み //
        String name = CharacterType.getCharacters()[0].name().toLowerCase();
        int korobu3id = getResources().getIdentifier(name + "_korobu_3", "drawable", getPackageName());
        int korobu1id = getResources().getIdentifier(name + "_korobu_1", "drawable", getPackageName());
        int standid = getResources().getIdentifier(name + "_stand", "drawable", getPackageName());
        int raisingHandid = getResources().getIdentifier(name + "_raising_hand", "drawable", getPackageName());

        Drawable frame1 = con.getResources().getDrawable(korobu3id);
        Drawable frame2 = con.getResources().getDrawable(korobu1id);
        Drawable frame3 = con.getResources().getDrawable(standid);
        Drawable frame4 = con.getResources().getDrawable(raisingHandid);

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
