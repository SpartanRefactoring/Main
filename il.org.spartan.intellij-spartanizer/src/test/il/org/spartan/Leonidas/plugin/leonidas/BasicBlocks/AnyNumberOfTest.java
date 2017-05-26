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
 * @author michalcohen
 * @since 19-05-2017.
 */
public class AnyNumberOfTest extends PsiTypeHelper {

    public void testGetNumberOfOccurrences() throws Exception {
        Encapsulator n = Encapsulator.buildTreeFromPsi(createTestStatementFromString("anyNumberOf(statement(1));"));
        AnyNumberOf ano = new AnyNumberOf().create(n.getChildren().get(0));
        Encapsulator e = Encapsulator.buildTreeFromPsi(createTestWhileStatementFromString("while (2 > 3) {x++; x++; x++; x++;}"));
        EncapsulatorIterator it = e.iterator();
        assertEquals(ano.getNumberOfOccurrences(it), 1);
        for (int i = 0; i < 13; i++) it.next();
        assertEquals(ano.getNumberOfOccurrences(it), 4);
    }

    public void testTipper() throws Exception {
        File f = new File(Resources.getResource("AnyNumberOfTipperTest.java").getPath());
        LeonidasTipper lt = new LeonidasTipper("AnyNumberOfTipperTest", IOUtils.toString(new BufferedReader(new InputStreamReader(new FileInputStream(f)))));
        PsiWhileStatement pws1 = createTestWhileStatementFromString("while (x > 2) {\nx++; y--; x--;  \n}");
        assertTrue(lt.canTip(pws1));
        PsiWhileStatement pws2 = createTestWhileStatementFromString("while (x > 2) {\nx++; x--;  \n}");
        assertTrue(lt.canTip(pws2));
        PsiWhileStatement pws3 = createTestWhileStatementFromString("while (x > 2) {\nx++;\n}");
        assertFalse(lt.canTip(pws3));
        PsiWhileStatement pws4 = createTestWhileStatementFromString("while (x > 2) {\nx++; x--; x--;  \n}");
        assertTrue(lt.canTip(pws4));
        PsiWhileStatement pws5 = createTestWhileStatementFromString("while (x > 2) {\nx++; x++; x--;  \n}");
        assertTrue(lt.canTip(pws5));
        PsiWhileStatement pws6 = createTestWhileStatementFromString("while (x > 2) {\nx++; y--; y++; x--;  \n}");
        assertTrue(lt.canTip(pws6));
    }

}