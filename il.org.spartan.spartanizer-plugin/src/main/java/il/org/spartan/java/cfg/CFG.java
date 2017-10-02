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
  enum Edges {
    beginnings, // Nodes which are first to be evaluated inside the node
    ends, // Nodes which are last to be evaluated inside the node
    incoming, // The node evaluated before you
    outgoing; // The node evaluated after you
    interface Of {
      Of add(ASTNode what);
      Of addAll(Collection<? extends ASTNode> what);
      Of addAll(Of what);
      Of clear();
      Nodes get();
      default Of set(final ASTNode what) {
        return clear().add(what);
      }
    }

    Nodes getNodes(ASTNode ¢) {
      return property.get(¢, getClass().getCanonicalName() + "." + this, Nodes::new);
    }
    Nodes nodes(ASTNode ¢) {
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
        @Override public Of addAll(Collection<? extends ASTNode> what) {
          if (what != null && to != null)
            get().addAll(what);
          return this;
        }
        @Override public Of addAll(Of what) {
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
      };
    }
  }

  static void init(final BodyDeclaration ¢) {
    if (¢ != null && beginnings.of(¢).get().isEmpty())
      ¢.accept(new CFGTraversal());
  }
}
