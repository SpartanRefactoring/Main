package il.org.spartan.spartanizer.cmdline;

import static il.org.spartan.tide.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

@SuppressWarnings("unused")
public class ASTNodeMetrics {
  
  private ASTNode n;

  private int length;
  private int tokens;
  private int nodes;
  private int body;
  private int statements;
  private int tide;
  private int essence;
    
  public ASTNodeMetrics(ASTNode n) {
    super();
    this.n = n;
  }  
  
  public void computeMetrics(){
    this.length = n.getLength();
    this.tokens = metrics.tokens(n + "");
    this.nodes = count.nodes(n);
    this.body = metrics.bodySize(n);
    this.statements = extract.statements(az.methodDeclaration(n).getBody()).size();
    this.tide = clean(n + "").length();
    this.essence = Essence.of(n + "").length();
  }
  
}
