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
