package il.org.spartan.spartanizer.utils.tdd;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.safety.*;

/** @author AnnaBel7
 * @author michalcohen
 * @since Nov 4, 2016 */
// TODO: I get warnings on unused imports in your code. --yg
/** @author Shay Segal
 * @author Sefi Albo
 * @author Daniel Shames */
public enum find {
  ;
  /** @author AnnaBel7
   * @author michalcohena
   * @since Nov 4, 2016 */
  public static TypeDeclaration ancestorType(@SuppressWarnings("unused") final ASTNode __) {
    return null;
  }

  public static MethodDeclaration ancestorMethod(ASTNode n) {
  ASTNode temp = n;
  if(temp==null) return null;
  while(temp!=null){
    if(az.methodDeclaration(temp) != null) return az.methodDeclaration(temp);
    temp=temp.getParent();
  }
  return null;
  }
}
