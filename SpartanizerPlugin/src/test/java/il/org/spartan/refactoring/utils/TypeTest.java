package il.org.spartan.refactoring.utils;

import static il.org.spartan.azzert.*;
import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.refactoring.utils.Type.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "javadoc", "static-method" }) //
public class TypeTest {
  @Ignore public static class Pending {
    @Test public void test00() {
      fail("Not yet implemented");
    }

    @Test public void test02() {
      azzert.that(kind(Into.e("2 + (2.0)*1L")),is(Type.DOUBLE));
    }
    
  }

  public static class Working {
    @Test public void test01() {
//      for (Type t : Type.values())
//        System.err.println("Erase me after you figured this out\n\t" + t.fullName());
      azzert.that(Type.BOOLEAN, is(Type.BOOLEAN));
    }
    
    @Test public void test04(){
      azzert.that(INT.under(PLUS1), is(INT));
    }
    
    @Test public void test06(){
      azzert.that(ALPHANUMERIC.under(PrefixExpression.Operator.COMPLEMENT), is(INTEGRAL));
    }
    
    @Test public void test15(){
      azzert.that(STRING.underPlus(STRING), is(STRING));
    }
    
    @Test public void test03(){
      azzert.that(DOUBLE.under(DECREMENT_PRE), is(DOUBLE));
    }
    
    @Test public void test05(){
      azzert.that(NOTHING.under(PrefixExpression.Operator.NOT), is(BOOLEAN));
    }
  
    @Test public void test13(){
      azzert.that(INT.underPlus(DOUBLE), is(DOUBLE));
    }
    
    @Test public void test14(){
      azzert.that(INT.underPlus(INT), is(INT));
    }
    
    @Test public void test16(){
      azzert.that(STRING.underPlus(NULL), is(STRING));
    }
  }
}
