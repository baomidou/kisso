package com.baomidou.kisso.common;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.pkcs.RSAPrivateKey;
import org.bouncycastle.asn1.pkcs.RSAPublicKey;
import org.springframework.security.jwt.codec.Codecs;

/**
 * Created by jobob on 2017/7/19.
 */
public class RsaKeyHelper {

    private static String BEGIN = "-----BEGIN";
    private static Pattern PEM_DATA = Pattern.compile("-----BEGIN (.*)-----(.*)-----END (.*)-----", 32);
    private static final Pattern SSH_PUB_KEY = Pattern.compile("ssh-(rsa|dsa) ([A-Za-z0-9/+]+=*) (.*)");

    RsaKeyHelper() {
    }

    public static KeyPair parseKeyPair(String pemData) {
        Matcher m = PEM_DATA.matcher(pemData.trim());
        if (!m.matches()) {
            throw new IllegalArgumentException("String is not PEM encoded data");
        } else {
            String type = m.group(1);
            byte[] content = Codecs.b64Decode(Codecs.utf8Encode(m.group(2)));
            PrivateKey privateKey = null;

            try {
                KeyFactory fact = KeyFactory.getInstance("RSA");
                PublicKey publicKey;
                ASN1Sequence seq;
                RSAPublicKeySpec pubSpec;
                if (type.equals("RSA PRIVATE KEY")) {
                    seq = ASN1Sequence.getInstance(content);
                    if (seq.size() != 9) {
                        throw new IllegalArgumentException("Invalid RSA Private Key ASN1 sequence.");
                    }

                    RSAPrivateKey key = RSAPrivateKey.getInstance(seq);
                    pubSpec = new RSAPublicKeySpec(key.getModulus(), key.getPublicExponent());
                    RSAPrivateCrtKeySpec privSpec = new RSAPrivateCrtKeySpec(key.getModulus(), key.getPublicExponent(), key.getPrivateExponent(), key.getPrime1(), key.getPrime2(), key.getExponent1(), key.getExponent2(), key.getCoefficient());
                    publicKey = fact.generatePublic(pubSpec);
                    privateKey = fact.generatePrivate(privSpec);
                } else if (type.equals("PUBLIC KEY")) {
                    KeySpec keySpec = new X509EncodedKeySpec(content);
                    publicKey = fact.generatePublic(keySpec);
                } else {
                    if (!type.equals("RSA PUBLIC KEY")) {
                        throw new IllegalArgumentException(type + " is not a supported format");
                    }

                    seq = ASN1Sequence.getInstance(content);
                    RSAPublicKey key = RSAPublicKey.getInstance(seq);
                    pubSpec = new RSAPublicKeySpec(key.getModulus(), key.getPublicExponent());
                    publicKey = fact.generatePublic(pubSpec);
                }

                return new KeyPair(publicKey, privateKey);
            } catch (InvalidKeySpecException var11) {
                throw new RuntimeException(var11);
            } catch (NoSuchAlgorithmException var12) {
                throw new IllegalStateException(var12);
            }
        }
    }

    public static java.security.interfaces.RSAPublicKey parsePublicKey(String key) {
        Matcher m = SSH_PUB_KEY.matcher(key);
        if (m.matches()) {
            String alg = m.group(1);
            String encKey = m.group(2);
            if (!"rsa".equalsIgnoreCase(alg)) {
                throw new IllegalArgumentException("Only RSA is currently supported, but algorithm was " + alg);
            } else {
                return parseSSHPublicKey(encKey);
            }
        } else if (!key.startsWith(BEGIN)) {
            return parseSSHPublicKey(key);
        } else {
            KeyPair kp = parseKeyPair(key);
            if (kp.getPublic() == null) {
                throw new IllegalArgumentException("Key data does not contain a public key");
            } else {
                return (java.security.interfaces.RSAPublicKey) kp.getPublic();
            }
        }
    }

    private static java.security.interfaces.RSAPublicKey parseSSHPublicKey(String encKey) {
        byte[] PREFIX = new byte[]{0, 0, 0, 7, 115, 115, 104, 45, 114, 115, 97};
        ByteArrayInputStream in = new ByteArrayInputStream(Codecs.b64Decode(Codecs.utf8Encode(encKey)));
        byte[] prefix = new byte[11];

        try {
            if (in.read(prefix) == 11 && Arrays.equals(PREFIX, prefix)) {
                BigInteger e = new BigInteger(readBigInteger(in));
                BigInteger n = new BigInteger(readBigInteger(in));
                return createPublicKey(n, e);
            } else {
                throw new IllegalArgumentException("SSH key prefix not found");
            }
        } catch (IOException var6) {
            throw new RuntimeException(var6);
        }
    }

    public static java.security.interfaces.RSAPublicKey createPublicKey(BigInteger n, BigInteger e) {
        try {
            return (java.security.interfaces.RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new RSAPublicKeySpec(n, e));
        } catch (Exception var3) {
            throw new RuntimeException(var3);
        }
    }

    private static byte[] readBigInteger(ByteArrayInputStream in) throws IOException {
        byte[] b = new byte[4];
        if (in.read(b) != 4) {
            throw new IOException("Expected length data as 4 bytes");
        } else {
            int l = b[0] << 24 | b[1] << 16 | b[2] << 8 | b[3];
            b = new byte[l];
            if (in.read(b) != l) {
                throw new IOException("Expected " + l + " key bytes");
            } else {
                return b;
            }
        }
    }
}
