package il.org.spartan.refactoring.utils;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.TargetSourceRangeComputer.SourceRange;

/**
 * An accessible container for the source code
 *
 * @author Ori Roth
 * @since 2016-04-17
 */
public class Source {

  private static String s;
  private static IPath p;
  private static CompilationUnit cu;
  private static ASTRewrite r;
  public static String get() {
    return s;
  }
  public static void set(String c) {
    s = c;
  }
  public static IPath getPath() {
    return p;
  }
  public static void setPath(IPath ip) {
    p = ip;
  }
  public static CompilationUnit getCompilationUnit() {
    return cu;
  }
  public static void setCompilationUnit(CompilationUnit u) {
    cu = u;
  }
  public static ASTRewrite getASTRewrite() {
    return r;
  }
  public static void setASTRewrite(ASTRewrite rew) {
    r = rew;
  }
  public static String get(ASTNode n) {
    SourceRange t = r.getExtendedSourceRangeComputer().computeSourceRange(n);
    return s.substring(t.getStartPosition(), t.getStartPosition() + t.getLength());
  }
}
