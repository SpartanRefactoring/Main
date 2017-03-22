package il.org.spartan.spartanizer.research.analyses.util;

import java.io.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.research.analyses.*;
import il.org.spartan.spartanizer.research.util.*;
import il.org.spartan.utils.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** File utils
 * @author Ori Marcovitch
 * @since Dec 14, 2016 */
public enum Files {
  ;
  public static void createOutputDirIfNeeded() {
    @NotNull final File dir = new File(outputDir());
    if (!dir.exists())
      dir.mkdir();
  }

  @Nullable @SuppressWarnings("deprecation") public static String getProperty(final String ¢) {
    return AnalyzerOptions.get(Analyze.class.getSimpleName(), ¢);
  }

  @SuppressWarnings("deprecation") public static void set(final String key, final String value) {
    AnalyzerOptions.set(key, value);
  }

  @Nullable public static String outputDir() {
    return getProperty("outputDir");
  }

  @NotNull public static Collection<File> inputFiles() {
    return getJavaFiles(getProperty("inputDir"));
  }

  public static void deleteOutputFile() {
    new File(outputDir() + "/after.java").delete();
  }

  public static void blank(@NotNull final String s) {
    try (@NotNull PrintWriter p = new PrintWriter(s)) {
      p.close();
    } catch (@NotNull final FileNotFoundException ¢) {
      ¢.printStackTrace();
    }
  }

  public static void blank(@NotNull final File f) {
    try (@NotNull PrintWriter p = new PrintWriter(f)) {
      p.close();
    } catch (@NotNull final FileNotFoundException ¢) {
      ¢.printStackTrace();
    }
  }

  /** Append String to file.
   * @param f file
   * @param s string */
  public static void appendFile(@NotNull final File f, @NotNull final String s) {
    try (@NotNull FileWriter w = new FileWriter(f, true)) {
      w.write(s);
    } catch (@NotNull final IOException ¢) {
      monitor.infoIOException(¢, "append");
    }
  }

  /** Write String to file.
   * @param f file
   * @param s string */
  public static void writeFile(@NotNull final File f, @NotNull final String s) {
    try (@NotNull FileWriter w = new FileWriter(f, false)) {
      w.write(s);
    } catch (@NotNull final IOException ¢) {
      monitor.infoIOException(¢, "write");
    }
  }

  /** Clean {@link cu} from any comments, javadoc, importDeclarations,
   * packageDeclarations and FieldDeclarations.
   * @param cu
   * @return */
  @NotNull private static ASTNode clean(@NotNull final ASTNode cu) {
    cu.accept(new CleanerVisitor());
    return cu;
  }

  /** @param ¢ file
   * @return compilation unit out of file */
  private static ASTNode getCompilationUnit(@NotNull final File ¢) {
    return makeAST.COMPILATION_UNIT.from(¢);
  }

  /** @param ¢ string
   * @return compilation unit out of string */
  public static ASTNode getCompilationUnit(@NotNull final String ¢) {
    return makeAST.COMPILATION_UNIT.from(¢);
  }

  /** Get all java files contained in outputFolder recursively. <br>
   * Heuristically, we ignore test files.
   * @param dirName name of directory to search in
   * @return All java files nested inside the outputFolder */
  @NotNull private static Collection<File> getJavaFiles(@NotNull final String dirName) {
    return getJavaFiles(new File(dirName));
  }

  /** Get all java files contained in outputFolder recursively. <br>
   * Heuristically, we ignore test files.
   * @param directory to search in
   * @return All java files nested inside the outputFolder */
  @NotNull private static Collection<File> getJavaFiles(@Nullable final File directory) {
    @NotNull final Collection<File> $ = new HashSet<>();
    if (directory == null || directory.listFiles() == null)
      return $;
    for (@NotNull final File entry : directory.listFiles())
      if (javaFile(entry) && notTest(entry))
        $.add(entry);
      else
        $.addAll(getJavaFiles(entry));
    return $;
  }

  private static boolean javaFile(@NotNull final File entry) {
    return entry.isFile() && entry.getPath().endsWith(".java");
  }

  private static boolean notTest(@NotNull final File entry) {
    return !entry.getPath().contains("src\\test") && !entry.getPath().contains("src/test") && !entry.getName().contains("Test");
  }

  @NotNull public static ASTNode compilationUnit(@NotNull final File ¢) {
    return clean(getCompilationUnit(¢));
  }

  @NotNull public static ASTNode compilationUnitWithBinding(@NotNull final File ¢) {
    return clean(getCompilationUnitWithBinding(¢));
  }

  @NotNull private static ASTNode getCompilationUnitWithBinding(@NotNull final File ¢) {
    return wizard.compilationUnitWithBinding(¢);
  }
}
