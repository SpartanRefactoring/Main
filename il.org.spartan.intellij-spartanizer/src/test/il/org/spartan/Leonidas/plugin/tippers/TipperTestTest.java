package il.org.spartan.Leonidas.plugin.tippers;
import com.intellij.testFramework.PsiTestCase;
import il.org.spartan.Leonidas.plugin.tippers.leonidas.IfDoubleNot;
import org.junit.Test;


/**
 * @author @roey maor
 * @since 27-05-2017.
 */
public class TipperTestTest extends PsiTestCase{

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void testTipperTest(){
        IfDoubleNot idn = new IfDoubleNot();
        TipperTest ts = new TipperTest(idn);
        ts.test();
    }
}
