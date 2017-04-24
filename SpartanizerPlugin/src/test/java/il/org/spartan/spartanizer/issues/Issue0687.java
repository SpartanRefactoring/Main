package il.org.spartan.spartanizer.issues;

import static il.org.spartan.azzert.*;

import static il.org.spartan.lisp.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.utils.tdd.*;
import nano.ly.*;

/** Tests for issue 687
 * @author Raviv Rachmiel
 * @author Kfir Marx
 * @since 2-11-2016 */
@SuppressWarnings("static-method")
public class Issue0687 {
  private static void forceTypeAtCompileTime(@SuppressWarnings("unused") final List<Name> __) {
    assert true;
  }

  @Test public void testCheckActualName() {
    assert "a".equals(the.headOf(getAll.names((Block) make.ast("{a=1+1;}"))) + "");
  }

  @Test public void testCheckNamesFineBlock() {
    // assuming we need to get all names in the block, including repetitions
    final List<Name> n = getAll.names((Block) make.ast("{a=1+1;b=2+3;System.out.println(a);c=2;c*=a;}"));
    azzert.that(the.headOf(n), iz("a"));
    azzert.that(n.get(1), iz("b"));
    azzert.that(n.get(2), iz("System"));
    azzert.that(n.get(3), iz("out"));
    azzert.that(n.get(4), iz("println"));
    azzert.that(n.get(5), iz("a"));
    azzert.that(n.get(6), iz("c"));
    azzert.that(n.get(7), iz("c"));
    azzert.that(n.get(8), iz("a"));
  }

  @Test public void testCheckTwoNamesWithMoreThenOneLiteral() {
    final List<Name> names = getAll.names((Block) make.ast("{aba=1+1; ima = 787-9;}"));
    assert "aba".equals(the.headOf(names) + "") && "ima".equals(names.get(1) + "");
  }

  @Test public void testGetEmpty() {
    assert getAll.names((Block) make.ast("{}")).isEmpty();
  }

  @Test public void testGetNull() {
    azzert.isNull(getAll.names(null));
  }

  @Test public void testGetOneNameSize() {
    assert getAll.names((Block) make.ast("{a=1+1;}")).size() == 1;
  }

  @Test public void testGetTwoNamesSize() {
    assert getAll.names((Block) make.ast("{a=1+1;b=2+2;}")).size() == 2;
  }

  @Test public void testReturnType() {
    forceTypeAtCompileTime(getAll.names((Block) make.ast("{}")));
  }
}
