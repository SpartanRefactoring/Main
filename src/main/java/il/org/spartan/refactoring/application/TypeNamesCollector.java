package il.org.spartan.refactoring.application;

import java.io.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.files.*;
import il.org.spartan.refactoring.engine.*;
import il.org.spartan.utils.*;

public class TypeNamesCollector {
  private static Set<String> basket = new TreeSet<>();

  private static void collect(final CompilationUnit u) {
    u.accept(new ASTVisitor() {
      SimpleName last(final Name n) {
        return n.isSimpleName() ? (SimpleName) n : n.isQualifiedName() ? ((QualifiedName) n).getName() : null;
      }

      @SuppressWarnings("synthetic-access") @Override public boolean visit(final SimpleType t) {
        basket.add("" + last(t.getName()));
        return true;
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

  public static void main(final String[] where) {
    collect(where.length != 0 ? where : new String[] { "." });
    for (final String s : basket)
      System.out.println(s + " --> " + spartan.shorten(s));
  }
}
