package il.org.spartan.Leonidas.plugin.tippers;

import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiMethodImpl;
import il.org.spartan.Leonidas.auxilary_layer.az;
import il.org.spartan.Leonidas.auxilary_layer.iz;
import il.org.spartan.Leonidas.plugin.tipping.Tip;

/**
 * @author RoeiRaz
 * @since 03-01-2017
 */
public class Delegator extends JavadocMarkerNanoPattern {
    @Override
    protected boolean prerequisites(PsiMethod m) {
        return delegation(m);
    }

    private boolean hasOneStatement(PsiCodeBlock b) {
        return (b != null) && b.getStatements().length == 1;
    }

    private boolean hasOnlyMethodCall(PsiCodeBlock b) {
        return getMethodCallExpression(b) != null;
    }

    private PsiMethodCallExpression getMethodCallExpression(PsiCodeBlock b) {
		if (!hasOneStatement(b))
			return null;
		PsiStatement statement = b.getStatements()[0];
		return iz.returnStatement(statement) && iz.methodCallExpression(az.returnStatement(statement).getReturnValue())
				? az.methodCallExpression(az.returnStatement(statement).getReturnValue())
				: iz.expressionStatement(statement)
						&& iz.methodCallExpression(az.expressionStatement(statement).getExpression())
								? az.methodCallExpression(az.expressionStatement(statement).getExpression()) : null;
	}

    @SuppressWarnings("ConstantConditions")
    private boolean argumentsParametersMatch(PsiParameterList l, PsiExpressionList argumentList) {
        for (PsiExpression expression : argumentList.getExpressions()) {
            if (iz.literal(expression))
				continue;
            if (!iz.referenceExpression(expression))
				return false;
            PsiElement c = az.referenceExpression(expression).getLastChild();
            if (!iz.identifier(c))
				return false;
            PsiIdentifier identifier = az.identifier(c);
            boolean foundMatchingParameter = false;
            for (PsiParameter parameter : l.getParameters())
				if (parameter.getNameIdentifier().getText().equals(identifier.getText())) {
					foundMatchingParameter = true;
					break;
				}
            if (!foundMatchingParameter)
				return false;
        }
        return true;
    }

    private boolean delegation(PsiMethod m) {
		if (!hasOnlyMethodCall(m.getBody()))
			return false;
		PsiMethodCallExpression methodCallExpression = getMethodCallExpression(m.getBody());
		assert methodCallExpression != null;
		return argumentsParametersMatch(m.getParameterList(), methodCallExpression.getArgumentList())
				&& methodCallExpression.getMethodExpression().getQualifierExpression() == null;
	}

    @Override
    protected Tip pattern(PsiMethod ¢) {
        return null;
    }

    @Override
    public String name() {
        return "Delegator";
    }

    @Override
    public Class<PsiMethodImpl> getPsiClass() {
        return PsiMethodImpl.class;
    }
}
