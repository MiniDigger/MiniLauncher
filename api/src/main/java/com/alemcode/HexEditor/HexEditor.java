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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class HexEditor {
    public String file_hex_string;
    public String filepath;
    public File file;

    public HexEditor(final String filepath) throws IOException {
        super();
        this.open(filepath);
    }

    public void open(final String filepath) throws IOException {
        this.filepath = filepath;
        this.file = new File(filepath);
        final byte[] file_bytes = new byte[(int) this.file.length()];
        final FileInputStream fis = new FileInputStream(this.file);
        int bytes_read = 0;
        do {
            bytes_read = fis.read(file_bytes);
        } while (bytes_read != -1);
        this.file_hex_string = new String(Hex.bytesToHex(file_bytes));
        fis.close();
    }

    public void saveAs(final String filepath) throws IOException, FileNotFoundException {
        final byte[] file_hex_bytes = Hex.hexToBytes(this.file_hex_string.toCharArray());
        final FileOutputStream file_save = new FileOutputStream(filepath);
        file_save.write(file_hex_bytes);
    }

    public void save() throws IOException, FileNotFoundException {
        this.saveAs(this.filepath);
    }

    public void delete() {
        this.file.delete();
    }

    public void replace(final CharSequence match, final CharSequence replacement) {
        this.file_hex_string = this.file_hex_string.replace(match, replacement);
    }

    public void regexReplace(final String regex, final String replacement) {
        this.file_hex_string = this.file_hex_string.replaceAll(regex, replacement);
    }

    public void replacePosition(final int position, final char replacement) {
        this.file_hex_string = this.file_hex_string.substring(0, position) + replacement + this.file_hex_string.substring(position + 1);
    }
}
