package il.org.spartan.spartanizer.spartanizations;

import static il.org.spartan.Utils.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.engine.*;

/** An empty <code><b>enum</b></code> for fluent programming. The name should
 * say it all: The name, followed by a dot, followed by a method name, should
 * read like a sentence phrase.
 * @author Yossi Gil
 * @since 2015-07-16 */
public enum Wrap {
  OUTER("package p; // BEGIN PACKAGE \n", "\n// END PACKAGE\n"),
  /** Algorithm for wrapping/unwrapping a method */
  Method("package p;\n" + "", "} // END p\n" + ""), //
  /** Algorithm for wrapping/unwrapping a statement */
  Statement(Method.before + "", "} // END m \n" + "" + Method.after + //
          ""), //
  /** Algorithm for wrapping/unwrapping an expression */
  Expression(//
      Statement.before //
          + "   while (", //
      ");\n" //
          + Statement.after //
  ), //
  //
  ;
  public static final Wrap[] WRAPS = new Wrap[] { Statement, Expression, Method, OUTER };

  public static String essence(final String codeFragment) {
    return tide.clean(removeComments(codeFragment));
  }

  /** Finds the most appropriate Wrap for a given code fragment
   * @param codeFragment JD
   * @return most appropriate Wrap, or null, if the parameter could not be
   *         parsed appropriately. */
  public static Wrap find(final String codeFragment) {
    for (final Wrap $ : WRAPS)
      if ($.contains($.intoCompilationUnit(codeFragment) + "", codeFragment))
        return $;
    azzert.fail("Cannot parse '\n" + codeFragment + "\n********* I tried the following options:" + options(codeFragment));
    throw new RuntimeException();
  }

  static String removeComments(final String codeFragment) {
    return codeFragment//
        .replaceAll("//.*?\n", "\n")//
        .replaceAll("/\\*(?=(?:(?!\\*/)[\\s\\S])*?)(?:(?!\\*/)[\\s\\S])*\\*/", "");
  }

  private static String options(final String codeFragment) {
    final StringBuilder $ = new StringBuilder();
    int i = 0;
    for (final Wrap w : Wrap.WRAPS) {
      final String on = w.on(codeFragment);
      final ASTNode n = makeAST.COMPILATION__UNIT.from(on);
      $.append("\n* Attempt ").append(++i).append(": ").append(w);
      $.append("\n* I = <").append(essence(on)).append(">;");
      $.append("\n* O = <").append(essence(n + "")).append(">;");
      $.append("\n**** PARSED=\n").append(w.intoCompilationUnit(codeFragment) + "");
      $.append("\n* AST=").append(essence(n.getAST() + ""));
      $.append("\n**** INPUT=\n").append(on);
      $.append("\n**** OUTPUT=\n").append(n + "");
    }
    return $ + "";
  }

  private final String before;
  private final String after;

  Wrap(final String before, final String after) {
    this.before = before;
    this.after = after;
  }

  /** Wrap a given code fragment, and then parse it, converting it into a
   * {@link CompilationUnit}.
   * @param codeFragment JD
   * @return a newly created {@link CompilationUnit} representing the parsed AST
   *         of the wrapped parameter. */
  public CompilationUnit intoCompilationUnit(final String codeFragment) {
    return (CompilationUnit) makeAST.COMPILATION__UNIT.from(on(codeFragment));
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
  public final String off(final String codeFragment) {
    return removeSuffix(removePrefix(codeFragment, before), after);
  }

  /** Place a wrap around a phrase
   * @param codeFragment some program phrase
   * @return wrapped phrase */
  public final String on(final String codeFragment) {
    return before + codeFragment + after;
  }

  private boolean contains(final String wrap, final String inner) {
    final String off = off(wrap);
    final String essence = essence(inner);
    final String essence2 = essence(off);
    assert essence2 != null;
    return essence2.contains(essence);
  }
}
