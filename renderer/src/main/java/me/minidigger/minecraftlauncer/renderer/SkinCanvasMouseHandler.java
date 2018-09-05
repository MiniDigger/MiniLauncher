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

package me.minidigger.minecraftlauncer.renderer;

import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

import static me.minidigger.minecraftlauncer.renderer.SkinData.scale;
import static me.minidigger.minecraftlauncer.renderer.SkinData.xRotation;
import static me.minidigger.minecraftlauncer.renderer.SkinData.yRotation;
import static me.minidigger.minecraftlauncer.renderer.SkinData.zRotation;

public class SkinCanvasMouseHandler {

    private double lastX;
    private double lastY;
    private double sensitivity;

    public SkinCanvasMouseHandler(double sensitivity) {
        this.sensitivity = sensitivity;
    }

    public void press(MouseEvent e) {
        lastX = -1;
        lastY = -1;
    }

    public void drag(MouseEvent e) {
        if (!(lastX == -1 || lastY == -1)) {
            if (e.isAltDown() || e.isControlDown() || e.isShiftDown()) {
                if (e.isShiftDown()) {
                    zRotation.setAngle(zRotation.getAngle() - (e.getSceneY() - lastY) * sensitivity);
                }
                if (e.isAltDown()) {
                    yRotation.setAngle(yRotation.getAngle() + (e.getSceneX() - lastX) * sensitivity);
                }
                if (e.isControlDown()) {
                    xRotation.setAngle(xRotation.getAngle() + (e.getSceneY() - lastY) * sensitivity);
                }
            } else {
                double yaw = yRotation.getAngle() + (e.getSceneX() - lastX) * sensitivity;
                yaw %= 360;
                if (yaw < 0) {
                    yaw += 360;
                }

                int flagX = yaw < 90 || yaw > 270 ? 1 : -1;
                int flagZ = yaw < 180 ? -1 : 1;

                double kx = Math.abs(90 - yaw % 180) / 90 * flagX;
                double kz = Math.abs(90 - (yaw + 90) % 180) / 90 * flagZ;

                xRotation.setAngle(xRotation.getAngle() + (e.getSceneY() - lastY) * sensitivity * kx);
                yRotation.setAngle(yaw);
                zRotation.setAngle(zRotation.getAngle() + (e.getSceneY() - lastY) * sensitivity * kz);
            }
        }
        lastX = e.getSceneX();
        lastY = e.getSceneY();
    }

    public void scroll(ScrollEvent e) {
        double delta = (e.getDeltaY() > 0 ? 1 : e.getDeltaY() == 0 ? 0 : -1) / 10D * sensitivity;
        scale.setX(Math.min(Math.max(scale.getX() - delta, 0.1), 10));
        scale.setY(Math.min(Math.max(scale.getY() - delta, 0.1), 10));
    }
}
