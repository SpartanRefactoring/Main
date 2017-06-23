package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiTypeElement;
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
        return iz.type(e) ? az.type(e).getText() : null;
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
    public GenericEncapsulator create(Encapsulator e, Map<Integer, List<Matcher.Constraint>> map) {
        return new Type(e);
    }

    @Override
    public List<PsiElement> replaceByRange(List<PsiElement> elements, Map<Integer, List<PsiElement>> m, PsiRewrite r) {
        if (iz.classDeclaration(elements.get(0))) { // Notice the element that type will be replaced by is assign to PsiClass, so we need to extract its name.
            PsiClass c = az.classDeclaration(elements.get(0));
            PsiTypeElement pte = JavaPsiFacade.getElementFactory(Utils.getProject()).createTypeElementFromText(c.getName(), c);
            inner = pte;
            return Utils.wrapWithList(pte);
        }
        return super.replaceByRange(elements, m, r);
    }
}

