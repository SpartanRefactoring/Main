package il.org.spartan.spartanizer.cmdline.tables;

import java.util.Comparator;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;

import fluent.ly.forget;
import il.org.spartan.spartanizer.cmdline.CurrentData;
import il.org.spartan.spartanizer.cmdline.GrandVisitor;
import il.org.spartan.spartanizer.cmdline.Tapper;
import il.org.spartan.spartanizer.research.Logger;
import il.org.spartan.spartanizer.research.util.AnnotationCleanerVisitor;
import il.org.spartan.spartanizer.tipping.Tipper;
import il.org.spartan.tables.Table;

/** Generates a table that shows how many times each nano occurred in each
 * project
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-01-03 */
public class Table_RawNanoStatistics extends NanoTable {
  static {
    Logger.subscribe(npStatistics::logNPInfo);
    // nanonizer.addRejected();
  }

  public static void summarize(final String path) {
    table.col("Project", path);
    npStatistics.keySet().stream()//
        .sorted(Comparator.comparing(λ -> npStatistics.get(λ).name))//
        .map(npStatistics::get)//
        .forEach(λ -> table.col(λ.name, λ.occurences));
    fillAbsents();
    table.nl();
    reset();
  }
  static void fillAbsents() {
    nanonizer.allNanoPatterns().stream()//
        .map(Tipper::className)//
        .filter(λ -> !npStatistics.keySet().contains(λ))//
        .forEach(λ -> table.col(λ, 0));
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
          table = new Table(Table.classToNormalizedFileName(Table_RawNanoStatistics.class) + "-" + corpus, outputFolder);
      }
    }.visitAll(new ASTVisitor(true) {
      @Override public boolean visit(final CompilationUnit $) {
        try {
          $.accept(new AnnotationCleanerVisitor());
          nanonizer.fixedPoint(spartanizer.fixedPoint($));
        } catch (final IllegalArgumentException | AssertionError __) {
          forget.em(__);
        }
        return super.visit($);
      }
    });
    table.close();
  }
}
