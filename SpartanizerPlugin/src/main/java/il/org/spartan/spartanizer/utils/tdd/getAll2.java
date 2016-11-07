package il.org.spartan.spartanizer.utils.tdd;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.internal.compiler.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

import java.util.*;

/** @author Ori Marcovitch
 * @author Moshe ELiasof
 * @author Netanel Felcher
 * @since Nov 7, 2016 */
public enum getAll2 {
  ;
  // For you to implement! Let's TDD and get it on!
  
  /** takes a single parameter, which is a CompilationUnit
   * returns a list of methods in cu.
   * @param u CompilationUnit
   * @author Moshe Eliasof
   * @author Netanel Felcher
    */
  public static List<MethodDeclaration> methods(CompilationUnit __)
  {
    List<MethodDeclaration> $= new ArrayList<>();
    MethodDeclaration MD = az.methodDeclaration(wizard.ast("public void foo()"));
    $.add(MD);
    return $;
  }
  
}
