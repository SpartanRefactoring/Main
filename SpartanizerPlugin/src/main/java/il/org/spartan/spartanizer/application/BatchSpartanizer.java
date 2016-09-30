package il.org.spartan.spartanizer.application;

import static il.org.spartan.tide.*;

import java.io.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.bench.*;
import il.org.spartan.collections.*;
import il.org.spartan.java.*;
import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.utils.*;

/** Scans files named by folder, ignore test files, and collect statistics.
 * @author Yossi Gil
 * @year 2015 */
public final class BatchSpartanizer {
  private static final String folder = "/tmp/";
  private static final String script = "./essence";
  private static final BatchApplicator batchApplicator = new BatchApplicator().disable(Nominal.class).disable(Nanos.class);
  private static boolean defaultDir = false;
  private static String outputDir = null;
  private static String inputDir = null;

  public static String essenced(final String fileName) {
    return fileName + ".essence";
  }

  public static void main(final String[] args) {
    if (args.length == 0)
      printHelpPrompt();
    else {
      parseCommandLineArgs(args);
      if(inputDir != null && outputDir != null){
        File iDir = new File(inputDir);
        File[] files = iDir.listFiles();
        for(File file: files){
          System.out.println(file.getAbsolutePath());
          new BatchSpartanizer(file.getAbsolutePath()).fire();
        }
      } 
      if(defaultDir){
        new BatchSpartanizer(".", "current-working-directory").fire();
        for (final String ¢ : args)
          new BatchSpartanizer(¢).fire();
      }
    }
  }

  /**
   * @param args
   */
  private static void parseCommandLineArgs(final String[] args) {
    int i = 0;
    while(i < args.length)
      if(args[i].equals("-o")){
        outputDir = args[i+1];
        System.out.println(outputDir);
        i+=2;
      } else if(args[i].equals("-i")) {
        inputDir = args[i+1];
        System.out.println(inputDir);
        i+=2;
      } else {
        System.out.println(args[i]);
        System.out.println("something went wrong!");
        i++;
      }
  }

  public static ProcessBuilder runScript() {
    return new ProcessBuilder("/bin/bash");
  }

  public static String runScript(final Process p) throws IOException {
    try (final InputStream s = p.getInputStream(); final BufferedReader r = new BufferedReader(new InputStreamReader(s))) {
      String ¢;
      for (final StringBuffer $ = new StringBuffer();; $.append(¢))
        if ((¢ = r.readLine()) == null)
          return $ + "";
    }
  }

