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
 * @since 13-05-2017.
 */
public class OptionalTest extends PsiTypeHelper {
    
    public void getNumberOfOccurrences() throws Exception {
        Encapsulator n = Encapsulator.buildTreeFromPsi(createTestStatementFromString("optional(statement(1));"));
        Optional ano = new Optional().create(n.getChildren().get(0));
        Encapsulator e = Encapsulator.buildTreeFromPsi(createTestWhileStatementFromString("while (2 > 3) {x++; x--;}"));
        Encapsulator.Iterator it = e.iterator();
        assertEquals(ano.getNumberOfOccurrences(it), 1);
        for (int i = 0; i < 10; i++) it.next();
        assertEquals(ano.getNumberOfOccurrences(it), 0);
    }

    public void testTip1() throws Exception {
        File f = new File(Resources.getResource("OptionalTestTipper1.java").getPath());
        LeonidasTipper lt = new LeonidasTipper("OptionalTestTipper1", IOUtils.toString(new BufferedReader(new InputStreamReader(new FileInputStream(f)))));
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
    }

}