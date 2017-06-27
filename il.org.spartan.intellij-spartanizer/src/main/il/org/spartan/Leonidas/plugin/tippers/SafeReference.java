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
 * This is a safeReference Nano pattern.
 * This tip works only for field accesses and for Method calls with no params.
 *
 * @author amirsagiv
 * @since 23-12-2016
 */
public class SafeReference extends NanoPatternTipper<PsiConditionalExpression> {
    @Override
    public boolean canTip(PsiElement ¢) {
        return firstScenario(¢) || secondScenario(¢) || thirdScenario(¢) || fourthScenario(¢);

    }

    @Override
    public String description(PsiConditionalExpression x) {
        return
				"Replace:\n" +
				"	x == null ? x.y : null\n" +
				"With:\n" +
				"	nullConditional(x ,  ¢ -> ¢.y)";
    }

	@Override
	public String description() {
		return
				"Replace:\n" +
						"	x == null ? x.y : null\n" +
						"With:\n" +
						"	nullConditional(x ,  ¢ -> ¢.y)";
	}

    @Override
	@SuppressWarnings("ConstantConditions")
	public PsiElement createReplacement(PsiConditionalExpression ¢) {
		return JavaPsiFacade
				.getElementFactory(
						¢.getProject())
				.createExpressionFromText(
						"nullConditional(" + (firstScenario(¢) || secondScenario(¢)
								? iz.referenceExpression(az.conditionalExpression(¢).getElseExpression()) ? az.referenceExpression(az.conditionalExpression(¢).getElseExpression()).getQualifier()
										.getText() + " , ¢ -> ¢."
										+ az.referenceExpression(az.conditionalExpression(¢).getElseExpression())
												.getReferenceNameElement().getText()
										: az.methodCallExpression(az.conditionalExpression(¢).getElseExpression())
												.getMethodExpression().getQualifier().getText()
												+ " , ¢ -> ¢."
												+ az.methodCallExpression(
														az.conditionalExpression(¢).getElseExpression())
														.getMethodExpression().getReferenceNameElement().getText()
												+ "()"
								: iz.referenceExpression(az.conditionalExpression(¢).getThenExpression())
										? az.referenceExpression(az.conditionalExpression(¢).getThenExpression())
												.getQualifier().getText()
												+ " , ¢ -> ¢."
												+ az.referenceExpression(
														az.conditionalExpression(¢).getThenExpression())
														.getReferenceNameElement().getText()
										: az.methodCallExpression(az.conditionalExpression(¢).getThenExpression())
												.getMethodExpression().getQualifier().getText()
												+ " , ¢ -> ¢."
												+ az.methodCallExpression(
														az.conditionalExpression(¢).getThenExpression())
														.getMethodExpression().getReferenceNameElement().getText()
												+ "()")
								+ ")",
						¢);
	}

    @Override
	public Class<? extends PsiConditionalExpression> getOperableType() {
		return PsiConditionalExpressionImpl.class;
    }

    @SuppressWarnings("ConstantConditions")
    private boolean firstScenario(PsiElement ¢) {
        return (iz.conditionalExpression(¢) && iz.binaryExpression(az.conditionalExpression(¢).getCondition())
				&& ("==".equals(
						az.binaryExpression(az.conditionalExpression(¢).getCondition()).getOperationSign().getText()))
				&& iz.nullExpression(az.binaryExpression(az.conditionalExpression(¢).getCondition()).getROperand())
				&& iz.nullExpression(az.conditionalExpression(¢).getThenExpression())
				&& (iz.referenceExpression(az.conditionalExpression(¢).getElseExpression())
						&& az.referenceExpression(az.conditionalExpression(¢).getElseExpression()).getQualifier()
								.getText()
								.equals(az.binaryExpression(az.conditionalExpression(¢).getCondition()).getLOperand()
										.getText())
						|| iz.methodCallExpression(az.conditionalExpression(¢).getElseExpression())
								&& az.methodCallExpression(az.conditionalExpression(¢).getElseExpression())
										.getMethodExpression().getQualifier().getText()
										.equals(az.binaryExpression(az.conditionalExpression(¢).getCondition())
												.getLOperand().getText())
								&& az.methodCallExpression(az.conditionalExpression(¢).getElseExpression())
										.getArgumentList().getExpressions().length == 0));
    }

