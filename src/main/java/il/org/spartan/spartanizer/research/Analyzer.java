package il.org.spartan.spartanizer.research;

import java.io.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.patterns.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class Analyzer {
  public static void main(final String args[]) {
    if (args.length != 2)
      System.out.println("Usage: Analyzer <inputFolder> <outputFolder>");
    analyze(args[0], args[1]);
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

  /** @param inputFolder of the project to be analyzed
   * @param outputDir to which the spartanized code file and CSV files will be
   *        placed in */
  private static void analyze(final String inputFolder, final String outputDir) {
    final InteractiveSpartanizer spartanizer = addNanoPatterns(new InteractiveSpartanizer());
    sanityCheck();
    String spartanizedCode = "";
    new File(outputDir + "/after.java").delete();
    for (final File ¢ : getJavaFiles(inputFolder)) {
      final ASTNode cu = clean(getCompilationUnit(¢));
      Logger.logCompilationUnit(cu);
      spartanizedCode = spartanizer.fixedPoint(cu + "");
      appendFile(new File(outputDir + "/after.java"), spartanizedCode);
      Logger.logSpartanizedCompilationUnit(getCompilationUnit(spartanizedCode));
    }
    Logger.summarize(outputDir);
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
            null) //
        .add(MethodDeclaration.class, //
            new MethodEmpty(), //
            new Getter(), //
            new Setter(), //
            new Mapper(), //
            new Exploder(), //
            // new JDPattern(), //
            new Examiner(), //
            new Delegator(), //
            new Carrier(), //
            new Fluenter(), //
            null);
  }

  /** This us just to check that the InteractiveSpartanizer works and that
   * tippers can be added to it. */
  private static void sanityCheck() {
    assert addNanoPatterns(new InteractiveSpartanizer())
        .fixedPoint(clean(makeAST.COMPILATION_UNIT.from("public class A{ Object f(){ return c;} }")) + "").contains("[[Getter]]");
  }
}
