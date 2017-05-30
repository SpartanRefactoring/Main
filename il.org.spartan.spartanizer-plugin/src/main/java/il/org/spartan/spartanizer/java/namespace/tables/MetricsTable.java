package il.org.spartan.spartanizer.java.namespace.tables;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.tables.*;

/** Generates a table of the class fields
 * @author Dor Ma'ayan
 * @since 2017-05-18 */
public class MetricsTable extends NominalTables {
  @SuppressWarnings("boxing") public static void main(final String[] args) {
    final HashMap<String, Integer> map = new HashMap<>();
    map.put("if", 0);
    map.put("loop", 0);
    map.put("return", 0);
    new ASTInFilesVisitor(args) {
      {
        listen(new Listener() {
          @Override public void endLocation() {
            done(getCurrentLocation());
          }
        });
      }

      void reset() {
        map.put("if", 0);
        map.put("loop", 0);
        map.put("return", 0);
      }
      protected void done(final String path) {
        summarize(path);
        reset();
        System.err.println(" " + path + " Done");
      }
      public void summarize(final String path) {
        initializeWriter();
        table.col("Project", path).col("Ifs", map.get("if")).col("Loops", map.get("loop"))
        .col("returns",map.get("return")).nl();
      }
      void initializeWriter() {
        if (table == null)
          table = new Table(MetricsTable.class + "-" + corpus, outputFolder);
      }
    }.visitAll(new ASTVisitor(true) {
      @Override public boolean visit(final CompilationUnit ¢) {
        ¢.accept(new ASTVisitor() {
          @Override public boolean visit(@SuppressWarnings("unused") final IfStatement x) {
            if(x!=null)
            map.put("if", map.get("if") + 1);
            return true;
          }
          @Override public boolean visit(@SuppressWarnings("unused") final ForStatement x) {
            if(x!=null)
            map.put("loop", map.get("loop") + 1);
            return true;
          }
          @Override public boolean visit(@SuppressWarnings("unused") final WhileStatement x) {
            if(x!=null)
            map.put("loop", map.get("loop") + 1);
            return true;
          }
          @Override public boolean visit(@SuppressWarnings("unused") final ReturnStatement x) {
            map.put("return", map.get("return") + 1);
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
