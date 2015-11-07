package net.exkazuu.mimicdance.exception;

import net.exkazuu.mimicdance.interpreter.CharacterTheme;

/**
 * Created by ENIXER on 2015/11/07.
 */
public class UnKnownCharacterThemeException extends RuntimeException {
    public UnKnownCharacterThemeException(CharacterTheme theme) {
        super("Unknown Character Theme: " + theme.name());
    }
}
