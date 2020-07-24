package il.org.spartan.spartanizer.java.namespace.tables;

import java.util.HashMap;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.WhileStatement;

import il.org.spartan.spartanizer.cmdline.CurrentData;
import il.org.spartan.spartanizer.cmdline.GrandVisitor;
import il.org.spartan.spartanizer.cmdline.Tapper;
import il.org.spartan.tables.Table;

/** Generates a table of the class fields
 * @author Dor Ma'ayan
 * @since 2017-05-18 */
public class MetricsTable extends NominalTables {
  @SuppressWarnings("boxing") public static void main(final String[] args) {
    final HashMap<String, Integer> map = new HashMap<>();
    map.put("if", 0);
    map.put("for", 0);
    map.put("while", 0);
    map.put("break", 0);
    map.put("continue", 0);
    map.put("return", 0);
    map.put("variables", 0);
    map.put("catch", 0);
    map.put("method_invocation", 0);
    new GrandVisitor(args) {
      {
        listen(new Tapper() {
          @Override public void endLocation() {
            done(CurrentData.location);
          }
        });
      }

      void reset() {
        map.put("if", 0);
        map.put("for", 0);
        map.put("while", 0);
        map.put("break", 0);
        map.put("continue", 0);
        map.put("return", 0);
        map.put("variables", 0);
        map.put("catch", 0);
        map.put("method_invocation", 0);
      }
      protected void done(final String path) {
        summarize(path);
        reset();
      }
      public void summarize(final String path) {
        initializeWriter();
        table.col("Project", path).col("Ifs", map.get("if")).col("for", map.get("for")).col("while", map.get("while")).col("break", map.get("break"))
            .col("continue", map.get("continue")).col("returns", map.get("return")).col("variables", map.get("variables"))
            .col("catch", map.get("catch")).col("method_invoc", map.get("method_invocation")).nl();
      }
      void initializeWriter() {
        if (table == null)
          table = new Table(MetricsTable.class + "-" + corpus, outputFolder);
      }
    }.visitAll(new ASTVisitor(true) {
      @Override public boolean visit(final CompilationUnit ¢) {
        ¢.accept(new ASTVisitor() {
          @Override public boolean visit(final IfStatement x) {
            if (x != null)
              map.put("if", map.get("if") + 1);
            return true;
          }
          @Override public boolean visit(final ForStatement x) {
            if (x != null)
              map.put("for", map.get("for") + 1);
            return true;
          }
          @Override public boolean visit(final WhileStatement x) {
            if (x != null)
              map.put("while", map.get("while") + 1);
            return true;
          }
          @Override public boolean visit(@SuppressWarnings("unused") final ReturnStatement x) {
            map.put("return", map.get("return") + 1);
            return true;
          }
          @Override public boolean visit(@SuppressWarnings("unused") final SingleVariableDeclaration x) {
            map.put("variables", map.get("variables") + 1);
            return true;
          }
          @Override public boolean visit(@SuppressWarnings("unused") final VariableDeclarationFragment x) {
            map.put("variables", map.get("variables") + 1);
            return true;
          }
          @Override public boolean visit(@SuppressWarnings("unused") final BreakStatement x) {
            map.put("break", map.get("break") + 1);
            return true;
          }
          @Override public boolean visit(@SuppressWarnings("unused") final ContinueStatement x) {
            map.put("continue", map.get("continue") + 1);
            return true;
          }
          @Override public boolean visit(@SuppressWarnings("unused") final CatchClause x) {
            map.put("catch", map.get("catch") + 1);
            return true;
          }
          @Override public boolean visit(@SuppressWarnings("unused") final MethodInvocation x) {
            map.put("method_invocation", map.get("method_invocation") + 1);
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
