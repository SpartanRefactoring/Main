package il.org.spartan.spartanizer.research;

import java.io.*;
import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.engine.*;
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
    parseArguments(args);
    initializeSpartanizer();
    createOutputDirIfNeeded();
    final String analysis = getProperty("analysis");
    if ("methods".equals(analysis))
      methodsAnalyze();
    else if ("understandability".equals(analysis))
      understandabilityAnalyze();
    else if ("classify".equals(analysis))
      classify();
    else
      analyze();
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

  private static void parseArguments(final String[] args) {
    if (args.length < 2)
      assert false : "You need to specify at least inputDir and outputDir!\nUsage: Analyzer -option=<value> -pattern.option2=<value> ...\n";
    for (final String arg : args)
      parseArgument(arg);
    System.out.println(AnalyzerOptions.options);
  }

  /** @param key
   * @param value */
  private static void set(final String key, final String value) {
    AnalyzerOptions.set(key, value);
  }

  private static void parseArgument(final String s) {
    assert s.charAt(0) == '-' : "property should start with '-'";
    final String[] li = bisect(s.substring(1), "=");
    assert li.length == 2 : "property should be of the form -x=y or -x.p=y but was [" + s + "]";
    if (!li[0].contains("."))
      setLocalProperty(li[0], li[1]);
    else
      setExternalProperty(li[0], li[1]);
  }

  /** @param s
   * @param by
   * @return */
  private static String[] bisect(final String s, final String by) {
    final String[] $ = new String[2];
    final int i = s.indexOf(by);
    $[0] = s.substring(0, i);
    $[1] = s.substring(i + 1);
    return $;
  }

  /** @param key
   * @param value */
  private static void setLocalProperty(final String key, final String value) {
    set(key, value);
  }

  /** @param left
   * @param right */
  private static void setExternalProperty(final String left, final String right) {
    setExternalProperty(left.split("\\.")[0], left.split("\\.")[1], right);
  }

  private static void setExternalProperty(final String cls, final String property, final String value) {
    AnalyzerOptions.set(cls, property, value);
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
    MagicNumbersAnalysis analyzer = new MagicNumbersAnalysis();
    for (final File f : inputFiles())
      for (final AbstractTypeDeclaration t : step.types(az.compilationUnit(compilationUnit(f))))
        if (haz.methods(t))
          for (final MethodDeclaration ¢ : step.methods(t).stream().filter(m -> !m.isConstructor()).collect(Collectors.toList()))
            try {
              analyzer.logMethod(¢, wizard.ast(Wrap.Method.off(spartanizer.fixedPoint(Wrap.Method.on(¢ + "")))));
            } catch (@SuppressWarnings("unused") final AssertionError __) {
              //
            }
    analyzer.printComparison();
    analyzer.printAccumulated();
  }

  private static void understandabilityAnalyze() {
    UnderstandabilityAnalyzer analyzer = new UnderstandabilityAnalyzer();
    for (final File f : inputFiles())
      for (final AbstractTypeDeclaration t : step.types(az.compilationUnit(compilationUnit(f))))
        if (haz.methods(t))
          for (final MethodDeclaration ¢ : step.methods(t).stream().filter(m -> !m.isConstructor()).collect(Collectors.toList()))
            try {
              analyzer.logMethod(¢, wizard.ast(Wrap.Method.off(spartanizer.fixedPoint(Wrap.Method.on(¢ + "")))));
            } catch (@SuppressWarnings("unused") final AssertionError __) {
              //
            }
    analyzer.printComparison();
    analyzer.printAccumulated();
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