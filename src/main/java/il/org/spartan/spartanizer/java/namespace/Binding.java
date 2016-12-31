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

  /** What do we know about an entry hidden by this one */
  public final Binding hiding;
  /** The node at which this entry was created */
  public final ASTNode self;
  /** What do we know about the type of this definition */
  public final type type;
  public final String key;

  public Binding() {
    hiding = null;
    type = null;
    self = null;
    key = null;
    // For now, nothing is known, we only maintain lists
  }

  @Override public String toString() {
    return type + "";
  }

  public Binding(@SuppressWarnings("unused") final ASTNode blockScope, final Binding hiding, final ASTNode self, final type type) {
    this.hiding = hiding;
    this.self = self;
    this.type = type;
    key = null;
  }

  public Binding(final type type) {
    this.type = type;
    self = null;
    hiding = null;
    key = null;
  }

  public Binding(String key, type type) {
    this.key = key;
    this.type = type;
    hiding = null;
    self = null;
  }

  public Binding(String key, ASTNode self) {
    this.key = key;
    this.self = self;
    hiding = null;
    type = null;
  }

  public boolean equals(final Binding ¢) {
    return eq(hiding, ¢.hiding) && eq(type, ¢.type) && eq(self, ¢.self);
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
    return (self == null ? 0 : self.hashCode()) + 31 * ((hiding == null ? 0 : hiding.hashCode()) + 31);
  }
}