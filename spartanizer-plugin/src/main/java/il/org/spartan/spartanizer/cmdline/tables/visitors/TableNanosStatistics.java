package il.org.spartan.spartanizer.cmdline.tables.visitors;

import static il.org.spartan.spartanizer.ast.navigate.step.body;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Comparator;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import fluent.ly.as;
import fluent.ly.note;
import fluent.ly.system;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.cmdline.good.DeprecatedFolderASTVisitor;
import il.org.spartan.spartanizer.cmdline.nanos.NanoPatternsOccurencesStatisticsLight;
import il.org.spartan.spartanizer.research.Logger;
import il.org.spartan.spartanizer.research.analyses.Nanonizer;
import il.org.spartan.spartanizer.research.nanos.common.JavadocMarkerNanoPattern;
import il.org.spartan.spartanizer.research.nanos.methods.HashCodeMethod;
import il.org.spartan.spartanizer.research.nanos.methods.ToStringMethod;
import il.org.spartan.spartanizer.research.util.CleanerVisitor;
import il.org.spartan.spartanizer.tipping.Tipper;
import il.org.spartan.spartanizer.utils.WrapIntoComilationUnit;
import il.org.spartan.tables.Table;

/** TODO orimarco {@code marcovitch.ori@gmail.com} please add a description
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-01-03 */
public class TableNanosStatistics extends DeprecatedFolderASTVisitor {
  private static final Nanonizer nanonizer = new Nanonizer();
  private static Table pWriter;
  private static final NanoPatternsOccurencesStatisticsLight npStatistics = new NanoPatternsOccurencesStatisticsLight();
  private static final Collection<JavadocMarkerNanoPattern> excluded = as.list(new HashCodeMethod(), new ToStringMethod());
  static {
    clazz = TableNanosStatistics.class;
    Logger.subscribe(npStatistics::logNPInfo);
  }

  private static void initializeWriter() {
    pWriter = new Table(TableNanosStatistics.class);
  }
  public static void main(final String[] args)
      throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    DeprecatedFolderASTVisitor.main(args);
    pWriter.close();
    System.err.println("Your output is in: " + system.tmp + pWriter.name);
  }
  @Override public boolean visit(final MethodDeclaration $) {
    if (!excludeMethod($))
      try {
        nanonizer.fixedPoint(WrapIntoComilationUnit.Method.on($ + ""));
      } catch (final AssertionError | IllegalArgumentException ¢) {
        note.bug(¢);
      }
    return super.visit($);
  }
  @Override public boolean visit(final CompilationUnit ¢) {
    ¢.accept(new CleanerVisitor());
    return true;
  }
  @Override protected void done(final String path) {
    summarizeNPStatistics(path);
    System.err.println(" " + path + " Done");
  }
  private static boolean excludeMethod(final MethodDeclaration ¢) {
    return iz.constructor(¢) || body(¢) == null || anyTips(excluded, ¢);
  }
  public static void summarizeNPStatistics(final String path) {
    if (pWriter == null)
      initializeWriter();
    pWriter.col("Project", path);
    npStatistics.keySet().stream()//
        .sorted(Comparator.comparing(λ -> npStatistics.get(λ).name))//
        .map(npStatistics::get)//
        .forEach(λ -> pWriter.col(λ.name, λ.occurences));
    fillAbsents();
    pWriter.nl();
    npStatistics.clear();
  }
  private static void fillAbsents() {
    nanonizer.allNanoPatterns().stream()//
        .map(Tipper::className)//
        .filter(λ -> !npStatistics.keySet().contains(λ))//
        .forEach(λ -> pWriter.col(λ, 0));
  }
  private static boolean anyTips(final Collection<JavadocMarkerNanoPattern> ps, final MethodDeclaration d) {
    return d != null && ps.stream().anyMatch(λ -> λ.check(d));
  }
}
