package il.org.spartan.spartanizer.research.methods;

import org.junit.*;

import il.org.spartan.spartanizer.research.nanos.methods.*;

/** TODO Ori Marcovitch please add a description
 * @author Ori Marcovitch
 * @since 2016 */
@SuppressWarnings("static-method")
public class UseParameterAndReturnItTest extends JavadocerTest {
  @BeforeClass public static void setUp() {
    setNano(new UseParameterAndReturnIt());
  }

  @Test public void a() {
    assert is("boolean foo(int a){return a;}");
  }

  @Test public void b() {
    assert not("boolean foo(){return foo();}");
  }

  @Test public void c() {
    assert not("@Override public int hashCode(Object b) {return this.b;}");
  }

  @Test public void d() {
    assert not("@Override public X unfiltered(int a, int b){  return a;}");
  }

  @Test public void e() {
    assert not("@Override public X unfiltered(Object a){  return (SetMultimap)a;}");
  }

  @Test public void f() {
    assert is("@Override public SetMultimap<K,V> unfiltered(A a){  if(a==null) print(a); else print(omg); return a;}");
  }

  @Test public void g() {
    assert is("@Override public SetMultimap<K,V> unfiltered(A a){  if(a==null) return a; use(); print(omg); return a;}");
  }

  @Test public void h() {
    assert not("@Override public void unfiltered(A a){return a;}");
  }
}
