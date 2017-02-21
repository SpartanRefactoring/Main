/**
 * 
 */
package il.org.spartan.spartanizer.cmdline.tables;

import java.lang.reflect.*;

import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.tables.*;

/**
 * TODO Matteo Orru': document class {@link }
 * @author Matteo Orru' <tt>matteo.orru@cs.technion.ac.il</tt>
 * @since 2017-02-21
 */
public class Table_TestClasses extends Table_SummaryForPaper {
  
  private static Table testClassesWriter;
  
  static {
    clazz = Table_TestClasses.class;
  }
  
  public static void main(final String[] args)
      throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    FolderASTVisitor.main(args);
    testClassesWriter.close();
    System.err.println("Your output is in: " + Table.temporariesFolder);
  }
  
  private void writeTestClasses(String path) {
    for(final CompilationUnitRecord ¢: CompilationUnitRecords)
      if(¢.testCount() > 0)
        testClassesWriter//
        .col("Project", path)//
        .col("AbsolutePath", ¢.getPath())//
        .col("RelativePath", ¢.getRelativePath())//
        .nl();   
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
