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

import javafx.scene.shape.Mesh;
import javafx.scene.shape.MeshView;

public class SkinCube extends MeshView {

    private double width, height, depth;
    private boolean isSlim;
    private Mesh model;

    public SkinCube(float width, float height, float depth, float scaleX, float scaleY, float startX, float startY, float enlarge, boolean isSlim) {
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.isSlim = isSlim;
        setMesh(model = new Model(width + enlarge, height + enlarge, depth + enlarge, scaleX, scaleY, startX, startY, isSlim));
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getWidth() {
        return width;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getHeight() {
        return height;
    }

    public void setDepth(double depth) {
        this.depth = depth;
    }

    public double getDepth() {
        return depth;
    }

    public boolean isSlim() {
        return isSlim;
    }

    public Mesh getModel() {
        return model;
    }

    public void setModel(Mesh model) {
        this.model = model;
        setMesh(model);
    }

}
