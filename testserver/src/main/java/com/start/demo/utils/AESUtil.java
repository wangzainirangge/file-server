package com.start.demo.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.UUID;

/**
 * AES加密算法工具类
 */
public class AESUtil {

    //密钥 (需要前端和后端保持一致)十六位作为密钥
    private static String KEY = null;

    //密钥偏移量 (需要前端和后端保持一致)十六位作为密钥偏移量
    private static final String IV = "ihaierForTodo_Iv";

    //算法
    private static final String ALGORITHMSTR = "AES/CBC/PKCS5Padding";

    /**
     * base 64 decode
     * @param base64Code 待解码的base 64 code
     * @return 解码后的byte[]
     * @throws Exception
     */
    public static byte[] base64Decode(String base64Code) throws Exception{
        /**sun.misc.BASE64Decoder是java内部类，有时候会报错，
         * 用org.apache.commons.codec.binary.Base64替代，效果一样。
         */
        //Base64 base64 = new Base64();
        //byte[] bytes = base64.decodeBase64(new String(base64Code).getBytes());
        //new BASE64Decoder().decodeBuffer(base64Code);
        return StringUtils.isEmpty(base64Code) ? null : new Base64().decodeBase64(new String(base64Code).getBytes());
    }

    /**
     * AES解密
     * @param encryptBytes 待解密的byte[]
     * @return 解密后的String
     * @throws Exception
     */
    public static String aesDecryptByBytes(byte[] encryptBytes) throws Exception {

        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);

        byte[] temp = IV.getBytes("UTF-8");
        IvParameterSpec iv = new IvParameterSpec(temp);

        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(KEY.getBytes(), "AES"), iv);
        byte[] decryptBytes = cipher.doFinal(encryptBytes);
        return new String(decryptBytes);
    }

    /**
     * AES加密
     * @param str
     * @return
     */
    public static HashMap<String, String> encryptString(String str) {
        KEY = UUID.randomUUID().toString().replaceAll("-","").substring(16);
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("key",KEY);
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
            IvParameterSpec iv = new IvParameterSpec(IV.getBytes(StandardCharsets.US_ASCII));
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(KEY.getBytes(StandardCharsets.US_ASCII), "AES"), iv);
            byte[] encryptBytes = cipher.doFinal(str.getBytes(StandardCharsets.UTF_8));
            hashMap.put("aesFile",new String(Base64.encodeBase64(encryptBytes),StandardCharsets.US_ASCII));
            //return Base64.getEncoder().encodeToString(encryptBytes);  //java.util.Base64
            return hashMap;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    /**
     * 将base 64 code AES解密
     * @param key   密钥
     * @param aesFile 待解密的文件
     * @return 解密后的string
     */
    public static String aesDecrypt(String key,String aesFile)  {
        KEY = key;
        String encryptStr = aesFile;
        String result = "";
        try {
            result =  StringUtils.isEmpty(encryptStr) ? null : aesDecryptByBytes(base64Decode(encryptStr));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }



}
