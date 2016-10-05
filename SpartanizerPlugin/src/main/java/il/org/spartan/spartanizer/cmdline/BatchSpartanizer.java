package il.org.spartan.spartanizer.cmdline;

import static il.org.spartan.tide.*;

import java.io.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.bench.*;
import il.org.spartan.collections.*;
import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.utils.*;

/** Scans files named by folder, ignore test files, and collect statistics.
 * @author Yossi Gil
 * @year 2015 */
public final class BatchSpartanizer {
  private static final String folder = "/tmp/";
  private static final String script = "./Essence";
  private static final InteractiveSpartanizer interactiveSpartanizer = new InteractiveSpartanizer().disable(Nominal.class).disable(Nanos.class);
  private static boolean defaultDir;
  private static String outputDir;
  private static String inputDir;

  public static void main(final String[] args) {
    if (args.length == 0)
      printHelpPrompt();
    else {
      parseCommandLineArgs(args);
      if (inputDir != null && outputDir != null)
        for (final File ¢ : new File(inputDir).listFiles()) {
          System.out.println(¢.getAbsolutePath());
          new BatchSpartanizer(¢.getAbsolutePath()).fire();
        }
      if (defaultDir) {
        new BatchSpartanizer(".", "current-working-directory").fire();
        for (final String ¢ : args)
          new BatchSpartanizer(¢).fire();
      }
    }
  }

  public static ProcessBuilder runScript¢(final String pathname) {
    final ProcessBuilder $ = system.runScript();
    $.redirectErrorStream(true);
    $.command(script, pathname);
    return $;
  }

  static double d(final double n1, final double n2) {
    return 1 - n2 / n1;
  }

  static String p(final int n1, final int n2) {
    return Unit.formatRelative(d(n1, n2));
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

  /** @param args */
  private static void parseCommandLineArgs(final String[] args) {
    for (int ¢ = 0; ¢ < args.length;)
      if ("-o".equals(args[¢])) {
        outputDir = args[¢ + 1];
        System.out.println(outputDir);
        ¢ += 2;
      } else if ("-i".equals(args[¢])) {
        inputDir = args[¢ + 1];
        System.out.println(inputDir);
        ¢ += 2;
      } else {
        System.out.println(args[¢]);
        System.out.println("something went wrong!");
        ++¢;
      }
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
    this(path, system.folder2File(path));
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
        return system.dumpOutput(p);
    } catch (final IOException x) {
      monitor.logProbableBug(this, x);
    }
    return null;
  }

  public Process shellEssenceMetrics(final String fileName) {
    return bash("./essence < " + fileName + " >" + system.essenced(fileName));
  }

  boolean collect(final AbstractTypeDeclaration in) {
    final int length = in.getLength();
    final int tokens = metrics.tokens(in + "");
    final int nodes = metrics.nodesCount(in);
    final int body = metrics.bodySize(in);
    final int tide = clean(in + "").length();
    final int essence = code.essenceNew(in + "").length();
    final String out = interactiveSpartanizer.fixedPoint(in + "");
    final int length2 = out.length();
    final int tokens2 = metrics.tokens(out);
    final int tide2 = clean(out + "").length();
    final int essence2 = code.essenceNew(out + "").length();
    final int wordCount = code.wc(code.essenceNew(out + ""));
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
        .put("δ Nodes", d(nodes, nodes2))//
        .put("δ Nodes %", Double.parseDouble(system.removePercentChar(p(nodes, nodes2))))//
        .put("Body", body)//
        .put("Body2", body2)//
        .put("Δ Body", body - body2)//
        .put("δ Body", d(body, body2))//
        .put("% Body", Double.parseDouble(system.removePercentChar(p(body, body2))))//
        .put("Length1", length)//
        .put("Tokens1", tokens)//
        .put("Tokens2", tokens2)//
        .put("Δ Tokens", tokens - tokens2)//
        .put("δ Tokens", d(tokens, tokens2))//
        .put("% Tokens", Double.parseDouble(system.removePercentChar(p(tokens, tokens2))))//
        .put("Length1", length)//
        .put("Length2", length2)//
        .put("Δ Length", length - length2)//
        .put("δ Length", d(length, length2))//
        .put("% Length", Double.parseDouble(system.removePercentChar(p(length, length2))))//
        .put("Tide1", tide)//
        .put("Tide2", tide2)//
        .put("Δ Tide2", tide - tide2)//
        .put("δ Tide2", d(tide, tide2))//
        .put("δ Tide2", Double.parseDouble(system.removePercentChar(p(tide, tide2))))//
        .put("Essence1", essence)//
        .put("Essence2", essence2)//
        .put("Δ Essence", essence - essence2)//
        .put("δ Essence", d(essence, essence2))//
        .put("% Essence", Double.parseDouble(system.removePercentChar(p(essence, essence2))))//
        .put("Words)", wordCount).put("R(T/L)", system.ratio(length, tide)) //
        .put("R(E/L)", system.ratio(length, essence)) //
        .put("R(E/T)", system.ratio(tide, essence)) //
        .put("R(B/S)", system.ratio(nodes, body)) //
    ;
    report.nl();
    // System.out.println("δ Nodes %: " + report.get("δ Nodes %"));
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
        monitor.infoIOException(e, "File = " + f);
      }
  }

  void collect(final String javaCode) {
    collect((CompilationUnit) makeAST.COMPILATION_UNIT.from(javaCode));
  }

  void fire() {
    collect();
    runEssence();
    runWordCount();
    System.err.printf("\n Our batch applicator had %d tippers dispersed over %d hooks\n", //
        box.it(interactiveSpartanizer.toolbox.tippersCount()), //
        box.it(interactiveSpartanizer.toolbox.hooksCount())//
    );
  }

  void runEssence() {
    shellEssenceMetrics(beforeFileName);
    shellEssenceMetrics(afterFileName);
  }

  private void applyEssenceCommandLine() {
    try {
      final String essentializedCodeBefore = system.runScript(beforeFileName);
      final String essentializedCodeAfter = system.runScript(afterFileName);
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
            "\n", //
        inputPath, //
        beforeFileName, //
        afterFileName);
    try (PrintWriter b = new PrintWriter(new FileWriter(beforeFileName)); //
        PrintWriter a = new PrintWriter(new FileWriter(afterFileName))) {
      befores = b;
      afters = a;
      report = new CSVStatistics(reportFileName, "property");
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
    bash("wc " + separate.these(beforeFileName, afterFileName, system.essenced(beforeFileName), system.essenced(afterFileName)));
  }
}