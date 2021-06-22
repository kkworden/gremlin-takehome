package takehome;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Random;

/**
 * Class with responsibility of retrieving quotes.
 */
public class QuoteRetriever {

    private final ForismaticDriver forismaticDriver;
    private final Random randomGen;

    /**
     * Constructor.
     * @param forismaticDriver - A driver used to get the actual quotes.
     * @param randomGen - A random number generator.
     */
    public QuoteRetriever(ForismaticDriver forismaticDriver, Random randomGen) {
        this.forismaticDriver = forismaticDriver;
        this.randomGen = randomGen;
    }

    /**
     * Gets a quote in the desired language.
     * @param language - The language to use for obtaining quotes.
     * @return The quote, in the language specified.
     * @throws IllegalArgumentException if the specified language is not supported.
     */
    public String getRandomQuote(Language language) throws IllegalArgumentException {
        int key = randomGen.nextInt(ForismaticConstants.KEY_BOUND);

        try {
            // Invoke the driver properly for the desired language.
            switch (language) {
                case ENGLISH:
                    return this.forismaticDriver.getQuote(ForismaticConstants.ENGLISH_CODENAME, key);
                case RUSSIAN:
                    return this.forismaticDriver.getQuote(ForismaticConstants.RUSSIAN_CODENAME, key);
                default:
                    throw new IllegalArgumentException("Language not supported: " + language);
            }
        } catch (IOException | URISyntaxException ex) {
            // Lazy exception handling if something went wrong
            ex.printStackTrace();
        }

        return null;
    }
}
