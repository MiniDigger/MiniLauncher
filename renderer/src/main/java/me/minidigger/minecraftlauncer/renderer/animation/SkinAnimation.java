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

package me.minidigger.minecraftlauncer.renderer.animation;

import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import me.minidigger.minecraftlauncer.renderer.SkinTransition;
import me.minidigger.minecraftlauncer.renderer.util.FunctionHelper;

public class SkinAnimation {

    private int weight;
    private int left;

    protected List<SkinTransition> transitions;

    public SkinAnimation(int weight) {
        this.weight = weight;
        this.left = 0;
        this.transitions = new ArrayList<>();
    }

    protected void init() {
        transitions.forEach(t -> {
            EventHandler<ActionEvent> oldHandler = t.getOnFinished();
            EventHandler<ActionEvent> newHandler = e -> left--;
            newHandler = oldHandler == null ? newHandler : FunctionHelper.link(oldHandler, newHandler);
            t.setOnFinished(newHandler);
        });
    }

    public int getWeight() {
        return weight;
    }

    public boolean isPlaying() {
        return left > 0;
    }

    public void play() {
        transitions.forEach(SkinTransition::play);
        left = transitions.size();
    }

    public void playFromStart() {
        transitions.forEach(SkinTransition::playFromStart);
        left = transitions.size();
    }

    public void stop() {
        transitions.forEach(SkinTransition::stop);
        left = 0;
    }
}
