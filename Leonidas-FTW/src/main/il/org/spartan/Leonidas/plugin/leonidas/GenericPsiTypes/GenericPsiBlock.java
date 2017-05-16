package il.org.spartan.Leonidas.plugin.leonidas.GenericPsiTypes;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiInvalidElementAccessException;
import il.org.spartan.Leonidas.auxilary_layer.iz;
import il.org.spartan.Leonidas.plugin.leonidas.KeyDescriptionParameters;
import org.jetbrains.annotations.NotNull;

/**
 * Generalizes code block as these inside a method of an if statement - Anything enclosed by "{" and "}".
 * @author Roey Maor, Amir sagiv
 * @since 20-01-2017
 */
public class GenericPsiBlock extends GenericPsi{

    public GenericPsiBlock(PsiElement element) {
        super(element, "GenericBlock");
    }

    @Override
    public boolean generalizes(PsiElement e) {
        return iz.blockStatement(e) || iz.block(e) || iz.statement(e);
    }

    @Override
	@NotNull
	public Project getProject() throws PsiInvalidElementAccessException {
		return inner.getProject();
	}

    @Override
    public String toString() {
        return "GenericBlock" + inner.getUserData(KeyDescriptionParameters.ID);
    }

    @Override
    public GenericPsiBlock copy() {
        PsiElement psiCopy = inner.copy();
        psiCopy.putUserData(KeyDescriptionParameters.ID, inner.getUserData(KeyDescriptionParameters.ID));
        psiCopy.putUserData(KeyDescriptionParameters.NO_OF_STATEMENTS, inner.getUserData(KeyDescriptionParameters.NO_OF_STATEMENTS));
        return new GenericPsiBlock(psiCopy);
    }

}
