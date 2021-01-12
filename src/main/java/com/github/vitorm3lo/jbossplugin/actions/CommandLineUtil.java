package com.github.vitorm3lo.jbossplugin.actions;

import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.remoteServer.runtime.deployment.debug.DebugConnector;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;

import javax.annotation.Nullable;

public class CommandLineUtil {

    public static final String DEBUG_CONSOLE = "Debug";
    public static final String RUN_CONSOLE = "Terminal";
    public static String DISPLAY_NAME = "CustomCommand";
    // open in integrated terminal tool window
    public static String CUSTOM_CONSOLE = "Terminal";

    @Nullable
    public static Content getConsoleViewContent(Project project) {

        if (project == null) {
            return null;
        }
        ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow(CUSTOM_CONSOLE);

        if (toolWindow == null) {
            return null;
        }
        Content consoleViewContent = getExistingConsoleViewContent(toolWindow);
        if (consoleViewContent != null) {
            return consoleViewContent;
        }

        // Create a new consoleView if it does not exist
        ConsoleView consoleView = TextConsoleBuilderFactory.getInstance().createBuilder(project).getConsole();
        Content[] contents = toolWindow.getContentManager().getContents();
        for (Content content : contents) {
            if(content.getDisplayName().equals(DISPLAY_NAME)) {
                return content;
            }
        }
        Content newConent =
                toolWindow.getContentManager().getFactory().createContent(consoleView.getComponent(), DISPLAY_NAME, false);

        // I don't know why this throws a null pointer the first time and the second time does not
        try {
            toolWindow.getContentManager().addContent(newConent);
        } catch (NullPointerException e) {
//            toolWindow.getContentManager().addContent(newConent);
        }

        return newConent;
    }

    @Nullable
    static Content getExistingConsoleViewContent(ToolWindow toolWindow) {
        ContentManager contentManager = toolWindow.getContentManager();
        return contentManager.findContent(DISPLAY_NAME);
    }

    public static void activateConsoleView(Project project) {
        Content content = getConsoleViewContent(project);
        ToolWindow toolWindow = getToolWindow(project);
        if (content != null && toolWindow != null) {
            toolWindow.show();
            toolWindow.getContentManager().setSelectedContent(content);
        }
    }

    @Nullable
    static ToolWindow getToolWindow(Project project) {
        if (project == null || project.isDisposed()) {
            return null;
        }
        return ToolWindowManager.getInstance(project).getToolWindow(CUSTOM_CONSOLE);
    }

}
