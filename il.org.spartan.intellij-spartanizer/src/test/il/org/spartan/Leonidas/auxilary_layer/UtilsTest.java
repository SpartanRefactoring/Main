package il.org.spartan.Leonidas.auxilary_layer;

import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiMethod;
import il.org.spartan.Leonidas.PsiTypeHelper;

import java.util.Optional;


/**
 * @author michalcohen
 * @since 22-12-2016.
 */
public class UtilsTest extends PsiTypeHelper {

    public void testIn() throws Exception {
        assert Utils.in(5, 3, 4, 5, 6);
        assert !Utils.in(2, 3, 4, 5, 6);
        assert Utils.in("banana", "apple", "pear", "avocado", "banana");
        assert !Utils.in(3);
        assert !Utils.in(new Object());
        assert !Utils.in(null);
    }

    public void testGetAllReferences() throws Exception {
        PsiMethod m = createTestMethodFromString("int foo() { int x = 0; x++; x--; return x;}");
        PsiIdentifier id = createTestIdentifierFromString("x");
        assertEquals(Utils.getAllReferences(m, id).size(), 4);
        id = createTestIdentifierFromString("id");
        PsiIdentifier nonExistent = createTestIdentifierFromString("banana");
        PsiMethod m1 = createTestMethodFromString("void foo(){ int k=5; }"),
                m2 = createTestMethodFromString("int id() { int id=7; return 8;}"),
                m3 = createTestMethodFromString("int id() { int id=7; return id;}");
        assert Utils.getAllReferences(null, null).isEmpty();
        assert Utils.getAllReferences(null, id).isEmpty();
        assert Utils.getAllReferences(m1, id).isEmpty();
        assert Utils.getAllReferences(m2, id).size() == 1;
        assert Utils.getAllReferences(m2, nonExistent).isEmpty();
        assert Utils.getAllReferences(m3, id).size() == 2;
    }

    public void testGetDocumentFromPsiElement() throws Exception {
        assertNull(Utils.getDocumentFromPsiElement(createTestIfStatement("x > 2", "x++;")));
    }

    public void testGetProject() throws Exception {
        assertEquals(Utils.getProject().getName(), "testGetProject");
    }

    public void testGetChildrenOfType() throws Exception {
        assertEquals(Utils.getChildrenOfType(createTestMethodFromString("int foo() { int x,y,z; x++; y--; z+=2;"),
                PsiIdentifier.class).size(), 7);
    }

    public void testFixSpacesProblemOnPath() throws Exception {
        String s = "C:\\Users\\";
        assertEquals(Utils.fixSpacesProblemOnPath(s), s);
        s = "C:\\Users\\J%20D";
        assertEquals(Utils.fixSpacesProblemOnPath(s), "C:\\Users\\J D");
    }

    public void testGetFirstElementInsideBody() throws Exception {
        assertEquals(Utils.getFirstElementInsideBody(createTestCodeBlockFromString("{x++;}")).getText(), "x++;");
    }

    public void testGetPublicMethod() throws Exception {
        assertEquals(Optional.empty(), Utils.getPublicMethod(Object.class, "myFakeMethod", Object.class));
        assertEquals(String.class.getMethod("substring", int.class, int.class),
                Utils.getPublicMethod(String.class, "substring",
                        int.class, int.class).orElse(null));

    }
}