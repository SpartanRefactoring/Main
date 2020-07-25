package il.org.spartan.spartanizer.cmdline.tables;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;

import fluent.ly.forget;
import il.org.spartan.spartanizer.cmdline.CurrentData;
import il.org.spartan.spartanizer.cmdline.GrandVisitor;
import il.org.spartan.spartanizer.cmdline.Tapper;
import il.org.spartan.spartanizer.research.util.AnnotationCleanerVisitor;
import il.org.spartan.spartanizer.tippers.BlockSingletonEliminate;
import il.org.spartan.tables.Table;

/** Generates a table that shows how many times each nano occurred in each
 * project
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-04-06 */
public class Table_RawNanoStatistics_Unspartanized extends Table_RawNanoStatistics {
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
            initializeWriter();
            summarize(CurrentData.location);
            System.err.println(" " + CurrentData.location + " Done"); // we need
                                                                      // to know
                                                                      // if the
            // process is finished or hang
          }
        });
      }

      void initializeWriter() {
        if (table == null)
          table = new Table(Table.classToNormalizedFileName(Table_RawNanoStatistics_Unspartanized.class) + "-" + corpus, outputFolder);
      }
    }.visitAll(new ASTVisitor(true) {
      @Override public boolean visit(final CompilationUnit $) {
        try {
          $.accept(new AnnotationCleanerVisitor());
          nanonizer.fixedPoint($);
        } catch (final IllegalArgumentException | AssertionError __) {
          forget.em(__);
        }
        return super.visit($);
      }
    });
    table.close();
  }
}
