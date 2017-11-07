package il.org.spartan.spartanizer.java.namespace.tables;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.tables.*;

/** Generates a table of the class fields
 * @author Dor Ma'ayan
 * @since 2017-10-6 */
public class FunctionsDeepInspection extends NominalTables {
  @SuppressWarnings("boxing") public static void main(final String[] args) {
    final HashMap<String, Integer> map = new HashMap<>();
    map.put("MethodDeclaration", 0);
    map.put("0-Arguments", 0);
    map.put("1-Arguments", 0);
    map.put("2-Arguments", 0);
    map.put("3-Arguments", 0);
    map.put("4+-Arguments", 0);
    new GrandVisitor(args) {
      {
        listen(new Tapper() {
          @Override public void endLocation() {
            done(CurrentData.location);
          }
        });
      }

      void reset() {
        map.put("MethodDeclaration", 0);
        map.put("0-Arguments", 0);
        map.put("1-Arguments", 0);
        map.put("2-Arguments", 0);
        map.put("3-Arguments", 0);
        map.put("4+-Arguments", 0);
      }
      protected void done(final String path) {
        summarize(path);
        reset();
      }
      public void summarize(final String path) {
        initializeWriter();
        table.col("Project", path).col("MethodDeclarations", map.get("MethodDeclaration")).col("0-Arguments", map.get("0-Arguments"))
            .col("1-Arguments", map.get("1-Arguments")).col("2-Arguments", map.get("2-Arguments")).col("3-Arguments", map.get("3-Arguments"))
            .col("4+-Arguments", map.get("4+-Arguments")).nl();
      }
      void initializeWriter() {
        if (table == null)
          table = new Table(FunctionsDeepInspection.class + "-" + corpus, outputFolder);
      }
    }.visitAll(new ASTVisitor(true) {
      @Override public boolean visit(final CompilationUnit ¢) {
        ¢.accept(new ASTVisitor() {
          @Override public boolean visit(final MethodDeclaration x) {
            if (x != null) {
              int numArgs = x.parameters().size();
              if (numArgs < 4)
                map.put(numArgs + "-Arguments", map.get(numArgs + "-Arguments") + 1);
              else
                map.put("4+-Arguments", map.get("4+-Arguments") + 1);
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
