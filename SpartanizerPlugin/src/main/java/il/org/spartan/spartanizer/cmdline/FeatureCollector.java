package il.org.spartan.spartanizer.cmdline;

import java.util.function.*;

import org.eclipse.jdt.core.dom.*;

/** Interface that implements useful methods for {@link MethodFeatureCollector}
 * {@link TypeFeatureCollector}, etc.
 * @author Matteo Orru'
 * @since 2016 */
public interface FeatureCollector<T> {
  default NamedFunction<ASTNode, Object> m(final String name, final Function<ASTNode, Object> r) {
    return new NamedFunction<>(name, r);
  }
  NamedFunction<ASTNode, T>[] functions();
  NamedFunction<ASTNode, T>[] functions(String id);

  @SuppressWarnings("hiding")
  class NamedFunction<ASTNode, T> {
    final String name;
    final Function<ASTNode, T> function;

    NamedFunction(final String name, final Function<ASTNode, T> function) {
      this.name = name;
      this.function = function;
    }
    public String name() {
      return name;
    }
    public Function<ASTNode, T> function() {
      return function;
    }
  }
}