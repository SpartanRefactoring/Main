package il.org.spartan.spartanizer.leonidas;

import java.util.*;
import org.eclipse.jdt.core.dom.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;

public class Pattern<N extends ASTNode> {
  ASTNode pattern;
  ASTNode replacement;
  String description;

  public Pattern(final String pattern, final String replacement, final String description) {
    this.pattern = into.e(pattern);
    this.replacement = into.e(replacement);
    this.description = description;
  }

  public UserDefinedTipper<N> getTipper() {
    return new UserDefinedTipper<N>() {
      @Override protected boolean prerequisite(N ¢) {
        return matches(pattern, ¢);
      }

      @Override public String description(@SuppressWarnings("unused") N __) {
        return description;
      }
      // TODO: No comments in pushed code, except for rare cases. 
      // @Override public Tip tip(final N e) {
      // return new Tip(description(e), e) {
      // @Override public void go(final ASTRewrite r, final TextEditGroup g) {
      // r.replace(e, into.e("If.True(" + az.comparison(step.expression(e)) +
      // ").then(" + step.then(e) + ").elze(" + step.elze(e) + ")"), g);
      // }
      // };
      // }
    };
  }

  @SuppressWarnings("unchecked") public static List<ASTNode> getChildren(ASTNode n) {
    if (n instanceof Block)
      return ((Block) n).statements();
    List<ASTNode> $ = new ArrayList<>();
    List<?> list = n.structuralPropertiesForType();
    // TOOD: Use a utility function in class Recurser
    for (int i = 0; i < list.size(); ++i) {
      Object child = n.getStructuralProperty((StructuralPropertyDescriptor) list.get(i));
      if (child instanceof ASTNode) {
        $.add((ASTNode) child);
        // System.out.println(child.toString() + " " + (child instanceof Name));
        getChildren((ASTNode) child);
      }
    }
    return $;
  }

  public boolean matches(ASTNode n) {
    return matches(pattern, n);
  }

  public static boolean matches(ASTNode p, ASTNode n) {
    return new Matcher().matches(p, n);
  }
}

class Matcher {
  Map<String, ArrayList<ASTNode>> ids = new HashMap<>();

  public Matcher() {
  }

  @SuppressWarnings("unchecked") public static List<ASTNode> getChildren(ASTNode n) {
    if (n instanceof Block)
      return ((Block) n).statements();
    List<ASTNode> $ = new ArrayList<>();
    List<?> list = n.structuralPropertiesForType();
    for (int i = 0; i < list.size(); ++i) {
      Object child = n.getStructuralProperty((StructuralPropertyDescriptor) list.get(i));
      if (child instanceof ASTNode) {
        $.add((ASTNode) child);
        // System.out.println(child.toString() + " " + (child instanceof Name));
        getChildren((ASTNode) child);
      }
    }
    return $;
  }

  public boolean matches(ASTNode p, ASTNode n) {
    if (p instanceof Name) {
      String id = ((Name) p).getFullyQualifiedName();
      if (!ids.containsKey(id))
        ids.put(id, new ArrayList<>());
      for (ASTNode other : ids.get(id))
        if (!n.toString().equals(other.toString()))
          return false;
      ids.get(id).add(n);
      return n instanceof Expression;
    }
    if (n.getNodeType() != p.getNodeType())
      return false;
    if (iz.literal(p))
      return p.toString().equals(n.toString());
    List<ASTNode> nChildren = getChildren(n);
    List<ASTNode> pChildren = getChildren(p);
    if (nChildren.size() != pChildren.size())
      return false;
    for (int i = 0; i < pChildren.size(); ++i)
      if (!matches(pChildren.get(i), nChildren.get(i)))
        return false;
    return true;
  }
}
