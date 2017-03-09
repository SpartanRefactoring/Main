package il.org.spartan.spartanizer.cmdline.tables;

import java.io.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.lisp.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.research.util.*;
import il.org.spartan.spartanizer.utils.*;
import il.org.spartan.utils.*;

/** TODO Matteo Orru': document class {@link }
 * @author Matteo Orru' <tt>matteo.orru@cs.technion.ac.il</tt>
 * @since 2017-02-10 */
public class CompilationUnitRecord {
  public CompilationUnit inner;
  final int linesOfCode;
  public final int numClasses;
  public final int numExpressions;
  int numMethods;
  public int numStatements;
  public String pakcage;
  int testCount;
  String path;
  String relativePath;

  public void setPath(final String path) {
    this.path = path;
  }

  public CompilationUnitRecord(final CompilationUnit inner) {
    this.inner = inner;
    numStatements = measure.statements(inner);
    numExpressions = measure.expressions(inner);
    linesOfCode = countLines(inner + "");
    numMethods = descendants.whoseClassIs(MethodDeclaration.class).from(inner).size();
    numClasses = descendants.whoseClassIs(AbstractTypeDeclaration.class).from(inner).size();
    final PackageDeclaration p = first(descendants.whoseClassIs(PackageDeclaration.class).from(inner));
    pakcage = p == null ? "" : p.getName() + "";
    countTestAnnotation(inner);
    // testCount = Int.valueOf(countTestAnnotation(inner));
  }

  private static int countLines(final String ¢) {
    return ¢.split("\r\n|\r|\n").length;
  }

  public int testCount() {
    return testCount;
  }

  static boolean hasTestAnnotation;

  @SuppressWarnings("hiding") public void countTestAnnotation(final CompilationUnit inner) {
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
  static boolean containsTestAnnotation(final File f) {
    try {
      final String javaCode = FileUtils.read(f);
      containsTestAnnotation(javaCode);
    } catch (final IOException x) {
      monitor.infoIOException(x, "File = " + f);
    }
    return hasTestAnnotation;
  }

  public static boolean containsTestAnnotation(final String javaCode) {
    final CompilationUnit cu = (CompilationUnit) makeAST.COMPILATION_UNIT.from(javaCode);
    cu.accept(new ASTVisitor(true) {
      @Override public boolean visit(final AnnotationTypeDeclaration node) {
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
}
