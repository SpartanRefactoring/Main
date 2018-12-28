package metatester;

import junit.framework.TestResult;
import junit.framework.TestSuite;

import org.junit.internal.AssumptionViolatedException;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;
import org.junit.runners.parameterized.BlockJUnit4ClassRunnerWithParametersFactory;
import org.junit.runners.parameterized.TestWithParameters;

import java.io.File;
import java.util.ArrayList;

import static metatester.aux_layer.FileUtils.testSourcePath;

/**
 * Meta Tester main run class.
 * <p>
 * This class runs the JUnit tests in the given class so that the following holds:
 * <ul>
 * <li>The input must be a valid JUnit 4+ test.</li>
 * <li>If the test fails in a plain run, then it will fail in the meta run, and vice versa</li>
 * <li>For each test method, the meta tester generates one or more meta tests, which make it possible for the programmer to pinpoint the exact nature
 * of the failure </il>
 * </ul>
 * The main advantages of using this class instead of the plain JUnit 4 tests are:`
 * <ul>
 * <li>It is possible to write assertions using the assert command, instead of hamcrest, e.g., one can write plain Java, e.g.,
 * {@code assert f()>2} will be automatically translated {@code assertThat(f(), isGreaterThan(2)}
 * </li>
 * <li>Two consecutive asserts in the same test method are OK. This class will replace the original test method with two methods, each containing one of
 * the assertions. In general, if there are <em>n</em> assertions in a method, the code run by this class will generate <em>n</em> test methods instead
 * ot the original one.
 * </li>
 * <li>In running a later test method, the meta tester executes all code in earlier assertions, without checking for correctness and while ignoring any
 * thrown exceptions</li>
 * </ul>
 * </p>
 * @author Dor Ma'ayan
 * @author Oren Afek
 * @since 3/27/2017
 */
public class MetaTester extends BlockJUnit4ClassRunner {
    private final Class<?> testClass;
    private final File sourceFile;
    private final String testName;
    private boolean hasRan;

    public MetaTester(final Class<?> clazz) throws InitializationError {
        super(clazz);
        testClass = clazz;
        testName = testClass.getSimpleName();
        sourceFile = openSourceFile();
    }

//    @Override
//    public Description getDescription() {
//        return Description.createTestDescription(testClass, "metatester");
//    }

    /**
     * The entry point of JUnit4
     *
     * @param __ JD
     * @param n  JD
     */

    @SuppressWarnings("unused")
    protected void runChild2(final FrameworkMethod method, final RunNotifier n) {
//        if (hasRan)
//            return;
        ASTTestClassGenerator generator = new ASTTestClassGenerator(testClass);
        String metaTestClassName = testClass.getSimpleName() + "_Meta";
        removeMetaFileIfExists(generator, metaTestClassName);
        final Class<?> newTestClass = generator.generate(metaTestClassName, sourceFile);
        final TestSuite suite = new TestSuite(newTestClass);
        suite.run(new TestResult());
        try {
            new BlockJUnit4ClassRunnerWithParametersFactory()
                    .createRunnerForTestWithParameters(new TestWithParameters(" ", new TestClass(newTestClass), new ArrayList<>())).run(n);
        } catch (final InitializationError ignore) {/**/}
        // Uncomment this to run the original test as well
        //super.runChild(method, n);
        //hasRan = true;
    }
    
    
    @Override
    protected void runChild(final FrameworkMethod method, RunNotifier notifier) {
        System.out.println(method);
    	Description description = describeChild(method);
        if (isIgnored(method)) {
            notifier.fireTestIgnored(description);
        } else {
        	notifier.addFirstListener(new RunListener() {
                public void testFailure(Failure failure) {
                	System.out.println("Wow what a failure" + method);
                	System.out.println(method.getMethod().toString());

                	//runLeaf(methodBlock(method), description, notifier);
                	//runChild2(method,notifier);
                }
             });
        	runLeaf(methodBlock(method), description, notifier);
        }
    }
    
    /**
     * Opens the original test's source file
     *
     * @return file object
     */
    private File openSourceFile() {
        return new File(testSourcePath(testClass));
    }

    /**
     * Removes the file if it exists
     *
     * @param generator the Generator Used to create the file.
     * @param className the new generated test class name.
     * @return
     */
    private boolean removeMetaFileIfExists(ASTTestClassGenerator generator, final String className) {
        File f = new File(generator.getMetaTestFilePath(className));
        return f.exists() && f.delete();
    }
}
