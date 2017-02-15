package il.org.spartan.spartanizer.cmdline.tables;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.lisp.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.research.util.*;

/** TODO Matteo Orru': document class {@link }
 * @author Matteo Orru' <tt>matteo.orru@cs.technion.ac.il</tt>
 * @since 2017-02-10 */
public class CopilationUnitRecord {
  public CompilationUnit inner;
  final int linesOfCode;
  public final int numClasses;
  public final int numExpressions;
  int numMethods = 0;
  public int numStatements;
  public String pakcage;

  public CopilationUnitRecord(final CompilationUnit inner) {
    this.inner = inner;
    numStatements = measure.statements(inner);
    numExpressions = measure.expressions(inner);
    linesOfCode = countLines(inner + ""); 
    numMethods = yieldDescendants.ofClass(MethodDeclaration.class).from(inner).size();
    numClasses = yieldDescendants.ofClass(AbstractTypeDeclaration.class).from(inner).size();
    PackageDeclaration p = first(yieldDescendants.ofClass(PackageDeclaration.class).from(inner));
    pakcage = p == null ? "" : p.getName() + "";
  }
  
  private static int countLines(String str){
    String[] lines = str.split("\r\n|\r|\n");
    return  lines.length;
 }
}
