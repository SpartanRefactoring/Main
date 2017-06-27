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
     * @param ¢ JD
     * @return true iff e is in the form: <boolean expression> ? null : <expression>
     */
    @Override
    public boolean canTip(PsiElement ¢) {
        return (iz.conditionalExpression(¢) && (iz.nullExpression(az.conditionalExpression(¢).getThenExpression())));
    }

    @Override
    public String description() {
        return "Change conditional expression to fluent Unless";
    }

    @Override
    public String description(PsiConditionalExpression ¢) {
        return "Change " + ¢.getText() + " to fluent eval().unless()";
    }

    /**
	 * @param ¢  - the element to be replaced
	 * @return  a method invocation to unless function.
	 */
	@Override
	@SuppressWarnings("ConstantConditions")
	public PsiElement createReplacement(PsiConditionalExpression ¢) {
		return JavaPsiFacade.getElementFactory(¢.getProject()).createExpressionFromText(
				"eval(" + ¢.getElseExpression().getText() + ").unless( " + ¢.getCondition().getText() + ")", ¢);
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
        Map<String,String> $ = new HashMap<>();
        $.put("x>6 ? null : x;","eval(x).unless(x>6);");
        $.put("x>6 ? x : null",null);
        $.put("6==6 ? null : x","eval(x).unless(6==6);");
        return $;
    }
}
