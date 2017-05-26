package il.org.spartan.Leonidas.plugin.tippers;

import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.impl.source.PsiMethodImpl;
import il.org.spartan.Leonidas.auxilary_layer.Wrapper;
import il.org.spartan.Leonidas.auxilary_layer.az;
import il.org.spartan.Leonidas.auxilary_layer.iz;
import il.org.spartan.Leonidas.auxilary_layer.step;

/**
 * <p>
 * Copied and migrated to work in IntellliJ.
 * Original author is Ori Marcovitch.
 *
 * @author RoeiRaz
 * @since 03-01-2017
 *
 */
public abstract class JavadocMarkerNanoPattern extends NanoPatternTipper<PsiMethod> {
    @Override
    public boolean canTip(PsiElement e) {
        return iz.method(e) && !hasTag(az.method(e)) && prerequisites(az.method(e));
    }

    @Override
	public final PsiElement createReplacement(PsiMethod m) {
		String docOld = step.docCommentString(m), docNew = docOld + tag();
		final Wrapper<String> methodText = new Wrapper<>("");
		m.acceptChildren(new JavaElementVisitor() {
			@Override
			public void visitElement(PsiElement e) {
				if (!iz.javadoc(e))
					methodText.set(methodText.get() + e.getText());
				super.visitElement(e);
			}
		});
		return JavaPsiFacade.getElementFactory(m.getProject()).createMethodFromText("/**" + docNew + "*/" + methodText,
				m.getContext());
	}

    @Override
    public String description(PsiMethod m) {
        return "Add \"Delegator\" javadoc";
    }

    protected abstract boolean prerequisites(PsiMethod ¢);

    public final String tag() {
        return "[[" + this.getClass().getSimpleName() + "]]";
    }

    private boolean hasTag(PsiMethod m) {
        return step.docCommentString(m).contains(tag());
    }

    @Override
    public Class<? extends PsiMethod> getPsiClass() {
        return PsiMethodImpl.class;
    }

    @Override
    public String name() {
        return "JavadocMarkerNanoPattern";
    }
}
