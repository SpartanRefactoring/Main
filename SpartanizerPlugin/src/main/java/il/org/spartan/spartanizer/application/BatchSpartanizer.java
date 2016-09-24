package il.org.spartan.spartanizer.application;

import static il.org.spartan.tide.*;

import java.io.*;

import org.eclipse.jdt.core.dom.*;
import il.org.spartan.*;
import il.org.spartan.bench.*;
import il.org.spartan.collections.*;
import il.org.spartan.java.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.utils.*;

/** Scans files named by folder, ignore test files, and collect statistics.
 * @author Yossi Gil
 * @year 2015 */
public final class BatchSpartanizer {
  private static final String folder = "/tmp/";

  public static void main(final String[] where) {
    if (where.length == 0)
      new BatchSpartanizer(".", "current-working-directory").fire();
    else
      for (final String ¢ : where)
        new BatchSpartanizer(¢).fire();
  }

  static String essenceNew(final String codeFragment) {
    return codeFragment.replaceAll("//.*?\r\n", "\n").replaceAll("/\\*(?=(?:(?!\\*/)[\\s\\S])*?)(?:(?!\\*/)[\\s\\S])*\\*/", "")
        .replaceAll("^\\s*$", "").replaceAll("^\\s*\\n", "").replaceAll("\\s*$", "").replaceAll("\\s+", " ")
        .replaceAll("\\([^a-zA-Z]\\) \\([^a-zA-Z]\\)", "\\([^a-zA-Z]\\)\\([^a-zA-Z]\\)")
        .replaceAll("\\([^a-zA-Z]\\) \\([a-zA-Z]\\)", "\\([^a-zA-Z]\\)\\([a-zA-Z]\\)")
        .replaceAll("\\([a-zA-Z]\\) \\([^a-zA-Z]\\)", "\\([a-zA-Z]\\)\\([^a-zA-Z]\\)");
  }

  static String folder2File(final String path) {
    return path//
        .replaceAll("[\\ /.]", "-")//
        .replaceAll("-+", "-")//
        .replaceAll("^-", "")//
        .replaceAll("-$", "")//
    ;
  }

  static String p(final int n1, final int n2) {
    return Unit.formatRelative(δ(n1, n2));
  }

  static double ratio(final double n1, final double n2) {
    return n2 / n1;
  }

  static int tokens(final String s) {
    int $ = 0;
    for (final Tokenizer tokenizer = new Tokenizer(new StringReader(s));;) {
      final Token t = tokenizer.next();
      if (t == null || t == Token.EOF)
        return $;
      if (t.kind == Token.Kind.COMMENT || t.kind == Token.Kind.NONCODE)
        continue;
      ++$;
    }
  }

  static double δ(final double n1, final double n2) {
    return 1 - n2 / n1;
  }

  private int classesDone;
  private final String inputPath;
  private final String beforeFileName;
  private final String afterFileName;
  private PrintWriter befores;
  private PrintWriter afters;
  private CSVStatistics report;
  private final String reportFileName;
  private final static String script = "./essence";

  private BatchSpartanizer(final String path) {
    this(path, folder2File(path));
  }

  private BatchSpartanizer(final String inputPath, final String name) {
    this.inputPath = inputPath;
    beforeFileName = folder + name + ".before.java";
    afterFileName = folder + name + ".after.java";
    reportFileName = folder + name + ".CSV";
  }

