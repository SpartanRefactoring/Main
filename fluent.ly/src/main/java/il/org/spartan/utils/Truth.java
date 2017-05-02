package il.org.spartan.utils;

import java.util.function.*;

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
    } catch (final NullPointerException __) {
      return N;
    } catch (final AssertionError __) {
      return X;
    } catch (final RuntimeException __) {
      return R;
    } catch (final Throwable __) {
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
