package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.trimmingOf;

import org.junit.Test;

import il.org.spartan.spartanizer.tippers.InfiniteForBreakToReturn;
import il.org.spartan.spartanizer.tippers.WhileInfiniteBreakToReturn;

/** Test case for bug in {@link InfiniteForBreakToReturn} and {@link WhileInfiniteBreakToReturn}
 * @author Yuval Simon
 * @since 2017-06-29 */
@SuppressWarnings("static-method")
public class Issue1548 {  
  @Test public void t1() {
    trimmingOf("for(;true;) { if(f()) break; if(r()) break; ++i; } return g();")
    .using(new InfiniteForBreakToReturn())
    .gives("for(;true;) { if(f()) return g(); if(r()) return g(); ++i; }");
  }
  @Test public void t2() {
    trimmingOf("while(true) { if(f()) break; if(r()) break; ++i; } return g();")
    .using(new WhileInfiniteBreakToReturn())
    .gives("while(true) { if(f()) return g(); if(r()) return g(); ++i; }");
  }
}
