package il.org.spartan.refactoring.utils;

import static il.org.spartan.azzert.*;
import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.refactoring.utils.Into.*;
import static il.org.spartan.refactoring.utils.extract.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.*;

@SuppressWarnings("static-method") public class TermsReogranizerTest {
  @Test public void seriesA_01() {
    azzert.that(TermsReogranizer.simplify(i("a+b+c")), iz("a + b + c"));
  }

  @Test public void seriesA_02() {
    azzert.that(TermsReogranizer.simplify(i("a-c")), iz("a-c"));
  }

  @Test public void seriesA_03() {
    azzert.that(TermsReogranizer.simplify(i("a + (b+c)")), iz("a + b+c"));
  }

  @Test public void seriesA_04() {
    azzert.that(TermsReogranizer.simplify(i("a + (b-c)")), iz("a + b-c"));
  }

  @Test public void seriesA_05() {
    azzert.that(TermsReogranizer.simplify(i("a + b -c + d -e + f")), iz("a + b + d + f -c -e"));
  }

  @Test public void seriesA_06() {
    azzert.that(TermsReogranizer.simplify(i("a + (b + c + d)")), iz("a + b + c + d"));
  }

  @Test public void seriesA_07() {
    azzert.that(TermsReogranizer.simplify(i("a - (c -d)")), iz("a + d -c"));
  }

  @Test public void seriesA_08() {
    azzert.that(TermsReogranizer.simplify(i("a + -a")), iz("a - a"));
  }

  @Test public void seriesA_09() {
    azzert.that(TermsReogranizer.simplify(i("+a + +a")), iz("a + a"));
  }

  @Test public void seriesA_10() {
    azzert.that(TermsReogranizer.simplify(i("a - -b")), iz("a + b"));
  }

  @Test public void seriesA_11() {
    azzert.that(TermsReogranizer.simplify(i("a + - - (b + c)")), iz("a + b + c"));
  }

  @Test public void seriesA_12() {
    azzert.that(TermsReogranizer.simplify(i("a + + - - - (b -c)")), iz("a +c -b"));
  }
  @Test public void seriesA_13() {
    azzert.that(TermsReogranizer.simplify(i("-a + + - - - (b -c)")), iz("c -b -a"));
  }
}
