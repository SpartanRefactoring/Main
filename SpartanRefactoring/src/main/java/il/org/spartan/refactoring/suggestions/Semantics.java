package il.org.spartan.refactoring.suggestions;

import il.org.*;
import il.org.spartan.spreadsheet.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.dom.*;

class Semantics<Extender extends Semantics<?>> extends Context<Semantics<Extender>> {
  @Override final boolean containedIn(final ASTNode n) {
    return range().includedIn(range(n));
  }
  @Override final boolean hasSelection() {
    return selection() != null && !selection().isEmpty() && selection().getLength() != 0;
  }
  @Override int intValue(final String propertyName) throws CoreException {
    return ((Integer) marker().getAttribute(propertyName)).intValue();
  }
  /**
   * determine whether a given node is included in the marker
   *
   * @param n
   * @return boolean whether a parameter is included in the marker
   *
   */
  @Override boolean isMarked(final ASTNode n) {
    try {
      return n.getStartPosition() < intValue(IMarker.CHAR_START)
          || n.getLength() + n.getStartPosition() > intValue(IMarker.CHAR_END);
    } catch (final CoreException e) {
      e.printStackTrace();
      return true;
    }
  }
  @Override boolean isSelected(final int offset) {
    return hasSelection() && offset >= selection().getOffset() && offset < selection().getLength() + selection().getOffset();
  }
  /**
   * Determines if the node is outside of the selected text.
   *
   * @return true if the node is not inside selection. If there is no selection
   *         at all will return false.
   */
  @Override boolean notSelected(final ASTNode n) {
    return !isSelected(n.getStartPosition());
  }
  @Override final boolean outOfRange(final ASTNode n) {
    return marker() != null ? !containedIn(n) : !hasSelection() || !notSelected(n);
  }

  final Edible<Integer> countNodes = new Computed<Integer>(() -> allNodes().size()).ingredients(allNodes);
}