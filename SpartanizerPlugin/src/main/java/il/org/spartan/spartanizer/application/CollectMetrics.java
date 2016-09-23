package il.org.spartan.spartanizer.application;

import java.io.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;

import il.org.spartan.*;
import il.org.spartan.collections.*;
import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.utils.*;

/** Collect basic metrics of files (later on, maybe change to classes)
 * @author Yossi Gil
 * @year 2016 */
public final class CollectMetrics {
  private static String OUTPUT = "/tmp/test.csv";
  private static String OUTPUT_SUGGESTIONS = "/tmp/suggestions.csv";
  private static CSVStatistics output = init(OUTPUT, "property");
  private static CSVStatistics suggestions = init(OUTPUT_SUGGESTIONS, "suggestions");

  public static void main(final String[] where) {
    go(where.length != 0 ? where : new String[] { "." });
    System.err.println("Your output should be here: " + output.close());
  }

  public static Document rewrite(final GUI$Applicator a, final CompilationUnit u, final Document $) {
    try {
      a.createRewrite(u).rewriteAST($, null).apply($);
      return $;
    } catch (MalformedTreeException | BadLocationException e) {
      throw new AssertionError(e);
    }
  }

  private static void collectsuggestions(final String javaCode, final CompilationUnit before) {
    reportSuggestions((new Trimmer()).collectSuggesions(before));
  }

  private static void go(final File f) {
    try {
      // This line is going to give you trouble if you process class by class.
      output.put("File", f.getName());
      suggestions.put("File", f.getName());
      go(FileUtils.read(f));
    } catch (final IOException e) {
      System.err.println(e.getMessage());
    }
  }

  private static void go(final String javaCode) {
    output.put("Characters", javaCode.length());
    final CompilationUnit before = (CompilationUnit) makeAST.COMPILATION_UNIT.from(javaCode);
    report("Before-", before);
    collectsuggestions(javaCode, before);
    final CompilationUnit after = spartanize(javaCode, before);
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

  /** Bug, what happens if we have many classes in the same file? Also, we do
   * not want to count imports, and package instructions. Write a method that
   * finds all classes, which could be none, at the upper level, and collect on
   * these. Note that you have to print the file name which is common to all
   * classes. Turn this if you like into a documentation
   * @param string */
  // TODO: Yossi, make this even more clever, by using function interfaces..
  private static void report(final String prefix, final CompilationUnit ¢) {
    // TODO Matteo make sure that the counting does not include comments. Do
    // this by adding stuff to the metrics suite.
    output.put(prefix + "Length", ¢.getLength());
    output.put(prefix + "Count", metrics.count(¢));
    output.put(prefix + "Non whites", metrics.countNonWhites(¢));
    output.put(prefix + "Condensed size", metrics.condensedSize(¢));
    output.put(prefix + "Lines", metrics.lineCount(¢));
    output.put(prefix + "Dexterity", metrics.dexterity(¢));
    output.put(prefix + "Leaves", metrics.leaves(¢));
    output.put(prefix + "Nodes", metrics.nodes(¢));
    output.put(prefix + "Internals", metrics.internals(¢));
    output.put(prefix + "Vocabulary", metrics.vocabulary(¢));
    output.put(prefix + "Literacy", metrics.literacy(¢));
    output.put(prefix + "Imports", metrics.countImports(¢));
    output.put(prefix + "No Imports", metrics.countNoImport(¢));
  }

  private static void reportSuggestions(final List<Suggestion> ¢) {
    // suggestions = new CSVStatistics("/tmp/suggestions.csv");
    for (final Suggestion $ : ¢) {
      suggestions.put("description", $.description);
      suggestions.put("from", $.from);
      suggestions.put("to", $.to);
      suggestions.put("linenumber", $.lineNumber);
      suggestions.nl();
    }
  }

  private static CompilationUnit spartanize(final String javaCode, final CompilationUnit before) {
    final Trimmer t = new Trimmer();
    assert t != null;
    final String spartanized = t.fixed(javaCode);
    output.put("Characters", spartanized.length());
    return (CompilationUnit) makeAST.COMPILATION_UNIT.from(spartanized);
  }
}