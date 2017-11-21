package tests;
import static org.assertj.core.api.Assertions.assertThat;
import metatester.MetaTester;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
/** 
 * @author Oren Afek
 * @since 02-Jul-17.
 */
@SuppressWarnings("all") public class Test1_Meta {
  int x=0;
  private int f(  int x){
    return x;
  }
@Test public void testOnlySimpleAsserts_meta0(){
assertThat(1).isEqualTo(1);}
@Test public void testOnlySimpleAsserts_meta1(){
assertThat(0).isEqualTo(1);}
@Test public void testOnlySimpleAsserts_meta2(){
assertThat(2).isEqualTo(2);}@Test public void testOnlyJunitAsserts_meta3(){
assertThat(1).isEqualTo(1);}
@Test public void testOnlyJunitAsserts_meta4(){
assertThat(1).isEqualTo(0);}
@Test public void testOnlyJunitAsserts_meta5(){
assertThat(2).isEqualTo(2);}@Test public void testGreaterThanSmallerThan_meta6(){
assertThat(1).isLessThan(2);}
@Test public void testGreaterThanSmallerThan_meta7(){
assertThat(3).isGreaterThan(5);}@Test public void testWithMethodCallsNoSideEffects_meta8(){
assertThat(x).isNotNull();
f(x);
f(f(f(f(f(f(x))))));}
@Test public void testWithMethodCallsNoSideEffects_meta9(){
assertThat(x).isNotNull();
f(x);
f(f(f(f(f(f(x))))));}
@Test public void testWithMethodCallsNoSideEffects_meta10(){
assertThat(f(x)).isEqualTo(x);
f(f(f(f(f(f(x))))));}
@Test public void testWithMethodCallsNoSideEffects_meta11(){
f(x);
assertThat(x).isNotNull();
f(f(f(f(f(f(x))))));}
@Test public void testWithMethodCallsNoSideEffects_meta12(){
f(x);
assertThat(x).isNotNull();
f(f(f(f(f(f(x))))));}
@Test public void testWithMethodCallsNoSideEffects_meta13(){
f(x);
assertThat(f(f(f(f(f(f(x))))))).isEqualTo(x);}@Test public void testWithBetweens_meta14(){
int a=1, b=2, c=0;}
@Test public void testWithBetweens_meta15(){
int a=1, b=2, c=0;
assertThat(b).isNotNull();}
@Test public void testWithBetweens_meta16(){
int a=1, b=2, c=0;
assertThat(a).isNotNull();}
@Test public void testWithBetweens_meta17(){
int a=1, b=2, c=0;
assertThat(c).isNotNull();}
@Test public void testWithBetweens_meta18(){
int a=1, b=2, c=0;
assertThat(b).isStrictlyBetween(a, c);}
@Test public void testWithBetweens_meta19(){
int a=1, b=2, c=0;
assertThat(b).isNotNull();}
@Test public void testWithBetweens_meta20(){
int a=1, b=2, c=0;
assertThat(a).isNotNull();}
@Test public void testWithBetweens_meta21(){
int a=1, b=2, c=0;
assertThat(c).isNotNull();}
@Test public void testWithBetweens_meta22(){
int a=1, b=2, c=0;
assertThat(b).isBetween(a, c);}@Test public void testThreeAsserts_meta23(){
int x=0;
x++;
x++;}
@Test public void testThreeAsserts_meta24(){
int x=0;
assertThat(x).isNotNull();
x++;
x++;}
@Test public void testThreeAsserts_meta25(){
int x=0;
assertThat(x++).isEqualTo(0);
x++;}
@Test public void testThreeAsserts_meta26(){
int x=0;
x++;
assertThat(x).isNotNull();
x++;}
@Test public void testThreeAsserts_meta27(){
int x=0;
x++;
assertThat(x++).isEqualTo(1);}
@Test public void testThreeAsserts_meta28(){
int x=0;
x++;
x++;
assertThat(x).isNotNull();}
@Test public void testThreeAsserts_meta29(){
int x=0;
x++;
x++;
assertThat(x).isEqualTo(2);}@Test public void testInvocations_meta30(){
List<String> l=new ArrayList<>();
l.add("aba");
l.contains("aba");}
@Test public void testInvocations_meta31(){
List<String> l=new ArrayList<>();
assertThat(l).isNotNull();
l.add("aba");
l.contains("aba");}
@Test public void testInvocations_meta32(){
List<String> l=new ArrayList<>();
assertThat(l).isEmpty();
l.add("aba");
l.contains("aba");}
@Test public void testInvocations_meta33(){
List<String> l=new ArrayList<>();
assertThat(l).isNotNull();
l.add("aba");
l.contains("aba");}
@Test public void testInvocations_meta34(){
List<String> l=new ArrayList<>();
l.add("aba");
l.contains("aba");}
@Test public void testInvocations_meta35(){
List<String> l=new ArrayList<>();
l.add("aba");
assertThat(l).isNotNull();
l.contains("aba");}
@Test public void testInvocations_meta36(){
List<String> l=new ArrayList<>();
l.add("aba");
assertThat(l.contains("aba")).isTrue();}
}
