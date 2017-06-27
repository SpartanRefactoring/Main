package il.org.spartan.Leonidas.auxilary_layer;

import com.intellij.psi.PsiCodeBlock;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiIfStatement;
import com.intellij.psi.PsiMethod;
import il.org.spartan.Leonidas.PsiTypeHelper;

import static il.org.spartan.Leonidas.auxilary_layer.Utils.getDocumentFromPsiElement;
import static il.org.spartan.Leonidas.auxilary_layer.Utils.in;

/**
 * @author michalcohen
 * @since 22-12-2016.
 */
public class UtilsTest extends PsiTypeHelper {

    public void testIn() throws Exception {
        assert in(5, 3, 4, 5, 6);
        assert !in(2, 3, 4, 5, 6);
        assert in("banana", "apple", "pear", "avocado", "banana");
        assert !in(3);
        assert !in(new Object());
        assert !in(null);
    }

    public void testGetAllReferences() throws Exception {
        PsiMethod m = createTestMethodFromString("int foo() { int x = 0; x++; x--; return x;}");
        PsiIdentifier id = createTestIdentifierFromString("x");
        assertEquals(Utils.getAllReferences(m, id).size(), 4);
        id = createTestIdentifierFromString("id");
        PsiIdentifier nonExistent = createTestIdentifierFromString("banana");
        String m1String = "void foo(){ int k=5; }";
        PsiMethod m1 = createTestMethodFromString(m1String);
        String m2String = "int id() { int id=7; return 8;}";
        PsiMethod m2 = createTestMethodFromString(m2String);
        String m3String = "int id() { int id=7; return id;}";
        PsiMethod m3 = createTestMethodFromString(m3String);
        assert Utils.getAllReferences(null, null).isEmpty();
        assert Utils.getAllReferences(null, id).isEmpty();
        assert Utils.getAllReferences(m1, id).isEmpty();
        assert Utils.getAllReferences(m2, id).size() == 1;
        assert Utils.getAllReferences(m2, nonExistent).isEmpty();
        assert Utils.getAllReferences(m3, id).size() == 2;
    }

    public void testGetDocumentFromPsiElement() throws Exception {
        PsiIfStatement i = createTestIfStatement("x > 2", "x++;");
        assertNull(getDocumentFromPsiElement(i));
    }

    public void testGetProject() throws Exception {
        assertEquals(Utils.getProject().getName(), "testGetProject");
    }

    public void testGetChildrenOfType() throws Exception {
        PsiMethod m = createTestMethodFromString("int foo() { int x,y,z; x++; y--; z+=2;");
        assertEquals(Utils.getChildrenOfType(m, PsiIdentifier.class).size(), 7);
    }

    public void testFixSpacesProblemOnPath() throws Exception {
        String s = "C:\\Users\\";
        assertEquals(Utils.fixSpacesProblemOnPath(s), s);
        s = "C:\\Users\\J%20D";
        assertEquals(Utils.fixSpacesProblemOnPath(s), "C:\\Users\\J D");
    }

    public void testGetFirstElementInsideBody() throws Exception {
        PsiCodeBlock cb = createTestCodeBlockFromString("{x++;}");
        assertEquals(Utils.getFirstElementInsideBody(cb).getText(), "x++;");
    }
}