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

/** Generates a table of the proveneance of different test methods
 * @author Dor Ma'ayan
 * @since 2017-10-16 */
public class TaxonomyTable extends NominalTables {
  static boolean isJunitAnnotation(List<String> annotations) {
    String[] anno = { "ParameterizedTest", "RepeatedTest", "TestFactory", "TestInstance", "TestTemplate", "DisplayName", "BeforeEach", "AfterEach",
        "BeforeAll", "AfterAll", "Nested", "Tag", "Disabled", "ExtendWith" };
    List<String> annoList = Arrays.asList(anno);
    for (String s : annotations) {
      if (annoList.contains(s))
        return true;
    }
    return false;
  }
  @SuppressWarnings("boxing") public static void main(final String[] args) throws Exception, UnsupportedEncodingException {
    final HashMap<String, Integer> map = new HashMap<>();
    map.put("#Tests", 0);
    map.put("#A", 0);
    map.put("#F", 0);
    map.put("#A*", 0);
    map.put("#F*", 0);
    map.put("#AF", 0);
    map.put("#A*F", 0);
    map.put("#AF*", 0);
    map.put("#A*F*", 0);
    map.put("#(A*F*)*", 0);
    PrintWriter writer = new PrintWriter("/Users/Dor/Desktop/TableTrending/raw.txt", "UTF-8");
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
        map.put("#A", 0);
        map.put("#F", 0);
        map.put("#A*", 0);
        map.put("#F*", 0);
        map.put("#AF", 0);
        map.put("#A*F", 0);
        map.put("#AF*", 0);
        map.put("#A*F*", 0);
        map.put("#(A*F*)*", 0);
      }
      protected void done(final String path) {
        summarize(path);
        reset();
      }
      public void summarize(final String path) {
        initializeWriter();
        if (map.get("#Tests") != 0) {
          table.col("Project", path).col("#Files", map.get("#Files")).col("#Tests", map.get("#Tests")).col("#A", map.get("#A")).nl();
          writer.write("~~~~~~~~~~~~~~~~~~~ Random Samplings from " + path + "~~~~~~~~~~~~~~~~~~~");
          writer.write("\n \n \n \n \n \n");
        }
      }
      void initializeWriter() {
        if (table == null)
          table = new Table(corpus, outputFolder);
      }
    }.visitAll(new ASTVisitor(true) {
      @Override public boolean visit(final CompilationUnit ¢) {
        map.put("#Files", map.get("#Files") + 1);
        ¢.accept(new ASTVisitor() {
          @Override public boolean visit(final MethodDeclaration x) {
            if (x != null) {
              List<String> annotations = extract.annotations(x).stream().map(a -> a.getTypeName().getFullyQualifiedName())
                  .collect(Collectors.toList());
              if (isJunitAnnotation(annotations))
                map.put("#JunitAnnotations", map.get("#JunitAnnotations") + 1);
              if (annotations.contains("Test") || (iz.typeDeclaration(x.getParent()) && az.typeDeclaration(x.getParent()).getSuperclassType() != null
                  && az.typeDeclaration(x.getParent()).getSuperclassType().toString().equals("TestCase"))) {
                // This is real test!
                Random rand = new Random();
                if (rand.nextInt(4) == 2) {
                  writer.write("~~~~~~~New Test~~~~~~~\n \n");
                  writer.write(x.toString());
                  writer.write("\n \n \n \n");
                }
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
                    if (iz.hamcrestAssert(x)) {
                      map.put("#HamcrestAsserts", map.get("#HamcrestAsserts") + 1);
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
                  @Override public boolean visit(final ForStatement x) {
                    map.put("#TestLoops", map.get("#TestLoops") + 1);
                    return true;
                  }
                  @Override public boolean visit(final WhileStatement x) {
                    map.put("#TestLoops", map.get("#TestLoops") + 1);
                    return true;
                  }
                  @Override public boolean visit(final EnhancedForStatement x) {
                    map.put("#TestLoops", map.get("#TestLoops") + 1);
                    return true;
                  }
                  @Override public boolean visit(final TryStatement x) {
                    map.put("#TryCatch", map.get("#TryCatch") + 1);
                    return true;
                  }
                });
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
