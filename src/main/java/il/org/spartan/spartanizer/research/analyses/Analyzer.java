package il.org.spartan.spartanizer.research.analyses;

import java.io.*;
import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.classifier.*;
import il.org.spartan.spartanizer.research.patterns.*;
import il.org.spartan.spartanizer.research.util.*;
import il.org.spartan.spartanizer.utils.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class Analyzer {
  {
    set("outputDir", "/tmp");
  }
  private static InteractiveSpartanizer spartanizer;

  public static void main(final String args[]) {
    AnalyzerOptions.parseArguments(args);
    initializeSpartanizer();
    createOutputDirIfNeeded();
    final String analysis = getProperty("analysis");
    if ("methods".equals(analysis))
      methodsAnalyze();
    else if ("understandability".equals(analysis))
      understandabilityAnalyze();
    else if ("understandability2".equals(analysis))
      understandability2Analyze();
    else if ("statementsToAverageU".equals(analysis))
      SameStatementsAverageUAnalyzer();
    else if ("classify".equals(analysis))
      classify();
    else
      analyze();
  }

  private static void SameStatementsAverageUAnalyzer() {
    methodsAnalyze((new SameStatementsAverageUAnalyzer()));
  }

  private static void initializeSpartanizer() {
    spartanizer = addNanoPatterns(new InteractiveSpartanizer());
  }

  /** run an interactive classifier to classify nanos! */
  private static void classify() {
    String code = "";
    for (final File ¢ : inputFiles())
      code += spartanize(compilationUnit(¢));
    new Classifier().analyze(getCompilationUnit(code));
  }

  private static void createOutputDirIfNeeded() {
    final File dir = new File(getProperty("outputDir"));
    if (!dir.exists())
      dir.mkdir();
  }

  /** @param ¢
   * @return */
  private static String getProperty(final String ¢) {
    return AnalyzerOptions.get(Analyzer.class.getSimpleName(), ¢);
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
    } catch (final IOException x) {
      monitor.infoIOException(x, "append");
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

  /** Get all java files contained in folder recursively. <br>
   * Heuristically, we ignore test files.
   * @param dirName name of directory to search in
   * @return All java files nested inside the folder */
  private static Set<File> getJavaFiles(final String dirName) {
    return getJavaFiles(new File(dirName));
  }

  /** Get all java files contained in folder recursively. <br>
   * Heuristically, we ignore test files.
   * @param directory to search in
   * @return All java files nested inside the folder */
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
      appendFile(new File(getProperty("outputDir") + "/after.java"), spartanize(cu));
    }
    Logger.summarize(getProperty("outputDir"));
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
    new File(getProperty("outputDir") + "/after.java").delete();
  }

  private static void methodsAnalyze() {
    methodsAnalyze((new MagicNumbersAnalysis()));
  }

  private static void methodsAnalyze(final MetricalAnalyzer<?> a) {
    for (final File f : inputFiles())
      for (final AbstractTypeDeclaration t : step.types(az.compilationUnit(compilationUnit(f))))
        if (haz.methods(t))
          for (final MethodDeclaration ¢ : step.methods(t).stream().filter(m -> !m.isConstructor()).collect(Collectors.toList()))
            try {
              a.logMethod(¢, wizard.ast(Wrap.Method.off(spartanizer.fixedPoint(Wrap.Method.on(¢ + "")))));
            } catch (@SuppressWarnings("unused") final AssertionError __) {
              //
            }
    a.printComparison();
    a.printAccumulated();
  }

  private static void understandabilityAnalyze() {
    methodsAnalyze(new UnderstandabilityAnalyzer());
  }

  private static void understandability2Analyze() {
    methodsAnalyze(new Understandability2Analyzer());
  }

  /** Add our wonderful patterns (which are actually just special tippers) to
   * the gUIBatchLaconizer.
   * @param ¢ our gUIBatchLaconizer
   * @return */
  private static InteractiveSpartanizer addNanoPatterns(final InteractiveSpartanizer ¢) {
    if ("false".equals(getProperty("nmethods")))
      addJavadocNanoPatterns(¢);
    return ¢
        .add(ConditionalExpression.class, //
            new DefaultsTo(), //
            new SafeReference(), //
            null) //
        .add(Assignment.class, //
            new AssignmentLazyEvaluation(), //
            null) //
        .add(Block.class, //
            new ReturnOld(), //
            new ReturnAnyMatches(), //
            null) //
        .add(CastExpression.class, //
            new Coercion(), //
            null) //
        .add(EnhancedForStatement.class, //
            new ApplyToEach(), //
            new FindFirst(), //
            null) //
        .add(IfStatement.class, //
            new IfNullThrow(), //
            new IfNullReturn(), //
            new IfNullReturnNull(), //
            new ExecuteWhen(), //
            new PutIfAbsent(), //
            new IfThrow(), //
            null) //
        .add(InstanceofExpression.class, //
            new InstanceOf(), //
            null)//
        .add(MethodDeclaration.class, //
            new SetterGoFluent(), //
            null) //
    ;
  }

  private static InteractiveSpartanizer addJavadocNanoPatterns(final InteractiveSpartanizer ¢) {
    return ¢.add(MethodDeclaration.class, //
        new Carrier(), //
        new Converter(), //
        new Delegator(), //
        new Examiner(), //
        new Exploder(), //
        new Fluenter(), //
        new FluentSetter(), ///
        new Getter(), //
        new Independent(), //
        new JDPattern(), //
        new Mapper(), //
        new MethodEmpty(), //
        new TypeChecker(), //
        null);
  }
}