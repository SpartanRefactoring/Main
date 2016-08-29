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

    @Test public void test01() {
      azzert.that(kind(Into.e("2 + (2.0)*1L")),is(Type.DOUBLE));
    }
    
  }

 
  public static class Working {
    
    /* first batch of tests looks into specific inner cases of the under methods*/
    @Test public void test02(){
      azzert.that(kind(Into.e("+2"),INT), is(INT));
    }
    
    @Test public void test03(){
      azzert.that(kind(Into.e("~2"),ALPHANUMERIC), is(INTEGRAL));
    }
     
    @Ignore("creates NumberLiteral instead of PrefixExpression, need to figure out why")
    @Test public void test04(){
      azzert.that(kind(Into.e("++3"),DOUBLE), is(DOUBLE));
    }
    
    @Test public void test05(){
      azzert.that(kind(Into.e("!x"),NOTHING), is(BOOLEAN));
    }
    
    @Test public void test06(){
      azzert.that(kind(Into.e("~'x'"),CHAR), is(INT));
    }
  
    @Test public void test07(){
      azzert.that(kind(Into.e("x+y"),NOTHING,NOTHING), is(ALPHANUMERIC));
    }
    
    @Test public void test08(){
      azzert.that(kind(Into.e("x+y"),INT,DOUBLE), is(DOUBLE));
    }
    
    @Test public void test09(){
      azzert.that(kind(Into.e("x+y"),INT,INT), is(INT));
    }
    
    @Test public void test10(){
      azzert.that(kind(Into.e("x+y"),STRING,STRING), is(STRING));
    }
    
    @Test public void test11(){
      azzert.that(kind(Into.e("x+y"),STRING,NULL), is(STRING));
    }
    
    @Test public void test12(){
      azzert.that(kind(Into.e("x+y"),NUMERIC,NULL), is(STRING));
    }
    
    @Test public void test13(){
      azzert.that(kind(Into.e("x+y"),LONG,INT), is(LONG));
    }
    
    @Test public void test14(){
      azzert.that(kind(Into.e("x+y"),LONG,INTEGRAL), is(LONG));
    }
    
    @Test public void test15(){
      azzert.that(kind(Into.e("x+y"),LONG,NUMERIC), is(NUMERIC));
    }
    
    @Test public void test16(){
      azzert.that(kind(Into.e("x+y"),INT,INTEGRAL), is(INTEGRAL));
    }
    
    @Test public void test17(){
      azzert.that(kind(Into.e("x&y"),INT,INT), is(INT));
    }
    
    @Test public void test18(){
      azzert.that(kind(Into.e("x|y"),INT,LONG), is(LONG));
    }
    
    @Test public void test19(){
      azzert.that(kind(Into.e("x<<y"),INTEGRAL,LONG), is(INTEGRAL));
    }
    
    @Test public void test20(){
      azzert.that(kind(Into.e("x%y"),NUMERIC,NOTHING), is(INTEGRAL));
    }
    
    @Test public void test21(){
      azzert.that(kind(Into.e("x>>y"),LONG,INTEGRAL), is(LONG));
    }
    
    @Test public void test22(){
      azzert.that(kind(Into.e("x^y"),NONNULL,INTEGRAL), is(INTEGRAL));
    }
    
    @Test public void test23(){
      azzert.that(kind(Into.e("x>y"),INT,INTEGRAL), is(BOOLEAN));
    }
    
    @Test public void test24(){
      azzert.that(kind(Into.e("x==y"),NONNULL,INTEGRAL), is(BOOLEAN));
    }
    
    @Test public void test25(){
      azzert.that(kind(Into.e("x!=y"),NUMERIC,BAPTIZED), is(BOOLEAN));
    }
    
    @Test public void test26(){
      azzert.that(kind(Into.e("x&&y"),BOOLEAN,BOOLEAN), is(BOOLEAN));
    }
    
    @Test public void test27(){
      azzert.that(kind(Into.e("x*y"),DOUBLE,NUMERIC), is(DOUBLE));
    }
    
    @Test public void test28(){
      azzert.that(kind(Into.e("x/y"),DOUBLE,INTEGRAL), is(DOUBLE));
    }
    
    @Test public void test29(){
      azzert.that(kind(Into.e("x-y"),INTEGRAL,LONG), is(LONG));
    }
    
    @Test public void test30(){
      azzert.that(kind(Into.e("x+y"),CHAR,CHAR), is(INT));
    }
    
    @Test public void test31(){
      azzert.that(kind(Into.e("x-y"),CHAR,INT), is(INT));
    }
  }
}
