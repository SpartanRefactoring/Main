package il.org.spartan.spartanizer.research;

/** A marker to mark an ASTNode as matched by a NanoPattern.
 * @author Ori Marcovitch
 * @since 2016 */
public class Marker {
  public final NanoPattern np;
  public static final String AST_PROPERTY_NAME_NP_LIST = "MARKER";

  public Marker(final NanoPattern np) {
    this.np = np;
  }
}
