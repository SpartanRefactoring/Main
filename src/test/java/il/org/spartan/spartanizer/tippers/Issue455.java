package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** @author Oren Afek
 * @since 2016 Testing {@link LambdaExpressionRemoveRedundantCurlyBraces } */
@SuppressWarnings("static-method")
public class Issue455 {
  @Test public void assertStatementShouldntTip() {
    trimmingOf("x -> {assert (1 < 2);}") //
        .stays();
  }

  @Test public void blockStatementShouldntTip() {
    trimmingOf("x -> {{}}") //
        .gives("x -> {}") //
        .stays();
  }

  @Test public void breakStatementShouldntTip() {
    trimmingOf("x -> {break;}") //
        .stays();
  }

  @Test public void continueStatementShouldntTip() {
    trimmingOf("x -> {continue;}") //
        .stays();
  }

  @Test public void doWhileStatementShouldntTip() {
    trimmingOf("x -> {do ++x; while(x < 10);}") //
        .stays();
  }

  @Test public void emptyReturnStatement() {
    trimmingOf("(x) -> {return;}").withTipper(LambdaExpression.class, new LambdaExpressionRemoveRedundantCurlyBraces())//
        .gives("(x) -> {}")//
        .stays();
  }

  @Test public void emptyStatementShouldTip() {
    trimmingOf("x -> {;}") //
        .gives("x -> {}") //
        .stays();
  }

  @Test public void lambdaBodyHasMoreThenOneStatementStays() {
    trimmingOf("x -> {double y = -x + Math.PI; System.out.println(x / y); System.out.println(x / (2*y));}")//
        .stays();
  }

  @Test public void nestedLambdaExpression() {
    trimmingOf("x -> y -> {return -y;}").gives("x -> y -> -y") //
        .stays();
  }

  @Test public void paransAreNotAddedToParams() {
    trimmingOf("x -> {return x;}").withTipper(LambdaExpression.class, new LambdaExpressionRemoveRedundantCurlyBraces())//
        .gives("x -> x")//
        .stays();
  }

  @Test public void paransAreNotRemovedFromParams() {
    trimmingOf("(x) -> {return x;}").withTipper(LambdaExpression.class, new LambdaExpressionRemoveRedundantCurlyBraces())//
        .gives("(x) -> x")//
        .stays();
  }

  @Test public void simpleBiFunction() {
    trimmingOf("(x,y) -> {return x + y;}").withTipper(LambdaExpression.class, new LambdaExpressionRemoveRedundantCurlyBraces())
        .gives("(x,y) -> x + y")//
        .stays();
  }

  @Test public void simpleConsumerWithotTipper() {
    trimmingOf("new Set<String>().forEach(item -> {return \"$\" + item;});")//
        .gives("new Set<String>().forEach(item -> \"$\" + item);")//
        .stays();
  }

  @Test public void simpleProducer() {
    trimmingOf("()->{return 42;}").withTipper(LambdaExpression.class, new LambdaExpressionRemoveRedundantCurlyBraces())//
        .gives("()->42")//
        .stays();
  }

  @Test public void singleClassDeclarationStatementShouldntTip() {
    trimmingOf("x -> {class A {}}") //
        .stays();
  }

  @Test public void singleConstructorInvocationStatement() {
    trimmingOf("x -> {new Object();}") //
        .gives("x -> new Object()") //
        .stays();
  }

  @Test public void singleEnhancedForStatementShouldntTip() {
    trimmingOf("x -> { " + "for (String y : l)" + "if (y.equals(x))" + "return;" + "}") //
        .stays();
  }

  @Test public void singleIfStatementShouldntTip() {
    trimmingOf("x -> {if(x > 0)--x;}") //
        .stays();
  }

  @Test public void singleNonReturnStatement() {
    trimmingOf("Consumer<Integer> x = (x) -> {System.out.println(x);};") //
        .gives("Consumer<Integer> x = (x) -> System.out.println(x);") //
        .stays();
  }

  @Test public void singleRangeBasedForStatementShouldntTip() {
    trimmingOf("x -> { " + "for (;;)" + "break;" + "}") //
        .stays();
  }

  @Test public void singleReturnStatementAndSingleParameterd() {
    trimmingOf("new ArrayList<Integer>().map(x->{return x+1;});").withTipper(LambdaExpression.class, new LambdaExpressionRemoveRedundantCurlyBraces())//
        .gives("new ArrayList<Integer>().map(x -> x+1);")//
        .stays();
  }

  @Test public void singleSwitchCaseStatementShouldntTip() {
    trimmingOf("x -> {switch(x){ case 0: ++x; break; default: --x;}}") //
        .stays();
  }

  @Test public void singleSynchronizedStatementShouldntTip() {
    trimmingOf("x -> {synchronized (System.in){}}") //
        .stays();
  }

  @Test public void singleThrowStatementShouldntTip() {
    trimmingOf("x -> {throw new Error();}") //
        .stays();
  }

  @Test public void singleTryFinallyStatementShouldntTip() {
    trimmingOf("x -> {try {throw new Error();}finally{}}") //
        .stays();
  }

  @Test public void singleTryStatementShouldntTip() {
    trimmingOf("x -> {try {throw new Error();}catch(Exception __){}}") //
        .stays();
  }

  @Test public void singleVariableDeclarationStatementShouldntTip() {
    trimmingOf("x -> {int y;}") //
        .stays();
  }

  @Test public void superConstructrInvocationShouldntTip() {
    trimmingOf("x -> {super(x);}") //
        .stays();
  }

  @Test public void whileStatementShouldntTip() {
    trimmingOf("x -> {while(x < 0);}") //
        .stays();
  }
}
