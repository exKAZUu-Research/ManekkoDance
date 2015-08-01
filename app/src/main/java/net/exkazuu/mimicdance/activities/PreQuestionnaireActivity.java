package net.exkazuu.mimicdance.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import net.exkazuu.mimicdance.R;
import net.exkazuu.mimicdance.models.PreQuestionnaireResult;

public class PreQuestionnaireActivity extends BaseActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // タイトルバー非表示
        setContentView(R.layout.pre_questionnaire);
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
        ((EditText) findViewById(R.id.examineeId)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ((Button) findViewById(R.id.btnSave)).setEnabled(isSavable());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        ((EditText) findViewById(R.id.age)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ((Button) findViewById(R.id.btnSave)).setEnabled(isSavable());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public boolean isSavable() {
        if (((EditText) findViewById(R.id.examineeId)).getText().length() == 0) {
            return false;
        }
        if (!((RadioButton) findViewById(R.id.radioMale)).isChecked() && !((RadioButton) findViewById(R.id.radioFemale)).isChecked()) {
            return false;
        }
        try {
            Integer.parseInt(((EditText) findViewById(R.id.age)).getText().toString());
        } catch (Exception e) {
            return false;
        }
        if (!((RadioButton) findViewById(R.id.radioYesProgramming)).isChecked() && !((RadioButton) findViewById(R.id.radioNoProgramming)).isChecked()) {
            return false;
        }
        if (!((RadioButton) findViewById(R.id.radioYesMimicDance)).isChecked() && !((RadioButton) findViewById(R.id.radioNoMimicdance)).isChecked()) {
            return false;
        }
        return true;
    }

    public void checkSavable(View view) {
        ((Button) findViewById(R.id.btnSave)).setEnabled(isSavable());
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
