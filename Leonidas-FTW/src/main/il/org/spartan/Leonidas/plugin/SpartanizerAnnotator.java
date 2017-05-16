package il.org.spartan.Leonidas.plugin;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.EffectType;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.ui.JBColor;
import com.intellij.util.IncorrectOperationException;
import il.org.spartan.Leonidas.plugin.utils.logging.Logger;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

/**
 * @author Oren Afek
 * @since 08-12-2016
 */
public class SpartanizerAnnotator implements Annotator {

    @Override
    public void annotate(@NotNull final PsiElement e, @NotNull AnnotationHolder h) {
        try {
            if (!Spartanizer.canTip(e) || e.getContainingFile().getName().contains("Spartanizer"))
                return;
            Annotation annotation = h.createInfoAnnotation(e, "Spartanize This!");
            annotation.registerFix(new IntentionAction() {
                @Nls
                @NotNull
                @Override
                public String getText() {
                    return Toolbox.getInstance().getTipper(e).description(e);
                }

                @Nls
                @NotNull
                @Override
                public String getFamilyName() {
                    return "SpartanizerAction";
                }

                @Override
                public boolean isAvailable(@NotNull Project __, Editor e, PsiFile f) {
                    return true;
                }

                @Override
                public void invoke(@NotNull Project p, Editor ed, PsiFile f) throws IncorrectOperationException {
                    Spartanizer.spartanizeElement(e);
                }

                @Override
                public boolean startInWriteAction() {
                    return false;
                }
            });
            TextAttributesKey.createTextAttributesKey("");
            annotation.setEnforcedTextAttributes(
                    (new TextAttributes(null, null, JBColor.BLUE, EffectType.WAVE_UNDERSCORE, 0)));

        } catch (Throwable t) {
            Logger l = new Logger(this.getClass());
            l.error("", t);
        }
    }
}
