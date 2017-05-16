package spartanizer.LeonidasTippers;

import com.intellij.psi.PsiIfStatement;
import il.org.spartan.Leonidas.plugin.leonidas.GenericPsiElementStub;
import il.org.spartan.Leonidas.plugin.leonidas.Leonidas;

/**
 * Replace if(b){;} else{s;} with if(!b){s;}
 *
 * @author Oren Afek
 * @since 10/01/17
 */
public class IfEmptyThen2 extends GenericPsiElementStub {

    @Leonidas(PsiIfStatement.class)
    public void from(){
        if (!booleanExpression(0))
			statement(1);
    }

    @Leonidas(PsiIfStatement.class)
    public void to(){
        if(!(booleanExpression(0)))
            statement(1);
    }
}
