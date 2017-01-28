package il.org.spartan.spartanizer.cmdline.tables;

import java.lang.reflect.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.research.util.*;
import il.org.spartan.tables.*;

/** TODO: orimarco <tt>marcovitch.ori@gmail.com</tt> please add a description
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-14 */
public class Table_Experminetal_rIndex extends TableReusabilityIndices {
  private static Table lWriter; // coverage
  protected static int totalMethodInvocations;
  protected static int totalMethodDeclarations;
  protected static int h;
  static {
    clazz = Table_Experminetal_rIndex.class;
  }

  public static void main(final String[] args)
      throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    FolderASTVisitor.main(args);
    lWriter.close();
  }

  @Override public boolean visit(final MethodInvocation ¢) {
    ++totalMethodInvocations;
    return super.visit(¢);
  }

  @Override public boolean visit(final MethodDeclaration ¢) {
    ++totalMethodDeclarations;
    return super.visit(¢);
  }

  @Override protected void done(final String path) {
    super.done(path);
    summarize(path);
    clearAll();
    System.err.println("Output is in: " + Table.temporariesFolder + path);
  }

  private static void clearAll() {
    totalMethodDeclarations = totalMethodInvocations = 0;
  }

  private static void initializeWriter() {
    lWriter = new Table(Table_Experminetal_rIndex.class);
  }

  public void summarize(final String path) {
    if (lWriter == null)
      initializeWriter();
    h = rMethod();
    lWriter.col("Project", path);
    lWriter.col("Methods", methods());
    lWriter.col("Invocations", invocations());
    lWriter.col("h", h);
    lWriter.col("h", h * h);
    lWriter.col("α", α());
    lWriter.nl();
  }

  private static double α() {
    return format.decimal(safe.div(invocations(), h * h));
  }

  private static int invocations() {
    return totalMethodInvocations;
  }

  private static int methods() {
    return totalMethodDeclarations;
  }
}
