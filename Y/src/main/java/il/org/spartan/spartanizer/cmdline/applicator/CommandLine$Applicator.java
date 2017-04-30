package il.org.spartan.spartanizer.cmdline.applicator;

import static java.util.stream.Collectors.*;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;

import fluent.ly.*;
import il.org.spartan.collections.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.navigate.count;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.cmdline.report.*;
import il.org.spartan.spartanizer.cmdline.report.ConfigurableReport.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.plugin.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.traversal.*;

/** Specific applicator
 * @author Matteo Orru'
 * @since 2016 */
public class CommandLine$Applicator extends GenericApplicator {
  final ChainStringToIntegerMap spectrum = new ChainStringToIntegerMap();
  final ChainStringToIntegerMap coverage = new ChainStringToIntegerMap();
  public static String presentFileName;
  public static String presentFilePath;
  public static long startingTimePerFile;
  public static long lastTime;
  public static long startingTime;

  public CommandLine$Applicator() {}

  public CommandLine$Applicator(final String... classes) {
    super(classes);
  }

  public CommandLine$Applicator(final String[] classes, final String... tipperGroups) {
    super(classes, tipperGroups);
  }

  public CommandLine$Applicator(final String[] classes, final String[] tipperGroups, final String... excludedTipperGroups) {
    super(classes, removeExcludedTippers(tipperGroups, excludedTipperGroups));
  }

  public CommandLine$Applicator(final String[] classes, final String[] tipperGroups, final String[] excludedTipperGroups,
      final String... excludedNanoPatterns) {
    // left intentionally empty
    super(classes, removeExcludedNanoPatternsAndTippers(tipperGroups, excludedTipperGroups, excludedNanoPatterns));
  }

  private static String[] removeExcludedNanoPatternsAndTippers(final String[] tipperGroups, final String[] excludedTipperGroups,
      final String... excludedNanoPatterns) {
    return removeExcludedNanoPatterns(removeExcludedTippers(tipperGroups, excludedTipperGroups), excludedNanoPatterns);
  }

  private static String[] removeExcludedNanoPatterns(final String[] tipperGroups, final String... excludedNanoPatterns) {
    return Stream.of(tipperGroups != null ? tipperGroups : setAllTipperGroups().toArray(new String[0]))
        .filter(λ -> !as.list(excludedNanoPatterns).contains(λ)).collect(toList()).toArray(new String[0]);
  }

  private static String[] removeExcludedTippers(final String[] tipperGroups, final String... excludedTipperGroups) {
    return Stream.of(tipperGroups != null ? tipperGroups : setAllTipperGroups().toArray(new String[0]))
        .filter(λ -> !as.list(excludedTipperGroups).contains(λ)).collect(toList()).toArray(new String[0]);
  }

  private void go(final CompilationUnit u) {
    // extract.type(u);
    // ReportGenerator.report("tips").put("FileName", presentFileName);
    // ReportGenerator.report("tips").put("FilePath", presentFilePath);
    // ReportGenerator.report("tips").put("ClassLOCBefore", count.lines(u));
    // ReportGenerator.report("tips").put("ClassTokenBefore", metrics.tokens(u +
    // ""));
    ReportGenerator.Util.initialize();
    u.accept(new ASTVisitor(true) {
      @Override public boolean preVisit2(final ASTNode ¢) {
        assert ¢ != null;
        System.out.println(¢.getClass() + ": " + selectedNodeTypes.contains(¢.getClass()));
        return !selectedNodeTypes.contains(¢.getClass()) || go(¢);
      }
    });
  }

