package il.org.spartan.Leonidas.plugin.tippers;

import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiConditionalExpression;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.java.PsiConditionalExpressionImpl;
import il.org.spartan.Leonidas.auxilary_layer.az;
import il.org.spartan.Leonidas.auxilary_layer.iz;
import il.org.spartan.Leonidas.plugin.tipping.Tip;

import java.util.HashMap;
import java.util.Map;

/**
 * @author michal cohen
 * @since 22-12-2016
 */
public class Unless extends NanoPatternTipper<PsiConditionalExpression> {

    /**
     * @param e JD
     * @return true iff e is in the form: <boolean expression> ? null : <expression>
     */
    @Override
    public boolean canTip(PsiElement e) {
        return (iz.conditionalExpression(e) && (iz.nullExpression(az.conditionalExpression(e).getThenExpression())));
    }

    @Override
    public String description() {
        return "Change conditional expression to fluent Unless";
    }

    @Override
    public String description(PsiConditionalExpression x) {
        return "Change " + x.getText() + " to fluent eval().unless()";
    }

    /**
     * @param x  - the element to be replaced
     * @return  a method invocation to unless function.
	 */
	@Override
	@SuppressWarnings("ConstantConditions")
    public PsiElement createReplacement(PsiConditionalExpression x) {
        return JavaPsiFacade.getElementFactory(x.getProject()).createExpressionFromText(
                "eval(" + x.getElseExpression().getText() + ").unless( " + x.getCondition().getText() + ")", x);
    }

    @Override
    public Class<? extends PsiConditionalExpression> getOperableType() {
        return PsiConditionalExpressionImpl.class;
    }

    @Override
    protected Tip pattern(final PsiConditionalExpression ¢) {
        return tip(¢);
    }

    @Override
    public String name() {
        return "Unless";
    }

    @Override
    public Map<String,String> getExamples(){
        Map<String, String> examples = new HashMap<>();
        examples.put("x>6 ? null : x;", "eval(x).unless(x>6);");
        examples.put("x>6 ? x : null", null);
        examples.put("6==6 ? null : x", "eval(x).unless(6==6)");
        return examples;
    }
}
