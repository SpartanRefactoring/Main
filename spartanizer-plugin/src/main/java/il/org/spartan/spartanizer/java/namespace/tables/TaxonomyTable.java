package il.org.spartan.spartanizer.java.namespace.tables;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;

import il.org.spartan.spartanizer.ast.navigate.extract;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.cmdline.CurrentData;
import il.org.spartan.spartanizer.cmdline.GrandVisitor;
import il.org.spartan.spartanizer.cmdline.Tapper;
import il.org.spartan.tables.Table;

/** Generates a table of the proveneance of different test methods
 * @author Dor Ma'ayan
 * @since 2017-10-16 */
public class TaxonomyTable extends NominalTables {
  @SuppressWarnings("boxing") public static void main(final String[] args) throws Exception {
    final HashMap<String, Integer> map = new HashMap<>();
    map.put("#Files", 0);
    map.put("#Tests", 0);
    map.put("#A", 0);
    map.put("#F", 0);
    map.put("#A+", 0);
    map.put("#F+", 0);
    map.put("#FA", 0);
    map.put("#F+A", 0);
    map.put("#FA+", 0);
    map.put("#F+A+", 0);
    map.put("#(F+A+)+", 0);
    map.put("#NonLinear", 0);
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
        map.put("#A+", 0);
        map.put("#F+", 0);
        map.put("#FA", 0);
        map.put("#F+A", 0);
        map.put("#FA+", 0);
        map.put("#F+A+", 0);
        map.put("#(F+A+)+", 0);
        map.put("#NonLinear", 0);
      }
      protected void done(final String path) {
        summarize(path);
        reset();
      }
      double rel(String s) {
        return (double) map.get(s) / map.get("#Tests");
      }
      public void summarize(final String path) {
        initializeWriter();
        if (map.get("#Tests") != 0)
			table.col("Project", path).col("#Files", map.get("#Files")).col("#Tests", map.get("#Tests"))
					.col("#A", rel("#A")).col("#F", rel("#F")).col("#A+", rel("#A+")).col("#F+", rel("#F+"))
					.col("#FA", rel("#FA")).col("#F+A", rel("#F+A")).col("#FA+", rel("#FA+")).col("#F+A+", rel("#F+A+"))
					.col("#(F+A+)+", rel("#(F+A+)+")).col("#NonLinear", rel("#NonLinear")).nl();
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
                map.put("#Tests", map.get("#Tests") + 1);
                List<Statement> statements = extract.statements(x.getBody());
                boolean linear = true;
                int assertionCounter = 0;
                int fixtureCounter = 0;
                boolean fixturesBefore = true;
                boolean assertionsStarted = false;
                boolean exchanges = false;
                for (Statement s : statements) {
                  if (iz.ifStatement(s) || iz.forStatement(s) || iz.enhancedFor(s) || iz.breakStatement(s) || iz.whileStatement(s)
                      || iz.tryStatement(s) || iz.returnStatement(s)) {
                    linear = false;
                    break;
                  }
                  if (iz.assertStatement(s) || iz.junitAssert(s) || iz.hamcrestAssert(s)) {
                    assertionCounter++;
                    assertionsStarted = true;
                    if (!fixturesBefore)
                      exchanges = true;
                  } else {
                    fixtureCounter++;
                    if (assertionsStarted)
                      fixturesBefore = false;
                  }
                }
                if (linear) {
                  if (assertionCounter == 1 && fixtureCounter == 0)
					map.put("#A", map.get("#A") + 1);
                  if (assertionCounter == 0 && fixtureCounter == 1)
					map.put("#F", map.get("#F") + 1);
                  if (assertionCounter >= 1 && fixtureCounter == 0)
					map.put("#A+", map.get("#A+") + 1);
                  if (assertionCounter == 0 && fixtureCounter >= 1)
					map.put("#F+", map.get("#F+") + 1);
                  if (assertionCounter == 1 && fixtureCounter == 1 && fixturesBefore)
					map.put("#FA", map.get("#FA") + 1);
                  if (assertionCounter >= 1 && fixtureCounter == 1 && fixturesBefore)
					map.put("#FA+", map.get("#FA+") + 1);
                  if (assertionCounter == 1 && fixtureCounter >= 1 && fixturesBefore)
					map.put("#F+A", map.get("#F+A") + 1);
                  if (assertionCounter >= 1 && fixtureCounter >= 1 && fixturesBefore)
					map.put("#F+A+", map.get("#F+A+") + 1);
                  if (exchanges)
					map.put("#(F+A+)+", map.get("#(F+A+)+") + 1);
                } else
					map.put("#NonLinear", map.get("#NonLinear") + 1);
              }
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
