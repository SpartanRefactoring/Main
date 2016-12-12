package il.org.spartan.spartanizer.research.analyses;

import java.io.*;
import java.text.*;
import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.classifier.*;
import il.org.spartan.spartanizer.research.patterns.*;
import il.org.spartan.spartanizer.research.patterns.characteristics.*;
import il.org.spartan.spartanizer.research.patterns.methods.*;
import il.org.spartan.spartanizer.research.util.*;
import il.org.spartan.spartanizer.utils.*;
import il.org.spartan.spartanizer.utils.tdd.*;
import il.org.spartan.utils.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class Analyze {
  {
    set("outputDir", "/tmp");
  }
  private static InteractiveSpartanizer spartanizer;
  @SuppressWarnings("rawtypes") private static Map<String, Analyzer> analyses = new HashMap<String, Analyzer>() {
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
      case "methods":
        methodsAnalyze();
        break;
      case "classify":
        classify();
        break;
      case "sort":
        spartanizeMethodsAndSort();
        break;
      default:
        analyze();
    }
    System.out.println("Took " + new DecimalFormat("#0.00").format((System.currentTimeMillis() - startTime) / 1000.0) + "s");
  }

  static class Count {
    static final Pair<Int, Int> ifStatements = new Pair<>(new Int(), new Int());
    static final Pair<Int, Int> loopsStatements = new Pair<>(new Int(), new Int());
    static final Pair<Int, Int> statements = new Pair<>(new Int(), new Int());

    public static void before(final ASTNode ¢) {
      ifStatements.first.inner += enumerate.ifStatements(¢);
      loopsStatements.first.inner += enumerate.loops(¢);
      statements.first.inner += enumerate.statements(¢);
    }

    public static void after(final ASTNode ¢) {
      ifStatements.second.inner += enumerate.ifStatements(¢);
      loopsStatements.second.inner += enumerate.loops(¢);
      statements.second.inner += enumerate.statements(¢);
    }

    /** @return */
    public static void print() {
      System.out.println("statements : " + statements.first.inner + " ---> " + statements.second.inner);
      System.out.println("loops : " + loopsStatements.first.inner + " ---> " + loopsStatements.second.inner);
      System.out.println("ifStatements : " + ifStatements.first.inner + " ---> " + ifStatements.second.inner);
    }
  }

  /** THE analysis */
  private static void spartanizeMethodsAndSort() {
    final List<MethodDeclaration> methods = new ArrayList<>();
    for (final File f : inputFiles()) {
      final CompilationUnit cu = az.compilationUnit(compilationUnit(f));
      Logger.logCompilationUnit(cu);
      step.types(cu).stream().filter(haz::methods).forEach(t -> {
        Logger.logType(t);
        for (final MethodDeclaration ¢ : step.methods(t).stream().filter(m -> !m.isConstructor()).collect(Collectors.toList()))
          try {
            // System.out.println(¢);
            Count.before(¢);
            MethodDeclaration after = findFirst.methodDeclaration(wizard.ast(Wrap.Method.off(spartanizer.fixedPoint(Wrap.Method.on(¢ + "")))));
            Count.after(after);
            methods.add(after);
          } catch (@SuppressWarnings("unused") final AssertionError __) {
            //
          }
        Logger.finishedType();
      });
    }
    methods.sort((x, y) -> count.statements(x) < count.statements(y) ? -1 : count.statements(x) > count.statements(y) ? 1 : 0);
    writeFile(new File(outputDir() + "/after.java"), methods.stream().map(x -> x + "").reduce("", (x, y) -> x + y));
    Logger.summarizeSortedMethodStatistics(outputDir());
    Count.print();
  }

  private static String outputDir() {
    return getProperty("outputDir");
  }

  private static void initializeSpartanizer() {
    spartanizer = addNanoPatterns(new InteractiveSpartanizer());
  }

  public static Toolbox toolboxWithNanoPatterns() {
    return addNanoPatterns(new InteractiveSpartanizer()).toolbox;
  }

  /** run an interactive classifier to classify nanos! */
  private static void classify() {
    String code = "";
    for (final File ¢ : inputFiles())
      code += spartanize(compilationUnit(¢));
    new Classifier().analyze(getCompilationUnit(code));
  }

  private static void createOutputDirIfNeeded() {
    final File dir = new File(outputDir());
    if (!dir.exists())
      dir.mkdir();
  }

  /** @param ¢
   * @return */
  private static String getProperty(final String ¢) {
    return AnalyzerOptions.get(Analyze.class.getSimpleName(), ¢);
  }

  /** @param key
   * @param value */
  private static void set(final String key, final String value) {
    AnalyzerOptions.set(key, value);
  }

  /** Append String to file.
   * @param f file
   * @param s string */
  private static void appendFile(final File f, final String s) {
    try (FileWriter w = new FileWriter(f, true)) {
      w.write(s);
    } catch (final IOException ¢) {
      monitor.infoIOException(¢, "append");
    }
  }

  /** Write String to file.
   * @param f file
   * @param s string */
  private static void writeFile(final File f, final String s) {
    try (FileWriter w = new FileWriter(f, false)) {
      w.write(s);
    } catch (final IOException ¢) {
      monitor.infoIOException(¢, "write");
    }
  }

  /** Clean {@link cu} from any comments, javadoc, importDeclarations,
   * packageDeclarations and FieldDeclarations.
   * @param cu
   * @return */
  private static ASTNode clean(final ASTNode cu) {
    cu.accept(new CleanerVisitor());
    return cu;
  }

  /** @param ¢ file
   * @return compilation unit out of file */
  private static ASTNode getCompilationUnit(final File ¢) {
    return makeAST.COMPILATION_UNIT.from(¢);
  }

  /** @param ¢ string
   * @return compilation unit out of string */
  private static ASTNode getCompilationUnit(final String ¢) {
    return makeAST.COMPILATION_UNIT.from(¢);
  }

  /** Get all java files contained in outputFolder recursively. <br>
   * Heuristically, we ignore test files.
   * @param dirName name of directory to search in
   * @return All java files nested inside the outputFolder */
  private static Set<File> getJavaFiles(final String dirName) {
    return getJavaFiles(new File(dirName));
  }

  /** Get all java files contained in outputFolder recursively. <br>
   * Heuristically, we ignore test files.
   * @param directory to search in
   * @return All java files nested inside the outputFolder */
  private static Set<File> getJavaFiles(final File directory) {
    final Set<File> $ = new HashSet<>();
    if (directory == null || directory.listFiles() == null)
      return $;
    for (final File entry : directory.listFiles())
      if (entry.isFile() && entry.getName().endsWith(".java") && !entry.getPath().contains("src/test") && !entry.getName().contains("Test"))
        $.add(entry);
      else
        $.addAll(getJavaFiles(entry));
    return $;
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
    }
    Logger.summarize(outputDir());
  }

  private static String spartanize(final ASTNode cu) {
    return spartanizer.fixedPoint(cu + "");
  }

  private static ASTNode compilationUnit(final File ¢) {
    return clean(getCompilationUnit(¢));
  }

  private static Set<File> inputFiles() {
    return getJavaFiles(getProperty("inputDir"));
  }

  private static void deleteOutputFile() {
    new File(outputDir() + "/after.java").delete();
  }

  @SuppressWarnings("rawtypes") private static void methodsAnalyze() {
    for (final File f : inputFiles())
      //
      step.types(az.compilationUnit(compilationUnit(f))).stream().filter(haz::methods).forEach(t -> {
        for (final MethodDeclaration ¢ : step.methods(t).stream().filter(m -> !m.isConstructor()).collect(Collectors.toList()))
          try {
            for (final Analyzer a : analyses.values())
              a.logMethod(¢, findFirst.methodDeclaration(wizard.ast(Wrap.Method.off(spartanizer.fixedPoint(Wrap.Method.on(¢ + ""))))));
          } catch (@SuppressWarnings("unused") final AssertionError __) {
            //
          }
      });
    for (final String a : analyses.keySet()) {
      System.out.println("++++++++" + a + "++++++++");
      analyses.get(a).printComparison();
      analyses.get(a).printAccumulated();
    }
  }

  /** Add our wonderful patterns (which are actually just special tippers) to
   * the gUIBatchLaconizer.
   * @param ¢ our gUIBatchLaconizer
   * @return */
  private static InteractiveSpartanizer addNanoPatterns(final InteractiveSpartanizer ¢) {
    if ("false".equals(getProperty("nmethods")))
      addCharacteristicMethodPatterns(¢);
    return addMethodPatterns(¢)
        .add(ConditionalExpression.class, //
            new DefaultsTo(), //
            new SafeReference(), //
            null) //
        .add(Assignment.class, //
            new AssignmentLazyEvaluation(), //
            null) //
        .add(Block.class, //
            new CreateFrom(), //
            new FindFirstBlock(), //
            new ReturnOld(), //
            new ReturnAllMatches(), //
            new ReturnAnyMatches(), //
            null) //
        .add(EnhancedForStatement.class, //
            new Aggregate(), //
            new ContainsEnhancedFor(), //
            new ForEach(), //
            // new ReduceEnhancedFor(), //
            null) //
        // .add(ForStatement.class, //
        // new Contains(), //
        // new CopyArray(), //
        // new FindFirst(), //
        // new ForEachEnhanced(), //
        // new InitArray(), //
        // new MaxEnhanced(), //
        // new Min(), //
        // new Reduce(), //
        // null) //
        .add(IfStatement.class, //
            new IfNullThrow(), //
            new IfNullReturn(), //
            new IfNullReturnNull(), //
            new ExecuteWhen(), //
            new PutIfAbsent(), //
            new IfThrow(), //
            null) //
    ;
  }

  private static InteractiveSpartanizer addMethodPatterns(final InteractiveSpartanizer ¢) {
    return ¢.add(MethodDeclaration.class, //
        new ConstantReturner(), //
        new Creator(), //
        new DefaultParametersAdder(), //
        new Delegator(), //
        new DoNothingReturnParam(), //
        new DoNothingReturnThis(), //
        new DownCaster(), //
        new Examiner(), //
        new FluentSetter(), ///
        new Getter(), //
        new ForEachApplier(), //
        new Setter(), //
        new SuperDelegator(), //
        new Thrower(), //
        new TypeChecker(), //
        new UpCaster(), //
        null);
  }

  private static InteractiveSpartanizer addCharacteristicMethodPatterns(final InteractiveSpartanizer ¢) {
    return ¢.add(MethodDeclaration.class, //
        new Fluenter(), //
        new Independent(), //
        new JDPattern(), //
        new MethodEmpty(), //
        new UseParameterAndReturnIt(), //
        null);
  }
}