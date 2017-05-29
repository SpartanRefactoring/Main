package il.org.spartan.Leonidas.plugin.tippers;

import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.java.PsiMethodCallExpressionImpl;
import com.intellij.psi.util.PsiTreeUtil;
import il.org.spartan.Leonidas.auxilary_layer.az;
import il.org.spartan.Leonidas.auxilary_layer.iz;
import il.org.spartan.Leonidas.plugin.tipping.Tip;

/**
 * @author AnnaBel7
 * @since 23-12-2016
 */

/*
List<int> l = new List<>();
l.get(l.size()-1) -> last(l);
 */
public class LispLastElement extends NanoPatternTipper<PsiMethodCallExpression> {

    @SuppressWarnings("ConstantConditions")
    private boolean canTip(PsiMethodCallExpression x) {
        PsiReferenceExpression[] outerReference = PsiTreeUtil.getChildrenOfType(x.getMethodExpression(),
                PsiReferenceExpression.class);
        PsiIdentifier[] outerIdentifier = PsiTreeUtil.getChildrenOfType(x.getMethodExpression(), PsiIdentifier.class);
        if (outerReference == null || outerIdentifier == null || outerReference.length != 1
                || outerIdentifier.length != 1 || !"get".equals(outerIdentifier[0].getText()))
            return false;
        PsiExpression[] arguments = x.getArgumentList().getExpressions();
        if (arguments.length != 1 || !iz.binaryExpression(arguments[0]))
            return false;
        PsiReferenceExpression[] innerReference = PsiTreeUtil.getChildrenOfType(
                az.methodCallExpression(az.binaryExpression(arguments[0]).getLOperand()).getMethodExpression(),
                PsiReferenceExpression.class);
        PsiIdentifier[] innerIdentifier = PsiTreeUtil.getChildrenOfType(
                az.methodCallExpression(az.binaryExpression(arguments[0]).getLOperand()).getMethodExpression(),
                PsiIdentifier.class);
        return innerIdentifier != null && innerReference != null
                && outerReference[0].getText().equals(innerReference[0].getText())
                && "size".equals(innerIdentifier[0].getText())
                && "1".equals(az.binaryExpression(arguments[0]).getROperand().getText())
                && "-".equals(az.binaryExpression(arguments[0]).getOperationSign().getText());
    }

    @Override
    public boolean canTip(PsiElement e) {
        return iz.methodCallExpression(e) && canTip(az.methodCallExpression(e));
    }

    @Override
    public String description(PsiMethodCallExpression x) {
        return "replace " + x.getText() + " with list last";
    }

    @Override
    public PsiElement createReplacement(PsiMethodCallExpression x) {
        @SuppressWarnings("ConstantConditions") PsiReferenceExpression container = PsiTreeUtil.getChildrenOfType(x.getMethodExpression(), PsiReferenceExpression.class)[0];
        return JavaPsiFacade.getElementFactory(x.getProject()).createExpressionFromText("last(" + container.getText() + ")", x);
    }

    @Override
    protected Tip pattern(PsiMethodCallExpression ¢) {
        return tip(¢);
    }

    @Override
    public Class<? extends PsiMethodCallExpression> getPsiClass() {
        return PsiMethodCallExpressionImpl.class;
    }

    @Override
    public String name() {
        return "LispLastElement";
    }
}
