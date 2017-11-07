package il.org.spartan.spartanizer.java.namespace.tables;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.tables.*;

/** Generates a table of the class fields
 * @author Dor Ma'ayan
 * @since 2017-10-16 */
public class TestingTable extends NominalTables {
  @SuppressWarnings("boxing") public static void main(final String[] args) {
    final HashMap<String, Integer> map = new HashMap<>();
    map.put("#Tests", 0);
    map.put("#JavaAsserts", 0);
    map.put("#JunitAsserts", 0);
    new GrandVisitor(args) {
      {
        listen(new Tapper() {
          @Override public void endLocation() {
            done(CurrentData.location);
          }
        });
      }

      void reset() {
        map.put("#Tests", 0);
        map.put("#JavaAsserts", 0);
        map.put("#JunitAsserts", 0);
      }
      protected void done(final String path) {
        summarize(path);
        reset();
      }
      public void summarize(final String path) {
        initializeWriter();
        if (map.get("#Tests") != 0) {
          table.col("Project", path).col("#Tests", map.get("#Tests")).col("#JavaAsserts", map.get("#JavaAsserts"))
              .col("#JunitAsserts", map.get("#JunitAsserts")).nl();
        }
      }
      void initializeWriter() {
        if (table == null)
          table = new Table(MetricsTable.class + "-" + corpus, outputFolder);
      }
    }.visitAll(new ASTVisitor(true) {
      @Override public boolean visit(final CompilationUnit ¢) {
        ¢.accept(new ASTVisitor() {
          @Override public boolean visit(final MethodDeclaration x) {
            if (x != null) {
              List<Annotation> anno = extract.annotations(x);
              for (Annotation a : anno) {
                // System.out.println((a.getTypeName().getFullyQualifiedName()));
                if (a.getTypeName().getFullyQualifiedName().equals("Test")) {
                  map.put("#Tests", map.get("#Tests") + 1);
                  List<Statement> lst = extract.statements(x.getBody());
                  for (Statement s : lst) {
                    if (iz.assertStatement(s))
                      map.put("#JavaAsserts", map.get("#JavaAsserts") + 1);
                    if (iz.junitAssert(s))
                      map.put("#JunitAsserts", map.get("#JunitAsserts") + 1);
                  }
                }
              }
            }
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
