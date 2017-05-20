package spartanizer.FutureLeonidasTippers;

import com.intellij.psi.PsiMethodCallExpression;
import il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub;
import il.org.spartan.Leonidas.plugin.leonidas.Leonidas;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Replace stream.[...].collect(); return !l.isEmpty() ? l.get(0) : null with
 * return stream().[...].findAny().orElseGet(null);
 *
 * @author Oren Afek
 * @since 10/01/17
 */
public class SeekForOneElementInStreamFix extends GenericPsiElementStub {

    @Leonidas(PsiMethodCallExpression.class)
    public void from(){
        List<Object> l = Arrays.stream(arrayIdentifier(0))
                .streamMethodInvocations(1)
                .collect(Collectors.toList());
                return l.isEmpty() ? null : l.get(0);
    }

    @Leonidas(PsiMethodCallExpression.class)
    public void to(){
        return Arrays.stream(arrayIdentifier(0))
                .streamMethodInvocations(1)
                .findAny().orElseGet(null);
    }
}
