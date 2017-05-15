package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.tippers.*;

/** TUnit tests for the GitHub issue thus numbered
 * @author Oren Afek
 * @since 2016 Testing {@link LambdaRemoveRedundantCurlyBraces } */
@SuppressWarnings("static-method")
public class Issue0455 {
  @Test public void assertStatementShouldntTip() {
    trimmingOf("x -> {assert (1 < 2);}") //
        .using(new LambdaRemoveRedundantCurlyBraces(), LambdaExpression.class)//
        .stays();
  }
  @Test public void blockStatementShouldntTip() {
    trimmingOf("x -> {{}}") //
        .gives("λ -> {{}}") //
        .gives("λ -> {}") //
        .stays();
  }
  @Test public void breakStatementShouldntTip() {
    trimmingOf("x -> {break;}") //
        .using(new LambdaRemoveRedundantCurlyBraces(), LambdaExpression.class)//
        .stays();
  }
  @Test public void continueStatementShouldntTip() {
    trimmingOf("x -> {continue;}") //
        .using(new LambdaRemoveRedundantCurlyBraces(), LambdaExpression.class)//
        .stays();
  }
  @Test public void doWhileStatementShouldntTip() {
    trimmingOf("x -> {do ++x; while(x < 10);}") //
        .using(new LambdaRemoveRedundantCurlyBraces(), LambdaExpression.class)//
        .stays();
  }
  @Test public void emptyReturnStatement() {
    trimmingOf("(x) -> {return;}")//
        .using(new LambdaRemoveRedundantCurlyBraces(), LambdaExpression.class)//
        .gives("(x) -> {}")//
        .gives("x -> {}")//
        .using(new LambdaRemoveRedundantCurlyBraces(), LambdaExpression.class)//
        .stays();
  }
  @Test public void emptyStatementShouldTip() {
    trimmingOf("x -> {;}") //
        .gives("λ -> {;}") //
        .gives("λ -> {}") //
        .stays();
  }
  @Test public void lambdaBodyHasMoreThenOneStatementStays() {
    trimmingOf("x -> {double y = -x + Math.PI; System.out.println(x / y); System.out.println(x / (2*y));}")//
        .stays();
  }
  @Test public void nestedLambdaExpression() {
    trimmingOf("x -> y -> {return -y;}")//
        .gives("x -> y -> -y") //
    // TODO Yossi Gil, the next two lines regard #1115 - uncomment when fixed
    // .gives("x -> ¢ -> -¢") //
    // .stays() //
    ;
  }
  @Test public void paransAreNotRemovedFromParams() {
    trimmingOf("(x) -> {return x;}") //
        .using(new LambdaRemoveRedundantCurlyBraces(), LambdaExpression.class)//
        .gives("(x) -> x")//
        .gives("x -> x")//
        .gives("λ->λ")//
        .stays();
  }
  @Test public void parenthesisAreNotAddedToParameters() {
    trimmingOf("x -> {return x;}")//
        .using(new LambdaRemoveRedundantCurlyBraces(), LambdaExpression.class)//
        .gives("x -> x")//
        .using(new LambdaRemoveRedundantCurlyBraces(), LambdaExpression.class)//
        .stays();
  }
  @Test public void simpleBiFunction() {
    trimmingOf("(x,y) -> {return x + y;}") //
        .using(new LambdaRemoveRedundantCurlyBraces(), LambdaExpression.class)//
        .gives("(x,y) -> x + y")//
        .using(new LambdaRemoveRedundantCurlyBraces(), LambdaExpression.class)//
        .stays();
  }
  @Test public void simpleConsumerWithotTipper() {
    trimmingOf("new Set<String>().forEach(item -> {return \"$\" + item;});")//
        .gives("new Set<String>().forEach(item -> \"$\" + item);")//
        .using(new LambdaRemoveRedundantCurlyBraces(), LambdaExpression.class)//
        .stays();
  }
  @Test public void simpleProducer() {
    trimmingOf("()->{return 42;}") //
        .using(new LambdaRemoveRedundantCurlyBraces(), LambdaExpression.class)//
        .gives("()->42")//
        .stays();
  }
  @Test public void singleClassDeclarationStatementShouldntTip() {
    trimmingOf("x -> {class A {}}") //
        .stays();
  }
  @Test public void singleConstructorInvocationStatement() {
    trimmingOf("x -> {new Object();}") //
        .using(new LambdaRemoveRedundantCurlyBraces(), LambdaExpression.class)//
        .gives("x -> new Object()") //
        .using(new LambdaRemoveRedundantCurlyBraces(), LambdaExpression.class)//
        .stays();
  }
  @Test public void singleEnhancedForStatementShouldntTip() {
    trimmingOf("x -> { for (String y : l)if (y.equals(x))return;}") //
        .using(new LambdaRemoveRedundantCurlyBraces(), LambdaExpression.class)//
        .stays();
  }
  // "(λ)->λ"
  @Test public void singleIfStatementShouldntTip() {
    trimmingOf("x -> {if(x > 0)--x;}") //
        .gives("λ-> {if(λ > 0)--λ;}") //
        .stays();
  }
  @Test public void singleNonReturnStatement0() {
    trimmingOf("(x) -> {System.out.println(x);}") //
        .using(new LambdaRemoveRedundantCurlyBraces(), LambdaExpression.class)//
        .gives("(x) -> System.out.println(x)") //
        .gives("x -> System.out.println(x)") //
        .gives("λ -> System.out.println(λ)") //
        .stays();
  }
  @Test public void singleNonReturnStatement() {
    trimmingOf("Consumer<Integer> x = (x) -> {System.out.println(x);};") //
        .using(new LambdaRemoveRedundantCurlyBraces(), LambdaExpression.class)//
        .gives("Consumer<Integer> x = (x) -> System.out.println(x);") //
        .gives("") //
        .stays();
  }
  @Test public void singleRangeBasedForStatementShouldntTip() {
    trimmingOf("x -> { for (;;)break;}") //
        .using(new LambdaRemoveRedundantCurlyBraces(), LambdaExpression.class)//
        .stays();
  }
  @Test public void singleReturnStatementAndSingleParameterd() {
    trimmingOf("new ArrayList<Integer>().map(x->{return x+1;});") //
        .using(new LambdaRemoveRedundantCurlyBraces(), LambdaExpression.class)//
        .gives("new ArrayList<Integer>().map(x -> x+1);")//
        .using(new LambdaRemoveRedundantCurlyBraces(), LambdaExpression.class)//
        .stays();
  }
  @Test public void singleSwitchCaseStatementShouldntTip() {
    trimmingOf("x -> {switch(x){ case 0: ++x; break; default: --x;}}") //
        .gives("λ -> {switch(λ){ case 0: ++λ; break; default: --λ;}}"); //
  }
  @Test public void singleSynchronizedStatementShouldntTip() {
    trimmingOf("x -> {synchronized (System.in){}}") //
        .gives("λ -> {synchronized (System.in){}}") //
        .stays();
  }
  @Test public void singleThrowStatementShouldntTip() {
    trimmingOf("x -> {throw new Error();}") //
        .gives("λ -> {throw new Error();}") //
        .stays();
  }
  @Test public void singleTryFinallyStatementShouldntTip() {
    trimmingOf("x -> {try {throw new Error();}finally{}}") //
        .gives("x -> {{throw new Error();}}") //
        .gives("λ -> {{throw new Error();}}") //
        .gives("λ -> {throw new Error();}") //
        .stays();
  }
  @Test public void singleTryStatementShouldntTip() {
    trimmingOf("x -> {try {throw new Error();}catch(Exception __){}}") //
        .using(new LambdaRemoveRedundantCurlyBraces(), LambdaExpression.class)//
        .stays();
  }
  @Test public void superConstructrInvocationShouldntTip() {
    trimmingOf("x ->{super(x);}") //
        .using(new LambdaRemoveRedundantCurlyBraces(), LambdaExpression.class)//
        .stays();
  }
  @Test public void whileStatementShouldntTip() {
    trimmingOf("x->{while(x < 0);}") //
        .using(new LambdaRemoveRedundantCurlyBraces(), LambdaExpression.class)//
        .stays();
  }
}
