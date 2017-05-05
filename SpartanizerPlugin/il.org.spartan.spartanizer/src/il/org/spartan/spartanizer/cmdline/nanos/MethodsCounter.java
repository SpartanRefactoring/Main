package il.org.spartan.spartanizer.cmdline.nanos;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.lang.reflect.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.research.util.*;
import il.org.spartan.utils.*;

/** TODO orimarco {@code marcovitch.ori@gmail.com} please add a description
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2016-12-19 */
public class MethodsCounter extends DeprecatedFolderASTVisitor {
  private final SortedMap<Integer, Int> methods = new TreeMap<>();
  static {
    clazz = MethodsCounter.class;
  }

  public static void main(final String[] args)
      throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    wizard.setParserResolveBindings();
    DeprecatedFolderASTVisitor.main(args);
  }

  @Override public boolean visit(final MethodDeclaration ¢) {
    if (excludeMethod(¢))
      return false;
    final Integer key = Integer.valueOf(measure.commands(¢));
    methods.putIfAbsent(key, new Int());
    ++methods.get(key).inner;
    return true;
  }

  @Override public boolean visit(final CompilationUnit ¢) {
    ¢.accept(new CleanerVisitor());
    return true;
  }

  @Override protected void init(final String path) {
    System.err.println("Processing: " + path);
  }

  @Override protected void done(final String __) {
    forget.em(__);
    dotter.line();
    summarizeNumbers();
    System.err.println("Your output is in: " + outputFolder);
  }

  private static boolean excludeMethod(final MethodDeclaration ¢) {
    return iz.constructor(¢) || body(¢) == null;
  }

  public static CSVStatistics openSummaryFile(final String $) {
    return new CSVStatistics($, "property");
  }

  public void summarizeNumbers() {
    final CSVStatistics report = openSummaryFile(outputFolder + "/countStatistics.csv");
    if (report == null)
      return;
    methods.keySet().forEach(λ -> {
      report //
          .put("Num. Statements", λ) //
          .put("Count", methods.get(λ).inner)//
      ;
      report.nl();
    });
    report.close();
    file.renameToCSV(outputFolder + "/countStatistics");
  }
}
