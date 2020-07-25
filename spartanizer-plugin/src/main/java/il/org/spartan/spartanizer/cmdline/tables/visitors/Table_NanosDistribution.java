package il.org.spartan.spartanizer.cmdline.tables.visitors;

import static il.org.spartan.spartanizer.ast.navigate.step.body;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import fluent.ly.note;
import fluent.ly.safe;
import fluent.ly.system;
import fluent.ly.unbox;
import il.org.spartan.spartanizer.ast.factory.make;
import il.org.spartan.spartanizer.ast.navigate.findFirst;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.cmdline.good.DeprecatedFolderASTVisitor;
import il.org.spartan.spartanizer.cmdline.nanos.NanoPatternsOccurencesStatistics;
import il.org.spartan.spartanizer.research.Logger;
import il.org.spartan.spartanizer.research.analyses.Nanonizer;
import il.org.spartan.spartanizer.research.util.CleanerVisitor;
import il.org.spartan.spartanizer.utils.WrapIntoComilationUnit;
import il.org.spartan.spartanizer.utils.format;
import il.org.spartan.tables.Table;
import il.org.spartan.utils.Int;

/** TODO orimarco {@code marcovitch.ori@gmail.com} please add a description
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2016-12-29 */
public class Table_NanosDistribution extends DeprecatedFolderASTVisitor {
  private static final Nanonizer nanonizer = new Nanonizer();
  private static final Map<Integer, Table> writers = new HashMap<>();
  private static final NanoPatternsOccurencesStatistics npStatistics = new NanoPatternsOccurencesStatistics();
  private static final CleanerVisitor cleanerVisitor = new CleanerVisitor();
  static {
    clazz = Table_NanosDistribution.class;
    Logger.subscribe(npStatistics::logNPInfo);
  }

  @SuppressWarnings("resource") private static void initializeWriter(final int type) {
    writers.put(Integer.valueOf(type), new Table("distribution_" + ASTNode.nodeClassForType(type).getSimpleName()));
  }
  public static void main(final String[] args) throws InstantiationException, IllegalAccessException, InvocationTargetException {
    DeprecatedFolderASTVisitor.main(args);
    writers.values().forEach(Table::close);
    System.err.println("Your output is in: " + system.tmp);
  }
  @Override public boolean visit(final MethodDeclaration $) {
    if (!excludeMethod($))
      try {
        npStatistics.logNode(findFirst.instanceOf(MethodDeclaration.class)
            .in(make.ast(WrapIntoComilationUnit.Method.off(nanonizer.fixedPoint(WrapIntoComilationUnit.Method.on($ + ""))))));
      } catch (final AssertionError | IllegalArgumentException | NullPointerException ¢) {
        note.bug(¢);
      }
    return super.visit($);
  }
  @Override public boolean visit(final CompilationUnit ¢) {
    ¢.accept(cleanerVisitor);
    return true;
  }
  @Override protected void done(final String path) {
    summarize(path);
    System.err.println("Your output is in: " + outputFolder);
  }
  private static boolean excludeMethod(final MethodDeclaration ¢) {
    return iz.constructor(¢) || body(¢) == null;
  }
  public static void summarize(final String path) {
    for (final Integer boxedType : npStatistics.keySet()) {
      if (!writers.containsKey(boxedType))
        initializeWriter(boxedType.intValue());
      @SuppressWarnings("resource") final Table writer = writers.get(boxedType);
      final int type = unbox.it(boxedType);
      writer//
          .col("Project", path)//
          .col("count", npStatistics.total(type))//
          .col("nanos count", npStatistics.covered(type))//
          .col("coverage", format.decimal(100 * npStatistics.coverage(type)))//
      ;
      final HashMap<String, Int> hist = npStatistics.nanoHistogram(boxedType);
      hist.keySet().forEach(λ -> writer.col(λ + " perc.", format.decimal(100 * safe.div(hist.get(λ).inner, npStatistics.total(type)))));
      hist.keySet().forEach(λ -> writer.col(λ, hist.get(λ).inner));
      writer.nl();
    }
    npStatistics.clear();
  }
}
