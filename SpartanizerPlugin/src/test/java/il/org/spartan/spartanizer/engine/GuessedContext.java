package il.org.spartan.spartanizer.engine;

import static il.org.spartan.Utils.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;

import il.org.spartan.*;

/** An empty <code><b>enum</b></code> for fluent programming. The name should
 * say it all: The name, followed by a dot, followed by a method name, should
 * read like a sentence phrase.
 * @author Yossi Gil
 * @since 2015-07-16 */
public enum GuessedContext {
  COMPILATION__UNIT__LOOK__ALIKE(//
      "/* BEGIN Compilation unit */\n", //
      "\n /* END compilation unit */\n"//
  ), OUTER__TYPE__LOOKALIKE(//
      COMPILATION__UNIT__LOOK__ALIKE.before + //
          "\n\t package p; /* BEGIN Outer type in a compilation unit */\n"//
      , //
      "\n\t /* END outer type in a compilation unit */\n" + //
          COMPILATION__UNIT__LOOK__ALIKE.after //
  ), METHOD__LOOKALIKE( //
      OUTER__TYPE__LOOKALIKE.before + //
          "\n\t\t public class C {/* BEGIN Class C*/\n" //
      , //
      "\n\t\t } /* END class C */\n" + //
          OUTER__TYPE__LOOKALIKE.after //
  ), STATEMENTS__LOOK__ALIKE(//
      METHOD__LOOKALIKE.before //
          + "\n\t\t\t public Object m() { /* BEGIN Public function m */\n" //
          + "\n\t\t\t\t while (f4324()) {"//
          + "\n\t\t\t\t g3423436();"//
      ,
      "\n\t\t\t\t h6463634();" + ""
  ), EXPRESSION__LOOK__ALIKE(//
      STATEMENTS__LOOK__ALIKE.before + //
          "\n\t\t\t\t if (foo("//
      , //
      ",0)) return g();\n" //
          + STATEMENTS__LOOK__ALIKE.after //
  ), not__statment__may__occur__in__initializer__block(//
      METHOD__LOOKALIKE.before + //
          "\n\t\t\t { /* BEGIN Instance initializer block */\n" //
      , //
      "\n\t\t\t } /* END instance initializer block */\n" + //
          METHOD__LOOKALIKE.after //
  ), not__statment__may__occur__in__static__initializer__block(//
      METHOD__LOOKALIKE.before + //
          "\n\t\t\t static{ /* BEGIN Instance initializer block */\n" //
      , //
      "\n\t\t\t } /* END instance initializer block */\n" + //
          METHOD__LOOKALIKE.after //
  ), //
  //
  ;
  public static final GuessedContext[] AlternativeContextToConsiderInOrder = new GuessedContext[] { //
      COMPILATION__UNIT__LOOK__ALIKE, //
      OUTER__TYPE__LOOKALIKE, //
      STATEMENTS__LOOK__ALIKE, //
      METHOD__LOOKALIKE, //
      EXPRESSION__LOOK__ALIKE, //
      not__statment__may__occur__in__initializer__block, //
      not__statment__may__occur__in__static__initializer__block, };

  public static String essence(final String codeFragment) {
    return tide.clean(removeComments(codeFragment));
  }

  /** Finds the most appropriate Guess for a given code fragment
   * @param codeFragment JD
   * @return most appropriate Guess, or null, if the parameter could not be
   *         parsed appropriately. */
  public static GuessedContext find(final String codeFragment) {
    for (final GuessedContext $ : AlternativeContextToConsiderInOrder)
      if ($.contains($.intoCompilationUnit(codeFragment) + "", codeFragment))
        return $;
    azzert.fail("שימ לב!\n" + //
        "Nota!\n" + //
        "Either I am buggy, or this must be a problem of incorrect Java code you placed\n" + //
        "at a string literal somewhere \n " + //
        "\t\t =>  in *your* __sbצלך__ @Test related Java code  <== \n" + //
        "To fix this problem, copy this trace window (try right clicking __here__). Then,\n" + //
        "paste the trace to examine it with some text editor. I printed  below my attempts\n" + //
        "of making sense of this code. It may have something you (or I) did wrong, but:\n" + //
        "It sure does not look like a correct Java code to me.\n" + //
        "\n" + //
        "Here are the attempts I made at literal ```" + codeFragment + "''':,\n" + //
        "\n" + //
        enumerateFailingAttempts(codeFragment));
    throw new RuntimeException();
  }

  static String enumerateFailingAttempts(final String codeFragment) {
    final StringBuilder $ = new StringBuilder();
    int i = 0;
    for (final GuessedContext w : GuessedContext.AlternativeContextToConsiderInOrder) {
      final String on = w.on(codeFragment);
      $.append("\n\nAttempt #" + ++i + " (of " + GuessedContext.AlternativeContextToConsiderInOrder.length + "):");
      $.append("\n\t\t Is it a " + w + "?");
      $.append("\n\t Let's see...");
      $.append("\n\t\t What I tried as input was (essentially) this literal:");
      $.append("\n\t```" + essence(on) + "'''");
      final CompilationUnit u = w.intoCompilationUnit(codeFragment);
      $.append("\n\t\t Alas, what the parser generated " + u.getProblems().length //
          + " on (essentially) this bit of code");
      $.append("\n\t\t\t```" + essence(u + "") + "'''");
      $.append("\n\t\t Properly formatted, this bit should look like so: ");
      $.append("\n\t\t\t```" + u + "'''");
      $.append("\n\t\t And the full list of problems was: ");
      $.append("\n\t\t\t```" + u.getProblems() + "'''");
    }
    return $ + "";
  }

  static String removeComments(final String codeFragment) {
    return codeFragment//
        .replaceAll("//.*?\n", "\n")//
        .replaceAll("/\\*(?=(?:(?!\\*/)[\\s\\S])*?)(?:(?!\\*/)[\\s\\S])*\\*/", "");
  }

  private final String before;
  private final String after;

  GuessedContext(final String before, final String after) {
    this.before = before;
    this.after = after;
  }

  private boolean contains(final String wrap, final String inner) {
    final String off = off(wrap);
    final String essence = essence(inner);
    final String essence2 = essence(off);
    assert essence2 != null;
    return essence2.contains(essence);
  }

  /** Guess a given code fragment, and then parse it, converting it into a
   * {@link CompilationUnit}.
   * @param codeFragment JD
   * @return a newly created {@link CompilationUnit} representing the parsed AST
   *         of the wrapped parameter. */
  public CompilationUnit intoCompilationUnit(final String codeFragment) {
    return (CompilationUnit) makeAST.COMPILATION__UNIT.from(on(codeFragment));
  }

  /** Guess a given code fragment, and converts it into a {@link Document}
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

  public void stays() {
    // TODO Auto-generated method stub
  }
}
