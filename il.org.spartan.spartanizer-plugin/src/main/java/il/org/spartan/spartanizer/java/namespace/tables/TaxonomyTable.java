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
    map.put("#Files", 0);
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
        map.put("#Files", 0);
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
              if (annotations.contains("Test") || (iz.typeDeclaration(x.getParent()) && az.typeDeclaration(x.getParent()).getSuperclassType() != null
                  && az.typeDeclaration(x.getParent()).getSuperclassType().toString().equals("TestCase"))) {
                // This is real test!
                final Int counter = new Int(); // asseerts counter
                map.put("#Tests", map.get("#Tests") + 1);
                x.accept(new ASTVisitor() {
                  /** handle regular assert */
                  @Override public boolean visit(final AssertStatement x) {
                    counter.step();
                    return true;
                  }
                  /** handle JunitAssert */
                  @Override public boolean visit(final ExpressionStatement x) {
                    if (iz.junitAssert(x)) {
                      counter.step();
                    }
                    if (iz.hamcrestAssert(x)) {
                      counter.step();
                    }
                    return true;
                  }
                });
                if (counter.inner() == 1)
                  map.put("#A", map.get("A") + 1);
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
