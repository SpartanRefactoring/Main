package il.org.spartan.spartanizer.cmdline.tables;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.lang.reflect.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.tables.*;

/** Generates a table, counting constants in repositories
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-02-08 */
public class Table_Constants extends DeprecatedFolderASTVisitor {
  private static Table writer;
  public static int ints;
  public static int longs;
  public static int strings;
  static {
    clazz = Table_Constants.class;
  }

  private static void initializeWriter() {
    writer = new Table(Table_Constants.class);
  }

  public static void main(final String[] args)
      throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    DeprecatedFolderASTVisitor.main(args);
    writer.close();
  }

  @Override protected void done(final String path) {
    summarizeNPStatistics(path);
    System.err.println(" " + path + " Done");
  }

  @Override public boolean visit(final FieldDeclaration ¢) {
    if (iz.constant(¢))//
      if (iz.intType(type(¢)))
        ++ints;
      else if (iz.stringType(type(¢)))
        ++strings;
      else if (iz.longType(type(¢)))
        ++longs;
    return super.visit(¢);
  }

  public static void summarizeNPStatistics(final String path) {
    if (writer == null)
      initializeWriter();
    writer.col("Project", path);
    writer//
        .col("Ints", ints)//
        .col("Longs", ints)//
        .col("Strings", strings)//
        .col("Total", ints + strings);
    writer.nl();
    ints = longs = strings = 0;
  }
}
