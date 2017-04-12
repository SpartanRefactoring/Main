package il.org.spartan.spartanizer.research.methods;

import org.junit.*;

import il.org.spartan.spartanizer.research.nanos.methods.*;

/** Tests {@link FactoryMethod}
 * @author Ori Marcovitch */
@SuppressWarnings("static-method")
public class FactoryMethodTest extends JavadocerTest {
  @BeforeClass public static void setUp() {
    setNano(new FactoryMethod());
  }

  @Test public void a() {
    assert is("void foo(){return new Object();}");
  }

  @Test public void b() {
    assert is("boolean foo(){return new Object(a);}");
  }

  @Test public void c() {
    assert not("boolean foo(){return new Object(a).c;}");
  }

  @Test public void d() {
    assert not("boolean foo(){return \"\" + new Object(a).c;}");
  }

  @Test public void e() {
    assert not("@Override public <T>HashCode hashObject(T instance,Funnel<? super T> t){ return newHasher().putObject(instance,t).hash(); }");
  }

  @Test public void f() {
    assert is("void foo(){return new Object(){};}");
  }

  @Test public void g() {
    assert is(
        "void foo(){return new Iterator(){    final Iterator<? extends A> fromIterator=fromIterable.iterator();  @Override public boolean hasNext(){   return fromIterator.hasNext();}};} ");
  }

  @Test public void h() {
    assert is("@SuppressWarnings(\"unchecked\") Segment<K,V>[] newSegmentArray(int ssize){return new Segment[ssize]; }");
  }
}
