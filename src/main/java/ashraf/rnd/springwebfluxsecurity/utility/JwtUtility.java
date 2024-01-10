package ashraf.rnd.springwebfluxsecurity.utility;

import java.util.Date;

public class JwtUtility {
    private JwtUtility() {
    }

    public static final String TOKEN_ISSUER = "webflux-security-app";
    public static final String TOKEN_SCOPE = "scope";
    public static final String TOKEN_SUBJECT = "client-app";
    public static final String TOKEN_CLAIM_USER = "userId";
    public static final String TOKEN_CLAIM_DEVICE = "deviceId";
    public static final String[] TOKEN_SCOPE_ARRAY = new String[]{"authentication", "authorization"};

    public static Date getTokenExpirationTime() {

        return new Date(System.currentTimeMillis() + 60000 * 5);
    }
}
