package il.org.spartan.spartanizer.research;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.patterns.*;
import il.org.spartan.spartanizer.utils.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class Analyzer {
  public static void main(final String args[]) {
    if (args.length != 3)
      System.out.println("Usage: Analyzer <operation> <inputFolder> <outputFolder>");
    final String inputFolder = args[1];
    switch (args[0]) {
      case "-clean":
        clean(inputFolder, args[2]);
        break;
      case "-analyze":
        analyze(inputFolder);
        break;
      case "-spartanize":
        spartanize(inputFolder, args[2]);
        break;
      case "-full":
      default:
        spartanize(inputFolder, args[2]);
        clean(inputFolder, args[2]);
        analyze(inputFolder);
    }
  }

  /** Remove all comments from all files in directory @param outputFolder */
  private static void clean(final String inputFolder, final String outputFolder) {
    for (final File f : getJavaFiles(inputFolder)) {
      final ASTNode cu = getCompilationUnit(f);
      clean(cu);
      updateFile(f, cu);
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

  private static void clean(final ASTNode cu) {
    cu.accept(new CommentsCleanerVisitor());
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
    markAllNP(¢);
    report(¢);
  }

  private static void report(final ASTNode root) {
    System.out.println("[" + markedNodes(root) + "/" + nodes(root) + "]");
    // TODO Marco: much more than that..
  }

  private static int nodes(final ASTNode root) {
    final AtomicInteger $ = new AtomicInteger();
    root.accept(new ASTVisitor() {
      @Override public void preVisit(@SuppressWarnings("unused") final ASTNode __) {
        $.incrementAndGet();
      }
    });
    return $.get();
  }

  private static int markedNodes(final ASTNode root) {
    final AtomicInteger $ = new AtomicInteger();
    root.accept(new ASTVisitor() {
      @Override public void preVisit(final ASTNode ¢) {
        if (¢.getProperty(Marker.AST_PROPERTY_NAME_NP_LIST) != null)
          $.incrementAndGet();
      }
    });
    return $.get();
  }

  /**
   *
   */
  private static void markAllNP(final ASTNode ¢) {
    ¢.accept(new NPMarkerVisitor());
  }

  /** @param inputFolder
   * @param outputFolder */
  private static void spartanize(final String inputFolder, final String outputFolder) {
    final InteractiveSpartanizer spartanizer = new InteractiveSpartanizer();
    addNanoPatterns(spartanizer);
    String spartanizedCode = "";
    for (final File ¢ : getJavaFiles(inputFolder)) {
      System.out.println("Now: " + ¢.getName());
      spartanizedCode = spartanizer.fixedPoint(getCompilationUnit(¢) + "");
      appendFile(new File(outputFolder + "/after.java"), spartanizedCode);
    }
  }

  private static void addNanoPatterns(final InteractiveSpartanizer ¢) {
    ¢.toolbox
        .add(ConditionalExpression.class, //
            new TernaryNullCoallescing(), //
            new TernaryNullConditional(), //
            null) //
        .add(Assignment.class, new AssignmentLazyEvaluation(), //
            null) //
        .add(MethodDeclaration.class, //
            new MethodEmpty(), //
            null);
  }
}
