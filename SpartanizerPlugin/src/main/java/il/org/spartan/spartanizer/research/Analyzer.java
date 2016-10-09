package il.org.spartan.spartanizer.research;

import java.io.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.utils.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class Analyzer {
  public static void main(final String args[]) {
    final String folderName = args[1];
    switch (args[0]) {
      case "-clean":
        clean(folderName);
        break;
      case "-analyze":
        analyze(folderName);
        break;
      case "-spartanize":
        spartanize(folderName);
        break;
      case "-full":
      default:
        spartanize(folderName);
        clean(folderName);
        analyze(folderName);
    }
  }

  private static void clean(final String folderName) {
    for (final File f : getJavaFiles(folderName)) {
      final ASTNode cu = getCompilationUnit(f);
      clean(cu);
      updateFile(f, cu);
    }
  }

  private static void updateFile(final File f, final ASTNode cu) {
    try (final PrintWriter writer = new PrintWriter(f.getAbsolutePath())) {
      writer.print(cu);
      writer.close();
    } catch (final FileNotFoundException e) {
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
      if (entry.isFile() && entry.getName().endsWith(".java"))
        $.add(entry);
      else
        $.addAll(getJavaFiles(entry));
    return $;
  }

  /** @param f */
  private static void analyze(final ASTNode cu) {
    markAllNP(cu);
    report();
  }

  /**
   *
   */
  private static void report() {
    // TODO output statistics
  }

  /**
   *
   */
  private static void markAllNP(final ASTNode ¢) {
    ¢.accept(new NPMarkerVisitor());
  }

  /** @param folderName */
  private static ASTNode spartanize(final String folderName) {
    return null;
    // TODO call some spartanizer
  }
}
