package net.exkazuu.mimicdance.interpreter;

import net.exkazuu.mimicdance.exception.UnKnownCharacterThemeException;

/**
 * Created by exKAZUu on 2015/05/30.
 */
public enum CharacterType {
    Piyo, AltPiyo, Cocco, AltCocco, Bo, AltBo, Boy, AltBoy, Girl, AltGirl, Graph, AltGraph;

    public static CharacterType[] getCharacters() {
        switch (CharacterTheme.getCurrentTheme()) {
            case Chicken:
                return new CharacterType[]{Piyo, AltPiyo, Cocco, AltCocco};
            case StickFig:
                return new CharacterType[]{Bo, AltBo, Bo, AltBo};
            case GirlToBoy:
                return new CharacterType[]{Boy, AltBoy, Girl, AltGirl};
            case BoyToGirl:
                return new CharacterType[]{Girl, AltGirl, Boy, AltBoy};
            case Graphic:
                return new CharacterType[]{Graph, AltGraph, Graph, AltGraph};
            default:
                throw new UnKnownCharacterThemeException(CharacterTheme.getCurrentTheme());
        }
    }

    public static String[] getViewIdPrefixes() {
        return new String[]{Piyo.name(), AltPiyo.name(), Cocco.name(), AltCocco.name()};
    }
}
