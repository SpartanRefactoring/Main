package il.org.spartan.spartanizer.ast.navigate;


import java.util.*;
import java.util.List;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.engine.*;

/** Tests for {@link have}, regarding issue #807
 * @author Kfir Marx
 * @authoe Raviv Rachmiel
 * @since 10-11-2016 */
@SuppressWarnings({"static-method","javadoc"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public final class haveTest {
  
  public List<Expression> ExpressionListMaker(String[] exps) {
    List<Expression> $ = new LinkedList<>();
    for(String e : exps)
      $.add(into.e(e));
    return $;

  }
  
  @Test public void booleanLiteralTestTrue() {
    azzert.assertTrue(have.booleanLiteral(ExpressionListMaker(new String[] {"1==1","2==2","true"}))); 
  }
  @Test public void booleanLiteralTestFalse() {
    azzert.assertFalse(have.booleanLiteral(ExpressionListMaker(new String[] {"1==1","2==2"}))); 
  }

  @Test public void booleanFalseLiteralTestTrue() {
    azzert.assertTrue(have.falseLiteral(ExpressionListMaker(new String[] {"false"})));
  }
  
  @Test public void booleanFalseLiteralTestFalse() {
    azzert.assertFalse(have.falseLiteral(ExpressionListMaker(new String[] {"true"})));
  }
  
  @Test public void hasLiteralTestTrue() {
    azzert.assertTrue(have.literal(ExpressionListMaker(new String[] {"2"})));
  }
  
  @Test public void hasLiteralTestFalse() {
    azzert.assertFalse(have.literal(ExpressionListMaker(new String[] {"1==2"})));
  }
  
  
  @Test public void booleanLiteralOneExpretionSucsses(){
    azzert.assertTrue(have.booleanLiteral(into.e("true")));
  }
  
  @Test public void booleanLiteralOneExpretionFail(){
    azzert.assertFalse(have.booleanLiteral(into.e("x=y")));
  }
  
  @Test public void literalFailOnAssigment(){
    azzert.assertFalse(have.literal(into.e("x=y")));
  }
  
  @Test public void literalFailOnComparison(){
    azzert.assertFalse(have.literal(into.e("value1 == value2")));
  }
  
  @Test public void literalSucssessForNumericalLit(){
    azzert.assertTrue(have.literal(into.e("5")));
  }
  
  
  @Test public void literalSucssessForString(){
    azzert.assertTrue(have.literal(into.e("\"java is the best!\"")));
  }
}
