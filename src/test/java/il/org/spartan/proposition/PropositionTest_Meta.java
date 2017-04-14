package il.org.spartan.proposition;
import static il.org.spartan.azzert.*;
import static il.org.spartan.utils.Proposition.*;
import static il.org.spartan.utils.Proposition.not;
import static il.org.spartan.utils.Proposition.that;
import java.util.*;
import java.util.function.*;
import org.junit.*;
import org.junit.runners.*;
import il.org.spartan.*;
import il.org.spartan.utils.*;
/** 
 * Tests class   {@link Proposition}
 * @author  Yossi Gil  {@code    Yossi.Gil@GMail.COM}
 * @since  2017-03-08 
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) @SuppressWarnings("all") public class PropositionTest_Meta {
  private static boolean ignoreNext(){
    return true;
  }
  @Before public void setUp(){
    B1=T_OR_F_OR_X=Proposition.OR("T OR F OR X",T,F,X);
    B2=T_AND_F_AND_X=Proposition.AND("T AND F AND X",T,F,X);
    B3=NOT_F_AND_NOT_F_OR_X_OR_N_OR_T=not(F).and(not(F)).or(X).or(N,T);
    B4=T_OR_F_OR_X_OR_NOT_F_AND_NOT_F_OR_X_OR_N_OR_T=Proposition.OR(T_OR_F_OR_X,NOT_F_AND_NOT_F_OR_X_OR_N_OR_T);
    B5=B1_AND_B2=B1.and(B2);
    B6=B2_AND_B1=B2.and(B1);
    B7=B1_OR_B2=B1.or(B2);
    B8=B2_OR_B1=B2.or(B1);
  }
  private boolean hasCycles(  final BooleanSupplier s){
    final Stack<BooleanSupplier> path=new Stack<>();
    path.add(s);
    final Queue<BooleanSupplier> todo=new LinkedList<>();
    do {
      final BooleanSupplier current=todo.isEmpty() ? path.pop() : todo.remove();
      if (path.contains(current))       return true;
      if (current instanceof Proposition.Some) {
        todo.addAll(((Proposition.Some)current).inner);
        continue;
      }
      if (current instanceof Proposition.Singleton)       path.push(((Proposition.Singleton)current).inner);
    }
 while (!path.isEmpty());
    return false;
  }
  private Proposition B1, T_OR_F_OR_X;
  private Proposition B2, T_AND_F_AND_X;
  private Proposition B3, NOT_F_AND_NOT_F_OR_X_OR_N_OR_T;
  private Proposition B4, T_OR_F_OR_X_OR_NOT_F_AND_NOT_F_OR_X_OR_N_OR_T;
  private Proposition B5, B1_AND_B2;
  private Proposition B6, B2_AND_B1;
  private Proposition B7, B1_OR_B2;
  private Proposition B8, B2_OR_B1;
  private final PropositionJavaNotation javaReducer=new PropositionJavaNotation();
  Proposition condition;
  Proposition inner;
  Object object;
  BooleanSupplier supplier;
@Test(expected=AssertionError.class) public void test1(){
X.getAsBoolean();

}@Test public void test2(){
object=Proposition.T;

}@Test public void test3(){
object=Proposition.T;

assert object != null;

}@Test public void test4(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

}@Test public void test5(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

}@Test public void test6(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

}@Test public void test7(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

}@Test public void test8(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

}@Test public void test9(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

}@Test public void test10(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

}@Test public void test11(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

}@Test public void test12(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

}@Test public void test13(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

}@Test public void test14(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

}@Test public void test15(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

}@Test public void test16(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

}@Test public void test17(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

}@Test public void test18(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

}@Test public void test19(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

}@Test public void test20(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

}@Test public void test21(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

}@Test public void test22(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

}@Test public void test23(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

}@Test public void test24(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

}@Test public void test25(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

}@Test public void test26(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

}@Test public void test27(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

}@Test public void test28(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

}@Test public void test29(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

}@Test public void test30(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

}@Test public void test31(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

}@Test public void test32(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

}@Test public void test33(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

}@Test public void test34(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

}@Test public void test35(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

}@Test public void test36(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

}@Test public void test37(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

}@Test public void test38(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

}@Test public void test39(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

}@Test public void test40(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

}@Test public void test41(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

}@Test public void test42(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

}@Test public void test43(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

}@Test public void test44(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

}@Test public void test45(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

}@Test public void test46(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

}@Test public void test47(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

}@Test public void test48(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

}@Test public void test49(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

}@Test public void test50(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

}@Test public void test51(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

}@Test public void test52(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

}@Test public void test53(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

}@Test public void test54(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

}@Test public void test55(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

}@Test public void test56(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

}@Test public void test57(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

}@Test public void test58(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

}@Test public void test59(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

}@Test public void test60(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

}@Test public void test61(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

}@Test public void test62(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

}@Test public void test63(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

Proposition.AND(T,T);

}@Test public void test64(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

Proposition.AND(T,T);

Proposition.AND(T,T,T);

}@Test public void test65(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

Proposition.AND(T,T);

Proposition.AND(T,T,T);

Proposition.AND(T,T,T,T);

}@Test public void test66(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

Proposition.AND(T,T);

Proposition.AND(T,T,T);

Proposition.AND(T,T,T,T);

assert Proposition.AND(T,T).getAsBoolean();

}@Test public void test67(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

Proposition.AND(T,T);

Proposition.AND(T,T,T);

Proposition.AND(T,T,T,T);

assert Proposition.AND(T,T).getAsBoolean();

supplier=AND(T,T);

}@Test public void test68(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

Proposition.AND(T,T);

Proposition.AND(T,T,T);

Proposition.AND(T,T,T,T);

assert Proposition.AND(T,T).getAsBoolean();

supplier=AND(T,T);

condition=AND(T,T);

}@Test public void test69(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

Proposition.AND(T,T);

Proposition.AND(T,T,T);

Proposition.AND(T,T,T,T);

assert Proposition.AND(T,T).getAsBoolean();

supplier=AND(T,T);

condition=AND(T,T);

inner=AND(T,T);

}@Test public void test70(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

Proposition.AND(T,T);

Proposition.AND(T,T,T);

Proposition.AND(T,T,T,T);

assert Proposition.AND(T,T).getAsBoolean();

supplier=AND(T,T);

condition=AND(T,T);

inner=AND(T,T);

assert Proposition.AND(T,T).getAsBoolean();

}@Test public void test71(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

Proposition.AND(T,T);

Proposition.AND(T,T,T);

Proposition.AND(T,T,T,T);

assert Proposition.AND(T,T).getAsBoolean();

supplier=AND(T,T);

condition=AND(T,T);

inner=AND(T,T);

assert Proposition.AND(T,T).getAsBoolean();

assert !Proposition.AND(T,F).getAsBoolean();

}@Test public void test72(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

Proposition.AND(T,T);

Proposition.AND(T,T,T);

Proposition.AND(T,T,T,T);

assert Proposition.AND(T,T).getAsBoolean();

supplier=AND(T,T);

condition=AND(T,T);

inner=AND(T,T);

assert Proposition.AND(T,T).getAsBoolean();

assert !Proposition.AND(T,F).getAsBoolean();

assert !Proposition.AND(F,T).getAsBoolean();

}@Test public void test73(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

Proposition.AND(T,T);

Proposition.AND(T,T,T);

Proposition.AND(T,T,T,T);

assert Proposition.AND(T,T).getAsBoolean();

supplier=AND(T,T);

condition=AND(T,T);

inner=AND(T,T);

assert Proposition.AND(T,T).getAsBoolean();

assert !Proposition.AND(T,F).getAsBoolean();

assert !Proposition.AND(F,T).getAsBoolean();

assert !Proposition.AND(F,F).getAsBoolean();

}@Test public void test74(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

Proposition.AND(T,T);

Proposition.AND(T,T,T);

Proposition.AND(T,T,T,T);

assert Proposition.AND(T,T).getAsBoolean();

supplier=AND(T,T);

condition=AND(T,T);

inner=AND(T,T);

assert Proposition.AND(T,T).getAsBoolean();

assert !Proposition.AND(T,F).getAsBoolean();

assert !Proposition.AND(F,T).getAsBoolean();

assert !Proposition.AND(F,F).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

}@Test public void test75(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

Proposition.AND(T,T);

Proposition.AND(T,T,T);

Proposition.AND(T,T,T,T);

assert Proposition.AND(T,T).getAsBoolean();

supplier=AND(T,T);

condition=AND(T,T);

inner=AND(T,T);

assert Proposition.AND(T,T).getAsBoolean();

assert !Proposition.AND(T,F).getAsBoolean();

assert !Proposition.AND(F,T).getAsBoolean();

assert !Proposition.AND(F,F).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,T).getAsBoolean();

}@Test public void test76(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

Proposition.AND(T,T);

Proposition.AND(T,T,T);

Proposition.AND(T,T,T,T);

assert Proposition.AND(T,T).getAsBoolean();

supplier=AND(T,T);

condition=AND(T,T);

inner=AND(T,T);

assert Proposition.AND(T,T).getAsBoolean();

assert !Proposition.AND(T,F).getAsBoolean();

assert !Proposition.AND(F,T).getAsBoolean();

assert !Proposition.AND(F,F).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,T).getAsBoolean();

assert !Proposition.AND(F,T,T).getAsBoolean();

}@Test public void test77(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

Proposition.AND(T,T);

Proposition.AND(T,T,T);

Proposition.AND(T,T,T,T);

assert Proposition.AND(T,T).getAsBoolean();

supplier=AND(T,T);

condition=AND(T,T);

inner=AND(T,T);

assert Proposition.AND(T,T).getAsBoolean();

assert !Proposition.AND(T,F).getAsBoolean();

assert !Proposition.AND(F,T).getAsBoolean();

assert !Proposition.AND(F,F).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,T).getAsBoolean();

assert !Proposition.AND(F,T,T).getAsBoolean();

assert !Proposition.AND(F,F,T).getAsBoolean();

}@Test public void test78(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

Proposition.AND(T,T);

Proposition.AND(T,T,T);

Proposition.AND(T,T,T,T);

assert Proposition.AND(T,T).getAsBoolean();

supplier=AND(T,T);

condition=AND(T,T);

inner=AND(T,T);

assert Proposition.AND(T,T).getAsBoolean();

assert !Proposition.AND(T,F).getAsBoolean();

assert !Proposition.AND(F,T).getAsBoolean();

assert !Proposition.AND(F,F).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,T).getAsBoolean();

assert !Proposition.AND(F,T,T).getAsBoolean();

assert !Proposition.AND(F,F,T).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

}@Test public void test79(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

Proposition.AND(T,T);

Proposition.AND(T,T,T);

Proposition.AND(T,T,T,T);

assert Proposition.AND(T,T).getAsBoolean();

supplier=AND(T,T);

condition=AND(T,T);

inner=AND(T,T);

assert Proposition.AND(T,T).getAsBoolean();

assert !Proposition.AND(T,F).getAsBoolean();

assert !Proposition.AND(F,T).getAsBoolean();

assert !Proposition.AND(F,F).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,T).getAsBoolean();

assert !Proposition.AND(F,T,T).getAsBoolean();

assert !Proposition.AND(F,F,T).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,X).getAsBoolean();

}@Test public void test80(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

Proposition.AND(T,T);

Proposition.AND(T,T,T);

Proposition.AND(T,T,T,T);

assert Proposition.AND(T,T).getAsBoolean();

supplier=AND(T,T);

condition=AND(T,T);

inner=AND(T,T);

assert Proposition.AND(T,T).getAsBoolean();

assert !Proposition.AND(T,F).getAsBoolean();

assert !Proposition.AND(F,T).getAsBoolean();

assert !Proposition.AND(F,F).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,T).getAsBoolean();

assert !Proposition.AND(F,T,T).getAsBoolean();

assert !Proposition.AND(F,F,T).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,X).getAsBoolean();

assert !Proposition.AND(F,X,X).getAsBoolean();

}@Test public void test81(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

Proposition.AND(T,T);

Proposition.AND(T,T,T);

Proposition.AND(T,T,T,T);

assert Proposition.AND(T,T).getAsBoolean();

supplier=AND(T,T);

condition=AND(T,T);

inner=AND(T,T);

assert Proposition.AND(T,T).getAsBoolean();

assert !Proposition.AND(T,F).getAsBoolean();

assert !Proposition.AND(F,T).getAsBoolean();

assert !Proposition.AND(F,F).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,T).getAsBoolean();

assert !Proposition.AND(F,T,T).getAsBoolean();

assert !Proposition.AND(F,F,T).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,X).getAsBoolean();

assert !Proposition.AND(F,X,X).getAsBoolean();

assert !Proposition.AND(F,F,X).getAsBoolean();

}@Test public void test82(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

Proposition.AND(T,T);

Proposition.AND(T,T,T);

Proposition.AND(T,T,T,T);

assert Proposition.AND(T,T).getAsBoolean();

supplier=AND(T,T);

condition=AND(T,T);

inner=AND(T,T);

assert Proposition.AND(T,T).getAsBoolean();

assert !Proposition.AND(T,F).getAsBoolean();

assert !Proposition.AND(F,T).getAsBoolean();

assert !Proposition.AND(F,F).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,T).getAsBoolean();

assert !Proposition.AND(F,T,T).getAsBoolean();

assert !Proposition.AND(F,F,T).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,X).getAsBoolean();

assert !Proposition.AND(F,X,X).getAsBoolean();

assert !Proposition.AND(F,F,X).getAsBoolean();

Proposition.OR(T,T);

}@Test public void test83(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

Proposition.AND(T,T);

Proposition.AND(T,T,T);

Proposition.AND(T,T,T,T);

assert Proposition.AND(T,T).getAsBoolean();

supplier=AND(T,T);

condition=AND(T,T);

inner=AND(T,T);

assert Proposition.AND(T,T).getAsBoolean();

assert !Proposition.AND(T,F).getAsBoolean();

assert !Proposition.AND(F,T).getAsBoolean();

assert !Proposition.AND(F,F).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,T).getAsBoolean();

assert !Proposition.AND(F,T,T).getAsBoolean();

assert !Proposition.AND(F,F,T).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,X).getAsBoolean();

assert !Proposition.AND(F,X,X).getAsBoolean();

assert !Proposition.AND(F,F,X).getAsBoolean();

Proposition.OR(T,T);

Proposition.OR(T,T);

}@Test public void test84(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

Proposition.AND(T,T);

Proposition.AND(T,T,T);

Proposition.AND(T,T,T,T);

assert Proposition.AND(T,T).getAsBoolean();

supplier=AND(T,T);

condition=AND(T,T);

inner=AND(T,T);

assert Proposition.AND(T,T).getAsBoolean();

assert !Proposition.AND(T,F).getAsBoolean();

assert !Proposition.AND(F,T).getAsBoolean();

assert !Proposition.AND(F,F).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,T).getAsBoolean();

assert !Proposition.AND(F,T,T).getAsBoolean();

assert !Proposition.AND(F,F,T).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,X).getAsBoolean();

assert !Proposition.AND(F,X,X).getAsBoolean();

assert !Proposition.AND(F,F,X).getAsBoolean();

Proposition.OR(T,T);

Proposition.OR(T,T);

Proposition.OR(T,T,T);

}@Test public void test85(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

Proposition.AND(T,T);

Proposition.AND(T,T,T);

Proposition.AND(T,T,T,T);

assert Proposition.AND(T,T).getAsBoolean();

supplier=AND(T,T);

condition=AND(T,T);

inner=AND(T,T);

assert Proposition.AND(T,T).getAsBoolean();

assert !Proposition.AND(T,F).getAsBoolean();

assert !Proposition.AND(F,T).getAsBoolean();

assert !Proposition.AND(F,F).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,T).getAsBoolean();

assert !Proposition.AND(F,T,T).getAsBoolean();

assert !Proposition.AND(F,F,T).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,X).getAsBoolean();

assert !Proposition.AND(F,X,X).getAsBoolean();

assert !Proposition.AND(F,F,X).getAsBoolean();

Proposition.OR(T,T);

Proposition.OR(T,T);

Proposition.OR(T,T,T);

Proposition.OR(T,T,T,T);

}@Test public void test86(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

Proposition.AND(T,T);

Proposition.AND(T,T,T);

Proposition.AND(T,T,T,T);

assert Proposition.AND(T,T).getAsBoolean();

supplier=AND(T,T);

condition=AND(T,T);

inner=AND(T,T);

assert Proposition.AND(T,T).getAsBoolean();

assert !Proposition.AND(T,F).getAsBoolean();

assert !Proposition.AND(F,T).getAsBoolean();

assert !Proposition.AND(F,F).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,T).getAsBoolean();

assert !Proposition.AND(F,T,T).getAsBoolean();

assert !Proposition.AND(F,F,T).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,X).getAsBoolean();

assert !Proposition.AND(F,X,X).getAsBoolean();

assert !Proposition.AND(F,F,X).getAsBoolean();

Proposition.OR(T,T);

Proposition.OR(T,T);

Proposition.OR(T,T,T);

Proposition.OR(T,T,T,T);

assert Proposition.OR(T,T).getAsBoolean();

}@Test public void test87(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

Proposition.AND(T,T);

Proposition.AND(T,T,T);

Proposition.AND(T,T,T,T);

assert Proposition.AND(T,T).getAsBoolean();

supplier=AND(T,T);

condition=AND(T,T);

inner=AND(T,T);

assert Proposition.AND(T,T).getAsBoolean();

assert !Proposition.AND(T,F).getAsBoolean();

assert !Proposition.AND(F,T).getAsBoolean();

assert !Proposition.AND(F,F).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,T).getAsBoolean();

assert !Proposition.AND(F,T,T).getAsBoolean();

assert !Proposition.AND(F,F,T).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,X).getAsBoolean();

assert !Proposition.AND(F,X,X).getAsBoolean();

assert !Proposition.AND(F,F,X).getAsBoolean();

Proposition.OR(T,T);

Proposition.OR(T,T);

Proposition.OR(T,T,T);

Proposition.OR(T,T,T,T);

assert Proposition.OR(T,T).getAsBoolean();

supplier=OR(T,T);

}@Test public void test88(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

Proposition.AND(T,T);

Proposition.AND(T,T,T);

Proposition.AND(T,T,T,T);

assert Proposition.AND(T,T).getAsBoolean();

supplier=AND(T,T);

condition=AND(T,T);

inner=AND(T,T);

assert Proposition.AND(T,T).getAsBoolean();

assert !Proposition.AND(T,F).getAsBoolean();

assert !Proposition.AND(F,T).getAsBoolean();

assert !Proposition.AND(F,F).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,T).getAsBoolean();

assert !Proposition.AND(F,T,T).getAsBoolean();

assert !Proposition.AND(F,F,T).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,X).getAsBoolean();

assert !Proposition.AND(F,X,X).getAsBoolean();

assert !Proposition.AND(F,F,X).getAsBoolean();

Proposition.OR(T,T);

Proposition.OR(T,T);

Proposition.OR(T,T,T);

Proposition.OR(T,T,T,T);

assert Proposition.OR(T,T).getAsBoolean();

supplier=OR(T,T);

condition=OR(T,T);

}@Test public void test89(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

Proposition.AND(T,T);

Proposition.AND(T,T,T);

Proposition.AND(T,T,T,T);

assert Proposition.AND(T,T).getAsBoolean();

supplier=AND(T,T);

condition=AND(T,T);

inner=AND(T,T);

assert Proposition.AND(T,T).getAsBoolean();

assert !Proposition.AND(T,F).getAsBoolean();

assert !Proposition.AND(F,T).getAsBoolean();

assert !Proposition.AND(F,F).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,T).getAsBoolean();

assert !Proposition.AND(F,T,T).getAsBoolean();

assert !Proposition.AND(F,F,T).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,X).getAsBoolean();

assert !Proposition.AND(F,X,X).getAsBoolean();

assert !Proposition.AND(F,F,X).getAsBoolean();

Proposition.OR(T,T);

Proposition.OR(T,T);

Proposition.OR(T,T,T);

Proposition.OR(T,T,T,T);

assert Proposition.OR(T,T).getAsBoolean();

supplier=OR(T,T);

condition=OR(T,T);

inner=OR(T,T);

}@Test public void test90(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

Proposition.AND(T,T);

Proposition.AND(T,T,T);

Proposition.AND(T,T,T,T);

assert Proposition.AND(T,T).getAsBoolean();

supplier=AND(T,T);

condition=AND(T,T);

inner=AND(T,T);

assert Proposition.AND(T,T).getAsBoolean();

assert !Proposition.AND(T,F).getAsBoolean();

assert !Proposition.AND(F,T).getAsBoolean();

assert !Proposition.AND(F,F).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,T).getAsBoolean();

assert !Proposition.AND(F,T,T).getAsBoolean();

assert !Proposition.AND(F,F,T).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,X).getAsBoolean();

assert !Proposition.AND(F,X,X).getAsBoolean();

assert !Proposition.AND(F,F,X).getAsBoolean();

Proposition.OR(T,T);

Proposition.OR(T,T);

Proposition.OR(T,T,T);

Proposition.OR(T,T,T,T);

assert Proposition.OR(T,T).getAsBoolean();

supplier=OR(T,T);

condition=OR(T,T);

inner=OR(T,T);

assert Proposition.OR(T,T).getAsBoolean();

}@Test public void test91(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

Proposition.AND(T,T);

Proposition.AND(T,T,T);

Proposition.AND(T,T,T,T);

assert Proposition.AND(T,T).getAsBoolean();

supplier=AND(T,T);

condition=AND(T,T);

inner=AND(T,T);

assert Proposition.AND(T,T).getAsBoolean();

assert !Proposition.AND(T,F).getAsBoolean();

assert !Proposition.AND(F,T).getAsBoolean();

assert !Proposition.AND(F,F).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,T).getAsBoolean();

assert !Proposition.AND(F,T,T).getAsBoolean();

assert !Proposition.AND(F,F,T).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,X).getAsBoolean();

assert !Proposition.AND(F,X,X).getAsBoolean();

assert !Proposition.AND(F,F,X).getAsBoolean();

Proposition.OR(T,T);

Proposition.OR(T,T);

Proposition.OR(T,T,T);

Proposition.OR(T,T,T,T);

assert Proposition.OR(T,T).getAsBoolean();

supplier=OR(T,T);

condition=OR(T,T);

inner=OR(T,T);

assert Proposition.OR(T,T).getAsBoolean();

assert Proposition.OR(T,F).getAsBoolean();

}@Test public void test92(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

Proposition.AND(T,T);

Proposition.AND(T,T,T);

Proposition.AND(T,T,T,T);

assert Proposition.AND(T,T).getAsBoolean();

supplier=AND(T,T);

condition=AND(T,T);

inner=AND(T,T);

assert Proposition.AND(T,T).getAsBoolean();

assert !Proposition.AND(T,F).getAsBoolean();

assert !Proposition.AND(F,T).getAsBoolean();

assert !Proposition.AND(F,F).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,T).getAsBoolean();

assert !Proposition.AND(F,T,T).getAsBoolean();

assert !Proposition.AND(F,F,T).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,X).getAsBoolean();

assert !Proposition.AND(F,X,X).getAsBoolean();

assert !Proposition.AND(F,F,X).getAsBoolean();

Proposition.OR(T,T);

Proposition.OR(T,T);

Proposition.OR(T,T,T);

Proposition.OR(T,T,T,T);

assert Proposition.OR(T,T).getAsBoolean();

supplier=OR(T,T);

condition=OR(T,T);

inner=OR(T,T);

assert Proposition.OR(T,T).getAsBoolean();

assert Proposition.OR(T,F).getAsBoolean();

assert Proposition.OR(F,T).getAsBoolean();

}@Test public void test93(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

Proposition.AND(T,T);

Proposition.AND(T,T,T);

Proposition.AND(T,T,T,T);

assert Proposition.AND(T,T).getAsBoolean();

supplier=AND(T,T);

condition=AND(T,T);

inner=AND(T,T);

assert Proposition.AND(T,T).getAsBoolean();

assert !Proposition.AND(T,F).getAsBoolean();

assert !Proposition.AND(F,T).getAsBoolean();

assert !Proposition.AND(F,F).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,T).getAsBoolean();

assert !Proposition.AND(F,T,T).getAsBoolean();

assert !Proposition.AND(F,F,T).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,X).getAsBoolean();

assert !Proposition.AND(F,X,X).getAsBoolean();

assert !Proposition.AND(F,F,X).getAsBoolean();

Proposition.OR(T,T);

Proposition.OR(T,T);

Proposition.OR(T,T,T);

Proposition.OR(T,T,T,T);

assert Proposition.OR(T,T).getAsBoolean();

supplier=OR(T,T);

condition=OR(T,T);

inner=OR(T,T);

assert Proposition.OR(T,T).getAsBoolean();

assert Proposition.OR(T,F).getAsBoolean();

assert Proposition.OR(F,T).getAsBoolean();

assert !Proposition.OR(F,F).getAsBoolean();

}@Test public void test94(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

Proposition.AND(T,T);

Proposition.AND(T,T,T);

Proposition.AND(T,T,T,T);

assert Proposition.AND(T,T).getAsBoolean();

supplier=AND(T,T);

condition=AND(T,T);

inner=AND(T,T);

assert Proposition.AND(T,T).getAsBoolean();

assert !Proposition.AND(T,F).getAsBoolean();

assert !Proposition.AND(F,T).getAsBoolean();

assert !Proposition.AND(F,F).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,T).getAsBoolean();

assert !Proposition.AND(F,T,T).getAsBoolean();

assert !Proposition.AND(F,F,T).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,X).getAsBoolean();

assert !Proposition.AND(F,X,X).getAsBoolean();

assert !Proposition.AND(F,F,X).getAsBoolean();

Proposition.OR(T,T);

Proposition.OR(T,T);

Proposition.OR(T,T,T);

Proposition.OR(T,T,T,T);

assert Proposition.OR(T,T).getAsBoolean();

supplier=OR(T,T);

condition=OR(T,T);

inner=OR(T,T);

assert Proposition.OR(T,T).getAsBoolean();

assert Proposition.OR(T,F).getAsBoolean();

assert Proposition.OR(F,T).getAsBoolean();

assert !Proposition.OR(F,F).getAsBoolean();

assert !Proposition.OR(F,F,F).getAsBoolean();

}@Test public void test95(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

Proposition.AND(T,T);

Proposition.AND(T,T,T);

Proposition.AND(T,T,T,T);

assert Proposition.AND(T,T).getAsBoolean();

supplier=AND(T,T);

condition=AND(T,T);

inner=AND(T,T);

assert Proposition.AND(T,T).getAsBoolean();

assert !Proposition.AND(T,F).getAsBoolean();

assert !Proposition.AND(F,T).getAsBoolean();

assert !Proposition.AND(F,F).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,T).getAsBoolean();

assert !Proposition.AND(F,T,T).getAsBoolean();

assert !Proposition.AND(F,F,T).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,X).getAsBoolean();

assert !Proposition.AND(F,X,X).getAsBoolean();

assert !Proposition.AND(F,F,X).getAsBoolean();

Proposition.OR(T,T);

Proposition.OR(T,T);

Proposition.OR(T,T,T);

Proposition.OR(T,T,T,T);

assert Proposition.OR(T,T).getAsBoolean();

supplier=OR(T,T);

condition=OR(T,T);

inner=OR(T,T);

assert Proposition.OR(T,T).getAsBoolean();

assert Proposition.OR(T,F).getAsBoolean();

assert Proposition.OR(F,T).getAsBoolean();

assert !Proposition.OR(F,F).getAsBoolean();

assert !Proposition.OR(F,F,F).getAsBoolean();

assert Proposition.OR(F,F,T).getAsBoolean();

}@Test public void test96(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

Proposition.AND(T,T);

Proposition.AND(T,T,T);

Proposition.AND(T,T,T,T);

assert Proposition.AND(T,T).getAsBoolean();

supplier=AND(T,T);

condition=AND(T,T);

inner=AND(T,T);

assert Proposition.AND(T,T).getAsBoolean();

assert !Proposition.AND(T,F).getAsBoolean();

assert !Proposition.AND(F,T).getAsBoolean();

assert !Proposition.AND(F,F).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,T).getAsBoolean();

assert !Proposition.AND(F,T,T).getAsBoolean();

assert !Proposition.AND(F,F,T).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,X).getAsBoolean();

assert !Proposition.AND(F,X,X).getAsBoolean();

assert !Proposition.AND(F,F,X).getAsBoolean();

Proposition.OR(T,T);

Proposition.OR(T,T);

Proposition.OR(T,T,T);

Proposition.OR(T,T,T,T);

assert Proposition.OR(T,T).getAsBoolean();

supplier=OR(T,T);

condition=OR(T,T);

inner=OR(T,T);

assert Proposition.OR(T,T).getAsBoolean();

assert Proposition.OR(T,F).getAsBoolean();

assert Proposition.OR(F,T).getAsBoolean();

assert !Proposition.OR(F,F).getAsBoolean();

assert !Proposition.OR(F,F,F).getAsBoolean();

assert Proposition.OR(F,F,T).getAsBoolean();

assert Proposition.OR(F,T,F).getAsBoolean();

}@Test public void test97(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

Proposition.AND(T,T);

Proposition.AND(T,T,T);

Proposition.AND(T,T,T,T);

assert Proposition.AND(T,T).getAsBoolean();

supplier=AND(T,T);

condition=AND(T,T);

inner=AND(T,T);

assert Proposition.AND(T,T).getAsBoolean();

assert !Proposition.AND(T,F).getAsBoolean();

assert !Proposition.AND(F,T).getAsBoolean();

assert !Proposition.AND(F,F).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,T).getAsBoolean();

assert !Proposition.AND(F,T,T).getAsBoolean();

assert !Proposition.AND(F,F,T).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,X).getAsBoolean();

assert !Proposition.AND(F,X,X).getAsBoolean();

assert !Proposition.AND(F,F,X).getAsBoolean();

Proposition.OR(T,T);

Proposition.OR(T,T);

Proposition.OR(T,T,T);

Proposition.OR(T,T,T,T);

assert Proposition.OR(T,T).getAsBoolean();

supplier=OR(T,T);

condition=OR(T,T);

inner=OR(T,T);

assert Proposition.OR(T,T).getAsBoolean();

assert Proposition.OR(T,F).getAsBoolean();

assert Proposition.OR(F,T).getAsBoolean();

assert !Proposition.OR(F,F).getAsBoolean();

assert !Proposition.OR(F,F,F).getAsBoolean();

assert Proposition.OR(F,F,T).getAsBoolean();

assert Proposition.OR(F,T,F).getAsBoolean();

assert Proposition.OR(F,T,T).getAsBoolean();

}@Test public void test98(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

Proposition.AND(T,T);

Proposition.AND(T,T,T);

Proposition.AND(T,T,T,T);

assert Proposition.AND(T,T).getAsBoolean();

supplier=AND(T,T);

condition=AND(T,T);

inner=AND(T,T);

assert Proposition.AND(T,T).getAsBoolean();

assert !Proposition.AND(T,F).getAsBoolean();

assert !Proposition.AND(F,T).getAsBoolean();

assert !Proposition.AND(F,F).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,T).getAsBoolean();

assert !Proposition.AND(F,T,T).getAsBoolean();

assert !Proposition.AND(F,F,T).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,X).getAsBoolean();

assert !Proposition.AND(F,X,X).getAsBoolean();

assert !Proposition.AND(F,F,X).getAsBoolean();

Proposition.OR(T,T);

Proposition.OR(T,T);

Proposition.OR(T,T,T);

Proposition.OR(T,T,T,T);

assert Proposition.OR(T,T).getAsBoolean();

supplier=OR(T,T);

condition=OR(T,T);

inner=OR(T,T);

assert Proposition.OR(T,T).getAsBoolean();

assert Proposition.OR(T,F).getAsBoolean();

assert Proposition.OR(F,T).getAsBoolean();

assert !Proposition.OR(F,F).getAsBoolean();

assert !Proposition.OR(F,F,F).getAsBoolean();

assert Proposition.OR(F,F,T).getAsBoolean();

assert Proposition.OR(F,T,F).getAsBoolean();

assert Proposition.OR(F,T,T).getAsBoolean();

assert Proposition.OR(T,F,F).getAsBoolean();

}@Test public void test99(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

Proposition.AND(T,T);

Proposition.AND(T,T,T);

Proposition.AND(T,T,T,T);

assert Proposition.AND(T,T).getAsBoolean();

supplier=AND(T,T);

condition=AND(T,T);

inner=AND(T,T);

assert Proposition.AND(T,T).getAsBoolean();

assert !Proposition.AND(T,F).getAsBoolean();

assert !Proposition.AND(F,T).getAsBoolean();

assert !Proposition.AND(F,F).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,T).getAsBoolean();

assert !Proposition.AND(F,T,T).getAsBoolean();

assert !Proposition.AND(F,F,T).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,X).getAsBoolean();

assert !Proposition.AND(F,X,X).getAsBoolean();

assert !Proposition.AND(F,F,X).getAsBoolean();

Proposition.OR(T,T);

Proposition.OR(T,T);

Proposition.OR(T,T,T);

Proposition.OR(T,T,T,T);

assert Proposition.OR(T,T).getAsBoolean();

supplier=OR(T,T);

condition=OR(T,T);

inner=OR(T,T);

assert Proposition.OR(T,T).getAsBoolean();

assert Proposition.OR(T,F).getAsBoolean();

assert Proposition.OR(F,T).getAsBoolean();

assert !Proposition.OR(F,F).getAsBoolean();

assert !Proposition.OR(F,F,F).getAsBoolean();

assert Proposition.OR(F,F,T).getAsBoolean();

assert Proposition.OR(F,T,F).getAsBoolean();

assert Proposition.OR(F,T,T).getAsBoolean();

assert Proposition.OR(T,F,F).getAsBoolean();

assert Proposition.OR(T,F,T).getAsBoolean();

}@Test public void test100(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

Proposition.AND(T,T);

Proposition.AND(T,T,T);

Proposition.AND(T,T,T,T);

assert Proposition.AND(T,T).getAsBoolean();

supplier=AND(T,T);

condition=AND(T,T);

inner=AND(T,T);

assert Proposition.AND(T,T).getAsBoolean();

assert !Proposition.AND(T,F).getAsBoolean();

assert !Proposition.AND(F,T).getAsBoolean();

assert !Proposition.AND(F,F).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,T).getAsBoolean();

assert !Proposition.AND(F,T,T).getAsBoolean();

assert !Proposition.AND(F,F,T).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,X).getAsBoolean();

assert !Proposition.AND(F,X,X).getAsBoolean();

assert !Proposition.AND(F,F,X).getAsBoolean();

Proposition.OR(T,T);

Proposition.OR(T,T);

Proposition.OR(T,T,T);

Proposition.OR(T,T,T,T);

assert Proposition.OR(T,T).getAsBoolean();

supplier=OR(T,T);

condition=OR(T,T);

inner=OR(T,T);

assert Proposition.OR(T,T).getAsBoolean();

assert Proposition.OR(T,F).getAsBoolean();

assert Proposition.OR(F,T).getAsBoolean();

assert !Proposition.OR(F,F).getAsBoolean();

assert !Proposition.OR(F,F,F).getAsBoolean();

assert Proposition.OR(F,F,T).getAsBoolean();

assert Proposition.OR(F,T,F).getAsBoolean();

assert Proposition.OR(F,T,T).getAsBoolean();

assert Proposition.OR(T,F,F).getAsBoolean();

assert Proposition.OR(T,F,T).getAsBoolean();

assert Proposition.OR(T,T,F).getAsBoolean();

}@Test public void test101(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

Proposition.AND(T,T);

Proposition.AND(T,T,T);

Proposition.AND(T,T,T,T);

assert Proposition.AND(T,T).getAsBoolean();

supplier=AND(T,T);

condition=AND(T,T);

inner=AND(T,T);

assert Proposition.AND(T,T).getAsBoolean();

assert !Proposition.AND(T,F).getAsBoolean();

assert !Proposition.AND(F,T).getAsBoolean();

assert !Proposition.AND(F,F).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,T).getAsBoolean();

assert !Proposition.AND(F,T,T).getAsBoolean();

assert !Proposition.AND(F,F,T).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,X).getAsBoolean();

assert !Proposition.AND(F,X,X).getAsBoolean();

assert !Proposition.AND(F,F,X).getAsBoolean();

Proposition.OR(T,T);

Proposition.OR(T,T);

Proposition.OR(T,T,T);

Proposition.OR(T,T,T,T);

assert Proposition.OR(T,T).getAsBoolean();

supplier=OR(T,T);

condition=OR(T,T);

inner=OR(T,T);

assert Proposition.OR(T,T).getAsBoolean();

assert Proposition.OR(T,F).getAsBoolean();

assert Proposition.OR(F,T).getAsBoolean();

assert !Proposition.OR(F,F).getAsBoolean();

assert !Proposition.OR(F,F,F).getAsBoolean();

assert Proposition.OR(F,F,T).getAsBoolean();

assert Proposition.OR(F,T,F).getAsBoolean();

assert Proposition.OR(F,T,T).getAsBoolean();

assert Proposition.OR(T,F,F).getAsBoolean();

assert Proposition.OR(T,F,T).getAsBoolean();

assert Proposition.OR(T,T,F).getAsBoolean();

assert Proposition.OR(T,T,T).getAsBoolean();

}@Test public void test102(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

Proposition.AND(T,T);

Proposition.AND(T,T,T);

Proposition.AND(T,T,T,T);

assert Proposition.AND(T,T).getAsBoolean();

supplier=AND(T,T);

condition=AND(T,T);

inner=AND(T,T);

assert Proposition.AND(T,T).getAsBoolean();

assert !Proposition.AND(T,F).getAsBoolean();

assert !Proposition.AND(F,T).getAsBoolean();

assert !Proposition.AND(F,F).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,T).getAsBoolean();

assert !Proposition.AND(F,T,T).getAsBoolean();

assert !Proposition.AND(F,F,T).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,X).getAsBoolean();

assert !Proposition.AND(F,X,X).getAsBoolean();

assert !Proposition.AND(F,F,X).getAsBoolean();

Proposition.OR(T,T);

Proposition.OR(T,T);

Proposition.OR(T,T,T);

Proposition.OR(T,T,T,T);

assert Proposition.OR(T,T).getAsBoolean();

supplier=OR(T,T);

condition=OR(T,T);

inner=OR(T,T);

assert Proposition.OR(T,T).getAsBoolean();

assert Proposition.OR(T,F).getAsBoolean();

assert Proposition.OR(F,T).getAsBoolean();

assert !Proposition.OR(F,F).getAsBoolean();

assert !Proposition.OR(F,F,F).getAsBoolean();

assert Proposition.OR(F,F,T).getAsBoolean();

assert Proposition.OR(F,T,F).getAsBoolean();

assert Proposition.OR(F,T,T).getAsBoolean();

assert Proposition.OR(T,F,F).getAsBoolean();

assert Proposition.OR(T,F,T).getAsBoolean();

assert Proposition.OR(T,T,F).getAsBoolean();

assert Proposition.OR(T,T,T).getAsBoolean();

assert Proposition.OR(F,T,X).getAsBoolean();

}@Test public void test103(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

Proposition.AND(T,T);

Proposition.AND(T,T,T);

Proposition.AND(T,T,T,T);

assert Proposition.AND(T,T).getAsBoolean();

supplier=AND(T,T);

condition=AND(T,T);

inner=AND(T,T);

assert Proposition.AND(T,T).getAsBoolean();

assert !Proposition.AND(T,F).getAsBoolean();

assert !Proposition.AND(F,T).getAsBoolean();

assert !Proposition.AND(F,F).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,T).getAsBoolean();

assert !Proposition.AND(F,T,T).getAsBoolean();

assert !Proposition.AND(F,F,T).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,X).getAsBoolean();

assert !Proposition.AND(F,X,X).getAsBoolean();

assert !Proposition.AND(F,F,X).getAsBoolean();

Proposition.OR(T,T);

Proposition.OR(T,T);

Proposition.OR(T,T,T);

Proposition.OR(T,T,T,T);

assert Proposition.OR(T,T).getAsBoolean();

supplier=OR(T,T);

condition=OR(T,T);

inner=OR(T,T);

assert Proposition.OR(T,T).getAsBoolean();

assert Proposition.OR(T,F).getAsBoolean();

assert Proposition.OR(F,T).getAsBoolean();

assert !Proposition.OR(F,F).getAsBoolean();

assert !Proposition.OR(F,F,F).getAsBoolean();

assert Proposition.OR(F,F,T).getAsBoolean();

assert Proposition.OR(F,T,F).getAsBoolean();

assert Proposition.OR(F,T,T).getAsBoolean();

assert Proposition.OR(T,F,F).getAsBoolean();

assert Proposition.OR(T,F,T).getAsBoolean();

assert Proposition.OR(T,T,F).getAsBoolean();

assert Proposition.OR(T,T,T).getAsBoolean();

assert Proposition.OR(F,T,X).getAsBoolean();

assert Proposition.OR(T,X,X).getAsBoolean();

}@Test public void test104(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

Proposition.AND(T,T);

Proposition.AND(T,T,T);

Proposition.AND(T,T,T,T);

assert Proposition.AND(T,T).getAsBoolean();

supplier=AND(T,T);

condition=AND(T,T);

inner=AND(T,T);

assert Proposition.AND(T,T).getAsBoolean();

assert !Proposition.AND(T,F).getAsBoolean();

assert !Proposition.AND(F,T).getAsBoolean();

assert !Proposition.AND(F,F).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,T).getAsBoolean();

assert !Proposition.AND(F,T,T).getAsBoolean();

assert !Proposition.AND(F,F,T).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,X).getAsBoolean();

assert !Proposition.AND(F,X,X).getAsBoolean();

assert !Proposition.AND(F,F,X).getAsBoolean();

Proposition.OR(T,T);

Proposition.OR(T,T);

Proposition.OR(T,T,T);

Proposition.OR(T,T,T,T);

assert Proposition.OR(T,T).getAsBoolean();

supplier=OR(T,T);

condition=OR(T,T);

inner=OR(T,T);

assert Proposition.OR(T,T).getAsBoolean();

assert Proposition.OR(T,F).getAsBoolean();

assert Proposition.OR(F,T).getAsBoolean();

assert !Proposition.OR(F,F).getAsBoolean();

assert !Proposition.OR(F,F,F).getAsBoolean();

assert Proposition.OR(F,F,T).getAsBoolean();

assert Proposition.OR(F,T,F).getAsBoolean();

assert Proposition.OR(F,T,T).getAsBoolean();

assert Proposition.OR(T,F,F).getAsBoolean();

assert Proposition.OR(T,F,T).getAsBoolean();

assert Proposition.OR(T,T,F).getAsBoolean();

assert Proposition.OR(T,T,T).getAsBoolean();

assert Proposition.OR(F,T,X).getAsBoolean();

assert Proposition.OR(T,X,X).getAsBoolean();

assert T_OR_F_OR_X.getAsBoolean();

}@Test public void test105(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

Proposition.AND(T,T);

Proposition.AND(T,T,T);

Proposition.AND(T,T,T,T);

assert Proposition.AND(T,T).getAsBoolean();

supplier=AND(T,T);

condition=AND(T,T);

inner=AND(T,T);

assert Proposition.AND(T,T).getAsBoolean();

assert !Proposition.AND(T,F).getAsBoolean();

assert !Proposition.AND(F,T).getAsBoolean();

assert !Proposition.AND(F,F).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,T).getAsBoolean();

assert !Proposition.AND(F,T,T).getAsBoolean();

assert !Proposition.AND(F,F,T).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,X).getAsBoolean();

assert !Proposition.AND(F,X,X).getAsBoolean();

assert !Proposition.AND(F,F,X).getAsBoolean();

Proposition.OR(T,T);

Proposition.OR(T,T);

Proposition.OR(T,T,T);

Proposition.OR(T,T,T,T);

assert Proposition.OR(T,T).getAsBoolean();

supplier=OR(T,T);

condition=OR(T,T);

inner=OR(T,T);

assert Proposition.OR(T,T).getAsBoolean();

assert Proposition.OR(T,F).getAsBoolean();

assert Proposition.OR(F,T).getAsBoolean();

assert !Proposition.OR(F,F).getAsBoolean();

assert !Proposition.OR(F,F,F).getAsBoolean();

assert Proposition.OR(F,F,T).getAsBoolean();

assert Proposition.OR(F,T,F).getAsBoolean();

assert Proposition.OR(F,T,T).getAsBoolean();

assert Proposition.OR(T,F,F).getAsBoolean();

assert Proposition.OR(T,F,T).getAsBoolean();

assert Proposition.OR(T,T,F).getAsBoolean();

assert Proposition.OR(T,T,T).getAsBoolean();

assert Proposition.OR(F,T,X).getAsBoolean();

assert Proposition.OR(T,X,X).getAsBoolean();

assert T_OR_F_OR_X.getAsBoolean();

assert Proposition.OR(T,X,X).getAsBoolean();

}@Test public void test106(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

Proposition.AND(T,T);

Proposition.AND(T,T,T);

Proposition.AND(T,T,T,T);

assert Proposition.AND(T,T).getAsBoolean();

supplier=AND(T,T);

condition=AND(T,T);

inner=AND(T,T);

assert Proposition.AND(T,T).getAsBoolean();

assert !Proposition.AND(T,F).getAsBoolean();

assert !Proposition.AND(F,T).getAsBoolean();

assert !Proposition.AND(F,F).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,T).getAsBoolean();

assert !Proposition.AND(F,T,T).getAsBoolean();

assert !Proposition.AND(F,F,T).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,X).getAsBoolean();

assert !Proposition.AND(F,X,X).getAsBoolean();

assert !Proposition.AND(F,F,X).getAsBoolean();

Proposition.OR(T,T);

Proposition.OR(T,T);

Proposition.OR(T,T,T);

Proposition.OR(T,T,T,T);

assert Proposition.OR(T,T).getAsBoolean();

supplier=OR(T,T);

condition=OR(T,T);

inner=OR(T,T);

assert Proposition.OR(T,T).getAsBoolean();

assert Proposition.OR(T,F).getAsBoolean();

assert Proposition.OR(F,T).getAsBoolean();

assert !Proposition.OR(F,F).getAsBoolean();

assert !Proposition.OR(F,F,F).getAsBoolean();

assert Proposition.OR(F,F,T).getAsBoolean();

assert Proposition.OR(F,T,F).getAsBoolean();

assert Proposition.OR(F,T,T).getAsBoolean();

assert Proposition.OR(T,F,F).getAsBoolean();

assert Proposition.OR(T,F,T).getAsBoolean();

assert Proposition.OR(T,T,F).getAsBoolean();

assert Proposition.OR(T,T,T).getAsBoolean();

assert Proposition.OR(F,T,X).getAsBoolean();

assert Proposition.OR(T,X,X).getAsBoolean();

assert T_OR_F_OR_X.getAsBoolean();

assert Proposition.OR(T,X,X).getAsBoolean();

assert Proposition.not(F).getAsBoolean();

}@Test public void test107(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

Proposition.AND(T,T);

Proposition.AND(T,T,T);

Proposition.AND(T,T,T,T);

assert Proposition.AND(T,T).getAsBoolean();

supplier=AND(T,T);

condition=AND(T,T);

inner=AND(T,T);

assert Proposition.AND(T,T).getAsBoolean();

assert !Proposition.AND(T,F).getAsBoolean();

assert !Proposition.AND(F,T).getAsBoolean();

assert !Proposition.AND(F,F).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,T).getAsBoolean();

assert !Proposition.AND(F,T,T).getAsBoolean();

assert !Proposition.AND(F,F,T).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,X).getAsBoolean();

assert !Proposition.AND(F,X,X).getAsBoolean();

assert !Proposition.AND(F,F,X).getAsBoolean();

Proposition.OR(T,T);

Proposition.OR(T,T);

Proposition.OR(T,T,T);

Proposition.OR(T,T,T,T);

assert Proposition.OR(T,T).getAsBoolean();

supplier=OR(T,T);

condition=OR(T,T);

inner=OR(T,T);

assert Proposition.OR(T,T).getAsBoolean();

assert Proposition.OR(T,F).getAsBoolean();

assert Proposition.OR(F,T).getAsBoolean();

assert !Proposition.OR(F,F).getAsBoolean();

assert !Proposition.OR(F,F,F).getAsBoolean();

assert Proposition.OR(F,F,T).getAsBoolean();

assert Proposition.OR(F,T,F).getAsBoolean();

assert Proposition.OR(F,T,T).getAsBoolean();

assert Proposition.OR(T,F,F).getAsBoolean();

assert Proposition.OR(T,F,T).getAsBoolean();

assert Proposition.OR(T,T,F).getAsBoolean();

assert Proposition.OR(T,T,T).getAsBoolean();

assert Proposition.OR(F,T,X).getAsBoolean();

assert Proposition.OR(T,X,X).getAsBoolean();

assert T_OR_F_OR_X.getAsBoolean();

assert Proposition.OR(T,X,X).getAsBoolean();

assert Proposition.not(F).getAsBoolean();

assert !Proposition.not(T).getAsBoolean();

}@Test public void test108(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

Proposition.AND(T,T);

Proposition.AND(T,T,T);

Proposition.AND(T,T,T,T);

assert Proposition.AND(T,T).getAsBoolean();

supplier=AND(T,T);

condition=AND(T,T);

inner=AND(T,T);

assert Proposition.AND(T,T).getAsBoolean();

assert !Proposition.AND(T,F).getAsBoolean();

assert !Proposition.AND(F,T).getAsBoolean();

assert !Proposition.AND(F,F).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,T).getAsBoolean();

assert !Proposition.AND(F,T,T).getAsBoolean();

assert !Proposition.AND(F,F,T).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,X).getAsBoolean();

assert !Proposition.AND(F,X,X).getAsBoolean();

assert !Proposition.AND(F,F,X).getAsBoolean();

Proposition.OR(T,T);

Proposition.OR(T,T);

Proposition.OR(T,T,T);

Proposition.OR(T,T,T,T);

assert Proposition.OR(T,T).getAsBoolean();

supplier=OR(T,T);

condition=OR(T,T);

inner=OR(T,T);

assert Proposition.OR(T,T).getAsBoolean();

assert Proposition.OR(T,F).getAsBoolean();

assert Proposition.OR(F,T).getAsBoolean();

assert !Proposition.OR(F,F).getAsBoolean();

assert !Proposition.OR(F,F,F).getAsBoolean();

assert Proposition.OR(F,F,T).getAsBoolean();

assert Proposition.OR(F,T,F).getAsBoolean();

assert Proposition.OR(F,T,T).getAsBoolean();

assert Proposition.OR(T,F,F).getAsBoolean();

assert Proposition.OR(T,F,T).getAsBoolean();

assert Proposition.OR(T,T,F).getAsBoolean();

assert Proposition.OR(T,T,T).getAsBoolean();

assert Proposition.OR(F,T,X).getAsBoolean();

assert Proposition.OR(T,X,X).getAsBoolean();

assert T_OR_F_OR_X.getAsBoolean();

assert Proposition.OR(T,X,X).getAsBoolean();

assert Proposition.not(F).getAsBoolean();

assert !Proposition.not(T).getAsBoolean();

assert Proposition.not(F).and(not(F)).getAsBoolean();

}@Test public void test109(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

Proposition.AND(T,T);

Proposition.AND(T,T,T);

Proposition.AND(T,T,T,T);

assert Proposition.AND(T,T).getAsBoolean();

supplier=AND(T,T);

condition=AND(T,T);

inner=AND(T,T);

assert Proposition.AND(T,T).getAsBoolean();

assert !Proposition.AND(T,F).getAsBoolean();

assert !Proposition.AND(F,T).getAsBoolean();

assert !Proposition.AND(F,F).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,T).getAsBoolean();

assert !Proposition.AND(F,T,T).getAsBoolean();

assert !Proposition.AND(F,F,T).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,X).getAsBoolean();

assert !Proposition.AND(F,X,X).getAsBoolean();

assert !Proposition.AND(F,F,X).getAsBoolean();

Proposition.OR(T,T);

Proposition.OR(T,T);

Proposition.OR(T,T,T);

Proposition.OR(T,T,T,T);

assert Proposition.OR(T,T).getAsBoolean();

supplier=OR(T,T);

condition=OR(T,T);

inner=OR(T,T);

assert Proposition.OR(T,T).getAsBoolean();

assert Proposition.OR(T,F).getAsBoolean();

assert Proposition.OR(F,T).getAsBoolean();

assert !Proposition.OR(F,F).getAsBoolean();

assert !Proposition.OR(F,F,F).getAsBoolean();

assert Proposition.OR(F,F,T).getAsBoolean();

assert Proposition.OR(F,T,F).getAsBoolean();

assert Proposition.OR(F,T,T).getAsBoolean();

assert Proposition.OR(T,F,F).getAsBoolean();

assert Proposition.OR(T,F,T).getAsBoolean();

assert Proposition.OR(T,T,F).getAsBoolean();

assert Proposition.OR(T,T,T).getAsBoolean();

assert Proposition.OR(F,T,X).getAsBoolean();

assert Proposition.OR(T,X,X).getAsBoolean();

assert T_OR_F_OR_X.getAsBoolean();

assert Proposition.OR(T,X,X).getAsBoolean();

assert Proposition.not(F).getAsBoolean();

assert !Proposition.not(T).getAsBoolean();

assert Proposition.not(F).and(not(F)).getAsBoolean();

assert !Proposition.not(F).and(not(T)).getAsBoolean();

}@Test public void test110(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

Proposition.AND(T,T);

Proposition.AND(T,T,T);

Proposition.AND(T,T,T,T);

assert Proposition.AND(T,T).getAsBoolean();

supplier=AND(T,T);

condition=AND(T,T);

inner=AND(T,T);

assert Proposition.AND(T,T).getAsBoolean();

assert !Proposition.AND(T,F).getAsBoolean();

assert !Proposition.AND(F,T).getAsBoolean();

assert !Proposition.AND(F,F).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,T).getAsBoolean();

assert !Proposition.AND(F,T,T).getAsBoolean();

assert !Proposition.AND(F,F,T).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,X).getAsBoolean();

assert !Proposition.AND(F,X,X).getAsBoolean();

assert !Proposition.AND(F,F,X).getAsBoolean();

Proposition.OR(T,T);

Proposition.OR(T,T);

Proposition.OR(T,T,T);

Proposition.OR(T,T,T,T);

assert Proposition.OR(T,T).getAsBoolean();

supplier=OR(T,T);

condition=OR(T,T);

inner=OR(T,T);

assert Proposition.OR(T,T).getAsBoolean();

assert Proposition.OR(T,F).getAsBoolean();

assert Proposition.OR(F,T).getAsBoolean();

assert !Proposition.OR(F,F).getAsBoolean();

assert !Proposition.OR(F,F,F).getAsBoolean();

assert Proposition.OR(F,F,T).getAsBoolean();

assert Proposition.OR(F,T,F).getAsBoolean();

assert Proposition.OR(F,T,T).getAsBoolean();

assert Proposition.OR(T,F,F).getAsBoolean();

assert Proposition.OR(T,F,T).getAsBoolean();

assert Proposition.OR(T,T,F).getAsBoolean();

assert Proposition.OR(T,T,T).getAsBoolean();

assert Proposition.OR(F,T,X).getAsBoolean();

assert Proposition.OR(T,X,X).getAsBoolean();

assert T_OR_F_OR_X.getAsBoolean();

assert Proposition.OR(T,X,X).getAsBoolean();

assert Proposition.not(F).getAsBoolean();

assert !Proposition.not(T).getAsBoolean();

assert Proposition.not(F).and(not(F)).getAsBoolean();

assert !Proposition.not(F).and(not(T)).getAsBoolean();

assert Proposition.not(F).and(not(F)).or(T).getAsBoolean();

}@Test public void test111(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

Proposition.AND(T,T);

Proposition.AND(T,T,T);

Proposition.AND(T,T,T,T);

assert Proposition.AND(T,T).getAsBoolean();

supplier=AND(T,T);

condition=AND(T,T);

inner=AND(T,T);

assert Proposition.AND(T,T).getAsBoolean();

assert !Proposition.AND(T,F).getAsBoolean();

assert !Proposition.AND(F,T).getAsBoolean();

assert !Proposition.AND(F,F).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,T).getAsBoolean();

assert !Proposition.AND(F,T,T).getAsBoolean();

assert !Proposition.AND(F,F,T).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,X).getAsBoolean();

assert !Proposition.AND(F,X,X).getAsBoolean();

assert !Proposition.AND(F,F,X).getAsBoolean();

Proposition.OR(T,T);

Proposition.OR(T,T);

Proposition.OR(T,T,T);

Proposition.OR(T,T,T,T);

assert Proposition.OR(T,T).getAsBoolean();

supplier=OR(T,T);

condition=OR(T,T);

inner=OR(T,T);

assert Proposition.OR(T,T).getAsBoolean();

assert Proposition.OR(T,F).getAsBoolean();

assert Proposition.OR(F,T).getAsBoolean();

assert !Proposition.OR(F,F).getAsBoolean();

assert !Proposition.OR(F,F,F).getAsBoolean();

assert Proposition.OR(F,F,T).getAsBoolean();

assert Proposition.OR(F,T,F).getAsBoolean();

assert Proposition.OR(F,T,T).getAsBoolean();

assert Proposition.OR(T,F,F).getAsBoolean();

assert Proposition.OR(T,F,T).getAsBoolean();

assert Proposition.OR(T,T,F).getAsBoolean();

assert Proposition.OR(T,T,T).getAsBoolean();

assert Proposition.OR(F,T,X).getAsBoolean();

assert Proposition.OR(T,X,X).getAsBoolean();

assert T_OR_F_OR_X.getAsBoolean();

assert Proposition.OR(T,X,X).getAsBoolean();

assert Proposition.not(F).getAsBoolean();

assert !Proposition.not(T).getAsBoolean();

assert Proposition.not(F).and(not(F)).getAsBoolean();

assert !Proposition.not(F).and(not(T)).getAsBoolean();

assert Proposition.not(F).and(not(F)).or(T).getAsBoolean();

assert Proposition.not(F).and(not(F)).or(T).eval();

}@Test public void test112(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

Proposition.AND(T,T);

Proposition.AND(T,T,T);

Proposition.AND(T,T,T,T);

assert Proposition.AND(T,T).getAsBoolean();

supplier=AND(T,T);

condition=AND(T,T);

inner=AND(T,T);

assert Proposition.AND(T,T).getAsBoolean();

assert !Proposition.AND(T,F).getAsBoolean();

assert !Proposition.AND(F,T).getAsBoolean();

assert !Proposition.AND(F,F).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,T).getAsBoolean();

assert !Proposition.AND(F,T,T).getAsBoolean();

assert !Proposition.AND(F,F,T).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,X).getAsBoolean();

assert !Proposition.AND(F,X,X).getAsBoolean();

assert !Proposition.AND(F,F,X).getAsBoolean();

Proposition.OR(T,T);

Proposition.OR(T,T);

Proposition.OR(T,T,T);

Proposition.OR(T,T,T,T);

assert Proposition.OR(T,T).getAsBoolean();

supplier=OR(T,T);

condition=OR(T,T);

inner=OR(T,T);

assert Proposition.OR(T,T).getAsBoolean();

assert Proposition.OR(T,F).getAsBoolean();

assert Proposition.OR(F,T).getAsBoolean();

assert !Proposition.OR(F,F).getAsBoolean();

assert !Proposition.OR(F,F,F).getAsBoolean();

assert Proposition.OR(F,F,T).getAsBoolean();

assert Proposition.OR(F,T,F).getAsBoolean();

assert Proposition.OR(F,T,T).getAsBoolean();

assert Proposition.OR(T,F,F).getAsBoolean();

assert Proposition.OR(T,F,T).getAsBoolean();

assert Proposition.OR(T,T,F).getAsBoolean();

assert Proposition.OR(T,T,T).getAsBoolean();

assert Proposition.OR(F,T,X).getAsBoolean();

assert Proposition.OR(T,X,X).getAsBoolean();

assert T_OR_F_OR_X.getAsBoolean();

assert Proposition.OR(T,X,X).getAsBoolean();

assert Proposition.not(F).getAsBoolean();

assert !Proposition.not(T).getAsBoolean();

assert Proposition.not(F).and(not(F)).getAsBoolean();

assert !Proposition.not(F).and(not(T)).getAsBoolean();

assert Proposition.not(F).and(not(F)).or(T).getAsBoolean();

assert Proposition.not(F).and(not(F)).or(T).eval();

assert Proposition.not(F).and(not(F)).or(T).or(X).eval();

}@Test public void test113(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

Proposition.AND(T,T);

Proposition.AND(T,T,T);

Proposition.AND(T,T,T,T);

assert Proposition.AND(T,T).getAsBoolean();

supplier=AND(T,T);

condition=AND(T,T);

inner=AND(T,T);

assert Proposition.AND(T,T).getAsBoolean();

assert !Proposition.AND(T,F).getAsBoolean();

assert !Proposition.AND(F,T).getAsBoolean();

assert !Proposition.AND(F,F).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,T).getAsBoolean();

assert !Proposition.AND(F,T,T).getAsBoolean();

assert !Proposition.AND(F,F,T).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,X).getAsBoolean();

assert !Proposition.AND(F,X,X).getAsBoolean();

assert !Proposition.AND(F,F,X).getAsBoolean();

Proposition.OR(T,T);

Proposition.OR(T,T);

Proposition.OR(T,T,T);

Proposition.OR(T,T,T,T);

assert Proposition.OR(T,T).getAsBoolean();

supplier=OR(T,T);

condition=OR(T,T);

inner=OR(T,T);

assert Proposition.OR(T,T).getAsBoolean();

assert Proposition.OR(T,F).getAsBoolean();

assert Proposition.OR(F,T).getAsBoolean();

assert !Proposition.OR(F,F).getAsBoolean();

assert !Proposition.OR(F,F,F).getAsBoolean();

assert Proposition.OR(F,F,T).getAsBoolean();

assert Proposition.OR(F,T,F).getAsBoolean();

assert Proposition.OR(F,T,T).getAsBoolean();

assert Proposition.OR(T,F,F).getAsBoolean();

assert Proposition.OR(T,F,T).getAsBoolean();

assert Proposition.OR(T,T,F).getAsBoolean();

assert Proposition.OR(T,T,T).getAsBoolean();

assert Proposition.OR(F,T,X).getAsBoolean();

assert Proposition.OR(T,X,X).getAsBoolean();

assert T_OR_F_OR_X.getAsBoolean();

assert Proposition.OR(T,X,X).getAsBoolean();

assert Proposition.not(F).getAsBoolean();

assert !Proposition.not(T).getAsBoolean();

assert Proposition.not(F).and(not(F)).getAsBoolean();

assert !Proposition.not(F).and(not(T)).getAsBoolean();

assert Proposition.not(F).and(not(F)).or(T).getAsBoolean();

assert Proposition.not(F).and(not(F)).or(T).eval();

assert Proposition.not(F).and(not(F)).or(T).or(X).eval();

assert Proposition.not(F).and(not(F)).or(T).or(X,X).eval();

}@Test public void test114(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

Proposition.AND(T,T);

Proposition.AND(T,T,T);

Proposition.AND(T,T,T,T);

assert Proposition.AND(T,T).getAsBoolean();

supplier=AND(T,T);

condition=AND(T,T);

inner=AND(T,T);

assert Proposition.AND(T,T).getAsBoolean();

assert !Proposition.AND(T,F).getAsBoolean();

assert !Proposition.AND(F,T).getAsBoolean();

assert !Proposition.AND(F,F).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,T).getAsBoolean();

assert !Proposition.AND(F,T,T).getAsBoolean();

assert !Proposition.AND(F,F,T).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,X).getAsBoolean();

assert !Proposition.AND(F,X,X).getAsBoolean();

assert !Proposition.AND(F,F,X).getAsBoolean();

Proposition.OR(T,T);

Proposition.OR(T,T);

Proposition.OR(T,T,T);

Proposition.OR(T,T,T,T);

assert Proposition.OR(T,T).getAsBoolean();

supplier=OR(T,T);

condition=OR(T,T);

inner=OR(T,T);

assert Proposition.OR(T,T).getAsBoolean();

assert Proposition.OR(T,F).getAsBoolean();

assert Proposition.OR(F,T).getAsBoolean();

assert !Proposition.OR(F,F).getAsBoolean();

assert !Proposition.OR(F,F,F).getAsBoolean();

assert Proposition.OR(F,F,T).getAsBoolean();

assert Proposition.OR(F,T,F).getAsBoolean();

assert Proposition.OR(F,T,T).getAsBoolean();

assert Proposition.OR(T,F,F).getAsBoolean();

assert Proposition.OR(T,F,T).getAsBoolean();

assert Proposition.OR(T,T,F).getAsBoolean();

assert Proposition.OR(T,T,T).getAsBoolean();

assert Proposition.OR(F,T,X).getAsBoolean();

assert Proposition.OR(T,X,X).getAsBoolean();

assert T_OR_F_OR_X.getAsBoolean();

assert Proposition.OR(T,X,X).getAsBoolean();

assert Proposition.not(F).getAsBoolean();

assert !Proposition.not(T).getAsBoolean();

assert Proposition.not(F).and(not(F)).getAsBoolean();

assert !Proposition.not(F).and(not(T)).getAsBoolean();

assert Proposition.not(F).and(not(F)).or(T).getAsBoolean();

assert Proposition.not(F).and(not(F)).or(T).eval();

assert Proposition.not(F).and(not(F)).or(T).or(X).eval();

assert Proposition.not(F).and(not(F)).or(T).or(X,X).eval();

assert not(F).and(not(F)).getAsBoolean();

}@Test public void test115(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

Proposition.AND(T,T);

Proposition.AND(T,T,T);

Proposition.AND(T,T,T,T);

assert Proposition.AND(T,T).getAsBoolean();

supplier=AND(T,T);

condition=AND(T,T);

inner=AND(T,T);

assert Proposition.AND(T,T).getAsBoolean();

assert !Proposition.AND(T,F).getAsBoolean();

assert !Proposition.AND(F,T).getAsBoolean();

assert !Proposition.AND(F,F).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,T).getAsBoolean();

assert !Proposition.AND(F,T,T).getAsBoolean();

assert !Proposition.AND(F,F,T).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,X).getAsBoolean();

assert !Proposition.AND(F,X,X).getAsBoolean();

assert !Proposition.AND(F,F,X).getAsBoolean();

Proposition.OR(T,T);

Proposition.OR(T,T);

Proposition.OR(T,T,T);

Proposition.OR(T,T,T,T);

assert Proposition.OR(T,T).getAsBoolean();

supplier=OR(T,T);

condition=OR(T,T);

inner=OR(T,T);

assert Proposition.OR(T,T).getAsBoolean();

assert Proposition.OR(T,F).getAsBoolean();

assert Proposition.OR(F,T).getAsBoolean();

assert !Proposition.OR(F,F).getAsBoolean();

assert !Proposition.OR(F,F,F).getAsBoolean();

assert Proposition.OR(F,F,T).getAsBoolean();

assert Proposition.OR(F,T,F).getAsBoolean();

assert Proposition.OR(F,T,T).getAsBoolean();

assert Proposition.OR(T,F,F).getAsBoolean();

assert Proposition.OR(T,F,T).getAsBoolean();

assert Proposition.OR(T,T,F).getAsBoolean();

assert Proposition.OR(T,T,T).getAsBoolean();

assert Proposition.OR(F,T,X).getAsBoolean();

assert Proposition.OR(T,X,X).getAsBoolean();

assert T_OR_F_OR_X.getAsBoolean();

assert Proposition.OR(T,X,X).getAsBoolean();

assert Proposition.not(F).getAsBoolean();

assert !Proposition.not(T).getAsBoolean();

assert Proposition.not(F).and(not(F)).getAsBoolean();

assert !Proposition.not(F).and(not(T)).getAsBoolean();

assert Proposition.not(F).and(not(F)).or(T).getAsBoolean();

assert Proposition.not(F).and(not(F)).or(T).eval();

assert Proposition.not(F).and(not(F)).or(T).or(X).eval();

assert Proposition.not(F).and(not(F)).or(T).or(X,X).eval();

assert not(F).and(not(F)).getAsBoolean();

assert !not(F).and(not(T)).getAsBoolean();

}@Test public void test116(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

Proposition.AND(T,T);

Proposition.AND(T,T,T);

Proposition.AND(T,T,T,T);

assert Proposition.AND(T,T).getAsBoolean();

supplier=AND(T,T);

condition=AND(T,T);

inner=AND(T,T);

assert Proposition.AND(T,T).getAsBoolean();

assert !Proposition.AND(T,F).getAsBoolean();

assert !Proposition.AND(F,T).getAsBoolean();

assert !Proposition.AND(F,F).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,T).getAsBoolean();

assert !Proposition.AND(F,T,T).getAsBoolean();

assert !Proposition.AND(F,F,T).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,X).getAsBoolean();

assert !Proposition.AND(F,X,X).getAsBoolean();

assert !Proposition.AND(F,F,X).getAsBoolean();

Proposition.OR(T,T);

Proposition.OR(T,T);

Proposition.OR(T,T,T);

Proposition.OR(T,T,T,T);

assert Proposition.OR(T,T).getAsBoolean();

supplier=OR(T,T);

condition=OR(T,T);

inner=OR(T,T);

assert Proposition.OR(T,T).getAsBoolean();

assert Proposition.OR(T,F).getAsBoolean();

assert Proposition.OR(F,T).getAsBoolean();

assert !Proposition.OR(F,F).getAsBoolean();

assert !Proposition.OR(F,F,F).getAsBoolean();

assert Proposition.OR(F,F,T).getAsBoolean();

assert Proposition.OR(F,T,F).getAsBoolean();

assert Proposition.OR(F,T,T).getAsBoolean();

assert Proposition.OR(T,F,F).getAsBoolean();

assert Proposition.OR(T,F,T).getAsBoolean();

assert Proposition.OR(T,T,F).getAsBoolean();

assert Proposition.OR(T,T,T).getAsBoolean();

assert Proposition.OR(F,T,X).getAsBoolean();

assert Proposition.OR(T,X,X).getAsBoolean();

assert T_OR_F_OR_X.getAsBoolean();

assert Proposition.OR(T,X,X).getAsBoolean();

assert Proposition.not(F).getAsBoolean();

assert !Proposition.not(T).getAsBoolean();

assert Proposition.not(F).and(not(F)).getAsBoolean();

assert !Proposition.not(F).and(not(T)).getAsBoolean();

assert Proposition.not(F).and(not(F)).or(T).getAsBoolean();

assert Proposition.not(F).and(not(F)).or(T).eval();

assert Proposition.not(F).and(not(F)).or(T).or(X).eval();

assert Proposition.not(F).and(not(F)).or(T).or(X,X).eval();

assert not(F).and(not(F)).getAsBoolean();

assert !not(F).and(not(T)).getAsBoolean();

assert not(F).and(not(F)).or(T).getAsBoolean();

}@Test public void test117(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

Proposition.AND(T,T);

Proposition.AND(T,T,T);

Proposition.AND(T,T,T,T);

assert Proposition.AND(T,T).getAsBoolean();

supplier=AND(T,T);

condition=AND(T,T);

inner=AND(T,T);

assert Proposition.AND(T,T).getAsBoolean();

assert !Proposition.AND(T,F).getAsBoolean();

assert !Proposition.AND(F,T).getAsBoolean();

assert !Proposition.AND(F,F).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,T).getAsBoolean();

assert !Proposition.AND(F,T,T).getAsBoolean();

assert !Proposition.AND(F,F,T).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,X).getAsBoolean();

assert !Proposition.AND(F,X,X).getAsBoolean();

assert !Proposition.AND(F,F,X).getAsBoolean();

Proposition.OR(T,T);

Proposition.OR(T,T);

Proposition.OR(T,T,T);

Proposition.OR(T,T,T,T);

assert Proposition.OR(T,T).getAsBoolean();

supplier=OR(T,T);

condition=OR(T,T);

inner=OR(T,T);

assert Proposition.OR(T,T).getAsBoolean();

assert Proposition.OR(T,F).getAsBoolean();

assert Proposition.OR(F,T).getAsBoolean();

assert !Proposition.OR(F,F).getAsBoolean();

assert !Proposition.OR(F,F,F).getAsBoolean();

assert Proposition.OR(F,F,T).getAsBoolean();

assert Proposition.OR(F,T,F).getAsBoolean();

assert Proposition.OR(F,T,T).getAsBoolean();

assert Proposition.OR(T,F,F).getAsBoolean();

assert Proposition.OR(T,F,T).getAsBoolean();

assert Proposition.OR(T,T,F).getAsBoolean();

assert Proposition.OR(T,T,T).getAsBoolean();

assert Proposition.OR(F,T,X).getAsBoolean();

assert Proposition.OR(T,X,X).getAsBoolean();

assert T_OR_F_OR_X.getAsBoolean();

assert Proposition.OR(T,X,X).getAsBoolean();

assert Proposition.not(F).getAsBoolean();

assert !Proposition.not(T).getAsBoolean();

assert Proposition.not(F).and(not(F)).getAsBoolean();

assert !Proposition.not(F).and(not(T)).getAsBoolean();

assert Proposition.not(F).and(not(F)).or(T).getAsBoolean();

assert Proposition.not(F).and(not(F)).or(T).eval();

assert Proposition.not(F).and(not(F)).or(T).or(X).eval();

assert Proposition.not(F).and(not(F)).or(T).or(X,X).eval();

assert not(F).and(not(F)).getAsBoolean();

assert !not(F).and(not(T)).getAsBoolean();

assert not(F).and(not(F)).or(T).getAsBoolean();

assert not(F).and(not(F)).or(T).eval();

}@Test public void test118(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

Proposition.AND(T,T);

Proposition.AND(T,T,T);

Proposition.AND(T,T,T,T);

assert Proposition.AND(T,T).getAsBoolean();

supplier=AND(T,T);

condition=AND(T,T);

inner=AND(T,T);

assert Proposition.AND(T,T).getAsBoolean();

assert !Proposition.AND(T,F).getAsBoolean();

assert !Proposition.AND(F,T).getAsBoolean();

assert !Proposition.AND(F,F).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,T).getAsBoolean();

assert !Proposition.AND(F,T,T).getAsBoolean();

assert !Proposition.AND(F,F,T).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,X).getAsBoolean();

assert !Proposition.AND(F,X,X).getAsBoolean();

assert !Proposition.AND(F,F,X).getAsBoolean();

Proposition.OR(T,T);

Proposition.OR(T,T);

Proposition.OR(T,T,T);

Proposition.OR(T,T,T,T);

assert Proposition.OR(T,T).getAsBoolean();

supplier=OR(T,T);

condition=OR(T,T);

inner=OR(T,T);

assert Proposition.OR(T,T).getAsBoolean();

assert Proposition.OR(T,F).getAsBoolean();

assert Proposition.OR(F,T).getAsBoolean();

assert !Proposition.OR(F,F).getAsBoolean();

assert !Proposition.OR(F,F,F).getAsBoolean();

assert Proposition.OR(F,F,T).getAsBoolean();

assert Proposition.OR(F,T,F).getAsBoolean();

assert Proposition.OR(F,T,T).getAsBoolean();

assert Proposition.OR(T,F,F).getAsBoolean();

assert Proposition.OR(T,F,T).getAsBoolean();

assert Proposition.OR(T,T,F).getAsBoolean();

assert Proposition.OR(T,T,T).getAsBoolean();

assert Proposition.OR(F,T,X).getAsBoolean();

assert Proposition.OR(T,X,X).getAsBoolean();

assert T_OR_F_OR_X.getAsBoolean();

assert Proposition.OR(T,X,X).getAsBoolean();

assert Proposition.not(F).getAsBoolean();

assert !Proposition.not(T).getAsBoolean();

assert Proposition.not(F).and(not(F)).getAsBoolean();

assert !Proposition.not(F).and(not(T)).getAsBoolean();

assert Proposition.not(F).and(not(F)).or(T).getAsBoolean();

assert Proposition.not(F).and(not(F)).or(T).eval();

assert Proposition.not(F).and(not(F)).or(T).or(X).eval();

assert Proposition.not(F).and(not(F)).or(T).or(X,X).eval();

assert not(F).and(not(F)).getAsBoolean();

assert !not(F).and(not(T)).getAsBoolean();

assert not(F).and(not(F)).or(T).getAsBoolean();

assert not(F).and(not(F)).or(T).eval();

assert not(F).and(not(F)).or(T).or(X).eval();

}@Test public void test119(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

Proposition.AND(T,T);

Proposition.AND(T,T,T);

Proposition.AND(T,T,T,T);

assert Proposition.AND(T,T).getAsBoolean();

supplier=AND(T,T);

condition=AND(T,T);

inner=AND(T,T);

assert Proposition.AND(T,T).getAsBoolean();

assert !Proposition.AND(T,F).getAsBoolean();

assert !Proposition.AND(F,T).getAsBoolean();

assert !Proposition.AND(F,F).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,T).getAsBoolean();

assert !Proposition.AND(F,T,T).getAsBoolean();

assert !Proposition.AND(F,F,T).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,X).getAsBoolean();

assert !Proposition.AND(F,X,X).getAsBoolean();

assert !Proposition.AND(F,F,X).getAsBoolean();

Proposition.OR(T,T);

Proposition.OR(T,T);

Proposition.OR(T,T,T);

Proposition.OR(T,T,T,T);

assert Proposition.OR(T,T).getAsBoolean();

supplier=OR(T,T);

condition=OR(T,T);

inner=OR(T,T);

assert Proposition.OR(T,T).getAsBoolean();

assert Proposition.OR(T,F).getAsBoolean();

assert Proposition.OR(F,T).getAsBoolean();

assert !Proposition.OR(F,F).getAsBoolean();

assert !Proposition.OR(F,F,F).getAsBoolean();

assert Proposition.OR(F,F,T).getAsBoolean();

assert Proposition.OR(F,T,F).getAsBoolean();

assert Proposition.OR(F,T,T).getAsBoolean();

assert Proposition.OR(T,F,F).getAsBoolean();

assert Proposition.OR(T,F,T).getAsBoolean();

assert Proposition.OR(T,T,F).getAsBoolean();

assert Proposition.OR(T,T,T).getAsBoolean();

assert Proposition.OR(F,T,X).getAsBoolean();

assert Proposition.OR(T,X,X).getAsBoolean();

assert T_OR_F_OR_X.getAsBoolean();

assert Proposition.OR(T,X,X).getAsBoolean();

assert Proposition.not(F).getAsBoolean();

assert !Proposition.not(T).getAsBoolean();

assert Proposition.not(F).and(not(F)).getAsBoolean();

assert !Proposition.not(F).and(not(T)).getAsBoolean();

assert Proposition.not(F).and(not(F)).or(T).getAsBoolean();

assert Proposition.not(F).and(not(F)).or(T).eval();

assert Proposition.not(F).and(not(F)).or(T).or(X).eval();

assert Proposition.not(F).and(not(F)).or(T).or(X,X).eval();

assert not(F).and(not(F)).getAsBoolean();

assert !not(F).and(not(T)).getAsBoolean();

assert not(F).and(not(F)).or(T).getAsBoolean();

assert not(F).and(not(F)).or(T).eval();

assert not(F).and(not(F)).or(T).or(X).eval();

final Proposition or=not(F).and(not(F)).or(T).or(X,X);

}@Test public void test120(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

Proposition.AND(T,T);

Proposition.AND(T,T,T);

Proposition.AND(T,T,T,T);

assert Proposition.AND(T,T).getAsBoolean();

supplier=AND(T,T);

condition=AND(T,T);

inner=AND(T,T);

assert Proposition.AND(T,T).getAsBoolean();

assert !Proposition.AND(T,F).getAsBoolean();

assert !Proposition.AND(F,T).getAsBoolean();

assert !Proposition.AND(F,F).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,T).getAsBoolean();

assert !Proposition.AND(F,T,T).getAsBoolean();

assert !Proposition.AND(F,F,T).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,X).getAsBoolean();

assert !Proposition.AND(F,X,X).getAsBoolean();

assert !Proposition.AND(F,F,X).getAsBoolean();

Proposition.OR(T,T);

Proposition.OR(T,T);

Proposition.OR(T,T,T);

Proposition.OR(T,T,T,T);

assert Proposition.OR(T,T).getAsBoolean();

supplier=OR(T,T);

condition=OR(T,T);

inner=OR(T,T);

assert Proposition.OR(T,T).getAsBoolean();

assert Proposition.OR(T,F).getAsBoolean();

assert Proposition.OR(F,T).getAsBoolean();

assert !Proposition.OR(F,F).getAsBoolean();

assert !Proposition.OR(F,F,F).getAsBoolean();

assert Proposition.OR(F,F,T).getAsBoolean();

assert Proposition.OR(F,T,F).getAsBoolean();

assert Proposition.OR(F,T,T).getAsBoolean();

assert Proposition.OR(T,F,F).getAsBoolean();

assert Proposition.OR(T,F,T).getAsBoolean();

assert Proposition.OR(T,T,F).getAsBoolean();

assert Proposition.OR(T,T,T).getAsBoolean();

assert Proposition.OR(F,T,X).getAsBoolean();

assert Proposition.OR(T,X,X).getAsBoolean();

assert T_OR_F_OR_X.getAsBoolean();

assert Proposition.OR(T,X,X).getAsBoolean();

assert Proposition.not(F).getAsBoolean();

assert !Proposition.not(T).getAsBoolean();

assert Proposition.not(F).and(not(F)).getAsBoolean();

assert !Proposition.not(F).and(not(T)).getAsBoolean();

assert Proposition.not(F).and(not(F)).or(T).getAsBoolean();

assert Proposition.not(F).and(not(F)).or(T).eval();

assert Proposition.not(F).and(not(F)).or(T).or(X).eval();

assert Proposition.not(F).and(not(F)).or(T).or(X,X).eval();

assert not(F).and(not(F)).getAsBoolean();

assert !not(F).and(not(T)).getAsBoolean();

assert not(F).and(not(F)).or(T).getAsBoolean();

assert not(F).and(not(F)).or(T).eval();

assert not(F).and(not(F)).or(T).or(X).eval();

final Proposition or=not(F).and(not(F)).or(T).or(X,X);

assert or.eval();

}@Test public void test121(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

Proposition.AND(T,T);

Proposition.AND(T,T,T);

Proposition.AND(T,T,T,T);

assert Proposition.AND(T,T).getAsBoolean();

supplier=AND(T,T);

condition=AND(T,T);

inner=AND(T,T);

assert Proposition.AND(T,T).getAsBoolean();

assert !Proposition.AND(T,F).getAsBoolean();

assert !Proposition.AND(F,T).getAsBoolean();

assert !Proposition.AND(F,F).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,T).getAsBoolean();

assert !Proposition.AND(F,T,T).getAsBoolean();

assert !Proposition.AND(F,F,T).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,X).getAsBoolean();

assert !Proposition.AND(F,X,X).getAsBoolean();

assert !Proposition.AND(F,F,X).getAsBoolean();

Proposition.OR(T,T);

Proposition.OR(T,T);

Proposition.OR(T,T,T);

Proposition.OR(T,T,T,T);

assert Proposition.OR(T,T).getAsBoolean();

supplier=OR(T,T);

condition=OR(T,T);

inner=OR(T,T);

assert Proposition.OR(T,T).getAsBoolean();

assert Proposition.OR(T,F).getAsBoolean();

assert Proposition.OR(F,T).getAsBoolean();

assert !Proposition.OR(F,F).getAsBoolean();

assert !Proposition.OR(F,F,F).getAsBoolean();

assert Proposition.OR(F,F,T).getAsBoolean();

assert Proposition.OR(F,T,F).getAsBoolean();

assert Proposition.OR(F,T,T).getAsBoolean();

assert Proposition.OR(T,F,F).getAsBoolean();

assert Proposition.OR(T,F,T).getAsBoolean();

assert Proposition.OR(T,T,F).getAsBoolean();

assert Proposition.OR(T,T,T).getAsBoolean();

assert Proposition.OR(F,T,X).getAsBoolean();

assert Proposition.OR(T,X,X).getAsBoolean();

assert T_OR_F_OR_X.getAsBoolean();

assert Proposition.OR(T,X,X).getAsBoolean();

assert Proposition.not(F).getAsBoolean();

assert !Proposition.not(T).getAsBoolean();

assert Proposition.not(F).and(not(F)).getAsBoolean();

assert !Proposition.not(F).and(not(T)).getAsBoolean();

assert Proposition.not(F).and(not(F)).or(T).getAsBoolean();

assert Proposition.not(F).and(not(F)).or(T).eval();

assert Proposition.not(F).and(not(F)).or(T).or(X).eval();

assert Proposition.not(F).and(not(F)).or(T).or(X,X).eval();

assert not(F).and(not(F)).getAsBoolean();

assert !not(F).and(not(T)).getAsBoolean();

assert not(F).and(not(F)).or(T).getAsBoolean();

assert not(F).and(not(F)).or(T).eval();

assert not(F).and(not(F)).or(T).or(X).eval();

final Proposition or=not(F).and(not(F)).or(T).or(X,X);

assert or.eval();

assert Proposition.that(F).or(T).and(T).eval();

}@Test public void test122(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

Proposition.AND(T,T);

Proposition.AND(T,T,T);

Proposition.AND(T,T,T,T);

assert Proposition.AND(T,T).getAsBoolean();

supplier=AND(T,T);

condition=AND(T,T);

inner=AND(T,T);

assert Proposition.AND(T,T).getAsBoolean();

assert !Proposition.AND(T,F).getAsBoolean();

assert !Proposition.AND(F,T).getAsBoolean();

assert !Proposition.AND(F,F).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,T).getAsBoolean();

assert !Proposition.AND(F,T,T).getAsBoolean();

assert !Proposition.AND(F,F,T).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,X).getAsBoolean();

assert !Proposition.AND(F,X,X).getAsBoolean();

assert !Proposition.AND(F,F,X).getAsBoolean();

Proposition.OR(T,T);

Proposition.OR(T,T);

Proposition.OR(T,T,T);

Proposition.OR(T,T,T,T);

assert Proposition.OR(T,T).getAsBoolean();

supplier=OR(T,T);

condition=OR(T,T);

inner=OR(T,T);

assert Proposition.OR(T,T).getAsBoolean();

assert Proposition.OR(T,F).getAsBoolean();

assert Proposition.OR(F,T).getAsBoolean();

assert !Proposition.OR(F,F).getAsBoolean();

assert !Proposition.OR(F,F,F).getAsBoolean();

assert Proposition.OR(F,F,T).getAsBoolean();

assert Proposition.OR(F,T,F).getAsBoolean();

assert Proposition.OR(F,T,T).getAsBoolean();

assert Proposition.OR(T,F,F).getAsBoolean();

assert Proposition.OR(T,F,T).getAsBoolean();

assert Proposition.OR(T,T,F).getAsBoolean();

assert Proposition.OR(T,T,T).getAsBoolean();

assert Proposition.OR(F,T,X).getAsBoolean();

assert Proposition.OR(T,X,X).getAsBoolean();

assert T_OR_F_OR_X.getAsBoolean();

assert Proposition.OR(T,X,X).getAsBoolean();

assert Proposition.not(F).getAsBoolean();

assert !Proposition.not(T).getAsBoolean();

assert Proposition.not(F).and(not(F)).getAsBoolean();

assert !Proposition.not(F).and(not(T)).getAsBoolean();

assert Proposition.not(F).and(not(F)).or(T).getAsBoolean();

assert Proposition.not(F).and(not(F)).or(T).eval();

assert Proposition.not(F).and(not(F)).or(T).or(X).eval();

assert Proposition.not(F).and(not(F)).or(T).or(X,X).eval();

assert not(F).and(not(F)).getAsBoolean();

assert !not(F).and(not(T)).getAsBoolean();

assert not(F).and(not(F)).or(T).getAsBoolean();

assert not(F).and(not(F)).or(T).eval();

assert not(F).and(not(F)).or(T).or(X).eval();

final Proposition or=not(F).and(not(F)).or(T).or(X,X);

assert or.eval();

assert Proposition.that(F).or(T).and(T).eval();

assert OR(F,T).and(T).eval();

}@Test public void test123(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

Proposition.AND(T,T);

Proposition.AND(T,T,T);

Proposition.AND(T,T,T,T);

assert Proposition.AND(T,T).getAsBoolean();

supplier=AND(T,T);

condition=AND(T,T);

inner=AND(T,T);

assert Proposition.AND(T,T).getAsBoolean();

assert !Proposition.AND(T,F).getAsBoolean();

assert !Proposition.AND(F,T).getAsBoolean();

assert !Proposition.AND(F,F).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,T).getAsBoolean();

assert !Proposition.AND(F,T,T).getAsBoolean();

assert !Proposition.AND(F,F,T).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,X).getAsBoolean();

assert !Proposition.AND(F,X,X).getAsBoolean();

assert !Proposition.AND(F,F,X).getAsBoolean();

Proposition.OR(T,T);

Proposition.OR(T,T);

Proposition.OR(T,T,T);

Proposition.OR(T,T,T,T);

assert Proposition.OR(T,T).getAsBoolean();

supplier=OR(T,T);

condition=OR(T,T);

inner=OR(T,T);

assert Proposition.OR(T,T).getAsBoolean();

assert Proposition.OR(T,F).getAsBoolean();

assert Proposition.OR(F,T).getAsBoolean();

assert !Proposition.OR(F,F).getAsBoolean();

assert !Proposition.OR(F,F,F).getAsBoolean();

assert Proposition.OR(F,F,T).getAsBoolean();

assert Proposition.OR(F,T,F).getAsBoolean();

assert Proposition.OR(F,T,T).getAsBoolean();

assert Proposition.OR(T,F,F).getAsBoolean();

assert Proposition.OR(T,F,T).getAsBoolean();

assert Proposition.OR(T,T,F).getAsBoolean();

assert Proposition.OR(T,T,T).getAsBoolean();

assert Proposition.OR(F,T,X).getAsBoolean();

assert Proposition.OR(T,X,X).getAsBoolean();

assert T_OR_F_OR_X.getAsBoolean();

assert Proposition.OR(T,X,X).getAsBoolean();

assert Proposition.not(F).getAsBoolean();

assert !Proposition.not(T).getAsBoolean();

assert Proposition.not(F).and(not(F)).getAsBoolean();

assert !Proposition.not(F).and(not(T)).getAsBoolean();

assert Proposition.not(F).and(not(F)).or(T).getAsBoolean();

assert Proposition.not(F).and(not(F)).or(T).eval();

assert Proposition.not(F).and(not(F)).or(T).or(X).eval();

assert Proposition.not(F).and(not(F)).or(T).or(X,X).eval();

assert not(F).and(not(F)).getAsBoolean();

assert !not(F).and(not(T)).getAsBoolean();

assert not(F).and(not(F)).or(T).getAsBoolean();

assert not(F).and(not(F)).or(T).eval();

assert not(F).and(not(F)).or(T).or(X).eval();

final Proposition or=not(F).and(not(F)).or(T).or(X,X);

assert or.eval();

assert Proposition.that(F).or(T).and(T).eval();

assert OR(F,T).and(T).eval();

assert OR(F,T).and(T).or(X).eval();

}@Test public void test124(){
object=Proposition.T;

assert object != null;

object=Proposition.F;

assert object != null;

object=Proposition.N;

assert object != null;

object=Proposition.X;

assert Proposition.T != null;

assert Proposition.T != null;

assert Proposition.F != null;

assert Proposition.T != null;

assert Proposition.F != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

supplier=Proposition.T;

assert supplier != null;

supplier=Proposition.F;

assert supplier != null;

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert ignoreNext() || Proposition.T.getAsBoolean();

assert ignoreNext() || Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

assert Proposition.T.getAsBoolean();

assert !Proposition.F.getAsBoolean();

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

Proposition.that(Proposition.T);

Proposition.that(Proposition.F);

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

(Proposition.that(Proposition.T) + "").hashCode();

(Proposition.that(Proposition.F) + "").hashCode();

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

object=Proposition.that(Proposition.T);

object=Proposition.that(Proposition.F);

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

assert Proposition.that(Proposition.T) != null;

assert Proposition.that(Proposition.F) != null;

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

condition=Proposition.that(Proposition.T);

condition=Proposition.that(Proposition.F);

assert Proposition.that(() -> true).getAsBoolean();

assert Proposition.that(() -> condition != null).getAsBoolean();

assert !Proposition.that(() -> false).getAsBoolean();

assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

assert Proposition.that(Proposition.T).getAsBoolean();

assert !Proposition.that(Proposition.F).getAsBoolean();

Proposition.AND(T,T);

Proposition.AND(T,T);

Proposition.AND(T,T,T);

Proposition.AND(T,T,T,T);

assert Proposition.AND(T,T).getAsBoolean();

supplier=AND(T,T);

condition=AND(T,T);

inner=AND(T,T);

assert Proposition.AND(T,T).getAsBoolean();

assert !Proposition.AND(T,F).getAsBoolean();

assert !Proposition.AND(F,T).getAsBoolean();

assert !Proposition.AND(F,F).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,T).getAsBoolean();

assert !Proposition.AND(F,T,T).getAsBoolean();

assert !Proposition.AND(F,F,T).getAsBoolean();

assert Proposition.AND(T,T,T).getAsBoolean();

assert !Proposition.AND(T,F,X).getAsBoolean();

assert !Proposition.AND(F,X,X).getAsBoolean();

assert !Proposition.AND(F,F,X).getAsBoolean();

Proposition.OR(T,T);

Proposition.OR(T,T);

Proposition.OR(T,T,T);

Proposition.OR(T,T,T,T);

assert Proposition.OR(T,T).getAsBoolean();

supplier=OR(T,T);

condition=OR(T,T);

inner=OR(T,T);

assert Proposition.OR(T,T).getAsBoolean();

assert Proposition.OR(T,F).getAsBoolean();

assert Proposition.OR(F,T).getAsBoolean();

assert !Proposition.OR(F,F).getAsBoolean();

assert !Proposition.OR(F,F,F).getAsBoolean();

assert Proposition.OR(F,F,T).getAsBoolean();

assert Proposition.OR(F,T,F).getAsBoolean();

assert Proposition.OR(F,T,T).getAsBoolean();

assert Proposition.OR(T,F,F).getAsBoolean();

assert Proposition.OR(T,F,T).getAsBoolean();

assert Proposition.OR(T,T,F).getAsBoolean();

assert Proposition.OR(T,T,T).getAsBoolean();

assert Proposition.OR(F,T,X).getAsBoolean();

assert Proposition.OR(T,X,X).getAsBoolean();

assert T_OR_F_OR_X.getAsBoolean();

assert Proposition.OR(T,X,X).getAsBoolean();

assert Proposition.not(F).getAsBoolean();

assert !Proposition.not(T).getAsBoolean();

assert Proposition.not(F).and(not(F)).getAsBoolean();

assert !Proposition.not(F).and(not(T)).getAsBoolean();

assert Proposition.not(F).and(not(F)).or(T).getAsBoolean();

assert Proposition.not(F).and(not(F)).or(T).eval();

assert Proposition.not(F).and(not(F)).or(T).or(X).eval();

assert Proposition.not(F).and(not(F)).or(T).or(X,X).eval();

assert not(F).and(not(F)).getAsBoolean();

assert !not(F).and(not(T)).getAsBoolean();

assert not(F).and(not(F)).or(T).getAsBoolean();

assert not(F).and(not(F)).or(T).eval();

assert not(F).and(not(F)).or(T).or(X).eval();

final Proposition or=not(F).and(not(F)).or(T).or(X,X);

assert or.eval();

assert Proposition.that(F).or(T).and(T).eval();

assert OR(F,T).and(T).eval();

assert OR(F,T).and(T).or(X).eval();

assert !OR(F,T).and(T).and(F).eval();

}@Test public void test125(){
azzert.that(T_OR_F_OR_X + "",is("T OR F OR X"));

}@Test public void test126(){
azzert.that(T_OR_F_OR_X + "",is("T OR F OR X"));

azzert.that(F.or(T).and(T).reduce(javaReducer),is("((F || T) && T)"));

}@Test public void test127(){
azzert.that(T_OR_F_OR_X + "",is("T OR F OR X"));

azzert.that(F.or(T).and(T).reduce(javaReducer),is("((F || T) && T)"));

azzert.that(F.and(X).or(T).reduce(javaReducer),is("((F && X) || T)"));

}@Test public void test128(){
azzert.that(T_OR_F_OR_X + "",is("T OR F OR X"));

azzert.that(F.or(T).and(T).reduce(javaReducer),is("((F || T) && T)"));

azzert.that(F.and(X).or(T).reduce(javaReducer),is("((F && X) || T)"));

azzert.that(F.and("X1",X).or(T).reduce(javaReducer),is("((F && X1) || T)"));

}@Test public void test129(){
azzert.that(T_OR_F_OR_X + "",is("T OR F OR X"));

azzert.that(F.or(T).and(T).reduce(javaReducer),is("((F || T) && T)"));

azzert.that(F.and(X).or(T).reduce(javaReducer),is("((F && X) || T)"));

azzert.that(F.and("X1",X).or(T).reduce(javaReducer),is("((F && X1) || T)"));

azzert.that(F.or("X1",X).or(T).reduce(javaReducer),is("(F || X1 || T)"));

}@Test public void test130(){
azzert.that(OR(F,X,N).and(T).reduce(javaReducer),is("((F || X || N) && T)"));

}@Test public void test131(){
assert !hasCycles(T);

}@Test public void test132(){
assert !hasCycles(T);

assert !hasCycles(X);

}@Test public void test133(){
assert !hasCycles(T);

assert !hasCycles(X);

assert !hasCycles(F);

}@Test public void test134(){
assert !hasCycles(T);

assert !hasCycles(X);

assert !hasCycles(F);

assert !hasCycles(N);

}@Test public void test135(){
assert !hasCycles(T);

assert !hasCycles(X);

assert !hasCycles(F);

assert !hasCycles(N);

assert !hasCycles(T.and(F));

}@Test public void test136(){
assert !hasCycles(T);

assert !hasCycles(X);

assert !hasCycles(F);

assert !hasCycles(N);

assert !hasCycles(T.and(F));

assert !hasCycles(F.and(T));

}@Test public void test137(){
assert !hasCycles(T);

assert !hasCycles(X);

assert !hasCycles(F);

assert !hasCycles(N);

assert !hasCycles(T.and(F));

assert !hasCycles(F.and(T));

assert !hasCycles(T.and(T));

}@Test public void test138(){
assert !hasCycles(T);

assert !hasCycles(X);

assert !hasCycles(F);

assert !hasCycles(N);

assert !hasCycles(T.and(F));

assert !hasCycles(F.and(T));

assert !hasCycles(T.and(T));

assert !hasCycles(T.or(T));

}@Test public void test139(){
assert !hasCycles(T);

assert !hasCycles(X);

assert !hasCycles(F);

assert !hasCycles(N);

assert !hasCycles(T.and(F));

assert !hasCycles(F.and(T));

assert !hasCycles(T.and(T));

assert !hasCycles(T.or(T));

assert !hasCycles(T_AND_F_AND_X);

}@Test public void test140(){
assert !hasCycles(T);

assert !hasCycles(X);

assert !hasCycles(F);

assert !hasCycles(N);

assert !hasCycles(T.and(F));

assert !hasCycles(F.and(T));

assert !hasCycles(T.and(T));

assert !hasCycles(T.or(T));

assert !hasCycles(T_AND_F_AND_X);

assert !hasCycles(T_OR_F_OR_X);

}@Test public void test141(){
assert !hasCycles(T.or(T));

}@Test public void test142(){
assert !hasCycles(T.and(T));

}@Test public void test143(){
T.reduce(javaReducer);

}@Test public void test144(){
F.and(F).reduce(javaReducer);

}@Test public void test145(){
T.or(X).reduce(javaReducer);

}@Test public void test146(){
azzert.that(X.reduce(javaReducer),instanceOf(String.class));

}@Test public void test147(){
azzert.that(X.reduce(javaReducer),instanceOf(String.class));

azzert.that(X.reduce(javaReducer),is("X"));

}@Test public void test148(){
azzert.that(X.reduce(javaReducer),instanceOf(String.class));

azzert.that(X.reduce(javaReducer),is("X"));

azzert.that(T.reduce(javaReducer),is("T"));

}@Test public void test149(){
azzert.that(X.reduce(javaReducer),instanceOf(String.class));

azzert.that(X.reduce(javaReducer),is("X"));

azzert.that(T.reduce(javaReducer),is("T"));

azzert.that(T.or(X).reduce(javaReducer),is("(T || X)"));

}@Test public void test150(){
azzert.that(T.and(X).reduce(javaReducer),is("(T && X)"));

}@Test public void test151(){
azzert.that(not(T).reduce(javaReducer),is("!T"));

}@Test public void test152(){
azzert.that(not(X).reduce(javaReducer),is("!X"));

}@Test public void test153(){
azzert.that(not(N).reduce(javaReducer),is("!N"));

}@Test public void test154(){
azzert.that(that(T).reduce(javaReducer),is("T"));

}@Test public void test155(){
azzert.that(T_OR_F_OR_X.reduce(javaReducer),is("(T || F || X)"));

}@Test public void test156(){
T.or(X,X).reduce(javaReducer);

}@Test public void test157(){
T.or(X,X).reduce(javaReducer);

azzert.that(T.or(X,X).reduce(javaReducer),is("(T || X || X)"));

}@Test public void test158(){
azzert.that(T_OR_F_OR_X.reduce(new PropositionReducer<String>(new ReduceStringConcatenate()){
  @Override protected String map(  final BooleanSupplier ¢){
    return ¢ + "";
  }
}
),is("TFX"));

}@Test public void test159(){
azzert.that(T_AND_F_AND_X.reduce(javaReducer),is("(T && F && X)"));

}@Test public void test160(){
azzert.that(NOT_F_AND_NOT_F_OR_X_OR_N_OR_T.reduce(javaReducer),is("((!F && !F) || X || N || T)"));

}@Test public void test161(){
azzert.that(T + "",is("T"));

}@Test public void test162(){
azzert.that(T + "",is("T"));

azzert.that(F + "",is("F"));

}@Test public void test163(){
azzert.that(T + "",is("T"));

azzert.that(F + "",is("F"));

azzert.that(N + "",is("N"));

}@Test public void test164(){
azzert.that(T + "",is("T"));

azzert.that(F + "",is("F"));

azzert.that(N + "",is("N"));

azzert.that(X + "",is("X"));

}@Test public void test165(){
azzert.that(T_OR_F_OR_X_OR_NOT_F_AND_NOT_F_OR_X_OR_N_OR_T.reduce(javaReducer),is("(T || F || X || (!F && !F) || X || N || T)"));

}@Test public void test166(){
azzert.that(Proposition.not(F).and(X).reduce(javaReducer),is("(!F && X)"));

}@Test public void test167(){
azzert.that(Proposition.not(F).and(X).reduce(javaReducer),is("(!F && X)"));

azzert.that(Proposition.not(F).or(X).reduce(javaReducer),is("(!F || X)"));

}@Test public void test168(){
azzert.that(Proposition.that(F).and(X).reduce(javaReducer),is("(F && X)"));

}@Test public void test169(){
azzert.that(Proposition.that(F).and(X).reduce(javaReducer),is("(F && X)"));

azzert.that(Proposition.that(F).or(X).reduce(javaReducer),is("(F || X)"));

}@Test public void test170(){
azzert.that(B3.reduce(javaReducer),is("((!F && !F) || X || N || T)"));

}@Test public void test171(){
azzert.that(B3.reduce(javaReducer),is("((!F && !F) || X || N || T)"));

azzert.that(NOT_F_AND_NOT_F_OR_X_OR_N_OR_T.reduce(javaReducer),is("((!F && !F) || X || N || T)"));

}@Test public void test172(){
azzert.that(B3.reduce(javaReducer),is("((!F && !F) || X || N || T)"));

azzert.that(NOT_F_AND_NOT_F_OR_X_OR_N_OR_T.reduce(javaReducer),is("((!F && !F) || X || N || T)"));

azzert.that(T_OR_F_OR_X_OR_NOT_F_AND_NOT_F_OR_X_OR_N_OR_T.reduce(javaReducer),is("(T || F || X || (!F && !F) || X || N || T)"));

}@Test public void test173(){
azzert.that(B3.reduce(javaReducer),is("((!F && !F) || X || N || T)"));

azzert.that(NOT_F_AND_NOT_F_OR_X_OR_N_OR_T.reduce(javaReducer),is("((!F && !F) || X || N || T)"));

azzert.that(T_OR_F_OR_X_OR_NOT_F_AND_NOT_F_OR_X_OR_N_OR_T.reduce(javaReducer),is("(T || F || X || (!F && !F) || X || N || T)"));

azzert.that(B4.reduce(javaReducer),is("(T || F || X || (!F && !F) || X || N || T)"));

}@Test public void test174(){
azzert.that(B3.reduce(javaReducer),is("((!F && !F) || X || N || T)"));

azzert.that(NOT_F_AND_NOT_F_OR_X_OR_N_OR_T.reduce(javaReducer),is("((!F && !F) || X || N || T)"));

azzert.that(T_OR_F_OR_X_OR_NOT_F_AND_NOT_F_OR_X_OR_N_OR_T.reduce(javaReducer),is("(T || F || X || (!F && !F) || X || N || T)"));

azzert.that(B4.reduce(javaReducer),is("(T || F || X || (!F && !F) || X || N || T)"));

azzert.that(B5.reduce(javaReducer),is("((T || F || X) && T && F && X)"));

}@Test public void test175(){
azzert.that(B3.reduce(javaReducer),is("((!F && !F) || X || N || T)"));

azzert.that(NOT_F_AND_NOT_F_OR_X_OR_N_OR_T.reduce(javaReducer),is("((!F && !F) || X || N || T)"));

azzert.that(T_OR_F_OR_X_OR_NOT_F_AND_NOT_F_OR_X_OR_N_OR_T.reduce(javaReducer),is("(T || F || X || (!F && !F) || X || N || T)"));

azzert.that(B4.reduce(javaReducer),is("(T || F || X || (!F && !F) || X || N || T)"));

azzert.that(B5.reduce(javaReducer),is("((T || F || X) && T && F && X)"));

azzert.that(B1_AND_B2.reduce(javaReducer),is("((T || F || X) && T && F && X)"));

}@Test public void test176(){
azzert.that(B3.reduce(javaReducer),is("((!F && !F) || X || N || T)"));

azzert.that(NOT_F_AND_NOT_F_OR_X_OR_N_OR_T.reduce(javaReducer),is("((!F && !F) || X || N || T)"));

azzert.that(T_OR_F_OR_X_OR_NOT_F_AND_NOT_F_OR_X_OR_N_OR_T.reduce(javaReducer),is("(T || F || X || (!F && !F) || X || N || T)"));

azzert.that(B4.reduce(javaReducer),is("(T || F || X || (!F && !F) || X || N || T)"));

azzert.that(B5.reduce(javaReducer),is("((T || F || X) && T && F && X)"));

azzert.that(B1_AND_B2.reduce(javaReducer),is("((T || F || X) && T && F && X)"));

azzert.that(B6.reduce(javaReducer),is("(T && F && X && (T || F || X))"));

}@Test public void test177(){
azzert.that(B3.reduce(javaReducer),is("((!F && !F) || X || N || T)"));

azzert.that(NOT_F_AND_NOT_F_OR_X_OR_N_OR_T.reduce(javaReducer),is("((!F && !F) || X || N || T)"));

azzert.that(T_OR_F_OR_X_OR_NOT_F_AND_NOT_F_OR_X_OR_N_OR_T.reduce(javaReducer),is("(T || F || X || (!F && !F) || X || N || T)"));

azzert.that(B4.reduce(javaReducer),is("(T || F || X || (!F && !F) || X || N || T)"));

azzert.that(B5.reduce(javaReducer),is("((T || F || X) && T && F && X)"));

azzert.that(B1_AND_B2.reduce(javaReducer),is("((T || F || X) && T && F && X)"));

azzert.that(B6.reduce(javaReducer),is("(T && F && X && (T || F || X))"));

azzert.that(B2_AND_B1.reduce(javaReducer),is("(T && F && X && (T || F || X))"));

}@Test public void test178(){
azzert.that(B3.reduce(javaReducer),is("((!F && !F) || X || N || T)"));

azzert.that(NOT_F_AND_NOT_F_OR_X_OR_N_OR_T.reduce(javaReducer),is("((!F && !F) || X || N || T)"));

azzert.that(T_OR_F_OR_X_OR_NOT_F_AND_NOT_F_OR_X_OR_N_OR_T.reduce(javaReducer),is("(T || F || X || (!F && !F) || X || N || T)"));

azzert.that(B4.reduce(javaReducer),is("(T || F || X || (!F && !F) || X || N || T)"));

azzert.that(B5.reduce(javaReducer),is("((T || F || X) && T && F && X)"));

azzert.that(B1_AND_B2.reduce(javaReducer),is("((T || F || X) && T && F && X)"));

azzert.that(B6.reduce(javaReducer),is("(T && F && X && (T || F || X))"));

azzert.that(B2_AND_B1.reduce(javaReducer),is("(T && F && X && (T || F || X))"));

azzert.that(B7.reduce(javaReducer),is("(T || F || X || (T && F && X))"));

}@Test public void test179(){
azzert.that(B3.reduce(javaReducer),is("((!F && !F) || X || N || T)"));

azzert.that(NOT_F_AND_NOT_F_OR_X_OR_N_OR_T.reduce(javaReducer),is("((!F && !F) || X || N || T)"));

azzert.that(T_OR_F_OR_X_OR_NOT_F_AND_NOT_F_OR_X_OR_N_OR_T.reduce(javaReducer),is("(T || F || X || (!F && !F) || X || N || T)"));

azzert.that(B4.reduce(javaReducer),is("(T || F || X || (!F && !F) || X || N || T)"));

azzert.that(B5.reduce(javaReducer),is("((T || F || X) && T && F && X)"));

azzert.that(B1_AND_B2.reduce(javaReducer),is("((T || F || X) && T && F && X)"));

azzert.that(B6.reduce(javaReducer),is("(T && F && X && (T || F || X))"));

azzert.that(B2_AND_B1.reduce(javaReducer),is("(T && F && X && (T || F || X))"));

azzert.that(B7.reduce(javaReducer),is("(T || F || X || (T && F && X))"));

azzert.that(B1_OR_B2.reduce(javaReducer),is("(T || F || X || (T && F && X))"));

}@Test public void test180(){
azzert.that(B3.reduce(javaReducer),is("((!F && !F) || X || N || T)"));

azzert.that(NOT_F_AND_NOT_F_OR_X_OR_N_OR_T.reduce(javaReducer),is("((!F && !F) || X || N || T)"));

azzert.that(T_OR_F_OR_X_OR_NOT_F_AND_NOT_F_OR_X_OR_N_OR_T.reduce(javaReducer),is("(T || F || X || (!F && !F) || X || N || T)"));

azzert.that(B4.reduce(javaReducer),is("(T || F || X || (!F && !F) || X || N || T)"));

azzert.that(B5.reduce(javaReducer),is("((T || F || X) && T && F && X)"));

azzert.that(B1_AND_B2.reduce(javaReducer),is("((T || F || X) && T && F && X)"));

azzert.that(B6.reduce(javaReducer),is("(T && F && X && (T || F || X))"));

azzert.that(B2_AND_B1.reduce(javaReducer),is("(T && F && X && (T || F || X))"));

azzert.that(B7.reduce(javaReducer),is("(T || F || X || (T && F && X))"));

azzert.that(B1_OR_B2.reduce(javaReducer),is("(T || F || X || (T && F && X))"));

azzert.that(B8.reduce(javaReducer),is("((T && F && X) || T || F || X)"));

}@Test public void test181(){
azzert.that(B3.reduce(javaReducer),is("((!F && !F) || X || N || T)"));

azzert.that(NOT_F_AND_NOT_F_OR_X_OR_N_OR_T.reduce(javaReducer),is("((!F && !F) || X || N || T)"));

azzert.that(T_OR_F_OR_X_OR_NOT_F_AND_NOT_F_OR_X_OR_N_OR_T.reduce(javaReducer),is("(T || F || X || (!F && !F) || X || N || T)"));

azzert.that(B4.reduce(javaReducer),is("(T || F || X || (!F && !F) || X || N || T)"));

azzert.that(B5.reduce(javaReducer),is("((T || F || X) && T && F && X)"));

azzert.that(B1_AND_B2.reduce(javaReducer),is("((T || F || X) && T && F && X)"));

azzert.that(B6.reduce(javaReducer),is("(T && F && X && (T || F || X))"));

azzert.that(B2_AND_B1.reduce(javaReducer),is("(T && F && X && (T || F || X))"));

azzert.that(B7.reduce(javaReducer),is("(T || F || X || (T && F && X))"));

azzert.that(B1_OR_B2.reduce(javaReducer),is("(T || F || X || (T && F && X))"));

azzert.that(B8.reduce(javaReducer),is("((T && F && X) || T || F || X)"));

azzert.that(B2_OR_B1.reduce(javaReducer),is("((T && F && X) || T || F || X)"));

}@Test public void test182(){
azzert.that(T_OR_F_OR_X.reduce(new PropositionReducer<String>(new ReduceStringConcatenate()){
  @Override protected String map(  @SuppressWarnings("unused") final BooleanSupplier __){
    return "";
  }
}
),is(""));

}@Test public void test183(){
assert T_OR_F_OR_X.eval();

}
}
