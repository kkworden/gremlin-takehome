package takehome;

import org.apache.commons.cli.*;
import org.apache.http.impl.client.HttpClientBuilder;

import java.util.List;
import java.util.Random;

/**
 * Our application's entrypoint.
 */
public class App {

    private static final String LANGUAGE_OPT = "language";
    private static final List<Language> SUPPORTED_LANGUAGES = List.of(Language.ENGLISH, Language.RUSSIAN);

    public static void main(String[] args) {
        Options options = new Options();
        Option languageOpt = Option.builder()
            .longOpt(LANGUAGE_OPT)  // The option name
            .argName("language")  // The option argument display name
            .hasArg()  // User needs to provide an input to this option
            .desc("the desired language. One of: " + SUPPORTED_LANGUAGES)
            .required() // Require this option
            .build();
        options.addOption(languageOpt);

        // Configure dependencies needed
        Random randomGen = new Random();
        ForismaticDriver forismaticDriver = new ForismaticDriver(HttpClientBuilder.create().build());
        QuoteRetriever retriever = new QuoteRetriever(forismaticDriver, randomGen);

        // Obtain a random quote in the language we want to use. Display help if we get weird input.
        try {
            Language desiredLanguage = getLanguageFromArgs(args, options);
            String randomQuote = retriever.getRandomQuote(desiredLanguage);

            System.out.println("Here is your quote:");
            System.out.println(randomQuote);
        } catch (ParseException ex) {
            System.err.println(ex.getMessage());
            printHelp(options);
        }
    }

    /**
     * Gets the desired language from command-line arguments.
     * @param args - The command line arguments.
     * @param options - The {@link Options} we wish to use for this application.
     * @return The desired language, as a {@link Language}.
     * @throws ParseException If the application was incorrectly invoked.
     */
    private static Language getLanguageFromArgs(String[] args, Options options) throws ParseException {
        // Parse the command line arguments
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        // Convert the desired language into an enum, will be null if not recognized
        Language desiredLanguage = Language.fromString(cmd.getOptionValue(LANGUAGE_OPT));

        // Ensure we support the desired language. If not, raise an error
        if (desiredLanguage == null || !SUPPORTED_LANGUAGES.contains(desiredLanguage)) {
            String errorMessage = String.format("%s must be one of: %s", LANGUAGE_OPT, SUPPORTED_LANGUAGES);
            throw new ParseException(errorMessage);
        }

        return desiredLanguage;
    }

    /**
     * Prints a help prompt.
     * @param options - The {@link Options} used for this application.
     */
    private static void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(" ", options);
    }
}
