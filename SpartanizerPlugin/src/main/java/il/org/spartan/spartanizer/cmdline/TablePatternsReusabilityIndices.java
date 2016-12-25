package il.org.spartan.spartanizer.cmdline;

import java.lang.reflect.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.analyses.*;
import il.org.spartan.spartanizer.research.analyses.util.*;
import il.org.spartan.spartanizer.research.util.*;
import il.org.spartan.spartanizer.utils.*;

/** @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2016-12-25 */
public class TablePatternsReusabilityIndices extends TableReusabilityIndices {
  static final SpartAnalyzer spartanalyzer = new SpartAnalyzer();
  private final Map<String, NanoPatternRecord> npStatistics = new HashMap<>();
  private static Relation pWriter;
  static {
    clazz = TablePatternsReusabilityIndices.class;
  }

  private static void initializeWriter() {
    pWriter = new Relation(outputFileName());
  }

  private static String outputFileName() {
    return clazz.getSimpleName();
  }

  public static void main(final String[] args)
      throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    TrimmerLog.off();
    TableReusabilityIndices.main(args);
    if (pWriter != null) {
      pWriter.close();
      System.err.println("Your output is in: " + Relation.temporariesFolder + outputFileName());
    }
    file.renameToCSV(Relation.temporariesFolder + outputFileName());
  }

  @Override public boolean visit(final MethodDeclaration $) {
    if (!excludeMethod($))
      try {
        spartanalyzer.fixedPoint(Wrap.Method.on($ + ""));
      } catch (final AssertionError ¢) {
        System.err.println(¢);
      }
    return super.visit($);
  }

  @Override public boolean visit(final CompilationUnit ¢) {
    ¢.accept(new CleanerVisitor());
    return true;
  }

  @Override protected void init(final String path) {
    super.init(path);
    Logger.subscribe((n, np) -> logNPInfo(n, np));
  }

  @Override protected void done(final String path) {
    summarizeNPStatistics(path);
    System.err.println("Your output is in: " + outputFolder);
  }

  private static boolean excludeMethod(final MethodDeclaration ¢) {
    return iz.constructor(¢) || body(¢) == null;
  }

  private void logNPInfo(final ASTNode n, final String np) {
    if (!npStatistics.containsKey(np))
      npStatistics.put(np, new NanoPatternRecord(np, n.getClass()));
    npStatistics.get(np).markNP(n);
  }

  public void summarizeNPStatistics(final String path) {
    if (pWriter == null)
      initializeWriter();
    final int r = methodRIndex();
    pWriter.put("Project", path);
    npStatistics.keySet().stream()//
        .sorted((k1, k2) -> npStatistics.get(k1).name.compareTo(npStatistics.get(k2).name))//
        .map(k -> npStatistics.get(k))//
        .forEach(n -> pWriter.put(n.name, n.occurences < r ? "-" : "+"));
    pWriter.nl();
  }
}
