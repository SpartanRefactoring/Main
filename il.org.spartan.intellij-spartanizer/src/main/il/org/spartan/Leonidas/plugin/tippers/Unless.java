package il.org.spartan.Leonidas.plugin.tippers;

import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiConditionalExpression;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.java.PsiConditionalExpressionImpl;
import il.org.spartan.Leonidas.auxilary_layer.az;
import il.org.spartan.Leonidas.auxilary_layer.iz;
import il.org.spartan.Leonidas.plugin.tipping.Tip;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * @author michal cohen
 * @since 22-12-2016
 */
public class Unless extends NanoPatternTipper {

    /**
     * @param e JD
     * @return true iff e is in the form: <boolean expression> ? null : <expression>
     */
    @Override
    public boolean canTip(PsiElement e) {
        return (iz.conditionalExpression(e) && (iz.nullExpression(az.conditionalExpression(e).getThenExpression())));
    }

    @NotNull
    @Override
    public String description() {
        return "Change conditional expression to fluent Unless";
    }

    @NotNull
    @Override
    public String description(PsiElement x) {
        return "Change " + x.getText() + " to fluent eval().unless()";
    }

    /**
     * @param e  - the element to be replaced
     * @return  a method invocation to unless function.
	 */
	@Override
	@SuppressWarnings("ConstantConditions")
    public PsiElement createReplacement(PsiElement e) {
        PsiConditionalExpression x = az.conditionalExpression(e);
        return JavaPsiFacade.getElementFactory(x.getProject()).createExpressionFromText(
                "eval(" + x.getElseExpression().getText() + ").unless( " + x.getCondition().getText() + ")", x);
    }

    @NotNull
    @Override
    public Class<? extends PsiConditionalExpression> getOperableType() {
        return PsiConditionalExpressionImpl.class;
    }

    @NotNull
    @Override
    protected Tip pattern(final PsiElement ¢) {
        return tip(¢);
    }

    @NotNull
    @Override
    public String name() {
        return "Unless";
    }

    @NotNull
    @Override
    public Map<String,String> getExamples(){
        Map<String, String> examples = new HashMap<>();
        examples.put("x>6 ? null : x;", "eval(x).unless(x>6);");
        examples.put("x>6 ? x : null", null);
        examples.put("6==6 ? null : x", "eval(x).unless(6==6)");
        return examples;
    }
}
