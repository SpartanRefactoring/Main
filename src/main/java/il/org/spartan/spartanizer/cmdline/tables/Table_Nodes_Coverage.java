package il.org.spartan.spartanizer.cmdline.tables;

import java.lang.reflect.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.cmdline.nanos.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.analyses.*;
import il.org.spartan.tables.*;
import il.org.spartan.utils.*;

/** Generates table presenting {@link ASTNode}s coverage
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-03-07 */
public class Table_Nodes_Coverage extends DeprecatedFolderASTVisitor {
  static final AgileSpartanizer spartanizer = new AgileSpartanizer();
  static final SpartAnalyzer spartanalyzer = new SpartAnalyzer();
  static final CompilationUnitCoverageStatistics statistics = new CompilationUnitCoverageStatistics();
  private static Table writer;
  static {
    clazz = Table_Nodes_Coverage.class;
    Logger.subscribe(statistics::markNP);
  }

  public static void main(final String[] args)
      throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    DeprecatedFolderASTVisitor.main(args);
    writer.close();
  }

  @Override public boolean visit(final CompilationUnit ¢) {
    try {
      statistics.logCompilationUnit(¢);
      final String spartanzied = spartanizer.fixedPoint(¢);
      statistics.logAfterSpartanization(into.cu(spartanzied));
      analyze(spartanzied);
    } catch (final AssertionError | MalformedTreeException | IllegalArgumentException __) {
      ___.unused(__);
    }
    return true;
  }

  @SuppressWarnings("static-method") protected String analyze(final String spartanzied) {
    return spartanalyzer.fixedPoint(spartanzied);
  }

  @Override protected void done(final String path) {
    summarizeStatistics(path);
    statistics.clear();
  }

  private static void initializeWriter() {
    writer = new Table(clazz);
  }

  public static void summarizeStatistics(final String path) {
    if (writer == null)
      initializeWriter();
    writer//
        .col("Project", path)//
        .col("Coverage", coverage())//
        .col("Nodes", nodes())//
        .col("Units", statistics.size())//
        .nl();
  }

  private static double coverage() {
    return statistics.covergae();
  }

  private static int nodes() {
    return statistics.nodes();
  }
}
