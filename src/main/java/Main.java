import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class Main {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        String username = args[0];
        String password = args[1];

        Challenge13 challenge = new Challenge13(username, password);
        challenge.solve();

        System.out.println(challenge.getSolution());
        System.out.println(challenge.getFlag());
    }
}
