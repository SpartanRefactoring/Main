package il.org.spartan.spartanizer.utils;

import static il.org.spartan.Utils.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import org.jetbrains.annotations.NotNull;

/** An empty <code><b>enum</b></code> for fluent programming. The name should
 * say it all: The name, followed by a dot, followed by a method name, should
 * read like a sentence phrase.
 * @author Yossi Gil
 * @since 2015-07-16 */
public enum Wrap {
  OUTER("package p; // BEGIN PACKAGE \n", "\n// END PACKAGE\n"),
  /** Algorithm for wrapping/unwrapping a method */
  Method("package p;\n" + "public final class CXXX23xyZ {\n", "} // END p\n"), //
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
  public static final Wrap[] WRAPS = { Statement, Expression, Method, OUTER };

  public static String essence(@NotNull final String codeFragment) {
    return tide.clean(trivia.removeComments(codeFragment));
  }

  /** Finds the most appropriate Wrap for a given code fragment
   * @param codeFragment JD
   * @return most appropriate Wrap, or null, if the parameter could not be
   *         parsed appropriately. */
  @NotNull public static Wrap find(@NotNull final String codeFragment) {
    for (final Wrap $ : WRAPS) // NANO
      if ($.contains($.intoCompilationUnit(codeFragment) + "", codeFragment))
        return $;
    azzert.fail("Cannot parse '\n" + codeFragment + "\n********* I tried the following options:" + options(codeFragment));
    throw new RuntimeException();
  }

  @NotNull private static String options(final String codeFragment) {
    final StringBuilder $ = new StringBuilder();
    int i = 0;
    for (final Wrap w : Wrap.WRAPS) {
      final String on = w.on(codeFragment);
      final ASTNode n = makeAST1.COMPILATION_UNIT.from(on);
      $.append("\n* Attempt ").append(++i).append(": ").append(w);
      $.append("\n* I = <").append(essence(on)).append(">;");
      $.append("\n* O = <").append(essence(n + "")).append(">;");
      $.append("\n**** PARSED=\n").append(w.intoCompilationUnit(codeFragment)).append("");
      $.append("\n* AST=").append(essence(n.getAST() + ""));
      $.append("\n**** INPUT=\n").append(on);
      $.append("\n**** OUTPUT=\n").append(n);
    }
    return $ + "";
  }

  private final String before;
  private final String after;

  Wrap(final String before, final String after) {
    this.before = before;
    this.after = after;
  }

  private boolean contains(final String wrap, @NotNull final String inner) {
    final String off = off(wrap), $ = essence(inner), essence2 = essence(off);
    assert essence2 != null;
    return essence2.contains($);
  }

  /** Wrap a given code fragment, and then parse it, converting it into a
   * {@link CompilationUnit}.
   * @param codeFragment JD
   * @return a newly created {@link CompilationUnit} representing the parsed AST
   *         of the wrapped parameter. */
  @NotNull public CompilationUnit intoCompilationUnit(final String codeFragment) {
    return (CompilationUnit) makeAST1.COMPILATION_UNIT.from(on(codeFragment));
  }

  /** Wrap a given code fragment, and converts it into a {@link Document}
   * @param codeFragment JD
   * @return a newly created {@link CompilationUnit} representing the parsed AST
   *         of the wrapped parameter. */
  @NotNull public Document intoDocument(final String codeFragment) {
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
  @NotNull public String on(final String codeFragment) {
    return before + codeFragment + after;
  }
}
