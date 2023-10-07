package pl.lodz.p.it.ssbd2023.ssbd03.util;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.Locale;
import java.util.ResourceBundle;

@ApplicationScoped
public class Internationalization {

    public String getMessage(String message, String language) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("translation", Locale.forLanguageTag(language));
        return resourceBundle.getString(message);
    }
}
