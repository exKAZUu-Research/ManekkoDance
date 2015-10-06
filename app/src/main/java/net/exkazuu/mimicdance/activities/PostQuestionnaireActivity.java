package net.exkazuu.mimicdance.activities;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import net.exkazuu.mimicdance.R;
import net.exkazuu.mimicdance.models.PostQuestionnaireResult;

public class PostQuestionnaireActivity extends BaseActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // タイトルバー非表示
        setContentView(R.layout.post_quetionnaire);
        setOnSeekBarChangeListener(R.id.gladness, R.id.gladnessLebel, "1.正解したとき嬉しかったですか？");
        setOnSeekBarChangeListener(R.id.vexation, R.id.vexationLabel, "2.正解できなかったとき悔しかったですか？");
        setOnSeekBarChangeListener(R.id.desireToPlay, R.id.desireToPlayLabel, "3.もっとアプリで遊びたいですか？");
        setOnSeekBarChangeListener(R.id.desireToLearn, R.id.desireToLearnLabel, "4.プログラミングを学びたいですか？");
        setOnSeekBarChangeListener(R.id.fun, R.id.funLabel, "5.プログラミングは楽しそうですか？");
        setOnSeekBarChangeListener(R.id.feasibility, R.id.feasibilityLabel, "6.プログラミングはできそうですか？");
        setOnSeekBarChangeListener(R.id.usefulness, R.id.usefulnessLabel, "7.あなたにとってプログラミングのスキルは役立つと思いますか？");
    }

    public void setOnSeekBarChangeListener(int seekBarId, final int seekBarLabelId, final String before) {
        final TextView seekBarLabel = ((TextView) findViewById(seekBarLabelId));
        ((SeekBar) findViewById(seekBarId)).setOnSeekBarChangeListener(
            new SeekBar.OnSeekBarChangeListener() {
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    seekBarLabel.setText(before + "(10が最高値, 現在は" + (seekBar.getProgress() + 1) + ")");
                    ((Button) findViewById(R.id.btnSave)).setEnabled(isSavable());
                }

                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            }
        );
    }

    public boolean isSavable() {
        if (((EditText) findViewById(R.id.examineeId)).getText().length() == 0) {
            return false;
        }
        if (!((RadioButton) findViewById(R.id.radioTime0)).isChecked() && !((RadioButton) findViewById(R.id.radioTime15)).isChecked() &&
            !((RadioButton) findViewById(R.id.radioTime30)).isChecked() && !((RadioButton) findViewById(R.id.radioTime45)).isChecked() &&
            !((RadioButton) findViewById(R.id.radioTime60)).isChecked() && !((RadioButton) findViewById(R.id.radioTime120)).isChecked()) {
            return false;
        }
        return true;
    }

    public void checkSavable(View view) {
        ((Button) findViewById(R.id.btnSave)).setEnabled(isSavable());
    }

    public void save(View view) {
        PostQuestionnaireResult result = new PostQuestionnaireResult();
        result.examineeId = ((EditText) findViewById(R.id.examineeId)).getText().toString();

        result.gladness = ((SeekBar) findViewById(R.id.gladness)).getProgress();
        result.vexation = ((SeekBar) findViewById(R.id.vexation)).getProgress();

        result.desireToPlay = ((SeekBar) findViewById(R.id.desireToPlay)).getProgress();
        if (((RadioButton) findViewById(R.id.radioTime0)).isChecked()) {
            result.additionalPlayTime = 0;
        } else if (((RadioButton) findViewById(R.id.radioTime15)).isChecked()) {
            result.additionalPlayTime = 15;
        } else if (((RadioButton) findViewById(R.id.radioTime30)).isChecked()) {
            result.additionalPlayTime = 30;
        } else if (((RadioButton) findViewById(R.id.radioTime45)).isChecked()) {
            result.additionalPlayTime = 45;
        } else if (((RadioButton) findViewById(R.id.radioTime60)).isChecked()) {
            result.additionalPlayTime = 60;
        } else if (((RadioButton) findViewById(R.id.radioTime120)).isChecked()) {
            result.additionalPlayTime = 120;
        }

        result.desireToLearn = ((SeekBar) findViewById(R.id.desireToLearn)).getProgress();
        result.fun = ((SeekBar) findViewById(R.id.fun)).getProgress();
        result.feasibility = ((SeekBar) findViewById(R.id.feasibility)).getProgress();
        result.usefulness = ((SeekBar) findViewById(R.id.usefulness)).getProgress();

        result.opinion = ((EditText) findViewById(R.id.opinion)).getText().toString();

        result.save();
        startTitleActivity(true);
    }

    public void startTitleActivity(View view) {
        startTitleActivity(true);
    }
}
