package il.org.spartan.spartanizer.java.namespace.tables;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.cmdline.visitor.*;
import il.org.spartan.tables.*;

/** Generates a table of all the names
 * @author Dor Ma'ayan
 * @since 2017-05-18 */
public class Table_All_Names extends NominalTables {
  public static void main(final String[] args) {
    namePrevelance = new HashMap<>();
    new MasterVisitor(args) {
      {
        listen(new Tapper() {
          @Override public void endLocation() {
            done(location);
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
          table = new Table(Table_All_Names.class + "-" + corpus, outputFolder);
      }
    }.visitAll(new ASTVisitor(true) {
      @Override public boolean visit(final CompilationUnit ¢) {
        ¢.accept(new ASTVisitor() {
          @Override @SuppressWarnings("boxing") public boolean visit(final SimpleName x) {
            final String n = x.getIdentifier();
            namePrevelance.put(n, !namePrevelance.containsKey(n) ? 1 : namePrevelance.get(n) + 1);
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
