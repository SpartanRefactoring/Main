package tests;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import metatester.MetaTester;
@SuppressWarnings("all") public class SimpleTest_Meta {
  public int f(){
    return 6;
  }
@Test public void test1_meta0(){
assertThat(3).isLessThan(2);
f();
f();}
@Test public void test1_meta1(){
assertThat(3).isLessThan(2);
f();
f();}
@Test public void test1_meta2(){
assertThat(1).isEqualTo(1);
f();
f();}
@Test public void test1_meta3(){
assertThat(0).isEqualTo(1);
f();
f();}
@Test public void test1_meta4(){
assertThat(2).isEqualTo(2);
f();
f();}
@Test public void test1_meta5(){
assertThat(f()).isLessThan(2);
f();}
@Test public void test1_meta6(){
f();
assertThat(2).isEqualTo(f());}
@Test public void test1_meta7(){
f();
f();
assertThat(f(),is(lessThan(2)));}
}
