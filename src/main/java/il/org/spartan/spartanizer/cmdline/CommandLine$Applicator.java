package il.org.spartan.spartanizer.cmdline;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;

import il.org.spartan.*;
import il.org.spartan.collections.*;
import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.cmdline.report.*;
import il.org.spartan.spartanizer.cmdline.report.ConfigurableReport.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.utils.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Specific applicator
 * @author Matteo Orru'
 * @since 2016 */
public class CommandLine$Applicator extends Generic$Applicator {
  final ChainStringToIntegerMap spectrum = new ChainStringToIntegerMap();
  final ChainStringToIntegerMap coverage = new ChainStringToIntegerMap();
  private String presentMethod;
  public static String presentFileName;
  public static String presentFilePath;
  public static long startingTimePerFile;
  public static long lastTime;
  public static long startingTime;

  public CommandLine$Applicator() {}

  public CommandLine$Applicator(final String[] classes) {
    super(classes);
  }

  public CommandLine$Applicator(final String[] classes, final String[] tipperGroups) {
    super(classes, tipperGroups);
  }

  public CommandLine$Applicator(final String[] classes, final String[] tipperGroups, final String[] excludedTipperGroups) {
    super(classes, removeExcludedTippers(tipperGroups, excludedTipperGroups));
  }

  public CommandLine$Applicator(final String[] classes, final String[] tipperGroups, final String[] excludedTipperGroups,
      final String[] excludedNanoPatterns) {
    // left intentionally empty
    super(classes, removeExcludedNanoPatternsAndTippers(tipperGroups, excludedTipperGroups, excludedNanoPatterns));
  }

  private static String[] removeExcludedNanoPatternsAndTippers(final String[] tipperGroups, final String[] excludedTipperGroups,
      final String[] excludedNanoPatterns) {
    return removeExcludedNanoPatterns(removeExcludedTippers(tipperGroups, excludedTipperGroups), excludedNanoPatterns);
  }

  private static String[] removeExcludedNanoPatterns(@Nullable final String[] tipperGroups, final String[] excludedNanoPatterns) {
    return Stream.of(tipperGroups != null ? tipperGroups : setAllTipperGroups().toArray(new String[] {}))
        .filter(λ -> !as.list(excludedNanoPatterns).contains(λ)).collect(Collectors.toList()).toArray(new String[] {});
  }

  private static String[] removeExcludedTippers(@Nullable final String[] tipperGroups, final String[] excludedTipperGroups) {
    return Stream.of(tipperGroups != null ? tipperGroups : setAllTipperGroups().toArray(new String[] {}))
        .filter(λ -> !as.list(excludedTipperGroups).contains(λ)).collect(Collectors.toList()).toArray(new String[] {});
  }

  private void go(@NotNull final CompilationUnit u) {
    // extract.type(u);
    // ReportGenerator.report("tips").put("FileName", presentFileName);
    // ReportGenerator.report("tips").put("FilePath", presentFilePath);
    // ReportGenerator.report("tips").put("ClassLOCBefore", count.lines(u));
    // ReportGenerator.report("tips").put("ClassTokenBefore", metrics.tokens(u +
    // ""));
    ReportGenerator.Util.initialize();
    u.accept(new ASTVisitor() {
      @Override public boolean preVisit2(@NotNull final ASTNode ¢) {
        assert ¢ != null;
        System.out.println(¢.getClass() + ": " + selectedNodeTypes.contains(¢.getClass()));
        return !selectedNodeTypes.contains(¢.getClass()) || go(¢);
      }
    });
  }

  boolean go(@NotNull final ASTNode input) {
    tippersAppliedOnCurrentObject = 0;
    System.out.println(input.getClass());
    ReportGenerator.report("metrics").put("File", presentFileName);
    ReportGenerator.report("methods").put("File", presentFileName);
    final String output = fixedPoint(input);
    final ASTNode outputASTNode = makeAST.COMPILATION_UNIT.from(output); // instead
                                                                          // of
                                                                          // CLASS_BODY_DECLARATIONS
    // ReportGenerator.report("tips").put("ClassLOCAfter",
    // count.lines(outputASTNode));
    // ReportGenerator.report("tips").put("ClassTokenAfter",
    // metrics.tokens(output));
    // ReportGenerator.report("tips").put("FileName", presentFileName);
    // ReportGenerator.report("tips").put("FilePath", presentFilePath);
    ReportGenerator.printFile(input + "", "before");
    ReportGenerator.printFile(output, "after");
    MetricsReport.getSettings(); // ?
    // add ASTNode to MetricsReport
    Settings.addInput(input);
    MetricsReport.getSettings(); // ?
    Settings.addOutput(outputASTNode);
    computeMetrics(input, outputASTNode);
    if (input instanceof TypeDeclaration)
      computeMethodMetrics(input, outputASTNode);
    return false;
  }

