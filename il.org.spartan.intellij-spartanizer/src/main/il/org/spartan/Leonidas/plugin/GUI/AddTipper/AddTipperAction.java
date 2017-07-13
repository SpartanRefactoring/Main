package il.org.spartan.Leonidas.plugin.GUI.AddTipper;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

/**
 * @author Sharon
 * @since 19-06-17.
 */
public class AddTipperAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        new AddTipperUI().setVisible(true);
    }
}
