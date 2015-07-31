package net.exkazuu.mimicdance.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import net.exkazuu.mimicdance.R;

public class HelpActivity extends BaseActivity {
    private final int[] HELP_IMAGE_RESOURCES = {R.drawable.tutorial1, R.drawable.tutorial2,
        R.drawable.tutorial3, R.drawable.tutorial4, R.drawable.tutorial5,
        R.drawable.helptext1, R.drawable.helptext2};
    private int pageNumber;

    private final String PAGE_SUFFIX = " / " + HELP_IMAGE_RESOURCES.length;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);

        pageNumber = 0;
        updatePageNumber();
    }

    public void next(View view) {
        pageNumber++;
        pageNumber %= HELP_IMAGE_RESOURCES.length;
        updatePageNumber();
    }

    public void prev(View view) {
        pageNumber--;
        pageNumber += HELP_IMAGE_RESOURCES.length;
        pageNumber %= HELP_IMAGE_RESOURCES.length;
        updatePageNumber();
    }

    public void close(View view) {
        finish();
    }

    public void updatePageNumber() {
        EditText pageText = (EditText) findViewById(R.id.page);
        pageText.setText((pageNumber + 1) + PAGE_SUFFIX);

        ImageView helpView = (ImageView) findViewById(R.id.helpImage);
        helpView.setImageResource(HELP_IMAGE_RESOURCES[pageNumber]);
    }
}
