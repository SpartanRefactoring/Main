package il.org.spartan.spartanizer.research.methods;

import org.junit.BeforeClass;
import org.junit.Test;

import il.org.spartan.spartanizer.research.nanos.methods.Adjuster;

/** Tests {@link Adjuster}
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2016-12-22 */
@SuppressWarnings("static-method")
public class AdjusterTest extends JavadocerTest {
  @BeforeClass public static void setUp() {
    setNano(new Adjuster());
  }
  @Test public void basic() {
    assert not("boolean foo(){return bar();}");
  }
  @Test public void basic10() {
    assert not("@Override public Set<E> inEdges(){return incidentEdges();} ");
  }
  @Test public void basic11() {
    assert not("@Override public Set<K> keySet(){ return delegate().keySet();}");
  }
  @Test public void basic12() {
    assert not("@Override public Set<K> keySet(){ return delegate().keySet(s);}");
  }
  @Test public void basic13() {
    assert not("@Override public Set<K> keySet(String s){ return delegate(s).keySet();}");
  }
  @Test public void basic14() {
    assert not("@Override public Set<K> keySet(String s){ return delegate().keySet(s);}");
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
    assert not(" @Override public void close() throws IOException { out.close(); }");
  }
  @Test public void basic19() {
    assert not("@Deprecated @Override public void writeBytes(String ¢) throws IOException {    ((DataOutputStream)out).writeBytes(¢);}");
  }
  @Test public void basic2() {
    assert not("boolean foo(int a){return bar(a);}");
  }
  @Test public void basic20() {
    assert not("@Override public int size(){synchronized (mutex) {    return delegate().size();}}");
  }
  @Test public void basic21() {
    assert not("  @Override public int indexOf(Object ¢){    synchronized (mutex) {        return delegate().indexOf(¢); }}");
  }
  @Test public void basic22() {
    assert not("public String join(Map<?,?> ¢){  return join(¢.entrySet());}");
  }
  @Test public void basic23() {
    assert not("public String join(Map<?,?> ¢){ synchronized(mutex){ return join(¢.entrySet());}}");
  }
  @Test public void basic24() {
    assert not(" @Override public int size(){    return multiset().entrySet().size();  }");
  }
  @Test public void basic25() {
    assert not("   void removeValuesForKey(Object key) {    multimap.keySet().remove(key);  }");
  }
  @Test public void basic26() {
    assert not("   int size(Object key) {    multimap.keySet().size();  }");
  }
  @Test public void basic3() {
    assert not("boolean foo(int a){return bar(a,a);}");
  }
  @Test public void basic4() {
    assert is("boolean foo(int a){return bar(a,f(a));}");
  }
  @Test public void basic5() {
    assert not("boolean foo(int a){if(a == null) return bar(a);}");
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
    assert not("void foo(int a){return bar(a);}");
  }
  @Test public void positive0() {
    assert is("@CanIgnoreReturnValue public ToStringHelper addValue(boolean value){return addHolder(String.valueOf(value));}");
  }
}
