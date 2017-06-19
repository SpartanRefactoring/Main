package il.org.spartan.Leonidas.plugin;

import com.intellij.psi.PsiFile;
import il.org.spartan.Leonidas.PsiTypeHelper;

import java.util.Arrays;
import java.util.HashSet;

/**
 * @author RoeiRaz
 * @since 19/06/17
 */
public class SpartanizationBatchTest extends PsiTypeHelper {

    // TODO put in a resource file @RoeiRaz
    String file0 = "class A { void foo() { if (true) { System.out.println('hi'); } } }";

    public void testSpartanizationOfOneElementNotThrowingException() {
        PsiFile psiFile0 = this.createTestFileFromString(file0);
        psiFile0.equals(psiFile0);
        new SpartanizationBatch(psiFile0.getProject(), new HashSet<>(Arrays.asList(psiFile0))).invoke();
    }
}