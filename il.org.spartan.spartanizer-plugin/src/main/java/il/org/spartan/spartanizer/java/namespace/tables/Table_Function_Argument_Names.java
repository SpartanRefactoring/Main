package il.org.spartan.spartanizer.java.namespace.tables;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.utils.tdd.*;
import il.org.spartan.tables.*;

/** Generates a table of funtion arguments
 * @author Dor Ma'ayan
 * @since 2017-05-18 */
public class Table_Function_Argument_Names extends NominalTables {
  public static void main(final String[] args) {
    namePrevelance = new HashMap<>();
    new GrandVisitor(args) {
      {
        listen(new Tapper() {
          @Override public void endLocation() {
            done(current.location);
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
            .forEachOrdered(λ -> table.col("Name", λ.getKey().split("--")[0]).col("Type", λ.getKey().split("--")[1])
                .col("#Args", λ.getKey().split("--")[2]).col("RetType", λ.getKey().split("--")[3]).col("Throws", λ.getKey().split("--")[4])
                .col("Ifs", λ.getKey().split("--")[5]).col("Loops", λ.getKey().split("--")[6])
                // .col("Suggestion", abbreviate.it(λ.getKey().split("--")[1]))
                // .col("Strategy",
                // λ.getKey().split("--")[0].toLowerCase().equals(λ.getKey().split("--")[1].toLowerCase())
                // ? "Type" : "Unkown")
                .col("Prev", λ.getValue()).nl());
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
              final String n1 = az.singleVariableDeclaration(az.astNode(p)).getName() + "",
                  n2 = az.singleVariableDeclaration(az.astNode(p)).getType() + "", n3 = x.parameters().size() + "", n4 = x.getReturnType2() + "",
                  n5 = x.thrownExceptionTypes().isEmpty() + "", n6 = (enumerate.ifStatements(x) != 0) + "", n7 = (enumerate.loops(x) != 0) + "";
              namePrevelance.put(n1 + "--" + n2 + "--" + n3 + "--" + n4 + "--" + n5 + "--" + n6 + "--" + n7,
                  !namePrevelance.containsKey(n1 + "--" + n2 + "--" + n3 + "--" + n4 + "--" + n5 + "--" + n6 + "--" + n7) ? 1
                      : namePrevelance.get(n1 + "--" + n2 + "--" + n3 + "--" + n4 + "--" + n5 + "--" + n6 + "--" + n7) + 1);
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
