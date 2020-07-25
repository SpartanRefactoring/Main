package il.org.spartan.spartanizer.cmdline.good;

import static il.org.spartan.tide.clean;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Stream;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import fluent.ly.as;
import fluent.ly.box;
import fluent.ly.note;
import fluent.ly.separate;
import fluent.ly.system;
import il.org.spartan.CSVStatistics;
import il.org.spartan.Essence;
import il.org.spartan.collections.FilesGenerator;
import il.org.spartan.spartanizer.ast.factory.makeAST;
import il.org.spartan.spartanizer.ast.navigate.countOf;
import il.org.spartan.spartanizer.ast.navigate.extract;
import il.org.spartan.spartanizer.ast.nodes.metrics.Metrics;
import il.org.spartan.spartanizer.cmdline.library.FileHeuristics;
import il.org.spartan.spartanizer.tipping.categories.Category;
import il.org.spartan.spartanizer.tipping.categories.Nominal;
import il.org.spartan.utils.FileUtils;

/** Scans files named by outputFolder, forget test files, and collect
 * statistics.
 * @author Yossi Gil
 * @author Matteo Orru'
 * @since Oct 2, 2016 */
@SuppressWarnings("TooBroadScope")
final class BatchSpartanizer extends DeprecatedFolderASTVisitor {
  private static final String folder = "/tmp";
  private static final String script = "./src/test/resources/essence";
  private static final InteractiveSpartanizer interactiveSpartanizer = new InteractiveSpartanizer().disable(Nominal.class)
      .disable(Category.Nanos.class);
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
    System.out.println("Batch" + system.myFullClassName());
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
      } else {
        if ("-d".equals(args[¢])) {
          inputDir = ".";
          outputDir = folder;
        } else {
          System.out.println(args[¢]);
          System.out.println("[ERROR]: Something went wrong!");
        }
        ++¢;
      }
  }
  BatchSpartanizer(final String path) {
    this(path, system.folder2File(path));
  }
  BatchSpartanizer(final String presentSourcePath, final String name) {
    this.presentSourcePath = presentSourcePath;
    beforeFileName = outputDir + File.separator + name + ".before.java";
    afterFileName = outputDir + File.separator + name + ".after.java";
    reportFileName = outputDir + File.separator + name + ".CSV";
    final File dir = new File(folder + outputDir);
    if (!dir.exists())
      System.out.println(dir.mkdir());
  }
  boolean collect(final AbstractTypeDeclaration in) {
    final int length = in.getLength(), tokens = Metrics.tokens(in + ""), nodes = countOf.nodes(in), body = Metrics.bodySize(in),
        tide = clean(in + "").length(), essence = Essence.of(in + "").length();
    final String out = interactiveSpartanizer.fixedPoint(in + "");
    final int length2 = out.length(), tokens2 = Metrics.tokens(out), tide2 = clean(out).length(), essence2 = Essence.of(out).length(),
        wordCount = system.wc(Essence.of(out));
    final ASTNode from = makeAST.COMPILATION_UNIT.from(out);
    final int nodes2 = countOf.nodes(from), body2 = Metrics.bodySize(from);
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
        .put("δ Nodes", FileHeuristics.d(nodes, nodes2))//
        .put("δ Nodes %", FileHeuristics.p(nodes, nodes2))//
        .put("Body", body)//
        .put("Body2", body2)//
        .put("Δ Body", body - body2)//
        .put("δ Body", FileHeuristics.d(body, body2))//
        .put("% Body", FileHeuristics.p(body, body2))//
        .put("Length1", length)//
        .put("Tokens1", tokens)//
        .put("Tokens2", tokens2)//
        .put("Δ Tokens", tokens - tokens2)//
        .put("δ Tokens", FileHeuristics.d(tokens, tokens2))//
        .put("% Tokens", FileHeuristics.p(tokens, tokens2))//
        .put("Length1", length)//
        .put("Length2", length2)//
        .put("Δ Length", length - length2)//
        .put("δ Length", FileHeuristics.d(length, length2))//
        .put("% Length", FileHeuristics.p(length, length2))//
        .put("Tide1", tide)//
        .put("Tide2", tide2)//
        .put("Δ Tide2", tide - tide2)//
        .put("δ Tide2", FileHeuristics.d(tide, tide2))//
        .put("δ Tide2", FileHeuristics.p(tide, tide2))//
        .put("Essence1", essence)//
        .put("Essence2", essence2)//
        .put("Δ Essence", essence - essence2)//
        .put("δ Essence", FileHeuristics.d(essence, essence2))//
        .put("% Essence", FileHeuristics.p(essence, essence2))//
        .put("Words)", wordCount)//
        .put("R(T/L)", FileHeuristics.ratio(length, tide)) //
        .put("R(E/L)", FileHeuristics.ratio(length, essence)) //
        .put("R(E/T)", FileHeuristics.ratio(tide, essence)) //
        .put("R(B/S)", FileHeuristics.ratio(nodes, body)) //
    ;
    report.nl();
    return false;
  }
  @Override void collect(final CompilationUnit u) {
    u.accept(new ASTVisitor(true) {
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
    if (!FileHeuristics.isTestFile(f))
      try {
        collect(FileUtils.read(f));
      } catch (final IOException ¢) {
        note.io(¢, "File = " + f);
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
        box.it(interactiveSpartanizer.traversals.traversal.toolbox.tippersCount()), //
        box.it(il.org.spartan.spartanizer.traversal.Toolboxes.hooksCount())//
    );
  }
  private void runEssence() {
    system.shellEssenceMetrics(beforeFileName);
    system.shellEssenceMetrics(afterFileName);
  }
  private void applyEssenceCommandLine() {
    try {
      final int numWordEssentialBefore = BatchSpartanizer.runScript(beforeFileName).trim().length(),
          numWordEssentialAfter = BatchSpartanizer.runScript(afterFileName).trim().length();
      System.err.println("Word Count Essentialized before: " + numWordEssentialBefore);
      System.err.println("Word Count Essentialized after: " + numWordEssentialAfter);
      System.err.println("Difference: " + (numWordEssentialAfter - numWordEssentialBefore));
    } catch (final IOException ¢) {
      note.bug(¢);
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
      note.io(¢, beforeFileName + "|" + afterFileName);
      System.err.println(classesDone + " files processed; processing of " + presentSourcePath + " failed for some I/O reason");
    }
    applyEssenceCommandLine();
    System.err.print("\n Done: " + classesDone + " files processed.");
    System.err.print("\n Summary: " + report.close());
  }
  private void runWordCount() {
    system.bash("wc " + separate.these(beforeFileName, afterFileName, system.essenced(beforeFileName), system.essenced(afterFileName)));
  }
  public static String runScript(final String pathname) throws IOException {
    return system.runScript(BatchSpartanizer.runScript¢(pathname).start());
  }
  private static boolean containsJavaFileOrJavaFileItSelf(final File f) {
    return f.getName().endsWith(".java") || f.isDirectory()
        && Stream.of(f.listFiles()).anyMatch(λ -> f.isDirectory() && containsJavaFileOrJavaFileItSelf(λ) || f.getName().endsWith(".java"));
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
