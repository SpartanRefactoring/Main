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
@SuppressWarnings("unused")
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
 
  @SuppressWarnings("hiding")
  default <ASTNode,Object> NamedFunction<ASTNode,Object> m(final String name, final Function<ASTNode,Object> r) {
    return new NamedFunction<ASTNode,Object>(name, r);
  }
  
  NamedFunction<ASTNode, Object>[] functions();
  
//@FunctionalInterface interface ToInt<R> {
//int run(R r);
//}
//
//@FunctionalInterface interface ToBoolean<R> {
//boolean run(R r);
//}
//
//@FunctionalInterface interface BiFunctionDouble<T, R> {
//double apply(T t, R r);
//}
//
//@FunctionalInterface interface BiFunctionFloat<T, R> {
//float apply(T t, R r);
//}
//
//@FunctionalInterface interface BiFunctionInteger<T, R> {
//int apply(T t, R r);
//}

//static class NamedFunction2<R> {
//final String name;
//final ToBoolean<R> f;
//
//NamedFunction2(final String name, final ToBoolean<R> f) {
//  this.name = name;
//  this.f = f;
//}
//public String name() {
//  return this.name;
//}
//public ToBoolean<R> function() {
//  return this.f;
//}
//}  
  
//static class NamedFunction<R> {
//final String name;
//final ToInt<R> f;
//
//NamedFunction(final String name, final ToInt<R> f) {
//  this.name = name;
//  this.f = f;
//}
//public String name() {
//  return this.name;
//}
//public ToInt<R> function() {
//  return this.f;
//}
//}
  
}
