package il.org.spartan.spartanizer.cmdline;

import java.io.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;

import il.org.spartan.*;
import il.org.spartan.collections.*;
import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.utils.*;
import org.jetbrains.annotations.NotNull;

/** Collect basic metrics of files (later on, maybe change to classes)
 * @year 2016
 * @author Yossi Gil
 * @since Oct 3, 2016 */
enum CollectMetrics {
  ;
  private static final String OUTPUT = "/tmp/test.csv";
  private static final String OUTPUT_Tips = "/tmp/tips.csv";
  private static final CSVStatistics output = init(OUTPUT, "property");
  private static final CSVStatistics Tips = init(OUTPUT_Tips, "tips");

  public static void main(@NotNull final String[] where) {
    go(where.length != 0 ? where : as.array("."));
    System.err.println("Your output should be here: " + output.close());
  }

  @NotNull public static Document rewrite(@NotNull final AbstractGUIApplicator a, @NotNull final CompilationUnit u, @NotNull final Document $) {
    try {
      a.createRewrite(u).rewriteAST($, null).apply($);
      return $;
    } catch (@NotNull MalformedTreeException | BadLocationException ¢) {
      throw new AssertionError(¢);
    }
  }

  // TODO Yossi Gil: eliminate warning
  private static void collectTips(@SuppressWarnings("unused") final String __, @NotNull final CompilationUnit before) {
    reportTips(new Trimmer().collectSuggestions(before));
  }

  private static void go(@NotNull final File f) {
    try {
      // This line is going to give you trouble if you process class by class.
      output.put("File", f.getName());
      Tips.put("File", f.getName());
      go(FileUtils.read(f));
    } catch (@NotNull final IOException ¢) {
      System.err.println(¢.getMessage());
    }
  }

  private static void go(@NotNull final String javaCode) {
    output.put("Characters", javaCode.length());
    final CompilationUnit before = (CompilationUnit) makeAST1.COMPILATION_UNIT.from(javaCode);
    report("Before-", before);
    collectTips(javaCode, before);
    final CompilationUnit after = spartanize(javaCode);
    assert after != null;
    report("After-", after);
    output.nl();
  }

  private static void go(final String[] where) {
    new FilesGenerator(".java").from(where).forEach(λ -> go(λ));
  }

  @NotNull private static CSVStatistics init(final String $, final String property) {
    try {
      return new CSVStatistics($, property);
    } catch (@NotNull final IOException ¢) {
      throw new RuntimeException(OUTPUT, ¢);
    }
  }

  /** fault, what happens if we have many classes in the same file? Also, we do
   * not want to count imports, and package instructions. Write a method that
   * finds all classes, which could be none, at the upper level, and collect on
   * these. Note that you have to print the file name which is common to all
   * classes. Turn this if you like into a documentation
   * @param string */
  // TODO: Yossi Gil: make this even more clever, by using function interfaces..
  private static void report(final String prefix, @NotNull final CompilationUnit ¢) {
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

  private static void reportTips(@NotNull final List<Tip> ¢) {
    for (final Tip $ : ¢) {
      Tips.put("description", $.description);
      Tips.put("from", $.from);
      Tips.put("to", $.to);
      Tips.put("linenumber", $.lineNumber);
      Tips.nl();
    }
  }

  @NotNull private static CompilationUnit spartanize(final String javaCode) {
    final String $ = new Trimmer().fixed(javaCode);
    output.put("Characters", $.length());
    return (CompilationUnit) makeAST1.COMPILATION_UNIT.from($);
  }
}
