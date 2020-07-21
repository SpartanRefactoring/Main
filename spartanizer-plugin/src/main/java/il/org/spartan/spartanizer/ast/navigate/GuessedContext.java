package il.org.spartan.spartanizer.ast.navigate;

import static il.org.spartan.Utils.*;

import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.engine.nominal.*;

/** An empty {@code enum} for fluent programming. The name should say it all:
 * The name, followed by a dot, followed by a method name, should read like a
 * sentence phrase.
 * @author Yossi Gil
 * @since 2015-07-16 */
public enum GuessedContext {
  BLOCK_LOOK_ALIKE(//
      "{", //
      "}"//
  ), COMPILATION_UNIT_LOOK_ALIKE(//
      "/* BEGIN Compilation unit */\n", //
      "\n /* END compilation unit */\n"//
  ), OUTER_TYPE_LOOKALIKE(//
      COMPILATION_UNIT_LOOK_ALIKE.before + //
          "\n\t package p466876; /* BEGIN Outer __ in a compilation unit */\n"//
      , //
      "\n\t /* END outer __ in a compilation unit */\n" + //
          COMPILATION_UNIT_LOOK_ALIKE.after //
  ), METHOD_LOOK_ALIKE( //
      OUTER_TYPE_LOOKALIKE.before + //
          "\n\t\t class C87456AZ {/* BEGIN Class C*/\n" //
      , //
      "\n\t\t } /* END class C87456AZ */\n" + //
          OUTER_TYPE_LOOKALIKE.after //
  ), STATEMENTS_LOOK_ALIKE(//
      METHOD_LOOK_ALIKE.before //
          + "\n\t\t\t public Object masdf767() { /* BEGIN Public function m */\n" //
      , " }" + METHOD_LOOK_ALIKE.after), EXPRESSION_LOOK_ALIKE(//
          STATEMENTS_LOOK_ALIKE.before + //
              "\n\t\t\t\t if (foo235rt("//
          , //
          ",0)) return g();\n" //
              + STATEMENTS_LOOK_ALIKE.after //
  ), not_statement_may_occur_in_initializer_block(//
      METHOD_LOOK_ALIKE.before + //
          "\n\t\t\t { /* BEGIN Instance initializer block */\n" //
      , //
      "\n\t\t\t } /* END instance initializer block */\n" + //
          METHOD_LOOK_ALIKE.after //
  ), not_statement_may_occur_in_static_initializer_block(//
      METHOD_LOOK_ALIKE.before + //
          "\n\t\t\t static{ /* BEGIN Instance initializer block */\n" //
      , //
      "\n\t\t\t } /* END instance initializer block */\n" + //
          METHOD_LOOK_ALIKE.after //
  ), //
  //
  ;
  public static final GuessedContext[] alternativeContextsToConsiderInThisOrder = { //
      COMPILATION_UNIT_LOOK_ALIKE, //
      OUTER_TYPE_LOOKALIKE, //
      STATEMENTS_LOOK_ALIKE, //
      METHOD_LOOK_ALIKE, //
      EXPRESSION_LOOK_ALIKE, //
      not_statement_may_occur_in_initializer_block, //
      not_statement_may_occur_in_static_initializer_block, //
  };

  /** Finds the most appropriate guess for a given code fragment
   * @param codeFragment JD
   * @return most appropriate Guess, or null, if the parameter could not be
   *         parsed appropriately. */
  public static GuessedContext find(final String codeFragment) {
    final String cleanFragment = codeFragment.replaceAll("\\s+", "").replaceAll(" ", "").replaceAll("\n", "");
    if (cleanFragment.startsWith("{") && cleanFragment.endsWith("}"))
      return BLOCK_LOOK_ALIKE;
    if (methodInvocationLookAlike(codeFragment))
      return EXPRESSION_LOOK_ALIKE;
    final GuessedContext $ = qfind(codeFragment);
    if ($ == null)
      throw new AssertionError(
          "GuessContext error: \n Here are the attempts I made at literal [" + codeFragment + "]:,\n\n" + enumerateFailingAttempts(codeFragment));
    return $;
  }
  public static GuessedContext qfind(final String codeFragment) {
    for (final GuessedContext $ : alternativeContextsToConsiderInThisOrder)
      if ($.contains($.intoCompilationUnit(codeFragment) + "", codeFragment) && wasActuallyInsertedToWrapper($, codeFragment))
        return $;
    return Stream.of(alternativeContextsToConsiderInThisOrder)
        .filter(λ -> λ.accurateContains(λ.intoCompilationUnit(codeFragment) + "", codeFragment) && wasActuallyInsertedToWrapper(λ, codeFragment))
        .findFirst().orElse(null);
  }
  private static boolean methodInvocationLookAlike(final String codeFragment) {
    return codeFragment.matches("[\\S]+\\(\\)");
  }
  private static boolean wasActuallyInsertedToWrapper(final GuessedContext $, final String codeFragment) {
    return !($.intoCompilationUnit("") + "").equals($.intoCompilationUnit(codeFragment) + "");
  }
  static String enumerateFailingAttempts(final String codeFragment) {
    final StringBuilder $ = new StringBuilder();
    int i = 0;
    for (final GuessedContext w : GuessedContext.alternativeContextsToConsiderInThisOrder) {
      final String on = w.on(codeFragment);
      $.append("\n\nAttempt #").append(++i).append(" (of ").append(GuessedContext.alternativeContextsToConsiderInThisOrder.length).append("):");
      $.append("\n\t\t Is it a ").append(w).append("?");
      $.append("\n\t Let's see...");
      $.append("\n\t\t What I tried as input was (essentially) this literal:");
      $.append("\n\t```").append(Trivia.essence(on)).append("'''");
      final CompilationUnit u = w.intoCompilationUnit(codeFragment);
      $.append("\n\t\t Alas, what the parser generated ").append(u.getProblems().length).append(" problems on (essentially) this bit of code");
      $.append("\n\t\t\t```").append(Trivia.essence(u + "")).append("'''");
      $.append("\n\t\t Properly formatted, this bit should look like so: ");
      $.append("\n\t\t\t```").append(u).append("'''");
      $.append("\n\t\t And the full list of problems was: ");
      $.append("\n\t\t\t```").append(wizard.problems(u)).append("'''");
    }
    return $ + "";
  }

  private final String before;
  private final String after;

  GuessedContext(final String before, final String after) {
    this.before = before;
    this.after = after;
  }
  /** Guess a given code fragment, and then parse it, converting it into a
   * {@link CompilationUnit}.
   * @param codeFragment JD
   * @return a newly created {@link CompilationUnit} representing the parsed AST
   *         of the wrapped parameter. */
  public CompilationUnit intoCompilationUnit(final String codeFragment) {
    return (CompilationUnit) makeAST.COMPILATION_UNIT.from(on(codeFragment));
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
  public String off(final String codeFragment) {
    return removeSuffix(removePrefix(codeFragment, before), after);
  }
  /** Place a wrap around a phrase
   * @param codeFragment some program phrase
   * @return wrapped phrase */
  public String on(final String codeFragment) {
    return before + codeFragment + after;
  }
  private boolean accurateContains(final String wrap, final String inner) {
    final String off = off(wrap), $ = Trivia.accurateEssence(inner), essence2 = Trivia.accurateEssence(off);
    assert essence2 != null;
    return essence2.contains($);
  }
  private boolean contains(final String wrap, final String inner) {
    final String off = off(wrap), $ = Trivia.essence(inner), essence2 = Trivia.essence(off);
    assert essence2 != null;
    return essence2.contains($);
  }
}
