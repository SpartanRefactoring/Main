package il.org.spartan.spartanizer.cmdline.applicators;

@FunctionalInterface
public interface ToInt<R> {
  int run(R r);
}