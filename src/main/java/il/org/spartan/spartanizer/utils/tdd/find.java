package il.org.spartan.spartanizer.utils.tdd;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.safety.*;
/** @author Ori Marcovitch*/
/** @author AnnaBel7
 * @author michalcohen
 * @since Nov 4, 2016 */
/** @author Shay Segal
 * @author Sefi Albo
 * @author Daniel Shames */
public enum find {
  ;
  /** @author AnnaBel7
   * @author michalcohena
   * @since Nov 4, 2016 */
  public static TypeDeclaration ancestorType(final ASTNode n) {
    return n == null ? null
        : n.getParent().getParent().getParent().getParent() == null ? (TypeDeclaration) n.getParent().getParent()
            : (TypeDeclaration) n.getParent().getParent().getParent().getParent();
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
