package me.minidigger.minecraftlauncer.renderer.animation.animations;

import javafx.util.Duration;
import me.minidigger.minecraftlauncer.renderer.SkinTransition;
import me.minidigger.minecraftlauncer.renderer.animation.SkinAnimation;
import me.minidigger.minecraftlauncer.renderer.canvas.SkinCanvas;
import me.minidigger.minecraftlauncer.renderer.util.FunctionHelper;

import static me.minidigger.minecraftlauncer.renderer.SkinData.lArm;
import static me.minidigger.minecraftlauncer.renderer.SkinData.lLeg;
import static me.minidigger.minecraftlauncer.renderer.SkinData.rArm;
import static me.minidigger.minecraftlauncer.renderer.SkinData.rLeg;

public class RunningAnimation extends SkinAnimation {

    private SkinTransition rArmTransition;
    private SkinTransition lArmTransition;

    public RunningAnimation(int weight, int time, double angle, SkinCanvas canvas) {
        super(weight);

        lArmTransition = new SkinTransition(Duration.millis(time),
                v -> v * (lArmTransition.getCount() % 4 < 2 ? 1 : -1) * angle,
                lArm.getXRotate().angleProperty(), rLeg.getXRotate().angleProperty());

        rArmTransition = new SkinTransition(Duration.millis(time),
                v -> v * (rArmTransition.getCount() % 4 < 2 ? 1 : -1) * -angle,
                rArm.getXRotate().angleProperty(), lLeg.getXRotate().angleProperty());

        FunctionHelper.alwaysB(SkinTransition::setAutoReverse, true, lArmTransition, rArmTransition);
        FunctionHelper.alwaysB(SkinTransition::setCycleCount, 16, lArmTransition, rArmTransition);
        FunctionHelper.always(transitions::add, lArmTransition, rArmTransition);

        init();
    }
}
