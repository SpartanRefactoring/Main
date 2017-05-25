package il.org.spartan.spartanizer.utils.tdd;

import static fluent.ly.azzert.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.research.util.*;

/** Tests of {@link measure.expressions}
 * @author Koby Ben Shimol
 * @author Yuval Simon
 * @since 16-11-2 */
@SuppressWarnings("static-method")
public class Issue0705 {
  @Test public void a() {
    auxList(getAll.instanceofs(null));
  }
  void auxList(@SuppressWarnings("unused") final List<InstanceofExpression> __) {
    assert true;
  }
  @Test public void b() {
    azzert.that(getAll.instanceofs((MethodDeclaration) make.ast("void func(){ return; }")).size(), is(0));
  }
  @Test public void c() {
    azzert.that(getAll.instanceofs((MethodDeclaration) make.ast("void a () {Integer obj = 5;if(obj instanceof Object){} }")).size(), is(1));
  }
  @Test public void d() {
    azzert.that(
        getAll.instanceofs(
            (MethodDeclaration) make.ast("boolean func (){Integer obj = 5;return (obj instanceof Object) || (obj instanceof Integer); }")).size(),
        is(2));
  }
}
