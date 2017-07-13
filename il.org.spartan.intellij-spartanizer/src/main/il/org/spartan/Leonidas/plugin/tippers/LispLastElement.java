package il.org.spartan.Leonidas.plugin.tippers;

import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.java.PsiMethodCallExpressionImpl;
import com.intellij.psi.util.PsiTreeUtil;
import il.org.spartan.Leonidas.auxilary_layer.ExampleMapFactory;
import il.org.spartan.Leonidas.auxilary_layer.az;
import il.org.spartan.Leonidas.auxilary_layer.iz;
import il.org.spartan.Leonidas.plugin.tipping.Tip;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * @author AnnaBel7
 * @since 23-12-2016
 */

public class LispLastElement extends NanoPatternTipper {

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

    @NotNull
    @Override
    public String description(PsiElement x) {
        return "replace " + x.getText() + " with list last";
    }

    @NotNull
    @Override
    public PsiElement createReplacement(PsiElement e) {
        PsiMethodCallExpression x = az.methodCallExpression(e);
        @SuppressWarnings("ConstantConditions") PsiReferenceExpression container = PsiTreeUtil.getChildrenOfType(x.getMethodExpression(), PsiReferenceExpression.class)[0];
        return JavaPsiFacade.getElementFactory(x.getProject()).createExpressionFromText("last(" + container.getText() + ")", x);
    }

    @NotNull
    @Override
    protected Tip pattern(PsiElement ¢) {
        return tip(¢);
    }

    @NotNull
    @Override
    public Class<? extends PsiMethodCallExpression> getOperableType() {
        return PsiMethodCallExpressionImpl.class;
    }

    @NotNull
    @Override
    public String name() {
        return "LispLastElement";
    }

    @NotNull
    @Override
    public Map<String, String> getExamples() {
        return new ExampleMapFactory()
                .put("l.get(l.size()-1);", "last(l);")
                .put("l.get(l.size())", null)
                .put("l.get(l.size()-2)", null)
                .put("l.get(l2.size())", null)
                .map();
    }

}
