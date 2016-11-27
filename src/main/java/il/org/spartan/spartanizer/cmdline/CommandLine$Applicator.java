package il.org.spartan.spartanizer.cmdline;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;

import il.org.spartan.*;
import il.org.spartan.collections.*;
import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.cmdline.report.*;
import il.org.spartan.spartanizer.cmdline.report.ConfigurableReport.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.utils.*;

/** Specific applicator
 * @author Matteo Orru'
 * @since 2016 */
public class CommandLine$Applicator extends Generic$Applicator {
  final ChainStringToIntegerMap spectrum = new ChainStringToIntegerMap();
  final ChainStringToIntegerMap coverage = new ChainStringToIntegerMap();
  private String presentFileName;
  private String presentFilePath;
  public static long startingTime;
  public static long lastTime;

  public CommandLine$Applicator() {}

  public CommandLine$Applicator(final String[] clazzes) {
    super(clazzes);
  }

  public CommandLine$Applicator(final String[] clazzes, final String[] tipperGroups) {
    super(clazzes, tipperGroups);
  }

  public CommandLine$Applicator(final String[] clazzes, final String[] tipperGroups, final String[] excludedTipperGroups) {
    super(clazzes, removeExcludedTippers(tipperGroups, excludedTipperGroups));
  }

  public CommandLine$Applicator(final String[] clazzes, final String[] tipperGroups, final String[] excludedTipperGroups,
      final String[] excludedNanoPatterns) {
    // left intentionally empty
    super(clazzes, removeExcludedNanoPatternsAndTippers(tipperGroups, excludedTipperGroups, excludedNanoPatterns));
  }

  private static String[] removeExcludedNanoPatternsAndTippers(final String[] tipperGroups, final String[] excludedTipperGroups,
      final String[] excludedNanoPatterns) {
    return removeExcludedNanoPatterns(removeExcludedTippers(tipperGroups, excludedTipperGroups), excludedNanoPatterns);
  }

  private static String[] removeExcludedNanoPatterns(final String[] tipperGroups, final String[] excludedNanoPatterns) {
    final List<String> temp = new ArrayList<>();
    final String[] tg = tipperGroups != null ? tipperGroups : setAllTipperGroups().toArray(new String[] {});
    for (final String ¢ : tg)
      if (!as.list(excludedNanoPatterns).contains(¢))
        temp.add(¢);
    return temp.toArray(new String[] {});
  }

  private static String[] removeExcludedTippers(final String[] tipperGroups, final String[] excludedTipperGroups) {
    final List<String> temp = new ArrayList<>();
    final String[] tg = tipperGroups != null ? tipperGroups : setAllTipperGroups().toArray(new String[] {});
    for (final String ¢ : tg)
      if (!as.list(excludedTipperGroups).contains(¢))
        temp.add(¢);
    return temp.toArray(new String[] {});
  }

  void go(final CompilationUnit u) {
    u.accept(new ASTVisitor() {
      @Override public boolean preVisit2(final ASTNode ¢) {
        assert ¢ != null;
        return !selectedNodeTypes.contains(¢.getClass()) || go(¢);
      }
    });
  }

  boolean go(final ASTNode input) {
    tippersAppliedOnCurrentObject = 0;
    final String output = fixedPoint(input);
    final ASTNode outputASTNode = makeAST.COMPILATION_UNIT.from(output); // instead
                                                                         // of
                                                                         // CLASS_BODY_DECLARATIONS
    ReportGenerator.report("tips").put("FileName", presentFileName);
    ReportGenerator.report("tips").put("FilePath", presentFilePath);
    ReportGenerator.printFile(input + "", "before");
    ReportGenerator.printFile(output, "after");
    MetricsReport.getSettings();
    // add ASTNode to MetricsReport
    Settings.addInput(input);
    MetricsReport.getSettings();
    Settings.addOutput(outputASTNode);
    computeMetrics(input, outputASTNode);
    return false;
  }

  @SuppressWarnings({ "boxing" }) protected void computeMetrics(final ASTNode input, final ASTNode output) {
    System.err.println(++done + " " + extract.category(input) + " " + extract.name(input));
    ReportGenerator.report("tips").put("Name", extract.name(input));
    ReportGenerator.report("tips").put("Category", extract.category(input));
    ReportGenerator.summaryFileName("metrics");
    ReportGenerator.name(input);
    ReportGenerator.writeMetrics(input, output, null);
    ReportGenerator.write(input, output, "Δ ", (n1, n2) -> (n1 - n2));
    ReportGenerator.write(input, output, "δ ", (n1, n2) -> system.d(n1, n2));
    ReportGenerator.writePerc(input, output, "δ ");
    // Reports.writeRatio(input, output, "", (n1,n2)->(n1/n2));
    ReportGenerator.nl("metrics");
  }

