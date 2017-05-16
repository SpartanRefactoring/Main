package il.org.spartan.Leonidas.plugin.GUI.ToolBoxControllerSwing;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

/**
 * @author Amir Sagiv
 * @since 24/04/2017
 */
public class ToolBoxControllerAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        new ToolBoxController();
    }

    public static void main(String[] args) {
        new ToolBoxController();
    }
}