    @SuppressWarnings("ConstantConditions")
    private boolean secondScenario(PsiElement ¢) {
        return (iz.conditionalExpression(¢) && iz.binaryExpression(az.conditionalExpression(¢).getCondition())
				&& ("==".equals(
						az.binaryExpression(az.conditionalExpression(¢).getCondition()).getOperationSign().getText()))
				&& iz.nullExpression(az.binaryExpression(az.conditionalExpression(¢).getCondition()).getLOperand())
				&& iz.nullExpression(az.conditionalExpression(¢).getThenExpression())
				&& (iz.referenceExpression(az.conditionalExpression(¢).getElseExpression())
						&& az.referenceExpression(az.conditionalExpression(¢).getElseExpression()).getQualifier()
								.getText()
								.equals(az.binaryExpression(az.conditionalExpression(¢).getCondition()).getROperand()
										.getText())
						|| iz.methodCallExpression(az.conditionalExpression(¢).getElseExpression())
								&& az.methodCallExpression(az.conditionalExpression(¢).getElseExpression())
										.getMethodExpression().getQualifier().getText()
										.equals(az.binaryExpression(az.conditionalExpression(¢).getCondition())
												.getROperand().getText())
								&& az.methodCallExpression(az.conditionalExpression(¢).getElseExpression())
										.getArgumentList().getExpressions().length == 0));
    }

    @SuppressWarnings("ConstantConditions")
    private boolean thirdScenario(PsiElement ¢) {
        return (iz.conditionalExpression(¢) && iz.binaryExpression(az.conditionalExpression(¢).getCondition())
				&& ("!=".equals(
						az.binaryExpression(az.conditionalExpression(¢).getCondition()).getOperationSign().getText()))
				&& iz.nullExpression(az.binaryExpression(az.conditionalExpression(¢).getCondition()).getROperand())
				&& iz.nullExpression(az.conditionalExpression(¢).getElseExpression())
				&& (iz.referenceExpression(az.conditionalExpression(¢).getThenExpression())
						&& az.referenceExpression(az.conditionalExpression(¢).getThenExpression()).getQualifier()
								.getText()
								.equals(az.binaryExpression(az.conditionalExpression(¢).getCondition()).getLOperand()
										.getText())
						|| iz.methodCallExpression(az.conditionalExpression(¢).getThenExpression())
								&& az.methodCallExpression(az.conditionalExpression(¢).getThenExpression())
										.getMethodExpression().getQualifier().getText()
										.equals(az.binaryExpression(az.conditionalExpression(¢).getCondition())
												.getLOperand().getText())
								&& az.methodCallExpression(az.conditionalExpression(¢).getThenExpression())
										.getArgumentList().getExpressions().length == 0));
    }

    @SuppressWarnings("ConstantConditions")
    private boolean fourthScenario(PsiElement ¢) {
        return (iz.conditionalExpression(¢) && iz.binaryExpression(az.conditionalExpression(¢).getCondition())
				&& ("!=".equals(
						az.binaryExpression(az.conditionalExpression(¢).getCondition()).getOperationSign().getText()))
				&& iz.nullExpression(az.binaryExpression(az.conditionalExpression(¢).getCondition()).getLOperand())
				&& iz.nullExpression(az.conditionalExpression(¢).getElseExpression())
				&& (iz.referenceExpression(az.conditionalExpression(¢).getThenExpression())
						&& az.referenceExpression(az.conditionalExpression(¢).getThenExpression()).getQualifier()
								.getText()
								.equals(az.binaryExpression(az.conditionalExpression(¢).getCondition()).getROperand()
										.getText())
						|| iz.methodCallExpression(az.conditionalExpression(¢).getThenExpression())
								&& az.methodCallExpression(az.conditionalExpression(¢).getThenExpression())
										.getMethodExpression().getQualifier().getText()
										.equals(az.binaryExpression(az.conditionalExpression(¢).getCondition())
												.getROperand().getText())
								&& az.methodCallExpression(az.conditionalExpression(¢).getThenExpression())
										.getArgumentList().getExpressions().length == 0));
    }

    @Override
    protected Tip pattern(final PsiConditionalExpression ¢) {
        return tip(¢);
    }

	@Override
	public String name() {
		return "SafeReference";
	}

	@Override
	public Map<String,String> getExamples(){
		Map<String,String> $ = new HashMap<>();
		$.put("x == null ? null : x.y","nullConditional(x , ¢ -> ¢.y)");
		$.put("null == x ? null : x.y","nullConditional(x , ¢ -> ¢.y)");
		$.put("x != null ? x.y : null","nullConditional(x , ¢ -> ¢.y)");
		$.put("null != x ? x.y : null","nullConditional(x , ¢ -> ¢.y)");
		$.put("x == null ? null : null",null);
		$.put("x == null ? x.y : null",null);
		$.put("x != null ? null : x.y",null);
		$.put("x != null ? null : null",null);
		$.put("y != null ? x.y: null",null);
		$.put("null < x ? x.y: null",null);
		$.put("null == x ? null : x.y()","nullConditional(x , ¢ -> ¢.y())");
		$.put("x == null ? null : x.y(p1)",null);

		return $;
	}
}