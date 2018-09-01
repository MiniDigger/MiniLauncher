package com.alemcode.HexEditor;

public class Hex {
    private static final char[] hex_digits;

    public Hex() {
        super();
    }

    public static char[] bytesToHex(final byte[] bytes) {
        final int bytes_length = bytes.length;
        final char[] hex_form = new char[bytes_length * 2];
        int i = 0;
        int j = 0;
        while (i < bytes_length) {
            hex_form[j] = Hex.hex_digits[(bytes[i] & 0xF0) >>> 4];
            hex_form[j + 1] = Hex.hex_digits[bytes[i] & 0xF];
            j = ++i * 2;
        }
        return hex_form;
    }

    public static byte[] hexToBytes(final char[] hex_chars) {
        final int hex_length = hex_chars.length;
        final int byte_length = hex_length / 2;
        final byte[] byte_form = new byte[byte_length];
        int i = 0;
        int j = 0;
        while (i < byte_length) {
            final int most_sig = hexToDec(hex_chars[j], 1);
            final int least_sig = hexToDec(hex_chars[j + 1], 0);
            byte_form[i] = (byte) (most_sig + least_sig);
            j = ++i * 2;
        }
        return byte_form;
    }

    public static int hexToDec(final char hex_char, final int position) {
        return Character.digit(hex_char, 16) << position * 4;
    }

    static {
        hex_digits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    }
}
