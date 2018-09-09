package me.minidigger.minecraftlauncher.launcher.gui;

import java.text.MessageFormat;

import me.minidigger.minecraftlauncher.launcher.Status;

public abstract class FragmentController extends AbstractGUIController {

    private FrameController mainFrame;

    public FrameController getMainFrame() {
        return mainFrame;
    }

    public void setMainFrame(FrameController frameController) {
        this.mainFrame = frameController;
        API.setEventHandler(mainFrame);
    }

    @Override
    public void setStatus(Status status) {
        setStatusText(MessageFormat.format(resourceBundle.getString("status.generic"), status));
    }

    public void setStatusText(String text) {
        mainFrame.setStatusText(text);
    }
}
