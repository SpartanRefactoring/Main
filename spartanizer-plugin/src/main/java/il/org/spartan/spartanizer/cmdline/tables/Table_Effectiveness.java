package il.org.spartan.spartanizer.cmdline.tables;

import static il.org.spartan.spartanizer.cmdline.tables.Table_ReusabilityIndices.rExternal;
import static il.org.spartan.spartanizer.cmdline.tables.Table_ReusabilityIndices.rInternal;
import static il.org.spartan.spartanizer.cmdline.tables.Table_ReusabilityIndices.rMethod;

import java.util.Comparator;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.text.edits.MalformedTreeException;

import fluent.ly.forget;
import il.org.spartan.spartanizer.cmdline.ASTInFilesVisitor;
import il.org.spartan.spartanizer.cmdline.tables.Table_ReusabilityIndices.RIndicesVisitor;
import il.org.spartan.spartanizer.research.Logger;
import il.org.spartan.spartanizer.research.util.AnnotationCleanerVisitor;
import il.org.spartan.spartanizer.tipping.Tipper;
import il.org.spartan.tables.Table;

/** Generates a table representing effectiveness of nanos. <br>
 * For each nano, the level of reusability is measured: <br>
 * 'M' - more occurrences than Method r-index.<br>
 * 'I' - more than Internal. <br>
 * 'X' - more than External.<br>
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2016-12-25 */
class Table_Effectiveness extends NanoTable {
  static {
    Logger.subscribe(npStatistics::logNPInfo);
  }
  static final RIndicesVisitor visitor = new Table_ReusabilityIndices.RIndicesVisitor() {
    @Override public boolean visit(final CompilationUnit $) {
      try {
        $.accept(new AnnotationCleanerVisitor());
        statistics.logCompilationUnit($);
        analyze.apply(spartanizer.fixedPoint($));
      } catch (final AssertionError | MalformedTreeException | IllegalArgumentException __) {
        forget.em(__);
      }
      return super.visit($);
    }
  };

  static void clear() {
    npStatistics.clear();
    RIndicesVisitor.clear();
  }
  public static void main(final String[] args) {
    new ASTInFilesVisitor(args) {
      {
        listen(new Listener() {
          @Override public void endLocation() {
            done(getCurrentLocation());
          }
        });
      }

      protected void done(final String path) {
        initializeWriter();
        summarize(path);
        clear();
      }
      void summarize(final String path) {
        final int rMethod = rMethod(), rInternal = rInternal(), rExternal = rExternal();
        table.put("Project", path);
        npStatistics.keySet().stream()//
            .sorted(Comparator.comparing(λ -> npStatistics.get(λ).name))//
            .map(npStatistics::get)//
            .forEach(λ -> table.put(λ.name, λ.occurences > rMethod ? "M" : λ.occurences > rInternal ? "I" : λ.occurences > rExternal ? "X" : "-"));
        fillAbsents();
        table.nl();
      }
      void initializeWriter() {
        if (table == null)
          table = new Table(Table.classToNormalizedFileName(Table_Effectiveness.class) + "-" + getCurrentLocation(), outputFolder);
      }
    }.visitAll(visitor);
    table.close();
  }
  static void fillAbsents() {
    nanonizer.allNanoPatterns().stream()//
        .map(Tipper::className)//
        .filter(λ -> !npStatistics.keySet().contains(λ))//
        .forEach(λ -> table.put(λ, "-"));
  }
}
