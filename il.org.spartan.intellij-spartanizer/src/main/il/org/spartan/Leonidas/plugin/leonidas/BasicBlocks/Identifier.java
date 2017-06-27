package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.auxilary_layer.Utils;
import il.org.spartan.Leonidas.auxilary_layer.az;
import il.org.spartan.Leonidas.auxilary_layer.iz;
import il.org.spartan.Leonidas.plugin.UserControlled;
import il.org.spartan.Leonidas.plugin.UserControlledMatcher;
import il.org.spartan.Leonidas.plugin.UserControlledReplacer;
import il.org.spartan.Leonidas.plugin.leonidas.Matcher;
import il.org.spartan.Leonidas.plugin.leonidas.MatchingResult;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * A basic block representing an identifier. Class0 identifier1 = expression(2);
 * @author Amir Sagiv
 * @Date 24-05-2017
 */
public class Identifier extends NamedElement {
    public static final String TEMPLATE = "identifier";
    @UserControlled(name="must contain: " , templatePart = "Matcher")
    public List<String> containsList = new LinkedList<>(); // present the user a list of strings he wishes the identifier will contain to modify.
    @UserControlled(name="must not contain: " , templatePart = "Matcher")
    public List<String> notContainsList = new LinkedList<>(); // present the user a list of strings he wishes the identifier will not contain to modify.
    @UserControlled(name="change name to: " , templatePart = "Replacer")
    public String nameToChange = ""; // present the user string he wishes the identifier will b replaced by to modify.


    public Identifier(Encapsulator e) {
        super(e, TEMPLATE);
    }

    /**
     * For reflection use DO NOT REMOVE!
     */
    public Identifier() {
        super(TEMPLATE);
    }

    @Override
    protected String getName(PsiElement ¢) {
        return !iz.identifier(¢) ? null : az.identifier(¢).getText();
    }

    @Override
    public boolean conforms(PsiElement ¢) {
        return iz.identifier(¢) && super.conforms(¢);
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
    public GenericEncapsulator create(Encapsulator e, Map<Integer, List<Matcher.Constraint>> m) {
        return new Identifier(e);
    }

    /* Constraints Methods */

    public void contains(String s) {
        containsList.add(s);
        addConstraint((e, m) -> containsList.stream().allMatch(cs -> az.identifier(e.inner).getText().contains(cs)));
    }

    public void notContains(String s) {
        notContainsList.add(s);
        addConstraint((e, m) -> notContainsList.stream().noneMatch(ncs -> az.identifier(e.inner).getText().contains(ncs)));
    }

    public void changeName(String name){
        nameToChange = name;
        addReplacingRule((e, m) -> JavaPsiFacade.getElementFactory(Utils.getProject()).createIdentifier(nameToChange));
    }
}
