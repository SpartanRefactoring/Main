package il.org.spartan.spartanizer.research.analyses.util;

import java.io.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.research.analyses.*;
import il.org.spartan.spartanizer.research.util.*;
import nano.ly.*;

/** File utils
 * @author Ori Marcovitch
 * @since Dec 14, 2016 */
public enum Files {
  ;
  public static void createOutputDirIfNeeded() {
    final File dir = new File(outputDir());
    if (!dir.exists())
      dir.mkdir();
  }

  @SuppressWarnings("deprecation") public static String getProperty(final String ¢) {
    return AnalyzerOptions.get(Analyze.class.getSimpleName(), ¢);
  }

  @SuppressWarnings("deprecation") public static void set(final String key, final String value) {
    AnalyzerOptions.set(key, value);
  }

  public static String outputDir() {
    return getProperty("outputDir");
  }

  public static Collection<File> inputFiles() {
    return getJavaFiles(getProperty("inputDir"));
  }

  public static void deleteOutputFile() {
    new File(outputDir() + "/after.java").delete();
  }

  public static void blank(final String s) {
    try (PrintWriter p = new PrintWriter(s)) {
      p.close();
    } catch (final FileNotFoundException ¢) {
      note.io(¢);
    }
  }

  public static void blank(final File f) {
    try (PrintWriter p = new PrintWriter(f)) {
      p.close();
    } catch (final FileNotFoundException ¢) {
      note.io(¢);
    }
  }

  /** Append String to file.
   * @param f file
   * @param s string */
  public static void appendFile(final File f, final String s) {
    try (FileWriter w = new FileWriter(f, true)) {
      w.write(s);
    } catch (final IOException ¢) {
      note.io(¢, "append");
    }
  }

  /** Write String to file.
   * @param f file
   * @param s string */
  public static void writeFile(final File f, final String s) {
    try (FileWriter w = new FileWriter(f, false)) {
      w.write(s);
    } catch (final IOException ¢) {
      note.io(¢, "write");
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
  public static ASTNode getCompilationUnit(final String ¢) {
    return makeAST.COMPILATION_UNIT.from(¢);
  }

  /** Get all java files contained in outputFolder recursively. <br>
   * Heuristically, we forget test files.
   * @param dirName name of directory to search in
   * @return All java files nested inside the outputFolder */
  private static Collection<File> getJavaFiles(final String dirName) {
    return getJavaFiles(new File(dirName));
  }

  /** Get all java files contained in outputFolder recursively. <br>
   * Heuristically, we forget test files.
   * @param directory to search in
   * @return All java files nested inside the outputFolder */
  private static Collection<File> getJavaFiles(final File directory) {
    final Collection<File> $ = new HashSet<>();
    if (directory == null || directory.listFiles() == null)
      return $;
    for (final File entry : directory.listFiles())
      if (javaFile(entry) && notTest(entry))
        $.add(entry);
      else
        $.addAll(getJavaFiles(entry));
    return $;
  }

  private static boolean javaFile(final File entry) {
    return entry.isFile() && entry.getPath().endsWith(".java");
  }

  private static boolean notTest(final File entry) {
    return !entry.getPath().contains("src\\test") && !entry.getPath().contains("src/test") && !entry.getName().contains("Test");
  }

  public static ASTNode compilationUnit(final File ¢) {
    return clean(getCompilationUnit(¢));
  }

  public static ASTNode compilationUnitWithBinding(final File ¢) {
    return clean(getCompilationUnitWithBinding(¢));
  }

  private static ASTNode getCompilationUnitWithBinding(final File ¢) {
    return wizard.compilationUnitWithBinding(¢);
  }
}
