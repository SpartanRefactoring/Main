package il.org.spartan.spartanizer.utils.tdd;

import java.util.*;



import org.eclipse.jdt.core.dom.*;

import il.org.spartan.classfiles.reify.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** @author Ori Marcovitch
 * @author Dor Ma'ayan
 * @author Raviv Rachmiel
 * @author Kfir Marx
 * @author Omri Ben- Shmuel
 * @since Oct 31, 2016 */
public enum getAll {
  ;
  /** Get all the methods invoked in m
   * @author Dor Ma'ayan
   * @param d JD
   * @return List of the names of the methods */
  public static Set<String> invocations(final MethodDeclaration ¢) {
    if (¢ == null)
      return null;
    Set<String> $ = new TreeSet<>();
    if (¢.getBody().statements().isEmpty())
      return $;
    ¢.accept(new ASTVisitor() {
      @Override public boolean visit(MethodInvocation m) {
        $.add(m.getName().toString());
        return true;
      }
    });
    return $;
  }

  /** Get list of names in a Block
   * @author Raviv Rachmiel
   * @author Kfir Marx
   * @param ¢ Block
   * @return List of the names in the block */
  public static List<Name> names(final Block b) {
    if(b==null) {
      return null;
    }
    List<Name> names = new ArrayList<>();
    b.accept(new ASTVisitor() {
      @Override public boolean visit(SimpleName n) {
        names.add(n);
        return true;
      }
    });
    return names;
  }
  
  
  /** returns a list of all instances of expressions at given method
   * @author Koby Ben Shimol
   * @author Yuval Simon
   * @since 16-11-01 */
  public void instanceofs(MethodDeclaration m){
    
  }
}
