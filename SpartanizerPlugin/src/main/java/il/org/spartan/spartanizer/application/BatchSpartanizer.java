package il.org.spartan.spartanizer.application;

import static il.org.spartan.tide.*;

import java.io.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.wizard.*;

import il.org.spartan.*;
import il.org.spartan.collections.*;
import il.org.spartan.java.*;
import il.org.spartan.java.Token.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.utils.*;

/** Scans all files and apply spartnize all.
 * @author Yossi Gil
 * @year 2015 */
public final class BatchSpartanizer {
  private static CSVStatistics report;
  private static String inputFileName = "/tmp/input.java";
  private static String outputFileName = "/tmp/output.java";
  static int n;
  private static PrintWriter fin;
  private static PrintWriter fout;

  public static void main(final String[] where) throws IOException {
    try (PrintWriter finLocal = new PrintWriter(new FileWriter(inputFileName)); //
        PrintWriter foutLocal = new PrintWriter(new FileWriter(outputFileName))) {
      fin = finLocal;
      fout = foutLocal;
      report = new CSVStatistics("/tmp/types.CSV");
      collect(where.length != 0 ? where : new String[] { "." });
      System.err.println("Look for your report here: " + report.close());
    }
  }

  static boolean collect(final AbstractTypeDeclaration in) {
    final int length = in.getLength();
    final int tokens = metrics.tokens(in + "");
    final int nodes = metrics.nodesCount(in);
    final int body = metrics.bodySize(in);
    final int tide = clean(in + "").length();
    final int essence = essence(in + "").length();
    final String out = NonGUIApplicator.fixedPoint(in + "");
    final int length2 = out.length();
    final int tokens2 = metrics.tokens(out);
    final int tide2 = clean(out + "").length();
    final int essence2 = essence(out + "").length();
    final ASTNode from = makeAST.COMPILATION_UNIT.from(out);
    final int nodes2 = metrics.nodesCount(from);
    final int body2 = metrics.bodySize(from);
    System.out.println(++n + " " + extract.category(in) + " " + extract.name(in));
    fin.print(in);
    fout.print(out);
    report//
        .put("Category", extract.category(in))//
        .put("Name", extract.name(in))//
        .put("Nodes1", nodes)//
        .put("Nodes2", nodes2)//
        .put("ΔNodes", nodes - nodes2)//
        .put("δNodes", δ(nodes, nodes2))//
        .put("Body", body)//
        .put("Body2", body2)//
        .put("ΔBody", body - body2)//
        .put("δBody", δ(body, body2))//
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
        .put("ΔEssence", essence - essence2)//
        .put("δEssence", δ(essence, essence2))//
        .put("R(T/L)", ratio(length, tide)) //
        .put("R(E/L)", ratio(length, essence)) //
        .put("R(E/T)", ratio(tide, essence)) //
        .put("R(B/S)", ratio(nodes, body)) //
    ;
    report.nl();
    return false;
  }

  static int tokens(final String s) {
    int $ = 0;
    for (final Tokenizer tokenizer = new Tokenizer(new StringReader(s));;) {
      final Token t = tokenizer.next();
      if (t == null)
        return $;
      if (t.kind == Kind.COMMENT || t.kind == Kind.NONCODE)
        continue;
      ++$;
    }
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
    if (f.getPath().contains("src/test"))
      return;
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

  private static double ratio(final double n1, final double n2) {
    return n2 / n1;
  }

  private static double δ(final int n1, final int n2) {
    return 1 - 1. * n2 / n1;
  }
}