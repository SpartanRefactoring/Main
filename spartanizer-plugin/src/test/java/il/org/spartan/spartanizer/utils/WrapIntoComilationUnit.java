package il.org.spartan.spartanizer.utils;

import static il.org.spartan.Utils.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.nominal.*;

/** An empty {@code enum} for fluent programming. The name should say it all:
 * The name, followed by a dot, followed by a method name, should read like a
 * sentence phrase.
 * @author Yossi Gil
 * @since 2015-07-16 */
public enum WrapIntoComilationUnit {
  OUTER("package p; // BEGIN PACKAGE \n", "\n// END PACKAGE\n"),
  /** Algorithm for wrapping/unwrapping a method */
  Method("package p;\npublic final class CXXX23xyZ {\n", "} // END p\n"), //
  /** Algorithm for wrapping/unwrapping a statement */
  Statement(Method.before + "public void m(){", "} // END m \n" + Method.after), //
  /** Algorithm for wrapping/unwrapping an expression */
  Expression(//
      Statement.before //
          + "   while (", //
      ");\n" //
          + Statement.after //
  ), //
  //
  ;
  public static final WrapIntoComilationUnit[] WRAPS = { Statement, Expression, Method, OUTER };

  /** Finds the most appropriate Wrap for a given code fragment
   * @param codeFragment JD
   * @return most appropriate Wrap, or null, if the parameter could not be
   *         parsed appropriately. */
  public static WrapIntoComilationUnit find(final String codeFragment) {
    for (final WrapIntoComilationUnit $ : WRAPS) // NANO
      if ($.contains($.intoCompilationUnit(codeFragment) + "", codeFragment))
        return $;
    if (!system.isBalanced(codeFragment))
      azzert.fail("Input \n'" + codeFragment + "'\n is not parenthesis balanced; cannot compile it");
    azzert.fail("Cannot parse '\n" + codeFragment + "\n W********* I tried the following options:" + options(codeFragment));
    throw new RuntimeException();
  }
  private static String options(final String codeFragment) {
    final StringBuilder $ = new StringBuilder();
    int i = 0;
    for (final WrapIntoComilationUnit w : WrapIntoComilationUnit.WRAPS) {
      final String on = w.on(codeFragment);
      final ASTNode n = makeAST.COMPILATION_UNIT.from(on);
      $.append("\n* Attempt ").append(++i).append(": ").append(w);
      $.append("\n* I = <").append(Trivia.essence(on)).append(">;");
      $.append("\n* O = <").append(Trivia.essence(n + "")).append(">;");
      $.append("\n**** PARSED=\n").append(w.intoCompilationUnit(codeFragment)).append("");
      $.append("\n* AST=").append(Trivia.essence(n.getAST() + ""));
      $.append("\n**** INPUT=\n").append(on);
      $.append("\n**** OUTPUT=\n").append(n);
      $.append("\n**** PROBLEMS=\n").append(wizard.problems(n));
    }
    return $ + "";
  }

  private final String before;
  private final String after;

  WrapIntoComilationUnit(final String before, final String after) {
    this.before = before;
    this.after = after;
  }
  private boolean contains(final String wrap, final String inner) {
    final String off = off(wrap), $ = Trivia.essence(inner), essence2 = Trivia.essence(off);
    assert essence2 != null;
    return essence2.contains($);
  }
  /** Wrap a given code fragment, and then parse it, converting it into a
   * {@link CompilationUnit}.
   * @param codeFragment JD
   * @return a newly created {@link CompilationUnit} representing the parsed AST
   *         of the wrapped parameter. */
  public CompilationUnit intoCompilationUnit(final String codeFragment) {
    return (CompilationUnit) makeAST.COMPILATION_UNIT.from(on(codeFragment));
  }
  /** Wrap a given code fragment, and converts it into a {@link Document}
   * @param codeFragment JD
   * @return a newly created {@link CompilationUnit} representing the parsed AST
   *         of the wrapped parameter. */
  public Document intoDocument(final String codeFragment) {
    return new Document(on(codeFragment));
  }
  /** Remove a wrap from around a phrase
   * @param codeFragment a wrapped program phrase
   * @return unwrapped phrase */
  public String off(final String codeFragment) {
    return removeSuffix(removePrefix(codeFragment, before), after);
  }
  /** Place a wrap around a phrase
   * @param codeFragment some program phrase
   * @return wrapped phrase */
  public String on(final String codeFragment) {
    return before + codeFragment + after;
  }
}
