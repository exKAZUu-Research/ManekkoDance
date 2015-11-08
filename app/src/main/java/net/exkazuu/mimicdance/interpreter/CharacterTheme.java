package net.exkazuu.mimicdance.interpreter;

/**
 * Created by ENIXER on 2015/11/07.
 */
public enum CharacterTheme {
    Chicken, StickFig, GirlToBoy, BoyToGirl, Graphic;
    private static String[] themeValues = {"ひよこがにわとりにならう", "ボウニンゲンがボウニンゲンにならう", "男の子が女の子にならう", "女の子が男の子にならう"};
    private static CharacterTheme currentTheme = Chicken;

    public static String[] getThemeValues() {
        return themeValues;
    }

    public static void setCharacterTheme(int type) {
        currentTheme = values()[type];
    }

    static CharacterTheme getCurrentTheme() {
        return currentTheme;
    }
}
