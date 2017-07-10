package il.org.spartan.Leonidas.plugin.tippers;

import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.java.PsiConditionalExpressionImpl;
import com.intellij.psi.tree.IElementType;
import il.org.spartan.Leonidas.auxilary_layer.az;
import il.org.spartan.Leonidas.auxilary_layer.haz;
import il.org.spartan.Leonidas.auxilary_layer.iz;
import il.org.spartan.Leonidas.auxilary_layer.step;
import il.org.spartan.Leonidas.plugin.tipping.Tip;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Replace X != null ? X : Y with defaults(X).to(Y) <br>
 * Replace X == null ? Y : X with defaults(X).to(Y) <br>
 * Replace null == X ? Y : X with defaults(X).to(Y) <br>
 * Replace null != X ? X : Y with defaults(X).to(Y) <br>
 *
 * @author Oren Afek
 * @since 24-12-2016
 */

public class DefaultsTo extends NanoPatternTipper {

    @Override
    public boolean canTip(PsiElement e) {

        if (!iz.conditionalExpression(e) || !iz.binaryExpression(step.conditionExpression(az.conditionalExpression(e))))
			return false;
        PsiConditionalExpression conditionalExpression = az.conditionalExpression(e);
        PsiBinaryExpression condition = az.binaryExpression(step.conditionExpression(conditionalExpression));
        return (haz.equalsOperator(condition) || haz.notEqualsOperator(condition)) &&
                isExactlyOneOfTheArgsNull(step.leftOperand(condition), step.rightOperand(condition)) &&
                areOperandsEqualsToBranches(conditionalExpression);

    }

    private boolean isExactlyOneOfTheArgsNull(PsiExpression lArg, PsiExpression rArg) {
        return (iz.null$(lArg) && iz.notNull(rArg)) || (iz.null$(rArg) && iz.notNull(lArg));
    }

    private boolean areOperandsEqualsToBranches(PsiConditionalExpression x) {
        PsiBinaryExpression condition = az.binaryExpression(step.conditionExpression(x));
        IElementType operator = step.operator(condition);
        PsiExpression lOp = step.leftOperand(condition), rOp = step.rightOperand(condition),
				thenExpr = step.thenExpression(x), elseExpr = step.elseExpression(x);
        return (iz.notNull(lOp) && ((iz.equalsOperator(operator) && lOp.getText().equals(elseExpr.getText())) ||
                (iz.notEqualsOperator(operator)) && lOp.getText().equals(thenExpr.getText()))) ||
                (iz.null$(lOp) && ((iz.equalsOperator(operator) && rOp.getText().equals(elseExpr.getText())) ||
                        (iz.notEqualsOperator(operator)) && rOp.getText().equals(thenExpr.getText())));
    }

    @NotNull
    @Override
    public String description(PsiElement x) {
        return "Change to defaults-to syntax";
    }

    @NotNull
    @Override
    public String name() {
        return "DefaultsTo";
    }

    private boolean eqOperator(PsiConditionalExpression x) {
        return iz.equalsOperator(step.operator(az.binaryExpression(step.conditionExpression(x))));
    }

    @NotNull
    @Override
    @SuppressWarnings("ConstantConditions")
    public PsiElement createReplacement(PsiElement e) {
        PsiConditionalExpression x = az.conditionalExpression(e);
        return JavaPsiFacade.getElementFactory(x.getProject()).createExpressionFromText("defaults(" + (eqOperator(x) ? x.getElseExpression() : x.getThenExpression()).getText() + ").to("
                + (eqOperator(x) ? x.getThenExpression() : x.getElseExpression()).getText() + ")", x);
    }

    @NotNull
    @Override
    protected Tip pattern(PsiElement ¢) {
        return null;
    }

    @NotNull
    @Override
    public Class<PsiConditionalExpressionImpl> getOperableType() {
        return PsiConditionalExpressionImpl.class;
    }

    @NotNull
    @Override
    public Map<String,String> getExamples(){
        Map<String, String> examples = new HashMap<>();
        examples.put("x != null ? x : y", "defaults(x).to(y)");
        examples.put("x == null ? y : x", "defaults(x).to(y)");
        examples.put("null == x ? y : x", "defaults(x).to(y)");
        examples.put("null != x ? x : y", "defaults(x).to(y)");
        examples.put("null == null ? x : y", null); //<should not be able to tip
        examples.put("null != null ? x : y", null);
        examples.put("x == y ? x : y", null);
        examples.put("x != null ? y : z", null);
        examples.put("x != null ? y : x", null);
        examples.put("x == null ? x : y", null);
        examples.put("null == x ? x : y", null);
        examples.put("null != x ? y : x", null);
        return examples;
    }
}