package il.org.spartan.spartanizer.cmdline.collector;

import java.util.function.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.cmdline.collector.FeatureCollector.*;

/** Interface that implements useful methods for {@link MethodFeatureCollector}
 * {@link TypeFeatureCollector}, etc.
 * @author Matteo Orru'
 * @param <ASTNode>
 * @param <T>
 * @since 2016 */
@SuppressWarnings({ "unused", "hiding" })
public interface FeatureCollector<ASTNode, T> {
  
  static class NamedFunction<ASTNode, T> {
    final String name;
    final Function<ASTNode, T> f;
    
    NamedFunction(final String name, final Function<ASTNode, T> f){
      this.name = name;
      this.f = f;
    }
    public String name(){
      return this.name;
    }
    public Function<ASTNode, T> function() {
      return this.f;
    }
  }
 
  default <ASTNode,Object> NamedFunction<ASTNode,Object> m(final String name, final Function<ASTNode,Object> r) {
    return new NamedFunction<ASTNode,Object>(name, r);
  }
  
  NamedFunction<ASTNode, T>[] functions();
  
}
