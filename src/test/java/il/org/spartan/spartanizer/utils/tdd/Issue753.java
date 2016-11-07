package il.org.spartan.spartanizer.utils.tdd;

import org.junit.*;
import org.junit.runners.*;

import static org.junit.Assert.*;

/** Tests of {@link enumerate.expressions}
 * @author Roei-m
 * @author RoeyMaor
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //

@SuppressWarnings("static-method") public class Issue753 {
  @Test public void a() {
    assertNull(getAll.methods(null));
  }
  
}
