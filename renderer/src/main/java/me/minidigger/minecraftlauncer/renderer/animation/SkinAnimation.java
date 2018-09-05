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
