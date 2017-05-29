package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.auxilary_layer.PsiRewrite;
import il.org.spartan.Leonidas.auxilary_layer.Utils;
import il.org.spartan.Leonidas.auxilary_layer.az;
import il.org.spartan.Leonidas.auxilary_layer.iz;
import il.org.spartan.Leonidas.plugin.leonidas.Matcher;

import javax.rmi.CORBA.Util;
import java.util.List;
import java.util.Map;

/**
 * @author amirsagiv83, michalcohen
 * @since 29-05-2017.
 */
public class Class extends NamedElement{

    private static final String TEMPLATE = "Class";

    public Class(Encapsulator e) {
        super(e, TEMPLATE);
    }

    /**
     * For reflection use DO NOT REMOVE!
     */
    public Class() {
        super(TEMPLATE);
    }

    @Override
    protected String getName(PsiElement e) {
        return iz.classDeclaration(e) ? az.classDeclaration(e).getName() : null;
    }

    @Override
    protected boolean goUpwards(Encapsulator prev, Encapsulator next) {
        return next != null && prev.getText().equals(next.getText());
    }

    @Override
    public GenericEncapsulator create(Encapsulator e, Map<Integer, List<Matcher.Constraint>> map) {
        return new Class(e);
    }

    @Override
    public void replaceByRange(List<PsiElement> elements, PsiRewrite r) {
        PsiClass c = az.classDeclaration(elements.get(0));
        if (iz.type(inner)){
            super.replaceByRange(Utils.wrapWithList(JavaPsiFacade.getElementFactory(Utils.getProject()).createTypeElementFromText(c.getName(), c)), r);
        } else {
            super.replaceByRange(elements, r);
        }
    }
}
