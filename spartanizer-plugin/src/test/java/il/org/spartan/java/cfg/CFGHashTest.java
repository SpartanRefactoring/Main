package il.org.spartan.java.cfg;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.junit.Test;
import org.junit.runner.RunWith;

import il.org.spartan.spartanizer.ast.factory.make;

/** TODO Roth: delete this file, an example
 * @author Ori Roth
 * @since 2017-10-03 */
@RunWith(HashRunner.class)
public class CFGHashTest {
  static final String[] cases = { "new X[] {{f()}, g()}" };
  @Focus(wrapper = true) final List<ASTNode> ns = Arrays.stream(cases).map(x -> make.ast(x)).collect(Collectors.toList());
  {
    for (ASTNode n : ns)
      for (ASTNode c : immidiateChildren(n))
        CFG.init(c);
  }

  @Test public void test() {
    for (ASTNode n : ns)
      CFG.init(n);
  }
  /** TODO Roth: document, move to utility */
  private static List<ASTNode> immidiateChildren(ASTNode n) {
    final List<ASTNode> $ = new LinkedList<>();
    n.accept(new ASTVisitor() {
      @Override public boolean preVisit2(ASTNode node) {
        if (node == n)
          return true;
        $.add(node);
        return false;
      }
    });
    return $;
  }
}
