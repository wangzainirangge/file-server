package com.start.demo.utils;

import java.util.HashMap;

/**
 *加密工具类
 */
public class FileEncryptionUtil {

    //RSA公钥
    public static final String RSA_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCMexv6E8P2MVji7ZURFgcs7Nh1VzVIslQtSKzCW6hEEU3JdjeCMCkbitIbGN0B0sg2y4BSctSe9L72KLLvY52C9xYugFflnbLFOEQkYW1VFqMb/TNxuRXvRJD+T5FOpIHT5RLke6C/20/+X7dYZsgiVMuFQJy8OGtDMBVKbnhfaQIDAQAB";


    /**
     * 通过AES对文件内容加密，然后通过RSA公钥对AES密钥进行加密
     * @param fileContent 文件内容
     * @return 加密后的文件内容和加密后的密钥
     */
    public static HashMap<String,String> fileEncryption(String fileContent){
        //AES对文件进行加密
        HashMap<String, String> hashMap = AESUtil.encryptString(fileContent);
        //AES加密后的文件
        String aesFile = hashMap.get("aesFile");
        //AES密钥
        String key = hashMap.get("key");
        //RSA公钥对AES密钥加密
        String keyCipher = RSAUtil.encrypt(key,RSA_PUBLIC_KEY);
        HashMap<String,String> resultMap = new HashMap<>();
        resultMap.put("aesFile",aesFile);
        resultMap.put("keyCipher",keyCipher);
        return resultMap;
    }

    /**
     * 通过RSA公钥对字符串解密
     * @param Str   要解密的字符串
     * @return  解密后的字符串
     */
    public static String StrDecrypt(String Str){
        String strDecrypt = RSAUtil.publicDecrypt(Str,RSA_PUBLIC_KEY);
        return strDecrypt;
    }

}
