package il.org.spartan.spartanizer.java.namespace.tables;

import org.eclipse.jdt.core.dom.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.tables.*;

/** Generates a table, counting constants in repositories
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-02-08 */
public class Table_Function_Argument_Names {
  static Table writer;
  public static int ints;
  public static int longs;
  public static int strings;

  private static void initializeWriter() {
    writer = new Table(Table_Function_Argument_Names.class);
  }
  public static void main(final String[] args) {
    initializeWriter();
    new ASTInFilesVisitor(args).visitAll(new ASTVisitor(true) {
      @Override public boolean visit(final CompilationUnit ¢) {
        ¢.accept(new ASTVisitor() {
          @Override public boolean visit(final SimpleName x) {
            writer.col("name", x.toString()).nl();
            return true;
          }
        });
        return super.visit(¢);
      }
    });
    writer.close();
    System.err.println(writer.description());
  }
}
