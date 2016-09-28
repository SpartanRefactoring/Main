package il.org.spartan.spartanizer.leonidas;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;

/** package il.org.spartan.spartanizer.leonidas;
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
 * @author Ori Marcovitch
 * @year 2016 */