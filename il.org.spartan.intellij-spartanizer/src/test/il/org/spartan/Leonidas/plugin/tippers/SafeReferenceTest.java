package il.org.spartan.Leonidas.plugin.tippers;
import il.org.spartan.Leonidas.PsiTypeHelper;
import org.junit.Test;


/**
 * @author @roey maor
 * @since 27-06-2017.
 */
public class SafeReferenceTest extends PsiTypeHelper {

    @Test
    public void testSafeReference(){
        new TipperTest(new SafeReference(), this, false, false).check();
    }

}
