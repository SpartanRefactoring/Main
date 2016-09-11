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
public class MetricsCollector {
  private static final String OUTPUT = "halstead.csv";
  private static CSVStatistics output = init();

  public static void main(final String[] where) {
    collect(where.length != 0 ? where : new String[] { "." });
    System.err.println("Your output should be here: " + output.close());
  }

  private static void collect(final CompilationUnit u) {
    output.put("Dexterity", metrics.dexterity(u));
    output.put("Leaves", metrics.leaves(u));
    output.put("Nodes", metrics.nodes(u));
    output.put("Internals", metrics.internals(u));
    output.put("Vocabulary?", metrics.vocabulary(u));
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