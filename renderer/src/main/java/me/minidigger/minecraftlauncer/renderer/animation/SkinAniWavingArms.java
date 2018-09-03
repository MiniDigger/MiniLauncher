package me.minidigger.minecraftlauncer.renderer.animation;

import javafx.util.Duration;
import me.minidigger.minecraftlauncer.renderer.FunctionHelper;
import me.minidigger.minecraftlauncer.renderer.SkinAnimation;
import me.minidigger.minecraftlauncer.renderer.SkinCanvas;
import me.minidigger.minecraftlauncer.renderer.SkinTransition;

public final class SkinAniWavingArms extends SkinAnimation {

    public SkinAniWavingArms(int weight, int time, double angle, SkinCanvas canvas) {
        SkinTransition larmTransition = new SkinTransition(Duration.millis(time), v -> v * angle,
                canvas.larm.getZRotate().angleProperty());

        SkinTransition rarmTransition = new SkinTransition(Duration.millis(time), v -> v * -angle,
                canvas.rarm.getZRotate().angleProperty());

        FunctionHelper.alwaysB(SkinTransition::setAutoReverse, true, larmTransition, rarmTransition);
        FunctionHelper.alwaysB(SkinTransition::setCycleCount, 2, larmTransition, rarmTransition);
        FunctionHelper.always(transitions::add, larmTransition, rarmTransition);
        this.weight = weight;
        init();
    }

}
