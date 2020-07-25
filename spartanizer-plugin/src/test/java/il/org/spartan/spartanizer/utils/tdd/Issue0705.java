package il.org.spartan.spartanizer.utils.tdd;

import static fluent.ly.azzert.is;

import java.util.List;

import org.eclipse.jdt.core.dom.InstanceofExpression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.junit.Test;

import fluent.ly.azzert;
import il.org.spartan.spartanizer.ast.factory.make;
import il.org.spartan.spartanizer.research.util.measure;

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
