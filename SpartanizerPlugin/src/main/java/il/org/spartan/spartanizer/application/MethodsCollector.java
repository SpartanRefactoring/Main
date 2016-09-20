package il.org.spartan.spartanizer.application;

import java.io.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.collections.*;
import il.org.spartan.java.*;
import il.org.spartan.java.Token.*;
import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.utils.*;

/** Demonstrates iteration through files.
 * @author Yossi Gil
 * @year 2015 */
public final class MethodsCollector {
  static Set<String> methods = new LinkedHashSet<>();

  public static void main(final String[] where) throws IOException {
    collect(where.length != 0 ? where : new String[] { "." });
    int n = 0;
    final CSVStatistics w = new CSVStatistics("methods.csv", "property");
    for (final String ¢ : methods) {
      if (++n % 10 != 0)
        continue;
      w.put("Body", ¢);
      w.put("Characters", ¢.length());
      w.put("Tokens", metrics.tokens(¢));
      w.nl();
    }
    System.err.println("Look for your output here: " + w.close());
  }

  private static void collect(final CompilationUnit u) {
    u.accept(new ASTVisitor() {
      @Override public boolean visit(final MethodDeclaration ¢) {
        MethodDeclaration $ = duplicate.of(¢);
        if (Modifier.isAbstract($.getModifiers()) || ¢.isConstructor())
          return false;
        $.setJavadoc(null);
        $.setName($.getAST().newSimpleName("f"));
        $.setFlags(0);
        String s = BatchApplicator.fixedPoint($ + "");
        methods.add(tide.clean(s));
        return false;
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
    for (final File ¢ : new FilesGenerator(".java").from(where))
      collect(¢);
  }
}