package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** TODO:  orimarco <tt>marcovitch.ori@gmail.com</tt>
 please add a description 
 @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-04 
 */

@SuppressWarnings("static-method")
public class NotHoldsOrThrowTest {
  @Test public void a() {
    trimmingOf("if(x.isCute()) throw new Watever();")//
        .using(IfStatement.class, new NotHoldsOrThrow())//
        .gives("holds(!(x.isCute())).orThrow(()->new Watever());")//
        .stays();
  }

  @Test public void b() {
    trimmingOf("if(x.isCute() || iWant()) throw new Watever(with(This, and, zis()));")//
        .using(IfStatement.class, new NotHoldsOrThrow())//
        .gives("holds(!(x.isCute()||iWant())).orThrow(()->new Watever(with(This,and,zis())));")//
        .gives("holds(!x.isCute()&&!iWant()).orThrow(()->new Watever(with(This,and,zis())));")//
        .stays();
  }
}

