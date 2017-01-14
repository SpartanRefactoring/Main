package il.org.spartan.spartanizer.java.namespace;

import il.org.spartan.spartanizer.annotations.*;

final class EnvironmentTestMoreCodeExamples {
  class A {
    @FlatEnvUse({ @Id(name = "str", clazz = "String") }) void foo() {
      /**/}
  }

  {
    new A().hashCode();
  }
}
