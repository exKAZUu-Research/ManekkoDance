package net.exkazuu.mimicdance.interpreter;

/**
 * Created by exKAZUu on 2015/05/30.
 */
public enum CharacterType {
    Piyo, AltPiyo, Cocco, AltCocco, Bo, AltBo;

    private static CharacterType currentType = Cocco;

    public static CharacterType getCurrentType() {
        return currentType;
    }

}
