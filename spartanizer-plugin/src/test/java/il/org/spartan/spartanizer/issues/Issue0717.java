package il.org.spartan.spartanizer.issues;

import java.util.Random;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.junit.Test;

import fluent.ly.range;
import il.org.spartan.spartanizer.ast.factory.make;
import il.org.spartan.spartanizer.utils.tdd.determineIf;

/** see Issue0 #717 for more details
 * @author Lidia Piatigorski
 * @author Nikita Dizhur
 * @author Alex V.
 * @since 16-11-05 */
@SuppressWarnings("static-method") //
public class Issue0717 {
  private static final String CHAR_LIST = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
  private static final int MAX_NAME_SIZE = 100;
  private static final int MAX_STAT_AMOUNT = 200;
  final MethodDeclaration fiveStatMethod = (MethodDeclaration) make.ast("public void foo() {int a; int b; int c; int d; int e;}");
  final MethodDeclaration oneStatMethod = (MethodDeclaration) make.ast("public void foo() {int a; }");
  final MethodDeclaration fourStatMethod = (MethodDeclaration) make.ast("public void foo() {int a; ; ; ; }");

  @Test public void bigBlockWithAnnotationReturnsTrue() {
    assert determineIf.hasBigBlock((MethodDeclaration) make.ast("@Override public int f(){;;;;;}"));
  }
  @Test public void fiveStatBlockReturnsTrue() {
    assert determineIf.hasBigBlock(fiveStatMethod);
  }
  @Test public void fourStatBlockReturnsFalse() {
    assert !determineIf.hasBigBlock(fourStatMethod);
  }
  private String generateRandomString() {
    final StringBuilder $ = new StringBuilder();
    final Random randomGenerator = new Random();
    final int len = Math.max(1, randomGenerator.nextInt(Issue0717.MAX_NAME_SIZE));
    $.append(CHAR_LIST.charAt(randomGenerator.nextInt(CHAR_LIST.length() - 10)));
    range.from(1).to(len).forEach(λ -> $.append(CHAR_LIST.charAt(randomGenerator.nextInt(CHAR_LIST.length()))));
    return $ + "";
  }
  @Test public void isCompiled() {
    assert true;
  }
  @Test public void methodWithNoBodyReturnsFalse() {
    assert !determineIf.hasBigBlock((MethodDeclaration) make.ast("public int a(String a);"));
  }
  @Test public void methodWithNoStatementsReturnsFalse() {
    assert !determineIf.hasBigBlock((MethodDeclaration) make.ast("public int f(int x){}"));
  }
  @Test public void nullCheckReturnsFalse() {
    assert !determineIf.hasBigBlock(null);
  }
  @Test public void oneStatBlockReturnsFalse() {
    assert !determineIf.hasBigBlock(oneStatMethod);
  }
  @Test public void randomBigBlockReturnsTrue() {
    final String methodName = generateRandomString(), firstStat = "{int x; ++x;", nextStat = "x=4;";
    final Random random = new Random();
    final int statAmount = random.nextInt(MAX_STAT_AMOUNT) < 10 ? 10 : random.nextInt(MAX_STAT_AMOUNT);
    String randomBigBlock = "public void " + methodName + "()" + firstStat;
    for (int ¢ = 0; ¢ < statAmount; ++¢)
      randomBigBlock += nextStat;
    randomBigBlock += "}";
    assert determineIf.hasBigBlock((MethodDeclaration) make.ast(randomBigBlock));
  }
  @Test public void smallBlockWithAnnotationReturnsFalse() {
    assert !determineIf.hasBigBlock((MethodDeclaration) make.ast("@Inherited private void g(){;;;;}"));
  }
  @Test public void smallBlockWithModifierReturnsFalse() {
    assert !determineIf.hasBigBlock((MethodDeclaration) make.ast("public static void g(){;;;;}"));
  }
  @Test public void smallBlockWithModifierReturnsTrue() {
    assert determineIf.hasBigBlock((MethodDeclaration) make.ast("private static void g(){;;;;;}"));
  }
}
