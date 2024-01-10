package ashraf.rnd.springwebfluxsecurity.utility;

public class EncryptionUtility {

    private EncryptionUtility() {
    }

    // PKCS#8 format
    public static final String BEGIN_PRIVATE_KEY = "-----BEGIN PRIVATE KEY-----";
    public static final String END_PRIVATE_KEY = "-----END PRIVATE KEY-----";
    // PKCS#1 format

    public static final String BEGIN_RSA_PRIVATE_START = "-----BEGIN RSA PRIVATE KEY-----";
    public static final String END_RSA_PRIVATE_END = "-----END RSA PRIVATE KEY-----";

    // Public Key Format
    public static final String BEGIN_PUBLIC_KEY = "-----BEGIN PUBLIC KEY-----";
    public static final String END_PUBLIC_KEY = "-----END PUBLIC KEY-----";

    // Error Message
    public static final String INVALID_PUBLIC_KEY = "Invalid Public Key!!!";
    public static final String INVALID_PRIVATE_KEY = "Invalid Private Key!!!";
    public static final String COULD_NOT_PARSE_PKS1 = "Could not parse a PKCS1 private key.";
    public static final String UTF_8 = "UTF-8";
    public static final String RSA = "RSA";
    public static final String AES_INSTANCE = "AES/CBC/PKCS5PADDING";
    public static final String AES = "AES";

    public static final String PUBLIC_KEY = "-----BEGIN PUBLIC KEY-----\n" +
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtWlyYpXAgcEp7Ih3UODA\n" +
            "LwkS3EH4U6J1XDbDEk3LqHruPnE/gSB/LW9iNERfwxkaEErcpGw3pb6tOMWs393h\n" +
            "roBRUhS5OGtwE7a8UU9cfMFv5Xlaa/EKm+XOCaG5KlAU61wLUN1ls3NzPOpDSTM1\n" +
            "w2D6tKKHvWDa+V4/yZr/U6ouWwtMTGa5ibLhmSSzSjPa1Atqv+dyXXjU9nQAP/Gd\n" +
            "cgATjoRAqynCHWwEWDBJQV12xp00wS9QcV2lLkRJVcx4UFhYJU68Fjhll4fefK24\n" +
            "HMtUyx6OHtUlTmVWDZE2x+2h0tYHmQwn0TFZQmwRlR77DPGAuvjjrgcvfM9cXRxg\n" +
            "eQIDAQAB\n" +
            "-----END PUBLIC KEY-----";

    public static final String PRIVATE_KEY = "-----BEGIN PRIVATE KEY-----\n" +
            "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC1aXJilcCBwSns\n" +
            "iHdQ4MAvCRLcQfhTonVcNsMSTcuoeu4+cT+BIH8tb2I0RF/DGRoQStykbDelvq04\n" +
            "xazf3eGugFFSFLk4a3ATtrxRT1x8wW/leVpr8Qqb5c4JobkqUBTrXAtQ3WWzc3M8\n" +
            "6kNJMzXDYPq0ooe9YNr5Xj/Jmv9Tqi5bC0xMZrmJsuGZJLNKM9rUC2q/53JdeNT2\n" +
            "dAA/8Z1yABOOhECrKcIdbARYMElBXXbGnTTBL1BxXaUuRElVzHhQWFglTrwWOGWX\n" +
            "h958rbgcy1TLHo4e1SVOZVYNkTbH7aHS1geZDCfRMVlCbBGVHvsM8YC6+OOuBy98\n" +
            "z1xdHGB5AgMBAAECggEAI5xXJpiIYUAqi2Kvb0orF6CTmfZ+OWao3IjoaoGocoJt\n" +
            "HYdMBLw9Es1sTN/GH41gIyYa4VDX7Jp9NOmC2bAbws4wCGmGgVd/uPL0TQ7D1D6B\n" +
            "iV10vT8kCr3F3/il2TM+pAPduco4Ek2dp63Btw8NJPT2ybok5K0sVuqkIV5idAjd\n" +
            "tKw1hSvzZ0bKcg+GvfAnGBNq00lL/IoVdWZlQjOnmAkU21S9VjknSsem0A6RUXSS\n" +
            "R+7w44+k7tjkvah/Yd64rgfmarQjBoWegwTIebBAaztci1WHt/OUivRgWd0W3IUg\n" +
            "ciaGw5Nj2Fph9GV0gKkUtqmwP7pJ5H1GmCB6AofGrQKBgQDYX7r4WVZWtgfuEcFr\n" +
            "NeWLSnBKgKMYGn2Iph8YFjLcbLd746r553MMlZhzqyVW6vL9o25F2geZcB9u4cWj\n" +
            "a2CsG6v1JRf5WTqYks1+cIXCiQAQASfdycNZFe5JX1DlYnRGSi3rpbhBrLfv2n37\n" +
            "jLhAVzimjk7ctsLc5mAQ5fYNhQKBgQDWopbcvWk3Fqo8R2CPLOYK4VjNrkSe6ukm\n" +
            "Ys+kkWwi0S8BCT6KcqdpQ0EiT2EDhSqoAp5eOYru3hB/IR1I6e+7+QseWd1E0Sqd\n" +
            "DagurYPqvhHObcIxe/3dGr30aeNkvchWtSrFGk/8QXkXmqzVSwH3cVhNaw2FgVXI\n" +
            "WVABXXlPZQKBgCgDzDgRjX4Lu/uzHOoO1Zyk749XqtHSVFTpI+b4c/9/u48V05DT\n" +
            "1NsG72K0y/r/CGEP0FzqrW0a5zVCW6KVQROTKoHzkjNy5PtCau5vMM2BOJm9HPpP\n" +
            "TekzvRWkovlI8+fr4AIsa575qiqZZaMXqxt7BecHel4nDnJ7yFhXruFhAoGBALZX\n" +
            "6Ozdac6oTbXqOclJLqJdsfEDNT8BeVPoLFm2RfUKW/F4Zg5+u2U9As/gOh4gew3D\n" +
            "M8H/Bt5KAB/RBS0VuVqriFufotC9FoPYv2HwfZrnIP+L1afum8zE1E4Zy1qhtNqd\n" +
            "DRnocb1v7i00ddK654/NVmRnJ1SdfUA5SDPwsofVAoGANoXWDyS74tdi2qxSoxra\n" +
            "7vOkEmas9+Y5GgqCfbZWvVCk5lDp6Wgf3Xz+tQY8nLfxFbz8mzUBgZhT/bBBDGX1\n" +
            "u8AAkj+FkyJT44s+jiNXvoC2RYKQYoE4FNZ9rFHkLZNyazVwrL4G70M7tFmBypEK\n" +
            "AETPn7yxqvC+je99LpBvj6M=\n" +
            "-----END PRIVATE KEY-----";

}
