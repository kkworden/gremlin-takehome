package takehome;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import static java.net.HttpURLConnection.HTTP_OK;

/**
 * Class with responsibility of retrieving quotes from the http://forismatic.com/en/api/ endpoint.
 */
public class ForismaticDriver {

    private static final String FORISMATIC_API_ENDPOINT = "http://api.forismatic.com/api/1.0/";

    private final CloseableHttpClient closeableHttpClient;

    /**
     * Constructor.
     * @param closeableHttpClient - Used to make the actual API call to Forismatic.
     */
    public ForismaticDriver(CloseableHttpClient closeableHttpClient) {
        this.closeableHttpClient = closeableHttpClient;
    }

    /**
     * Gets a random quote from the Forismatic endpoint.
     * @param languageCodename - The 'lang' codename to use for the quote.
     * @param key - A random integer between 0-999999 that helps Forismatic select a quote.
     * @return A random quote in the specified language.
     * @throws IOException If an IOException occurred.
     * @throws URISyntaxException If the URI was ill-formatted.
     */
    public String getQuote(String languageCodename, int key) throws IOException, URISyntaxException {
        URI uri = new URIBuilder(FORISMATIC_API_ENDPOINT)
            .addParameter("method", "getQuote")
            .addParameter("format", "text")
            .addParameter("key", Integer.toString(key))
            .addParameter("lang", languageCodename)
            .build();

        HttpGet getRequest = new HttpGet(uri);

        try (CloseableHttpResponse response = this.closeableHttpClient.execute(getRequest)) {
            // Check the status code
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HTTP_OK) {
                throw new RuntimeException("The Forismatic API returned HTTP " + statusCode);
            }

            // If everything is ok, return the response body
            return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        }
    }
}
