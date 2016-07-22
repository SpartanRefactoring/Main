package il.org.spartan.refactoring.suggestions;

import org.eclipse.jdt.core.dom.*;

@SuppressWarnings("rawtypes") abstract class DispatchingPostVisitor extends il.org.spartan.DispatchingVisitor {
  @Override public void postVisit(final ASTNode n) {
    postGo(n);
  }
  abstract <N extends ASTNode> void postGo(final N n);
}