package il.org.spartan.utils;

import java.util.function.*;

public enum Truth {
  T("true"), //
  F("false"), //
  X("Assertion exception"), //
  N("Null pointer exception"), //
  R("Runtime exception"), //
  Ħ("A throwable of unknown kind");
  public static Truth truthOf(BooleanSupplier $) {
    try {
      return $.getAsBoolean() ? T : F;
    } catch (NullPointerException __) {
      return N;
    } catch (AssertionError __) {
      return X;
    } catch (RuntimeException __) {
      return R;
    } catch (Throwable __) {
      return Ħ;
    }
  }

  Truth not() {
    return this == T ? F : //
        this == F ? T : //
            this;
  }

  Truth or(Truth other) {
    return this == T ? this : other;
  }

  Truth and(Truth other) {
    return this == F ? this : other;
  }

  public static String letterOf(BooleanSupplier ¢) {
    return truthOf(¢) + "";
  }

  public String description;

  Truth(String description) {
    this.description = description;
  }

  @Override public String toString() {
    return description;
  }
}
