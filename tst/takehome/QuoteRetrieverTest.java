package takehome;

import org.apache.commons.codec.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Random;

import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class QuoteRetrieverTest {

    private static final String QUOTE = "QUOTE";

    // Unit under test
    QuoteRetriever quoteRetriever;

    @Mock
    ForismaticDriver forismaticDriver;

    @Mock
    Random randomGen;

    @Captor
    ArgumentCaptor<String> languageCodenameCaptor;

    @BeforeEach
    void setup() throws Exception {
        MockitoAnnotations.initMocks(this);

        quoteRetriever = new QuoteRetriever(forismaticDriver, randomGen);
    }

    /**
     * Test the happy path for English.
     */
    @Test
    void test_getRandomQuote_english() throws Exception {
        when(forismaticDriver.getQuote(languageCodenameCaptor.capture(), any(Integer.class))).thenReturn(QUOTE);

        // We should get our quote
        assertEquals(QUOTE, quoteRetriever.getRandomQuote(Language.ENGLISH));

        // Assert we invoked Forismatic with the correct language codename
        assertEquals(ForismaticConstants.ENGLISH_CODENAME, languageCodenameCaptor.getValue());
    }

    /**
     * Test the happy path for Russian.
     */
    @Test
    void test_getRandomQuote_russian() throws Exception {
        when(forismaticDriver.getQuote(languageCodenameCaptor.capture(), any(Integer.class))).thenReturn(QUOTE);

        // We should get our quote
        assertEquals(QUOTE, quoteRetriever.getRandomQuote(Language.RUSSIAN));

        // Assert we invoked Forismatic with the correct language codename
        assertEquals(ForismaticConstants.RUSSIAN_CODENAME, languageCodenameCaptor.getValue());
    }

    /**
     * Test when our quote driver fails.
     */
    @Test
    void test_getRandomQuote_driver_failure() throws Exception {
        when(forismaticDriver.getQuote(any(String.class), any(Integer.class)))
                .thenThrow(RuntimeException.class);

        // The Runtime exception will bubble up to our QuoteRetriever.
        assertThrows(RuntimeException.class, () ->
                quoteRetriever.getRandomQuote(Language.ENGLISH));
    }

    /**
     * Test when we get a bad input language.
     */
    @Test
    void test_getRandomQuote_bad_language() {
        // We only support English and Russian for now
        assertThrows(IllegalArgumentException.class, () ->
                quoteRetriever.getRandomQuote(Language.UNKNOWN_LANGUAGE));
    }
}
