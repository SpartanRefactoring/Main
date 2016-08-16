package il.org.spartan.refactoring.wring;

import static il.org.spartan.azzert.is;
import static il.org.spartan.refactoring.utils.Funcs.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.junit.runners.Parameterized.*;

import il.org.spartan.*;
import il.org.spartan.refactoring.utils.*;
import il.org.spartan.refactoring.wring.AbstractWringTest.*;
import il.org.spartan.utils.Utils;

/** Unit tests
 * @author Alex and Dan
 * @since 2016-09-16 */
@SuppressWarnings("javadoc") //
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
public class DoubleCastConversionTest {
  @RunWith(Parameterized.class) //
  @FixMethodOrder(MethodSorters.NAME_ASCENDING) //
  public static class Wringed extends AbstractWringTest.WringedExpression<CastExpression> {
    private static String[][] cases = Utils.asArray(new String[] { "(double)a", "(int)a"},null);

    
    
    
    
    
    
//The next is not relevant, was copied from an other test and wasn't finished.
    
    /** Generate test cases for this parameterized class.
     * @return a collection of cases, where each case is an array of three
     *         objects, the test case name, the input, and the file. */
    @Parameters(name = DESCRIPTION) //
    public static Collection<Object[]> cases() {
      return collect(cases);
    }
    /** Instantiates the enclosing class ({@link Wringed}) */
    public Wringed() {
      super(WRING);
    }
    @Test public void isfStatementElseIsEmpty() {
      azzert.that(extract.statements(asMe().getElseStatement()).size(), is(0));
    }

  }

  static final Wring<CastExpression> WRING = new DoubleCastConversion();
}
