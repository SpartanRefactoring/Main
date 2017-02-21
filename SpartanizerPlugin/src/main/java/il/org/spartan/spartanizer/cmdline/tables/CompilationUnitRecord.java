package il.org.spartan.spartanizer.cmdline.tables;

import java.io.*;
import java.util.*;

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
  int numMethods = 0;
  public int numStatements;
  public String pakcage;
  private int testCount = 0;

  public CompilationUnitRecord(final CompilationUnit inner) {
    this.inner = inner;
    numStatements = measure.statements(inner);
    numExpressions = measure.expressions(inner);
    linesOfCode = countLines(inner + ""); 
    numMethods = yieldDescendants.ofClass(MethodDeclaration.class).from(inner).size();
    numClasses = yieldDescendants.ofClass(AbstractTypeDeclaration.class).from(inner).size();
    PackageDeclaration p = first(yieldDescendants.ofClass(PackageDeclaration.class).from(inner));
    pakcage = p == null ? "" : p.getName() + "";
    countTestAnnotation(inner);
    // testCount = Int.valueOf(countTestAnnotation(inner));
  }
 
  private static int countLines(String str){
    String[] lines = str.split("\r\n|\r|\n");
    return  lines.length;
 }

  public int testCount() {
    return testCount;
  }
  
  static boolean hasTestAnnotation = false;
  
  @SuppressWarnings("hiding")
  public void countTestAnnotation(final CompilationUnit inner){
    inner.accept(new ASTVisitor() {
        @SuppressWarnings("synthetic-access")
        @Override public boolean visit(MethodDeclaration node) {
          List<Annotation> t = extract.annotations(node);
//        if(extract.annotations(node).stream().anyMatch(λ -> "@Test".equals(λ + "")))
//          testCount++;
            for (Annotation a: t){
              if(a.toString().equals("@Test"))
                testCount++;
            }
          return false;
        }
      });
  }
  
  /** 
   * Check if a file contains Test annotations
   * <p>
   * @param f
   * @return
   * <p> [[SuppressWarningsSpartan]]
   */
 static boolean containsTestAnnotation(File f) {
   try {
     String javaCode = FileUtils.read(f);
     containsTestAnnotation(javaCode);
   } catch (IOException x) {
     monitor.infoIOException(x, "File = " + f);
   }
   return hasTestAnnotation;
 }

  public static boolean containsTestAnnotation(String javaCode) {
    CompilationUnit cu = (CompilationUnit) makeAST.COMPILATION_UNIT.from(javaCode);
     cu.accept(new ASTVisitor() {
       @Override public boolean visit(AnnotationTypeDeclaration node) {
         if (node.getName().getFullyQualifiedName() == ("Test")){
           System.out.println(node.getName().getFullyQualifiedName());
           hasTestAnnotation = true;
         }
         return true;
       }
     });
     return hasTestAnnotation;
  }
}