  String fixedPoint(final ASTNode ¢) {
    return fixedPoint(¢ + "");
  }

  public String fixedPoint(final String from) {
    for (final Document $ = new Document(from);;) {
      final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from($.get()); // instead
                                                                                          // of
                                                                                          // CLASS_BODY_DECLARATIONS
      final ASTRewrite r = createRewrite(u);
      final TextEdit e = r.rewriteAST($, null);
      try {
        e.apply($);
      } catch (final MalformedTreeException | IllegalArgumentException | BadLocationException ¢) {
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
  public ASTRewrite createRewrite(final CompilationUnit ¢) {
    final ASTRewrite $ = ASTRewrite.create(¢.getAST());
    lastTime = new Date().getTime();
    consolidateTips($, ¢);
    return $;
  }

  /** createRewrite on BodyDeclaration TODO Matteo -- this gonna be removed? --
   * matteo
   * @param u
   * @return */
  public ASTRewrite createRewrite(final BodyDeclaration u) {
    final ASTRewrite $ = ASTRewrite.create(u.getAST());
    consolidateTips($, u);
    return $;
  }

  /** consolidate tips on CompilationUnit
   * @param r
   * @param u */
  public void consolidateTips(final ASTRewrite r, final CompilationUnit u) {
    toolbox = Toolbox.defaultInstance();
    u.accept(new DispatchingVisitor() {
      @Override protected <N extends ASTNode> boolean go(final N n) {
        TrimmerLog.visitation(n);
        if (disabling.on(n))
          return true;
        Tipper<N> tipper = null;
        try {
          tipper = getTipper(n);
        } catch (final Exception ¢) {
          monitor.debug(this, ¢);
        }
        if (tipper == null)
          return true;
        Tip s = null;
        try {
          s = tipper.tip(n, exclude);
          ReportGenerator.writeTipsLine(n, s, "tips");
        } catch (final Exception ¢) {
          monitor.debug(this, ¢);
        }
        if (s != null) {
          ++tippersAppliedOnCurrentObject;
          // tick2(tipper); // save coverage info
          TrimmerLog.application(r, s);
        }
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
        } catch (final Exception ¢) {
          monitor.debug(this, ¢);
        }
        if (tipper == null)
          return true;
        Tip s = null;
        try {
          s = tipper.tip(n, exclude);
        } catch (final Exception ¢) {
          monitor.debug(this, ¢);
        }
        if (s != null) {
          
          ++tippersAppliedOnCurrentObject;
          // tick2(tipper); // save coverage info
          TrimmerLog.application(r, s);
        }
        return true;
      }

      @Override protected void initialization(final ASTNode ¢) {
        disabling.scan(¢);
      }
    });
  }

  // @Override <N extends ASTNode> Tipper<N> getTipper(final N ¢) {
  // return toolbox.firstTipper(¢);
  // }
  /** @param u
   * @param __
   * @return */
  public boolean apply(final WrappedCompilationUnit u, @SuppressWarnings("unused") final AbstractSelection<?> __) {
    apply(u);
    return false;
  }

  /** Apply to single compilation unit
   * @param ¢
   * @return */
  public boolean apply(final WrappedCompilationUnit ¢) {
    // System.out.println("*********");
    presentFileName = ¢.getFileName();
    presentFilePath = ¢.getFilePath();
    startingTime = new Date().getTime();
    go(¢.compilationUnit);
    return false;
  }

  /** @param __
   * @return */
  public boolean apply(final AbstractSelection<?> __) {
    for (final WrappedCompilationUnit w : ((CommandLineSelection) __).get()){
      System.out.println("presentFileName: " + presentFileName);
      System.out.println("presentFilePath: " + presentFilePath);
      w.compilationUnit.accept(new ASTVisitor() {
        @Override public boolean preVisit2(final ASTNode ¢) {
          return !selectedNodeTypes.contains(¢.getClass()) || go(¢); // ||
                                                                     // !filter(¢)
        }
      });
    }
    return false;
  }
}