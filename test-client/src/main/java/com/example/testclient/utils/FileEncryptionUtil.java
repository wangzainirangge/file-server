package com.example.testclient.utils;

/**
 *加密整合工具类
 */
public class FileEncryptionUtil {

    //RSA私钥
    public static final String RSA_PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIx7G/oTw/YxWOLtlREWByzs2HVXNUiyVC1IrMJbqEQRTcl2N4IwKRuK0hsY3QHSyDbLgFJy1J70vvYosu9jnYL3Fi6AV+WdssU4RCRhbVUWoxv9M3G5Fe9EkP5PkU6kgdPlEuR7oL/bT/5ft1hmyCJUy4VAnLw4a0MwFUpueF9pAgMBAAECgYEAiZvKcvRLn+0E2f3KaJtAWiSDgKX1fwmYZWdsO1LXiB2/KdpQU4njqqQYXgzD6RZVoz9CqwDq/+5U5QJHDTJyRjJxAhoVBPN+CSi1sng8CW7VH6hvEHXKuYbRb0bbVfs6fNhYaOF4Jg1F0mKJry95bm2fDR8MAvmVAwFC6QsC2gECQQDWi4bECOijzs8BArcJyrvzWzB5CtjSlmfJPuJmQOwg/oC4PYcZjzpLXPJ0VZPPgiVcYU7St/O6ycbIUed51UApAkEAp5//1QEJN2a0aVJxKKprYRKP8J0lnaZQBK2XT3UHVxFjJ5Sily1GMxyWixcMCtbVrt8Q4mshKdY3oMCM0KkNQQJAbRxUk4o8VmKtIERzNkWmxKMRyd1cW+0mJ1EV7w8Bh94nNIwGE1emPAGvRL7pB1WXDp5magAnsk1ADUyqPmnX0QJAZv/LpcjjLuWvCeCco85D+Pv88m69hGeDV7yK80oH3ppSgSm11or9Tb7NSl45ChgEaVWr/FSzpg95Uw+99yFJgQJAMAFHwOQVfm39HLkFS5BcvsOt6u4nCCi3hOHmWh4G082djVc44EkKPRaPrxeqF5j22w+/zMaP2Vgmk8epWqpqhg==";

    /**
     * 通过RSA私钥加密
     * @param Str   要加密的字符串
     * @return      加密后的字符串
     */
    public static String StrEncryption(String Str){
        //RSA公钥加密
        String strEncryption = RSAUtil.privateEncrypt(Str,RSA_PRIVATE_KEY);
        return  strEncryption;
    }

    /**
     * 通过RSA私钥对加密后的数据信封解密，再通过解密后的AES密钥对加密文件解密
     * @param aesFile
     * @param keyCipher
     * @return
     */
    public static String fileDecrypt(String aesFile,String keyCipher){
        //调用RSA工具类对AES密钥解密
        String key = RSAUtil.decrypt(keyCipher,RSA_PRIVATE_KEY);
        //通过解密后的AES密钥对加密文件解密
        String fileContent = AESUtil.aesDecrypt(key,aesFile);
        return fileContent;
    }

}
