package takehome;

/**
 * Enum for declaring languages that can be used in the application.
 */
public enum Language {

    ENGLISH("english"),
    RUSSIAN("russian"),
    UNKNOWN_LANGUAGE("unknown");

    private String lang;

    /**
     * Constructor.
     * @param lang - Human readable name of the language.
     */
    Language(String lang) {
        this.lang = lang;
    }

    public String toString() {
        return this.lang;
    }

    public static Language fromString(String lang) {
        for (Language l : Language.values()) {
            if (l.lang.equalsIgnoreCase(lang)) {
                return l;
            }
        }
        return UNKNOWN_LANGUAGE;
    }
}
