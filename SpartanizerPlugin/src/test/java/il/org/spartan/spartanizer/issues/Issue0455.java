package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.tippers.*;

/** TUnit tests for the GitHub issue thus numbered
 * @author Oren Afek
 * @since 2016 Testing {@link LambdaRemoveRedundantCurlyBraces } */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings("static-method")
public class Issue0455 {
  @Test public void assertStatementShouldntTip() {
    topDownTrimming("x -> {assert (1 < 2);}") //
        .using(LambdaExpression.class, new LambdaRemoveRedundantCurlyBraces())//
        .stays();
  }

  @Test public void blockStatementShouldntTip() {
    topDownTrimming("x -> {{}}") //
        .gives("λ -> {{}}") //
        .gives("λ -> {}") //
        .stays();
  }

  @Test public void breakStatementShouldntTip() {
    topDownTrimming("x -> {break;}") //
        .using(LambdaExpression.class, new LambdaRemoveRedundantCurlyBraces())//
        .stays();
  }

  @Test public void continueStatementShouldntTip() {
    topDownTrimming("x -> {continue;}") //
        .using(LambdaExpression.class, new LambdaRemoveRedundantCurlyBraces())//
        .stays();
  }

  @Test public void doWhileStatementShouldntTip() {
    topDownTrimming("x -> {do ++x; while(x < 10);}") //
        .using(LambdaExpression.class, new LambdaRemoveRedundantCurlyBraces())//
        .stays();
  }

  @Test public void emptyReturnStatement() {
    topDownTrimming("(x) -> {return;}")//
        .using(LambdaExpression.class, new LambdaRemoveRedundantCurlyBraces())//
        .gives("(x) -> {}")//
        .gives("x -> {}")//
        .using(LambdaExpression.class, new LambdaRemoveRedundantCurlyBraces())//
        .stays();
  }

  @Test public void emptyStatementShouldTip() {
    topDownTrimming("x -> {;}") //
        .gives("λ -> {;}") //
        .gives("λ -> {}") //
        .stays();
  }

  @Test public void lambdaBodyHasMoreThenOneStatementStays() {
    topDownTrimming("x -> {double y = -x + Math.PI; System.out.println(x / y); System.out.println(x / (2*y));}")//
        .stays();
  }

  @Test public void nestedLambdaExpression() {
    topDownTrimming("x -> y -> {return -y;}")//
        .gives("x -> y -> -y") //
    // TODO Yossi Gil, the next two lines regard #1115 - uncomment when fixed
    // .gives("x -> ¢ -> -¢") //
    // .stays() //
    ;
  }

  @Test public void paransAreNotRemovedFromParams() {
    topDownTrimming("(x) -> {return x;}") //
        .using(LambdaExpression.class, new LambdaRemoveRedundantCurlyBraces())//
        .gives("(x) -> x")//
        .gives("x -> x")//
        .gives("λ->λ")//
        .stays();
  }

  @Test public void parenthesisAreNotAddedToParameters() {
    topDownTrimming("x -> {return x;}")//
        .using(LambdaExpression.class, new LambdaRemoveRedundantCurlyBraces())//
        .gives("x -> x")//
        .using(LambdaExpression.class, new LambdaRemoveRedundantCurlyBraces())//
        .stays();
  }

  @Test public void simpleBiFunction() {
    topDownTrimming("(x,y) -> {return x + y;}") //
        .using(LambdaExpression.class, new LambdaRemoveRedundantCurlyBraces())//
        .gives("(x,y) -> x + y")//
        .using(LambdaExpression.class, new LambdaRemoveRedundantCurlyBraces())//
        .stays();
  }

  @Test public void simpleConsumerWithotTipper() {
    topDownTrimming("new Set<String>().forEach(item -> {return \"$\" + item;});")//
        .gives("new Set<String>().forEach(item -> \"$\" + item);")//
        .using(LambdaExpression.class, new LambdaRemoveRedundantCurlyBraces())//
        .stays();
  }

  @Test public void simpleProducer() {
    topDownTrimming("()->{return 42;}") //
        .using(LambdaExpression.class, new LambdaRemoveRedundantCurlyBraces())//
        .gives("()->42")//
        .stays();
  }

