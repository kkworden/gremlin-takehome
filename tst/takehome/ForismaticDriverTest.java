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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class ForismaticDriverTest {

    private static final String RESPONSE_BODY = "RESPONSE_BODY";

    // Unit under test
    ForismaticDriver forismaticDriver;

    @Mock
    CloseableHttpClient httpClient;

    @Mock
    CloseableHttpResponse response;

    @Mock
    StatusLine statusLine; // The status line of the response

    @Mock
    HttpEntity entity; // The entity of the response

    @BeforeEach
    void setup() throws Exception {
        MockitoAnnotations.initMocks(this);

        forismaticDriver = new ForismaticDriver(httpClient);

        // Mock the HTTP client so we can simulate HTTP response scenarios
        when(httpClient.execute(any(HttpGet.class))).thenReturn(response);

        // Mock the response objects
        when(response.getStatusLine()).thenReturn(statusLine);
        when(response.getEntity()).thenReturn(entity);

        // Mock the response's entity so we can set the response body
        when(entity.getContent()).thenReturn(IOUtils.toInputStream(RESPONSE_BODY, Charsets.UTF_8));
    }

    /**
     * Test the happy path.
     */
    @Test
    void test_getQuote() throws Exception {
        when(statusLine.getStatusCode()).thenReturn(HTTP_OK);

        assertEquals(RESPONSE_BODY, forismaticDriver.getQuote(ForismaticConstants.ENGLISH_CODENAME, 123));
        verify(httpClient, times(1)).execute(any(HttpGet.class));
    }

    /**
     * Test when we get a non-200 HTTP response.
     */
    @Test
    void test_getQuote_bad_response() throws Exception {
        when(statusLine.getStatusCode()).thenReturn(HTTP_INTERNAL_ERROR);

        assertThrows(RuntimeException.class, () ->
            forismaticDriver.getQuote(ForismaticConstants.ENGLISH_CODENAME, 123));
        verify(httpClient, times(1)).execute(any(HttpGet.class));
    }
}
