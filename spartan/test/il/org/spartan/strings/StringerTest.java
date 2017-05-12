package il.org.spartan.strings;

import static fluent.ly.azzert.*;

import org.junit.*;

import fluent.ly.*;
import il.org.spartan.*;

@SuppressWarnings("static-method") public class StringerTest {
  @Test public void testFixNumericalSuffix() {
    azzert.that(Stringer.fixNumericalSuffix("abc1"), is("abc0000001"));
    azzert.that(Stringer.fixNumericalSuffix("abc12"), is("abc0000012"));
    azzert.that(Stringer.fixNumericalSuffix("abc123"), is("abc0000123"));
    azzert.that(Stringer.fixNumericalSuffix("abc1234"), is("abc0001234"));
    azzert.that(Stringer.fixNumericalSuffix("abc12345"), is("abc0012345"));
    azzert.that(Stringer.fixNumericalSuffix("abc123456"), is("abc0123456"));
    azzert.that(Stringer.fixNumericalSuffix("abc1234567"), is("abc1234567"));
    azzert.that(Stringer.fixNumericalSuffix("abc12345678"), is("abc12345678"));
    azzert.that(Stringer.fixNumericalSuffix(null), is(null));
    azzert.that(Stringer.fixNumericalSuffix(""), is(""));
    azzert.that(Stringer.fixNumericalSuffix("abc"), is("abc"));
  }
}
