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
  @Test public void booleanLiteralTestTrue() {
    List<Expression> expressions = new LinkedList<>();
    expressions.add(into.e("1==1"));
    expressions.add(into.e("2==2"));
    expressions.add(into.e("true"));
    azzert.assertTrue(have.booleanLiteral(expressions));
  }
  @Test public void booleanLiteralTestFalse() {
    List<Expression> expressions = new LinkedList<>();
    expressions.add(into.e("1==1"));
    expressions.add(into.e("2==2"));
    azzert.assertFalse(have.booleanLiteral(expressions));
  }
 
  @Test public void booleanFalseLiteralTestTrue() {
    List<Expression> expressions = new LinkedList<>();
    expressions.add(into.e("false"));
    azzert.assertTrue(have.falseLiteral(expressions));
  }
  
  @Test public void booleanFalseLiteralTestFalse() {
    List<Expression> expressions = new LinkedList<>();
    expressions.add(into.e("true"));
    azzert.assertFalse(have.falseLiteral(expressions));
  }
  
  @Test public void hasLiteralTestTrue() {
    List<Expression> expressions = new LinkedList<>();
    expressions.add(into.e("2"));
    azzert.assertTrue(have.literal(expressions));
  }
  
  @Test public void hasLiteralTestFalse() {
    List<Expression> expressions = new LinkedList<>();
    expressions.add(into.e("1==2"));
    azzert.assertFalse(have.literal(expressions));
  }
  
}
