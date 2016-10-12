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
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/**
 * @author Yossi Gil 
 * @since 2016 */
abstract class AbstractBatch {
  protected static final String folder = "/tmp/";
  protected String afterFileName;
  protected PrintWriter afters;
  protected String beforeFileName;
  protected PrintWriter befores;
  protected File currentFile;
  protected int done;
  protected String inputPath;
  protected CSVStatistics report;
  protected String reportFileName;
  protected Toolbox toolbox = new Toolbox();
  protected final ChainStringToIntegerMap spectrum = new ChainStringToIntegerMap();
  protected final ChainStringToIntegerMap coverage = new ChainStringToIntegerMap();
  protected CSVStatistics spectrumStats;
  protected CSVStatistics coverageStats;
  private String spectrumFileName;
  private String coverageFileName;
  static String presentFileName;
  static String presentMethod;
  protected static List<Class<? extends BodyDeclaration>> selectedNodeTypes = as.list(MethodDeclaration.class);
  int tippersAppliedOnCurrentObject;

  public static void main(String[] args) {
    System.out.println(system.p(12, 15));
    System.out.println(system.p(18, 15));
  }

  AbstractBatch(final String path) {
    this(path, system.folder2File(path));
  }

  AbstractBatch(final String inputPath, final String name) {
    this.inputPath = inputPath;
    beforeFileName = folder + name + ".before.java";
    afterFileName = folder + name + ".after.java";
    reportFileName = folder + name + ".CSV";
    spectrumFileName = folder + name + ".spectrum.CSV";
    coverageFileName = folder + name + ".coverage.CSV";
  }

  public void consolidateTips(final ASTRewrite r, final BodyDeclaration u) {
    toolbox = Toolbox.defaultInstance();
    u.accept(new DispatchingVisitor() {
      @Override protected <N extends ASTNode> boolean go(final N n) {
        TrimmerLog.visitation(n);
        if (disabling.on(n))
          return true;
        final Tipper<N> tipper = getTipper(n);
        if (tipper == null)
          return true;
        Tip s = null;
        try {
          s = tipper.tip(n, exclude);
          tick(n, tipper);
        } catch (final TipperFailure f) {
          monitor.debug(this, f);
        }
        if (s != null) {
          ++tippersAppliedOnCurrentObject;
          tick2(n, tipper); // save coverage info
          TrimmerLog.application(r, s);
        }
        return true;
      }

      <N extends ASTNode> void tick2(N n, Tipper<N> w) {
        String key = presentFileName + "-" + presentMethod + monitor.className(w.getClass());
        if (!coverage.containsKey(key))
          coverage.put(key, 0);
        coverage.put(key, coverage.get(key) + 1);
      }

      /** @param n
       * @param w
       * @throws TipperFailure */
      <N extends ASTNode> void tick(final N n, final Tipper<N> w) throws TipperFailure {
        tick(w);
        TrimmerLog.tip(w, n);
      }

      /** @param w */
      <N extends ASTNode> void tick(final Tipper<N> w) {
        String key = monitor.className(w.getClass());
        if (!spectrum.containsKey(key))
          spectrum.put(key, 0);
        spectrum.put(key, spectrum.get(key) + 1);
      }

      @Override protected void initialization(final ASTNode ¢) {
        disabling.scan(¢);
      }
    });
  }

