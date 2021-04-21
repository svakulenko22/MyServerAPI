package com.myserver.api.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AESHelper {

    private static final SecretKeySpec SECRET_KEY_SPEC = new SecretKeySpec(Constants.KEY.getBytes(), "AES");
    private static final IvParameterSpec IV_PARAMETER_SPEC = new IvParameterSpec(Constants.IV.getBytes());

    public static String encode(final String text) {
        try {
            final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, SECRET_KEY_SPEC, IV_PARAMETER_SPEC);

            return Base64.getEncoder().encodeToString(cipher.doFinal(text.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static String decode(final String text) {
        try {
            final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, SECRET_KEY_SPEC, IV_PARAMETER_SPEC);

            final byte[] decodeText = Base64.getDecoder().decode(text.getBytes(StandardCharsets.UTF_8));
            return new String(cipher.doFinal(decodeText));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