  boolean collect(final AbstractTypeDeclaration in) {
    final int length = in.getLength();
    final int tokens = metrics.tokens(in + "");
    final int nodes = metrics.nodesCount(in);
    final int body = metrics.bodySize(in);
    final int tide = clean(in + "").length();
    final int essence = BatchSpartanizer.essenceNew(in + "").length();
    final String out = NonGUIApplicator.fixedPoint(in + "");
    final int length2 = out.length();
    final int tokens2 = metrics.tokens(out);
    final int tide2 = clean(out + "").length();
    final int essence2 = BatchSpartanizer.essenceNew(out + "").length();
    final int essenceWC = wc(BatchSpartanizer.essenceNew(out + ""));
    final ASTNode from = makeAST.COMPILATION_UNIT.from(out);
    final int nodes2 = metrics.nodesCount(from);
    final int body2 = metrics.bodySize(from);
    System.err.println(++classesDone + " " + extract.category(in) + " " + extract.name(in));
    befores.print(in);
    afters.print(out);
    report//
        .put("Category", extract.category(in))//
        .put("Name", extract.name(in))//
        .put("Nodes1", nodes)//
        .put("Nodes2", nodes2)//
        .put("ΔNodes", nodes - nodes2)//
        .put("δNodes", δ(nodes, nodes2))//
        .put("δNodes%", p(nodes, nodes2))//
        .put("Body", body)//
        .put("Body2", body2)//
        .put("Δ Body", body - body2)//
        .put("δ Body", δ(body, body2))//
        .put("% Body", p(body, body2))//
        .put("Length1", length)//
        .put("Tokens1", tokens)//
        .put("Tokens2", tokens2)//
        .put("Δ Tokens", tokens - tokens2)//
        .put("δ Toknes", δ(tokens, tokens2))//
        .put("% Toknes", p(tokens, tokens2))//
        .put("Length1", length)//
        .put("Length2", length2)//
        .put("Δ Length", length - length2)//
        .put("δ Length", δ(length, length2))//
        .put("% Length", p(length, length2))//
        .put("Tide1", tide)//
        .put("Tide2", tide2)//
        .put("Δ Tide2", tide - tide2)//
        .put("δ Tide2", δ(tide, tide2))//
        .put("δ Tide2", p(tide, tide2))//
        .put("Essence1", essence)//
        .put("Essence2", essence2)//
        .put("Δ Essence", essence - essence2)//
        .put("δ Essence", δ(essence, essence2))//
        .put("% Essence", p(essence, essence2))//
        .put("Essence (wc)", essenceWC) // essence in terms of words (not characters)
        .put("R(T/L)", ratio(length, tide)) //
        .put("R(E/L)", ratio(length, essence)) //
        .put("R(E/T)", ratio(tide, essence)) //
        .put("R(B/S)", ratio(nodes, body)) //
    ;
    report.nl();
    return false;
  }

  private static int wc(String $) {
     return $.trim().isEmpty() ? 0 : $.trim().split("\\s+").length;
  }

  void collect(final CompilationUnit u) {
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

  void collect(final File f) {
    if (!f.getPath().contains("src/test"))
      try {
        collect(FileUtils.read(f));
      } catch (final IOException e) {
        System.err.println(e.getMessage());
      }
    System.err.print("\n Summary:: " + report.close());
  }

  void collect(final String javaCode) {
    collect((CompilationUnit) makeAST.COMPILATION_UNIT.from(javaCode));
  }

  void fire() {
    collect();
    count();
  }

  private void collect() {
    System.err.printf(
        "Input path=%s\n" + //
            "Collective before path=%s\n" + //
            "Collective after path=%s\n" + //
            "\n" //
        , inputPath, //
        beforeFileName, //
        afterFileName);
    try (PrintWriter b = new PrintWriter(new FileWriter(beforeFileName)); //
        PrintWriter a = new PrintWriter(new FileWriter(afterFileName))) {
      befores = b;
      afters = a;
      report = new CSVStatistics(reportFileName);
      for (final File ¢ : new FilesGenerator(".java").from(inputPath))
        collect(¢);
    } catch (final IOException x) {
      x.printStackTrace();
      System.err.println(classesDone + " files processed; processing of " + inputPath + " failed for some I/O reason");
    }
    applyEssenceCommandLine();
    System.err.print("\n Done: " + classesDone + " files processed.");
  }

  private void count() {
  }
  
  private void applyEssenceCommandLine(){
    try {
 
    String essentializedCodeBefore = executeScript(beforeFileName);
    String essentializedCodeAfter = executeScript(afterFileName);
    
    int numWordEssentialBefore = essenceNew(essentializedCodeBefore.toString()).trim().length();
    int numWordEssentialAfter = essenceNew(essentializedCodeAfter.toString()).trim().length();

    System.err.println("Word Count Essentialized before: " + numWordEssentialBefore);
    System.err.println("Word Count Essentialized after: " + numWordEssentialAfter);
    System.err.println("Difference: " + (numWordEssentialAfter - numWordEssentialBefore));
    
    } catch (final IOException e) {
      System.err.println(e.getMessage());
    }
  }

  private static String executeScript(final String pathname) throws IOException {
    ProcessBuilder builder = new ProcessBuilder("/bin/bash");
    builder.command(script, pathname);
    builder.redirectErrorStream(true);
    Process process = builder.start();
    
    InputStream stdout = process.getInputStream ();
    BufferedReader reader = new BufferedReader (new InputStreamReader(stdout));
    
    String line;
    StringBuffer sb = new StringBuffer();
    while ((line = reader.readLine ()) != null) {
      sb.append(line);
    }
    return sb.toString();
  }
}