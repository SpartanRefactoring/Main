package il.org.spartan.java.cfg;

import static il.org.spartan.java.cfg.CFG.Edges.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** The main class of the CFG implementation
 * @author Dor Ma'ayan
 * @author Ori Roth
 * @since 2017-06-14 */
public interface CFG {
  /** Representation of the properties of the CFG per ASTNode: The first and last
   * Nodes to be executed and the nodes evaluated before and after the current
   * node.
   * @author Dor Ma'ayan
   * @author Ori Roth
   * @since 2017-11-15 */
  enum Edges {
    beginnings, // Nodes which are first to be evaluated inside the node
    ends, // Nodes which are last to be evaluated inside the node
    incoming, // The node evaluated before you
    outgoing; // The node evaluated after you
    /**
     *  Interface for working and modifying the CFG.
     * @since 2017-11-15
     */
    interface Of {
      Of add(ASTNode what);
      Of addAll(Collection<? extends ASTNode> what);
      Of addAll(Of what);
      Of clear();
      Nodes get();
      boolean isPresent();
      default Of set(final ASTNode what) {
        return clear().add(what);
      }
    }

    Nodes getNodes(final ASTNode ¢) {
      return property.get(¢, getClass().getCanonicalName() + "." + this, Nodes::new);
    }
    Nodes nodes(final ASTNode ¢) {
      if (!property.has(¢, getClass().getCanonicalName() + "." + this))
        init(az.bodyDeclaration(yieldAncestors.untilClass(BodyDeclaration.class).from(¢)));
      return getNodes(¢);
    }
    Of of(final ASTNode to) {
      return new Of() {
        @Override public Of add(final ASTNode what) {
          if (what != null && to != null)
            get().add(what);
          return this;
        }
        @Override public Of addAll(final Collection<? extends ASTNode> what) {
          if (what != null && to != null)
            get().addAll(what);
          return this;
        }
        @Override public Of addAll(final Of what) {
          if (what != null && to != null)
            get().addAll(what.get().asSet());
          return this;
        }
        @Override public Of clear() {
          if (to != null)
            get().clear();
          return this;
        }
        @Override public Nodes get() {
          return Edges.this.getNodes(to);
        }
        @Override public boolean isPresent() {
          return property.has(to, Edges.class.getCanonicalName() + "." + Edges.this);
        }
      };
    }
  }

  /** Initialize the CFG of a given ASTNode
   * @param ¢ an ASTNode */
  public static void init(final ASTNode ¢) {
    if (¢ != null && !beginnings.of(¢).isPresent())
      ¢.accept(new CFGTraversal());
  }
  /** Initialize the CFG of a given BodyDeclaration
   * @param ¢ a BodyDeclaration */
  public static void init(final BodyDeclaration ¢) {
    if (¢ != null && !beginnings.of(¢).isPresent())
      ¢.accept(new CFGTraversal());
  }
}
