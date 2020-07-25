package il.org.spartan.spartanizer.cmdline.tables;

import static il.org.spartan.spartanizer.cmdline.tables.Table_ReusabilityIndices.rExternal;
import static il.org.spartan.spartanizer.cmdline.tables.Table_ReusabilityIndices.rInternal;
import static il.org.spartan.spartanizer.cmdline.tables.Table_ReusabilityIndices.rMethod;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.text.edits.MalformedTreeException;

import fluent.ly.note;
import il.org.spartan.spartanizer.cmdline.GrandVisitor;
import il.org.spartan.spartanizer.cmdline.Tapper;
import il.org.spartan.spartanizer.cmdline.tables.Table_ReusabilityIndices.RIndicesVisitor;
import il.org.spartan.spartanizer.research.Logger;
import il.org.spartan.spartanizer.research.util.AnnotationCleanerVisitor;
import il.org.spartan.spartanizer.utils.format;
import il.org.spartan.tables.Table;
import il.org.spartan.utils.Int;

/** Generates a table that shows for every nano it's prevalence in corpus
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-03-20 */
class Table_Prevalence extends NanoTable {
  static final Map<String, Int> prevalence = new HashMap<>();
  static {
    Logger.subscribe(npStatistics::logNPInfo);
  }
  static final RIndicesVisitor visitor = new Table_ReusabilityIndices.RIndicesVisitor() {
    @Override public boolean visit(final CompilationUnit $) {
      try {
        $.accept(new AnnotationCleanerVisitor());
        statistics.logCompilationUnit($);
        analyze.apply(spartanizer.fixedPoint($));
      } catch (final AssertionError | MalformedTreeException | IllegalArgumentException ¢) {
        note.bug(¢);
      }
      return super.visit($);
    }
  };

  static void clear() {
    npStatistics.clear();
    RIndicesVisitor.clear();
  }
  public static void main(final String[] args) {
    new GrandVisitor(args) {
      {
        listen(new Tapper() {
          @Override public void endLocation() {
            initializeWriter();
            summarize();
            clear();
          }
        });
      }

      void summarize() {
        final int rMethod = rMethod(), rInternal = rInternal(), rExternal = rExternal();
        npStatistics.keySet().stream()//
            .map(npStatistics::get)//
            .forEach(λ -> {
              prevalence.putIfAbsent(λ.name, new Int());
              prevalence.get(λ.name).inner += λ.occurences > rMethod ? 6 : λ.occurences > rInternal ? 4 : λ.occurences > rExternal ? 3 : 0;
            });
      }
      void initializeWriter() {
        if (table == null)
          table = new Table(Table.classToNormalizedFileName(Table_Prevalence.class) + "-" + corpus, outputFolder);
      }
    }.visitAll(visitor);
    for (final String ¢ : prevalence.keySet()) {
      table.put("Nano", ¢);
      table.put("Prevalence", Double.valueOf(format.decimal(prevalence.get(¢).inner / 6.0)));
      table.nl();
    }
    table.close();
  }
}
