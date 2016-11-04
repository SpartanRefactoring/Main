package il.org.spartan.spartanizer.cmdline;

import java.io.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;

import il.org.spartan.*;
import il.org.spartan.collections.*;
import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.utils.*;

/** Collect basic metrics of files (later on, maybe change to classes)
 * @author Yossi Gil
 * @year 2016 */
public final class CollectMetrics {
  private static String OUTPUT = "/tmp/test.csv";
  private static String OUTPUT_Tips = "/tmp/tips.csv";
  private static CSVStatistics output = init(OUTPUT, "property");
  private static CSVStatistics Tips = init(OUTPUT_Tips, "tips");

  public static void main(final String[] where) {
    go(where.length != 0 ? where : as.array("."));
    System.err.println("Your output should be here: " + output.close());
  }

  public static Document rewrite(final AbstractGUIApplicator a, final CompilationUnit u, final Document $) {
    try {
      a.createRewrite(u).rewriteAST($, null).apply($);
      return $;
    } catch (MalformedTreeException | BadLocationException e) {
      throw new AssertionError(e);
    }
  }

  // TODO Yossi Gil: eliminate warning
  private static void collectTips(@SuppressWarnings("unused") final String __, final CompilationUnit before) {
    reportTips(new Trimmer().collectSuggesions(before));
  }

  private static void go(final File f) {
    try {
      // This line is going to give you trouble if you process class by class.
      output.put("File", f.getName());
      Tips.put("File", f.getName());
      go(FileUtils.read(f));
    } catch (final IOException e) {
      System.err.println(e.getMessage());
    }
  }

  private static void go(final String javaCode) {
    output.put("Characters", javaCode.length());
    final CompilationUnit before = (CompilationUnit) makeAST.COMPILATION_UNIT.from(javaCode);
    report("Before-", before);
    collectTips(javaCode, before);
    final CompilationUnit after = spartanize(javaCode);
    assert after != null;
    report("After-", after);
    output.nl();
  }

  private static void go(final String[] where) {
    for (final File ¢ : new FilesGenerator(".java").from(where))
      go(¢);
  }

  private static CSVStatistics init(final String outputDir, final String property) {
    try {
      return new CSVStatistics(outputDir, property);
    } catch (final IOException e) {
      throw new RuntimeException(OUTPUT, e);
    }
  }

  /** fault, what happens if we have many classes in the same file? Also, we do
   * not want to count imports, and package instructions. Write a method that
   * finds all classes, which could be none, at the upper level, and collect on
   * these. Note that you have to print the file name which is common to all
   * classes. Turn this if you like into a documentation
   * @param string */
  // TODO: Yossi Gil: make this even more clever, by using function interfaces..
  private static void report(final String prefix, final CompilationUnit ¢) {
    // TODO Matteo: make sure that the counting does not include comments.
    // Do
    // this by adding stuff to the metrics suite.
    output.put(prefix + "Length", ¢.getLength());
    output.put(prefix + "Count", count.nodes(¢));
    output.put(prefix + "Non whites", count.nonWhiteCharacters(¢));
    output.put(prefix + "Condensed size", metrics.condensedSize(¢));
    output.put(prefix + "Lines", count.lines(¢));
    output.put(prefix + "Dexterity", metrics.dexterity(¢));
    output.put(prefix + "Leaves", metrics.leaves(¢));
    output.put(prefix + "Nodes", metrics.nodes(¢));
    output.put(prefix + "Internals", metrics.internals(¢));
    output.put(prefix + "Vocabulary", metrics.vocabulary(¢));
    output.put(prefix + "Literacy", metrics.literacy(¢));
    output.put(prefix + "Imports", count.imports(¢));
    output.put(prefix + "No Imports", count.noimports(¢));
  }

  private static void reportTips(final List<Tip> ¢) {
    for (final Tip $ : ¢) {
      Tips.put("description", $.description);
      Tips.put("from", $.from);
      Tips.put("to", $.to);
      Tips.put("linenumber", $.lineNumber);
      Tips.nl();
    }
  }

  private static CompilationUnit spartanize(final String javaCode) {
    final Trimmer t = new Trimmer();
    final String spartanized = t.fixed(javaCode);
    output.put("Characters", spartanized.length());
    return (CompilationUnit) makeAST.COMPILATION_UNIT.from(spartanized);
  }
}