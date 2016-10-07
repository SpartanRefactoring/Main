package il.org.spartan.spartanizer.dispatch;

/** Auxiliary type: currently unused, will be used when we have categories of
 * nano-patterns */
public interface Nanos extends TipperCategory {
  final String label = "Nano";

  @Override default String description() {
    return label;
  }
}