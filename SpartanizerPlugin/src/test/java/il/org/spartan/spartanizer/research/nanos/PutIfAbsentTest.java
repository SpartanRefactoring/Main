package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** TODO orimarco {@code marcovitch.ori@gmail.com} please add a description
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-01-04 */
@SuppressWarnings("static-method")
public class PutIfAbsentTest {
  @Test public void a() {
    topDownTrimming("if(!map.containsKey(y)) map.put(y,z);")//
        .using(IfStatement.class, new PutIfAbsent())//
        .gives("map.putIfAbsent(y,z);")//
        .stays();
  }

  @Test public void b() {
    topDownTrimming("if(!map.containsKey(y)) map.put(y,new OMG(Its, (a)big, one));")//
        .using(IfStatement.class, new PutIfAbsent())//
        .gives("map.putIfAbsent(y,new OMG(Its, (a)big, one));")//
        .stays();
  }

  @Test public void c() {
    topDownTrimming("if(!m.a.p.containsKey(y)) m.a.p.put(y,new OMG(Its, (a)big, one));")//
        .using(IfStatement.class, new PutIfAbsent())//
        .gives("m.a.p.putIfAbsent(y,new OMG(Its, (a)big, one));")//
        .stays();
  }

  @Test public void d() {
    topDownTrimming("if(!this.map.containsKey(y)) this.map.put(y,new OMG(Its, (a)big, one));")//
        .using(IfStatement.class, new PutIfAbsent())//
        .gives("this.map.putIfAbsent(y,new OMG(Its, (a)big, one));")//
        .stays();
  }

  @Test public void e() {
    topDownTrimming("if(!this.containsKey(y)) this.put(y,new OMG(Its, (a)big, one));")//
        .using(IfStatement.class, new PutIfAbsent())//
        .gives("this.putIfAbsent(y,new OMG(Its, (a)big, one));")//
        .stays();
  }

  @Test public void f() {
    topDownTrimming("if(!containsKey(y)) put(y,new OMG(Its, (a)big, one));")//
        .using(IfStatement.class, new PutIfAbsent())//
        .gives("putIfAbsent(y,new OMG(Its, (a)big, one));")//
        .stays();
  }
}
