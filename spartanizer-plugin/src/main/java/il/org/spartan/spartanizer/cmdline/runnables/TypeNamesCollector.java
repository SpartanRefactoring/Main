package il.org.spartan.spartanizer.cmdline.runnables;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.SimpleType;

import fluent.ly.as;
import fluent.ly.box;
import fluent.ly.note;
import il.org.spartan.CSVStatistics;
import il.org.spartan.collections.FilesGenerator;
import il.org.spartan.spartanizer.ast.factory.makeAST;
import il.org.spartan.spartanizer.ast.navigate.hop;
import il.org.spartan.spartanizer.engine.nominal.abbreviate;
import il.org.spartan.utils.FileUtils;

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
