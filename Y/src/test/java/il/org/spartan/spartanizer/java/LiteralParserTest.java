package il.org.spartan.spartanizer.java;

import static fluent.ly.azzert.*;

import org.junit.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.engine.type.*;

@SuppressWarnings({ "static-method", "javadoc" }) //
public final class LiteralParserTest {
  @Test public void doubleBinaryLiterals() {
    azzert.that(new NumericLiteralClassifier("0x1.fffffffffffffP+1023").type(), is(Primitive.Certain.DOUBLE));
    azzert.that(new NumericLiteralClassifier("0x1.0p-1022").type(), is(Primitive.Certain.DOUBLE));
    azzert.that(new NumericLiteralClassifier("0x0.0000000000001P-1022").type(), is(Primitive.Certain.DOUBLE));
  }

  @Test public void doubleLiteralsFromSpecification() {
    azzert.that(new NumericLiteralClassifier("1e1").type(), is(Primitive.Certain.DOUBLE));
    azzert.that(new NumericLiteralClassifier("2.").type(), is(Primitive.Certain.DOUBLE));
    azzert.that(new NumericLiteralClassifier(".3").type(), is(Primitive.Certain.DOUBLE));
    azzert.that(new NumericLiteralClassifier("0.0").type(), is(Primitive.Certain.DOUBLE));
    azzert.that(new NumericLiteralClassifier("3.14").type(), is(Primitive.Certain.DOUBLE));
    azzert.that(new NumericLiteralClassifier("1e-9d").type(), is(Primitive.Certain.DOUBLE));
    azzert.that(new NumericLiteralClassifier("1e137").type(), is(Primitive.Certain.DOUBLE));
  }

  @Test public void floatLiteralsFromSpecification() {
    azzert.that(new NumericLiteralClassifier("1e1f").type(), is(Primitive.Certain.FLOAT));
    azzert.that(new NumericLiteralClassifier("2.f").type(), is(Primitive.Certain.FLOAT));
    azzert.that(new NumericLiteralClassifier(".3f").type(), is(Primitive.Certain.FLOAT));
    azzert.that(new NumericLiteralClassifier("0f").type(), is(Primitive.Certain.FLOAT));
    azzert.that(new NumericLiteralClassifier("3.14f").type(), is(Primitive.Certain.FLOAT));
    azzert.that(new NumericLiteralClassifier("6.022137e+23f").type(), is(Primitive.Certain.FLOAT));
  }

  @Test public void hasConstructor() {
    assert new NumericLiteralClassifier("a") != null;
  }

  @Test public void hasKind() {
    azzert.that(new NumericLiteralClassifier("2F").type().ordinal(), greaterThanOrEqualTo(0));
  }

  @Test public void hasVisibleValue() {
    azzert.that(new NumericLiteralClassifier("2F").inner, is("2F"));
  }

  @Test public void issue490a() {
    assert Long.parseLong("0") == 0L;
    azzert.that(new NumericLiteralClassifier("0L").type(), is(Primitive.Certain.LONG));
  }

  @Test public void issue490b() {
    azzert.that(new NumericLiteralClassifier("0x38495ab5").type(), is(Primitive.Certain.INT));
  }

  @Test public void kindCharacter() {
    azzert.that(new NumericLiteralClassifier("'l'").type(), is(Primitive.Certain.CHAR));
  }

  @Test public void kindDoubleLower() {
    azzert.that(new NumericLiteralClassifier("2d").type(), is(Primitive.Certain.DOUBLE));
  }

  @Test public void kindDoubleNoDoublePart() {
    azzert.that(new NumericLiteralClassifier("0.4").type(), is(Primitive.Certain.DOUBLE));
  }

  @Test public void kindDoubleNoFraction() {
    azzert.that(new NumericLiteralClassifier("0.").type(), is(Primitive.Certain.DOUBLE));
  }

  @Test public void kindDoublePlain() {
    azzert.that(new NumericLiteralClassifier("0.5").type(), is(Primitive.Certain.DOUBLE));
  }

  @Test public void kindDoubleUpper() {
    azzert.that(new NumericLiteralClassifier("2D").type(), is(Primitive.Certain.DOUBLE));
  }

  @Test public void kindDoubleWithExponetLower() {
    azzert.that(new NumericLiteralClassifier("0E1").type(), is(Primitive.Certain.DOUBLE));
  }

  @Test public void kindDoubleWithExponetUpper() {
    azzert.that(new NumericLiteralClassifier("0E1").type(), is(Primitive.Certain.DOUBLE));
  }

  @Test public void kindFloatLower() {
    azzert.that(new NumericLiteralClassifier("2f").type(), is(Primitive.Certain.FLOAT));
  }

  @Test public void kindFloatUpper() {
    azzert.that(new NumericLiteralClassifier("2F").type(), is(Primitive.Certain.FLOAT));
  }

  @Test public void kindInteger() {
    azzert.that(new NumericLiteralClassifier("22").type(), is(Primitive.Certain.INT));
  }

  @Test public void kindLongLower() {
    azzert.that(new NumericLiteralClassifier("22l").type(), is(Primitive.Certain.LONG));
  }

  @Test public void kindLongUpper() {
    azzert.that(new NumericLiteralClassifier("22L").type(), is(Primitive.Certain.LONG));
  }
}
