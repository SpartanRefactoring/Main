package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.google.common.io.Resources;
import il.org.spartan.Leonidas.PsiTypeHelper;
import il.org.spartan.Leonidas.plugin.tippers.LeonidasTipper;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * @author michalcohen
 * @since 13-05-2017.
 */
public class OptionalMethodCallBasedTest extends PsiTypeHelper {
    
    public void getNumberOfOccurrences() throws Exception {
        Encapsulator n = Encapsulator.buildTreeFromPsi(createTestStatementFromString("optional(statement(1));"));
        OptionalMethodCallBased ano = new OptionalMethodCallBased().create(n.getChildren().get(0), new HashMap<>());
        EncapsulatorIterator it = Encapsulator.buildTreeFromPsi(createTestWhileStatementFromString("while (2 > 3) {x++; x--;}")).iterator();
        assertEquals(ano.getNumberOfOccurrences(it, new HashMap<>()), 1);
        for (int i = 0; i < 10; ++i) it.next();
        assertEquals(ano.getNumberOfOccurrences(it, new HashMap<>()), 0);
    }

    public void testTip1() throws Exception {
        File f = new File(Resources.getResource("OptionalTestTipper1.java").getPath());
        LeonidasTipper lt = new LeonidasTipper("OptionalTestTipper1", IOUtils.toString(new BufferedReader(new InputStreamReader(new FileInputStream(f)))));
        assert lt.canTip(createTestWhileStatementFromString("while (x > 2) {\nx++; y--; x--;  \n}"));
        assert lt.canTip(createTestWhileStatementFromString("while (x > 2) {\nx++; x--;  \n}"));
        assert !lt.canTip(createTestWhileStatementFromString("while (x > 2) {\nx++;\n}"));
        assert lt.canTip(createTestWhileStatementFromString("while (x > 2) {\nx++; x--; x--;  \n}"));
        assert lt.canTip(createTestWhileStatementFromString("while (x > 2) {\nx++; x++; x--;  \n}"));
    }

}