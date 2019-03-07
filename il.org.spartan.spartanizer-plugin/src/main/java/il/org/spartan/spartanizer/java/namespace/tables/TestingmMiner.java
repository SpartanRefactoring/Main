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

/** Filter projects which does not contain any Junit test (Dor Msc.)
 * @author Dor Ma'ayan
 * @since 2019-03-07 */
public class TestingmMiner extends TestTables {
  @SuppressWarnings({ "unused" }) public static void main(final String[] args) throws Exception, UnsupportedEncodingException {
    final Set<String> testMethods = new HashSet<>();
    // Maps from method names to counters
    final HashMap<String, Integer> assertionMapCounter = new HashMap<>();
    final HashMap<String, Integer> ifElseMapCounter = new HashMap<>();
    final HashMap<String, Integer> loopMapCounter = new HashMap<>();
    final HashMap<String, Integer> tryCatchMapCounter = new HashMap<>();
    final HashMap<String, Integer> printMapCounter = new HashMap<>();
    final HashMap<String, Integer> throwsExceptionMapCounter = new HashMap<>();
    new GrandVisitor(args) {
      {
        listen(new Tapper() {
          @Override public void endLocation() {
            done(CurrentData.location);
          }
        });
      }

      void reset() {
        testMethods.clear();
        assertionMapCounter.clear();
        ifElseMapCounter.clear();
        tryCatchMapCounter.clear();
      }
      protected void done(final String path) {
        summarize(path);
        reset();
      }
      public void summarize(final String path) {
        initializeWriter();
        for (String method : testMethods) {
          table.col("Project", path).col("MethodName", method).col("#Assrtions", assertionMapCounter.get(method))
              .col("#Conditions", ifElseMapCounter.get(method)).col("#TryCatch", tryCatchMapCounter.get(method)).nl();
        }
      }
      void initializeWriter() {
        if (table == null)
          table = new Table(corpus, outputFolder);
      }
    }.visitAll(new ASTVisitor(true) {
      @Override public boolean visit(final CompilationUnit ¢) {
        ¢.accept(new ASTVisitor() {
          @Override public boolean visit(final TypeDeclaration x) {
            if (isJunit4(¢) || isJunit5(¢)) {
              x.accept(new ASTVisitor(true) {
                @SuppressWarnings("boxing") @Override public boolean visit(final MethodDeclaration m) {
                  List<String> annotations = extract.annotations(m).stream().map(a -> a.getTypeName().getFullyQualifiedName())
                      .collect(Collectors.toList());
                  if (annotations.contains("Test")) {
                    testMethods.add(extract.name(x) + "." + extract.name(m));
                  }
                  // Define per test method counters
                  final Int assertionCounter = new Int();
                  final Int ifElseCounter = new Int();
                  final Int tryCatchCounter = new Int();
                  // Traverse over the test method AST
                  m.accept(new ASTVisitor() {
                    @Override public boolean visit(final ExpressionStatement a) {
                      if (iz.junitAssert(a) || iz.hamcrestAssert(a)) {
                        assertionCounter.step();
                      }
                      return true;
                    }
                    @Override public boolean visit(final IfStatement a) {
                      ifElseCounter.step();
                      return true;
                    }
                    @Override public boolean visit(final TryStatement a) {
                      tryCatchCounter.step();
                      return true;
                    }
                  });
                  // Then, put the local counters in the global map before analyzing the next
                  // method
                  assertionMapCounter.put(extract.name(x) + "." + extract.name(m), assertionCounter.get());
                  ifElseMapCounter.put(extract.name(x) + "." + extract.name(m), ifElseCounter.get());
                  tryCatchMapCounter.put(extract.name(x) + "." + extract.name(m), tryCatchCounter.get());
                  return true;
                }
              });
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