  @Test public void singleClassDeclarationStatementShouldntTip() {
    topDownTrimming("x -> {class A {}}") //
        .stays();
  }

  @Test public void singleConstructorInvocationStatement() {
    topDownTrimming("x -> {new Object();}") //
        .using(LambdaExpression.class, new LambdaRemoveRedundantCurlyBraces())//
        .gives("x -> new Object()") //
        .using(LambdaExpression.class, new LambdaRemoveRedundantCurlyBraces())//
        .stays();
  }

  @Test public void singleEnhancedForStatementShouldntTip() {
    topDownTrimming("x -> { for (String y : l)if (y.equals(x))return;}") //
        .using(LambdaExpression.class, new LambdaRemoveRedundantCurlyBraces())//
        .stays();
  }

  // "(λ)->λ"
  @Test public void singleIfStatementShouldntTip() {
    topDownTrimming("x -> {if(x > 0)--x;}") //
        .gives("λ-> {if(λ > 0)--λ;}") //
        .stays();
  }

  @Test public void singleNonReturnStatement0() {
    topDownTrimming("(x) -> {System.out.println(x);}") //
        .using(LambdaExpression.class, new LambdaRemoveRedundantCurlyBraces())//
        .gives("(x) -> System.out.println(x)") //
        .gives("x -> System.out.println(x)") //
        .gives("λ -> System.out.println(λ)") //
        .stays();
  }

  @Test public void singleNonReturnStatement() {
    topDownTrimming("Consumer<Integer> x = (x) -> {System.out.println(x);};") //
        .using(LambdaExpression.class, new LambdaRemoveRedundantCurlyBraces())//
        .gives("Consumer<Integer> x = (x) -> System.out.println(x);") //
        .gives("") //
        .stays();
  }

  @Test public void singleRangeBasedForStatementShouldntTip() {
    topDownTrimming("x -> { for (;;)break;}") //
        .using(LambdaExpression.class, new LambdaRemoveRedundantCurlyBraces())//
        .stays();
  }

  @Test public void singleReturnStatementAndSingleParameterd() {
    topDownTrimming("new ArrayList<Integer>().map(x->{return x+1;});") //
        .using(LambdaExpression.class, new LambdaRemoveRedundantCurlyBraces())//
        .gives("new ArrayList<Integer>().map(x -> x+1);")//
        .using(LambdaExpression.class, new LambdaRemoveRedundantCurlyBraces())//
        .stays();
  }

  @Test public void singleSwitchCaseStatementShouldntTip() {
    topDownTrimming("x -> {switch(x){ case 0: ++x; break; default: --x;}}") //
        .gives("λ -> {switch(λ){ case 0: ++λ; break; default: --λ;}}"); //
  }

  @Test public void singleSynchronizedStatementShouldntTip() {
    topDownTrimming("x -> {synchronized (System.in){}}") //
        .gives("λ -> {synchronized (System.in){}}") //
        .stays();
  }

  @Test public void singleThrowStatementShouldntTip() {
    topDownTrimming("x -> {throw new Error();}") //
        .gives("λ -> {throw new Error();}") //
        .stays();
  }

  @Test public void singleTryFinallyStatementShouldntTip() {
    topDownTrimming("x -> {try {throw new Error();}finally{}}") //
        .gives("x -> {{throw new Error();}}") //
        .gives("λ -> {{throw new Error();}}") //
        .gives("λ -> {throw new Error();}") //
        .stays();
  }

  @Test public void singleTryStatementShouldntTip() {
    topDownTrimming("x -> {try {throw new Error();}catch(Exception __){}}") //
        .using(LambdaExpression.class, new LambdaRemoveRedundantCurlyBraces())//
        .stays();
  }

  @Test public void superConstructrInvocationShouldntTip() {
    topDownTrimming("x ->{super(x);}") //
        .using(LambdaExpression.class, new LambdaRemoveRedundantCurlyBraces())//
        .stays();
  }

  @Test public void whileStatementShouldntTip() {
    topDownTrimming("x->{while(x < 0);}") //
        .using(LambdaExpression.class, new LambdaRemoveRedundantCurlyBraces())//
        .stays();
  }
}
