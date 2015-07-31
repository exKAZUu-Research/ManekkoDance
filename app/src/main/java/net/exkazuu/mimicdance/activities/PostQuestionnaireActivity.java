package net.exkazuu.mimicdance.activities;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;

import net.exkazuu.mimicdance.R;
import net.exkazuu.mimicdance.models.PostQuestionnaireResult;

public class PostQuestionnaireActivity extends BaseActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // タイトルバー非表示
        setContentView(R.layout.post_quetionnaire);

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