  private void computeMethodMetrics(@NotNull final ASTNode input, final ASTNode output) {
    System.err.println(++done + " " + extract.category(input) + " " + extract.name(input));
    ReportGenerator.summaryFileName("methods");
    ReportGenerator.name(input);
    ReportGenerator.writeMethodMetrics(input, output, "methods");
    ReportGenerator.nl("methods");
  }

  @SuppressWarnings({ "boxing" }) private void computeMetrics(@NotNull final ASTNode input, final ASTNode output) {
    System.err.println(++done + " " + extract.category(input) + " " + extract.name(input));
    // ReportGenerator.report("tips").put("Name", extract.name(input));
    // ReportGenerator.report("tips").put("Category", extract.category(input));
    ReportGenerator.summaryFileName("metrics");
    ReportGenerator.name(input);
    ReportGenerator.writeMetrics(input, output, "metrics");
    ReportGenerator.write(input, output, "Δ ", (n1, n2) -> (n1 - n2));
    ReportGenerator.write(input, output, "δ ", system::d);
    ReportGenerator.writePerc(input, output, "δ ");
    // Reports.writeRatio(input, output, "", (n1,n2)->(n1/n2));
    ReportGenerator.nl("metrics");
  }

  private String fixedPoint(final ASTNode ¢) {
    return fixedPoint(¢ + "");
  }

  private String fixedPoint(final String from) {
    for (final Document $ = new Document(from);;) {
      final TextEdit e = createRewrite((CompilationUnit) makeAST.COMPILATION_UNIT.from($.get())).rewriteAST($, null);
      try {
        e.apply($);
      } catch (@NotNull final MalformedTreeException | IllegalArgumentException | BadLocationException ¢) {
        monitor.logEvaluationError(this, ¢);
        throw new AssertionError(¢);
      }
      if (!e.hasChildren())
        return $.get();
    }
  }

  /** createRewrite on CompilationUnit
   * @param ¢
   * @return */
  @NotNull private ASTRewrite createRewrite(@NotNull final CompilationUnit ¢) {
    final ASTRewrite $ = ASTRewrite.create(¢.getAST());
    lastTime = new Date().getTime();
    consolidateTips($, ¢);
    ReportGenerator.report("metrics").put("# Tippers", tippersAppliedOnCurrentObject);
    return $;
  }

  /** createRewrite on BodyDeclaration TODO Matteo -- this gonna be removed? --
   * matteo
   * @param u
   * @return */
  @NotNull public ASTRewrite createRewrite(@NotNull final BodyDeclaration u) {
    final ASTRewrite $ = ASTRewrite.create(u.getAST());
    consolidateTips($, u);
    return $;
  }

  /** consolidate tips on CompilationUnit
   * @param r
   * @param u */
  private void consolidateTips(final ASTRewrite r, @NotNull final CompilationUnit u) {
    toolbox = Toolbox.defaultInstance();
    u.accept(new DispatchingVisitor() {
      @Override @SuppressWarnings("boxing") protected <N extends ASTNode> boolean go(@NotNull final N n) {
        TrimmerLog.visitation(n);
        if (disabling.on(n))
          return true;
        Tipper<N> tipper = null;
        try {
          tipper = getTipper(n);
        } catch (@NotNull final Exception ¢) {
          monitor.debug(this, ¢);
        }
        if (tipper == null)
          return true;
        Tip s = null;
        try {
          s = tipper.tip(n, exclude);
        } catch (@NotNull final Exception ¢) {
          monitor.debug(this, ¢);
        }
        if (s != null) {
          ++tippersAppliedOnCurrentObject;
          final AbstractTypeDeclaration includingClass = yieldAncestors.untilContainingType().from(n);
          ReportGenerator.report("tips").put("including Class", includingClass.getName());
          ReportGenerator.report("tips").put("Class LOC", count.lines(includingClass));
          ReportGenerator.report("tips").put("Class Tokens", metrics.tokens(includingClass + ""));
          final MethodDeclaration includingMethod = yieldAncestors.untilContainingMethod().from(n);
          ReportGenerator.report("tips").put("including Method", includingMethod == null ? "not in method" : includingMethod.getName());
          ReportGenerator.report("tips").put("Method LOC", includingMethod == null ? "not applicable" : count.lines(includingMethod));
          ReportGenerator.report("tips").put("Method Tokens", includingMethod == null ? "not applicable" : metrics.tokens(includingMethod + ""));
          ReportGenerator.writeTipsLine(n, s, "tips");
          // tick2(tipper); // save coverage info
          TrimmerLog.application(r, s);
        }
        // else {
        // ReportGenerator.emptyTipsLine();
        // }
        return true;
      }

      @Override protected void initialization(final ASTNode ¢) {
        disabling.scan(¢);
      }
    });
  }

