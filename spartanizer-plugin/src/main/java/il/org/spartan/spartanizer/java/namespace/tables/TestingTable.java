package il.org.spartan.spartanizer.java.namespace.tables;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

import il.org.spartan.spartanizer.ast.navigate.extract;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.cmdline.CurrentData;
import il.org.spartan.spartanizer.cmdline.GrandVisitor;
import il.org.spartan.spartanizer.cmdline.Tapper;
import il.org.spartan.tables.Table;
import il.org.spartan.utils.Int;

/** Generates a table of the class fields
 * @author Dor Ma'ayan
 * @since 2017-10-16 */
public class TestingTable extends NominalTables {
  static boolean isJunitAnnotation(List<String> annotations) {
    String[] anno = { "After", "AfterClass", "Before", "BeforeClass" };
    List<String> annoList = Arrays.asList(anno);
    for (String s : annotations)
		if (annoList.contains(s))
			return true;
    return false;
  }
  static boolean isIgnoredTest(List<String> annotations) {
    String[] anno = { "Ignore" };
    List<String> annoList = Arrays.asList(anno);
    for (String s : annotations)
		if (annoList.contains(s))
			return true;
    return false;
  }
  @SuppressWarnings({ "boxing", "unused" }) public static void main(final String[] args) throws Exception, UnsupportedEncodingException {
    final HashMap<String, Integer> map = new HashMap<>();
    map.put("#Tests", 0);
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
          table.col("Project", path).col("#Files", map.get("#Files")).col("#Tests", map.get("#Tests"))
              .col("#BeforeAfterAnnotations", map.get("#BeforeAfterAnnotations")).col("#IgnoredTests", map.get("#IgnoredTests"))
              .col("#JavaAsserts", map.get("#JavaAsserts")).col("#JunitAsserts", map.get("#JunitAsserts"))
              .col("#HamcrestAsserts", map.get("#HamcrestAsserts")).col("#TryCatch", map.get("#TryCatch"))
              .col("#IfStatements", map.get("#IfStatements")).col("#ReturnStatements", map.get("#ReturnStatements"))
              .col("#TestLoops", map.get("#TestLoops")).col("#0-Asseerts", map.get("#0-Asseerts")).col("#1-Asseerts", map.get("#1-Asseerts"))
              .col("#2-Asseerts", map.get("#2-Asseerts")).col("#3-Asseerts", map.get("#3-Asseerts")).col("#4-Asseerts", map.get("#4-Asseerts"))
              .col("#5-Asseerts", map.get("#5-Asseerts")).col("#6+-Asseerts", map.get("#6+-Asseerts")).col("UsingMockito?", map.get("UsingMockito?"))
              .col("#LinearTests", map.get("#LinearTests")).nl();
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
        for (ImportDeclaration i : imports)
			if (i.getName().getFullyQualifiedName().equals("org.mockito.Mockito"))
				if (map.get("UsingMockito?") == 0)
					map.put("UsingMockito?", 1);
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
                // This is real test!
                // Random rand = new Random();
                // if (rand.nextInt(4) == 2) {
                writer.write("~~~~~~~New Test~~~~~~~\n \n");
                writer.write(x.toString());
                writer.write("\n \n \n \n");
                // }
                final Int counter = new Int(); // asseerts counter
                final Int irregulars = new Int(); // asseerts counter
                map.put("#Tests", map.get("#Tests") + 1);
                x.accept(new ASTVisitor() {
                  /** handle regular assert */
                  @Override public boolean visit(final AssertStatement s) {
                    map.put("#JavaAsserts", map.get("#JavaAsserts") + 1);
                    counter.step();
                    return true;
                  }
                  /** handle JunitAssert */
                  @Override public boolean visit(final ExpressionStatement s) {
                    if (iz.junitAssert(s)) {
                      map.put("#JunitAsserts", map.get("#JunitAsserts") + 1);
                      counter.step();
                    }
                    if (iz.hamcrestAssert(s))
						map.put("#HamcrestAsserts", map.get("#HamcrestAsserts") + 1);
                    return true;
                  }
                  /** handle Returns */
                  @Override public boolean visit(final ReturnStatement s) {
                    map.put("#ReturnStatements", map.get("#ReturnStatements") + 1);
                    irregulars.step();
                    return true;
                  }
                  /** handle Returns */
                  @Override public boolean visit(final IfStatement s) {
                    map.put("#IfStatements", map.get("#IfStatements") + 1);
                    irregulars.step();
                    return true;
                  }
                  @Override public boolean visit(final ForStatement s) {
                    map.put("#TestLoops", map.get("#TestLoops") + 1);
                    irregulars.step();
                    return true;
                  }
                  @Override public boolean visit(final WhileStatement s) {
                    map.put("#TestLoops", map.get("#TestLoops") + 1);
                    irregulars.step();
                    return true;
                  }
                  @Override public boolean visit(final EnhancedForStatement s) {
                    map.put("#TestLoops", map.get("#TestLoops") + 1);
                    irregulars.step();
                    return true;
                  }
                  @Override public boolean visit(final TryStatement s) {
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
