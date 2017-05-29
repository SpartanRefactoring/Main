package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.auxilary_layer.az;

/**
 * @author amirsagiv, michalcohen
 * @since 29-05-2017
 */
public abstract class NamedElement extends GenericEncapsulator{

    public NamedElement(Encapsulator e, String template) {
        super(e, template);
    }

    public NamedElement(String template) { this.template = template; }

    @Override
    public boolean conforms(PsiElement e) {
        return getName(e) != null && getName(e).startsWith(template);
    }

    @Override
    public Integer extractId(PsiElement e) {
        return Integer.parseInt(getName(e).substring(template.length()));
    }

    protected abstract String getName(PsiElement e);

    public void startsWith(String s) {
        addConstraint(e -> getName(e.getInner()).startsWith(s));
    }
}
