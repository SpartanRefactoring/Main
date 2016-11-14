package il.org.spartan.spartanizer.cmdline;

import static il.org.spartan.spartanizer.cmdline.system.*;
import static il.org.spartan.tide.*;

import java.io.*;
import java.util.*;
import java.util.Map.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;

import il.org.spartan.*;
import il.org.spartan.collections.*;
import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.utils.*;
import il.org.spartan.utils.*;

public abstract class AbstractCommandLineSpartanizerOld {
  static String presentFileName;
  static String presentMethod;
  static List<Class<? extends BodyDeclaration>> selectedNodeTypes = as.list(MethodDeclaration.class);

  static AbstractGUIApplicator getSpartanizer(final String tipperName) {
    return Tips2.get(tipperName);
  }

  protected String folder = "/tmp/";
  protected String afterFileName;
  protected String beforeFileName;
  protected String inputPath;
  protected String reportFileName;
  protected String spectrumFileName;
  protected PrintWriter afters;
  protected PrintWriter befores;
  File currentFile;
  int done;
  int tippersAppliedOnCurrentObject;
  CSVStatistics report;
  CSVStatistics spectrumStats;
  CSVStatistics coverageStats;
  Toolbox toolbox = new Toolbox();
  final ChainStringToIntegerMap spectrum = new ChainStringToIntegerMap();
  final ChainStringToIntegerMap coverage = new ChainStringToIntegerMap();

  public abstract void apply();

  public void consolidateTips(final ASTRewrite r, final BodyDeclaration u) {
    toolbox = Toolbox.defaultInstance();
    u.accept(new DispatchingVisitor() {
      @Override protected <N extends ASTNode> boolean go(final N n) {
        TrimmerLog.visitation(n);
        if (disabling.on(n))
          return true;
        Tipper<N> tipper = null;
        try {
          tipper = getTipper(n);
        } catch (final Exception x) {
          monitor.debug(this, x);
        }
        if (tipper == null)
          return true;
        Tip s = null;
        try {
          s = tipper.tip(n, exclude);
          tick(n, tipper);
        } catch (final Exception x) {
          monitor.debug(this, x);
        }
        if (s != null) {
          ++tippersAppliedOnCurrentObject;
          tick2(tipper); // save coverage info
          TrimmerLog.application(r, s);
        }
        return true;
      }

      @Override protected void initialization(final ASTNode ¢) {
        disabling.scan(¢);
      }

      /** @param n
       * @param w */
      <N extends ASTNode> void tick(final N n, final Tipper<N> w) {
        tick(w);
        TrimmerLog.tip(w, n);
      }

      /** @param w */
      <N extends ASTNode> void tick(final Tipper<N> w) {
        final String key = monitor.className(w.getClass());
        if (!spectrum.containsKey(key))
          spectrum.put(key, 0);
        spectrum.put(key, spectrum.get(key) + 1);
      }

      <N extends ASTNode> void tick2(final Tipper<N> w) {
        final String key = presentFileName + "-" + presentMethod + monitor.className(w.getClass());
        if (!coverage.containsKey(key))
          coverage.put(key, 0);
        coverage.put(key, coverage.get(key) + 1);
      }
    });
  }

  public ASTRewrite createRewrite(final BodyDeclaration u) {
    final ASTRewrite $ = ASTRewrite.create(u.getAST());
    consolidateTips($, u);
    return $;
  }

  void fire() {
    run();
    reportSpectrum();
    // reportCoverage();
    runEssence();
    runWordCount();
  }

  /** @param ¢
   * @return */
  String fixedPoint(final ASTNode ¢) {
    return fixedPoint(¢ + "");
  }

  public String fixedPoint(final String from) {
    for (final Document $ = new Document(from);;) {
      final BodyDeclaration u = (BodyDeclaration) makeAST.CLASS_BODY_DECLARATIONS.from($.get());
      final ASTRewrite r = createRewrite(u);
      final TextEdit e = r.rewriteAST($, null);
      try {
        e.apply($);
      } catch (final MalformedTreeException | IllegalArgumentException | BadLocationException x) {
        monitor.logEvaluationError(this, x);
        throw new AssertionError(x);
      }
      if (!e.hasChildren())
        return $.get();
    }
  }

  <N extends ASTNode> Tipper<N> getTipper(final N ¢) {
    return toolbox.firstTipper(¢);
  }

  private void run() {
    System.err.printf( //
        " Input path=%s\n" + //
            "Before path=%s\n" + //
            " After path=%s\n" + //
            "Report path=%s\n" + //
            "\n", //
        inputPath, //
        beforeFileName, //
        afterFileName, //
        reportFileName);
    apply();
    closePrintWriters();
    System.err.print("\n Done: " + done + " items processed.");
    System.err.print("\n Summary: " + report.close());
  }

  private void closePrintWriters() {
    befores.close();
    afters.close();
  }

