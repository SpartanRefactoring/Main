package il.org.spartan.spartanizer.meta;

import static il.org.spartan.azzert.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.junit.runners.Parameterized.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.java.namespace.*;
import il.org.spartan.spartanizer.tippers.*;
import il.org.spartan.spartanizer.utils.*;

/** A demo on testing with a {!@link {@link MetaFixture}
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2017-01-17 */
public class SentenceTestTemplate {
  public static final Trimmer trimmer = new Trimmer();

  static List<List<MethodDeclaration>> allSentences() {
    return collectSentences(new Issue1090());
  }

  static List<List<MethodDeclaration>> collectSentences(final MetaFixture... fs) {
    final List<List<MethodDeclaration>> $ = new ArrayList<>();
    for (final MetaFixture f : fs)
      for (final AnonymousClassDeclaration d : searchDescendants.forClass(AnonymousClassDeclaration.class).from(f.reflectedCompilationUnit())) {
        final Vocabulary reify = MetaTestCase.reify(d);
        if (reify != null)
          $.add(new ArrayList<>(reify.values()));
      }
    return $;
  }

  /** A phrase is made of two consecutive words. If a sentence has n words, then
   * it has n-1 phrases.
   * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
   * @since 2017-01-18 */
  @Ignore
  @RunWith(Parameterized.class)
  public static class Changes {
    @Parameter(1) public MethodDeclaration changes;
    @Parameter(0) public String name;

    @Test public void changes() {
      final String from = changes + "";
      final String wrap = Wrap.Method.on(from);
      final String unpeeled = TrimmerTestsUtils.applyTrimmer(trimmer, wrap);
      azzert.that("Nothing done on " + name, wrap, is(not(unpeeled)));
      final String peeled = Wrap.Method.off(unpeeled);
      azzert.that("No trimming of " + name, peeled, is(not(from)));
      azzert.that("Trimming of " + name + " is just reformatting", tide.clean(from), is(not(tide.clean(peeled))));
    }

    @Parameters(name = "{index}. {0} ") public static Collection<Object[]> ____() {
      final Collection<Object[]> $ = new ArrayList<>();
      for (final List<MethodDeclaration> sentence : allSentences())
        for (final MethodDeclaration ¢ : sentence)
          if (disabling.specificallyDisabled(¢))
            $.add(____(¢));
      return $;
    }

    public static Object[] ____(final MethodDeclaration changes) {
      return new Object[] { changes.getName() + "", changes };
    }
  }

  /** A phrase is made of two consecutive words. If a sentence has n words, then
   * it has n-1 phrases.
   * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
   * @since 2017-01-18 */
  @Ignore
  @RunWith(Parameterized.class)
  public static class ChangesTo {
    @Parameter(1) public MethodDeclaration first;
    @Parameter(0) public String name;
    @Parameter(2) public MethodDeclaration second;

    @Test public void chagesTo() {
      final String peeled = Wrap.Method.off(TrimmerTestsUtils.applyTrimmer(trimmer, Wrap.Method.on(firstBody())));
      final String to = secondBody();
      if (!to.equals(peeled))
        azzert.that(Wrap.essence(peeled), is(Wrap.essence(to)));
    }

    String firstBody() {
      return (first + "").replaceAll(disabling.disabler, "");
    }

    String firstName() {
      return first.getName() + "";
    }

    String secondBody() {
      return (second + "").replaceAll(secondName(), firstName()).replaceAll(disabling.disabler, "");
    }

    String secondName() {
      return second.getName() + "";
    }

    @Parameters(name = "{index}. {0} ") public static Collection<Object[]> ____() {
      final Collection<Object[]> $ = new ArrayList<>();
      for (final List<MethodDeclaration> sentence : allSentences())
        for (int ¢ = 0; ¢ < sentence.size() - 1; ++¢)
          if (disabling.specificallyDisabled(sentence.get(¢)))
            $.add(____(sentence.get(¢), sentence.get(¢ + 1)));
      return $;
    }

    public static Object[] ____(final MethodDeclaration from, final MethodDeclaration to) {
      return new Object[] { from.getName() + " -> " + to.getName(), from, to, };
    }
  }

  /** A period is a any word in a sentence whose method does not have a
   * {@link disabling} label in its javaDoc.
   * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
   * @since 2017-01-18 */
  @Ignore
  @RunWith(Parameterized.class)
  public static class Stays {
    @Parameter(0) public String name;
    @Parameter(1) public MethodDeclaration stays;

    @Test public void stays() {
      final String from = stays + "";
      final String wrap = Wrap.Method.on(from);
      final String unpeeled = TrimmerTestsUtils.applyTrimmer(trimmer, wrap);
      if (wrap.equals(unpeeled))
        return;
      final String peeled = Wrap.Method.off(unpeeled);
      if (!peeled.equals(from) && !tide.clean(peeled).equals(tide.clean(from)))
        azzert.that(Wrap.essence(peeled), is(Wrap.essence(from)));
    }

    @Parameters(name = "{index}. {0} ") public static Collection<Object[]> ____() {
      final Collection<Object[]> $ = new ArrayList<>();
      for (final List<MethodDeclaration> sentence : allSentences())
        for (final MethodDeclaration ¢ : sentence)
          if (!disabling.specificallyDisabled(¢))
            $.add(____(¢));
      return $;
    }

    public static Object[] ____(final MethodDeclaration stays) {
      return new Object[] { stays.getName() + "", stays, };
    }
  }
}
