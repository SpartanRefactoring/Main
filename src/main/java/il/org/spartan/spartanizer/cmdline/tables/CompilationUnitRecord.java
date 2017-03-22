package il.org.spartan.spartanizer.cmdline.tables;

import static il.org.spartan.lisp.*;

import java.io.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.research.util.*;
import il.org.spartan.utils.*;
import org.jetbrains.annotations.NotNull;

/** TODO Matteo Orru': document class {@link }
 * @author Matteo Orru' <tt>matteo.orru@cs.technion.ac.il</tt>
 * @since 2017-02-10 */
public class CompilationUnitRecord {
  @NotNull public final CompilationUnit inner;
  final int linesOfCode;
  public final int numClasses;
  public final int numExpressions;
  final int numMethods;
  public final int numStatements;
  @NotNull public final String pakcage;
  int testCount;
  String path;
  String relativePath;

  public void setPath(final String path) {
    this.path = path;
  }

  public CompilationUnitRecord(@NotNull final CompilationUnit inner) {
    this.inner = inner;
    numStatements = measure.commands(inner);
    numExpressions = measure.expressions(inner);
    linesOfCode = countLines(inner + "");
    numMethods = descendants.whoseClassIs(MethodDeclaration.class).from(inner).size();
    numClasses = descendants.whoseClassIs(AbstractTypeDeclaration.class).from(inner).size();
    final PackageDeclaration p = first(descendants.whoseClassIs(PackageDeclaration.class).from(inner));
    pakcage = p == null ? "" : p.getName() + "";
    countTestAnnotation(inner);
    // testCount = Int.valueOf(countTestAnnotation(inner));
  }

  private static int countLines(@NotNull final String ¢) {
    return ¢.split("\r\n|\r|\n").length;
  }

  public int testCount() {
    return testCount;
  }

  static boolean hasTestAnnotation;

  @SuppressWarnings("hiding") public void countTestAnnotation(@NotNull final CompilationUnit inner) {
    // noinspection SameReturnValue
    inner.accept(new ASTVisitor(true) {
      @Override public boolean visit(final MethodDeclaration node) {
        if (extract.annotations(node).stream().anyMatch(λ -> "@Test".equals(λ + "")))
          ++testCount;
        return false;
      }
    });
  }

  /** Check if a file contains Test annotations
   * <p>
   * @param f
   * @return
   *         <p>
   *         [[SuppressWarningsSpartan]] */
  static boolean containsTestAnnotation(@NotNull final File f) {
    try {
      @NotNull final String javaCode = FileUtils.read(f);
      containsTestAnnotation(javaCode);
    } catch (@NotNull final IOException x) {
      monitor.infoIOException(x, "File = " + f);
    }
    return hasTestAnnotation;
  }

  public static boolean containsTestAnnotation(@NotNull final String javaCode) {
    @NotNull final CompilationUnit cu = (CompilationUnit) makeAST.COMPILATION_UNIT.from(javaCode);
    // noinspection SameReturnValue
    cu.accept(new ASTVisitor(true) {
      @Override public boolean visit(@NotNull final AnnotationTypeDeclaration node) {
        if (node.getName().getFullyQualifiedName() == "Test") {
          System.out.println(node.getName().getFullyQualifiedName());
          hasTestAnnotation = true;
        }
        return true;
      }
    });
    return hasTestAnnotation;
  }

  public String getPath() {
    return path;
  }

  public void setRelativePath(final String relativePath) {
    this.relativePath = relativePath;
  }

  public String getRelativePath() {
    return relativePath;
  }

  public boolean noTests() {
    return testCount == 0;
  }
}
