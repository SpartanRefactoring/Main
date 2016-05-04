package il.org.spartan.refactoring.utils;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.TargetSourceRangeComputer.SourceRange;

/**
 * An accessible container for the source code, and other global resources
 *
 * @author Ori Roth
 * @since 2016-04-17
 */
public class Source {
  private static String s;
  private static CompilationUnit cu;
  private static ASTRewrite r;

  /**
   * Get source code which was set before. Updated source not guaranteed
   *
   * @return source code
   */
  public static String get() {
    return s;
  }
  /**
   * Set source code to be used for next rewrite
   *
   * @param c source code
   */
  public static void set(String c) {
    s = c;
  }
  /**
   * Get current {@link CompilationUnit}
   *
   * @return {@link CompilationUnit} built for current code
   */
  public static CompilationUnit getCompilationUnit() {
    return cu;
  }
  /**
   * Set {@link CompilationUnit}
   *
   * @param u {@link CompilationUnit} built for current code
   */
  public static void setCompilationUnit(CompilationUnit u) {
    cu = u;
  }
  /**
   * Get current {@link ASTRewrite} in use
   *
   * @return {@link ASTRewrite} in use
   */
  public static ASTRewrite getASTRewrite() {
    return r;
  }
  /**
   * Set {@link ASTRewrite}
   *
   * @param rew {@link ASTRewrite} currently in use
   */
  public static void setASTRewrite(ASTRewrite rew) {
    r = rew;
  }
  /**
   * Get all source code associated with the {@link ASTNode}. This includes any
   * comments or whitespaces considered part of the node
   *
   * @param n {@link ASTNode}
   * @return node's source code
   */
  public static String get(ASTNode n) {
    final SourceRange t = r.getExtendedSourceRangeComputer().computeSourceRange(n);
    return s.substring(t.getStartPosition(), t.getStartPosition() + t.getLength());
  }
}
