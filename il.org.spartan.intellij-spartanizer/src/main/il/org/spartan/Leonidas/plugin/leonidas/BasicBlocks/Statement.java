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
@SuppressWarnings("Duplicates")
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
        addConstraint(e -> countReferences(e, s) == 0);
    }

    private int countReferences(Encapsulator e, Integer id, Map<Integer, List<PsiElement>> m) {
        Wrapper<Integer> wi = new Wrapper<>(0);
        e.accept(n -> {
            if (iz.identifier(n.getInner()) && az.identifier(n.getInner()).getText().equals(m.get(id).get(0).getText()))
                wi.set(wi.get() + 1);
        });
        return wi.get();
    }

    private int countReferences(Encapsulator e, String s) {
        Wrapper<Integer> wi = new Wrapper<>(0);
        e.accept(n -> {
            if (iz.identifier(n.getInner()) && az.identifier(n.getInner()).getText().equals(s))
                wi.set(wi.get() + 1);
        });
        return wi.get();
    }

    /**
     * Will accepts only if not contains identifier
     */
    public void mustNotRefer(Integer id) {
        addConstraint((e, m) -> countReferences(e, id, m) == 0);
    }

    /**
     * Will accept only if this statement is not a declaration statement.
     */
    public void isNotDeclarationStatement() {
        addConstraint(e -> !iz.declarationStatement(e.inner));
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
    /*Impl: replacing generic element with an another generic element
      [for example: changing expression(0) with expression(1)
       will make a call: replaceIdentifiers(0,1)
    */
    public void replaceIdentifiers(Integer from, Integer to) {
        addReplacingRule((e, map) -> {
            e.accept(new JavaRecursiveElementVisitor() {
                @Override
                public void visitIdentifier(PsiIdentifier identifier) {
                    super.visitIdentifier(identifier);
                    if (identifier.getText().equals(map.get(from).get(0).getText())) {
                        PsiRewrite prr = new PsiRewrite();
                        //Impl: assuming that map.get(to) is a Singleton List
                        prr.replace(identifier, map.get(to).get(0));
                    }
                }
            });
            return e;
        });
    }

    public void refersOnce(Integer ¢) {
        addConstraint((e, m) -> countReferences(e, ¢, m) == 1);
    }
}
