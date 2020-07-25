package il.org.spartan.utils;

import java.util.function.BooleanSupplier;

import fluent.ly.forget;

public enum Truth {
  T("true"), //
  F("false"), //
  X("Assertion exception"), //
  N("Null pointer exception"), //
  R("Runtime exception"), //
  Ħ("Throwable of some other kind");

  public static Truth truthOf(final BooleanSupplier $) {
    try {
      return $.getAsBoolean() ? T : F;
    } catch (final NullPointerException x) {
      forget.it(x);
      return N;
    } catch (final AssertionError x) {
      forget.it(x);
      return X;
    } catch (final RuntimeException x) {
      forget.it(x);
      return R;
    } catch (final Throwable x) {
      forget.it(x);
      return Ħ;
    }
  }

  Truth not() {
    return this == T ? F : //
        this == F ? T : //
            this;
  }

  Truth or(final Truth other) {
    return this == T ? this : other;
  }

  Truth and(final Truth other) {
    return this == F ? this : other;
  }

  public static String letterOf(final BooleanSupplier ¢) {
    return truthOf(¢) + "";
  }

  public String description;

  Truth(final String description) {
    this.description = description;
  }

  @Override public String toString() {
    return description;
  }
}
