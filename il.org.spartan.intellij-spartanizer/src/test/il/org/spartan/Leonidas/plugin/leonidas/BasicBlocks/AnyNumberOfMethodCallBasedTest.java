package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.google.common.io.Resources;
import com.intellij.psi.PsiWhileStatement;
import il.org.spartan.Leonidas.PsiTypeHelper;
import il.org.spartan.Leonidas.auxilary_layer.az;
import il.org.spartan.Leonidas.plugin.leonidas.Pruning;
import il.org.spartan.Leonidas.plugin.tippers.LeonidasTipper;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * @author michalcohen
 * @since 19-05-2017.
 */
public class AnyNumberOfMethodCallBasedTest extends PsiTypeHelper {

    public void testGetNumberOfOccurrences() throws Exception {
        Encapsulator n = Encapsulator.buildTreeFromPsi(createTestStatementFromString("anyNumberOf(statement(1));"));
        AnyNumberOfMethodCallBased ano = az.anyNumberOf(Pruning.prune(n, new HashMap<>()));
        EncapsulatorIterator it = Encapsulator.buildTreeFromPsi(createTestWhileStatementFromString("while (2 > 3) {x++; x++; x++; x++;}")).iterator();
        assertEquals(ano.getNumberOfOccurrences(it, new HashMap<>()), 1);
        for (int ¢ = 0; ¢ < 13; ++¢) it.next();
        assertEquals(ano.getNumberOfOccurrences(it, new HashMap<>()), 4);
    }

    public void testTipper() throws Exception {
        File f = new File(Resources.getResource("AnyNumberOfTipperTest.java").getPath());
        LeonidasTipper lt = new LeonidasTipper("AnyNumberOfTipperTest", IOUtils.toString(new BufferedReader(new InputStreamReader(new FileInputStream(f)))));
        assert lt.canTip(createTestWhileStatementFromString("while (x > 2) {\nx++; y--; x--;  \n}"));
        assert lt.canTip(createTestWhileStatementFromString("while (x > 2) {\nx++; x--;  \n}"));
        assert !lt.canTip(createTestWhileStatementFromString("while (x > 2) {\nx++;\n}"));
        assert lt.canTip(createTestWhileStatementFromString("while (x > 2) {\nx++; x--; x--;  \n}"));
        assert lt.canTip(createTestWhileStatementFromString("while (x > 2) {\nx++; x++; x--;  \n}"));
        assert lt.canTip(createTestWhileStatementFromString("while (x > 2) {\nx++; y--; y++; x--;  \n}"));
    }
}