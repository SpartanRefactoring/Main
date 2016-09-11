package il.org.spartan.spartanizer.application;

import java.io.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.collections.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.utils.*;

/** Demonstrates iteration through files.
 * @author Yossi Gil
 * @year 2015 */
public class TypeNamesCollector {
  static Map<String, Integer> count = new TreeMap<>();
  static Map<String, Set<String>> shortToFull = new TreeMap<>();

  public static void main(final String[] where) throws IOException {
    collect(where.length != 0 ? where : new String[] { "." });
    CSVStatistics w = new CSVStatistics("types.csv", "property"); 
    for (final String s : count.keySet()) {
      String shortName = spartan.shorten(s);
      w.put("Count", count.get(s).intValue());
      w.put("Log(Count)", Math.log(count.get(s).intValue()));
      w.put("Sqrt(Count)", Math.sqrt(count.get(s).intValue()));
      w.put("Collisions",  shortToFull.get(shortName).size()); 
      w.put("Short", spartan.shorten(s));
      w.put("Original", s);
      w.nl();
    }
    System.err.println("Look for your output here: " + w.close());
  }

  private static void collect(final CompilationUnit u) {
    u.accept(new ASTVisitor() {
      @Override public boolean visit(final SimpleType t) {
        record(last(t.getName()) + "");
        return true;
      }

      void record(String longName) {
        if (!count.containsKey(longName))
          count.put(longName, Integer.valueOf(0));
        count.put(longName, box.it(count.get(longName).intValue() + 1));
        String shortName = spartan.shorten(longName);
        if (!shortToFull.containsKey(shortName))
          shortToFull.put(shortName, new HashSet<String>());
        shortToFull.get(shortName).add(longName);
      }

      SimpleName last(final Name n) {
        return n.isSimpleName() ? (SimpleName) n : n.isQualifiedName() ? ((QualifiedName) n).getName() : null;
      }
    });
  }

  private static void collect(final File f) {
    try {
      collect(FileUtils.read(f));
    } catch (final IOException e) {
      System.err.println(e.getMessage());
    }
  }

  private static void collect(final String javaCode) {
    collect((CompilationUnit) makeAST.COMPILATION_UNIT.from(javaCode));
  }

  private static void collect(final String[] where) {
    for (final File f : new FilesGenerator(".java").from(where))
      collect(f);
  }
}