package il.org.spartan.Leonidas.plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import il.org.spartan.Leonidas.plugin.utils.logging.Logger;
import org.jetbrains.annotations.Nullable;

/**
 * @author Roey Maor, Oren Afek, michalcohen
 * @since 18-11-2016
 */
public class SpartanizerAction extends AnAction {

    private static Logger logger = new Logger(SpartanizerAction.class);

    @Override
    public void actionPerformed(AnActionEvent e) {
        PsiClass psiClass = getPsiClassFromContext(e);
        if (psiClass == null)
			logger.warn("Spartanization action retrieved null PSI class");
		else {
			logger.info("Spartanization action on\nPSI class '" + psiClass.getQualifiedName() + "'");
			Spartanizer.spartanizeFileRecursively(psiClass.getContainingFile());
		}
    }

    /**
     * @param e the action event
     * @return the psiElement extracted from the event's context
     **/
    @Nullable
    private PsiElement getPsiElementFromContext(AnActionEvent e) {
        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        return psiFile == null || editor == null ? null : psiFile.findElementAt(editor.getCaretModel().getOffset());
    }

    /**
     * @param ¢ the action event
     * @return the psiClass extracted from the event's context
     **/
    @Nullable
    private PsiClass getPsiClassFromContext(AnActionEvent ¢) {
        return PsiTreeUtil.getParentOfType(getPsiElementFromContext(¢), PsiClass.class);
    }


}
