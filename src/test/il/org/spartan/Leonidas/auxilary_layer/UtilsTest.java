package il.org.spartan.Leonidas.auxilary_layer;

import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiMethod;
import il.org.spartan.Leonidas.PsiTypeHelper;

import static il.org.spartan.Leonidas.auxilary_layer.Utils.in;

/**
 * @author michalcohen
 * @since 22-12-2016.
 */
public class UtilsTest extends PsiTypeHelper {
    private static final String dummyFileName = "test.java";

    public void testIn() throws Exception {
        assertTrue(in(5, 3, 4, 5, 6));
        assertFalse(in(2, 3, 4, 5, 6));
        assertTrue(in("banana", "apple", "pear", "avocado", "banana"));
    }

    public void testGetAllReferences() throws Exception {
        PsiMethod m = createTestMethodFromString("int foo() { int x = 0; x++; x--; return x;}");
        PsiIdentifier id = createTestIdentifierFromString("x");
        assertEquals(Utils.getAllReferences(m, id).size(), 4);
    }

    public void testGetClassFromFile() throws Exception {

    }

    public void testGetDocumentFromPsiElement() throws Exception {

    }

    public void testGetProject() throws Exception {

    }

    public void testGetChildrenOfType() throws Exception {

    }

    public void testGetSourceCode() throws Exception {

    }

    public void testFixSpacesProblemOnPath() throws Exception {

    }
/*
    public void testin() throws Exception {
        List<Integer> aList = new ArrayList<>();
        aList.addAll(Arrays.asList(55, 44, 33, 22, 11));
        assert Utils.in(44, aList.toArray());
        assert !Utils.in(43, aList.toArray());
        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection") List<Integer> emptyList = new ArrayList<>();
        assert !Utils.in(0, emptyList.toArray());
        assert !Utils.in(null, aList);
        assert !Utils.in(null, emptyList.toArray());
        assert !Utils.in(40);
    }

*/

/*
    public void testfindClass() throws Exception {
        //the assert statement is used as a latch to check if it is in a parent class or not
        String classExistsSource1 = "package test; " +
                "public class Test { " +
                "public Test() { " +
                "final int x=3; assert(true); System.out.println(\"lalala\"); " +
                "} " +
                "}";
        PsiFile classExistsFile1 = createFile(dummyFileName, classExistsSource1);
        PsiAssertStatement assertInSource1 = Utils.getChildrenOfType(classExistsFile1, PsiAssertStatement.class).get(0);
        String classExistsSource2 =
                "public Test() { " +
                        "final int x=3; assert(true); System.out.println(\"lalala\"); " +
                        "} ";
        PsiMethod methodIn2 = createTestMethodFromString(classExistsSource2);
        assert Utils.findClass(assertInSource1) != null;
        assert Objects.equals(Utils.findClass(assertInSource1).getName(), "Test");
        assert Utils.findClass(methodIn2) == null;
    }
  */
    //TODO: FAIL SINCE SPARTANIZATION
//    public void testgetAllReferences() throws Exception {
//        PsiIdentifier id = createTestIdentifierFromString("id"),
//				nonExistant = createTestIdentifierFromString("ahgrtjygk");
//        String m1String = "void foo(){ int k=5; }";
//        PsiMethod m1 = createTestMethodFromString(m1String);
//        String m2String = "int id() { int id=7; return 8;}";
//        PsiMethod m2 = createTestMethodFromString(m2String);
//        String m3String = "int id() { int id=7; return id;}";
//        PsiMethod m3 = createTestMethodFromString(m3String);
//        assert Utils.getAllReferences(null, null).isEmpty();
//        assert Utils.getAllReferences(null, id).isEmpty();
//        assert Utils.getAllReferences(m1, id).isEmpty();
//        assert Utils.getAllReferences(m2, id).size() == 1;
//        assert Utils.getAllReferences(m2, nonExistant).isEmpty();
//        assert Utils.getAllReferences(m3, id).size() == 2;
//    }

    public void ignoretestgetCurrentClass() throws Exception {
        //Cannot be tested here, requires real editor with active moving caret
    }

    public void ignoretestconforms() throws Exception {
        //conforms is currently unused
    }



}