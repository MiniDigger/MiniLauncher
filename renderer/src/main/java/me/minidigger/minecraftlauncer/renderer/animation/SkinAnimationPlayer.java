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

import javafx.animation.AnimationTimer;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

public class SkinAnimationPlayer {
    protected LinkedList<SkinAnimation> animations = new LinkedList<>();
    protected SkinAnimation playing;
    protected boolean running;
    protected int weightedSum = 0;
    protected long lastPlayTime = -1L, interval = 10_000_000_000L;
    protected AnimationTimer animationTimer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            if (playing == null || !playing.isPlaying() && now - lastPlayTime > interval) {
                int nextAni = ThreadLocalRandom.current().nextInt(weightedSum);
                SkinAnimation tmp = null;
                for (SkinAnimation animation : animations) {
                    nextAni -= animation.getWeight();
                    tmp = animation;
                    if (nextAni <= 0) {
                        break;
                    }
                }
                playing = tmp;
                if (playing == null && animations.size() > 0) {
                    playing = animations.getLast();
                }
                if (playing != null) {
                    playing.playFromStart();
                    lastPlayTime = now;
                }
            }
        }
    };

    public int getWeightedSum() {
        return weightedSum;
    }

    public void setInterval(long interval) {
        this.interval = interval;
        if (interval < 1) {
            animationTimer.stop();
        } else {
            start();
        }
    }

    public long getInterval() {
        return interval;
    }

    public long getLastPlayTime() {
        return lastPlayTime;
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isPlaying() {
        return playing != null;
    }

    public SkinAnimation getPlaying() {
        return playing;
    }

    public void addSkinAnimation(SkinAnimation... animations) {
        this.animations.addAll(Arrays.asList(animations));
        this.weightedSum = this.animations.stream().mapToInt(SkinAnimation::getWeight).sum();

        start();
    }

    public void start() {
        if (!running && weightedSum > 0 && interval > 0) {
            animationTimer.start();
            running = true;
        }
    }

    public void stop() {
        if (running) {
            animationTimer.stop();
        }

        if (playing != null) {
            playing.stop();
        }

        running = false;
    }
}