  public ASTRewrite createRewrite(final BodyDeclaration u) {
    final ASTRewrite $ = ASTRewrite.create(u.getAST());
    consolidateTips($, u);
    return $;
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

  public ASTRewrite rewriterOf(final BodyDeclaration ¢) {
    final ASTRewrite $ = ASTRewrite.create(¢.getAST());
    consolidateTips($, ¢);
    return $;
  }

  protected void fire() {
    go();
    reportSpectrum();
    reportCoverage();
    runEssence();
    runWordCount();
  }

  private void reportCoverage() {
    for (Entry<String, Integer> ¢ : coverage.entrySet()) {
      int i = ¢.getKey().indexOf(".java");
      String file = ¢.getKey().substring(0, i + 5);
      coverageStats.put("File", file);
      coverageStats.put("Method", ¢.getKey());
      coverageStats.put("Spartanization", ¢.getValue());
      coverageStats.nl();
    }
    System.err.print("\n Coverage: " + coverageStats.close());
  }

  private void reportSpectrum() {
    for (Entry<String, Integer> ¢ : spectrum.entrySet()) {
      spectrumStats.put("Tipper", ¢.getKey());
      spectrumStats.put("Times", ¢.getValue());
      spectrumStats.nl();
    }
    System.err.print("\n Spectrum: " + spectrumStats.close());
  }

  boolean go(final ASTNode input) {
    tippersAppliedOnCurrentObject = 0;
    final int length = input.getLength();
    final int tokens = metrics.tokens(input + "");
    final int nodes = metrics.nodesCount(input);
    final int body = metrics.bodySize(input);
    final int statements = extract.statements(az.methodDeclaration(input).getBody()).size();
    final int tide = clean(input + "").length();
    final int essence = Essence.of(input + "").length();
    final String out = fixedPoint(input);
    final int length2 = out.length();
    final int tokens2 = metrics.tokens(out);
    final int tide2 = clean(out + "").length();
    final int essence2 = Essence.of(out + "").length();
    final int wordCount = code.wc(il.org.spartan.spartanizer.cmdline.Essence.of(out + ""));
    final ASTNode to = makeAST.CLASS_BODY_DECLARATIONS.from(out);
   final int nodes2 = metrics.nodesCount(to);
    final int body2 = metrics.bodySize(to);
    MethodDeclaration methodDeclaration = az.methodDeclaration(to);
    int statements2 = methodDeclaration == null ? -1 : extract.statements(methodDeclaration.getBody()).size();
    System.err.println(++done + " " + extract.category(input) + " " + extract.name(input));
    befores.print(input);
    afters.print(out);
    report.summaryFileName();
    report//
        .put("File", currentFile)//
        .put("Category", extract.category(input))//
        .put("Name", extract.name(input))//
        .put("# Tippers", tippersAppliedOnCurrentObject) //
        .put("Nodes1", nodes)//
        .put("Nodes2", nodes2)//
        .put("Δ Nodes", nodes - nodes2)//
        .put("δ Nodes", system.d(nodes, nodes2))//
        .put("δ Nodes %", ((system.p(nodes, nodes2))))//
        .put("Body", body)//
        .put("Body2", body2)//
        .put("Δ Body", body - body2)//
        .put("δ Body", system.d(body, body2))//
        .put("% Body", ((system.p(body, body2))))//
        .put("Length1", length)//
        .put("Tokens1", tokens)//
        .put("Tokens2", tokens2)//
        .put("Δ Tokens", tokens - tokens2)//
        .put("δ Tokens", system.d(tokens, tokens2))//
        .put("% Tokens", ((system.p(tokens, tokens2))))//
        .put("Length1", length)//
        .put("Length2", length2)//
        .put("Δ Length", length - length2)//
        .put("δ Length", system.d(length, length2))//
        .put("% Length", ((system.p(length, length2))))//
        .put("Tide1", tide)//
        .put("Tide2", tide2)//
        .put("Δ Tide2", tide - tide2)//
        .put("δ Tide2", system.d(tide, tide2))//
        .put("δ Tide2", ((system.p(tide, tide2))))//
        .put("Essence1", essence)//
        .put("Essence2", essence2)//
        .put("Δ Essence", essence - essence2)//
        .put("δ Essence", system.d(essence, essence2))//
        .put("% Essence", ((system.p(essence, essence2))))//
        .put("Statements1", statements)//
        .put("Statement2", statements2)//
        .put("Δ Statement", statements - statements2)//
        .put("δ Statement", system.d(statements, statements2))//
        .put("% Statement", ((system.p(essence, essence2))))//
        .put("Words)", wordCount).put("R(T/L)", system.ratio(length, tide)) //
        .put("R(E/L)", system.ratio(length, essence)) //
        .put("R(E/T)", system.ratio(tide, essence)) //
        .put("R(B/S)", system.ratio(nodes, body)) //
    ;
    report.nl();
    return false;
  }

  void go(final CompilationUnit u) {
    u.accept(new ASTVisitor() {
      @Override public boolean preVisit2(ASTNode ¢) {
        return !selectedNodeTypes.contains(¢.getClass()) || !filter(¢) || go(¢);
      }
    });
  }

  boolean filter(ASTNode ¢) {
    return true;
  }

  static String getEnclosingMethodName(final BodyDeclaration ¢) {
    ASTNode parentNode = ¢.getParent();
    assert parentNode != null;
    while (parentNode.getNodeType() != ASTNode.METHOD_DECLARATION) {
      if (parentNode instanceof CompilationUnit)
        return null;
      parentNode = parentNode.getParent();
    }
    return ((MethodDeclaration) parentNode).getName() + "";
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

  private void runWordCount() {
    system.bash("wc " + separate.these(beforeFileName, afterFileName, essenced(beforeFileName), essenced(afterFileName)));
  }

  private void go() {
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
    try (PrintWriter b = new PrintWriter(new FileWriter(beforeFileName)); //
        PrintWriter a = new PrintWriter(new FileWriter(afterFileName))) {
      befores = b;
      afters = a;
      report = new CSVStatistics(reportFileName, "property");
      spectrumStats = new CSVStatistics(spectrumFileName, "property");
      coverageStats = new CSVStatistics(coverageFileName, "property");
      for (final File ¢ : new FilesGenerator(".java").from(inputPath)) {
        presentFileName = ¢.getName();
        System.out.println("Free memory (bytes): " + Unit.BYTES.format(Runtime.getRuntime().freeMemory()));
        go(¢);
      }
    } catch (final IOException x) {
      x.printStackTrace();
      System.err.println(done + " items processed; processing of " + inputPath + " failed for some I/O reason");
    }
    System.err.print("\n Done: " + done + " items processed.");
    System.err.print("\n Summary: " + report.close());
  }

  protected <N extends ASTNode> Tipper<N> getTipper(final N ¢) {
    return toolbox.firstTipper(¢);
  }

  void runEssence() {
    system.shellEssenceMetrics(beforeFileName);
    system.shellEssenceMetrics(afterFileName);
  }

  /** @param ¢
   * @return */
  String fixedPoint(final ASTNode ¢) {
    return fixedPoint(¢ + "");
  }

  @SuppressWarnings("static-method") public void selectedNodes(@SuppressWarnings("unchecked") final Class<? extends BodyDeclaration>... ¢) {
    selectedNodeTypes = as.list(¢);
  }
}
