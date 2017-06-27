package il.org.spartan.Leonidas.plugin.GUI.ToolBoxController;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

/**
 * @author Amir Sagiv
 * @since 24/04/2017
 */
class ToolBoxControllerAction extends AnAction {
    public static void main(String[] args) {
        new ToolBoxController();
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        new ToolBoxController();
    }
}