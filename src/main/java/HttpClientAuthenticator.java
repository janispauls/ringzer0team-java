import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HttpClientAuthenticator {

    private final CloseableHttpClient client;
    private final String loginUrl;

    public HttpClientAuthenticator(CloseableHttpClient client, String loginUrl) {
        this.client = client;
        this.loginUrl = loginUrl;
    }

    public CloseableHttpClient authenticate(String username, String password) throws IOException {
        List<NameValuePair> formParams = new ArrayList<NameValuePair>();
        formParams.add(new BasicNameValuePair("username", username));
        formParams.add(new BasicNameValuePair("password", password));

        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formParams, "UTF-8");
        HttpPost request = new HttpPost(loginUrl);
        request.setEntity(entity);
        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");

        CloseableHttpResponse response = client.execute(request);
        EntityUtils.consume(response.getEntity());
        return client;
    }
}
