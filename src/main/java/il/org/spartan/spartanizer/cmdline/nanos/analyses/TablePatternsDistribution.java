package il.org.spartan.spartanizer.cmdline.nanos.analyses;

import java.lang.reflect.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.cmdline.nanos.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.analyses.*;
import il.org.spartan.spartanizer.research.util.*;
import il.org.spartan.spartanizer.utils.*;
import il.org.spartan.tables.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.ast;

/** @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2016-12-29 */
public class TablePatternsDistribution extends FolderASTVisitor {
  private static final SpartAnalyzer spartanalyzer = new SpartAnalyzer();
  private static final Map<Integer, Table> writers = new HashMap<>();
  private static final NanoPatternsDistributionStatistics npStatistics = new NanoPatternsDistributionStatistics();
  private static final CleanerVisitor cleanerVisitor = new CleanerVisitor();
  static {
    clazz = TablePatternsDistribution.class;
    Logger.subscribe((n, np) -> npStatistics.logNPInfo(n, np));
  }

  @SuppressWarnings("resource") private static void initializeWriter(final int type) {
    writers.put(Integer.valueOf(type), new Table("distribution_" + ASTNode.nodeClassForType(type).getSimpleName()));
  }

  public static void main(final String[] args)
      throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    FolderASTVisitor.main(args);
    writers.values().forEach(w -> w.close());
    System.err.println("Your output is in: " + Table.temporariesFolder);
  }

  @Override public boolean visit(final MethodDeclaration $) {
    if (!excludeMethod($))
      try {
        npStatistics.logMethod(findFirst.methodDeclaration(ast(Wrap.Method.off(spartanalyzer.fixedPoint(Wrap.Method.on($ + ""))))));
      } catch (@SuppressWarnings("unused") final AssertionError __) {
        System.err.print("X");
      } catch (@SuppressWarnings("unused") final NullPointerException ¢) {
        System.err.print("N");
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
    npStatistics.fillAbsents();
    for (final Integer type : npStatistics.keySet()) {
      if (!writers.containsKey(type))
        initializeWriter(type.intValue());
      @SuppressWarnings("resource") final Table writer = writers.get(type);
      writer//
          .put("Project", path)//
          .put("count", npStatistics.count(type))//
          .put("nanos count", npStatistics.countNanos(type))//
          .put("coverage", format.decimal(100 * npStatistics.coverage(type)))//
      ;
      final HashMap<String, Int> hist = npStatistics.nanoHistogram(type);
      for (final String ¢ : hist.keySet())
        writer.put(¢ + " perc.", format.decimal(100 * safe.div(hist.get(¢).inner, npStatistics.count(type))));
      for (final String ¢ : hist.keySet())
        writer.put(¢, hist.get(¢).inner);
      writer.nl();
    }
    npStatistics.clear();
  }
}
