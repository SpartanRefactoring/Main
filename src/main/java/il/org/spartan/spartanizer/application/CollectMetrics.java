package il.org.spartan.spartanizer.application;

import java.io.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.collections.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.utils.*;

/** Collect basic metrics of files (later on, maybe change to classes)
 * @author Yossi Gil
 * @year 2016 */
public class CollectMetrics {
  private static final String OUTPUT = "/tmp/halstead.CSV";
  private static CSVStatistics output = init();

  public static void main(final String[] where) {
    collect(where.length != 0 ? where : new String[] { "." });
    System.err.println("Your output should be here: " + output.close());
  }

  /** Bug, what happens if we have many classes in the same file? Also, we do
   * not want to count imports, and package instructions. Write a method that
   * finds all classes, which could be none, at the upper level, and collect on
   * these. Note that you have to print the file name which is common to all
   * classes. Turn this if you like into a documentation */
  private static void collect(final CompilationUnit ¢) {
    // TODO Matteo make sure that the counting does not include comments. Do
    // this by adding stuff to the metrics suite.
    output.put("Length", ¢.getLength());
    // TODO: Yossi, make this even more clever, by using function interfaces..
    output.put("Count", metrics.count(¢));
    output.put("Non whites", metrics.countNonWhites(¢));
    output.put("Condensed size", metrics.condensedSize(¢));
    output.put("Lines", metrics.lineCount(¢));
    output.put("Dexterity", metrics.dexterity(¢));
    output.put("Leaves", metrics.leaves(¢));
    output.put("Nodes", metrics.nodes(¢));
    output.put("Internals", metrics.internals(¢));
    output.put("Vocabulary", metrics.vocabulary(¢));
    output.put("Literacy", metrics.literacy(¢));
    output.nl();
  }

  private static void collect(final File f) {
    try {
      output.put("File", f.getName());
      collect(FileUtils.read(f));
    } catch (final IOException e) {
      System.err.println(e.getMessage());
    }
  }

  private static void collect(final String javaCode) {
    output.put("Characters", javaCode.length());
    collect((CompilationUnit) makeAST.COMPILATION_UNIT.from(javaCode));
  }

  private static void collect(final String[] where) {
    for (final File f : new FilesGenerator(".java").from(where))
      collect(f);
  }

  private static CSVStatistics init() {
    try {
      return new CSVStatistics(OUTPUT, "property");
    } catch (final IOException e) {
      throw new RuntimeException(OUTPUT, e);
    }
  }
}