package net.exkazuu.mimicdance.interpreter;

import net.exkazuu.mimicdance.R;

public enum IconType {
    RightHandUp("右腕を上げる", R.drawable.icon_right_hand_up, R.id.programIconView1),
    RightHandDown("右腕を下げる", R.drawable.icon_right_hand_down, R.id.programIconView2),
    LeftHandUp("左腕を上げる", R.drawable.icon_left_hand_up, R.id.programIconView3),
    LeftHandDown("左腕を下げる", R.drawable.icon_left_hand_down, R.id.programIconView4),
    //    RightFootUp("右足を上げる", R.drawable.icon_right_foot_up),
    //    RightFootDown("右足を下げる", R.drawable.icon_right_foot_down),
    //    LeftFootUp("左足を上げる", R.drawable.icon_left_foot_up),
    //    LeftFootDown("左足を下げる", R.drawable.icon_left_foot_down),
    //    Jump("ジャンプする", R.drawable.icon_jump),
    Loop("くりかえし", R.drawable.icon_loop, R.id.programIconView5),
    EndLoop("ここまで", R.drawable.icon_end_loop, R.id.programIconView6),
    White("しろ", R.drawable.icon_yellow, R.id.programIconView7),
    Yellow("きいろ", R.drawable.icon_brawn, R.id.programIconView8),
    If("もしも", R.drawable.icon_if, R.id.programIconView9),
    Else("もしくは", R.drawable.icon_else, R.id.programIconView10),
    EndIf("もしおわり", R.drawable.icon_end_if, R.id.programIconView11),
    Number0("0", R.drawable.icon_num0, R.id.numberIconView0),
    Number1("1", R.drawable.icon_num1, R.id.numberIconView1),
    Number2("2", R.drawable.icon_num2, R.id.numberIconView2),
    Number3("3", R.drawable.icon_num3, R.id.numberIconView3),
    Number4("4", R.drawable.icon_num4, R.id.numberIconView4),
    Number5("5", R.drawable.icon_num5, R.id.numberIconView5),
    Number6("6", R.drawable.icon_num6, R.id.numberIconView6),
    Number7("7", R.drawable.icon_num7, R.id.numberIconView7),
    Number8("8", R.drawable.icon_num8, R.id.numberIconView8),
    Number9("9", R.drawable.icon_num9, R.id.numberIconView9),;

    public final String text;
    public final int drawable;
    public final int id;

    IconType(String text, int drawable, int id) {
        this.text = text;
        this.drawable = drawable;
        this.id = id;
    }

    public static IconType getByText(String text) {
        for (IconType iconType : values()) {
            if (iconType.text.equals(text)) {
                return iconType;
            }
        }
        return null;
    }

    public static IconType getByDrawable(int drawable) {
        for (IconType iconType : values()) {
            if (iconType.drawable == drawable) {
                return iconType;
            }
        }
        return null;
    }

    public static IconType getById(int id) {
        for (IconType iconType : values()) {
            if (iconType.id == id) {
                return iconType;
            }
        }
        return null;
    }
}
