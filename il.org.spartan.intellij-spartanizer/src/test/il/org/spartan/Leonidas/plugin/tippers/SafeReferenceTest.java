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
        SafeReference idn = new SafeReference();
        TipperTest ts = new TipperTest(idn,this,false);
        ts.check();
    }

}
