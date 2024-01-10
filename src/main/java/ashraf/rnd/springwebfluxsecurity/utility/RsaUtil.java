package ashraf.rnd.springwebfluxsecurity.utility;

import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class RsaUtil {

    private final Cipher cipher;

    public RsaUtil() throws NoSuchAlgorithmException, NoSuchPaddingException {
        this.cipher = Cipher.getInstance(EncryptionUtility.RSA);
    }

    public String decrypt(String encryptedText, String privateKeyStr) throws GeneralSecurityException {
        var key = getPrivateKey(privateKeyStr);
        cipher.init(Cipher.DECRYPT_MODE, key);

        return new String(cipher.doFinal(Base64.getDecoder().decode(encryptedText)), StandardCharsets.UTF_8);
    }

    public String encrypt(String plainText, String publicKeyStr) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException {
        var key = getPublicKey(publicKeyStr);
        cipher.init(Cipher.ENCRYPT_MODE, key);

        return Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8)));
    }


    public PrivateKey getPrivateKey(final String privateKeyStr) throws GeneralSecurityException {
        if (privateKeyStr.contains(EncryptionUtility.BEGIN_PRIVATE_KEY)) { // PKCS#8 format
            var privateKeyPem = privateKeyStr.replace(EncryptionUtility.BEGIN_PRIVATE_KEY, "")
                    .replace(EncryptionUtility.END_PRIVATE_KEY, "")
                    .replaceAll("\\s", "");

            var pkcs8EncodedKey = Base64.getDecoder().decode(privateKeyPem);
            var factory = KeyFactory.getInstance(EncryptionUtility.RSA);

            return factory.generatePrivate(new PKCS8EncodedKeySpec(pkcs8EncodedKey));

        } else {
            throw new GeneralSecurityException(EncryptionUtility.INVALID_PRIVATE_KEY);
        }
    }

    public PublicKey getPublicKey(final String publicKeyStr) {
        try {
            var privateKeyPem = publicKeyStr.replace(EncryptionUtility.BEGIN_PUBLIC_KEY, "").
                    replace(EncryptionUtility.END_PUBLIC_KEY, "")
                    .replaceAll("\\s", "");

            var pkcs8EncodedKey = Base64.getDecoder().decode(privateKeyPem);
            var spec = new X509EncodedKeySpec(pkcs8EncodedKey);
            var kf = KeyFactory.getInstance(EncryptionUtility.RSA);

            return kf.generatePublic(spec);

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
