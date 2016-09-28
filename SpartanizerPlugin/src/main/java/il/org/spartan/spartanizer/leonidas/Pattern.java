package il.org.spartan.spartanizer.leonidas;

/** @author Ori Marcovitch
 * @year 2016 */
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

  public boolean matches(ASTNode p, ASTNode n) {
    if (iz.name(p)) {
      String id = ((Name) p).getFullyQualifiedName();
      if (id.startsWith("$")) {
        if (id.startsWith("$X"))
          return (n instanceof Expression) && consistent(n, id);
        if (id.startsWith("$M"))
          return (n instanceof MethodInvocation) && consistent(n, id);
      }
      return (n instanceof Name) && id.equals(((Name) p).getFullyQualifiedName());
    }
    if (n.getNodeType() != p.getNodeType())
      return false;
    if (iz.literal(p))
      return p.toString().equals(n.toString());
    List<? extends ASTNode> nChildren = Recurser.children(n);
    List<? extends ASTNode> pChildren = Recurser.children(p);
    if (nChildren.size() != pChildren.size())
      return false;
    for (int i = 0; i < pChildren.size(); ++i)
      if (!matches(pChildren.get(i), nChildren.get(i)))
        return false;
    return true;
  }

  private boolean consistent(ASTNode n, String id) {
    if (!ids.containsKey(id))
      ids.put(id, new ArrayList<>());
    ids.get(id).add(n);
    for (ASTNode other : ids.get(id))
      if (!n.toString().equals(other.toString()))
        return false;
    return true;
  }
}
