package com.speacode.uploader;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class UploadVideo extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        UploaderGUI gui = new UploaderGUI(e.getProject(),true);
        gui.show();
    }
}
