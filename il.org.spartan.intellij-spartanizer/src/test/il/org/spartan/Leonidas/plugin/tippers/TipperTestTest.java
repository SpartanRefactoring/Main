package il.org.spartan.Leonidas.plugin.tippers;
import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.PsiTypeHelper;
import il.org.spartan.Leonidas.plugin.tippers.leonidas.IfDoubleNot;
import org.junit.Test;


/**
 * @author @roey maor
 * @since 27-05-2017.
 */
public class TipperTestTest extends PsiTypeHelper {

    private static final String before = "package test;\n public class Foo\n{\n public void Bar(){\n";
    private static final String after = "\n }\n}";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void testTipperTest(){
        IfDoubleNot idn = new IfDoubleNot();
        TipperTest ts = new TipperTest(idn,this);
        PsiElement beforeElement = createTestFileFromString(before+"int x = 3;"+after);
        ts.check();
    }
}
