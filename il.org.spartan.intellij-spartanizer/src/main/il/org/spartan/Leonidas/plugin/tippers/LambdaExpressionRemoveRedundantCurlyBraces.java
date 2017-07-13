package il.org.spartan.Leonidas.plugin.tippers;

import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.java.PsiLambdaExpressionImpl;
import il.org.spartan.Leonidas.auxilary_layer.ExampleMapFactory;
import il.org.spartan.Leonidas.auxilary_layer.PsiRewrite;
import il.org.spartan.Leonidas.auxilary_layer.az;
import il.org.spartan.Leonidas.auxilary_layer.iz;
import il.org.spartan.Leonidas.plugin.tipping.Tip;
import il.org.spartan.Leonidas.plugin.tipping.Tipper;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static il.org.spartan.Leonidas.auxilary_layer.step.*;

/**
 * @author Oren Afek
 * @since 01-12-2016
 */

/*
() -> {sout;} => () -> sout;
() -> {return x;} => () -> x;
 */

public class LambdaExpressionRemoveRedundantCurlyBraces implements Tipper {

    @Override
    public boolean canTip(PsiElement ¢) {
        return ¢ instanceof PsiLambdaExpression && canTip((PsiLambdaExpression) ¢);
    }

    private boolean canTip(PsiLambdaExpression element) {
        return iz.block(element.getBody()) && statements(blockBody(element)).size() == 1 //
                && (isCandidateReturnStatement(element) || isCandidateStatementExpression(element));
    }

    private boolean isCandidateReturnStatement(PsiLambdaExpression element) {
        return iz.returnStatement(firstStatement(blockBody(element)));
    }

    private boolean isCandidateStatementExpression(PsiLambdaExpression element) {
        return iz.expressionStatement(firstStatement(blockBody(element)));
    }

    @NotNull
    @Override
    public String description(PsiElement x) {
        return "Remove redundant curly braces";
    }

    @NotNull
    @Override
    public String name() {
        return "LambdaExpressionRemoveRedundantCurlyBraces";
    }

    @Override
    public Tip tip(final PsiElement e) {
        PsiLambdaExpression element = az.lambdaExpression(e);
        assert statements(blockBody(element)).size() == 1;
        final PsiStatement s = firstStatement(blockBody(element));

        return new Tip(description(element), element, this.getClass()) {
            @Override
            public void go(final PsiRewrite r) {
                if (isCandidateReturnStatement(element))
                    r.replace(element.getBody(), expression((PsiReturnStatement) s));

                if (isCandidateStatementExpression(element))
                    r.replace(element.getBody(), expression((PsiExpressionStatement) s));

            }
        };

    }

    @NotNull
    @Override
    public Class<? extends PsiLambdaExpression> getOperableType() {
        return PsiLambdaExpressionImpl.class;
    }

    @NotNull
    @Override
    public Map<String, String> getExamples() {
        return new ExampleMapFactory()
                .put("() -> {return x;}", "() -> x")
                .map();
    }
}
