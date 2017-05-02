package il.org.spartan.spartanizer.cmdline.tables;

import static java.util.stream.Collectors.*;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.research.analyses.*;
import il.org.spartan.spartanizer.research.util.*;
import il.org.spartan.spartanizer.utils.*;
import il.org.spartan.tables.*;

/** TODO Matteo Orru': document class {@link }
 * @author Matteo Orru' {@code matteo.orru@cs.technion.ac.il}
 * @since 2017-02-10 */
public class Table_SummaryForPaper extends DeprecatedFolderASTVisitor {
  private static Table writer;
  @SuppressWarnings("unused") private static final HashMap<String, HashSet<String>> packageMap = new HashMap<>();
  private static final Collection<String> packages = new HashSet<>();
  protected final Collection<CompilationUnitRecord> compilationUnitRecords = new Stack<>();
  private final Stack<ClassRecord> classRecords = new Stack<>();
  protected static final SortedMap<Integer, List<CompilationUnitRecord>> CUStatistics = new TreeMap<>(Integer::compareTo);
  protected static final SortedMap<Integer, List<ClassRecord>> classStatistics = new TreeMap<>(Integer::compareTo);
  static final Nanonizer nanonizer = new Nanonizer();
  static {
    clazz = Table_SummaryForPaper.class;
    // Logger.subscribe(Table_SummaryForPaper::logNanoContainingMethodInfo);
  }

  public static void main(final String[] args)
      throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    DeprecatedFolderASTVisitor.main(args);
    writer.close();
    System.err.println("Your output is in: " + system.tmp);
  }

  @Override public boolean visit(final CompilationUnit ¢) {
    compilationUnitRecords.add(foo(¢));
    ¢.accept(new CleanerVisitor());
    return true;
  }

  private CompilationUnitRecord foo(final CompilationUnit ¢) {
    final CompilationUnitRecord $ = new CompilationUnitRecord(¢);
    $.setPath(absolutePath);
    $.setRelativePath(relativePath);
    return $;
  }

  @Override public boolean visit(final PackageDeclaration ¢) {
    packages.add(¢.getName().getFullyQualifiedName());
    return true;
  }

  @Override @SuppressWarnings("unused") public boolean visit(final TypeDeclaration $) {
    // if (!excludeMethod($))
    try {
      final Integer key = box.it(measure.commands($));
      //
      CUStatistics.putIfAbsent(key, an.empty.list());
      classStatistics.putIfAbsent(key, an.empty.list());
      //
      final ClassRecord c = new ClassRecord($);
      classRecords.push(c);
      classStatistics.get(key).add(c);
      findFirst.instanceOf(TypeDeclaration.class)
          .in(make.ast(WrapIntoComilationUnit.OUTER.off(nanonizer.fixedPoint(WrapIntoComilationUnit.OUTER.on($ + "")))));
    } catch (final AssertionError __) {
      System.err.print("X");
    } catch (final NullPointerException __) {
      System.err.print("N");
    } catch (final IllegalArgumentException __) {
      System.err.print("I");
    }
    return true; // super.visit($);
  }

  @Override protected void done(final String path) {
    if (writer == null)
      initializeWriter();
    writeSummary(path);
    System.err.println("Your output is in: " + outputFolder);
  }

  private void writeSummary(final String path) {
    writer//
        .col("Project", path)//
        .col("#Packages", countNoTestPackages())//
        .col("#LOC", countNoTestLOC())//
        .col("#Classes", countNoTestClasses() - countTestClasses())//
        // .col("#TestClasses", countTestClasses())//
        .col("#Methods", countNoTestMethods())//
        .nl();
  }

  private int countTestClasses() {
    return compilationUnitRecords.stream().filter(λ -> !λ.noTests()).mapToInt(λ -> λ.numClasses).sum();
  }

  private int countNoTestClasses() {
    return productionCompilationUnits().mapToInt(λ -> λ.numClasses).sum();
  }

  private int countNoTestLOC() {
    return productionCompilationUnits().mapToInt(λ -> λ.linesOfCode).sum();
  }

  private boolean noTests(final CompilationUnitRecord ¢) {
    return ¢.noTests();
  }

  private int countNoTestMethods() {
    return productionCompilationUnits().mapToInt(λ -> λ.numMethods).sum();
  }

  private Stream<CompilationUnitRecord> productionCompilationUnits() {
    return compilationUnitRecords.stream().filter(this::noTests);
  }

  private int countNoTestPackages() {
    return productionCompilationUnits().map(λ -> λ.pakcage).collect(toSet()).size();
  }

  private static void initializeWriter() {
    writer = new Table(clazz);
  }
}
