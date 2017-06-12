package il.org.spartan.spartanizer.research.metatester;

import static il.org.spartan.spartanizer.research.metatester.FileUtils.*;

import java.io.*;
import java.net.*;
import java.util.*;

import javax.tools.*;

import fluent.ly.*;
import il.org.spartan.utils.*;

/** @author Oren Afek
 * @since 2017-04-12 **/
@UnderConstruction("Oren Afek 13.4.17")
@SuppressWarnings("unused")
public interface TestClassGenerator {
  String JAVA_SUFFIX = ".java";

  default Class<?> generate(final String testClassName) {
    return Object.class;
  }
  default Class<?> generate(final String testClassName, final File originalSourceFile) {
    return Object.class;
  }
  @SuppressWarnings("resource") default void compileSourceCode(final String className, final String sourceCode, final Class<?> testClass,
      final String sourcePath) {
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
    } catch (final IOException ignore) {/**/}
  }
  @SuppressWarnings("resource") default Class<?> loadClass(final String $, final String sourceCode, final Class<?> testClass,
      final String sourcePath) {
    compileSourceCode($, sourceCode, testClass, sourcePath);
    try {
      return new URLClassLoader(new URL[] { new File(generatedClassPath(testClass)).toURI().toURL() })
          .loadClass(packageName("\\.", testClass) + "." + $);
    } catch (final IOException x) {
      note.io(x);
    } catch (final ClassNotFoundException x) {
      note.bug(x);
    }
    return Object.class;
  }
}
