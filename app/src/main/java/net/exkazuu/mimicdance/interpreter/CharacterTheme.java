package net.exkazuu.mimicdance.interpreter;

/**
 * Created by ENIXER on 2015/11/07.
 */
public enum CharacterTheme {
    Chicken, StickFig, GirlToBoy, BoyToGirl, Graphic;
    private static String[] themeValues = {"ひよことにわとり", "棒人間", "男の子と女の子", "女の子と男の子"};
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
