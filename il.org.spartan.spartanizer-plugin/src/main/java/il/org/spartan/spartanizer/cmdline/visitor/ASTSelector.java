package il.org.spartan.spartanizer.cmdline.visitor;

/** TODO Yossi Gil: document class 
 * 
 * @author Yossi Gil
 * @since 2017-08-25 */
public class ASTSelector extends ASTTraversor {

  public ASTSelector(String[] arguments) {
    super(arguments);
    withVisitor(new Visitor() {

      @Override public void visitFile() {}

    }
    );
  }
  
}
