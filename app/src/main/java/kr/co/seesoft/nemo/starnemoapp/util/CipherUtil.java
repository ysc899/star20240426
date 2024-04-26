package kr.co.seesoft.nemo.starnemoapp.util;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 암호화 유틸
 */
public class CipherUtil {


    private static final String CIPHER_KEY = "STARAESKEY";

    /**
     * @param text 텍스트
     * @return 복호화된 문장
     * @throws Exception
     */
    public static String Decrypt(String text) {

        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            byte[] keyBytes = new byte[16];

            byte[] b = CIPHER_KEY.getBytes("UTF-8");

            int len = b.length;

            if (len > keyBytes.length) len = keyBytes.length;

            System.arraycopy(b, 0, keyBytes, 0, len);

            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

            IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);

            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);


            byte[] results = cipher.doFinal(Base64.getDecoder().decode(text));

            return new String(results, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return text;
        }

    }


    /**
     * @param text 평문
     * @return 암호 문
     * @throws Exception
     */
    public static String Encrypt(String text) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            byte[] keyBytes = new byte[16];

            byte[] b = CIPHER_KEY.getBytes("UTF-8");

            int len = b.length;

            if (len > keyBytes.length) len = keyBytes.length;

            System.arraycopy(b, 0, keyBytes, 0, len);

            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

            IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);

            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

            byte[] results = cipher.doFinal(text.getBytes("UTF-8"));

            return Base64.getEncoder().encodeToString(results);
        } catch (Exception e) {
            e.printStackTrace();
            return text;
        }
    }


}
