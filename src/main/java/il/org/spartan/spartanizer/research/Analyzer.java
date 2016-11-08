package il.org.spartan.spartanizer.research;

import java.io.*;
import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.patterns.*;
import il.org.spartan.spartanizer.utils.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class Analyzer {
  {
    set("outputDir", "/tmp");
  }

  public static void main(final String args[]) {
    parseArguments(args);
    createOutputDirIfNeeded();
    if (!"true".equals(getProperty("methodsAnalyze")))
      analyze();
    else
      methodsAnalyze();
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

  static final String patternsPackage = Analyzer.class.getPackage().getName() + ".patterns";

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
  /** @param ¢ String
   * @return compilation unit out of file */
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
  /** @param outputDir to which the spartanized code file and CSV files will be
   *        placed in */
  private static void analyze() {
    final InteractiveSpartanizer spartanizer = addJavadocNanoPatterns(addNanoPatterns(new InteractiveSpartanizer()));
    sanityCheck();
    String spartanizedCode = "";
    new File(getProperty("outputDir") + "/after.java").delete();
    for (final File ¢ : getJavaFiles(getProperty("inputDir"))) {
      System.out.println("\nnow: " + ¢.getPath());
      final ASTNode cu = clean(getCompilationUnit(¢));
      spartanizedCode = spartanizer.fixedPoint(cu + "");
      appendFile(new File(getProperty("outputDir") + "/after.java"), spartanizedCode);
      Logger.logSpartanizedCompilationUnit(getCompilationUnit(spartanizedCode));
    }
    Logger.summarize(getProperty("outputDir"));
  }
  private static void methodsAnalyze() {
    final InteractiveSpartanizer spartanizer = addNanoPatterns(new InteractiveSpartanizer());
    for (final File f : getJavaFiles(getProperty("inputDir")))
      for (final AbstractTypeDeclaration t : step.types(az.compilationUnit(clean(getCompilationUnit(f)))))
        if (haz.methods(t))
          for (final MethodDeclaration ¢ : step.methods(t).stream().filter(m -> !m.isConstructor()).collect(Collectors.toList()))
            try {
              MagicNumbers.logMethod(¢, wizard.ast(Wrap.Method.off(spartanizer.fixedPoint(Wrap.Method.on(¢ + "")))));
            } catch (@SuppressWarnings("unused") final AssertionError __) {
              //
            }
    MagicNumbers.printComparison();
    MagicNumbers.printAccumulated();
  }
  /** Add our wonderful patterns (which are actually just special tippers) to
   * the gUIBatchLaconizer.
   * @param ¢ our gUIBatchLaconizer
   * @return */
  private static InteractiveSpartanizer addNanoPatterns(final InteractiveSpartanizer ¢) {
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
            null) //
        .add(CastExpression.class, //
            new Coercion(), //
            null) //
        .add(EnhancedForStatement.class, //
            new ApplyToEach(), //
            null) //
        .add(IfStatement.class, //
            new IfNullThrow(), //
            new IfNullReturn(), //
            new IfNullReturnNull(), //
            new ExecuteWhen(), //
            new PutIfAbsent(), //
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
        new JDPattern(), //
        new Mapper(), //
        new MethodEmpty(), //
        new TypeChecker(), //
        null);
  }
  /** This us just to check that the InteractiveSpartanizer works and that
   * tippers can be added to it. */
  private static void sanityCheck() {
    assert addJavadocNanoPatterns(new InteractiveSpartanizer())
        .fixedPoint(clean(makeAST.COMPILATION_UNIT.from("public class A{ Object f(){ return c;} }")) + "").contains("[[Getter]]");
  }
}
