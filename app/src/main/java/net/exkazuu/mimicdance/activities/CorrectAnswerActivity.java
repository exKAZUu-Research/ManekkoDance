package net.exkazuu.mimicdance.activities;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import net.exkazuu.mimicdance.Lessons;
import net.exkazuu.mimicdance.R;
import net.exkazuu.mimicdance.interpreter.CharacterType;
import net.exkazuu.mimicdance.models.LessonClear;

public class CorrectAnswerActivity extends BaseActivity {
    public static final int DURATION = 500;
    private AnimationDrawable coccoAnimation = null;
    private AnimationDrawable piyoAnimation = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // タイトルバー非表示
        setContentView(R.layout.correct_answer);

        final int lessonNumber = getIntent().getIntExtra("lessonNumber", 1);
        final String piyoCode = getIntent().getStringExtra("piyoCode");
        ImageView jumpCocco = (ImageView) findViewById(R.id.cocco);
        ImageView jumpPiyo = (ImageView) findViewById(R.id.piyo);

        startTeacherAnimation(jumpCocco);
        startPiyoAnimation(jumpPiyo);

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

    private void startTeacherAnimation(View v) {
        coccoAnimation = new AnimationDrawable();

        // 画像の読み込み
        String characterNamePrefix = CharacterType.getCharacters()[2].name().toLowerCase();
        int jump1id = getResources().getIdentifier(characterNamePrefix + "_jump1", "drawable", getPackageName());
        int jump2id = getResources().getIdentifier(characterNamePrefix + "_jump2", "drawable", getPackageName());
        setAnimation(v, jump1id, jump2id, coccoAnimation);
    }

    private void setAnimation(View v, int jump1id, int jump2id, AnimationDrawable animation) {
        Drawable frame1 = getResources().getDrawable(jump1id);
        Drawable frame2 = getResources().getDrawable(jump2id);

        // 画像をアニメーションのコマとして追加していく
        animation.addFrame(frame1, DURATION);
        animation.addFrame(frame2, DURATION);

        // 繰り返し設定
        animation.setOneShot(false);

        // ビューの背景画像にアニメーションを設定
        v.setBackgroundDrawable(animation);

        // アニメーション開始
        animation.start();
    }

    private void startPiyoAnimation(View v) {
        piyoAnimation = new AnimationDrawable();

        // 画像の読み込み
        String characterNamePrefix = CharacterType.getCharacters()[0].name().toLowerCase();
        int jump1id = getResources().getIdentifier(characterNamePrefix + "_jump1", "drawable", getPackageName());
        int jump2id = getResources().getIdentifier(characterNamePrefix + "_jump2", "drawable", getPackageName());
        setAnimation(v, jump1id, jump2id, piyoAnimation);
    }
}
