package il.org.spartan.spartanizer.cmdline.tables;

import java.util.function.Function;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.text.edits.MalformedTreeException;

import fluent.ly.forget;
import il.org.spartan.spartanizer.cmdline.CurrentData;
import il.org.spartan.spartanizer.cmdline.GrandVisitor;
import il.org.spartan.spartanizer.cmdline.Tapper;
import il.org.spartan.spartanizer.cmdline.nanos.CompilationUnitCoverageStatistics;
import il.org.spartan.spartanizer.engine.parse;
import il.org.spartan.spartanizer.research.Logger;
import il.org.spartan.spartanizer.research.analyses.Nanonizer;
import il.org.spartan.spartanizer.research.analyses.NoBrainDamagedTippersSpartanizer;
import il.org.spartan.spartanizer.research.util.AnnotationCleanerVisitor;
import il.org.spartan.tables.Table;

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
    new GrandVisitor(args) {
      {
        listen(new Tapper() {
          @Override public void endLocation() {
            summarizeStatistics(CurrentData.location);
            statistics.clear();
          }
        });
      }
    }.visitAll(new ASTVisitor(true) {
      @Override public boolean visit(final CompilationUnit ¢) {
        ¢.accept(new AnnotationCleanerVisitor());
        try {
          statistics.logCompilationUnit(¢);
          final String spartanzied = spartanizer.fixedPoint(¢);
          statistics.logAfterSpartanization(parse.cu(spartanzied));
          analyze.apply(spartanzied);
        } catch (final AssertionError | MalformedTreeException | IllegalArgumentException __) {
          forget.em(__);
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
