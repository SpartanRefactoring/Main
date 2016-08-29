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
      azzert.that(Type.BOOLEAN, is(Type.BOOLEAN));
    }
    
    @Test public void test04(){
      azzert.that(INT.under(PLUS1), is(INT));
    }
    
    @Test public void test06(){
      azzert.that(ALPHANUMERIC.under(PrefixExpression.Operator.COMPLEMENT), is(INTEGRAL));
    }
    
    @Test public void test15(){
      azzert.that(STRING.underBinaryOperator(PLUS2, STRING), is(STRING));
    }
    
    @Test public void test03(){
      azzert.that(DOUBLE.under(DECREMENT_PRE), is(DOUBLE));
    }
    
    @Test public void test05(){
      azzert.that(NOTHING.under(PrefixExpression.Operator.NOT), is(BOOLEAN));
    }
  
    @Test public void test07(){
      azzert.that(NOTHING.underBinaryOperator(PLUS2, NOTHING), is(ALPHANUMERIC));
    }
    
    @Test public void test13(){
      azzert.that(INT.underBinaryOperator(PLUS2, DOUBLE), is(DOUBLE));
    }
    
    @Test public void test14(){
      azzert.that(INT.underBinaryOperator(PLUS2, INT), is(INT));
    }
    
    @Test public void test16(){
      azzert.that(STRING.underBinaryOperator(PLUS2, NULL), is(STRING));
    }
    
    @Test public void test17(){
      azzert.that(NUMERIC.underBinaryOperator(PLUS2, NULL), is(STRING));
    }
    
    @Test public void test18(){
      azzert.that(LONG.underBinaryOperator(PLUS2, INT), is(LONG));
    }
    
    @Test public void test19(){
      azzert.that(LONG.underBinaryOperator(PLUS2, INTEGRAL), is(LONG));
    }
    
    @Test public void test20(){
      azzert.that(LONG.underBinaryOperator(PLUS2, NUMERIC), is(NUMERIC));
    }
    
    @Test public void test21(){
      azzert.that(INT.underBinaryOperator(PLUS2, INTEGRAL), is(INTEGRAL));
    }
    
    @Test public void test22(){
      azzert.that(INT.underBinaryOperator(InfixExpression.Operator.AND, INT), is(INT));
    }
    
    @Test public void test23(){
      azzert.that(INT.underBinaryOperator(InfixExpression.Operator.OR, LONG), is(LONG));
    }
    
    @Test public void test24(){
      azzert.that(INTEGRAL.underBinaryOperator(InfixExpression.Operator.LEFT_SHIFT, LONG), is(LONG));
    }
    
    @Test public void test25(){
      azzert.that(NUMERIC.underBinaryOperator(InfixExpression.Operator.REMAINDER, NOTHING), is(INTEGRAL));
    }
    
    @Test public void test26(){
      azzert.that(LONG.underBinaryOperator(InfixExpression.Operator.RIGHT_SHIFT_SIGNED, INTEGRAL), is(LONG));
    }
    
    @Test public void test27(){
      azzert.that(STRING.underBinaryOperator(InfixExpression.Operator.XOR, INTEGRAL), is(INTEGRAL));
    }
    
    @Test public void test28(){
      azzert.that(INT.underBinaryOperator(InfixExpression.Operator.GREATER, INTEGRAL), is(BOOLEAN));
    }
    
    @Test public void test29(){
      azzert.that(NONNULL.underBinaryOperator(InfixExpression.Operator.EQUALS, STRING), is(BOOLEAN));
    }
    
    @Test public void test30(){
      azzert.that(NUMERIC.underBinaryOperator(InfixExpression.Operator.NOT_EQUALS, BAPTIZED), is(BOOLEAN));
    }
    
    @Test public void test31(){
      azzert.that(BOOLEAN.underBinaryOperator(InfixExpression.Operator.CONDITIONAL_AND, BOOLEAN), is(BOOLEAN));
    }
    
    @Test public void test32(){
      azzert.that(DOUBLE.underBinaryOperator(InfixExpression.Operator.TIMES, INTEGRAL), is(DOUBLE));
    }
    
    @Test public void test33(){
      azzert.that(DOUBLE.underBinaryOperator(InfixExpression.Operator.DIVIDE, NUMERIC), is(DOUBLE));
    }
    
    @Test public void test34(){
      azzert.that(INTEGRAL.underBinaryOperator(MINUS2, LONG), is(LONG));
    }
    
    @Test public void test35(){
      azzert.that(CHAR.underBinaryOperator(PLUS2, CHAR), is(CHAR));
    }
    
    @Test public void test36(){
      azzert.that(CHAR.underBinaryOperator(MINUS2, INT), is(INT));
    }
    
    @Test public void test37(){
      azzert.that(CHAR.under(PrefixExpression.Operator.COMPLEMENT), is(CHAR));
    }
  }
}
