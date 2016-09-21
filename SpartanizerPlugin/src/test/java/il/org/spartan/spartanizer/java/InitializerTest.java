package il.org.spartan.spartanizer.java;

// TODO: Yossi, did you want me to play with initializers? I moved this from the
// ToolBox. Didn't finish yet.
public final class InitializerTest {
  static {
    ++InitializerTest.a;
    System.out.println("Static initializer end (0).");
    // Seems that we can't do here anything that is connected to the instance.
    // But can be used for initializing some global data structures which are
    // referenced by
    // the instances of this class...
  }
  static int a;

  public static void main(final String[] __) {
    new InitializerTest(3).hashCode();
    System.out.print(InitializerTest.a);
  }

  {
    // b is not recognized here.
    ++a;
    // a is updated here instead of being incremented in all the constructors.
    System.out.println("Instance initializer end (1).");
  }

  InitializerTest() {
    // b is not recognized here.
    System.out.println("Empty Constructor");
  }

  InitializerTest(final int i) {
    System.out.println("int constructor" + i);
  }

  InitializerTest(final String s) {
    // b is not recognized here.
    System.out.println("String constructor" + s);
  }
}