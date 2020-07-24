package il.org.spartan.spartanizer.research.analyses.analyzers;

import static il.org.spartan.spartanizer.ast.navigate.step.body;
import static il.org.spartan.spartanizer.ast.navigate.step.javadoc;
import static il.org.spartan.spartanizer.ast.navigate.step.methods;
import static il.org.spartan.spartanizer.ast.navigate.step.types;
import static il.org.spartan.spartanizer.research.analyses.util.Files.appendFile;
import static il.org.spartan.spartanizer.research.analyses.util.Files.compilationUnit;
import static il.org.spartan.spartanizer.research.analyses.util.Files.createOutputDirIfNeeded;
import static il.org.spartan.spartanizer.research.analyses.util.Files.deleteOutputFile;
import static il.org.spartan.spartanizer.research.analyses.util.Files.getCompilationUnit;
import static il.org.spartan.spartanizer.research.analyses.util.Files.getProperty;
import static il.org.spartan.spartanizer.research.analyses.util.Files.inputFiles;
import static il.org.spartan.spartanizer.research.analyses.util.Files.outputDir;
import static il.org.spartan.spartanizer.research.analyses.util.Files.set;
import static il.org.spartan.spartanizer.research.analyses.util.Files.writeFile;
import static java.util.stream.Collectors.toList;

import java.io.File;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import fluent.ly.as;
import fluent.ly.forget;
import fluent.ly.safe;
import il.org.spartan.CSVStatistics;
import il.org.spartan.spartanizer.ast.factory.make;
import il.org.spartan.spartanizer.ast.navigate.countOf;
import il.org.spartan.spartanizer.ast.navigate.findFirst;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.cmdline.good.DeprecatedFolderASTVisitor;
import il.org.spartan.spartanizer.cmdline.good.InteractiveSpartanizer;
import il.org.spartan.spartanizer.java.haz;
import il.org.spartan.spartanizer.research.Logger;
import il.org.spartan.spartanizer.research.MethodRecord;
import il.org.spartan.spartanizer.research.analyses.MagicNumbersAnalysis;
import il.org.spartan.spartanizer.research.analyses.Nanonizer;
import il.org.spartan.spartanizer.research.analyses.hIndex;
import il.org.spartan.spartanizer.research.analyses.util.Count;
import il.org.spartan.spartanizer.research.classifier.Classifier;
import il.org.spartan.spartanizer.utils.WrapIntoComilationUnit;
import il.org.spartan.spartanizer.utils.format;

/** Old class for some anlyzes, sort of deprecated since we use
 * {@link DeprecatedFolderASTVisitor}
 * @author Ori Marcovitch */
@Deprecated
@SuppressWarnings("deprecation")
public enum Analyze {
  ;
  static {
    set("outputDir", "/tmp");
  }
  private static InteractiveSpartanizer spartanizer;
  @SuppressWarnings("rawtypes") private static final Map<String, Analyzer> analyses = new HashMap<>() {
    static final long serialVersionUID = 0x124DFEE321504D08L;
    {
      put("AvgIndicatorMetrical", new AvgIndicatorMetricalAnalyzer());
      put("understandability", new UnderstandabilityAnalyzer());
      put("understandability2", new Understandability2Analyzer());
      put("statementsToAverageU", new SameStatementsAverageUAnalyzer());
      put("magic numbers", new MagicNumbersAnalysis());
    }
  };

