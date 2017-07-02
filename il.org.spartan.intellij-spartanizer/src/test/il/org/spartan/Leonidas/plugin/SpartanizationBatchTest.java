package il.org.spartan.Leonidas.plugin;

import com.intellij.openapi.progress.ProgressManager;
import il.org.spartan.Leonidas.PsiTypeHelper;

import java.util.Arrays;
import java.util.HashSet;

/**
 * @author RoeiRaz
 * @since 19/06/17
 */
public class SpartanizationBatchTest extends PsiTypeHelper {

    String file0 = "class A { void foo() { if (true) { System.out.println('hi'); } } }";

    public void testSpartanizationOfOneElementNotThrowingException() throws InterruptedException {
        ProgressManager.getInstance().run(
                new SpartanizationBatch(getProject(), new HashSet<>(Arrays.asList(createTestFileFromString(file0)))));
    }
}