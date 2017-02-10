package il.org.spartan.spartanizer.cmdline;

import static il.org.spartan.tide.*;

import java.io.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.collections.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.utils.*;
import il.org.spartan.utils.*;

/** Scans files named by outputFolder, ignore test files, and collect
 * statistics.
 * 
 * @author Yossi Gil
 * @author Matteo Orru'
 * @since Oct 2, 2016 */
@SuppressWarnings("TooBroadScope")
final class BatchSpartanizer extends FolderASTVisitor {
  private static final String folder = "/tmp";
  private static final String script = "./src/test/resources/essence";
  private static final InteractiveSpartanizer interactiveSpartanizer = new InteractiveSpartanizer().disable(Nominal.class)
      .disable(TipperCategory.Nanos.class);
  private static String outputDir;
  private static String inputDir;
  private static boolean defaultDir;
  private int classesDone;
  private final String beforeFileName;
  private final String afterFileName;
  private PrintWriter befores;
  private PrintWriter afters;
  private CSVStatistics report;
  private final String reportFileName;

  /** Main method used to run BatchSpartanizer as a stand alone application
   * @param args */
  public static void main(final String[] args) {
    if (args.length == 0)
      printHelpPrompt();
    else {
      parseCommandLineArgs(args);
      if (inputDir != null && outputDir != null)
        spartanize();
      if (defaultDir) {
        // spartanizeDir(".");
        new BatchSpartanizer(".", "current-working-directory").fire();
        as.list(args).forEach(λ -> new BatchSpartanizer(λ).fire());
      }
    }
  }

  private static void spartanize() {
    final File input = new File(inputDir);
    if (input.isDirectory()) {
      System.out.println(" ---- Analyzing directory: " + input.getAbsolutePath() + " -------- ");
      spartanizeDir(input);
    } else {
      System.out.println(" ---- Analyzing single file: " + input.getAbsolutePath() + " -------- ");
      spartanizeFile(input);
    }
  }

  private static void spartanizeFile(final File input) {
    new BatchSpartanizer(input.getAbsolutePath()).fire();
  }

  private static void spartanizeDir(final File input) {
    for (final File ¢ : input.listFiles())
      if (¢.getName().endsWith(".java") || containsJavaFileOrJavaFileItSelf(¢)) {
        System.out.println(¢.getAbsolutePath());
        spartanizeFile(¢);
      }
  }

  public static ProcessBuilder runScript¢(final String pathname) {
    final ProcessBuilder $ = system.runScript();
    $.redirectErrorStream(true);
    $.command(script, pathname);
    return $;
  }

  private static void printHelpPrompt() {
    System.out.println("Batch GUIBatchLaconizer");
    System.out.println("");
    System.out.println("Options:");
    System.out.println("  -d       default directory: use the current directory for the analysis");
    System.out.println("  -o       output directory: here go the results of the analysis");
    System.out.println("  -i       input directory: place here the projects that you want to analyze.");
    System.out.println("");
  }

  private static void parseCommandLineArgs(final String... args) {
    for (int ¢ = 0; ¢ < args.length;)
      if ("-o".equals(args[¢])) {
        outputDir = args[¢ + 1];
        // System.out.println("OutputDir: " + outputDir);
        ¢ += 2;
      } else if ("-i".equals(args[¢])) {
        inputDir = args[¢ + 1];
        // System.out.println("InputDir: " + inputDir);
        ¢ += 2;
      } else if ("-d".equals(args[¢])) {
        inputDir = ".";
        outputDir = folder;
        ¢ += 1;
      } else {
        System.out.println(args[¢]);
        System.out.println("[ERROR]: Something went wrong!");
        ++¢;
      }
  }

  BatchSpartanizer(final String path) {
    this(path, system.folder2File(path));
  }

  BatchSpartanizer(final String presentSourcePath, final String name) {
    this.presentSourcePath = presentSourcePath;
    beforeFileName = outputDir + system.fileSeparator + name + ".before.java";
    afterFileName = outputDir + system.fileSeparator + name + ".after.java";
    reportFileName = outputDir + system.fileSeparator + name + ".CSV";
    final File dir = new File(folder + outputDir);
    if (!dir.exists())
      System.out.println(dir.mkdir());
  }

