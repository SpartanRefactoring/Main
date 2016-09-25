package il.org.spartan.spartanizer.java;

import org.junit.*;

import il.org.spartan.spartanizer.annotations.*;

@Ignore("This should never be furn") @SuppressWarnings("all") public final class EnvironmentCodeExamples {
  static class EX02 {
    int x = 1;
    @FlatEnvUse({ @Id(name = "x", clazz = "int") }) int y;
  }
}
