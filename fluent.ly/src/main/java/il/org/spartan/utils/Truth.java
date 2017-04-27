package il.org.spartan.utils;

import java.util.function.*;

public enum Truth {
  T("true"), //
  F("false"), //
  A("Assertion exception"), //
  N("Null pointer exception"), //
  X("Runtime exception"), //
  Ħ("A throwable of unknown kind");
  public static Truth truthOf(BooleanSupplier s) {
    try {
      return s.getAsBoolean() ? T : F;
    } catch (NullPointerException __) {
      return N;
    } catch (AssertionError __) {
      return A;
    } catch (RuntimeException __) {
      return X;
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

  public static String letterOf(BooleanSupplier s) {
    return truthOf(s) + "";
  }

  private String description;

  Truth(String description) {
    this.description = description;
  }
}
