package il.org.spartan.spartanizer.engine;

import org.eclipse.core.resources.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;

/** An empty <code><b>enum</b></code> for fluent programming. The name should
 * say it all: The name, followed by a dot, followed by a method name, should
 * read like a sentence phrase.
 * @author Yossi Gil
 * @since 2015-07-16 */
public enum make1 {
  /** Strategy for conversion into a compilation unit */
  COMPILATION_UNIT(ASTParser.K_COMPILATION_UNIT), //
  /** Strategy for conversion into an expression */
  EXPRESSION(ASTParser.K_EXPRESSION), //
  /** Strategy for conversion into an sequence of sideEffects */
  STATEMENTS(ASTParser.K_STATEMENTS), //
  /** Strategy for conversion into a class body */
  CLASS_BODY_DECLARATIONS(ASTParser.K_CLASS_BODY_DECLARATIONS); //
  /** Converts the {@link makeAST1} value to its corresponding {@link make1}
   * enum value
   * @param tipper The {@link makeAST1} type
   * @return corresponding {@link make1} value to the argument */
  public static make1 from(final makeAST1 ¢) {
    switch (¢) {
      case CLASS_BODY_DECLARATIONS:
        return make1.CLASS_BODY_DECLARATIONS;
      case COMPILATION_UNIT:
        return make1.COMPILATION_UNIT;
      case EXPRESSION:
        return make1.EXPRESSION;
      case STATEMENTS:
        return make1.STATEMENTS;
      default:
        return null;
    }
  }

  private final int kind;

  make1(final int kind) {
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
  public ASTParser parser(final Document ¢) {
    final ASTParser $ = wizard.parser(kind);
    $.setSource(¢.get().toCharArray());
    return $;
  }

  /** Creates a no-binding parser for a given compilation unit
   * @param u what to parse
   * @return a newly created parser for the parameter */
  public ASTParser parser(final ICompilationUnit ¢) {
    final ASTParser $ = wizard.parser(kind);
    $.setSource(¢);
    return $;
  }

  /** Creates a binding parser for a given compilation unit
   * @param u what to parse
   * @return a newly created parser for the parameter */
  public ASTParser parserWithBinding(final ICompilationUnit ¢) {
    final ASTParser $ = wizard.parser(kind);
    $.setSource(¢);
    $.setResolveBindings(true);
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
    return parser(makeAST1.iCompilationUnit(¢));
  }

  /** Creates a no-binding parser for a given text
   * @param text what to parse
   * @return a newly created parser for the parameter */
  public ASTParser parser(final String text) {
    return parser(text.toCharArray());
  }
}