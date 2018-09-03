package me.minidigger.minecraftlauncer.renderer.model;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

public class Face extends Group {

    public Face(Image image, int startX, int startY, int width, int height, int interval, boolean reverseX, boolean reverseY,
                Supplier<Box> supplier, BiConsumer<Box, Point2D> consumer) {
        PixelReader reader = image.getPixelReader();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int argb;
                if ((argb = reader.getArgb(startX + (reverseX ? width - x - 1 : x) * interval,
                        startY + (reverseY ? height - y - 1 : y) * interval)) != 0) {
                    Box pixel = supplier.get();
                    consumer.accept(pixel, new Point2D(x, y));
                    pixel.setMaterial(createMaterial(Color.rgb(
                            (argb >> 16) & 0xFF, (argb >> 8) & 0xFF, (argb >> 0) & 0xFF)));
                    getChildren().add(pixel);
                }
            }
        }
    }

    private Material createMaterial(Color color) {
        return new PhongMaterial(color);
    }
}