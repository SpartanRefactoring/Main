package il.org.spartan.Leonidas.plugin.leonidas.GenericPsiTypes;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiStatement;
import il.org.spartan.Leonidas.auxilary_layer.iz;
import il.org.spartan.Leonidas.plugin.leonidas.KeyDescriptionParameters;

/**
 * Generalizes any kind of statement. For example: "System.out.print(...);", "id--;", "foo();", "if(true) x++;".
 * @author michalcohen
 * @since 11-01-17
 */
public class GenericPsiStatement extends GenericPsi implements PsiStatement {

    public GenericPsiStatement(PsiElement inner) {
        super(inner, "GenericStatement");
    }

    @Override
    public String toString() {
        return "GenericStatement" + inner.getUserData(KeyDescriptionParameters.ID);
    }

    @Override
    public boolean generalizes(PsiElement e) {
        return iz.statement(e) && !iz.blockStatement(e);
    }

    @Override
    public GenericPsiStatement copy() {
        PsiElement psiCopy = inner.copy();
        psiCopy.putUserData(KeyDescriptionParameters.ID, inner.getUserData(KeyDescriptionParameters.ID));
        return new GenericPsiStatement(psiCopy);
    }

}
