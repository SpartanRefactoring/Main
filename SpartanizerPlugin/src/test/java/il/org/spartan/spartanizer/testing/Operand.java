package il.org.spartan.spartanizer.testing;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.testing.TestUtilsAll.*;
import static il.org.spartan.spartanizer.utils.Wrap.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.utils.*;

public class Operand extends Wrapper<String> {
  private static final String QUICK = "Quick fix by MARK, COPY, PASTE, and REFORMAT is:\n";
  private final Trimmer trimmer;

  public Operand(final String inner) {
    super(inner);
    trimmer = new Trimmer();
  }

  void checkExpected(final String expected) {
    final Wrap w = Wrap.find(get());
    final String wrap = w.on(get()), unpeeled = trim.apply(new Trimmer(), wrap);
    if (wrap.equals(unpeeled))
      azzert.fail("Nothing done on " + get());
    final String peeled = w.off(unpeeled);
    if (peeled.equals(get()))
      azzert.that("No trimming of " + get(), peeled, is(not(get())));
    if (tide.clean(peeled).equals(tide.clean(get())))
      azzert.that("Trimming of " + get() + "is just reformatting", tide.clean(get()), is(not(tide.clean(peeled))));
    assertSimilar(expected, peeled);
  }

  public void doesNotCrash() {
    try {
      apply();
    } catch (Throwable ¢) {
      System.err.println("*** Test crashed with " + ¢.getClass().getSimpleName());
      System.err.println("*** Test crashed with " + ¢.getMessage());
      System.err.println("*** Test crashed rerunning " );
      monitor.now = monitor.INTERACTIVE_TDD;
      apply();
    }
  }

  public String apply() {
    return trim.apply(trimmer, Wrap.find(get()).on(get()));
  }

  public Operand gives(final String $) {
    final Wrap w = Wrap.find(get());
    final String wrap = w.on(get()), unpeeled = trim.apply(trimmer, wrap);
    if (wrap.equals(unpeeled)) {
      rerun();
      copyPasteReformat("stays()//\n  ;");
      azzert.fail("Nothing done on " + get());
    }
    final String peeled = w.off(unpeeled);
    if (peeled.equals(get()))
      azzert.that("No trimming of " + get(), peeled, is(not(get())));
    if (tide.clean(peeled).equals(tide.clean(get())))
      azzert.that("Trimming of " + get() + "is just reformatting", tide.clean(get()), is(not(tide.clean(peeled))));
    if (!$.equals(peeled) && !essence(peeled).equals(essence($))) {
      System.err.printf(//
          QUICK + ":\n" + //
              "\n        .gives(\"%s\") //\n\n\n" + //
              "Compare with current " + //
              "\n        .gives(\"%s\") //\n",
          trivia.escapeQuotes(essence(peeled)), //
          trivia.escapeQuotes(essence($)));
      azzert.that(essence(peeled), is(essence($)));
    }
    return new Operand($);
  }

  private static void copyPasteReformat(String format, Object... os) {
    System.err.printf(QUICK + format, os);
  }

  /** Check whether one of the code options is correct
   * @param options
   * @return Operand
   * @author Dor Ma'ayan
   * @since 09-12-2016 */
  public Operand givesEither(final String... options) {
    assert options != null;
    final Wrap w = Wrap.find(get());
    final String wrap = w.on(get()), unpeeled = trim.apply(trimmer, wrap);
    if (wrap.equals(unpeeled))
      azzert.fail("Nothing done on " + get());
    final String peeled = w.off(unpeeled);
    if (peeled.equals(get()))
      azzert.that("No trimming of " + get(), peeled, is(not(get())));
    if (tide.clean(peeled).equals(tide.clean(get())))
      azzert.that("Trimming of " + get() + "is just reformatting", tide.clean(get()), is(not(tide.clean(peeled))));
    for (final String $ : options)
      if (essence($).equals(essence(peeled)))
        return new Operand($);
    azzert.fail("Expects: " + peeled + " But None of the given options match");
    return null;
  }

  public void stays() {
    final Wrap w = Wrap.find(get());
    final String wrap = w.on(get()), unpeeled = trim.apply(trimmer, wrap);
    if (wrap.equals(unpeeled))
      return;
    final String peeled = w.off(unpeeled);
    if (peeled.equals(get()) || tide.clean(peeled).equals(tide.clean(get())))
      return;
    final String expected = get();
    if (expected.equals(peeled) || essence(peeled).equals(essence(expected)))
      return;
    rerun();
    copyPasteReformat(
        "\n .gives(\"%s\") //\nCompare with\n .gives(\"%s\") //\n", //
        essence(peeled), //
        essence(expected));
    azzert.that(essence(peeled), is(essence(expected)));
  }

  private void rerun() {
    System.err.println("*** Test failed (rerunning to collect more information)");
    TrimmerLog.on();
    monitor.now = monitor.INTERACTIVE_TDD;
    apply();
    TrimmerLog.off();
    System.err.println("*** Rerun done. (scroll back to find logging infromation)");
  }

  public <N extends ASTNode> Operand using(final Class<N> c, final Tipper<N> ¢) {
    trimmer.fix(c, ¢);
    return this;
  }

  @SafeVarargs public final <N extends ASTNode> Operand using(final Class<N> c, final Tipper<N>... ts) {
    as.list(ts).forEach(λ -> trimmer.addSingleTipper(c, λ));
    return this;
  }
}