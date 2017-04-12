package il.org.spartan.research.metatester;

import static il.org.spartan.research.metatester.FileUtils.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.stream.*;

import javax.tools.*;

/** @author Oren Afek
 * @since 3/26/2017 */
@SuppressWarnings("static-method")
public class StringTestClassGenerator implements TestClassGenerator {
  /** C:\Users\oren.afek\git\Spartanizer\src\test\java\il\org\spartan\spartanizer\research\metatester */
  private static final String JAVA_SUFFIX = ".java";
  private final Class<?> testClass;
  private final String sourcePath;
  public final String packageName;
  private final String testName;
  private final File originalSourceFile;
  

  public StringTestClassGenerator(final Class<?> testClass, String testName, File originalSourceFile) {
    this.testClass = testClass;
    sourcePath = makePath(System.getProperty("user.dir"), "src", "test", "java", packageName("\\\\", testClass));
    packageName = packageName("\\.", testClass);
    this.testName = testName;
    this.originalSourceFile = originalSourceFile;
  }

  @Override public Class<?> generate(final String testClassName) {
   final Collection<? extends SourceLine> $ = readAllLines();
    return loadClass(testClassName, getClassString($.stream().filter(λ -> λ instanceof TestLine).map(λ -> (TestLine) λ).collect(Collectors.toList()),
        $.stream().filter(λ -> λ instanceof ImportLine).map(λ -> (ImportLine) λ).collect(Collectors.toList()), testClassName));
  }
  
  @SuppressWarnings({ "unused", "resource" }) private List<SourceLine> readAllLines() {
    final List<SourceLine> $ = new ArrayList<>();
    try {
      final BufferedReader linesStream = new BufferedReader(new FileReader(originalSourceFile));
      String line = linesStream.readLine();
      final SourceLine.SourceLineFactory factory = new SourceLine.SourceLineFactory(testName);
      for (int ¢ = 1; line != null; ++¢) {
        if (line.contains("void") && line.contains("()"))
          factory.setTestMethodName(
              line.replace("public void ", "").replace("()", "").replace("{", "").replace("@SuppressWarnings(\"static-method\")", "").trim());
        $.add(factory.createSourceLine(line, ¢));
        line = linesStream.readLine();
      }
      $.remove(SourceLine.EMPTY);
      return $;
    } catch (final IOException ignore) {/**/}
    return new ArrayList<>();
  }
  
  @SuppressWarnings("resource") private void compileSourceCode(final String className, final String sourceCode) {
    FileWriter writer = null;
    final File sourceFile = new File(makePath(sourcePath, className + JAVA_SUFFIX));
    try {
      sourceFile.createNewFile();
      writer = new FileWriter(sourceFile);
      writer.write(sourceCode);
      writer.close();
      final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
      final StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
      final File classFile = new File(generatedClassPath(testClass));
      classFile.createNewFile();
      fileManager.setLocation(StandardLocation.CLASS_OUTPUT, Collections.singletonList(classFile));
      compiler
          .getTask(new StringWriter(), fileManager, null, null, null, fileManager.getJavaFileObjectsFromFiles(Collections.singletonList(sourceFile)))
          .call();
      fileManager.close();
    } catch (@SuppressWarnings("unused") final IOException ignore) {/**/}
  }

  @SuppressWarnings("resource") private Class<?> loadClass(final String $, final String sourceCode) {
    compileSourceCode($, sourceCode);
    try {
      return new URLClassLoader(new URL[] { new File(generatedClassPath(testClass)).toURI().toURL() })
          .loadClass(packageName("\\.", testClass) + "." + $);
    } catch (IOException | ClassNotFoundException ignore) {
      ignore.printStackTrace();
    }
    return Object.class;
  }

  private String getClassString(final Collection<TestLine> testsLines, final Collection<ImportLine> ls, final String className) {
    return packageHeaderString(packageName) + "\n" + importStatementString(ls) + "\n" + classHeaderString(className) + "\n" + testMethods(testsLines)
        + "\n}";
  }

  private String testMethods(final Collection<TestLine> testsLines) {
    return testsLines.stream().map(TestLine::generateTestMethod).reduce((acc, s1) -> acc + "\n\n" + s1).orElse("");
  }

  private String importStatementString(final Collection<ImportLine> ls) {
    return ls.stream().filter(λ -> !"import org.junit.runner.RunWith;".equals(λ.getContent().trim())).reduce("",
        (s, importLine) -> s + importLine.getContent() + "\n", (s, s2) -> s + s2);
  }

  private String classHeaderString(final String className) {
    return "@SuppressWarnings(\"static-method\")\npublic class " + className + " { \n";
  }

  private String packageHeaderString(final String packageNameString) {
    return "package " + packageNameString + ";";
  }
}
