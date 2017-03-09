package il.org.spartan.spartanizer.cmdline.tables;

import java.lang.reflect.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

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
import il.org.spartan.utils.*;

/** TODO: orimarco <tt>marcovitch.ori@gmail.com</tt> please add a description
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2016-12-29 */
public class Table_NanosDistribution extends DeprecatedFolderASTVisitor {
  private static final SpartAnalyzer spartanalyzer = new SpartAnalyzer();
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

  public static void main(final String[] args)
      throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    DeprecatedFolderASTVisitor.main(args);
    writers.values().forEach(Table::close);
    System.err.println("Your output is in: " + Table.temporariesFolder);
  }

  @Override public boolean visit(final MethodDeclaration $) {
    if (!excludeMethod($))
      try {
        npStatistics
            .logNode(findFirst.instanceOf(MethodDeclaration.class).in(ast(Wrap.Method.off(spartanalyzer.fixedPoint(Wrap.Method.on($ + ""))))));
      } catch (@SuppressWarnings("unused") final AssertionError __) {
        System.err.print("X");
      } catch (@SuppressWarnings("unused") final NullPointerException ¢) {
        System.err.print("N");
      } catch (@SuppressWarnings("unused") final IllegalArgumentException ¢) {
        System.err.print("I");
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
    for (final Integer boxedType : npStatistics.keySet()) {
      if (!writers.containsKey(boxedType))
        initializeWriter(boxedType.intValue());
      @SuppressWarnings("resource") final Table writer = writers.get(boxedType);
      final int type = Unbox.it(boxedType);
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
