package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.JavaRecursiveElementVisitor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiIdentifier;
import il.org.spartan.Leonidas.auxilary_layer.*;
import il.org.spartan.Leonidas.plugin.leonidas.Matcher;
import il.org.spartan.Leonidas.plugin.leonidas.MatchingResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Oren Afek
 * @since 5/3/2017.
 */
public class Statement extends GenericMethodCallBasedBlock {

    private static final String TEMPLATE = "statement";

    private Map<Integer, String> replacingIdentifier = new HashMap<>();

    public Statement(PsiElement e) {
        super(e, TEMPLATE);
    }

    public Statement(Encapsulator n) {
        super(n, TEMPLATE);
    }

    /**
     * For reflection use DO NOT REMOVE!
     */
    @SuppressWarnings("unused")
    protected Statement() {
        super(TEMPLATE);
    }

    @Override
    public MatchingResult generalizes(Encapsulator e, Map<Integer, List<PsiElement>> m) {
        return super.generalizes(e, m).combineWith(new MatchingResult(iz.statement(e.getInner()) && !iz.blockStatement(e.getInner())));
    }

    @Override
    protected boolean goUpwards(Encapsulator prev, Encapsulator next) {
        return !prev.getText().endsWith(";") && (prev.getText().equals(next.getText()) || next.getText().equals(prev.getText() + ";"));
    }

    @Override
    public GenericEncapsulator create(Encapsulator e, Map<Integer, List<Matcher.Constraint>> map) {
        return new Statement(e);
    }


    /**
     * Will accepts only if not contains identifier
     */
    public void mustNotRefer(String s) {
        addConstraint((e, m) -> {
            Wrapper<Boolean> wb = new Wrapper<>(true);
            e.accept(n -> {
                if (iz.identifier(n.getInner()) && az.identifier(n.getInner()).getText().equals(s))
                    wb.set(false);
            });
            return wb.get();
        });
    }

    /**
     * Will accepts only if not contains identifier
     */
    public void mustNotRefer(Integer id) {
        addConstraint((e, m) -> {
            Wrapper<Boolean> wb = new Wrapper<>(true);
            e.accept(n -> {
                if (iz.identifier(n.getInner()) && az.identifier(n.getInner()).getText().equals(m.get(id).get(0).getText()))
                    wb.set(false);
            });
            return wb.get();
        });
    }

    public void replaceIdentifiers(Integer id, String to) {
        replacingIdentifier.put(id, to);
        addReplacingRule((e, map) -> {
            e.accept(new JavaRecursiveElementVisitor() {
                @Override
                public void visitIdentifier(PsiIdentifier identifier) {
                    super.visitIdentifier(identifier);
                    if (identifier.getText().equals(map.get(id).get(0).getText())) {
                        PsiRewrite prr = new PsiRewrite();
                        prr.replace(identifier, JavaPsiFacade.getElementFactory(Utils.getProject()).createIdentifier(replacingIdentifier.get(id)));
                    }
                }
            });
            return e;
        });
    }
}
