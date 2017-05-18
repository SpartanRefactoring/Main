package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.PsiTypeHelper;

/**
 * @author Oren Afek
 * @since 5/14/2017.
 */
public class EncapsulatorIteratorTest extends PsiTypeHelper {

    public void testNextNoGenerics() {
//        Encapsulator.Iterator $ = freshIterator("int x = 1;");
//        assertEquals(";", nextAsText($));
//        assertEquals("1", nextAsText($));
//        assertEquals("1", nextAsText($));
//        assertEquals("=", nextAsText($));
//        assertEquals("x", nextAsText($));
//        assertEquals("int", nextAsText($));
//        assertEquals("int", nextAsText($));
//        assertEquals("", nextAsText($));
//        assertEquals("int x = 1;", nextAsText($));

    }

    public void testClone() {
//        Encapsulator.Iterator $ = freshIterator("int x;");
//        assertEquals(";", nextAsText($));
//
//        Encapsulator.Iterator $_clone = (Encapsulator.Iterator) $.clone();
//
//        assertEquals("x", nextAsText($));
//        $.next();
//        assertEquals("x", nextAsText($_clone));

    }

    public void testSkips() {
//        Encapsulator.Iterator $ = freshIterator("int x;");
//        assertEquals(";", nextAsText($));
//        $.setNumberOfOccurrences(2);
//        assertEquals(";", nextAsText($));
//        assertEquals(";", nextAsText($));
//        assertEquals("x", nextAsText($));
//

    }

    private String nextAsText(Encapsulator.Iterator it) {
        return it.next().getInner().getText();
    }

    private Encapsulator.Iterator freshIterator(String code) {
        PsiElement testEI = createTestStatementFromString(code);
        return new Encapsulator(testEI).iterator();
    }


}