package me.minidigger.minecraftlauncer.renderer.animation.animations;

import javafx.util.Duration;
import me.minidigger.minecraftlauncer.renderer.SkinTransition;
import me.minidigger.minecraftlauncer.renderer.animation.SkinAnimation;
import me.minidigger.minecraftlauncer.renderer.SkinCanvas;
import me.minidigger.minecraftlauncer.renderer.util.FunctionHelper;

import static me.minidigger.minecraftlauncer.renderer.SkinData.lArm;
import static me.minidigger.minecraftlauncer.renderer.SkinData.rArm;

public final class WavingArmsAnimation extends SkinAnimation {

    public WavingArmsAnimation(int weight, int time, double angle, SkinCanvas canvas) {
        super(weight);

        SkinTransition lArmTransition = new SkinTransition(Duration.millis(time), v -> v * angle,
                lArm.getZRotate().angleProperty());

        SkinTransition rArmTransition = new SkinTransition(Duration.millis(time), v -> v * -angle,
                rArm.getZRotate().angleProperty());

        FunctionHelper.alwaysB(SkinTransition::setAutoReverse, true, lArmTransition, rArmTransition);
        FunctionHelper.alwaysB(SkinTransition::setCycleCount, 16, lArmTransition, rArmTransition);
        FunctionHelper.always(transitions::add, lArmTransition, rArmTransition);

        init();
    }
}