  public static ProcessBuilder runScript¢(final String pathname) {
    final ProcessBuilder $ = runScript();
    $.redirectErrorStream(true);
    $.command(script, pathname);
    return $;
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

  private static String removePercentChar(final String p) {
    return !p.contains("--") ? p.replace("%", "") : p.replace("%", "").replaceAll("--", "-");
  }

  private static String runScript(final String pathname) throws IOException {
    return runScript(runScript¢(pathname).start());
  }

  private static int wc(final String $) {
    return $.trim().isEmpty() ? 0 : $.trim().split("\\s+").length;
  }

  private int classesDone;
  private final String inputPath;
  private final String beforeFileName;
  private final String afterFileName;
  private PrintWriter befores;
  private PrintWriter afters;
  private CSVStatistics report;
  private final String reportFileName;

  private BatchSpartanizer(final String path) {
    this(path, folder2File(path));
  }

  private BatchSpartanizer(final String inputPath, final String name) {
    this.inputPath = inputPath;
    beforeFileName = folder + name + ".before.java";
    afterFileName = folder + name + ".after.java";
    reportFileName = folder + name + ".CSV";
  }

  public Process bash(final String shellCommand) {
    final String[] command = { "/bin/bash", "-c", shellCommand };
    try {
      final Process p = Runtime.getRuntime().exec(command);
      if (p != null)
        return dumpOutput(p);
    } catch (final IOException x) {
      LoggingManner.logProbableBug(this, x);
    }
    return null;
  }

  public Process shellEssenceMetrics(final String fileName) {
    return bash("./essence < " + fileName + " >" + essenced(fileName));
  }

  boolean collect(final AbstractTypeDeclaration in) {
    final int length = in.getLength();
    final int tokens = metrics.tokens(in + "");
    final int nodes = metrics.nodesCount(in);
    final int body = metrics.bodySize(in);
    final int tide = clean(in + "").length();
    final int essence = essenceNew(in + "").length();
    final String out = batchApplicator.fixedPoint(in + "");
    final int length2 = out.length();
    final int tokens2 = metrics.tokens(out);
    final int tide2 = clean(out + "").length();
    final int essence2 = BatchSpartanizer.essenceNew(out + "").length();
    final int wordCount = wc(BatchSpartanizer.essenceNew(out + ""));
    final ASTNode from = makeAST.COMPILATION_UNIT.from(out);
    final int nodes2 = metrics.nodesCount(from);
    final int body2 = metrics.bodySize(from);
    System.err.println(++classesDone + " " + extract.category(in) + " " + extract.name(in));
    befores.print(in);
    afters.print(out);
    report.summaryFileName();
    report//
        .put("TipperCategory", extract.category(in))//
        .put("Name", extract.name(in))//
        .put("Nodes1", nodes)//
        .put("Nodes2", nodes2)//
        .put("Δ Nodes", nodes - nodes2)//
        .put("δ Nodes", δ(nodes, nodes2))//
        .put("δ Nodes %", Double.parseDouble(removePercentChar(p(nodes, nodes2))))//
        .put("Body", body)//
        .put("Body2", body2)//
        .put("Δ Body", body - body2)//
        .put("δ Body", δ(body, body2))//
        .put("% Body", Double.parseDouble(removePercentChar(p(body, body2))))//
        .put("Length1", length)//
        .put("Tokens1", tokens)//
        .put("Tokens2", tokens2)//
        .put("Δ Tokens", tokens - tokens2)//
        .put("δ Tokens", δ(tokens, tokens2))//
        .put("% Tokens", Double.parseDouble(removePercentChar(p(tokens, tokens2))))//
        .put("Length1", length)//
        .put("Length2", length2)//
        .put("Δ Length", length - length2)//
        .put("δ Length", δ(length, length2))//
        .put("% Length", Double.parseDouble(removePercentChar(p(length, length2))))//
        .put("Tide1", tide)//
        .put("Tide2", tide2)//
        .put("Δ Tide2", tide - tide2)//
        .put("δ Tide2", δ(tide, tide2))//
        .put("δ Tide2", Double.parseDouble(removePercentChar(p(tide, tide2))))//
        .put("Essence1", essence)//
        .put("Essence2", essence2)//
        .put("Δ Essence", essence - essence2)//
        .put("δ Essence", δ(essence, essence2))//
        .put("% Essence", Double.parseDouble(removePercentChar(p(essence, essence2))))//
        .put("Words)", wordCount).put("R(T/L)", ratio(length, tide)) //
        .put("R(E/L)", ratio(length, essence)) //
        .put("R(E/T)", ratio(tide, essence)) //
        .put("R(B/S)", ratio(nodes, body)) //
    ;
    report.nl();
//    System.out.println("δ Nodes %: " + report.get("δ Nodes %"));
    return false;
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
        LoggingManner.infoIOException(e, "File = " + f);
      }
  }

  void collect(final String javaCode) {
    collect((CompilationUnit) makeAST.COMPILATION_UNIT.from(javaCode));
  }

  Process dumpOutput(final Process p) {
    try (BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
      for (String line = in.readLine(); line != null; line = in.readLine(), System.out.println(line))
        ;
    } catch (final IOException x) {
      LoggingManner.logProbableBug(this, x);
    }
    return p;
  }

  void fire() {
    collect();
    runEssence();
    runWordCount();
    System.err.printf("\n Our batch applicator had %d tippers dispersed over %d hooks\n", //
        box.it(batchApplicator.toolbox.tippersCount()), //
        box.it(batchApplicator.toolbox.hooksCount())//
    );
  }

  void runEssence() {
    shellEssenceMetrics(beforeFileName);
    shellEssenceMetrics(afterFileName);
  }

  private void applyEssenceCommandLine() {
    try {
      final String essentializedCodeBefore = runScript(beforeFileName);
      final String essentializedCodeAfter = runScript(afterFileName);
      final int numWordEssentialBefore = essentializedCodeBefore.trim().length();
      final int numWordEssentialAfter = essentializedCodeAfter.trim().length();
      System.err.println("Word Count Essentialized before: " + numWordEssentialBefore);
      System.err.println("Word Count Essentialized after: " + numWordEssentialAfter);
      System.err.println("Difference: " + (numWordEssentialAfter - numWordEssentialBefore));
    } catch (final IOException e) {
      System.err.println(e.getMessage());
    }
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
    System.err.print("\n Summary: " + report.close());
  }

  private void runWordCount() {
    bash("wc " + separate.these(beforeFileName, afterFileName, essenced(beforeFileName), essenced(afterFileName)));
  }
  
  static void printHelpPrompt() {
    System.out.println("Batch Spartanizer");
    System.out.println("");
    System.out.println("Options:");
    System.out.println("  -d       default directory: use the current directory for the analysis");
    System.out.println("  -o       output directory: here go the results of the analysis");
    System.out.println("  -i       input directory: place here the projects that you want to analyze.");
    System.out.println("");
  }
}