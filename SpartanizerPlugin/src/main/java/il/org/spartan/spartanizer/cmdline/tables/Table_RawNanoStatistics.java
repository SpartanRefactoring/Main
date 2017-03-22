package il.org.spartan.spartanizer.cmdline.tables;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.util.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.tables.*;
import il.org.spartan.utils.*;
import org.jetbrains.annotations.NotNull;

/** Generates a table that shows how many times each nano occurred in each
 * project
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-03 */
public class Table_RawNanoStatistics extends NanoTable {
  static {
    Logger.subscribe(npStatistics::logNPInfo);
    spartanalyzer.addRejected();
  }

  public static void summarize(final String path) {
    writer.col("Project", path);
    npStatistics.keySet().stream()//
        .sorted(Comparator.comparing(λ -> npStatistics.get(λ).name))//
        .map(npStatistics::get)//
        .forEach(λ -> writer.col(λ.name, λ.occurences));
    fillAbsents();
    writer.nl();
    reset();
  }

  static void fillAbsents() {
    spartanalyzer.allNanoPatterns().stream()//
        .map(Tipper::nanoName)//
        .filter(λ -> !npStatistics.keySet().contains(λ))//
        .forEach(λ -> writer.col(λ, 0));
  }

  public static void main(final String[] args) {
    new ASTInFilesVisitor(args) {
      @Override protected void done(final String path) {
        initializeWriter();
        summarize(path);
        System.err.println(" " + path + " Done"); // we need to know if the
                                                  // process is finished or hang
      }

      void initializeWriter() {
        if (writer == null)
          writer = new Table(Table.classToNormalizedFileName(Table_RawNanoStatistics.class) + "-" + corpus, outputFolder);
      }
    }.fire(new ASTVisitor(true) {
      @Override public boolean visit(@NotNull final CompilationUnit $) {
        try {
          $.accept(new AnnotationCleanerVisitor());
          spartanalyzer.fixedPoint(spartanizer.fixedPoint($));
        } catch (@NotNull final IllegalArgumentException | AssertionError __) {
          ___.unused(__);
        }
        return super.visit($);
      }

      @Override public boolean visit(final FieldDeclaration ¢) {
        spartanalyzer.fixedPoint(ast(¢ + ""));
        return true;
      }
    });
    writer.close();
  }
}
