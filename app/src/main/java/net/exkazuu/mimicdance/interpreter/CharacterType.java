package net.exkazuu.mimicdance.interpreter;

import net.exkazuu.mimicdance.exception.UnKnownCharacterThemeException;

/**
 * Created by exKAZUu on 2015/05/30.
 */
public enum CharacterType {
    Piyo, AltPiyo, Cocco, AltCocco, Bos, AltBos, Bot, AltBot, Boy, AltBoy, Girl, AltGirl;

    private static CharacterType[] getCharacters() {
        switch (CharacterTheme.getCurrentTheme()) {
            case Chicken:
                return new CharacterType[]{Piyo, AltPiyo, Cocco, AltCocco};
            case StickFig:
                return new CharacterType[]{Bos, AltBos, Bot, AltBot};
            case GirlToBoy:
                return new CharacterType[]{Boy, AltBoy, Girl, AltGirl};
            case BoyToGirl:
                return new CharacterType[]{Girl, AltGirl, Boy, AltBoy};
            default:
                throw new UnKnownCharacterThemeException(CharacterTheme.getCurrentTheme());
        }
    }

    public static String[] getViewIdPrefixes() {
        return new String[]{Piyo.name(), AltPiyo.name(), Cocco.name(), AltCocco.name()};
    }

    public static CharacterType getStudent() {
        return getCharacters()[0];
    }

    public static CharacterType getAltStudent() {
        return getCharacters()[1];
    }

    public static CharacterType getTeacher() {
        return getCharacters()[2];
    }

    public static CharacterType getAltTeacher() {
        return getCharacters()[3];
    }

}
