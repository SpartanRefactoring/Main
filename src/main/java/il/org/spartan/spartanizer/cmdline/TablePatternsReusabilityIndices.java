package il.org.spartan.spartanizer.cmdline;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.analyses.*;
import il.org.spartan.spartanizer.research.analyses.util.*;
import il.org.spartan.spartanizer.research.util.*;
import il.org.spartan.spartanizer.utils.*;

/** @author Ori Marcovitch
 * @since Dec 14, 2016 */
public class TablePatternsReusabilityIndices extends TableReusabilityIndices {
  static final SpartAnalyzer spartanalyzer = new SpartAnalyzer();
  private final Map<String, NanoPatternRecord> npStatistics = new HashMap<>();
  private static CSVStatistics pWriter;
  static {
    clazz = TablePatternsReusabilityIndices.class;
  }

  private static void initializeWriter() {
    try {
      pWriter = new CSVStatistics(outputFileName(), "$\\#$");
    } catch (final IOException ¢) {
      throw new RuntimeException(¢);
    }
  }

  private static String outputFileName() {
    return makeFile(clazz.getSimpleName());
  }

  public static void main(final String[] args)
      throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    TrimmerLog.off();
    TableReusabilityIndices.main(args);
    if (pWriter != null)
      System.err.println("Your output is in: " + pWriter.close());
    file.renameToCSV(outputFileName());
  }

  @Override public boolean visit(final MethodDeclaration ¢) {
    if (!excludeMethod(¢))
      spartanalyzer.fixedPoint(Wrap.Method.on(¢ + ""));
    return super.visit(¢);
  }

  @Override public boolean visit(final CompilationUnit ¢) {
    System.out.println(packageDeclaration(¢));
    ¢.accept(new CleanerVisitor());
    return true;
  }

  @Override public boolean visit(TypeDeclaration ¢) {
    System.out.println(name(¢));
    return super.visit(¢);
  }

  @Override protected void init(final String path) {
    super.init(path);
    Logger.subscribe((n, np) -> logNPInfo(n, np));
  }

  @Override protected void done(final String path) {
    super.done(path);
    summarizeNPStatistics();
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

  public void summarizeNPStatistics() {
    if (pWriter == null)
      initializeWriter();
    final int r = methodRIndex();
    pWriter.put("Project", presentSourceName);
    npStatistics.keySet().stream()
        .sorted((k1, k2) -> npStatistics.get(k1).occurences < npStatistics.get(k2).occurences ? 1
            : npStatistics.get(k1).occurences > npStatistics.get(k2).occurences ? -1 : 0)
        .map(k -> npStatistics.get(k))//
        .forEach(n -> pWriter.put(n.name, n.occurences < r ? "-" : "+"));
    pWriter.nl();
  }
}