  /** consolidateTips on BodyDeclaration TODO Matteo -- this gonna be removed?
   * -- matteo
   * @param r
   * @param u */
  private void consolidateTips(final ASTRewrite r, @NotNull final BodyDeclaration u) {
    toolbox = Toolbox.defaultInstance();
    u.accept(new DispatchingVisitor() {
      @Override protected <N extends ASTNode> boolean go(@NotNull final N n) {
        TrimmerLog.visitation(n);
        if (disabling.on(n))
          return true;
        Tipper<N> tipper = null;
        try {
          tipper = getTipper(n);
        } catch (@NotNull final Exception ¢) {
          monitor.debug(this, ¢);
        }
        if (tipper == null)
          return true;
        Tip s = null;
        try {
          s = tipper.tip(n, exclude);
          tick(n, tipper);
        } catch (@NotNull final Exception ¢) {
          monitor.debug(this, ¢);
        }
        if (s != null) {
          ++tippersAppliedOnCurrentObject;
          tick2(n, tipper); // save coverage info
          TrimmerLog.application(r, s);
        }
        return true;
      }

      @SuppressWarnings("unused") <N extends ASTNode> void tick(final N n, @NotNull final Tipper<N> w) throws TipperFailure {
        tick(w);
        TrimmerLog.tip(w, n);
      }

      <N extends ASTNode> void tick(@NotNull final Tipper<N> w) {
        final String key = monitor.className(w.getClass());
        if (!spectrum.containsKey(key))
          spectrum.put(key, 0);
        spectrum.put(key, spectrum.get(key) + 1);
      }

      <N extends ASTNode> void tick2(@SuppressWarnings("unused") final N __, @NotNull final Tipper<N> w) {
        @SuppressWarnings("synthetic-access") final String key = presentFileName + "-" + presentMethod + monitor.className(w.getClass());
        if (!coverage.containsKey(key))
          coverage.put(key, 0);
        coverage.put(key, coverage.get(key) + 1);
      }

      @Override protected void initialization(final ASTNode ¢) {
        disabling.scan(¢);
      }
    });
  }

  // @Override <N extends ASTNode> Tipper<N> getTipper(final N ¢) {
  // return toolbox.firstTipper(¢);
  // }
  public boolean apply(@NotNull final WrappedCompilationUnit u, @SuppressWarnings("unused") final AbstractSelection<?> __) {
    apply(u);
    return false;
  }

  /** Apply to single compilation unit
   * @param ¢
   * @return */
  private boolean apply(@NotNull final WrappedCompilationUnit ¢) {
    // System.out.println("*********");
    presentFileName = ¢.getFileName();
    presentFilePath = ¢.getFilePath();
    startingTimePerFile = new Date().getTime();
    go(¢.compilationUnit);
    return false;
  }

  public boolean apply(@NotNull final AbstractSelection<?> __) {
    for (final WrappedCompilationUnit w : ((CommandLineSelection) __).get()) {
      System.out.println("presentFileName: " + presentFileName);
      System.out.println("presentFilePath: " + presentFilePath);
      w.compilationUnit.accept(new ASTVisitor() {
        @Override public boolean preVisit2(@NotNull final ASTNode ¢) {
          return !selectedNodeTypes.contains(¢.getClass()) || go(¢); // ||
                                                                     // !filter(¢)
        }
      });
    }
    return false;
  }
}