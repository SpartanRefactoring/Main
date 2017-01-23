package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** TODO:  orimarco <tt>marcovitch.ori@gmail.com</tt>
 please add a description 
 @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2016-12-20 
 */

@SuppressWarnings("static-method")
public class LispFirstElementTest {
  @Test public void a() {
    trimmingOf("li.get(0)")//
        .using(MethodInvocation.class, new LispFirstElement())//
        .gives("first(li)")//
        .stays();
  }

  @Test public void b() {
    trimmingOf("li.get(0);")//
        .using(MethodInvocation.class, new LispFirstElement())//
        .gives("first(li);")//
        .stays();
  }

  @Test public void c() {
    trimmingOf("omg(li.get(0),li.get(0));")//
        .using(MethodInvocation.class, new LispFirstElement())//
        .gives("omg(first(li),first(li));")//
        .stays();
  }
}

