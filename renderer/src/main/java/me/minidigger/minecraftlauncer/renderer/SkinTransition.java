package me.minidigger.minecraftlauncer.renderer;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import javafx.animation.Transition;
import javafx.beans.value.WritableValue;
import javafx.util.Duration;

public class SkinTransition extends Transition {

    private Function<Double, Double> expression;
    private List<WritableValue<Number>> observables;
    private int count;

    public int getCount() {
        return count;
    }

    public SkinTransition(Duration duration, Function<Double, Double> expression, WritableValue<Number>... observables) {
        setCycleDuration(duration);
        this.expression = expression;
        this.observables = Arrays.asList(observables);
    }

    @Override
    protected void interpolate(double frac) {
        if (frac == 0 || frac == 1) {
            count++;
        }
        double val = expression.apply(frac);
        observables.forEach(w -> w.setValue(val));
    }

    @Override
    public void play() {
        count = 0;
        super.play();
    }

}
