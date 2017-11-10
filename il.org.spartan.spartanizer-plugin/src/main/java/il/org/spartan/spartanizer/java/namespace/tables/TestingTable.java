package il.org.spartan.spartanizer.java.namespace.tables;

import java.io.*;
import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.tables.*;
import il.org.spartan.utils.*;

/** Generates a table of the class fields
 * @author Dor Ma'ayan
 * @since 2017-10-16 */
public class TestingTable extends NominalTables {
  @SuppressWarnings("boxing") public static void main(final String[] args) throws Exception, UnsupportedEncodingException {
    final HashMap<String, Integer> map = new HashMap<>();
    map.put("#Tests", 0);
    map.put("#JavaAsserts", 0);
    map.put("#JunitAsserts", 0);
    map.put("#IfStatements", 0);
    map.put("#ReturnStatements", 0);
    map.put("#1-Asseerts", 0);
    map.put("#2-Asseerts", 0);
    map.put("#3-Asseerts", 0);
    map.put("#4-Asseerts", 0);
    map.put("#5-Asseerts", 0);
    map.put("#6+-Asseerts", 0);
    map.put("#Files", 0);
    PrintWriter writer = new PrintWriter("/Users/Dor/Desktop/TestingTables/raw.txt", "UTF-8");
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
        map.put("#IfStatements", 0);
        map.put("#ReturnStatements", 0);
        map.put("#1-Asseerts", 0);
        map.put("#2-Asseerts", 0);
        map.put("#3-Asseerts", 0);
        map.put("#4-Asseerts", 0);
        map.put("#5-Asseerts", 0);
        map.put("#6+-Asseerts", 0);
        map.put("#Files", 0);
      }
      protected void done(final String path) {
        summarize(path);
        reset();
      }
      public void summarize(final String path) {
        initializeWriter();
        if (map.get("#Tests") != 0) {
          table.col("Project", path).col("#Files", map.get("#Files")).col("#Tests", map.get("#Tests")).col("#JavaAsserts", map.get("#JavaAsserts"))
              .col("#JunitAsserts", map.get("#JunitAsserts")).col("#IfStatements", map.get("#IfStatements"))
              .col("#ReturnStatements", map.get("#ReturnStatements")).col("#1-Asseerts", map.get("#1-Asseerts"))
              .col("#2-Asseerts", map.get("#2-Asseerts")).col("#3-Asseerts", map.get("#3-Asseerts")).col("#4-Asseerts", map.get("#4-Asseerts"))
              .col("#5-Asseerts", map.get("#5-Asseerts")).col("#6+-Asseerts", map.get("#6+-Asseerts")).nl();
        }
      }
      void initializeWriter() {
        if (table == null)
          table = new Table(corpus, outputFolder);
      }
    }.visitAll(new ASTVisitor(true) {
      @Override public boolean visit(final CompilationUnit ¢) {
        writer.write("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~New Test~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n \n");
        writer.write(¢.toString());
        writer.write("\n \n \n \n");
        map.put("#Files", map.get("#Files") + 1);
        ¢.accept(new ASTVisitor() {
          @Override public boolean visit(final MethodDeclaration x) {
            if (x != null) {
              List<String> annotations = extract.annotations(x).stream().map(a -> a.getTypeName().getFullyQualifiedName())
                  .collect(Collectors.toList());
              if (annotations.contains("Test") || (iz.typeDeclaration(x.getParent()) && az.typeDeclaration(x.getParent()).getSuperclassType() != null
                  && az.typeDeclaration(x.getParent()).getSuperclassType().toString().equals("TestCase"))) {
                final Int counter = new Int(); // asseerts counter
                map.put("#Tests", map.get("#Tests") + 1);
                x.accept(new ASTVisitor() {
                  /** handle regular assert */
                  @Override public boolean visit(final AssertStatement x) {
                    map.put("#JavaAsserts", map.get("#JavaAsserts") + 1);
                    counter.step();
                    return true;
                  }
                  /** handle JunitAssert */
                  @Override public boolean visit(final ExpressionStatement x) {
                    if (iz.junitAssert(x)) {
                      map.put("#JunitAsserts", map.get("#JunitAsserts") + 1);
                      counter.step();
                    }
                    return true;
                  }
                  /** handle Returns */
                  @Override public boolean visit(final ReturnStatement x) {
                    map.put("#ReturnStatements", map.get("#ReturnStatements") + 1);
                    return true;
                  }
                  /** handle Returns */
                  @Override public boolean visit(final IfStatement x) {
                    map.put("#IfStatements", map.get("#IfStatements") + 1);
                    return true;
                  }
                });
                //if (counter.inner == 0) {

                //}
                if (counter.inner > 0 && counter.inner < 6)
                  map.put("#" + counter.inner + "-Asseerts", map.get("#" + counter.inner + "-Asseerts") + 1);
                else if (counter.inner > 5)
                  map.put("#6+-Asseerts", map.get("#6+-Asseerts") + 1);
              }
            }
            return true;
          }
        });
        return super.visit(¢);
      }
    });
    table.close();
    writer.close();
    System.err.println(table.description());
  }
}
