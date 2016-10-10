package il.org.spartan.spartanizer.research;

import java.io.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.utils.*;
import il.org.spartan.utils.*;

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

  private static void clean(final String inputFolder, final String __) {
    for (final File f : getJavaFiles(inputFolder)) {
      final ASTNode cu = getCompilationUnit(f);
      clean(cu);
      updateFile(f, cu);
    }
  }

  private static void updateFile(final File f, final ASTNode cu) {
    updateFile(f, cu);
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

  static String readFile(final String fileName) {
    try {
      return FileUtils.read(new File(fileName));
    } catch (final IOException e) {
      e.printStackTrace();
    }
    return null;
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

  /**
   *
   */
  private static void report(final ASTNode root) {
    System.out.println("[" + markedNodes(root) + "/" + nodes(root) + "]");
    // TODO: much more then that..
  }

  static int nodes(final ASTNode root) {
    final Int $ = new Int();
    root.accept(new ASTVisitor() {
      @Override public void preVisit(@SuppressWarnings("unused") final ASTNode __) {
        $.inner += 1;
      }
    });
    return $.inner;
  }

  static int markedNodes(final ASTNode root) {
    final Int $ = new Int();
    root.accept(new ASTVisitor() {
      @Override public void preVisit(final ASTNode ¢) {
        if (¢.getProperty(Marker.AST_PROPERTY_NAME_NP_LIST) != null)
          $.inner += 1;
      }
    });
    return $.inner;
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
    final InteractiveSpartanizer is = new InteractiveSpartanizer();
    String spartanizedCode = "";
    for (final File ¢ : getJavaFiles(inputFolder)) {
      System.out.println("Now: " + ¢.getName());
      spartanizedCode = is.fixedPoint(getCompilationUnit(¢) + "");
      appendFile(new File(outputFolder + "/after.java"), spartanizedCode);
    }
  }
}
