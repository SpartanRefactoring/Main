package il.org.spartan.spartanizer.engine;

import org.eclipse.core.resources.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;

import il.org.spartan.spartanizer.ast.*;

/** An empty <code><b>enum</b></code> for fluent programming. The name should
 * say it all: The name, followed by a dot, followed by a method name, should
 * read like a sentence phrase.
 * @author Yossi Gil
 * @since 2015-07-16 */
public enum Make {
  /** Strategy for conversion into a compilation unit */
  COMPILATION_UNIT(ASTParser.K_COMPILATION_UNIT), //
  /** Strategy for conversion into an expression */
  EXPRESSION(ASTParser.K_EXPRESSION), //
  /** Strategy for conversion into an sequence of statements */
  STATEMENTS(ASTParser.K_STATEMENTS), //
  /** Strategy for conversion into a class body */
  CLASS_BODY_DECLARATIONS(ASTParser.K_CLASS_BODY_DECLARATIONS); //
  /** Converts the {@link makeAST} value to its corresponding {@link Make} enum
   * value
   * @param t The {@link makeAST} type
   * @return corresponding {@link Make} value to the argument */
  public static Make of(final makeAST ¢) {
    switch (¢) {
      case STATEMENTS:
        return Make.STATEMENTS;
      case EXPRESSION:
        return Make.EXPRESSION;
      case COMPILATION_UNIT:
        return Make.COMPILATION_UNIT;
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

  /** Creates a no-binding parser for a given text
   * @param text what to parse
   * @return a newly created parser for the parameter */
  public ASTParser parser(final char[] text) {
    final ASTParser $ = wizard.parser(kind);
    $.setSource(text);
    return $;
  }

  /** Creates a parser for a given {@link Document}
   * @param d JD
   * @return created parser */
  public ASTParser parser(final Document d) {
    final ASTParser $ = wizard.parser(kind);
    $.setSource(d.get().toCharArray());
    return $;
  }

  /** Creates a no-binding parser for a given compilation unit
   * @param u what to parse
   * @return a newly created parser for the parameter */
  public ASTParser parser(final ICompilationUnit u) {
    final ASTParser $ = wizard.parser(kind);
    $.setSource(u);
    return $;
  }

  /** Creates a parser for a given {@link IFile}
   * @param f JD
   * @return created parser */
  public ASTParser parser(final IFile ¢) {
    return parser(JavaCore.createCompilationUnitFrom(¢));
  }

  /** Creates a parser for a given marked text.
   * @param m JD
   * @return created parser */
  public ASTParser parser(final IMarker ¢) {
    return parser(makeAST.iCompilationUnit(¢));
  }

  /** Creates a no-binding parser for a given text
   * @param text what to parse
   * @return a newly created parser for the parameter */
  public ASTParser parser(final String text) {
    return parser(text.toCharArray());
  }
}