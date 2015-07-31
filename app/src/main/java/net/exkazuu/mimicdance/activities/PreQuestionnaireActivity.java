package net.exkazuu.mimicdance.activities;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;

import net.exkazuu.mimicdance.R;
import net.exkazuu.mimicdance.models.PreQuestionnaireResult;

public class PreQuestionnaireActivity extends BaseActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // タイトルバー非表示
        setContentView(R.layout.pre_questionnaire);
    }

    public void save(View view) {
        PreQuestionnaireResult result = new PreQuestionnaireResult();
        result.examineeId = ((EditText) findViewById(R.id.examineeId)).getText().toString();
        result.sex = ((RadioButton) findViewById(R.id.radioMale)).isChecked() ? "男" : "女";
        try {
            result.age = Integer.parseInt(((EditText) findViewById(R.id.age)).getText().toString());
        } catch (Exception e) {
        }

        result.knowledgeOfProgramming = ((RadioButton) findViewById(R.id.radioYesProgramming)).isChecked();
        result.knowledgeOfMimicDance = ((RadioButton) findViewById(R.id.radioYesMimicDance)).isChecked();

        result.desireToLearn = ((SeekBar) findViewById(R.id.desireToLearn)).getProgress();
        result.fun = ((SeekBar) findViewById(R.id.fun)).getProgress();
        result.feasibility = ((SeekBar) findViewById(R.id.feasibility)).getProgress();
        result.usefulness = ((SeekBar) findViewById(R.id.usefulness)).getProgress();

        result.save();
        startTitleActivity(true);
    }

    public void startTitleActivity(View view) {
        startTitleActivity(true);
    }

}
