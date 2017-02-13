package il.org.spartan.spartanizer.cmdline.tables;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.research.util.*;

/**
 * TODO Matteo Orru': document class {@link }
 * @author Matteo Orru' <tt>matteo.orru@cs.technion.ac.il</tt>
 * @since 2017-02-10
 */
public class CURecord {
  
  public int numStatements;
  public int numExpressions;
  public CompilationUnit before;
  
  private String packg;
  private Integer loc = Integer.valueOf(0);
  private Integer numMethods = Integer.valueOf(0);
  private Integer numClasses = Integer.valueOf(0);
  
  @SuppressWarnings("boxing")
  public CURecord(final CompilationUnit c){
    before = c;
    numStatements = measure.statements(c);
    numExpressions = measure.expressions(c);
    loc = count.lines(c);
    findPackage(c);
    countMethodNumber(c);
    countClassNumber(c);
  }

  private void findPackage(final CompilationUnit u) {
    u.accept(new ASTVisitor() {
      @SuppressWarnings("synthetic-access")
      @Override
      public boolean visit(final PackageDeclaration ¢){
        packg = ¢.getName() + "";
        return false;
      }
    });
  }
  
  private void countMethodNumber(final CompilationUnit u) {
    u.accept(new ASTVisitor() {
      @Override @SuppressWarnings({ "boxing", "unused", "synthetic-access" })
      public boolean visit(final MethodDeclaration ¢){
        ++numMethods;
        return false;
      }
    });
  }
  
  private void countClassNumber(final CompilationUnit u) {
    u.accept(new ASTVisitor() {
      @Override @SuppressWarnings({ "unused", "boxing", "synthetic-access" })
      public boolean visit(final TypeDeclaration ¢){
        ++numClasses;
        return false;
      }
    });
  }

  public Integer getNumMethods() {
    return numMethods;
  }

  public Integer getLOC() {
    return loc;
  }

  public String getPackage() {
    return packg;
  }

}
