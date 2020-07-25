package il.org.spartan.spartanizer.cmdline.metrics;

import java.io.File;
import java.io.IOException;

import org.eclipse.jdt.core.dom.CompilationUnit;

import fluent.ly.as;
import fluent.ly.note;
import il.org.spartan.CSVStatistics;
import il.org.spartan.collections.FilesGenerator;
import il.org.spartan.spartanizer.ast.factory.makeAST;
import il.org.spartan.spartanizer.ast.navigate.countOf;
import il.org.spartan.spartanizer.ast.nodes.metrics.Metrics;
import il.org.spartan.spartanizer.plugin.TextualTraversals;
import il.org.spartan.utils.FileUtils;

/** Collect basic metrics of files (later on, maybe change to classes)
 * @author Yossi Gil
 * @since Oct 3, 2016 */
enum CollectClassMetrics {
  ;
  private static final String OUTPUT = "/tmp/commons-lang-halstead.CSV";
  private static final CSVStatistics output = init();

  public static void main(final String[] where) {
    go(where.length != 0 ? where : as.array("."));
    System.err.println("Your output should be here: " + output.close());
  }
  static CompilationUnit spartanize(final CompilationUnit $) {
    new TextualTraversals().once($);
    return $;
  }
  private static void go(final File f) {
    try {
      // This line is going to give you trouble if you process class by class.
      output.put("File", f.getName());
      go(FileUtils.read(f));
    } catch (final IOException ¢) {
      note.bug(¢);
    }
  }
  private static void go(final String javaCode) {
    output.put("Characters", javaCode.length());
    report("Before-", (CompilationUnit) makeAST.COMPILATION_UNIT.from(javaCode));
  }
  private static void go(final String... where) {
    new FilesGenerator(".java").from(where).forEach(CollectClassMetrics::go);
  }
  private static CSVStatistics init() {
    return new CSVStatistics(OUTPUT, "property");
  }
  /** fault, what happens if we have many classes in the same file? Also, we do
   * not want to count imports, and package instructions. Write a method that
   * finds all classes, which could be none, at the upper level, and collect on
   * these. Note that you have to print the file name which is common to all
   * classes. Turn this if you like into a documentation
   * @param string */
  private static void report(final String prefix, final CompilationUnit ¢) {
    // TODO Matteo: make sure that the counting does not include comments.
    // Do
    // this by adding stuff to the metrics suite.
    output.put(prefix + "Length", ¢.getLength());
    output.put(prefix + "Count", countOf.nodes(¢));
    output.put(prefix + "Non whites", countOf.nonWhiteCharacters(¢));
    output.put(prefix + "Condensed size", Metrics.condensedSize(¢));
    output.put(prefix + "Lines", countOf.lines(¢));
    output.put(prefix + "Dexterity", Metrics.dexterity(¢));
    output.put(prefix + "Leaves", Metrics.leaves(¢));
    output.put(prefix + "Nodes", Metrics.nodes(¢));
    output.put(prefix + "Internals", Metrics.internals(¢));
    output.put(prefix + "Vocabulary", Metrics.vocabulary(¢));
    output.put(prefix + "Literacy", Metrics.literacy(¢));
    output.put(prefix + "Imports", countOf.imports(¢));
    output.put(prefix + "No Imports", countOf.noimports(¢));
    output.nl();
  }
}