  boolean go(final ASTNode input) {
    tippersAppliedOnCurrentObject = 0;
    final String output = fixedPoint(input);
    final ASTNode outputASTNode = makeAST.CLASS_BODY_DECLARATIONS.from(output);
    befores.print(input);
    afters.print(output);
    computeMetrics(input, outputASTNode);
    return false;
  }

  /** @param input
   * @param output */
  protected void computeMetrics(final ASTNode input, final ASTNode output) {
    // input metrics
    final int length = input.getLength();
    final int tokens = metrics.tokens(input + "");
    final int nodes = count.nodes(input);
    final int body = metrics.bodySize(input);
    final int statements = extract.statements(az.methodDeclaration(input).getBody()).size();
    final int tide = clean(input + "").length();
    final int essence = Essence.of(input + "").length();
    // output metrics
    final String outputString = output + "";
    final int length2 = outputString.length();
    final int tokens2 = metrics.tokens(outputString);
    final int nodes2 = count.nodes(output);
    final int body2 = metrics.bodySize(output);
    final MethodDeclaration methodDeclaration = az.methodDeclaration(output);
    final int statements2 = methodDeclaration == null ? -1 : extract.statements(methodDeclaration.getBody()).size();
    final int tide2 = clean(outputString).length();
    final int essence2 = Essence.of(outputString).length();
    final int wordCount = code.wc(il.org.spartan.spartanizer.cmdline.Essence.of(outputString));
    // final ASTNode to = makeAST.CLASS_BODY_DECLARATIONS.from(output);
    System.err.println(++done + " " + extract.category(input) + " " + extract.name(input));
    System.out.println(befores.checkError());
    report.summaryFileName();
    report//
        // .put("File", currentFile)//
        .put("Category", extract.category(input))//
        .put("Name", extract.name(input))//
        .put("# Tippers", tippersAppliedOnCurrentObject) //
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
        .put("Statements1", statements)//
        .put("Statement2", statements2)//
        .put("Δ Statement", statements - statements2)//
        .put("δ Statement", system.d(statements, statements2))//
        .put("% Statement", system.p(essence, essence2))//
        .put("Words)", wordCount).put("R(T/L)", system.ratio(length, tide)) //
        .put("R(E/L)", system.ratio(length, essence)) //
        .put("R(E/T)", system.ratio(tide, essence)) //
        .put("R(B/S)", system.ratio(nodes, body)) //
    ;
    report.nl();
  }

  void go(final CompilationUnit u) {
    u.accept(new ASTVisitor() {
      @Override public boolean preVisit2(final ASTNode ¢) {
        System.out.println(!selectedNodeTypes.contains(¢.getClass()) || go(¢));
        return !selectedNodeTypes.contains(¢.getClass()) || go(¢);
      }
    });
  }

  void go(final File f) {
    if (!system.isTestFile(f))
      try {
        currentFile = f;
        go(FileUtils.read(f));
      } catch (final IOException e) {
        monitor.infoIOException(e, "File = " + f);
      }
  }

  void go(final String javaCode) {
    go((CompilationUnit) makeAST.COMPILATION_UNIT.from(javaCode));
  }

  private void reportSpectrum() {
    for (final Entry<String, Integer> ¢ : spectrum.entrySet()) {
      spectrumStats.put("Tipper", ¢.getKey());
      spectrumStats.put("Times", ¢.getValue());
      spectrumStats.nl();
    }
    System.err.print("\n Spectrum: " + spectrumStats.close());
  }

  public ASTRewrite rewriterOf(final BodyDeclaration ¢) {
    final ASTRewrite $ = ASTRewrite.create(¢.getAST());
    consolidateTips($, ¢);
    return $;
  }

  void runEssence() {
    system.shellEssenceMetrics(beforeFileName);
    system.shellEssenceMetrics(afterFileName);
  }

  private void runWordCount() {
    system.bash("wc " + separate.these(beforeFileName, afterFileName, essenced(beforeFileName), essenced(afterFileName)));
  }

  @SuppressWarnings("static-method") public void selectedNodes(@SuppressWarnings("unchecked") final Class<? extends BodyDeclaration>... ¢) {
    selectedNodeTypes = as.list(¢);
  }

  /** Setup PrintWriters
   * @author matteo */
  protected void setUpPrintWriters() {
    try (PrintWriter b = new PrintWriter(new FileWriter(beforeFileName)); //
        PrintWriter a = new PrintWriter(new FileWriter(afterFileName))) {
      befores = b;
      afters = a;
    } catch (final IOException x) {
      x.printStackTrace();
      System.err.println(done + " items processed; processing of " + inputPath + " failed for some I/O reason");
    }
  }

  /** Setup reports
   * @author matteo */
  protected void setUpReports() {
    try {
      report = new CSVStatistics(reportFileName, "property");
      spectrumStats = new CSVStatistics(spectrumFileName, "property");
    } catch (final IOException x) {
      x.printStackTrace();
      System.err.println("problem in setting up reports");
    }
  }
}