package il.org.spartan.refactoring.utils;

import static il.org.spartan.hamcrest.SpartanAssert.*;
import static org.hamcrest.text.IsEqualIgnoringWhiteSpace.*;
import static org.junit.Assert.*;
import il.org.spartan.refactoring.utils.LiteralParser.Types;

import org.junit.*;

@SuppressWarnings({ "static-method", "javadoc" }) public class LiteralParserTest {
  @Test public void doubleBinaryLiterals() {
    assertThat(new LiteralParser("0x1.fffffffffffffP+1023").type(), is(Types.DOUBLE.ordinal()));
    assertThat(new LiteralParser("0x1.0p-1022").type(), is(Types.DOUBLE.ordinal()));
    assertThat(new LiteralParser("0x0.0000000000001P-1022").type(), is(Types.DOUBLE.ordinal()));
  }
  @Test public void doubleLiteralsFromSpecification() {
    assertThat(new LiteralParser("1e1").type(), is(Types.DOUBLE.ordinal()));
    assertThat(new LiteralParser("2.").type(), is(Types.DOUBLE.ordinal()));
    assertThat(new LiteralParser(".3").type(), is(Types.DOUBLE.ordinal()));
    assertThat(new LiteralParser("0.0").type(), is(Types.DOUBLE.ordinal()));
    assertThat(new LiteralParser("3.14").type(), is(Types.DOUBLE.ordinal()));
    assertThat(new LiteralParser("1e-9d").type(), is(Types.DOUBLE.ordinal()));
    assertThat(new LiteralParser("1e137").type(), is(Types.DOUBLE.ordinal()));
  }
  @Test public void floatLiteralsFromSpecification() {
    assertThat(new LiteralParser("1e1f").type(), is(Types.FLOAT.ordinal()));
    assertThat(new LiteralParser("2.f").type(), is(Types.FLOAT.ordinal()));
    assertThat(new LiteralParser(".3f").type(), is(Types.FLOAT.ordinal()));
    assertThat(new LiteralParser("0f").type(), is(Types.FLOAT.ordinal()));
    assertThat(new LiteralParser("3.14f").type(), is(Types.FLOAT.ordinal()));
    assertThat(new LiteralParser("6.022137e+23f").type(), is(Types.FLOAT.ordinal()));
  }
  @Test public void hasConstructor() {
    assertThat(new LiteralParser("a"), notNullValue());
  }
  @Test public void hasKind() {
    assertThat(new LiteralParser("2F").type(), greaterThanOrEqualTo(0));
  }
  @Test public void hasVisibleValue() {
    assertThat(new LiteralParser("2F").literal, equalToIgnoringWhiteSpace("2F"));
  }
  @Test public void kindCharacter() {
    assertThat(new LiteralParser("'l'").type(), is(Types.CHARACTER.ordinal()));
  }
  @Test public void kindDoubleLower() {
    assertThat(new LiteralParser("2d").type(), is(Types.DOUBLE.ordinal()));
  }
  @Test public void kindDoubleNoDoublePart() {
    assertThat(new LiteralParser("0.4").type(), is(Types.DOUBLE.ordinal()));
  }
  @Test public void kindDoubleNoFraction() {
    assertThat(new LiteralParser("0.").type(), is(Types.DOUBLE.ordinal()));
  }
  @Test public void kindDoublePlain() {
    assertThat(new LiteralParser("0.5").type(), is(Types.DOUBLE.ordinal()));
  }
  @Test public void kindDoubleUpper() {
    assertThat(new LiteralParser("2D").type(), is(Types.DOUBLE.ordinal()));
  }
  @Test public void kindDoubleWithExponetLower() {
    assertThat(new LiteralParser("0E1").type(), is(Types.DOUBLE.ordinal()));
  }
  @Test public void kindDoubleWithExponetUpper() {
    assertThat(new LiteralParser("0E1").type(), is(Types.DOUBLE.ordinal()));
  }
  @Test public void kindFloatLower() {
    assertThat(new LiteralParser("2f").type(), is(Types.FLOAT.ordinal()));
  }
  @Test public void kindFloatUpper() {
    assertThat(new LiteralParser("2F").type(), is(Types.FLOAT.ordinal()));
  }
  @Test public void kindInteger() {
    assertThat(new LiteralParser("22").type(), is(Types.INTEGER.ordinal()));
  }
  @Test public void kindLongLower() {
    assertThat(new LiteralParser("22l").type(), is(Types.LONG.ordinal()));
  }
  @Test public void kindLongUpper() {
    assertThat(new LiteralParser("22L").type(), is(Types.LONG.ordinal()));
  }
}