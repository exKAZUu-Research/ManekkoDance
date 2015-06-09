package net.exkazuu.mimicdance.activities;

import android.app.Activity;
import android.content.Intent;

public abstract class BaseActivity extends Activity {

    /**
     * 実行結果を取得できる形で判定画面に遷移します。
     *
     * @param lessonNumber
     * @param piyoCode
     * @param clear
     */
    protected void startEvaluationActivity(int lessonNumber, String piyoCode, boolean clear) {
        Intent intent = new Intent(this, EvaluationActivity.class);
        intent.putExtra("lessonNumber", lessonNumber);
        intent.putExtra("piyoCode", piyoCode);
        if (clear) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        startActivity(intent);
    }

    /**
     * お手本画面に遷移します。
     *
     * @param lessonNumber
     * @param piyoCode
     * @param clear
     */
    protected void startCoccoActivity(int lessonNumber, String piyoCode, boolean clear) {
        Intent intent = new Intent(this, CoccoActivity.class);
        intent.putExtra("lessonNumber", lessonNumber);
        intent.putExtra("piyoCode", piyoCode);
        if (clear) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        startActivity(intent);
    }

    /**
     * コーディング画面に遷移します。
     *
     * @param lessonNumber
     * @param piyoCode
     * @param clear
     */
    protected void startCodingActivity(int lessonNumber, String piyoCode, boolean clear) {
        Intent intent = new Intent(this, CodingActivity.class);
        intent.putExtra("lessonNumber", lessonNumber);
        intent.putExtra("piyoCode", piyoCode);
        if (clear) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        startActivity(intent);
    }

    /**
     * ヘルプ画面に遷移します。
     *
     * @param clear
     */
    protected void startHelpActivity(boolean clear) {
        Intent intent = new Intent(this, HelpActivity.class);
        if (clear) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        startActivity(intent);
    }

    /**
     * タイトル画面に遷移します。
     *
     * @param clear
     */
    protected void startTitleActivity(boolean clear) {
        Intent intent = new Intent(this, TitleActivity.class);
        if (clear) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        startActivity(intent);
    }

    /**
     * レッスン選択画面に遷移します。
     *
     * @param clear
     */
    protected void startLessonListActivity(boolean clear) {
        Intent intent = new Intent(this, LessonListActivity.class);
        if (clear) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        startActivity(intent);
    }
}