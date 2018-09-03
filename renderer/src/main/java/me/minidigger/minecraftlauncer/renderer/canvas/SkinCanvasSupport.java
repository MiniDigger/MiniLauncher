package me.minidigger.minecraftlauncer.renderer.canvas;

import java.util.function.Consumer;

import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

import static me.minidigger.minecraftlauncer.renderer.SkinData.scale;
import static me.minidigger.minecraftlauncer.renderer.SkinData.xRotation;
import static me.minidigger.minecraftlauncer.renderer.SkinData.yRotation;
import static me.minidigger.minecraftlauncer.renderer.SkinData.zRotation;

public abstract class SkinCanvasSupport implements Consumer<SkinCanvas> {

    public static final class Mouse extends SkinCanvasSupport {

        private double lastX, lastY, sensitivity;

        public void setSensitivity(double sensitivity) {
            this.sensitivity = sensitivity;
        }

        public double getSensitivity() {
            return sensitivity;
        }

        public Mouse(double sensitivity) {
            this.sensitivity = sensitivity;
        }

        @Override
        public void accept(SkinCanvas t) {
            t.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
                lastX = -1;
                lastY = -1;
            });
            t.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
                if (!(lastX == -1 || lastY == -1)) {
                    if (e.isAltDown() || e.isControlDown() || e.isShiftDown()) {
                        if (e.isShiftDown())
                            zRotation.setAngle(zRotation.getAngle() - (e.getSceneY() - lastY) * sensitivity);
                        if (e.isAltDown())
                            yRotation.setAngle(yRotation.getAngle() + (e.getSceneX() - lastX) * sensitivity);
                        if (e.isControlDown())
                            xRotation.setAngle(xRotation.getAngle() + (e.getSceneY() - lastY) * sensitivity);
                    } else {
                        double yaw = yRotation.getAngle() + (e.getSceneX() - lastX) * sensitivity;
                        yaw %= 360;
                        if (yaw < 0)
                            yaw += 360;

                        int flagX = yaw < 90 || yaw > 270 ? 1 : -1;
                        int flagZ = yaw < 180 ? -1 : 1;
                        double kx = Math.abs(90 - yaw % 180) / 90 * flagX, kz = Math.abs(90 - (yaw + 90) % 180) / 90 * flagZ;

                        xRotation.setAngle(xRotation.getAngle() + (e.getSceneY() - lastY) * sensitivity * kx);
                        yRotation.setAngle(yaw);
                        zRotation.setAngle(zRotation.getAngle() + (e.getSceneY() - lastY) * sensitivity * kz);
                    }
                }
                lastX = e.getSceneX();
                lastY = e.getSceneY();
            });
            t.addEventHandler(ScrollEvent.SCROLL, e -> {
                double delta = (e.getDeltaY() > 0 ? 1 : e.getDeltaY() == 0 ? 0 : -1) / 10D * sensitivity;
                scale.setX(Math.min(Math.max(scale.getX() - delta, 0.1), 10));
                scale.setY(Math.min(Math.max(scale.getY() - delta, 0.1), 10));
            });
        }

    }
}