  boolean go(final ASTNode input) {
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

  private void computeMethodMetrics(final ASTNode input, final ASTNode output) {
    System.err.println(++done + " " + extract.category(input) + " " + extract.name(input));
    ReportGenerator.summaryFileName("methods");
    ReportGenerator.name(input);
    ReportGenerator.writeMethodMetrics(input, output, "methods");
    ReportGenerator.nl("methods");
  }

  @SuppressWarnings("boxing") private void computeMetrics(final ASTNode input, final ASTNode output) {
    System.err.println(++done + " " + extract.category(input) + " " + extract.name(input));
    // ReportGenerator.report("tips").put("Name", extract.name(input));
    // ReportGenerator.report("tips").put("Category", extract.category(input));
    ReportGenerator.summaryFileName("metrics");
    ReportGenerator.name(input);
    ReportGenerator.writeMetrics(input, output, "metrics");
    ReportGenerator.write(input, output, "Δ ", (n1, n2) -> (n1 - n2));
    ReportGenerator.write(input, output, "δ ", Utils::d);
    ReportGenerator.writePerc(input, output, "δ ");
    // Reports.writeRatio(input, output, "", (n1,n2)->(n1/n2));
    ReportGenerator.nl("metrics");
  }

  private String fixedPoint(final ASTNode ¢) {
    return fixedPoint(¢ + "");
  }

  private String fixedPoint(final String from) {
    for (final IDocument $ = new Document(from);;) {
      final TextEdit e = createRewrite((CompilationUnit) makeAST.COMPILATION_UNIT.from($.get())).rewriteAST($, null);
      try {
        e.apply($);
      } catch (final MalformedTreeException | IllegalArgumentException | BadLocationException ¢) {
        note.bug(this, ¢);
        throw new AssertionError(¢);
      }
      if (!e.hasChildren())
        return $.get();
    }
  }

  /** createRewrite on CompilationUnit
   * @param ¢
   * @return */
  private ASTRewrite createRewrite(final CompilationUnit ¢) {
    final ASTRewrite $ = ASTRewrite.create(¢.getAST());
    lastTime = new Date().getTime();
    consolidateTips($, ¢);
    ReportGenerator.report("metrics").put("# Tippers", tippersAppliedOnCurrentObject);
    return $;
  }

  /** consolidate tips on CompilationUnit
   * @param r
   * @param u */
  private void consolidateTips(final ASTRewrite r, final CompilationUnit u) {
    configuration = il.org.spartan.spartanizer.traversal.Configurations.all();
    u.accept(new DispatchingVisitor() {
      @Override @SuppressWarnings("boxing") protected <N extends ASTNode> boolean go(final N n) {
        TraversalMonitor.visitation(n);
        if (disabling.on(n))
          return true;
        Tipper<N> tipper = null;
        try {
          tipper = getTipper(n);
        } catch (final Exception ¢) {
          note.ignore(this, ¢);
        }
        if (tipper == null)
          return true;
        Tip s = null;
        try {
          s = tipper.tip(n);
        } catch (final Exception ¢) {
          note.ignore(this, ¢);
        }
        if (s == null)
          return true;
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
        TraversalMonitor.rewrite(r, s);
        return true;
      }

      @Override protected void initialization(final ASTNode ¢) {
        disabling.scan(¢);
      }
    });
  }

  public boolean apply(final WrappedCompilationUnit u, @SuppressWarnings("unused") final AbstractSelection<?> __) {
    apply(u);
    return false;
  }

  /** Apply to single compilation unit
   * @param ¢
   * @return */
  private boolean apply(final WrappedCompilationUnit ¢) {
    presentFileName = ¢.getFileName();
    presentFilePath = ¢.getFilePath();
    startingTimePerFile = new Date().getTime();
    go(¢.compilationUnit);
    return false;
  }

  public boolean apply(final AbstractSelection<?> __) {
    for (final WrappedCompilationUnit w : ((CommandLineSelection) __).get()) {
      System.out.println("presentFileName: " + presentFileName);
      System.out.println("presentFilePath: " + presentFilePath);
      w.compilationUnit.accept(new ASTVisitor(true) {
        @Override public boolean preVisit2(final ASTNode ¢) {
          return !selectedNodeTypes.contains(¢.getClass()) || go(¢); // ||
                                                                     // !filter(¢)
        }
      });
    }
    return false;
  }
}