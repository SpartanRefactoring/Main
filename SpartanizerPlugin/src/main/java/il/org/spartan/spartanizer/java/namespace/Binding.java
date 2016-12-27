package il.org.spartan.spartanizer.java.namespace;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;

/** Information about a variable in the environment - its {@link ASTNode}, its
 * parent's, its {@link type}, and which other variables does it hide. This
 * class is intentionally package level, and intentionally defined local. For
 * now, clients should not be messing with it
 * @since 2016 */
public class Binding {
  public static boolean eq(final Object o1, final Object o2) {
    return o1 == o2 || o1 == null && o2 == null || o2.equals(o1);
  }

  /** For Information purposes, {@link type}s are equal if their key is
   * equal. */
  static boolean eq(final type t1, final type t2) {
    return t1 == null ? t2 == null : t2 != null && t1.key().equals(t2.key());
  }

  /** The containing block, whose death marks the death of this entry; not sure,
   * but I think this entry can be shared by many nodes at the same level */
  public final ASTNode blockScope;
  /** What do we know about an entry hidden by this one */
  public final Binding hiding;
  /** The node at which this entry was created */
  public final ASTNode self;
  /** What do we know about the type of this definition */
  public final type type;

  // For now, nothing is known, we only maintain lists
  public Binding() {
    blockScope = self = null;
    type = null;
    hiding = null;
  }

  public Binding(final ASTNode blockScope, final Binding hiding, final ASTNode self, final type type) {
    this.blockScope = blockScope;
    this.hiding = hiding;
    this.self = self;
    this.type = type;
  }

  public Binding(final type type) {
    blockScope = self = null;
    this.type = type;
    hiding = null;
  }

  public boolean equals(final Binding ¢) {
    return eq(blockScope, ¢.blockScope) && eq(hiding, ¢.hiding) && eq(type, ¢.type) && eq(self, ¢.self);
  }

  /** @param ¢
   * @return <code><b>true</b></code> <em>iff</em> the ASTNode (self) and its
   *         parent (blockScope) are the same ones, the type's key() is the
   *         same, and if the Information nodes hidden are equal. */
  // Required for MapEntry equality, which is, in turn, required for Set
  // containment check, which is required for testing.
  @Override public boolean equals(final Object ¢) {
    return ¢ == this || ¢ != null && getClass() == ¢.getClass() && equals((Binding) ¢);
  }

  // Required for MapEntry equality, which is, in turn, required for Set
  // containment check, which is required for testing.
  @Override public int hashCode() {
    return (self == null ? 0 : self.hashCode())
        + 31 * ((hiding == null ? 0 : hiding.hashCode()) + 31 * ((blockScope == null ? 0 : blockScope.hashCode()) + 31));
  }
}