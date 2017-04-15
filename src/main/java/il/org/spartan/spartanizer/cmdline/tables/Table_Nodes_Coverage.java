package il.org.spartan.spartanizer.cmdline.tables;

import java.util.function.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.cmdline.nanos.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.analyses.*;
import il.org.spartan.spartanizer.research.util.*;
import il.org.spartan.tables.*;
import il.org.spartan.utils.*;

/** Generates table presenting {@link ASTNode}s coverage
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-03-07 */
public class Table_Nodes_Coverage {
  static final NoBrainDamagedTippersSpartanizer spartanizer = new NoBrainDamagedTippersSpartanizer();
  protected static final Nanonizer nanonizer = new Nanonizer();
  static final CompilationUnitCoverageStatistics statistics = new CompilationUnitCoverageStatistics();
  private static Table writer;
  protected static Function<String, String> analyze = nanonizer::fixedPoint;

  public static void main(final String[] args) {
    new ASTInFilesVisitor(args) {
      @Override protected void done(final String path) {
        summarizeStatistics(path);
        statistics.clear();
      }
    }.fire(new ASTVisitor(true) {
      @Override public boolean visit(final CompilationUnit ¢) {
        ¢.accept(new AnnotationCleanerVisitor());
        try {
          statistics.logCompilationUnit(¢);
          final String spartanzied = spartanizer.fixedPoint(¢);
          statistics.logAfterSpartanization(into.cu(spartanzied));
          analyze.apply(spartanzied);
        } catch (final AssertionError | MalformedTreeException | IllegalArgumentException __) {
          ___.unused(__);
        }
        return true;
      }
    });
    writer.close();
  }

  static {
    Logger.subscribe(statistics::markNP);
  }

  private static void initializeWriter() {
    writer = new Table(Table_Nodes_Coverage.class);
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
    return statistics.nodesCoverage();
  }

  private static int nodes() {
    return statistics.nodes();
  }
}
