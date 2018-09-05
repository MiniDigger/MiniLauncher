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

package me.minidigger.minecraftlauncer.renderer.model;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.shape.Box;

public class SkinMultipleCubes extends Group {

    private int width;
    private int height;
    private int depth;

    private float startX;
    private float startY;

    private double length;
    private double thick;

    public SkinMultipleCubes(int width, int height, int depth, float startX, float startY, double length, double thick) {
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.startX = startX;
        this.startY = startY;
        this.length = length;
        this.thick = thick;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getWidth() {
        return width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getDepth() {
        return depth;
    }

    public void setStartX(float startX) {
        this.startX = startX;
    }

    public float getStartX() {
        return startX;
    }

    public void setStartY(float startY) {
        this.startY = startY;
    }

    public float getStartY() {
        return startY;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getLength() {
        return length;
    }

    public void setThick(double thick) {
        this.thick = thick;
    }

    public double getThick() {
        return thick;
    }

    public void updateSkin(Image skin) {
        getChildren().clear();
        int start_x = (int) (startX * skin.getWidth()), start_y = (int) (startY * skin.getHeight()),
                interval = (int) Math.max(skin.getWidth() / 64, 1),
                width_interval = width * interval, height_interval = height * interval, depth_interval = depth * interval;
        // FRONT
        getChildren().add(new Face(skin, start_x + depth_interval, start_y + depth_interval, width, height, interval, false, false,
                () -> new Box(length, length, thick), (b, p) -> {
            b.setTranslateX(((width - 1) / 2.0 - p.getX()) * b.getWidth());
            b.setTranslateY(-((height - 1) / 2.0 - p.getY()) * b.getHeight());
            b.setTranslateZ((depth * length + thick) / 2.0);
        }));
        // BACK
        getChildren().add(new Face(skin, start_x + width_interval + depth_interval * 2, start_y + depth_interval, width, height, interval, true, false,
                () -> new Box(length, length, thick), (b, p) -> {
            b.setTranslateX(((width - 1) / 2.0 - p.getX()) * b.getWidth());
            b.setTranslateY(-((height - 1) / 2.0 - p.getY()) * b.getHeight());
            b.setTranslateZ(-(depth * length + thick) / 2.0);
        }));
        // LEFT
        getChildren().add(new Face(skin, start_x + width_interval + depth_interval, start_y + depth_interval, depth, height, interval, false, false,
                () -> new Box(thick, length, length), (b, p) -> {
            b.setTranslateX((width * length + thick) / 2.0);
            b.setTranslateY(-((height - 1) / 2.0 - p.getY()) * b.getHeight());
            b.setTranslateZ(((depth - 1) / 2.0 - p.getX()) * b.getDepth());
        }));
        // RIGHT
        getChildren().add(new Face(skin, start_x, start_y + depth_interval, depth, height, interval, true, false,
                () -> new Box(thick, length, length), (b, p) -> {
            b.setTranslateX(-(width * length + thick) / 2.0);
            b.setTranslateY(-((height - 1) / 2.0 - p.getY()) * b.getHeight());
            b.setTranslateZ(((depth - 1) / 2.0 - p.getX()) * b.getDepth());
        }));
        // TOP
        getChildren().add(new Face(skin, start_x + depth_interval, start_y, width, depth, interval, false, false,
                () -> new Box(length, thick, length), (b, p) -> {
            b.setTranslateX(((width - 1) / 2.0 - p.getX()) * b.getWidth());
            b.setTranslateY(-(height * length + thick) / 2.0);
            b.setTranslateZ(-((depth - 1) / 2.0 - p.getY()) * b.getDepth());
        }));
        // BOTTOM
        getChildren().add(new Face(skin, start_x + width_interval + depth_interval, start_y, width, depth, interval, false, false,
                () -> new Box(length, thick, length), (b, p) -> {
            b.setTranslateX(((width - 1) / 2.0 - p.getX()) * b.getWidth());
            b.setTranslateY((height * length + thick) / 2.0);
            b.setTranslateZ(-((depth - 1) / 2.0 - p.getY()) * b.getDepth());
        }));
    }

}
