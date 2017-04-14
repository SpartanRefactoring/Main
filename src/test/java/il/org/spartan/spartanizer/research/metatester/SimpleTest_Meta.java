package il.org.spartan.spartanizer.research.metatester;
import static org.junit.Assert.*;
import org.junit.*;
/** 
 * @author Oren Afek
 * @since 3/27/2017 
 */
@SuppressWarnings("all") public class SimpleTest_Meta {
@Test public void test1(){
assertEquals(1,1);

}@Test public void test2(){
assertEquals(1,1);

System.out.println("");

}@Test public void test3(){
assertEquals(1,1);

System.out.println("");

assertEquals(1,1);

}@Test public void test4(){
assertEquals(1,1);

System.out.println("");

assertEquals(1,1);

System.out.println("");

}@Test public void test5(){
assertEquals(1,1);

System.out.println("");

assertEquals(1,1);

System.out.println("");

assert true;

}@Test public void test6(){
assert true;

}
}
