package il.org.spartan.spartanizer.research.methods;

import org.junit.BeforeClass;
import org.junit.Test;

import il.org.spartan.spartanizer.research.nanos.methods.Delegator;

/** Tests {@link Delegator}
 * @author Ori Marcovitch */
@SuppressWarnings("static-method")
public class DelegatorTest extends JavadocerTest {
  @BeforeClass public static void setUp() {
    setNano(new Delegator());
  }
  @Test public void basic() {
    assert is("boolean foo(){return bar();}");
  }
  @Test public void basic10() {
    assert is("@Override public Set<E> inEdges(){return incidentEdges();} ");
  }
  @Test public void basic11() {
    assert is("@Override public Set<K> keySet(){ return delegate().keySet();}");
  }
  @Test public void basic12() {
    assert not("@Override public Set<K> keySet(){ return delegate().keySet(s);}");
  }
  @Test public void basic13() {
    assert not("@Override public Set<K> keySet(String s){ return delegate(s).keySet();}");
  }
  @Test public void basic14() {
    assert is("@Override public Set<K> keySet(String s){ return delegate().keySet(s);}");
  }
  @Test public void basic15() {
    assert not("@Override @CanIgnoreReturnValue public V put(R rowKey,C columnKey,V value){ return row(rowKey).put(columnKey,value);}");
  }
  @Test public void basic16() {
    assert not("@Override public String toString(){ return \"\" + member;}");
  }
  @Test public void basic17() {
    assert not("long reserveAndGetWaitLength(int permits,long nowMicros){ return max(reserveEarliestAvailable(permits,nowMicros) - nowMicros,0);}");
  }
  @Test public void basic18() {
    assert is(" @Override public void close() throws IOException { out.close(); }");
  }
  @Test public void basic19() {
    assert is("@Deprecated @Override public void writeBytes(String ¢) throws IOException {    ((DataOutputStream)out).writeBytes(¢);}");
  }
  @Test public void basic2() {
    assert is("boolean foo(int a){return bar(a);}");
  }
  @Test public void basic20() {
    assert is("@Override public int size(){synchronized (mutex) {    return delegate().size();}}");
  }
  @Test public void basic21() {
    assert is("  @Override public int indexOf(Object ¢){    synchronized (mutex) {        return delegate().indexOf(¢); }}");
  }
  @Test public void basic22() {
    assert is("public String join(Map<?,?> ¢){  return join(¢.entrySet());}");
  }
  @Test public void basic23() {
    assert is("public String join(Map<?,?> ¢){ synchronized(mutex){ return join(¢.entrySet());}}");
  }
  @Test public void basic24() {
    assert is(" @Override public int size(){    return multiset().entrySet().size();  }");
  }
  @Test public void basic25() {
    assert is("   void removeValuesForKey(Object key) {    multimap.keySet().remove(key);  }");
  }
  @Test public void basic26() {
    assert is("   int size(Object key) {    multimap.keySet().size();  }");
  }
  @Test public void basic27() {
    assert is(" @Override @CanIgnoreReturnValue public Service stopAsync(){delegate.stopAsync();return this;}");
  }
  @Test public void basic28() {
    assert is("@SuppressWarnings({\"a\"}) public void bar() {      use();    }");
  }
  @Test public void basic3() {
    assert is("boolean foo(int a){return bar(a,a);}");
  }
  @Test public void basic4() {
    assert not("boolean foo(int a){return bar(a,f(a));}");
  }
  @Test public void basic5() {
    assert not("boolean foo(int a){if(a == 7) return bar(a);}");
  }
  @Test public void basic6() {
    assert not("boolean foo(int a){return bar(a,b);}");
  }
  @Test public void basic7() {
    assert not("boolean foo(int a){return bar(a,f(b));} ");
  }
  @Test public void basic8() {
    assert not("boolean foo(int a){return bar(a,f(f(b)));} ");
  }
  @Test public void basic9() {
    assert is("void foo(int a){return bar(a);}");
  }
}
