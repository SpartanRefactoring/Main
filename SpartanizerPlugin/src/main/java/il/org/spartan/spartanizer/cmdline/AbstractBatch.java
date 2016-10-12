package il.org.spartan.spartanizer.cmdline;

import static il.org.spartan.spartanizer.cmdline.system.*;
import static il.org.spartan.tide.*;

import java.io.*;
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

/** @author Yossi Gil
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
  protected CSVStatistics spectrumStats;
  private final String spectrumFileName;

  AbstractBatch(final String path) {
    this(path, system.folder2File(path));
  }

  AbstractBatch(final String inputPath, final String name) {
    this.inputPath = inputPath;
    beforeFileName = folder + name + ".before.java";
    afterFileName = folder + name + ".after.java";
    reportFileName = folder + name + ".CSV";
    spectrumFileName = folder + name + ".spectrum.CSV";
  }

  public void consolidateTips(final ASTRewrite r, final CompilationUnit u) {
    toolbox = Toolbox.defaultInstance();
    u.accept(new DispatchingVisitor() {
      @Override protected <N extends ASTNode> boolean go(final N n) {
        TrimmerLog.visitation(n);
        // if astnode is in selectedNodeType check is true
        if (!check(n) || disabling.on(n))
          return true;
        final Tipper<N> w = getTipper(n);
        if (w == null)
          return true;
        Tip s = null;
        try {
          s = w.tip(n, exclude);
          tick(n, w);
        } catch (final TipperFailure f) {
          monitor.debug(this, f);
        }
        if (s != null)
          TrimmerLog.application(r, s);
        return true;
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
        final String key = monitor.className(w.getClass());
        if (!spectrum.containsKey(key))
          spectrum.put(key, 0);
        spectrum.put(key, spectrum.get(key) + 1);
      }

      @Override protected void initialization(final ASTNode ¢) {
        disabling.scan(¢);
      }
    });
  }

  abstract boolean check(ASTNode n);

  public ASTRewrite createRewrite(final CompilationUnit ¢) {
    final ASTRewrite $ = ASTRewrite.create(¢.getAST());
    consolidateTips($, ¢);
    return $;
  }

  public String fixedPoint(final String from) {
    for (final Document $ = new Document(from);;) {
      final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from($.get());
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

  public ASTRewrite rewriterOf(final CompilationUnit ¢) {
    final ASTRewrite $ = ASTRewrite.create(¢.getAST());
    consolidateTips($, ¢);
    return $;
  }

  public static Process shellEssenceMetrics(final String fileName) {
    return system.bash("./essence < " + fileName + " >" + essenced(fileName));
  }

  protected void fire() {
    go();
    reportSpectrum();
    runEssence();
    runWordCount();
  }

  /**
   *
   */
  private void reportSpectrum() {
    for (final Entry<String, Integer> ¢ : spectrum.entrySet()) {
      spectrumStats.put("Tipper", ¢.getKey());
      spectrumStats.put("Times", ¢.getValue());
      spectrumStats.nl();
    }
    System.err.print("\n Spectrum: " + spectrumStats.close());
  }

  boolean go(final BodyDeclaration ¢) {
    System.out.println("Free memory (bytes): " + Runtime.getRuntime().freeMemory());
    final int length = ¢.getLength();
    final int tokens = metrics.tokens(¢ + "");
    final int nodes = metrics.nodesCount(¢);
    final int body = metrics.bodySize(¢);
    final int tide = clean(¢ + "").length();
    final int essence = Essence.of(¢ + "").length();
    // perform spartanization
    final String out = fixedPoint(¢);
    final int length2 = out.length();
    final int tokens2 = metrics.tokens(out);
    final int tide2 = clean(out + "").length();
    final int essence2 = Essence.of(out + "").length();
    final int wordCount = code.wc(il.org.spartan.spartanizer.cmdline.Essence.of(out + ""));
    final ASTNode from = makeAST.COMPILATION_UNIT.from(out);
    final int nodes2 = metrics.nodesCount(from);
    final int body2 = metrics.bodySize(from);
    System.err.println(++done + " " + extract.category(¢) + " " + extract.name(¢));
    // printout
    befores.print(¢);
    afters.print(out);
    // generate csv with statistics
    report.summaryFileName();
    report//
        .put("File", currentFile)//
        .put("TipperCategory", extract.category(¢))//
        .put("Name", extract.name(¢))//
        .put("Nodes1", nodes)//
        .put("Nodes2", nodes2)//
        .put("Δ Nodes", nodes - nodes2)//
        .put("δ Nodes", system.d(nodes, nodes2))//
        .put("δ Nodes %", Double.parseDouble(removePercentChar(system.p(nodes, nodes2))))//
        .put("Body", body)//
        .put("Body2", body2)//
        .put("Δ Body", body - body2)//
        .put("δ Body", system.d(body, body2))//
        .put("% Body", Double.parseDouble(removePercentChar(system.p(body, body2))))//
        .put("Length1", length)//
        .put("Tokens1", tokens)//
        .put("Tokens2", tokens2)//
        .put("Δ Tokens", tokens - tokens2)//
        .put("δ Tokens", system.d(tokens, tokens2))//
        .put("% Tokens", Double.parseDouble(removePercentChar(system.p(tokens, tokens2))))//
        .put("Length1", length)//
        .put("Length2", length2)//
        .put("Δ Length", length - length2)//
        .put("δ Length", system.d(length, length2))//
        .put("% Length", Double.parseDouble(removePercentChar(system.p(length, length2))))//
        .put("Tide1", tide)//
        .put("Tide2", tide2)//
        .put("Δ Tide2", tide - tide2)//
        .put("δ Tide2", system.d(tide, tide2))//
        .put("δ Tide2", Double.parseDouble(removePercentChar(system.p(tide, tide2))))//
        .put("Essence1", essence)//
        .put("Essence2", essence2)//
        .put("Δ Essence", essence - essence2)//
        .put("δ Essence", system.d(essence, essence2))//
        .put("% Essence", Double.parseDouble(removePercentChar(system.p(essence, essence2))))//
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
      @Override public boolean visit(final AnnotationTypeMemberDeclaration ¢) {
        return go(¢);
      }

      @Override public boolean visit(final MethodDeclaration ¢) {
        return go(¢);
      }

      @Override public boolean visit(final TypeDeclaration ¢) {
        return go(¢);
      }

      @Override public boolean visit(final EnumConstantDeclaration ¢) {
        return go(¢);
      }

      @Override public boolean visit(final FieldDeclaration ¢) {
        return go(¢);
      }

      @Override public boolean visit(final EnumDeclaration ¢) {
        return go(¢);
      }

      @Override public boolean visit(final Initializer ¢) {
        return go(¢);
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
      for (final File ¢ : new FilesGenerator(".java").from(inputPath))
        go(¢);
    } catch (final IOException x) {
      x.printStackTrace();
      System.err.println(done + " files processed; processing of " + inputPath + " failed for some I/O reason");
    }
    System.err.print("\n Done: " + done + " files processed.");
    System.err.print("\n Summary: " + report.close());
  }

  protected <N extends ASTNode> Tipper<N> getTipper(final N ¢) {
    return toolbox.firstTipper(¢);
  }

  void runEssence() {
    shellEssenceMetrics(beforeFileName);
    shellEssenceMetrics(afterFileName);
  }

  /** @param ¢
   * @return */
  String fixedPoint(final BodyDeclaration ¢) {
    return fixedPoint(¢ + "");
  }
}
