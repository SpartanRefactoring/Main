package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.auxilary_layer.Utils;
import il.org.spartan.Leonidas.auxilary_layer.az;
import il.org.spartan.Leonidas.auxilary_layer.haz;
import il.org.spartan.Leonidas.auxilary_layer.iz;
import il.org.spartan.Leonidas.plugin.leonidas.Matcher;
import il.org.spartan.Leonidas.plugin.leonidas.MatchingResult;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * A basic block representing an identifier. Class0 identifier1 = expression(2);
 *
 * @author Amir Sagiv
 * @Date 24-05-2017
 */
public abstract class Identifiable extends NamedElement {

    @UserControlled(name = "must contain: ", templatePart = "Matcher")
    public List<String> containsList = new LinkedList<>(); // present the user a list of strings he wishes the identifier will contain to modify.
    @UserControlled(name = "must not contain: ", templatePart = "Matcher")
    public List<String> notContainsList = new LinkedList<>(); // present the user a list of strings he wishes the identifier will not contain to modify.
    @UserControlled(name = "change name to: ", templatePart = "Replacer")
    public String nameToChange = ""; // present the user string he wishes the identifier will b replaced by to modify.


    protected Identifiable(Encapsulator e, String TEMPLATE) {
        super(e, TEMPLATE);
    }

    protected Identifiable(String TEMPLATE) {
        super(TEMPLATE);

    }

    @Override
    protected String getName(PsiElement e) {
        return !iz.identifier(e) ? null : az.identifier(e).getText();
    }

    @Override
    public boolean conforms(PsiElement e) {
        return iz.identifier(e) && super.conforms(e);
    }

    @Override
    public MatchingResult generalizes(Encapsulator e, Map<Integer, List<PsiElement>> m) {
        return new MatchingResult(super.generalizes(e, m).matches() && iz.identifier(e.getInner()));
    }

    @Override
    protected boolean goUpwards(Encapsulator prev, Encapsulator next) {
        return false;
    }

    @Override
    public abstract GenericEncapsulator create(Encapsulator e, Map<Integer, List<Matcher.Constraint>> m);

    @Override
    public void copyTo(GenericEncapsulator dst) {
        if (!(dst instanceof Identifiable)) return;
        super.copyTo(dst);
        Identifiable castDst = (Identifiable) dst;
        castDst.containsList = new LinkedList(containsList);
        castDst.notContainsList = new LinkedList(notContainsList);
        castDst.nameToChange = String.valueOf(nameToChange);
    }

    /* Constraints Methods */

    public void contains(String s) {
        containsList.add(s);
        addConstraint((e, m) -> containsList.stream().allMatch(cs -> az.identifier(e.inner).getText().contains(cs)));
    }

    public void notContains(String s) {
        notContainsList.add(s);
        addConstraint(
                (e, m) ->
                        notContainsList
                                .stream()
                                .noneMatch(ncs ->
                                        (iz.identifier(e.inner) && (az.identifier(e.inner)
                                                .getText()
                                                .contains(ncs)))));
    }

    public void changeName(String name) {
        nameToChange = name;
        addReplacingRule((e, m) -> JavaPsiFacade.getElementFactory(Utils.getProject()).createIdentifier(nameToChange));
    }

    public void notExpressionStatement() {
        addConstraint((e, m) -> !Utils.inExpressionStatement(e));
    }

    public void noMoreMethodsApply() {
        addConstraint((e, m) -> !haz.moreMethodsCalls(e));
    }
}
