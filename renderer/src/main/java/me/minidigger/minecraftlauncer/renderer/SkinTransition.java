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

    @SafeVarargs
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
