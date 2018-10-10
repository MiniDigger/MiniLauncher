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

import javafx.scene.image.Image;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import me.minidigger.minecraftlauncer.renderer.model.SkinCube;
import me.minidigger.minecraftlauncer.renderer.model.SkinGroup;
import me.minidigger.minecraftlauncer.renderer.model.SkinMultipleCubes;

public class SkinData {

    public static final Image ALEX = new Image(SkinData.class.getClassLoader().getResourceAsStream("alex.png"));
    public static final Image STEVE = new Image(SkinData.class.getClassLoader().getResourceAsStream("steve.png"));

    public static final SkinCube ALEX_LEFT_ARM = new SkinCube(3, 12, 4, 14F / 64F, 16F / 64F, 32F / 64F, 48F / 64F, 0F, true);
    public static final SkinCube ALEX_RIGHT_ARM = new SkinCube(3, 12, 4, 14F / 64F, 16F / 64F, 40F / 64F, 16F / 64F, 0F, true);

    public static final SkinCube STEVEN_LEFT_ARM = new SkinCube(4, 12, 4, 16F / 64F, 16F / 64F, 32F / 64F, 48F / 64F, 0F, false);
    public static final SkinCube STEVEN_RIGHT_ARM = new SkinCube(4, 12, 4, 16F / 64F, 16F / 64F, 40F / 64F, 16F / 64F, 0F, false);

    public static final Rotate xRotation = new Rotate(0, Rotate.X_AXIS);
    public static final Rotate yRotation = new Rotate(180, Rotate.Y_AXIS);
    public static final Rotate zRotation = new Rotate(0, Rotate.Z_AXIS);
    public static final Translate translation = new Translate(0, 0, -80);
    public static final Scale scale = new Scale(1, 1);

    public static final SkinMultipleCubes headOuter = new SkinMultipleCubes(8, 8, 8, 32F / 64F, 0F, 1.125, 0.2);
    public static final SkinMultipleCubes bodyOuter = new SkinMultipleCubes(8, 12, 4, 16F / 64F, 32F / 64F, 1, 0.2);
    public static final SkinMultipleCubes lArmOuter = new SkinMultipleCubes(4, 12, 4, 48F / 64F, 48F / 64F, 1.0625, 0.2);
    public static final SkinMultipleCubes rArmOuter = new SkinMultipleCubes(4, 12, 4, 40F / 64F, 32F / 64F, 1.0625, 0.2);
    public static final SkinMultipleCubes lLegOuter = new SkinMultipleCubes(4, 12, 4, 0F / 64F, 48F / 64F, 1.0625, 0.2);
    public static final SkinMultipleCubes rLegOuter = new SkinMultipleCubes(4, 12, 4, 0F / 64F, 32F / 64F, 1.0625, 0.2);

    public static final SkinCube headInside = new SkinCube(8, 8, 8, 32F / 64F, 16F / 64F, 0F, 0F, 0F, false);
    public static final SkinCube bodyInside = new SkinCube(8, 12, 4, 24F / 64F, 16F / 64F, 16F / 64F, 16F / 64F, 0.03F, false);
    public static final SkinCube lArmInside = new SkinCube(4, 12, 4, 16F / 64F, 16F / 64F, 32F / 64F, 48F / 64F, 0F, false);
    public static final SkinCube rArmInside = new SkinCube(4, 12, 4, 16F / 64F, 16F / 64F, 40F / 64F, 16F / 64F, 0F, false);
    public static final SkinCube lLegInside = new SkinCube(4, 12, 4, 16F / 64F, 16F / 64F, 16F / 64F, 48F / 64F, 0F, false);
    public static final SkinCube rLegInside = new SkinCube(4, 12, 4, 16F / 64F, 16F / 64F, 0F, 16F / 64F, 0F, false);

    public static final SkinGroup head = new SkinGroup(
            new Rotate(0, 0, headInside.getHeight() / 2, 0, Rotate.X_AXIS),
            new Rotate(0, Rotate.Y_AXIS),
            new Rotate(0, 0, headInside.getHeight() / 2, 0, Rotate.Z_AXIS),
            headOuter, headInside
    );

    public static final SkinGroup body = new SkinGroup(
            new Rotate(0, Rotate.X_AXIS),
            new Rotate(0, Rotate.Y_AXIS),
            new Rotate(0, Rotate.Z_AXIS),
            bodyOuter, bodyInside
    );

    public static final SkinGroup lArm = new SkinGroup(
            new Rotate(0, 0, -lArmInside.getHeight() / 2, 0, Rotate.X_AXIS),
            new Rotate(0, Rotate.Y_AXIS),
            new Rotate(0, +lArmInside.getWidth() / 2, -lArmInside.getHeight() / 2, 0, Rotate.Z_AXIS),
            lArmOuter, lArmInside
    );

    public static final SkinGroup rArm = new SkinGroup(
            new Rotate(0, 0, -rArmInside.getHeight() / 2, 0, Rotate.X_AXIS),
            new Rotate(0, Rotate.Y_AXIS),
            new Rotate(0, -rArmInside.getWidth() / 2, -rArmInside.getHeight() / 2, 0, Rotate.Z_AXIS),
            rArmOuter, rArmInside
    );

    public static final SkinGroup lLeg = new SkinGroup(
            new Rotate(0, 0, -lLegInside.getHeight() / 2, 0, Rotate.X_AXIS),
            new Rotate(0, Rotate.Y_AXIS),
            new Rotate(0, 0, -lLegInside.getHeight() / 2, 0, Rotate.Z_AXIS),
            lLegOuter, lLegInside
    );

    public static final SkinGroup rLeg = new SkinGroup(
            new Rotate(0, 0, -rLegInside.getHeight() / 2, 0, Rotate.X_AXIS),
            new Rotate(0, Rotate.Y_AXIS),
            new Rotate(0, 0, -rLegInside.getHeight() / 2, 0, Rotate.Z_AXIS),
            rLegOuter, rLegInside
    );
}