  public static void summarize(final String outputDir) {
    summarizeMethodStatistics(outputDir);
  }
  public static CSVStatistics openMethodSummaryFile(final String outputDir) {
    return openSummaryFile(outputDir + "/methodStatistics");
  }
  public static CSVStatistics openNPSummaryFile(final String outputDir) {
    return openSummaryFile(outputDir + "/npStatistics.csv");
  }
  public static CSVStatistics openSummaryFile(final String $) {
    return new CSVStatistics($, "property");
  }
  private static void summarizeMethodStatistics(final String outputDir) {
    final CSVStatistics report = openMethodSummaryFile(outputDir);
    if (report == null)
      return;
    double sumSratio = 0, sumEratio = 0;
    for (final Integer k : Logger.methodsStatistics.keySet()) {
      final MethodRecord m = Logger.methodsStatistics.get(k);
      report //
          .put("Name", m.methodClassName + "~" + m.methodName) //
          .put("#Statement", m.numStatements) //
          .put("#NP Statements", m.numNPStatements()) //
          .put("Statement ratio", m.numStatements == 0 ? 1 : Math.min(1, safe.div(m.numNPStatements(), m.numStatements))) //
          .put("#Expressions", m.numExpressions) //
          .put("#NP expressions", m.numNPExpressions()) //
          .put("Expression ratio", m.numExpressions == 0 ? 1 : Math.min(1, safe.div(m.numNPExpressions(), m.numExpressions))) //
          .put("#Parameters", m.numParameters) //
          .put("#NP", m.nps.size()) //
      ;
      report.nl();
      sumSratio += m.numStatements == 0 ? 1 : Math.min(1, safe.div(m.numNPStatements(), m.numStatements));
      sumEratio += m.numExpressions == 0 ? 1 : Math.min(1, safe.div(m.numNPExpressions(), m.numExpressions));
    }
    System.out.println("Total methods: " + Logger.numMethods);
    System.out.println("Average statement ratio: " + safe.div(sumSratio, Logger.numMethods));
    System.out.println("Average Expression ratio: " + safe.div(sumEratio, Logger.numMethods));
    report.close();
  }
  public static void main(final String[] args) {
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
    final List<MethodDeclaration> methods = an.empty.list();
    for (final File f : inputFiles()) {
      final CompilationUnit cu = az.compilationUnit(compilationUnit(f));
      Logger.logCompilationUnit(cu);
      types(cu).stream().filter(haz::methods).forEach(t -> {
        Logger.logType(t);
        for (final MethodDeclaration ¢ : methods(t).stream().filter(λ -> !excludeMethod(λ)).collect(toList()))
          try {
            Count.before(¢);
            final MethodDeclaration after = findFirst.instanceOf(MethodDeclaration.class)
                .in(make.ast(WrapIntoComilationUnit.Method.off(spartanizer.fixedPoint(WrapIntoComilationUnit.Method.on(¢ + "")))));
            Count.after(after);
            methods.add(after);
          } catch (@SuppressWarnings("unused") final AssertionError __) {
            //
          }
        Logger.finishedType();
      });
    }
    methods.sort((x, y) -> countOf.statements(x) < countOf.statements(y) ? -1 : as.bit(countOf.statements(x) > countOf.statements(y)));
    writeFile(new File(outputDir() + "/after.java"), methods.stream().map(λ -> format.code(λ + "")).reduce("", (x, y) -> x + y));
    writeFile(new File(outputDir() + "/notTagged.java"),
        methods.stream().filter(λ -> !(javadoc(λ) + "").contains("[[")).map(λ -> format.code(λ + "")).reduce("", (x, y) -> x + y));
    // Logger.summarizeSortedMethodStatistics(outputDir());
    // Logger.summarizeNPStatistics(outputDir());
    Count.print();
  }
  private static boolean excludeMethod(final MethodDeclaration ¢) {
    return iz.constructor(¢) || body(¢) == null;
  }
  private static void initializeSpartanizer() {
    spartanizer = new Nanonizer();
  }
  /** run an interactive classifier to classify nanos! */
  private static void classify() {
    new Classifier().analyze(getCompilationUnit(inputFiles().stream().map(λ -> spartanize(compilationUnit(λ))).reduce((x, y) -> x + y).get()));
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
      // @author matteo append also before */
      // appendFile(new File(getProperty("outputDir") + "/before.java"), (cu +
      // ""));
      // appendFile(new File(getProperty("outputDir") + "/after.java"),
      // spartanize(cu));
    }
    summarize(outputDir());
  }
  private static String spartanize(final ASTNode cu) {
    return spartanizer.fixedPoint(cu + "");
  }
  private static void methodsAnalyze() {
    inputFiles()//
        .forEach(f -> types(az.compilationUnit(compilationUnit(f))).stream()//
            .filter(haz::methods)
            .forEach(t -> methods(t).stream()//
                .filter(λ -> !λ.isConstructor())//
                .collect(toList())//
                .forEach(¢ -> {
                  try {
                    analyses.values().forEach(λ -> λ.logMethod(¢, findFirst.instanceOf(MethodDeclaration.class)
                        .in(make.ast(WrapIntoComilationUnit.Method.off(spartanizer.fixedPoint(WrapIntoComilationUnit.Method.on(¢ + "")))))));
                  } catch (final AssertionError __) {
                    forget.em(__);
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
