package me.minidigger.minecraftlauncer.renderer.util;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public interface SkinHelper {

    static boolean isNoRequest(Image image) {
        return image.getRequestedWidth() == 0 && image.getRequestedHeight() == 0;
    }

    static boolean isSkin(Image image) {
        return image.getWidth() % 64 == 0 && image.getWidth() / 64 > 0 &&
                (image.getHeight() == image.getWidth() / 2 || image.getHeight() == image.getWidth());
    }

    static Image x32Tox64(Image srcSkin) {
        if (srcSkin.getHeight() == 64) {
            return srcSkin;
        }

        WritableImage newSkin = new WritableImage((int) srcSkin.getWidth(), (int) srcSkin.getHeight() * 2);
        PixelCopier copyer = new PixelCopier(srcSkin, newSkin);
        // HEAD & HAT
        copyer.copy(0 / 64F, 0 / 32F, 0 / 64F, 0 / 64F, 64 / 64F, 16 / 32F);

        // LEFT-LEG
        x32Tox64(copyer, 0 / 64F, 16 / 32F, 16 / 64F, 48 / 64F, 4 / 64F, 12 / 32F, 4 / 64F);

        // RIGHT-LEG
        copyer.copy(0 / 64F, 16 / 32F, 0 / 64F, 16 / 64F, 16 / 64F, 16 / 32F);

        // BODY
        copyer.copy(16 / 64F, 16 / 32F, 16 / 64F, 16 / 64F, 24 / 64F, 16 / 32F);

        // LEFT-ARM
        x32Tox64(copyer, 40 / 64F, 16 / 32F, 32 / 64F, 48 / 64F, 4 / 64F, 12 / 32F, 4 / 64F);

        // RIGHT-ARM
        copyer.copy(40 / 64F, 16 / 32F, 40 / 64F, 16 / 64F, 16 / 64F, 16 / 32F);

        return newSkin;
    }

    static void x32Tox64(PixelCopier copyer, float srcX, float srcY, float toX, float toY, float width, float height, float depth) {
        // TOP
        copyer.copy(srcX + depth, srcY, toX + depth, toY, width, depth * 2, true, false);

        // BOTTOM
        copyer.copy(srcX + depth + width, srcY, toX + depth + width, toY, width, depth * 2, true, false);

        // INS
        copyer.copy(srcX, srcY + depth * 2, toX + width + depth, toY + depth, depth, height, true, false);

        // OUTS
        copyer.copy(srcX + width + depth, srcY + depth * 2, toX, toY + depth, depth, height, true, false);

        // FRONT
        copyer.copy(srcX + depth, srcY + depth * 2, toX + depth, toY + depth, width, height, true, false);

        // BACK
        copyer.copy(srcX + width + depth * 2, srcY + depth * 2, toX + width + depth * 2, toY + depth, width, height, true, false);
    }

    static Image enlarge(Image srcSkin, int multiple) {
        WritableImage newSkin = new WritableImage((int) srcSkin.getWidth() * multiple, (int) srcSkin.getHeight() * multiple);
        PixelReader reader = srcSkin.getPixelReader();
        PixelWriter writer = newSkin.getPixelWriter();

        for (int x = 0, lenX = (int) srcSkin.getWidth(); x < lenX; x++) {
            for (int y = 0, lenY = (int) srcSkin.getHeight(); y < lenY; y++) {
                for (int mx = 0; mx < multiple; mx++) {
                    for (int my = 0; my < multiple; my++) {
                        int argb = reader.getArgb(x, y);
                        writer.setArgb(x * multiple + mx, y * multiple + my, argb);
                    }
                }
            }
        }

        return newSkin;
    }
}
