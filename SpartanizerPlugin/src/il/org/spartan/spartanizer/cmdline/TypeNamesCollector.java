package il.org.spartan.spartanizer.cmdline;

import java.io.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.*;
import il.org.spartan.collections.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.utils.*;

/** Demonstrates iteration through files.
 * @year 2015
 * @author Yossi Gil
 * @since Dec 20, 2016 */
public enum TypeNamesCollector {
  DUMMY_ENUM_INSTANCE_INTRODUCING_SINGLETON_WITH_STATIC_METHODS;
  static final Map<String, Integer> longNames = new TreeMap<>();
  static final Map<String, Set<String>> shortToFull = new TreeMap<>();

  public static void main(final String[] where) {
    collect(where.length != 0 ? where : as.array("."));
    final CSVStatistics w = new CSVStatistics("types.csv", "property");
    for (final String s : longNames.keySet()) {
      final String shortName = abbreviate.it(s);
      w.put("Count", longNames.get(s).intValue());
      w.put("Log(Count)", Math.log(longNames.get(s).intValue()));
      w.put("Sqrt(Count)", Math.sqrt(longNames.get(s).intValue()));
      w.put("Collisions", shortToFull.get(shortName).size());
      w.put("Short", abbreviate.it(s));
      w.put("Original", s);
      w.nl();
    }
    System.err.println("Look for your output here: " + w.close());
  }

  private static void collect(final CompilationUnit u) {
    // noinspection SameReturnValue
    u.accept(new ASTVisitor(true) {
      @Override public boolean visit(final SimpleType ¢) {
        record(hop.simpleName(¢) + "");
        return true;
      }

      void record(final String longName) {
        longNames.putIfAbsent(longName, Integer.valueOf(0));
        longNames.put(longName, box.it(longNames.get(longName).intValue() + 1));
        final String shortName = abbreviate.it(longName);
        shortToFull.putIfAbsent(shortName, new HashSet<>());
        shortToFull.get(shortName).add(longName);
      }
    });
  }

  private static void collect(final File f) {
    try {
      collect(FileUtils.read(f));
    } catch (final IOException ¢) {
      note.bug(¢);
    }
  }

  private static void collect(final String javaCode) {
    collect((CompilationUnit) makeAST.COMPILATION_UNIT.from(javaCode));
  }

  private static void collect(final String... where) {
    new FilesGenerator(".java").from(where).forEach(TypeNamesCollector::collect);
  }
}
