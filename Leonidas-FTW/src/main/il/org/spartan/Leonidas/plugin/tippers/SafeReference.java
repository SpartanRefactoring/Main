package il.org.spartan.Leonidas.plugin.tippers;

import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiConditionalExpression;
import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.auxilary_layer.az;
import il.org.spartan.Leonidas.auxilary_layer.iz;
import il.org.spartan.Leonidas.plugin.tipping.Tip;

/**
 * This is a safeReference Nano pattern.
 * This tip works only for field accesses and for Method calls with no params.
 *
 * @author amirsagiv
 * @since 23-12-2016
 */
public class SafeReference extends NanoPatternTipper<PsiConditionalExpression> {
    @Override
    public boolean canTip(PsiElement e) {
        return firstScenario(e) || secondScenario(e) || thirdScenario(e) || fourthScenario(e);

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
	public PsiElement createReplacement(PsiConditionalExpression x) {
		return JavaPsiFacade
				.getElementFactory(
						x.getProject())
				.createExpressionFromText(
						"nullConditional(" + (firstScenario(x) || secondScenario(x)
								? iz.referenceExpression(az.conditionalExpression(x).getElseExpression()) ? az.referenceExpression(az.conditionalExpression(x).getElseExpression()).getQualifier()
										.getText() + " , ¢ -> ¢."
										+ az.referenceExpression(az.conditionalExpression(x).getElseExpression())
												.getReferenceNameElement().getText()
										: az.methodCallExpression(az.conditionalExpression(x).getElseExpression())
												.getMethodExpression().getQualifier().getText()
												+ " , ¢ -> ¢."
												+ az.methodCallExpression(
														az.conditionalExpression(x).getElseExpression())
														.getMethodExpression().getReferenceNameElement().getText()
												+ "()"
								: iz.referenceExpression(az.conditionalExpression(x).getThenExpression())
										? az.referenceExpression(az.conditionalExpression(x).getThenExpression())
												.getQualifier().getText()
												+ " , ¢ -> ¢."
												+ az.referenceExpression(
														az.conditionalExpression(x).getThenExpression())
														.getReferenceNameElement().getText()
										: az.methodCallExpression(az.conditionalExpression(x).getThenExpression())
												.getMethodExpression().getQualifier().getText()
												+ " , ¢ -> ¢."
												+ az.methodCallExpression(
														az.conditionalExpression(x).getThenExpression())
														.getMethodExpression().getReferenceNameElement().getText()
												+ "()")
								+ ")",
						x);
	}

    @Override
    public Class<PsiConditionalExpression> getPsiClass() {
        return PsiConditionalExpression.class;
    }

    @SuppressWarnings("ConstantConditions")
    private boolean firstScenario(PsiElement e) {
        return (iz.conditionalExpression(e) && iz.binaryExpression(az.conditionalExpression(e).getCondition())
				&& ("==".equals(
						az.binaryExpression(az.conditionalExpression(e).getCondition()).getOperationSign().getText()))
				&& iz.nullExpression(az.binaryExpression(az.conditionalExpression(e).getCondition()).getROperand())
				&& iz.nullExpression(az.conditionalExpression(e).getThenExpression())
				&& (iz.referenceExpression(az.conditionalExpression(e).getElseExpression())
						&& az.referenceExpression(az.conditionalExpression(e).getElseExpression()).getQualifier()
								.getText()
								.equals(az.binaryExpression(az.conditionalExpression(e).getCondition()).getLOperand()
										.getText())
						|| iz.methodCallExpression(az.conditionalExpression(e).getElseExpression())
								&& az.methodCallExpression(az.conditionalExpression(e).getElseExpression())
										.getMethodExpression().getQualifier().getText()
										.equals(az.binaryExpression(az.conditionalExpression(e).getCondition())
												.getLOperand().getText())
								&& az.methodCallExpression(az.conditionalExpression(e).getElseExpression())
										.getArgumentList().getExpressions().length == 0));
    }

    @SuppressWarnings("ConstantConditions")
    private boolean secondScenario(PsiElement e) {
        return (iz.conditionalExpression(e) && iz.binaryExpression(az.conditionalExpression(e).getCondition())
				&& ("==".equals(
						az.binaryExpression(az.conditionalExpression(e).getCondition()).getOperationSign().getText()))
				&& iz.nullExpression(az.binaryExpression(az.conditionalExpression(e).getCondition()).getLOperand())
				&& iz.nullExpression(az.conditionalExpression(e).getThenExpression())
				&& (iz.referenceExpression(az.conditionalExpression(e).getElseExpression())
						&& az.referenceExpression(az.conditionalExpression(e).getElseExpression()).getQualifier()
								.getText()
								.equals(az.binaryExpression(az.conditionalExpression(e).getCondition()).getROperand()
										.getText())
						|| iz.methodCallExpression(az.conditionalExpression(e).getElseExpression())
								&& az.methodCallExpression(az.conditionalExpression(e).getElseExpression())
										.getMethodExpression().getQualifier().getText()
										.equals(az.binaryExpression(az.conditionalExpression(e).getCondition())
												.getROperand().getText())
								&& az.methodCallExpression(az.conditionalExpression(e).getElseExpression())
										.getArgumentList().getExpressions().length == 0));
    }

    @SuppressWarnings("ConstantConditions")
    private boolean thirdScenario(PsiElement e) {
        return (iz.conditionalExpression(e) && iz.binaryExpression(az.conditionalExpression(e).getCondition())
				&& ("!=".equals(
						az.binaryExpression(az.conditionalExpression(e).getCondition()).getOperationSign().getText()))
				&& iz.nullExpression(az.binaryExpression(az.conditionalExpression(e).getCondition()).getROperand())
				&& iz.nullExpression(az.conditionalExpression(e).getElseExpression())
				&& (iz.referenceExpression(az.conditionalExpression(e).getThenExpression())
						&& az.referenceExpression(az.conditionalExpression(e).getThenExpression()).getQualifier()
								.getText()
								.equals(az.binaryExpression(az.conditionalExpression(e).getCondition()).getLOperand()
										.getText())
						|| iz.methodCallExpression(az.conditionalExpression(e).getThenExpression())
								&& az.methodCallExpression(az.conditionalExpression(e).getThenExpression())
										.getMethodExpression().getQualifier().getText()
										.equals(az.binaryExpression(az.conditionalExpression(e).getCondition())
												.getLOperand().getText())
								&& az.methodCallExpression(az.conditionalExpression(e).getThenExpression())
										.getArgumentList().getExpressions().length == 0));
    }

    @SuppressWarnings("ConstantConditions")
    private boolean fourthScenario(PsiElement e) {
        return (iz.conditionalExpression(e) && iz.binaryExpression(az.conditionalExpression(e).getCondition())
				&& ("!=".equals(
						az.binaryExpression(az.conditionalExpression(e).getCondition()).getOperationSign().getText()))
				&& iz.nullExpression(az.binaryExpression(az.conditionalExpression(e).getCondition()).getLOperand())
				&& iz.nullExpression(az.conditionalExpression(e).getElseExpression())
				&& (iz.referenceExpression(az.conditionalExpression(e).getThenExpression())
						&& az.referenceExpression(az.conditionalExpression(e).getThenExpression()).getQualifier()
								.getText()
								.equals(az.binaryExpression(az.conditionalExpression(e).getCondition()).getROperand()
										.getText())
						|| iz.methodCallExpression(az.conditionalExpression(e).getThenExpression())
								&& az.methodCallExpression(az.conditionalExpression(e).getThenExpression())
										.getMethodExpression().getQualifier().getText()
										.equals(az.binaryExpression(az.conditionalExpression(e).getCondition())
												.getROperand().getText())
								&& az.methodCallExpression(az.conditionalExpression(e).getThenExpression())
										.getArgumentList().getExpressions().length == 0));
    }

    @Override
    protected Tip pattern(final PsiConditionalExpression ¢) {
        return tip(¢);
    }

}