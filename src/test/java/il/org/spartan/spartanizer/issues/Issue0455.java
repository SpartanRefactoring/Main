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
    trimminKof("x -> {assert (1 < 2);}") //
        .using(LambdaExpression.class, new LambdaRemoveRedundantCurlyBraces())//
        .stays();
  }

  @Test public void blockStatementShouldntTip() {
    trimminKof("x -> {{}}") //
        .gives("λ -> {{}}") //
        .gives("λ -> {}") //
        .stays();
  }

  @Test public void breakStatementShouldntTip() {
    trimminKof("x -> {break;}") //
        .using(LambdaExpression.class, new LambdaRemoveRedundantCurlyBraces())//
        .stays();
  }

  @Test public void continueStatementShouldntTip() {
    trimminKof("x -> {continue;}") //
        .using(LambdaExpression.class, new LambdaRemoveRedundantCurlyBraces())//
        .stays();
  }

  @Test public void doWhileStatementShouldntTip() {
    trimminKof("x -> {do ++x; while(x < 10);}") //
        .using(LambdaExpression.class, new LambdaRemoveRedundantCurlyBraces())//
        .stays();
  }

  @Test public void emptyReturnStatement() {
    trimminKof("(x) -> {return;}")//
        .using(LambdaExpression.class, new LambdaRemoveRedundantCurlyBraces())//
        .gives("(x) -> {}")//
        .gives("x -> {}")//
        .using(LambdaExpression.class, new LambdaRemoveRedundantCurlyBraces())//
        .stays();
  }

  @Test public void emptyStatementShouldTip() {
    trimminKof("x -> {;}") //
        .gives("λ -> {;}") //
        .gives("λ -> {}") //
        .stays();
  }

  @Test public void lambdaBodyHasMoreThenOneStatementStays() {
    trimminKof("x -> {double y = -x + Math.PI; System.out.println(x / y); System.out.println(x / (2*y));}")//
        .stays();
  }

  @Test public void nestedLambdaExpression() {
    trimminKof("x -> y -> {return -y;}")//
        .gives("x -> y -> -y") //
    // TODO Yossi Gil, the next two lines regard #1115 - uncomment when fixed
    // .gives("x -> ¢ -> -¢") //
    // .stays() //
    ;
  }

  @Test public void paransAreNotRemovedFromParams() {
    trimminKof("(x) -> {return x;}") //
        .using(LambdaExpression.class, new LambdaRemoveRedundantCurlyBraces())//
        .gives("(x) -> x")//
        .gives("x -> x")//
        .gives("λ->λ")//
        .stays();
  }

  @Test public void parenthesisAreNotAddedToParameters() {
    trimminKof("x -> {return x;}")//
        .using(LambdaExpression.class, new LambdaRemoveRedundantCurlyBraces())//
        .gives("x -> x")//
        .using(LambdaExpression.class, new LambdaRemoveRedundantCurlyBraces())//
        .stays();
  }

  @Test public void simpleBiFunction() {
    trimminKof("(x,y) -> {return x + y;}") //
        .using(LambdaExpression.class, new LambdaRemoveRedundantCurlyBraces())//
        .gives("(x,y) -> x + y")//
        .using(LambdaExpression.class, new LambdaRemoveRedundantCurlyBraces())//
        .stays();
  }

  @Test public void simpleConsumerWithotTipper() {
    trimminKof("new Set<String>().forEach(item -> {return \"$\" + item;});")//
        .gives("new Set<String>().forEach(item -> \"$\" + item);")//
        .using(LambdaExpression.class, new LambdaRemoveRedundantCurlyBraces())//
        .stays();
  }

  @Test public void simpleProducer() {
    trimminKof("()->{return 42;}") //
        .using(LambdaExpression.class, new LambdaRemoveRedundantCurlyBraces())//
        .gives("()->42")//
        .stays();
  }

  @Test public void singleClassDeclarationStatementShouldntTip() {
    trimminKof("x -> {class A {}}") //
        .stays();
  }

  @Test public void singleConstructorInvocationStatement() {
    trimminKof("x -> {new Object();}") //
        .using(LambdaExpression.class, new LambdaRemoveRedundantCurlyBraces())//
        .gives("x -> new Object()") //
        .using(LambdaExpression.class, new LambdaRemoveRedundantCurlyBraces())//
        .stays();
  }

  @Test public void singleEnhancedForStatementShouldntTip() {
    trimminKof("x -> { for (String y : l)if (y.equals(x))return;}") //
        .using(LambdaExpression.class, new LambdaRemoveRedundantCurlyBraces())//
        .stays();
  }

  // "(λ)->λ"
  @Test public void singleIfStatementShouldntTip() {
    trimminKof("x -> {if(x > 0)--x;}") //
        .gives("λ-> {if(λ > 0)--λ;}") //
        .stays();
  }

  @Test public void singleNonReturnStatement0() {
    trimminKof("(x) -> {System.out.println(x);}") //
        .using(LambdaExpression.class, new LambdaRemoveRedundantCurlyBraces())//
        .gives("(x) -> System.out.println(x)") //
        .gives("x -> System.out.println(x)") //
        .gives("λ -> System.out.println(λ)") //
        .stays();
  }

  @Test public void singleNonReturnStatement() {
    trimminKof("Consumer<Integer> x = (x) -> {System.out.println(x);};") //
        .using(LambdaExpression.class, new LambdaRemoveRedundantCurlyBraces())//
        .gives("Consumer<Integer> x = (x) -> System.out.println(x);") //
        .gives("") //
        .stays();
  }

  @Test public void singleRangeBasedForStatementShouldntTip() {
    trimminKof("x -> { for (;;)break;}") //
        .using(LambdaExpression.class, new LambdaRemoveRedundantCurlyBraces())//
        .stays();
  }

  @Test public void singleReturnStatementAndSingleParameterd() {
    trimminKof("new ArrayList<Integer>().map(x->{return x+1;});") //
        .using(LambdaExpression.class, new LambdaRemoveRedundantCurlyBraces())//
        .gives("new ArrayList<Integer>().map(x -> x+1);")//
        .using(LambdaExpression.class, new LambdaRemoveRedundantCurlyBraces())//
        .stays();
  }

  @Test public void singleSwitchCaseStatementShouldntTip() {
    trimminKof("x -> {switch(x){ case 0: ++x; break; default: --x;}}") //
        .gives("λ -> {switch(λ){ case 0: ++λ; break; default: --λ;}}"); //
  }

  @Test public void singleSynchronizedStatementShouldntTip() {
    trimminKof("x -> {synchronized (System.in){}}") //
        .gives("λ -> {synchronized (System.in){}}") //
        .stays();
  }

  @Test public void singleThrowStatementShouldntTip() {
    trimminKof("x -> {throw new Error();}") //
        .gives("λ -> {throw new Error();}") //
        .stays();
  }

  @Test public void singleTryFinallyStatementShouldntTip() {
    trimminKof("x -> {try {throw new Error();}finally{}}") //
        .gives("x -> {{throw new Error();}}") //
        .gives("λ -> {{throw new Error();}}") //
        .gives("λ -> {throw new Error();}") //
        .stays();
  }

  @Test public void singleTryStatementShouldntTip() {
    trimminKof("x -> {try {throw new Error();}catch(Exception __){}}") //
        .using(LambdaExpression.class, new LambdaRemoveRedundantCurlyBraces())//
        .stays();
  }

  @Test public void superConstructrInvocationShouldntTip() {
    trimminKof("x ->{super(x);}") //
        .using(LambdaExpression.class, new LambdaRemoveRedundantCurlyBraces())//
        .stays();
  }

  @Test public void whileStatementShouldntTip() {
    trimminKof("x->{while(x < 0);}") //
        .using(LambdaExpression.class, new LambdaRemoveRedundantCurlyBraces())//
        .stays();
  }
}
