package il.org.spartan.spartanizer.utils.tdd;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
 
/**
 * @author Doron Meshulam
 * @author Tomer Dragucki
 */

@SuppressWarnings({ "static-method", "javadoc"})
public class Issue707 {
  
  @Test
  public void a() {
    getAll2.names(null);
  }
  
  @Test
  public void b() {
    getAll2.names((Block) null);
  }
}
