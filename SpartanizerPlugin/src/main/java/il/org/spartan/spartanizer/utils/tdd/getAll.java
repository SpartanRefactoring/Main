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
 * @since Oct 31, 2016 */
public enum getAll {
  ;
  /** Get all the methods invoked in m
   * @param d JD
   * @return List of the names of the methods */
  public static Set<String> invocations(final MethodDeclaration ¢) {
    if(¢ == null)
      return null;
    Set<String> $ = new TreeSet<>();
    if(¢.getBody().statements().isEmpty())
      return  $;
    for(Statement s : (List<Statement>)¢.getBody().statements()){
      if(iz.expressionStatement(s) && //
          az.methodInvocation(az.expressionStatement(s).getExpression())!=null)
        $.add((az.methodInvocation(az.expressionStatement(s).getExpression()).getName() + ""));
    }
    return $;
  }

  /** Get list of names in a Block
   * @param ¢ Block
   * @return List of the names in the block */
  public static List<Name> names(final Block b) {
    if(b==null) {
      return null;
    }
    if(b.getLength()>2) { 
      List<Name> l = new ArrayList<>();
      Name n = null; 
      l.add(n);
      return l; 
    }
    return new ArrayList<>();
  }
  
  

}
