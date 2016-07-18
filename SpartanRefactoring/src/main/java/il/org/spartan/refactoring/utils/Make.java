package il.org.spartan.refactoring.utils;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jface.text.Document;

/**
 * An empty <code><b>enum</b></code> for fluent programming. The name should say
 * it all: The name, followed by a dot, followed by a method name, should read
 * like a sentence phrase.
 *
 * @author Yossi Gil
 * @since 2015-07-16
 */
public enum Make {
  /** Strategy for conversion into a compilation unit */
  COMPILIATION_UNIT(ASTParser.K_COMPILATION_UNIT), //
  /** Strategy for conversion into an expression */
  EXPRESSION(ASTParser.K_EXPRESSION), //
  /** Strategy for conversion into an sequence of statements */
  STATEMENTS(ASTParser.K_STATEMENTS), //
  /** Strategy for conversion into a class body */
  CLASS_BODY_DECLARATIONS(ASTParser.K_CLASS_BODY_DECLARATIONS); //
  /**
   * Converts the {@link As} value to its corresponding {@link Make} enum value
   *
   * @param a The {@link As} type
   * @return the corresponding {@link Make} value to the argument
   */
  public static Make of(final As a) {
    switch (a) {
      case STATEMENTS:
        return Make.STATEMENTS;
      case EXPRESSION:
        return Make.EXPRESSION;
      case COMPILIATION_UNIT:
        return Make.COMPILIATION_UNIT;
      case CLASS_BODY_DECLARATIONS:
        return Make.CLASS_BODY_DECLARATIONS;
      default:
        return null;
    }
  }
  private final int kind;
  private Make(final int kind) {
    this.kind = kind;
  }
  /**
   * Creates a no-binding parser for a given text
   *
   * @param text what to parse
   * @return a newly created parser for the parameter
   */
  public ASTParser parser(final char[] text) {
    final ASTParser $ = parser();
    $.setSource(text);
    return $;
  }
  /**
   * Creates a parser for a given {@link Document}
   *
   * @param d JD
   * @return the created parser
   */
  public ASTParser parser(final Document d) {
    final ASTParser $ = parser();
    $.setSource(d.get().toCharArray());
    return $;
  }
  /**
   * Creates a no-binding parser for a given compilation unit
   *
   * @param u what to parse
   * @return a newly created parser for the parameter
   */
  public ASTParser parser(final ICompilationUnit u) {
    final ASTParser $ = parser();
    $.setSource(u);
    return $;
  }
  /**
   * Creates a parser for a given {@link IFile}
   *
   * @param f JD
   * @return the created parser
   */
  public ASTParser parser(final IFile f) {
    return parser(JavaCore.createCompilationUnitFrom(f));
  }
  /**
   * Creates a parser for a given marked text.
   *
   * @param m JD
   * @return the created parser
   */
  public ASTParser parser(final IMarker m) {
    return parser(As.iCompilationUnit(m));
  }
  /**
   * Creates a no-binding parser for a given text
   *
   * @param text what to parse
   * @return a newly created parser for the parameter
   */
  public ASTParser parser(final String text) {
    return parser(text.toCharArray());
  }
  private ASTParser parser() {
    final ASTParser $ = ASTParser.newParser(AST.JLS8);
    $.setKind(kind);
    $.setResolveBindings(true);
    return $;
  }
}
