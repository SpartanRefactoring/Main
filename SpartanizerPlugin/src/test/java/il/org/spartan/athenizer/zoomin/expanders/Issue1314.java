package il.org.spartan.athenizer.zoomin.expanders;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.testing.*;
import il.org.spartan.spartanizer.tippers.*;

/** TODO Yossi Gil: document class
 * @author Yossi Gil
 * @since 2017-05-21 */
@Ignore("Bugs") //
@SuppressWarnings("static-method")
public class Issue1314 {
  @Test public void a() {
    trimmingOf("List<T> x = new ArrayList<>(); x.addAll(ys);") //
        .using(new LocalInitializedNewAddAll()).gives("List<T> x = new ArrayList<>(ys);")//
    ;
  }
  @Test public void b() {
    new TestOperand("List<T> x = new ArrayList<>(); x.addAll(ys); y(x); return x;") //
        .gives("List<T> x = new ArrayList<>(ys); y(x); return x;")//
    ;
  }
  /** Introduced by Yogi on Sun-May-21-00:01:57-IDT-2017 (code generated
   * automatically by {@link JUnitTestMethodFacotry}) */
  @Test public void c() {
    trimmingOf("List<T> x = new ArrayList<>(); x.addAll(ys);")//
        .stays()//
    ;
  }
  /** Introduced by Yogi on Sun-May-21-00:01:57-IDT-2017 (code generated
   * automatically by {@link JUnitTestMethodFacotry}) */
  @Test public void aBaNewCabc() {
    trimmingOf("A<B>a=new C<>();a.b(c);") //
        .using(VariableDeclarationFragment.class, new LocalInitializedStatementTerminatingScope()) //
        .gives("(new C<>()).b(c);") //
        .stays() //
    ;
  }
}
