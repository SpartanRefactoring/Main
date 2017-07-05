package il.org.spartan.java.cfg;

import org.eclipse.jdt.core.dom.*;

/** TODO dormaayn: document class 
 * 
 * @author dormaayn
 * @since 2017-07-04 */
public class MethodBuilder {
  
  private CFG cfg;
  private MethodDeclaration methodDeclaration;
  private MethodBuilder(CFG cfg, MethodDeclaration methodDeclaration){
    this.cfg = cfg;
    this.methodDeclaration = methodDeclaration;
  }
  public static MethodBuilder of(CFG g, MethodDeclaration methodDeclaration){
    return new MethodBuilder(g,methodDeclaration);
  }
  
  void go(){
    
  }
  
  
}
