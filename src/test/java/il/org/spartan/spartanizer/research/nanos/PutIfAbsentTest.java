package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** TODO orimarco {@code marcovitch.ori@gmail.com} please add a description
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-01-04 */
@SuppressWarnings("static-method")
public class PutIfAbsentTest {
  @Test public void a() {
    trimminKof("if(!map.containsKey(y)) map.put(y,z);")//
        .using(new PutIfAbsent(), IfStatement.class)//
        .gives("map.putIfAbsent(y,z);")//
        .stays();
  }

  @Test public void b() {
    trimminKof("if(!map.containsKey(y)) map.put(y,new OMG(Its, (a)big, one));")//
        .using(new PutIfAbsent(), IfStatement.class)//
        .gives("map.putIfAbsent(y,new OMG(Its, (a)big, one));")//
        .stays();
  }

  @Test public void c() {
    trimminKof("if(!m.a.p.containsKey(y)) m.a.p.put(y,new OMG(Its, (a)big, one));")//
        .using(new PutIfAbsent(), IfStatement.class)//
        .gives("m.a.p.putIfAbsent(y,new OMG(Its, (a)big, one));")//
        .stays();
  }

  @Test public void d() {
    trimminKof("if(!this.map.containsKey(y)) this.map.put(y,new OMG(Its, (a)big, one));")//
        .using(new PutIfAbsent(), IfStatement.class)//
        .gives("this.map.putIfAbsent(y,new OMG(Its, (a)big, one));")//
        .stays();
  }

  @Test public void e() {
    trimminKof("if(!this.containsKey(y)) this.put(y,new OMG(Its, (a)big, one));")//
        .using(new PutIfAbsent(), IfStatement.class)//
        .gives("this.putIfAbsent(y,new OMG(Its, (a)big, one));")//
        .stays();
  }

  @Test public void f() {
    trimminKof("if(!containsKey(y)) put(y,new OMG(Its, (a)big, one));")//
        .using(new PutIfAbsent(), IfStatement.class)//
        .gives("putIfAbsent(y,new OMG(Its, (a)big, one));")//
        .stays();
  }
}
