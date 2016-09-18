package il.org.spartan.spartanizer.java;

import static il.org.spartan.azzert.*;

import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.engine.type.*;

@SuppressWarnings({ "static-method", "javadoc" }) public final class LiteralParserTest {
  @Test public void doubleBinaryLiterals() {
    azzert.that(new LiteralParser("0x1.fffffffffffffP+1023").type(), is(Primitive.Certain.DOUBLE));
    azzert.that(new LiteralParser("0x1.0p-1022").type(), is(Primitive.Certain.DOUBLE));
    azzert.that(new LiteralParser("0x0.0000000000001P-1022").type(), is(Primitive.Certain.DOUBLE));
  }

  @Test public void doubleLiteralsFromSpecification() {
    azzert.that(new LiteralParser("1e1").type(), is(Primitive.Certain.DOUBLE));
    azzert.that(new LiteralParser("2.").type(), is(Primitive.Certain.DOUBLE));
    azzert.that(new LiteralParser(".3").type(), is(Primitive.Certain.DOUBLE));
    azzert.that(new LiteralParser("0.0").type(), is(Primitive.Certain.DOUBLE));
    azzert.that(new LiteralParser("3.14").type(), is(Primitive.Certain.DOUBLE));
    azzert.that(new LiteralParser("1e-9d").type(), is(Primitive.Certain.DOUBLE));
    azzert.that(new LiteralParser("1e137").type(), is(Primitive.Certain.DOUBLE));
  }

  @Test public void floatLiteralsFromSpecification() {
    azzert.that(new LiteralParser("1e1f").type(), is(Primitive.Certain.FLOAT));
    azzert.that(new LiteralParser("2.f").type(), is(Primitive.Certain.FLOAT));
    azzert.that(new LiteralParser(".3f").type(), is(Primitive.Certain.FLOAT));
    azzert.that(new LiteralParser("0f").type(), is(Primitive.Certain.FLOAT));
    azzert.that(new LiteralParser("3.14f").type(), is(Primitive.Certain.FLOAT));
    azzert.that(new LiteralParser("6.022137e+23f").type(), is(Primitive.Certain.FLOAT));
  }

  @Test public void hasConstructor() {
    assert new LiteralParser("a") != null;
  }

  @Test public void hasKind() {
    azzert.that(new LiteralParser("2F").type().ordinal(), greaterThanOrEqualTo(0));
  }

  @Test public void hasVisibleValue() {
    azzert.that(new LiteralParser("2F").literal, is("2F"));
  }

  @Test public void kindCharacter() {
    azzert.that(new LiteralParser("'l'").type(), is(Primitive.Certain.CHAR));
  }

  @Test public void kindDoubleLower() {
    azzert.that(new LiteralParser("2d").type(), is(Primitive.Certain.DOUBLE));
  }

  @Test public void kindDoubleNoDoublePart() {
    azzert.that(new LiteralParser("0.4").type(), is(Primitive.Certain.DOUBLE));
  }

  @Test public void kindDoubleNoFraction() {
    azzert.that(new LiteralParser("0.").type(), is(Primitive.Certain.DOUBLE));
  }

  @Test public void kindDoublePlain() {
    azzert.that(new LiteralParser("0.5").type(), is(Primitive.Certain.DOUBLE));
  }

  @Test public void kindDoubleUpper() {
    azzert.that(new LiteralParser("2D").type(), is(Primitive.Certain.DOUBLE));
  }

  @Test public void kindDoubleWithExponetLower() {
    azzert.that(new LiteralParser("0E1").type(), is(Primitive.Certain.DOUBLE));
  }

  @Test public void kindDoubleWithExponetUpper() {
    azzert.that(new LiteralParser("0E1").type(), is(Primitive.Certain.DOUBLE));
  }

  @Test public void kindFloatLower() {
    azzert.that(new LiteralParser("2f").type(), is(Primitive.Certain.FLOAT));
  }

  @Test public void kindFloatUpper() {
    azzert.that(new LiteralParser("2F").type(), is(Primitive.Certain.FLOAT));
  }

  @Test public void kindInteger() {
    azzert.that(new LiteralParser("22").type(), is(Primitive.Certain.INT));
  }

  @Test public void kindLongLower() {
    azzert.that(new LiteralParser("22l").type(), is(Primitive.Certain.LONG));
  }

  @Test public void kindLongUpper() {
    azzert.that(new LiteralParser("22L").type(), is(Primitive.Certain.LONG));
  }
}