package il.org.spartan.spartanizer.cmdline;

/** Scans files named by folder, ignore test files, and collect statistics, on
 * classes, methods, etc.
 * @autho/home/matteo/MUTATION_TESTING/test-spartanizer/projects/commons-bcelr Yossi Gil
 * @year 2015 */
public final class Spartanizer extends AbstractBatch {
  public static void main(final String[] args) {
    for (final String ¢ : args.length != 0 ? args : new String[] { "." })
      new Spartanizer(¢).fire();
  }

  Spartanizer(final String path) {
    super(path);
  }
}
