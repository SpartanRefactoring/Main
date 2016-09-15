package il.org.spartan.spartanizer.wring.dispatch;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.wring.TrimmerTestsUtils.*;

import java.util.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.wring.*;
import il.org.spartan.spartanizer.wring.dispatch.*;

/** Unit tests for {@link NameYourClassHere}
 * @author TODO // Write your name here
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class AsSpartanizationTest {
  private static final Class<BlockSimplify> BLOCK_SIMPLIFY = BlockSimplify.class;
  private final 
    AsSpartanization it = new AsSpartanization(new BlockSimplify());
  
  @Test public void exists() {
    azzert.notNull(it);
  }

  @Test public void wring() {
    azzert.notNull(it.wring);
  }
   

  @Test public void wringIsCorrect() {
    azzert.that(it.wring, instanceOf(BLOCK_SIMPLIFY));
  }

  @Test public void nameIsCorrect() {
    azzert.that(it.wring, instanceOf(BLOCK_SIMPLIFY));
  }
 @Test public void clazzIsNotNull() {
    azzert.notNull(it.clazz);
  }
 @Test public void clazzIsCorrect() {
    azzert.that(it.clazz, is(BLOCK_SIMPLIFY));
  }

  
 
}
