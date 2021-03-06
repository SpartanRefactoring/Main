package il.org.spartan.spartanizer.traversal;

import java.io.ObjectStreamClass;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTNode;
import org.junit.Assert;
import org.junit.Test;

import il.org.spartan.spartanizer.tipping.Tipper;

/** Test that all tippers in {@link Toolboxes} doesn't have a serialVersionUID
 * equals to 1L (default) and that they're all different- Issue #1285
 * @author tomerdragucki
 * @since 2017-04-29 */
@SuppressWarnings("static-method")
public class Issue1285 {
  @Test public void serialVersionUIDNotDefault() {
    for (final Tipper<? extends ASTNode> ¢ : Toolbox.full().getAllTippers())
      Assert.assertNotEquals(1L, ObjectStreamClass.lookup(¢.getClass()).getSerialVersionUID());
  }
  @Test @SuppressWarnings("boxing") public void allSerialUIDsAreDifferent() {
    final HashSet<Long> serialUIDs = new HashSet<>();
    final Set<Class<?>> distinctTippersClasses = new HashSet<>();
    for (final Tipper<? extends ASTNode> ¢ : Toolbox.full().getAllTippers())
      distinctTippersClasses.add(¢.getClass());
    for (final Class<?> ¢ : distinctTippersClasses)
      assert serialUIDs.add(ObjectStreamClass.lookup(¢).getSerialVersionUID());
  }
}
