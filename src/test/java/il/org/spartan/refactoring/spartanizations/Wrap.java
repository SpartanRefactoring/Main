package il.org.spartan.refactoring.spartanizations;

import static il.org.spartan.Utils.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;

import il.org.spartan.*;
import il.org.spartan.refactoring.engine.*;
import il.org.spartan.refactoring.utils.*;

/** An empty <code><b>enum</b></code> for fluent programming. The name should
 * say it all: The name, followed by a dot, followed by a method name, should
 * read like a sentence phrase.
 * @author Yossi Gil
 * @since 2015-07-16 */
public enum Wrap {
  FULL_COMILATION_UNIT(//
      "// BEGIN Compilation unit", //
      "\n// END compilation unit" //
  ), OUTER_TYPE_IN_SOME_COMILATION_UNIT(//
      FULL_COMILATION_UNIT.before + //
          "\n\t package p; // BEGIN Outer type in a compilation unit\n"//
      , //
      "\n\t // END outer type in a compilation unit\n" + //
          FULL_COMILATION_UNIT.after //
  ), A_CLASS_MEMBER_OF_SOME_SORT( //
      OUTER_TYPE_IN_SOME_COMILATION_UNIT.before + //
          "\n\t\t public class C {// BEGIN Class C\n" //
      , //
      "\n\t\t } // END class C\n" + //
          OUTER_TYPE_IN_SOME_COMILATION_UNIT.after //
  ), STATEMENT_OR_SOMETHING_THAT_MAY_APPEAR_IN_A_METHOD(//
      A_CLASS_MEMBER_OF_SOME_SORT.before + //
          "\n\t\t\t Object m() { // BEGIN Public function m\n" //
      , //
      "\n\t\t\t } // END public function \n" + //
          A_CLASS_MEMBER_OF_SOME_SORT.after //
  ), EXPRESSION_IE_SOMETHING_THAT_MAY_SERVE_AS_ARGUMENT(//
      STATEMENT_OR_SOMETHING_THAT_MAY_APPEAR_IN_A_METHOD.before + //
          "\n\t\t\t\t if (foo("//
      , //
      ",0)) return g();\n" //
          + STATEMENT_OR_SOMETHING_THAT_MAY_APPEAR_IN_A_METHOD.after //
  ), //
  //
  ;
  public static final Wrap[] WRAPS = new Wrap[] { //
      FULL_COMILATION_UNIT,
      OUTER_TYPE_IN_SOME_COMILATION_UNIT, //
      A_CLASS_MEMBER_OF_SOME_SORT, //
      STATEMENT_OR_SOMETHING_THAT_MAY_APPEAR_IN_A_METHOD, //
      EXPRESSION_IE_SOMETHING_THAT_MAY_SERVE_AS_ARGUMENT,//
  };

  public static String essence(final String codeFragment) {
    return tide.clean(removeComments(codeFragment));
  }

  /** Finds the most appropriate Wrap for a given code fragment
   * @param codeFragment JD
   * @return most appropriate Wrap, or null, if the parameter could not be
   *         parsed appropriately. */
  public static Wrap find(final String codeFragment) {
    for (final Wrap $ : WRAPS)
      if ($.contains("" + $.intoCompilationUnit(codeFragment), codeFragment))
        return $;
    azzert.fail("שימ לב!\n" + //
        "Nota!\n" + //
        "Either I am buggy, or this must be a problem of incorrect Java code you placed\n" + //
        "at a string literal somewhere \n " + //
        "\t\t =>  In *your* _אצלך_ @Test related Java code  <== \n" + //
        "To fix this problem, copy this trace window (try right clicking _here_). Then,\n" + //
        "paste the trace to examine it with some text editor. I printed  below my attempts\n" + //
        "of making sense of this code. It may have something you (or I) did wrong, but:\n" + //
        "It sure does not look like a correct Java code to me.\n" + //
        "\n" + //
        "Here are the attempts I made at literal ```" + codeFragment + "''':,\n" + //
        "\n" + //
        options(codeFragment));
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
      final ASTNode n = MakeAST.COMPILATION_UNIT.from(on);
      $.append("\n\nAttempt #" + ++i + " (of " + Wrap.WRAPS.length + "): is it a " + w + "? Let's see:\n");
      $.append("\n\t What I tried as input was (essentially) this literal ```" + essence(on)).append("'''\n");
      $.append("\n\t Alas, what the parser gave me was (essentially) ```" + w.intoCompilationUnit(codeFragment) + "'''\n");
      $.append("\n\t This might be another variant, who knows?  ```" + n + "'''\n");
    }
    return "" + $;
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
    azzert.notNull(essence2);
    return essence2.contains(essence);
  }
}
