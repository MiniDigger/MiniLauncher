/*
 * MIT License
 *
 * Copyright (c) 2018 Ammar Ahmad
 * Copyright (c) 2018 Martin Benndorf
 * Copyright (c) 2018 Mark Vainomaa
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
