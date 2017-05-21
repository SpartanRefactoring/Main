package il.org.spartan.Leonidas.plugin.GUI.PlaygroundController;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

/**
 * @author Anna Belozovsky
 * @since 22/04/2017
 */
public class PlaygroundControllerAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        new Playground();
    }
}
