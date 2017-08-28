package il.org.spartan.spartanizer.cmdline.visitor;

/** TODO Yossi Gil: document class
 * @author Yossi Gil
 * @since 2017-08-25 */
public class SimpleTraverse {
  public static void main(final String[] arguments) {
    Traverse.create().withArguments(arguments).withListener(new Traverse.E.Empty(){} ).go();
  }
}
