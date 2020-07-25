package il.org.spartan.spartanizer.cmdline.library;

import java.io.File;
import java.io.IOException;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import fluent.ly.box;
import fluent.ly.note;
import il.org.spartan.spartanizer.ast.factory.makeAST;
import il.org.spartan.spartanizer.ast.navigate.extract;
import il.org.spartan.spartanizer.cmdline.ASTTrotter;
import il.org.spartan.utils.Bool;
import il.org.spartan.utils.FileUtils;
import il.org.spartan.utils.¢;
import junit.framework.Test;

/** TODO Yossi Gil: document class
 * @author Yossi Gil
 * @since 2017-03-19 */
public interface FileHeuristics {
  static double d(final double n1, final double n2) {
    return 1 - n2 / n1;
  }
  static int digits(final double ¢) {
    if (¢ == 0)
      return -1;
    final double $ = Math.log10(¢);
    return $ < 0 ? 0 : (int) $ + 1;
  }
  /** [[SuppressWarningsSpartan]] - see #1246 */
  static String format2(final double ¢) {
    if (¢ < 0)
      return "-" + format2(-¢);
    final double $ = 100 * ¢;
    return "%" + ($ < 0.01 ? ".0f" : $ < 0.1 ? ".2f" : $ < 1 || $ < 10 ? ".1f" : $ < 100 || $ < 1000 ? ".0f" : "5.0g");
  }
  static String format3(final double ¢) {
    if (¢ == 0 || ¢ >= 1 && ¢ - (int) ¢ < 0.0005)
      return "%.0f";
    switch (digits(FileHeuristics.round3(¢))) {
      case 1:
        return "%.2f";
      case 2:
        return "%.1f";
      case -1:
      case 0:
        return "%.3f";
      default:
        return "%.0f";
    }
  }
  static boolean isProductionCode(final File ¢) {
    return !FileHeuristics.isTestSourceFile(¢.getName());
  }
  static boolean isTestFile(final File ¢) {
    return FileHeuristics.isTestSourceFile(¢.getName());
  }
  static boolean isTestSourceFile(final String fileName) {
    return fileName.contains("/test/") || fileName.matches("[\\/A-Za-z0-9]*[\\/]test[\\/A-Za-z0-9]*")
        || fileName.matches("[A-Za-z0-9_-]*[Tt]est[A-Za-z0-9_-]*.java$");
  }
  static String p(final int n1, final int n2) {
    return FileHeuristics.formatRelative(d(n1, n2));
  }
  static double ratio(final double n1, final double n2) {
    return n2 / n1;
  }
  static String formatRelative(final double ¢) {
    return String.format(format2(¢) + "%%", box.it(100 * ¢));
  }
  static String formatRelative(final double d1, final double d2) {
    return formatRelative(d1 / d2);
  }
  static double round3(final double ¢) {
    switch (digits(¢)) {
      case 1:
        return Math.round(100 * ¢) / 100.0;
      case 2:
        return Math.round(10 * ¢) / 10.0;
      case -1:
      case 0:
        return Math.round(1000 * ¢) / 1000.0;
      default:
        return ¢;
    }
  }
  /** Check whether given string containing Java code contains {@link Test}
   * annotations
   * <p>
   * @param function
   */
  static boolean containsTestAnnotation(final String javaCode) {
    final CompilationUnit cu = (CompilationUnit) makeAST.COMPILATION_UNIT.from(javaCode);
    final Bool $ = new Bool();
    cu.accept(new ASTTrotter() {
      @Override public boolean visit(final MethodDeclaration node) {
        if (extract.annotations(node).stream().noneMatch(λ -> "@Test".equals(λ + "")))
          return true;
        startFolding();
        $.set();
        return true;
      }
    });
    return $.get();
  }
  /** Determines whether a file is production code, using the heuristic that
   * production code does not contain {@code @}{@link Test} annotations
   * <p>
   */
  static boolean productionCode(@¢ final File $) {
    try {
      return !containsTestAnnotation(FileUtils.read($));
    } catch (final IOException ¢) {
      note.io(¢, "File = " + $);
      return false;
    }
  }
  public static boolean p(final File f) {
    return isProductionCode(f) && productionCode(f);
  }
}
