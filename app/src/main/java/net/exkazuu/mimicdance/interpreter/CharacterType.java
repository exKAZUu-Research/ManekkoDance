package net.exkazuu.mimicdance.interpreter;

/**
 * Created by exKAZUu on 2015/05/30.
 */
public enum CharacterType {
    Piyo, AltPiyo, Cocco, AltCocco, Bo, AltBo;

    private static CharacterType currentType = Bo;

    public static CharacterType getCurrentType() {
        return currentType;
    }

    public static void setCocco() {
        currentType = Cocco;
    }

    public static void setBo() {
        currentType = Bo;
    }

}
