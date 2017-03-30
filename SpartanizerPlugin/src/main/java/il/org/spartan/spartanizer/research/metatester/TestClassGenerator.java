package il.org.spartan.spartanizer.research.metatester;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import static il.org.spartan.spartanizer.research.metatester.FileUtils.*;

/** @author Oren Afek
 * @since 3/26/2017 */
public class TestClassGenerator {
  /** C:\Users\oren.afek\git\Spartanizer\src\test\java\il\org\spartan\spartanizer\research\metatester */
  private static final String sourcePath = makePath(System.getProperty("user.dir"), "src", "test", "java", packageName("\\\\"), "generated");

  private static String packageName(String seperator) {
    return new TestClassGenerator().getClass().getPackage().getName().replaceAll("\\.", seperator);
  }

  public static final String packageName = packageName("\\.") + "." + "generated";
  public static final String JAVA_SUFFIX = ".java";

  public Class<?> generate(String testClassName, Collection<? extends SourceLine> ls) {
    return loadClass(testClassName, getClassString(ls.stream().filter(λ -> λ instanceof TestLine).map(λ -> (TestLine) λ).collect(Collectors.toList()),
        ls.stream().filter(λ -> λ instanceof ImportLine).map(λ -> (ImportLine) λ).collect(Collectors.toList()), testClassName));
  }

  @SuppressWarnings({ "resource", "static-method" }) private void compileSourceCode(String className, String sourceCode) {
    FileWriter writer = null;
    File sourceFile = new File(makePath(sourcePath, className + JAVA_SUFFIX));
    try {
      sourceFile.createNewFile();
      writer = new FileWriter(sourceFile);
      writer.write(sourceCode);
      writer.close();
      JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
      StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
      File classFile = new File(generatedClassPath);
      classFile.createNewFile();
      fileManager.setLocation(StandardLocation.CLASS_OUTPUT, Collections.singletonList(classFile));
      compiler
          .getTask(new StringWriter(), fileManager, null, null, null, fileManager.getJavaFileObjectsFromFiles(Collections.singletonList(sourceFile)))
          .call();
      fileManager.close();
    } catch (@SuppressWarnings("unused") IOException ignore) {/**/}
  }

  @SuppressWarnings("resource") private Class<?> loadClass(String $, String sourceCode) {
    compileSourceCode($, sourceCode);
    try {
      return new URLClassLoader(new URL[] { new File(generatedClassPath).toURI().toURL() }).loadClass(packageName + "." + $);
    } catch (IOException | ClassNotFoundException ignore) {
      ignore.printStackTrace();
    }
    return Object.class;
  }

  private static String getClassString(Collection<TestLine> testsLines, Collection<ImportLine> ls, String className) {
    return packageHeaderString(packageName) + "\n" + importStatementString(ls) + "\n" + classHeaderString(className) + "\n" + testMethods(testsLines)
        + "\n" + "}";
  }

  private static String testMethods(Collection<TestLine> testsLines) {
    return testsLines.stream().map(TestLine::generateTestMethod).reduce((acc, s1) -> acc + "\n\n" + s1).orElse("");
  }

  private static String importStatementString(Collection<ImportLine> ls) {
    return ls.stream().filter(λ -> !"import org.junit.runner.RunWith;".equals(λ.getContent().trim())).reduce("", (s, importLine) -> s + importLine.getContent() + "\n", (s, s2) -> s + s2);
  }

  private static String classHeaderString(String className) {
    return "@SuppressWarnings(\"static-method\")" + "\n" + "public class " + className + " { \n";
  }

  private static String packageHeaderString(String packageNameString) {
    return "package " + packageNameString + ";";
  }
}
