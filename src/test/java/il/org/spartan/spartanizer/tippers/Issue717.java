package il.org.spartan.spartanizer.tippers;

import static org.junit.Assert.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.utils.tdd.*;

/** see Issue #717 for more details
 * @author Lidia Piatigorski
 * @author Nikita Dizhur
 * @author Alex V.
 * @since 16-11-05 */
@SuppressWarnings("static-method") public class Issue717 {
  
  MethodDeclaration fiveStatMethod = (MethodDeclaration) wizard.ast("public void foo() {int a; int b; int c; int d; int e;}");

  private static final String CHAR_LIST =
      "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
  
  @Test public void isCompiled() {
    assert true;
  }
  
  @Test  public void nullCheckReturnsFalse(){
    assertFalse (determineIf.hasBigBlock(null));
  }


  @Test public void fiveStatBlockReturnsTrue() {
    assertTrue(determineIf.hasBigBlock(fiveStatMethod));
  }
  
  private String generateRandomString(int maxLen) {
    StringBuffer randStr = new StringBuffer();
    int len = 0;
    Random randomGenerator = new Random();
    len = randomGenerator.nextInt(maxLen);
    if(len<=0)
      len = 1;
    for (int ¢ = 0; ¢ < len; ++¢)
      randStr.append(CHAR_LIST.charAt(randomGenerator.nextInt(CHAR_LIST.length())));
    return randStr + "";
  }
  
  private final int MAX_NAME_SIZE = 100;
  private final int MAX_STAT_AMOUNT = 100;
  
  @Test public void randomBigBlockReturnsTrue() {
    String methodName = generateRandomString(MAX_NAME_SIZE);
    String firstStat = "{int x;";
    String nextStat = "x=4;";
    
    int statAmount = 0;
    Random randomGenerator = new Random();
    statAmount = randomGenerator.nextInt(MAX_STAT_AMOUNT);
    if(statAmount<4)
      statAmount = 4;
    

    String randomBigBlock = "public void " + methodName+"()"+firstStat;
    for(int ¢=0; ¢<statAmount; ++¢)
      randomBigBlock += nextStat;
    randomBigBlock += "}";
    
    assertTrue(determineIf.hasBigBlock(((MethodDeclaration) wizard.ast(randomBigBlock))));
  }
  

}