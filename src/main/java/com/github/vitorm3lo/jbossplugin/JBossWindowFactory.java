package com.github.vitorm3lo.jbossplugin;

import com.github.vitorm3lo.jbossplugin.forms.JBossToolWindow;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

public class JBossWindowFactory implements ToolWindowFactory {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        JBossToolWindow jbossToolWindow = new JBossToolWindow(toolWindow);
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(jbossToolWindow.getContent(), "", false);
        toolWindow.getContentManager().addContent(content);
    }

}