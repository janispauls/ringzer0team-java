import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Challenge13 {

    private final String username;
    private final String password;
    private final String challengeUrl = "http://ringzer0team.com/challenges/13";
    private final String loginUrl = "http://ringzer0team.com/login";
    private String solution;
    private String flag;
    private CloseableHttpClient client;

    public Challenge13(String username, String password) {
        this.username = username;
        this.password = password;
    }

    private static String getLongestString(String[] parts) {
        int maxLength = 0;
        String longest = null;
        for (String s : parts) {
            if (s.length() > maxLength) {
                maxLength = s.length();
                longest = s;
            }
        }
        return longest;
    }

    public void solve() throws IOException, NoSuchAlgorithmException {
        client = buildHttpClient(loginUrl, username, password);
        solution = solveChallenge(client);
        flag = submitSolutionAndGetFlag(solution);
    }

    private String solveChallenge(CloseableHttpClient client) throws IOException, NoSuchAlgorithmException {
        HttpGet challengeRequest = new HttpGet(challengeUrl);
        CloseableHttpResponse response = client.execute(challengeRequest);

        Document doc = Jsoup.parse(IOUtils.toString(response.getEntity().getContent(), "UTF-8"));
        EntityUtils.consume(response.getEntity());

        Element challengeWrapper = doc.select(".challenge-wrapper").first();
        Element message = challengeWrapper.select(".message").first();
        String[] parts = message.text().split("\\s+");

        String data = getLongestString(parts);

        MessageDigest sha512 = MessageDigest.getInstance("SHA-512");
        sha512.update(data.getBytes());
        return String.valueOf(Hex.encodeHex(sha512.digest()));
    }

    public String getSolution() {
        return solution;
    }

    public String getFlag() {
        return flag;
    }

    private String submitSolutionAndGetFlag(String solution) throws IOException {
        String solutionUrl = challengeUrl + "/" + solution;
        HttpGet solutionRequest = new HttpGet(solutionUrl);
        CloseableHttpResponse solutionResponse = client.execute(solutionRequest);
        Document solutionDoc = Jsoup.parse(IOUtils.toString(solutionResponse.getEntity().getContent(), "UTF-8"));
        return solutionDoc.select(".alert-info").first().text();
    }

    private CloseableHttpClient buildHttpClient(String loginUrl, String username, String password) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        return new HttpClientAuthenticator(client, loginUrl).authenticate(username, password);
    }
}
