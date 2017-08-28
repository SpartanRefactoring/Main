package il.org.spartan.spartanizer.cmdline.visitor;

/** TODO Yossi Gil: document class
 * @author Yossi Gil
 * @since 2017-08-25 */
public class ExampleApplication {
  public static void main(final String[] arguments) {
    new FeaturedTraversor<>().withArguments(arguments).execute();
  }
}
