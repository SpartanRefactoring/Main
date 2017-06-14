package il.org.spartan.Leonidas.plugin;

import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import icons.Icons;

/**
 * @author Anna Belozovsky
 * @since 14/06/2017
 */
public class NanoPatternsEnableAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        assert psiFile != null;
        Project p = psiFile.getProject();
        Toolbox tb = Toolbox.getInstance();
        if (tb.nanoEnabled) {
            tb.excludeNanoPatterns();
        } else {
            tb.includeNanoPatterns();
        }
        Presentation presentation = e.getPresentation();
        presentation.setText((Toolbox.getInstance().checkExcluded(e.getData(LangDataKeys.PSI_FILE)) ? "Enable" : "Disable")
                + " Nano Patterns");
        presentation.setIcon((Toolbox.getInstance().checkExcluded(e.getData(LangDataKeys.PSI_FILE)) ? Icons.Enable : Icons.Disable));
        DaemonCodeAnalyzer.getInstance(p).restart();
    }
}
