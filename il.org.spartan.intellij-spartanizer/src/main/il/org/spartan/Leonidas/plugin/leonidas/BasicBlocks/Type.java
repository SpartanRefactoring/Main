package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiTypeElement;
import com.intellij.psi.util.PsiTreeUtil;
import il.org.spartan.Leonidas.auxilary_layer.PsiRewrite;
import il.org.spartan.Leonidas.auxilary_layer.Utils;
import il.org.spartan.Leonidas.auxilary_layer.az;
import il.org.spartan.Leonidas.auxilary_layer.iz;
import il.org.spartan.Leonidas.plugin.leonidas.Matcher;

import java.util.List;
import java.util.Map;

/**
 * A basic block representing a type element.
 * @author amirsagiv83, michalcohen
 * @since 29-05-2017.
 */
public class Type extends NamedElement {

    private static final String TEMPLATE = "Class";

    boolean setAsOuterClass;

    public Type(Encapsulator e) {
        super(e, TEMPLATE);
    }

    /**
     * For reflection use DO NOT REMOVE!
     */
    public Type() {
        super(TEMPLATE);
    }

    @Override
    protected String getName(PsiElement e) {
        return !iz.type(e) ? null : az.type(e).getText();
    }

    @Override
    public boolean conforms(PsiElement e) {
        return iz.type(e) && super.conforms(e);
    }

    @Override
    protected boolean goUpwards(Encapsulator prev, Encapsulator next) {
        return iz.type(next.getInner());
    }

    @Override
    public GenericEncapsulator create(Encapsulator e, Map<Integer, List<Matcher.Constraint>> m) {
        return new Type(e);
    }

    @Override
    public List<PsiElement> replaceByRange(List<PsiElement> es, Map<Integer, List<PsiElement>> m, PsiRewrite r) {
        if (setAsOuterClass) {
            List<PsiElement>[] a = new List[m.values().size()];
            m.values().toArray(a);
            PsiClass c = PsiTreeUtil.getParentOfType(a[0].get(0), PsiClass.class);
            return Utils.wrapWithList(
                    JavaPsiFacade.getElementFactory(Utils.getProject()).createTypeElementFromText(c.getName(), c));

        }
        if (!iz.classDeclaration(es.get(0)))
			return super.replaceByRange(es, m, r);
		PsiClass c = az.classDeclaration(es.get(0));
        PsiTypeElement pte = JavaPsiFacade.getElementFactory(Utils.getProject()).createTypeElementFromText(c.getName(),
                c);
        inner = pte;
        return Utils.wrapWithList(pte);
    }

    @Override
    public void copyTo(GenericEncapsulator dst) {
        if (!(dst instanceof Type)) return;
        super.copyTo(dst);
        ((Type) dst).setAsOuterClass = setAsOuterClass;
    }

    public void setAsOuterClass() {
        setAsOuterClass = true;
    }
}

