package il.org.spartan.spartanizer.cmdline.tables;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.cmdline.nanos.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.analyses.*;
import il.org.spartan.spartanizer.research.util.*;
import il.org.spartan.tables.*;
import il.org.spartan.utils.*;

/** Generates a table that shows how many times each nano occurred in each
 * project
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-03 */
public class Table_RawNanoStatistics {
  static final SpartAnalyzer spartanalyzer = new SpartAnalyzer();
  static final AgileSpartanizer spartanizer = new AgileSpartanizer();
  static Table pWriter;
  static final NanoPatternsStatistics npStatistics = new NanoPatternsStatistics();
  static {
    Logger.subscribe(npStatistics::logNPInfo);
  }

  public static void main(final String[] args) {
    new FileSystemASTVisitor(args) {
      @Override protected void done(final String path) {
        summarizeNPStatistics(path);
        System.err.println(" " + path + " Done");
      }

      public void summarizeNPStatistics(final String path) {
        if (pWriter == null)
          initializeWriter(outputFolder);
        pWriter.col("Project", path);
        npStatistics.keySet().stream()//
            .sorted(Comparator.comparing(λ -> npStatistics.get(λ).name))//
            .map(npStatistics::get)//
            .forEach(λ -> pWriter.col(λ.name, λ.occurences));
        fillAbsents();
        pWriter.nl();
        npStatistics.clear();
      }

      void fillAbsents() {
        spartanalyzer.getAllPatterns().stream()//
            .map(λ -> λ.className())//
            .filter(λ -> !npStatistics.keySet().contains(λ))//
            .forEach(λ -> pWriter.col(λ, 0));
      }
    }.fire(new ASTVisitor(true) {
      @Override public boolean visit(final CompilationUnit $) {
        try {
          $.accept(new AnnotationCleanerVisitor());
          spartanalyzer.fixedPoint(spartanizer.fixedPoint($));
        } catch (final IllegalArgumentException | AssertionError __) {
          ___.unused(__);
        }
        return super.visit($);
      }

      @Override public boolean visit(final FieldDeclaration ¢) {
        spartanalyzer.fixedPoint(ast(¢ + ""));
        return true;
      }
    });
    pWriter.close();
  }

  static void initializeWriter(final String outputFolder) {
    pWriter = new Table(Table_RawNanoStatistics.class, outputFolder);
  }

  static void initializeWriter() {
    pWriter = new Table(Table_RawNanoStatistics.class);
  }
}
