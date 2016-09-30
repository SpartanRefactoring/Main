package il.org.spartan.spartanizer.tipping;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.engine.*;

/** MultipleReplaceCurrentNode replaces multiple nodes in current statement with
 * multiple nodes (or a single node).
 * @author Ori Roth <code><ori.rothh [at] gmail.com></code>
 * @since 2016-04-25 */
public abstract class MultipleReplaceCurrentNode<N extends ASTNode> extends CarefulTipper<N> {
  @Override public boolean prerequisite(final N ¢) {
    return go(ASTRewrite.create(¢.getAST()), ¢, null, new ArrayList<>(), new ArrayList<>()) != null;
  }

  @Override public final Tip tip(final N n) {
    return new Tip(description(n), n) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final List<ASTNode> input = new ArrayList<>();
        final List<ASTNode> output = new ArrayList<>();
        MultipleReplaceCurrentNode.this.go(r, n, g, input, output);
        if (output.size() == 1)
          for (final ASTNode ¢ : input)
            r.replace(¢, output.get(0), g);
        else if (input.size() == output.size())
          for (int ¢ = 0; ¢ < input.size(); ++¢, r.replace(input.get(¢), output.get(¢), g))
            ;
//        else if (input.size() == 1) {
////          ASTNode[] ds = new ASTNode[output.size()];
////          for (int i=0 ; i<ds.length ; ++i) {
////            ASTNode o = output.get(i);
////            ds[i] = r.createStringPlaceholder(o.toString().trim() + (i != ds.length - 1 && o instanceof BodyDeclaration ? "\n\n" : ""),
////                o.getNodeType());
////          }
////          r.replace(input.get(0), r.createGroupNode(ds), g);
//          ListRewrite l = r.getListRewrite(input.get(0).getParent(), TypeDeclaration.BODY_DECLARATIONS_PROPERTY);
//          l.replace(input.get(0), output.get(0), g);
//          l.insertAfter(output.get(1), output.get(0), g);
//        }
      }
    };
  }

  public abstract ASTRewrite go(ASTRewrite r, N n, TextEditGroup g, List<ASTNode> bss, List<ASTNode> crs);
}