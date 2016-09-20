package il.org.spartan.spartanizer.application;

import static il.org.spartan.azzert.*;
import static il.org.spartan.tide.*;

import java.io.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.wizard.*;

import il.org.spartan.*;
import il.org.spartan.bench.*;
import il.org.spartan.collections.*;
import il.org.spartan.java.*;
import il.org.spartan.java.Token.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.utils.*;

/** Demonstrates iteration through files.
 * @author Yossi Gil
 * @year 2015 */
public final class TypeCollector {
  static Set<String> methods = new LinkedHashSet<>();
  private static CSVStatistics output;

  public static void main(final String[] where) throws IOException {
    output = new CSVStatistics("types.CSV");
    collect(where.length != 0 ? where : new String[] { "." });
    System.err.println("Look for your output here: " + output.close());
  }

  static int tokens(String s) {
    int $ = 0;
    for (Tokenizer tokenizer = new Tokenizer(new StringReader(s));;) {
      Token t = tokenizer.next();
      if (t == null)
        return $;
      if (t.kind == Kind.COMMENT || t.kind == Kind.NONCODE)
        continue;
      ++$;
    }
  }

  static int n;

  static boolean collect(AbstractTypeDeclaration in) {
    final int length = in.getLength();
    final int tokens = metrics.tokens(in + "");
    final int nodes = metrics.nodesCount(in);
    final int tide = clean(in + "").length();
    final int essence = essence(in + "").length();
    final String out = BatchApplicator.fixedPoint(in + "");
    final int length2 = out.length();
    final int tokens2 = metrics.tokens(out);
    final int tide2 = clean(out + "").length();
    final int essence2 = essence(out + "").length();
    final int nodes2 = metrics.nodesCount(makeAST.COMPILATION_UNIT.from(out));
    System.out.println(++n + " " + extract.category(in) + " " + extract.name(in));
    output//
        .put("Category", extract.category(in))//
        .put("Name", extract.name(in))//
        .put("Nodes1", nodes)//
        .put("Nodes2", nodes2)//
        .put("ΔNodes", nodes - nodes2)//
        .put("δNodees", δ(nodes, nodes2))//
        .put("Length1", length)//
        .put("Tokens1", tokens)//
        .put("Tokens2", tokens2)//
        .put("ΔTokens", tokens - tokens2)//
        .put("δToknes", δ(tokens, tokens2))//
        .put("Length1", length)//
        .put("Length2", length2)//
        .put("ΔLength", length - length2)//
        .put("δLength", δ(length, length2))//
        .put("Tide1", tide)//
        .put("Tide2", tide2)//
        .put("ΔTide2", tide - tide2)//
        .put("δTide2", δ(tide, tide2))//
        .put("Essence1", essence)//
        .put("Essence2", essence2)//
        .put("ΔEssence2", essence - essence2)//
        .put("δEssence2", δ(essence, essence2))//
        .put("R(T/L)", ratio(length, tide)) //
        .put("R(E/L)", ratio(length, essence)) //
        .put("R(E/T)", ratio(tide, essence)) //
    ;
    output.nl();
    return false;
  }

  private static double δ(int n1, int n2) {
    return (1- 1. * n2 / n1);
  }

  private static double ratio(double n1, double n2) {
    return n2 / n1;
  }

  private static String diffString(int n1, int n2) {
    return Unit.formatRelative(diff(n1, n2));
  }

  private static double diff(int n1, int n2) {
    final int sum = Math.abs(n1) + Math.abs(n2);
    return sum == 0 ? sum : 2.0 * (n1 - n2) / sum;
  }

  private static void collect(final CompilationUnit u) {
    u.accept(new ASTVisitor() {
      @Override public boolean visit(final AnnotationTypeDeclaration ¢) {
        return collect(¢);
      }

      @Override public boolean visit(final EnumDeclaration ¢) {
        return collect(¢);
      }

      @Override public boolean visit(final TypeDeclaration ¢) {
        return collect(¢);
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