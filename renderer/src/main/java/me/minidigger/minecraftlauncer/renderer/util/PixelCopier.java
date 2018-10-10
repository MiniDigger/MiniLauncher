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

package me.minidigger.minecraftlauncer.renderer.util;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class PixelCopier {

    private Image srcImage;
    private WritableImage newImage;

    public PixelCopier(Image srcImage, WritableImage newImage) {
        this.srcImage = srcImage;
        this.newImage = newImage;
    }

    public void copy(int srcX, int srcY, int width, int height) {
        copy(srcX, srcY, srcX, srcY, width, height);
    }

    public void copy(int srcX, int srcY, int toX, int toY, int width, int height) {
        copy(srcX, srcY, toX, toY, width, height, false, false);
    }

    public void copy(int srcX, int srcY, int toX, int toY, int width, int height, boolean reversalX, boolean reversalY) {
        PixelReader reader = srcImage.getPixelReader();
        PixelWriter writer = newImage.getPixelWriter();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                writer.setArgb(toX + x, toY + y,
                        reader.getArgb(srcX + (reversalX ? width - x - 1 : x), srcY + (reversalY ? height - y - 1 : y)));
            }
        }
    }

    public void copy(float srcX, float srcY, float toX, float toY, float width, float height) {
        copy(srcX, srcY, toX, toY, width, height, false, false);
    }

    public void copy(float srcX, float srcY, float toX, float toY, float width, float height, boolean reversalX, boolean reversalY) {
        PixelReader reader = srcImage.getPixelReader();
        PixelWriter writer = newImage.getPixelWriter();
        int srcScaleX = (int) srcImage.getWidth();
        int srcScaleY = (int) srcImage.getHeight();
        int newScaleX = (int) newImage.getWidth();
        int newScaleY = (int) newImage.getHeight();
        int srcWidth = (int) (width * srcScaleX);
        int srcHeight = (int) (height * srcScaleY);

        for (int x = 0; x < srcWidth; x++) {
            for (int y = 0; y < srcHeight; y++) {
                writer.setArgb((int) (toX * newScaleX + x), (int) (toY * newScaleY + y),
                        reader.getArgb((int) (srcX * srcScaleX + (reversalX ? srcWidth - x - 1 : x)),
                                (int) (srcY * srcScaleY + (reversalY ? srcHeight - y - 1 : y))));
            }
        }
    }
}