  boolean collect(final AbstractTypeDeclaration in) {
    final int length = in.getLength(), tokens = metrics.tokens(in + ""), nodes = count.nodes(in), body = metrics.bodySize(in),
        tide = clean(in + "").length(), essence = Essence.of(in + "").length();
    final String out = interactiveSpartanizer.fixedPoint(in + "");
    final int length2 = out.length(), tokens2 = metrics.tokens(out), tide2 = clean(out + "").length(), essence2 = Essence.of(out + "").length(),
        wordCount = system.wc(Essence.of(out + ""));
    final ASTNode from = makeAST.COMPILATION_UNIT.from(out);
    final int nodes2 = count.nodes(from), body2 = metrics.bodySize(from);
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
        .put("δ Nodes", system.d(nodes, nodes2))//
        .put("δ Nodes %", system.p(nodes, nodes2))//
        .put("Body", body)//
        .put("Body2", body2)//
        .put("Δ Body", body - body2)//
        .put("δ Body", system.d(body, body2))//
        .put("% Body", system.p(body, body2))//
        .put("Length1", length)//
        .put("Tokens1", tokens)//
        .put("Tokens2", tokens2)//
        .put("Δ Tokens", tokens - tokens2)//
        .put("δ Tokens", system.d(tokens, tokens2))//
        .put("% Tokens", system.p(tokens, tokens2))//
        .put("Length1", length)//
        .put("Length2", length2)//
        .put("Δ Length", length - length2)//
        .put("δ Length", system.d(length, length2))//
        .put("% Length", system.p(length, length2))//
        .put("Tide1", tide)//
        .put("Tide2", tide2)//
        .put("Δ Tide2", tide - tide2)//
        .put("δ Tide2", system.d(tide, tide2))//
        .put("δ Tide2", system.p(tide, tide2))//
        .put("Essence1", essence)//
        .put("Essence2", essence2)//
        .put("Δ Essence", essence - essence2)//
        .put("δ Essence", system.d(essence, essence2))//
        .put("% Essence", system.p(essence, essence2))//
        .put("Words)", wordCount)//
        .put("R(T/L)", system.ratio(length, tide)) //
        .put("R(E/L)", system.ratio(length, essence)) //
        .put("R(E/T)", system.ratio(tide, essence)) //
        .put("R(B/S)", system.ratio(nodes, body)) //
    ;
    report.nl();
    return false;
  }

  @Override void collect(final CompilationUnit u) {
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

  private void collect(final File f) {
    if (!system.isTestFile(f))
      try {
        collect(FileUtils.read(f));
      } catch (final IOException ¢) {
        monitor.infoIOException(¢, "File = " + f);
      }
  }

  @Override void collect(final String javaCode) {
    collect((CompilationUnit) makeAST.COMPILATION_UNIT.from(javaCode));
  }

  void fire() {
    collect();
    runEssence();
    runWordCount();
    System.err.printf("\n Our batch applicator had %d tippers dispersed over %d hooks\n", //
        box.it(interactiveSpartanizer.toolbox.tippersCount()), //
        box.it(Toolbox.hooksCount())//
    );
  }

  private void runEssence() {
    system.shellEssenceMetrics(beforeFileName);
    system.shellEssenceMetrics(afterFileName);
  }

  private void applyEssenceCommandLine() {
    try {
      final int numWordEssentialBefore = system.runScript(beforeFileName).trim().length(),
          numWordEssentialAfter = system.runScript(afterFileName).trim().length();
      System.err.println("Word Count Essentialized before: " + numWordEssentialBefore);
      System.err.println("Word Count Essentialized after: " + numWordEssentialAfter);
      System.err.println("Difference: " + (numWordEssentialAfter - numWordEssentialBefore));
    } catch (final IOException ¢) {
      System.err.println(¢.getMessage());
    }
  }

  private void collect() {
    System.err.printf(
        "Input path=%s\n" + //
            "Collective before path=%s\n" + //
            "Collective after path=%s\n" + //
            "\n", //
        presentSourcePath, //
        beforeFileName, //
        afterFileName);
    try (PrintWriter b = new PrintWriter(new FileWriter(beforeFileName)); //
        PrintWriter a = new PrintWriter(new FileWriter(afterFileName))) {
      befores = b;
      afters = a;
      report = new CSVStatistics(reportFileName, "property");
      new FilesGenerator(".java").from(presentSourcePath).forEach(this::collect);
    } catch (final IOException ¢) {
      monitor.infoIOException(¢, beforeFileName + "|" + afterFileName);
      System.err.println(classesDone + " files processed; processing of " + presentSourcePath + " failed for some I/O reason");
    }
    applyEssenceCommandLine();
    System.err.print("\n Done: " + classesDone + " files processed.");
    System.err.print("\n Summary: " + report.close());
  }

  private void runWordCount() {
    system.bash("wc " + separate.these(beforeFileName, afterFileName, system.essenced(beforeFileName), system.essenced(afterFileName)));
  }

  private static boolean containsJavaFileOrJavaFileItSelf(final File f) {
    if (f.getName().endsWith(".java"))
      return true;
    if (f.isDirectory())
      for (final File ff : f.listFiles())
        if (f.isDirectory() && containsJavaFileOrJavaFileItSelf(ff) || f.getName().endsWith(".java"))
          return true;
    return false;
  }

  /** This method is called from outside, like in the case of
   * {@link InteractiveSpartanizer}
   * @param fileNames */
  public static void fire(final String... fileNames) {
    inputDir = fileNames[0];
    outputDir = folder;
    spartanize();
  }
}
