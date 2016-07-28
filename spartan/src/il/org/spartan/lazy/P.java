/** Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package il.org.spartan.lazy;


interface P<T1, T2, T3> {// @formatter:off
  interface dog {}
  interface cat {}

  static <X>  P<X, dog, X> α() { return null; }
  static <Y>  P<cat,Y, Y> β() { return null;} 
  static <Y>  P<cat,cat, Y> c() { return null;} 
  static <T> T unify(T t1, T t2) {  return null ; }
  static <T> T unify(T t) {  return null ; }
  static <X> P<X, X, X> f() { return null; }
  static <X> P<X, X, X> g() { return null; }
  static Object a=  unify(α(), β() );
  static Object x=  unify(α(), c() );
  static  P<Object, dog, Object> z =  unify(α());

}
