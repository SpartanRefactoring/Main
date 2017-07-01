package il.org.spartan.Leonidas.plugin.GUI.About;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

/**
 * @author Amir Sagiv
 * @since 28/04/2017
 */
public class AboutAction extends AnAction {

    public static void main(String[] args) {
        new AboutLeonidas();
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        new AboutLeonidas();
    }
}
