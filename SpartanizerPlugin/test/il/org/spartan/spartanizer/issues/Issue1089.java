package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

import il.org.spartan.spartanizer.tippers.*;

/** Testing disabling lambda expression tipping
 * {@link LocalInitializedStatementTerminatingScope}
 * @author tomerdragucki {@code tomerd@campus.technion.ac.il}
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