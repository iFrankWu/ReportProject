package com.tibco.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

public class RSAToolUtil {

    public static final String KEY_ALGORITHM = "RSA";

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 245;

    /**
     * 获取公钥
     *
     * @return
     * @throws Exception
     */
    public static PublicKey getPublicKey() throws Exception {
        String pubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0ZIoF5VgD67t+8rn+PJCzAMeU4JMaJarFXVbvBRlbYvFoHsP5Sp/UITq4pNH5JVke9gyPTg8jaLh7cdcaVOUdfMbYj013cUv7h94GpiUIm0AbNB3hkLB5FAjTFeE4DlpVtCcy8wnY1LuCYjwFPDn2QjC7bLKEsJd3+IYMtWTKzcrDFCx0QJLpA/lOBvgPvCex6+lLWFxKVDkUXEi5t7TuMX9MjIP7EmnI46hfiuomMC6yTydrokVcz69eoYFYCHSfuSoNGjBRFCr3XhiCeTiKoupVOlUP5NG1GBlTnPXMf3dMLU4NWiLzlPvFZz+dHyArNRhL2evneFv5e5eK27/XQIDAQAB";

        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(
                Base64.decodeBase64(pubKey));
        KeyFactory factory;
        factory = KeyFactory.getInstance(KEY_ALGORITHM);
        return factory.generatePublic(x509EncodedKeySpec);
    }

    /**
     * 数据加密
     *
     * @param plainText
     * @return
     */
    public static String RSAEncode(String plainText) {
        byte[] b = plainText.getBytes();
        try {
            int inputLen = b.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, getPublicKey());
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(b, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(b, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            byte[] decryptedData = out.toByteArray();
            out.close();
            return Base64.encodeBase64String(decryptedData);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {

        JSONObject obj = new JSONObject();
        obj.put("card_no", "6666882002017213957");
        obj.put("client_ip", "118.190.66.252");
        obj.put("client_service", "pc_client");
        obj.put("customer_no", "05000036409");
        obj.put("encode", "UTF-8");
        obj.put("sign_type", "MD5");
        obj.put("third_custom", "");
        obj.put("timestamp", "1544068394");
        obj.put("uuid", "fd0f08aa6c904583a91b6d525d167c22");
        obj.put("version", "2.0.0");
        obj.put("version", "中文");

        try {
            String e = RSAEncode(obj.toJSONString());
            System.out.println(e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
