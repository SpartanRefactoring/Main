package il.org.spartan.spartanizer.cmdline.tables;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.text.edits.MalformedTreeException;

import fluent.ly.forget;
import il.org.spartan.spartanizer.cmdline.CurrentData;
import il.org.spartan.spartanizer.cmdline.GrandVisitor;
import il.org.spartan.spartanizer.cmdline.Tapper;
import il.org.spartan.spartanizer.research.util.AnnotationCleanerVisitor;
import il.org.spartan.spartanizer.tippers.BlockSingletonEliminate;
import il.org.spartan.tables.Table;

/** Generates a table summarizing important statistics about nano patterns
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-04-06 */
public class Table_Summary_Unspartanized extends Table_Summary {
  static {
    nanonizer.removeSpartanizerTippers();
    nanonizer.add(Block.class, //
        new BlockSingletonEliminate(), //
        null);
  }

  public static void main(final String[] args) {
    new GrandVisitor(args) {
      {
        listen(new Tapper() {
          @Override public void endLocation() {
            done(CurrentData.location);
          }
        });
      }

      protected void done(final String path) {
        summarize(path);
        reset();
        System.err.println(" " + path + " Done"); // we need to know if the
                                                  // process is finished or hang
      }
      public void summarize(final String path) {
        initializeWriter();
        table//
            .col("Project", path)//
            .col("Commands", statementsCoverage())//
            .col("Expressions", expressionsCoverage())//
            .col("Nodes", statistics.nodesCoverage())//
            .col("Methods", methodsCovered())//
            .col("Touched", touched())//
            .col("Iteratives", iterativesCoverage())//
            .col("ConditionalExpressions", conditionalExpressionsCoverage())//
            .col("ConditionalCommands", conditionalStatementsCoverage())//
            .nl();
      }
      void initializeWriter() {
        if (table == null)
          table = new Table(Table.classToNormalizedFileName(Table_Summary_Unspartanized.class) + "-" + corpus, outputFolder);
      }
    }.visitAll(new ASTVisitor(true) {
      @Override public boolean visit(final CompilationUnit ¢) {
        try {
          ¢.accept(new AnnotationCleanerVisitor());
          statistics.logCompilationUnit(¢);
          logAfterSpartanization(¢);
          analyze.apply(¢ + "");
        } catch (final AssertionError | MalformedTreeException | IllegalArgumentException __) {
          forget.em(__);
        }
        return true;
      }
      void logAfterSpartanization(final CompilationUnit ¢) {
        statistics.logAfterSpartanization(¢);
        npDistributionStatistics.logNode(¢);
      }
    });
    table.close();
  }
}
