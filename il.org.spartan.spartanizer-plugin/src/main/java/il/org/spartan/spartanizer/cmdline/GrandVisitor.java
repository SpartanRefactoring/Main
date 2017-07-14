package il.org.spartan.spartanizer.cmdline;

/** Parse and AST visit all Java files under a given path.
 * <p>
 * @author Yossi Gil
 * @since 2017-03-09 */
public class GrandVisitor extends JavaProductionFilesVisitor {
  public GrandVisitor() {
    this(null);
  }
  public GrandVisitor(final String[] args) {
    super(args);
  }
}