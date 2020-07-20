package com.greencross.gctemperlib.greencare.bluetooth.device;

import java.math.BigDecimal;

public class BleUtil {

    public static float changeOnePoint(float paramFloat, int paramInt) {
        return new BigDecimal(paramFloat).setScale(paramInt, 4).floatValue();
    }

    public static int dec(String hex) {
        String[] temp = hex.split("0x");
        StringBuffer dec = null;
        for (String strArr : temp) {
            dec = new StringBuffer();
            dec.append(strArr);
        }
        return Integer.parseInt(dec.toString(), 16);
    }

    public static int hexToTen(String paramString) {
        if ((paramString == null) || ((paramString != null) && ("".equals(paramString)))) {
        }
        for (int i = 0; ; i = Integer.valueOf(paramString, 16).intValue()) {
            return i;
        }
    }

    public static float getHexToDec(String hex) {
        long v = Long.parseLong(hex, 16);
        return Float.parseFloat(String.valueOf(v));
    }

    public static int getHexToInt(String hex) {
        int v = Integer.parseInt(hex, 16);
        return v;
    }

    public static String byteToBinaryString(byte n) {
        StringBuilder sb = new StringBuilder("00000000");
        for (int bit = 0; bit < 8; bit++) {
            if (((n >> bit) & 1) > 0) {
                sb.setCharAt(7 - bit, '1');
            }
        }
        return sb.toString();
    }

    public static String getHexString(byte b) {
        try {
            return Integer.toString((b & 0xff) + 0x100, 16).substring(1);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getHexString(byte[] b) {
        String result = "";
        try {
            for (int i = 0; i < b.length; i++) {
                result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
            }
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder();
        for(final byte b: a)
            sb.append(String.format("%02x ", b&0xff));
        return sb.toString();
    }


    public static String ByteArrayToHex_position(byte[] a,int start, int end) {
        StringBuilder sb = new StringBuilder();
        if (a.length > 0){
            for (int i = start; i <= end; i++) {
                sb.append(String.format("%02x", a[i] & 0xff));
            }
        } else {
            return "";
        }
        return sb.toString();
    }

    public static String ByteArrayToLockBit(byte[] a,int position, int start, int end) {
        StringBuilder sb = new StringBuilder();
        if (a.length > 0){
            sb.append(String.format("%8s", Integer.toBinaryString(a[position] & 0xFF)).replace(' ', '0').substring(start,end));
        } else {
            return "";
        }
        return sb.toString();
    }
}
