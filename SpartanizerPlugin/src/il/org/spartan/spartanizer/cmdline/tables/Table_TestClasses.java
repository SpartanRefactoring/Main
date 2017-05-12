package il.org.spartan.spartanizer.cmdline.tables;

import java.lang.reflect.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.tables.*;

/** TODO Matteo Orru': document class {@link }
 * @author Matteo Orru' {@code matteo.orru@cs.technion.ac.il}
 * @since 2017-02-21 */
public class Table_TestClasses extends Table_SummaryForPaper {
  private static Table testClassesWriter;
  static {
    clazz = Table_TestClasses.class;
  }

  public static void main(final String[] args)
      throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    DeprecatedFolderASTVisitor.main(args);
    testClassesWriter.close();
    System.err.println("Your output is in: " + system.tmp);
  }

  private void writeTestClasses(final String path) {
    compilationUnitRecords.stream().filter(λ -> λ.testCount() > 0)
        .forEachOrdered(λ -> testClassesWriter//
            .col("Project", path)//
            .col("AbsolutePath", λ.getPath())//
            .col("RelativePath", λ.getRelativePath())//
            .col("#TestMethods", λ.testCount())//
            .nl());
  }

  @Override protected void done(final String path) {
    if (testClassesWriter == null)
      initializeWriter();
    writeTestClasses(path);
    System.err.println("Your output is in: " + outputFolder);
  }

  private static void initializeWriter() {
    testClassesWriter = new Table(clazz);
  }
}
