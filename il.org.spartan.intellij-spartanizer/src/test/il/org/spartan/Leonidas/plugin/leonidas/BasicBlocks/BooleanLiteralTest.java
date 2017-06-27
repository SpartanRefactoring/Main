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
import java.util.HashMap;

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
        assert booleanLiteral.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("true")), new HashMap<>()).matches();
        assert booleanLiteral.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("false")), new HashMap<>()).matches();
    }

    public void testDoesNotGeneralizeStatements() {
        assert !booleanLiteral.generalizes(Encapsulator.buildTreeFromPsi(createTestStatementFromString("x++;")), new HashMap<>()).matches();
        assert !booleanLiteral.generalizes(Encapsulator.buildTreeFromPsi(createTestStatementFromString("x = y + z;")), new HashMap<>()).matches();
        assert !booleanLiteral.generalizes(Encapsulator.buildTreeFromPsi(createTestStatementFromString("m();")), new HashMap<>()).matches();
        assert !booleanLiteral.generalizes(Encapsulator.buildTreeFromPsi(createTestStatementFromString("return 's';")), new HashMap<>()).matches();
    }

    public void testDoesNotGeneralizeOtherExpressions() {
        assert !booleanLiteral.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("x + y")), new HashMap<>()).matches();
        assert !booleanLiteral.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("x++")), new HashMap<>()).matches();
        assert !booleanLiteral.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("'a'")), new HashMap<>()).matches();
        assert !booleanLiteral.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("2")), new HashMap<>()).matches();
        assert !booleanLiteral.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("null")), new HashMap<>()).matches();
        assert !booleanLiteral.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("a()")), new HashMap<>()).matches();
    }

    public void testTipper() throws Exception {
        File f = new File(Resources.getResource("BooleanLiteralTestTip.java").getPath());
        LeonidasTipper lt = new LeonidasTipper("BooleanLiteralTestTip.java", IOUtils.toString(new BufferedReader(new InputStreamReader(new FileInputStream(f)))));
        assert lt.canTip(createTestWhileStatementFromString("while (true) {\nx++;\n}"));
        assert lt.canTip(createTestWhileStatementFromString("while (false) {\nx++;\n}"));
        assert !lt.canTip(createTestWhileStatementFromString("while (x > 2) {\nx++;\n}"));
        assert !lt.canTip(createTestWhileStatementFromString("while (true) {\nx++; x--;\n}"));
    }
}
