package il.org.spartan.spartanizer.java.namespace.tables;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;

import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.cmdline.CurrentData;
import il.org.spartan.spartanizer.cmdline.GrandVisitor;
import il.org.spartan.spartanizer.cmdline.Tapper;
import il.org.spartan.tables.Table;

/** Generates a table of the class fields
 * @author Dor Ma'ayan
 * @since 2017-05-18 */
public class Table_Class_Field_Names extends NominalTables {
  public static void main(final String[] args) {
    namePrevelance = new HashMap<>();
    new GrandVisitor(args) {
      {
        listen(new Tapper() {
          @Override public void endLocation() {
            done(CurrentData.location);
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
          table = new Table(Table_Class_Field_Names.class + "-" + corpus, outputFolder);
      }
    }.visitAll(new ASTVisitor(true) {
      @Override public boolean visit(final CompilationUnit ¢) {
        ¢.accept(new ASTVisitor() {
          @Override @SuppressWarnings({ "boxing", "unchecked" }) public boolean visit(final FieldDeclaration x) {
            x.fragments().stream().forEach(p -> {
              final String n = az.variableDeclrationFragment(az.astNode(p)).getName().getIdentifier();
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
