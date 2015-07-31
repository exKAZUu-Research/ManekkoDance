package net.exkazuu.mimicdance.activities;

import android.os.Bundle;
import android.view.Window;

import net.exkazuu.mimicdance.R;

/**
 * Created by t-yokoi on 2015/07/31.
 */
public class PostQuestionnaireActivity extends BaseActivity{
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // タイトルバー非表示
        setContentView(R.layout.post_quetionnaire);



    }
}
