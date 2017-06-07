package il.org.spartan.Leonidas.plugin;

import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

/**
 * @author michalcohen
 * @since 03-01-2017
 */
public class SpartanizerEnableAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        assert psiFile != null;
        Project p = psiFile.getProject();
        Toolbox tb = Toolbox.getInstance();
        if (tb.checkExcluded(psiFile))
			tb.includeFile(psiFile);
		else
			tb.excludeFile(psiFile);
        Presentation presentation = e.getPresentation();
        presentation.setText((Toolbox.getInstance().checkExcluded(e.getData(LangDataKeys.PSI_FILE)) ? "Enable" : "Disable")
				+ " Spartanization In Current File");
        DaemonCodeAnalyzer.getInstance(p).restart();
    }
}
