package il.org.spartan.Leonidas.plugin.leonidas.GenericPsiTypes;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiType;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.tree.IElementType;
import il.org.spartan.Leonidas.auxilary_layer.iz;
import il.org.spartan.Leonidas.plugin.leonidas.KeyDescriptionParameters;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Generalizes any kind of expression. For example: "x >= 4", "id--", "5 + 3".
 * @author michalcohen
 * @since 11-01-2017
 */
public class GenericPsiExpression extends GenericPsi {
    private PsiType t;

    public GenericPsiExpression(PsiType t, PsiElement e) {
        super(e, "GenericExpression");
        this.t = t;
    }

    @Override
    public boolean processDeclarations(@NotNull PsiScopeProcessor p, @NotNull ResolveState s, @Nullable PsiElement lastParent, @NotNull PsiElement place) {
        return false;
    }

    @Override
    public String toString() {
        return "Generic expression" + inner.getUserData(KeyDescriptionParameters.ID);
    }

    public PsiType evaluationType() {
        return t;
    }

    @Override
    public boolean generalizes(PsiElement e) {
        return iz.expression(e);
    }

    @Override
    public IElementType getTokenType() {
        return getElementType();
    }

    @Override
    public GenericPsiExpression copy() {
        PsiElement psiCopy = inner.copy();
        psiCopy.putUserData(KeyDescriptionParameters.ID, inner.getUserData(KeyDescriptionParameters.ID));
        return new GenericPsiExpression(t, psiCopy);
    }

}
