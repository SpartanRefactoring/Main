package il.org.spartan.refactoring.java;

import il.org.spartan.refactoring.annotations.*;

public class EnvironmentTestMoreCodeExamples {
  class A{
    @FlatEnvUse({@Id(name="str",clazz="String")}) void foo(){/**/}
  }
}
