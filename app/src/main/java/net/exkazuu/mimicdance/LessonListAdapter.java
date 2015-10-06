package net.exkazuu.mimicdance;

import android.content.Context;
import android.graphics.Color;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class LessonListAdapter extends ArrayAdapter<String> {

    public LessonListAdapter(Context context, int textViewResourceId, List<String> objects) {
        super(context, textViewResourceId, objects);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        if (position % 2 == 0) {
            view.setBackgroundColor(Color.rgb(255, 153, 255));
        } else {
            view.setBackgroundColor(Color.rgb(255, 204, 204));
        }
        TextView textView = (TextView) view;
        textView.setTextSize(36);

        textView.setHeight(167);//lesson6の場合は195で設定
        return view;
    }
}
