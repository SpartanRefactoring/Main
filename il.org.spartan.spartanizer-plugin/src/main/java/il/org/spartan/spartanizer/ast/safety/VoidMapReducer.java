package il.org.spartan.spartanizer.ast.safety;

/** TODO Yossi Gil: document class 
 * 
 * @author Yossi Gil
 * @since 2017-07-13 */
public class VoidMapReducer extends ASTMapReducer<Void> {
  @Override public Void reduce() {
    return null;
  }
  @Override public Void reduce(Void r1, Void r2) {
    return null;
  }
}
