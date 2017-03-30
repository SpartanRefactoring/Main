package il.org.spartan.spartanizer.research.metatester;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.TestClass;
import org.junit.runners.parameterized.BlockJUnit4ClassRunnerWithParametersFactory;
import org.junit.runners.parameterized.TestWithParameters;

import static il.org.spartan.spartanizer.research.metatester.FileUtils.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** @author Oren Afek
 * @since 3/27/2017 */
public class MetaTester extends BlockJUnit4ClassRunner {
  private Class<?> testClass;
  private File sourceFile;
  private String testName;
  private List<SourceLine> sourceLines;

  public MetaTester(Class<?> clazz) throws InitializationError {
    super(clazz);
    this.testClass = clazz;
    this.testName = this.testClass.getSimpleName();
    this.sourceFile = openSourceFile(testName);
    this.sourceLines = readAllLines(testName);
  }

  @Override public Description getDescription() {
    return Description.createTestDescription(testClass, "MetaTester");
  }

  @Override @SuppressWarnings("unused") protected void runChild(FrameworkMethod __, RunNotifier n) {
    Class<?> newTestClass = new TestClassGenerator(this.testClass).generate(this.testClass.getSimpleName() + "_CustomTest", this.sourceLines);
    TestSuite suite = new TestSuite(newTestClass);
    suite.run(new TestResult());
    try {
      new BlockJUnit4ClassRunnerWithParametersFactory()
          .createRunnerForTestWithParameters(new TestWithParameters(" ", new TestClass(newTestClass), new ArrayList<>())).run(n);
    } catch (InitializationError ignore) {/**/}
    // Uncomment this to run the original test aswell
    /* super.runChild(method, notifier); */
  }

  private File openSourceFile(String className) {
    return new File(FileUtils.makePath(testSourcePath(testClass), className + ".java"));
  }

  @SuppressWarnings({ "unused", "resource" }) private List<SourceLine> readAllLines(String testName1) {
    List<SourceLine> $ = new ArrayList<>();
    try {
      BufferedReader linesStream = new BufferedReader(new FileReader(sourceFile));
      String line = linesStream.readLine();
      SourceLine.SourceLineFactory factory = new SourceLine.SourceLineFactory(testName1);
      for (int ¢ = 1; line != null; ++¢) {
        if (line.contains("void") && line.contains("()"))
          factory.setTestMethodName(line.replace("public void ", "").replace("()", "").replace("{", "").trim());
        $.add(factory.createSourceLine(line, ¢));
        line = linesStream.readLine();
      }
      $.remove(SourceLine.EMPTY);
      return $;
    } catch (IOException ignore) {/**/}
    return new ArrayList<>();
  }
}
