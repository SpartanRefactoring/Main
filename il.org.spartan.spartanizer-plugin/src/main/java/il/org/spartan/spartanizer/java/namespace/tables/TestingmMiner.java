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

/** Test Mining for Msc.thesis
 * @author Dor Ma'ayan
 * @since 2019-01-08 */
public class TestingmMiner extends NominalTables {
  static boolean isJunitAnnotation(List<String> annotations) {
    String[] anno = { "After", "AfterClass", "Before", "BeforeClass" };
    List<String> annoList = Arrays.asList(anno);
    for (String s : annotations) {
      if (annoList.contains(s))
        return true;
    }
    return false;
  }
  static boolean isIgnoredTest(List<String> annotations) {
    String[] anno = { "Ignore" };
    List<String> annoList = Arrays.asList(anno);
    for (String s : annotations) {
      if (annoList.contains(s))
        return true;
    }
    return false;
  }
  static boolean isJunit4(CompilationUnit c) {
    List<ImportDeclaration> imports = extract.imports(c);
    for (ImportDeclaration i : imports) {
      if (i.getName().getFullyQualifiedName().contains("org.junit") && !isJunit5(c)) {
        return true;
      }
    }
    return false;
  }
  static boolean isJunit5(CompilationUnit c) {
    List<ImportDeclaration> imports = extract.imports(c);
    for (ImportDeclaration i : imports) {
      if (i.getName().getFullyQualifiedName().contains("org.junit.jupiter")) {
        return true;
      }
    }
    return false;
  }
  @SuppressWarnings({ "boxing", "unused" }) public static void main(final String[] args) throws Exception, UnsupportedEncodingException {
    final HashMap<String, Integer> map = new HashMap<>();
    map.put("#Tests", 0);
    map.put("#Junit4", 0);
    map.put("#Junit5", 0);
    map.put("#BeforeAfterAnnotations", 0);
    map.put("#IgnoredTests", 0);
    map.put("#JavaAsserts", 0);
    map.put("#JunitAsserts", 0);
    map.put("#HamcrestAsserts", 0);
    map.put("#IfStatements", 0);
    map.put("#ReturnStatements", 0);
    map.put("#0-Asseerts", 0);
    map.put("#1-Asseerts", 0);
    map.put("#2-Asseerts", 0);
    map.put("#3-Asseerts", 0);
    map.put("#4-Asseerts", 0);
    map.put("#5-Asseerts", 0);
    map.put("#6+-Asseerts", 0);
    map.put("#Files", 0);
    map.put("UsingMockito?", 0);
    map.put("#TestLoops", 0);
    map.put("#TryCatch", 0);
    map.put("#LinearTests", 0);
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
        map.put("#Junit4", 0);
        map.put("#Junit5", 0);
        map.put("#BeforeAfterAnnotations", 0);
        map.put("#IgnoredTests", 0);
        map.put("#JavaAsserts", 0);
        map.put("#JunitAsserts", 0);
        map.put("#IfStatements", 0);
        map.put("#ReturnStatements", 0);
        map.put("#0-Asseerts", 0);
        map.put("#1-Asseerts", 0);
        map.put("#2-Asseerts", 0);
        map.put("#3-Asseerts", 0);
        map.put("#4-Asseerts", 0);
        map.put("#5-Asseerts", 0);
        map.put("#6+-Asseerts", 0);
        map.put("#Files", 0);
        map.put("UsingMockito?", 0);
        map.put("#HamcrestAsserts", 0);
        map.put("#TestLoops", 0);
        map.put("#TryCatch", 0);
        map.put("#LinearTests", 0);
      }
      protected void done(final String path) {
        summarize(path);
        reset();
      }
      public void summarize(final String path) {
        initializeWriter();
        if (map.get("#Tests") != 0) {
          table.col("Project", path).col("#Files", map.get("#Files")).col("#Tests", map.get("#Tests")).col("#Junit4Classes", map.get("#Junit4"))
              .col("#Junit5Classes", map.get("#Junit5")).nl();
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
        List<ImportDeclaration> imports = extract.imports(¢);
        for (ImportDeclaration i : imports) {
          if (i.getName().getFullyQualifiedName().equals("org.mockito.Mockito")) {
            if (map.get("UsingMockito?") == 0)
              map.put("UsingMockito?", 1);
          }
        }
        if (isJunit4(¢))
          map.put("#Junit4", map.get("#Junit4") + 1);
        if (isJunit5(¢))
          map.put("#Junit5", map.get("#Junit5") + 1);
        // Random Sample of test classes
        Random rand = new Random();
        if (rand.nextInt(50) == 25 && (isJunit4(¢) || isJunit5(¢))) {
          writer.write("~~~~~~~Test Classs~~~~~~~\n \n");
          writer.write(¢.toString());
          writer.write("\n \n \n \n");
        }
        ¢.accept(new ASTVisitor() {
          @Override public boolean visit(final MethodDeclaration x) {
            if (x != null) {
              List<String> annotations = extract.annotations(x).stream().map(a -> a.getTypeName().getFullyQualifiedName())
                  .collect(Collectors.toList());
              if (isJunitAnnotation(annotations))
                map.put("#BeforeAfterAnnotations", map.get("#BeforeAfterAnnotations") + 1);
              if (isIgnoredTest(annotations))
                map.put("#IgnoredTests", map.get("#IgnoredTests") + 1);
              if (annotations.contains("Test") || (iz.typeDeclaration(x.getParent()) && az.typeDeclaration(x.getParent()).getSuperclassType() != null
                  && az.typeDeclaration(x.getParent()).getSuperclassType().toString().equals("TestCase"))) {
                final Int counter = new Int(); // asseerts counter
                final Int irregulars = new Int(); // asseerts counter
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
                    irregulars.step();
                    return true;
                  }
                  /** handle Returns */
                  @Override public boolean visit(final IfStatement x) {
                    map.put("#IfStatements", map.get("#IfStatements") + 1);
                    irregulars.step();
                    return true;
                  }
                  @Override public boolean visit(final ForStatement x) {
                    map.put("#TestLoops", map.get("#TestLoops") + 1);
                    irregulars.step();
                    return true;
                  }
                  @Override public boolean visit(final WhileStatement x) {
                    map.put("#TestLoops", map.get("#TestLoops") + 1);
                    irregulars.step();
                    return true;
                  }
                  @Override public boolean visit(final EnhancedForStatement x) {
                    map.put("#TestLoops", map.get("#TestLoops") + 1);
                    irregulars.step();
                    return true;
                  }
                  @Override public boolean visit(final TryStatement x) {
                    map.put("#TryCatch", map.get("#TryCatch") + 1);
                    irregulars.step();
                    return true;
                  }
                });
                if (irregulars.get() == 0)
                  map.put("#LinearTests", map.get("#LinearTests") + 1);
                if (counter.inner >= 0 && counter.inner < 6)
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
