package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.google.common.io.Resources;
import com.intellij.psi.PsiWhileStatement;
import il.org.spartan.Leonidas.PsiTypeHelper;
import il.org.spartan.Leonidas.plugin.tippers.LeonidasTipper;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * @author Sharon
 * @since 14.5.17
 */
public class BooleanLiteralTest extends PsiTypeHelper {
    protected BooleanLiteral booleanLiteral;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        booleanLiteral = new BooleanLiteral();
    }

    public void testGeneralizesBooleanLiterals() {
        assert booleanLiteral.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("true"))).matches();
        assert booleanLiteral.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("false"))).matches();
    }

    public void testDoesNotGeneralizeStatements() {
        assert !booleanLiteral.generalizes(Encapsulator.buildTreeFromPsi(createTestStatementFromString("x++;"))).matches();
        assert !booleanLiteral.generalizes(Encapsulator.buildTreeFromPsi(createTestStatementFromString("x = y + z;"))).matches();
        assert !booleanLiteral.generalizes(Encapsulator.buildTreeFromPsi(createTestStatementFromString("m();"))).matches();
        assert !booleanLiteral.generalizes(Encapsulator.buildTreeFromPsi(createTestStatementFromString("return 's';"))).matches();
    }

    public void testDoesNotGeneralizeOtherExpressions() {
        assert !booleanLiteral.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("x + y"))).matches();
        assert !booleanLiteral.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("x++"))).matches();
        assert !booleanLiteral.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("'a'"))).matches();
        assert !booleanLiteral.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("2"))).matches();
        assert !booleanLiteral.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("null"))).matches();
        assert !booleanLiteral.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("a()"))).matches();
    }

    public void testTipper() throws Exception {
        File f = new File(Resources.getResource("BooleanLiteralTestTip.java").getPath());
        LeonidasTipper lt = new LeonidasTipper("BooleanLiteralTestTip.java", IOUtils.toString(new BufferedReader(new InputStreamReader(new FileInputStream(f)))));
        PsiWhileStatement pws1 = createTestWhileStatementFromString("while (true) {\nx++;\n}");
        assertTrue(lt.canTip(pws1));
        PsiWhileStatement pws2 = createTestWhileStatementFromString("while (false) {\nx++;\n}");
        assertTrue(lt.canTip(pws2));
        PsiWhileStatement pws3 = createTestWhileStatementFromString("while (x > 2) {\nx++;\n}");
        assertFalse(lt.canTip(pws3));
        PsiWhileStatement pws4 = createTestWhileStatementFromString("while (true) {\nx++; x--;\n}");
        assertFalse(lt.canTip(pws4));
    }
}
