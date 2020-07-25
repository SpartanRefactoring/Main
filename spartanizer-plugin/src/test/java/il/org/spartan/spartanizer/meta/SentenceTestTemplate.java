package il.org.spartan.spartanizer.meta;

import static fluent.ly.azzert.is;
import static fluent.ly.azzert.not;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import fluent.ly.azzert;
import il.org.spartan.tide;
import il.org.spartan.spartanizer.ast.navigate.descendants;
import il.org.spartan.spartanizer.engine.nominal.Trivia;
import il.org.spartan.spartanizer.issues.Issue1008;
import il.org.spartan.spartanizer.testing.trim;
import il.org.spartan.spartanizer.traversal.Traversal;
import il.org.spartan.spartanizer.traversal.TraversalImplementation;
import il.org.spartan.spartanizer.traversal.disabling;
import il.org.spartan.spartanizer.utils.WrapIntoComilationUnit;

/** A demo on testing with a {!@link {@link MetaFixture}
 * @author Yossi Gil
 * @since 2017-01-17 */
public enum SentenceTestTemplate {
  ;
  public static final Traversal traversal = new TraversalImplementation();

  static Iterable<List<MethodDeclaration>> allSentences() {
    return collectSentences(new Issue1008());
  }
  static Iterable<List<MethodDeclaration>> collectSentences(final MetaFixture... ¢) {
    return Stream.of(¢).flatMap(λ -> descendants.whoseClassIs(AnonymousClassDeclaration.class).from(λ.reflectedCompilationUnit()).stream())
        .map(AlphabeticallySortedSentence::reify).filter(Objects::nonNull).map(λ -> new ArrayList<>(λ.values())).collect(toList());
  }

  /** A phrase is made of two consecutive words. If a sentence has n words, then
   * it has n-1 phrases.
   * @author Yossi Gil
   * @since 2017-01-18 */
  @RunWith(Parameterized.class)
  public static class Changes {
    @Parameters(name = "{index}. {0} ") public static Collection<Object[]> ____() {
      final Collection<Object[]> $ = an.empty.list();
      allSentences().forEach(λ -> $.addAll(λ.stream().filter(disabling::specificallyDisabled).map(Changes::____).collect(toList())));
      return $;
    }
    public static Object[] ____(final MethodDeclaration changes) {
      return new Object[] { changes.getName() + "", changes };
    }

    @Parameter(1) @SuppressWarnings("CanBeFinal") public MethodDeclaration changes;
    @Parameter(0) @SuppressWarnings("CanBeFinal") public String name;

    @Ignore @Test public void changes() {
      final String from = changes + "", wrap = WrapIntoComilationUnit.Method.on(from), unpeeled = trim.apply(traversal, wrap);
      azzert.that("Nothing done on " + name, wrap, is(not(unpeeled)));
      final String peeled = WrapIntoComilationUnit.Method.off(unpeeled);
      azzert.that("No trimming of " + name, peeled, is(not(from)));
      azzert.that("Trimming of " + name + " is just reformatting", tide.clean(from), is(not(tide.clean(peeled))));
    }
  }

  /** A phrase is made of two consecutive words. If a sentence has n words, then
   * it has n-1 phrases.
   * @author Yossi Gil
   * @since 2017-01-18 */
  @Ignore
  @RunWith(Parameterized.class)
  public static class ChangesTo {
    @Parameters(name = "{index}. {0} ") public static Collection<Object[]> ____() {
      final Collection<Object[]> $ = an.empty.list();
      for (final List<MethodDeclaration> sentence : allSentences())
        for (int ¢ = 0; ¢ < sentence.size() - 1; ++¢)
          if (disabling.specificallyDisabled(sentence.get(¢)))
            $.add(____(sentence.get(¢), sentence.get(¢ + 1)));
      return $;
    }
    public static Object[] ____(final MethodDeclaration from, final MethodDeclaration to) {
      return new Object[] { from.getName() + " -> " + to.getName(), from, to, };
    }

    @Parameter(0) @SuppressWarnings("CanBeFinal") public String _0name;
    @Parameter(1) @SuppressWarnings("CanBeFinal") public MethodDeclaration _1first;
    @Parameter(2) @SuppressWarnings("CanBeFinal") public MethodDeclaration _2second;

    @Test public void chagesTo() {
      final String peeled = WrapIntoComilationUnit.Method.off(trim.apply(traversal, WrapIntoComilationUnit.Method.on(firstBody()))),
          to = secondBody();
      if (!to.equals(peeled))
        azzert.that(Trivia.essence(peeled), is(Trivia.essence(to)));
    }
    String firstBody() {
      return (_1first + "").replace(disabling.ByComment.disabler, "");
    }
    CharSequence firstName() {
      return _1first.getName() + "";
    }
    String secondBody() {
      return (_2second + "").replace(secondName(), firstName()).replace(disabling.ByComment.disabler, "");
    }
    CharSequence secondName() {
      return _2second.getName() + "";
    }
  }

  /** A period is a any word in a sentence whose method does not have a
   * {@link disabling} label in its javaDoc.
   * @author Yossi Gil
   * @since 2017-01-18 */
  @RunWith(Parameterized.class)
  public static class Stays {
    @Parameters(name = "{index}. {0} ") public static Collection<Object[]> ____() {
      final Collection<Object[]> $ = an.empty.list();
      allSentences()
          .forEach(sentence -> $.addAll(sentence.stream().filter(λ -> !disabling.specificallyDisabled(λ)).map(Stays::____).collect(toList())));
      return $;
    }
    public static Object[] ____(final MethodDeclaration stays) {
      return new Object[] { stays.getName() + "", stays, };
    }

    @Parameter(0) public String name;
    @Parameter(1) @SuppressWarnings("CanBeFinal") public MethodDeclaration stays;

    @Test public void stays() {
      final String from = stays + "", wrap = WrapIntoComilationUnit.Method.on(from), unpeeled = trim.apply(traversal, wrap);
      if (wrap.equals(unpeeled))
        return;
      final String peeled = WrapIntoComilationUnit.Method.off(unpeeled);
      if (!peeled.equals(from) && !tide.clean(peeled).equals(tide.clean(from)))
        azzert.that(Trivia.essence(peeled), is(Trivia.essence(from)));
    }
  }
}
