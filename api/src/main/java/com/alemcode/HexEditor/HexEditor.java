package com.alemcode.HexEditor;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.File;

public class HexEditor
{
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
        final byte[] file_bytes = new byte[(int)this.file.length()];
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
