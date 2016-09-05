package il.org.spartan.refactoring.java;

import org.eclipse.jdt.core.dom.*;

/** Mumbo jumbo of stuff we will do later. Document it, but do not maintaing it
 * for now, this class is intentionally package level, and intenrationally
 * defined locall. For now, cients should not be messing with it */

public class Information {
  /** The containing block, whose death marks the death of this entry; not sure,
   * but I think this entry can be shared by many nodes at the same leve */
  public final ASTNode blockScope;
  /** What do we know about an entry hidden by this one */
  public final Information hiding;
  /** The node at which this entry was created */
  public final ASTNode self;
  /** What do we know about the type of this definition */
  public final PrudentType prudentType;

  // For now, nothing is known, we only maintain lists
  Information() {
    blockScope = self = null;
    prudentType = null;
    hiding = null;
  }


}
