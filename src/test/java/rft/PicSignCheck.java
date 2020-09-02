package rft;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class PicSignCheck {
    
    static String picPath = "C:\\Users\\xiaom\\Pictures\\aaa.jpg";

    public static void main(String[] args) throws IOException {

        RandomAccessFile r = new RandomAccessFile(new File(picPath), "r");
        byte[] bytes = new byte[(int) r.length()];
        r.readFully(bytes);
        System.out.println(bytes.length);

        String base64Str = Base64.getEncoder().encodeToString(bytes);

        String sign = getSHA256(base64Str);

        System.out.println(sign);

    }

    /**
     * 利用java原生的类实现SHA256加密
     *
     * @param str 加密后的报文
     * @return
     */
    public static String getSHA256(String str) {
        MessageDigest messageDigest;
        String encodestr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes("UTF-8"));
            encodestr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodestr;
    }

    /**
     * 将byte转为16进制
     *
     * @param bytes
     * @return
     */
    private static String byte2Hex(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i = 0; i < bytes.length; i++) {
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length() == 1) {
                // 1得到一位的进行补0操作
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }
    
}
