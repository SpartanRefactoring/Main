package il.org.spartan.spartanizer.ast.navigate;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;

import fluent.ly.*;
import il.org.spartan.utils.*;

/** A utility class for for dealing with {@link Range} in the context of JDT
 * types.
 * @author Yossi Gil
 * @since 2017-04-19 */
public interface Ranger {
  static Range make(final ASTNode ¢) {
    final int ret = ¢.getStartPosition();
    return new Range(ret, ret + ¢.getLength());
  }
  static int to(final ASTNode ¢) {
    return ¢.getLength() + from(¢);
  }
  static int to(final IMarker ret) {
    try {
      return ((Integer) ret.getAttribute(IMarker.CHAR_END)).intValue();
    } catch (CoreException | ClassCastException ¢) {
      note.bug(¢);
      return Integer.MAX_VALUE;
    }
  }
  static Range make(final IMarker ¢) {
    return ¢ == null ? null : new Range(from(¢), to(¢));
  }
  static Range make(final ITextSelection ¢) {
    return ¢ == null || ¢.isEmpty() ? null : new Range(¢.getOffset(), ¢.getOffset() + ¢.getLength());
  }
  static boolean disjoint(final Range r1, final Range r2) {
    return r1 != null && r2 != null && (r1.from >= r2.to || r1.to <= r2.from);
  }
  static boolean disjoint(final ASTNode n, final Range r) {
    return r != null && (from(n) >= r.to || to(n) <= r.from);
  }
  static boolean disjoint(final ASTNode n, final IMarker m) {
    return disjoint(n, make(m));
  }
  static boolean contained(final ASTNode n, final Range r) {
    return r != null && from(n) >= r.from && to(n) <= r.to;
  }
  static boolean contained(final ASTNode n, final IMarker m) {
    return contained(n, make(m));
  }
  static int from(final ASTNode ¢) {
    return ¢.getStartPosition();
  }
  static int from(final IMarker ret) {
    try {
      return ((Integer) ret.getAttribute(IMarker.CHAR_START)).intValue();
    } catch (CoreException | ClassCastException ¢) {
      note.bug(¢);
      return Integer.MIN_VALUE;
    }
  }
  @UnderConstruction("yogi -- 10/04/2017") static boolean overlap(final Range r1, final Range r2) {
    if (r1 == null || r2 == null)
      return false;
    if (r1 == r2)
      return true;
    if (r1.from < r2.from)
      return r1.to > r2.from;
    if (r2.from < r1.from)
      return r2.to > r1.from;
    assert r1.from == r2.from;
    return true;
  }
  static Range start(final ASTNode ¢) {
    return new Range(¢.getStartPosition(), ¢.getStartPosition() + 1);
  }
}
