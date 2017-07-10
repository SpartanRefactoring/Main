package il.org.spartan.Leonidas.plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import icons.Icons;
import il.org.spartan.Leonidas.plugin.GUI.LoadingIndicator.LoadingIndicator;
import il.org.spartan.Leonidas.plugin.utils.logging.Logger;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

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
//			Spartanizer.spartanizeFileRecursively(psiClass.getContainingFile());
//            Spartanizer.spartanizeFileRecursively(psiClass.getContainingFile());//bypass replacing stuck bug

            PsiDirectory srcDir = psiClass.getContainingFile().getContainingDirectory();
            try {
                srcDir.checkCreateSubdirectory("spartanizer");
                Object[] options = {"Accept", "Cancel"};
                if (JOptionPane.showOptionDialog(new JFrame(),
                        "You might be about to apply nano patterns.\nPlease notice that nano pattern tippers are "
                                + "code transformations that require adding a '.java' file "
								+ "to your project directory.\n"
								+ "To apply these tippers, please press the Accept button.",
						"SpartanizerUtils", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, Icons.Leonidas,
                        options, options[1]) == 1) {
                    Spartanizer.spartanizeFileRecursivelyNoNanos(psiClass.getContainingFile());
                    Spartanizer.spartanizeFileRecursivelyNoNanos(psiClass.getContainingFile());
                    return;
                }
                new WriteCommandAction.Simple(e.getProject()) {
                    @Override
                    protected void run() throws Throwable {
                        new AddSpartanizerUtilsAction().createEnvironment(e);
                    }
                }.execute();
            } catch (Exception ignored) {
            }
            LoadingIndicator li = new LoadingIndicator("Spartanizing current file");
            Spartanizer.spartanizeFileRecursively(psiClass.getContainingFile());
            Spartanizer.spartanizeFileRecursively(psiClass.getContainingFile());
            li.dispose();

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
