package com.kcwl.framework.sm;

import lombok.Data;

import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 姚华成
 * @date 2018-05-18
 */
@Data
 class SM4Utils {
    private String secretKey = "";

    private String iv = "";

    private boolean hexString = false;

     SM4Utils() {
    }

     String encryptDataECB(String plainText) {
        try {
            SM4_Context ctx = new SM4_Context();
            ctx.isPadding = true;
            ctx.mode = SM4.SM4_ENCRYPT;

            byte[] keyBytes;
            if (hexString) {
                keyBytes = Util.hexStringToBytes(secretKey);
            } else {
                keyBytes = secretKey.getBytes();
            }

            SM4 sm4 = new SM4();
            sm4.sm4SetKeyEnc(ctx, keyBytes);
            byte[] encrypted = sm4.sm4CryptEcb(ctx, plainText.getBytes("GBK"));
            String cipherText = Base64.getEncoder().encodeToString(encrypted);
            if (cipherText != null && cipherText.trim().length() > 0) {
                Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                Matcher m = p.matcher(cipherText);
                cipherText = m.replaceAll("");
            }
            return cipherText;
        } catch (Exception e) {
            return null;
        }
    }

     String decryptDataECB(String cipherText) {
        try {
            SM4_Context ctx = new SM4_Context();
            ctx.isPadding = true;
            ctx.mode = SM4.SM4_DECRYPT;

            byte[] keyBytes;
            if (hexString) {
                keyBytes = Util.hexStringToBytes(secretKey);
            } else {
                keyBytes = secretKey.getBytes();
            }

            SM4 sm4 = new SM4();
            sm4.sm4SetkeyDec(ctx, keyBytes);
            byte[] decrypted = sm4.sm4CryptEcb(ctx, Base64.getDecoder().decode(cipherText));
            return new String(decrypted, "GBK");
        } catch (Exception e) {
            return null;
        }
    }

     String encryptDataCBC(String plainText) {
        try {
            SM4_Context ctx = new SM4_Context();
            ctx.isPadding = true;
            ctx.mode = SM4.SM4_ENCRYPT;

            byte[] keyBytes;
            byte[] ivBytes;
            if (hexString) {
                keyBytes = Util.hexStringToBytes(secretKey);
                ivBytes = Util.hexStringToBytes(iv);
            } else {
                keyBytes = secretKey.getBytes();
                ivBytes = iv.getBytes();
            }

            SM4 sm4 = new SM4();
            sm4.sm4SetKeyEnc(ctx, keyBytes);
            byte[] encrypted = sm4.sm4CryptCbc(ctx, ivBytes, plainText.getBytes("GBK"));
            String cipherText = Base64.getEncoder().encodeToString(encrypted);
            if (cipherText != null && cipherText.trim().length() > 0) {
                Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                Matcher m = p.matcher(cipherText);
                cipherText = m.replaceAll("");
            }
            return cipherText;
        } catch (Exception e) {
            return null;
        }
    }

     String decryptDataCBC(String cipherText) {
        try {
            SM4_Context ctx = new SM4_Context();
            ctx.isPadding = true;
            ctx.mode = SM4.SM4_DECRYPT;

            byte[] keyBytes;
            byte[] ivBytes;
            if (hexString) {
                keyBytes = Util.hexStringToBytes(secretKey);
                ivBytes = Util.hexStringToBytes(iv);
            } else {
                keyBytes = secretKey.getBytes();
                ivBytes = iv.getBytes();
            }

            SM4 sm4 = new SM4();
            sm4.sm4SetkeyDec(ctx, keyBytes);
            byte[] decrypted = sm4.sm4CryptCbc(ctx, ivBytes, Base64.getDecoder().decode(cipherText));
            return new String(decrypted, "GBK");
        } catch (Exception e) {
            return null;
        }
    }
}
