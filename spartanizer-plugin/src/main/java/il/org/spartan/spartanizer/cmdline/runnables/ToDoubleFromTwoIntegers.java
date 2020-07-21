package il.org.spartan.spartanizer.cmdline.runnables;

@FunctionalInterface
public interface ToDoubleFromTwoIntegers {
  double apply(int i1, int i2);
}