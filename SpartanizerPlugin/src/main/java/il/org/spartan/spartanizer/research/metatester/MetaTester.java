package il.org.spartan.spartanizer.research.metatester;

import static il.org.spartan.spartanizer.research.metatester.FileUtils.*;

import java.io.*;
import java.util.*;

import org.junit.runner.*;
import org.junit.runner.notification.*;
import org.junit.runners.*;
import org.junit.runners.model.*;
import org.junit.runners.parameterized.*;

import junit.framework.*;

/** @author Oren Afek
 * @since 3/27/2017 */
public class MetaTester extends BlockJUnit4ClassRunner {
  private final Class<?> testClass;
  private final File sourceFile;
  private final String testName;
  private final List<SourceLine> sourceLines;

  public MetaTester(final Class<?> clazz) throws InitializationError {
    super(clazz);
    testClass = clazz;
    testName = testClass.getSimpleName();
    sourceFile = openSourceFile(testName);
    sourceLines = readAllLines(testName);
  }

  @Override public Description getDescription() {
    return Description.createTestDescription(testClass, "MetaTester");
  }

  @Override @SuppressWarnings("unused") protected void runChild(final FrameworkMethod __, final RunNotifier n) {
    final Class<?> newTestClass = new TestClassGenerator(testClass).generate(testClass.getSimpleName() + "_CustomTest", sourceLines);
    final TestSuite suite = new TestSuite(newTestClass);
    suite.run(new TestResult());
    try {
      new BlockJUnit4ClassRunnerWithParametersFactory()
          .createRunnerForTestWithParameters(new TestWithParameters(" ", new TestClass(newTestClass), new ArrayList<>())).run(n);
    } catch (final InitializationError ignore) {/**/}
    // Uncomment this to run the original test aswell
    /* super.runChild(method, notifier); */
  }

  private File openSourceFile(final String className) {
    return new File(FileUtils.makePath(testSourcePath(testClass), className + ".java"));
  }

  @SuppressWarnings({ "unused", "resource" }) private List<SourceLine> readAllLines(final String testName1) {
    final List<SourceLine> $ = new ArrayList<>();
    try {
      final BufferedReader linesStream = new BufferedReader(new FileReader(sourceFile));
      String line = linesStream.readLine();
      final SourceLine.SourceLineFactory factory = new SourceLine.SourceLineFactory(testName1);
      for (int ¢ = 1; line != null; ++¢) {
        if (line.contains("void") && line.contains("()"))
          factory.setTestMethodName(line.replace("public void ", "").replace("()", "").replace("{", "")
              .replace("@SuppressWarnings(\"static-method\")","").trim());
        $.add(factory.createSourceLine(line, ¢));
        line = linesStream.readLine();
      }
      $.remove(SourceLine.EMPTY);
      return $;
    } catch (final IOException ignore) {/**/}
    return new ArrayList<>();
  }
}
