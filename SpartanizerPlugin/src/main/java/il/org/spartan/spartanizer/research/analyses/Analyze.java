package il.org.spartan.spartanizer.research.analyses;

import static il.org.spartan.spartanizer.research.analyses.util.Files.*;

import java.io.*;
import java.text.*;
import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.analyses.util.*;
import il.org.spartan.spartanizer.research.classifier.*;
import il.org.spartan.spartanizer.research.util.*;
import il.org.spartan.spartanizer.utils.*;
import il.org.spartan.utils.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class Analyze {
  static {
    set("outputDir", "/tmp");
  }
  private static InteractiveSpartanizer spartanizer;
  @SuppressWarnings("rawtypes") private static final Map<String, Analyzer> analyses = new HashMap<String, Analyzer>() {
    static final long serialVersionUID = 1L;
    {
      put("AvgIndicatorMetrical", new AvgIndicatorMetricalAnalyzer());
      put("understandability", new UnderstandabilityAnalyzer());
      put("understandability2", new Understandability2Analyzer());
      put("statementsToAverageU", new SameStatementsAverageUAnalyzer());
      put("magic numbers", new MagicNumbersAnalysis());
    }
  };

  public static void main(final String args[]) {
    AnalyzerOptions.parseArguments(args);
    initializeSpartanizer();
    createOutputDirIfNeeded();
    final long startTime = System.currentTimeMillis();
    switch (getProperty("analysis")) {
      case "classify":
        classify();
        break;
      case "methods":
        methodsAnalyze();
        break;
      case "sort":
        spartanizeMethodsAndSort();
        break;
      case "hindex":
        hIndex.analyze();
        break;
      default:
        analyze();
        break;
    }
    System.out.println("Took " + new DecimalFormat("#0.00").format((System.currentTimeMillis() - startTime) / 1000.0) + "s");
  }

  /** THE analysis */
  private static void spartanizeMethodsAndSort() {
    final List<MethodDeclaration> methods = new ArrayList<>();
    for (final File f : inputFiles()) {
      final CompilationUnit cu = az.compilationUnit(compilationUnit(f));
      Logger.logCompilationUnit(cu);
      types(cu).stream().filter(haz::methods).forEach(t -> {
        Logger.logType(t);
        for (final MethodDeclaration ¢ : methods(t).stream().filter(m -> !excludeMethod(m)).collect(Collectors.toList()))
          try {
            Count.before(¢);
            final MethodDeclaration after = findFirst.methodDeclaration(wizard.ast(Wrap.Method.off(spartanizer.fixedPoint(Wrap.Method.on(¢ + "")))));
            Count.after(after);
            methods.add(after);
          } catch (@SuppressWarnings("unused") final AssertionError __) {
            //
          }
        Logger.finishedType();
      });
    }
    methods.sort((x, y) -> count.statements(x) < count.statements(y) ? -1 : count.statements(x) > count.statements(y) ? 1 : 0);
    writeFile(new File(outputDir() + "/after.java"), methods.stream().map(x -> format.code(x + "")).reduce("", (x, y) -> x + y));
    writeFile(new File(outputDir() + "/notTagged.java"),
        methods.stream().filter(m -> !(javadoc(m) + "").contains("[[")).map(x -> format.code(x + "")).reduce("", (x, y) -> x + y));
    // Logger.summarizeSortedMethodStatistics(outputDir());
    // Logger.summarizeNPStatistics(outputDir());
    Count.print();
  }

  private static boolean excludeMethod(final MethodDeclaration ¢) {
    return iz.constructor(¢) || body(¢) == null;
  }

  private static void initializeSpartanizer() {
    spartanizer = new SpartAnalyzer();
  }

  public static Toolbox toolboxWithNanoPatterns() {
    return new SpartAnalyzer().toolbox;
  }

  /** run an interactive classifier to classify nanos! */
  private static void classify() {
    String code = "";
    for (final File ¢ : inputFiles())
      code += spartanize(compilationUnit(¢));
    new Classifier().analyze(getCompilationUnit(code));
  }

  /** analyze nano patterns in code. */
  private static void analyze() {
    AnalyzerOptions.setVerbose();
    deleteOutputFile();
    for (final File ¢ : inputFiles()) {
      System.out.println("\nnow: " + ¢.getPath());
      final ASTNode cu = compilationUnit(¢);
      Logger.logCompilationUnit(az.compilationUnit(cu));
      Logger.logFile(¢.getName());
      try {
        appendFile(new File(outputDir() + "/after.java"), spartanize(cu));
      } catch (@SuppressWarnings("unused") final AssertionError __) {
        //
      }
      /** @author matteo append also before */
      // appendFile(new File(getProperty("outputDir") + "/before.java"), (cu +
      // ""));
      // appendFile(new File(getProperty("outputDir") + "/after.java"),
      // spartanize(cu));
    }
    Logger.summarize(outputDir());
  }

  private static String spartanize(final ASTNode cu) {
    return spartanizer.fixedPoint(cu + "");
  }

  private static void methodsAnalyze() {
    (inputFiles()).forEach(f -> types(az.compilationUnit(compilationUnit(f))).stream().filter(haz::methods)
        .forEach(t -> (methods(t).stream().filter(m -> !m.isConstructor()).collect(Collectors.toList())).forEach(¢ -> {
          try {
            (analyses.values()).forEach(
                a -> a.logMethod(¢, findFirst.methodDeclaration(wizard.ast(Wrap.Method.off(spartanizer.fixedPoint(Wrap.Method.on(¢ + "")))))));
          } catch (final AssertionError __) {
            ___.unused(__);
            //
          }
        })));
    for (final String a : analyses.keySet()) {
      System.out.println("++++++++" + a + "++++++++");
      analyses.get(a).printComparison();
      analyses.get(a).printAccumulated();
    }
  }
}