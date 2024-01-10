package ashraf.rnd.springwebfluxsecurity.utility;

import com.auth0.jwt.algorithms.Algorithm;

import java.security.GeneralSecurityException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class JwtAlgorithmManager {

    private String rsaPublicKey;
    private String rsaPrivateKey;

    public JwtAlgorithmManager(String rsaPublicKey, String rsaPrivateKey) {
        this.rsaPublicKey = rsaPublicKey;
        this.rsaPrivateKey = rsaPrivateKey;
    }

    public Algorithm getTokenAlgorithm() {
        try {
            RsaUtil rsaUtil = new RsaUtil();
            RSAPublicKey publicKey = null;
            if (this.rsaPublicKey != null) {
                publicKey = (RSAPublicKey) rsaUtil.getPublicKey(this.rsaPublicKey);
            }
            RSAPrivateKey privateKey = null;
            if (this.rsaPrivateKey != null) {
                privateKey = (RSAPrivateKey) rsaUtil.getPrivateKey(this.rsaPrivateKey);
            }
            return Algorithm.RSA512(publicKey, privateKey);
        } catch (GeneralSecurityException ex) {
            throw new RuntimeException(ex);
        }
    }
}
