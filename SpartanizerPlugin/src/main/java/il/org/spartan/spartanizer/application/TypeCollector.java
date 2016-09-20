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

  static boolean collect(AbstractTypeDeclaration ¢) {
    System.out.println(++n + " " + extract.category(¢) + " " + extract.name(¢));
    final int length = ¢.getLength();
    final int tide = clean(¢ + "").length();
    final String text = essence(¢ + "");
    final int tokens = metrics.tokens(¢ + "");
    final int essence = text.length();
    final int nodesCount = metrics.nodesCount(¢);
    String spartanized = BatchApplicator.fixedPoint(¢ + "");
    final int tokensAfter = metrics.tokens(spartanized); 
    output//
        .put("Category", extract.category(¢))//
        .put("Name", extract.name(¢))//
        .put("Nodes", nodesCount)//
        .put("Tokens", tokens)//
        .put("Lost tokens", tokens - metrics.tokens(text))//
        .put("Length", length)//
        .put("Tide", tide)//
        .put("Essence", essence) //
        .put("Ds(L∻T)", diffString(length, tide)) //
        .put("Ds(L∻E)", diffString(length, essence)) //
        .put("Ds(T∻E)", diffString(tide, essence)) //
        .put("D(L∻T)", diff(length, tide)) //
        .put("D(L∻E)", diff(length, essence)) //
        .put("D(T/E)", diff(tide, essence)) //
        .put("R(T/L)", ratio(length, tide)) //
        .put("R(E/L)", ratio(length, essence)) //
        .put("R(E/T)", ratio(tide, essence)) //
        .put("Tokens after", tokensAfter) //
        .put("Tokens saved", tokens - tokensAfter) //
        .put("Tb∻Ta", diffString(tokens, tokensAfter)) //
    // .put("Text", text.substring(0,Math.min(100,text.length()))) //
    ;
    
    
    
    output.nl();
    return false;
  }

  private static double ratio(double n1, double n2) {
    return n2 / n1;
  }

  private static String diffString(int n1, int n2) {
    final double ratio = diff(n1, n2);
    return Unit.formatRelative(ratio);
  }

  private static double diff(int n1, int n2) {
    final int sum = Math.abs(n1) + Math.abs(n2);
    final double ratio = sum == 0 ? sum : 2.0 * (n1 - n2) / sum;
    return ratio;
  }

  private static void collect(final CompilationUnit u) {
    u.accept(new ASTVisitor() {
      @Override public boolean visit(final TypeDeclaration ¢) {
        return collect(¢);
      }

      @Override public boolean visit(final AnnotationTypeDeclaration ¢) {
        return collect(¢);
      }

      @Override public boolean visit(final EnumDeclaration ¢) {
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