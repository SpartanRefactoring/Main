package il.org.spartan.spartanizer.java.namespace.tables;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.tables.*;

/** Generates a table, counting constants in repositories
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-02-08 */
public class Table_Function_Argument_Names extends NominalTables {
  public static void main(final String[] args) {
    namePrevelance = new HashMap<>();
    new ASTInFilesVisitor(args) {
      {
        listen(new Listener() {
          @Override public void endLocation() {
            done(getCurrentLocation());
          }
        });
      }

      protected void done(final String path) {
        summarize();
        // reset();
        System.err.println(" " + path + " Done");
      }
      public void summarize() {
        initializeWriter();
        namePrevelance.entrySet().stream().sorted(Map.Entry.<String, Integer> comparingByValue().reversed())
            .forEachOrdered(λ -> table.col("Name", λ.getKey()).col("Prev", λ.getValue()).nl());
      }
      void initializeWriter() {
        if (table == null)
          table = new Table(Table_Function_Argument_Names.class + "-" + corpus, outputFolder);
      }
    }.visitAll(new ASTVisitor(true) {
      @Override public boolean visit(final CompilationUnit ¢) {
        ¢.accept(new ASTVisitor() {
          @Override @SuppressWarnings({ "boxing", "unchecked" }) public boolean visit(final MethodDeclaration x) {
            x.parameters().stream().forEach(p -> {
              String n = az.singleVariableDeclaration(az.astNode(p)).getName() + "";
              namePrevelance.put(n, !namePrevelance.containsKey(n) ? 1 : namePrevelance.get(n) + 1);
            });
            return true;
          }
        });
        return super.visit(¢);
      }
    });
    table.close();
    System.err.println(table.description());
  }
}
