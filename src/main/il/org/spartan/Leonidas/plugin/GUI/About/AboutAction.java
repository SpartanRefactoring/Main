package il.org.spartan.Leonidas.plugin.GUI.About;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

/**
 * @author Amir Sagiv
 * @since 28/04/2017
 */
public class AboutAction extends AnAction{
    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        new AboutLeonidas();
    }

//    public static void main(String[] args) {
//        new AboutLeonidas();
//    }
}
