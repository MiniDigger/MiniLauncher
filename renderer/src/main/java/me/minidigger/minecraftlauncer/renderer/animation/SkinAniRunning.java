package me.minidigger.minecraftlauncer.renderer.animation;

import javafx.util.Duration;
import me.minidigger.minecraftlauncer.renderer.FunctionHelper;
import me.minidigger.minecraftlauncer.renderer.SkinAnimation;
import me.minidigger.minecraftlauncer.renderer.SkinCanvas;
import me.minidigger.minecraftlauncer.renderer.SkinTransition;

public final class SkinAniRunning extends SkinAnimation {
	
	private SkinTransition larmTransition, rarmTransition;
	
	public SkinAniRunning(int weight, int time, double angle, SkinCanvas canvas) {
		larmTransition = new SkinTransition(Duration.millis(time),
				v -> v * (larmTransition.getCount() % 4 < 2 ? 1 : -1) * angle,
				canvas.larm.getXRotate().angleProperty(), canvas.rleg.getXRotate().angleProperty());
		
		rarmTransition = new SkinTransition(Duration.millis(time),
				v -> v * (rarmTransition.getCount() % 4 < 2 ? 1 : -1) * -angle,
				canvas.rarm.getXRotate().angleProperty(), canvas.lleg.getXRotate().angleProperty());
		
		FunctionHelper.alwaysB(SkinTransition::setAutoReverse, true, larmTransition, rarmTransition);
		FunctionHelper.alwaysB(SkinTransition::setCycleCount, 16, larmTransition, rarmTransition);
		FunctionHelper.always(transitions::add, larmTransition, rarmTransition);
		this.weight = weight;
		init();
	}

}
