package il.org.spartan.spartanizer.research;

import java.io.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.patterns.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class Analyzer {
  public static void main(final String args[]) {
    if (args.length != 3)
      System.out.println("Usage: Analyzer <operation> <inputFolder> <outputFolder>");
    switch (args[0]) {
      case "-analyze":
        analyze(args[1]);
        break;
      case "-spartanize":
        spartanize(args[1], args[2]);
        break;
      case "-full":
      default:
        spartanize(args[1], args[2]);
        analyze(args[1]);
    }
  }

  private static void updateFile(final File f, final ASTNode cu) {
    updateFile(f, cu + "");
  }

  private static void updateFile(final File f, final String s) {
    try (final PrintWriter writer = new PrintWriter(f.getAbsolutePath())) {
      writer.print(s);
      writer.close();
    } catch (final FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  private static void appendFile(final File f, final String s) {
    try (FileWriter fw = new FileWriter(f, true)) {
      fw.write(s);
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }

  private static ASTNode clean(final ASTNode cu) {
    cu.accept(new CleanerVisitor());
    return cu;
  }

  private static ASTNode getCompilationUnit(final File ¢) {
    return makeAST.COMPILATION_UNIT.from(¢);
  }

  public static void analyze(final String folderName) {
    for (final File f : getJavaFiles(folderName)) {
      final ASTNode cu = getCompilationUnit(f);
      analyze(cu);
      updateFile(f, cu);
    }
  }

  private static Set<File> getJavaFiles(final String folderName) {
    return getJavaFiles(new File(folderName));
  }

  /** @param folderName
   * @return */
  private static Set<File> getJavaFiles(final File dir) {
    final Set<File> $ = new HashSet<>();
    if (dir == null || dir.listFiles() == null)
      return $;
    for (final File entry : dir.listFiles())
      if (entry.isFile() && entry.getName().endsWith(".java") && !entry.getPath().contains("src/test") && !entry.getName().contains("Test"))
        $.add(entry);
      else
        $.addAll(getJavaFiles(entry));
    return $;
  }

  private static void analyze(final ASTNode ¢) {
    report(¢);
  }

  private static void report(final ASTNode root) {
    root.accept(new ReporterVisitor());
  }

  /** @param inputFolder
   * @param outputDir */
  private static void spartanize(final String inputFolder, final String outputDir) {
    final InteractiveSpartanizer spartanizer = new InteractiveSpartanizer();
    addNanoPatterns(spartanizer);
    sanityCheck();
    String spartanizedCode = "";
    new File(outputDir + "/after.java").delete();
    for (final File ¢ : getJavaFiles(inputFolder)) {
      final ASTNode cu = clean(getCompilationUnit(¢));
      Logger.logCompilationUnit(cu);
      spartanizedCode = spartanizer.fixedPoint(cu + "");
      appendFile(new File(outputDir + "/after.java"), spartanizedCode);
    }
    Logger.summarize(outputDir);
  }

  private static void addNanoPatterns(final InteractiveSpartanizer ¢) {
    ¢.toolbox
        .add(ConditionalExpression.class, //
            new DefaultsTo(), //
            new TernaryNullConditional(), //
            null) //
        .add(Assignment.class, //
            new AssignmentLazyEvaluation(), //
            null) //
        .add(CastExpression.class, //
            new Coercion(), //
            null) //
        .add(IfStatement.class, //
            new IfNullThrow(), //
            null) //
        .add(MethodDeclaration.class, //
            new MethodEmpty(), //
            new Getter(), //
            new Setter(), //
            new Mapper(), //
            new Exploder(), //
            new JDPattern(), //
            null);
  }

  private static void sanityCheck() {
    final InteractiveSpartanizer spartanizer = new InteractiveSpartanizer();
    addNanoPatterns(spartanizer);
    assert spartanizer.fixedPoint(clean(makeAST.COMPILATION_UNIT.from("public class A{ Object f(){ return c;} }")) + "").contains("[[Getter]]");
  }
}
