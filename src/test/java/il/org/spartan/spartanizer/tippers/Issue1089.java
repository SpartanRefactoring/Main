package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.testing.TrimmerTestsUtils.*;

import org.junit.*;

/** Testing disabling lambda expression tipping
 * {@link FragmentInitializerStatementTerminatingScope}
 * @author tomerdragucki <tt>tomerd@campus.technion.ac.il</tt>
 * @since 2017-01-22 */
@SuppressWarnings("static-method")
public class Issue1089 {
  @Test public void a() {
    trimmingOf("eG g=new eG();(new nA()).f().forEach(c->{oB r=new oB(c);r.sEg(g);});")
        .gives("eG g=new eG();(new nA()).f().forEach(c->{new oB(c).sEg(g);});") //
        .gives("eG g=new eG();(new nA()).f().forEach(c->new oB(c).sEg(g));") //
        .gives("eG g=new eG();(new nA()).f().forEach(λ->new oB(λ).sEg(g));") //
        .stays();
  }

  @Test public void b() {
    trimmingOf("Object o=new Object();l.forEach(c->a(o));")//
        .gives("Object o=new Object();l.forEach(λ->a(o));")//
        .stays();
  }
}