package il.org.spartan.Leonidas.plugin.tippers;
import il.org.spartan.Leonidas.PsiTypeHelper;
import org.junit.Test;


/**
 * @author @roey maor
 * @since 27-05-2017.
 */
public class TipperTestTest extends PsiTypeHelper {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void testTipperTest(){
        SafeReference idn = new SafeReference();
        TipperTest ts = new TipperTest(idn,this,false);
        ts.check();
    }

}
