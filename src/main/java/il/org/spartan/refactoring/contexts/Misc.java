package il.org.spartan.refactoring.contexts;

import static il.org.spartan.lazy.Environment.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;

import il.org.spartan.*;
import il.org.spartan.lazy.Environment.*;
import il.org.spartan.refactoring.utils.*;
import il.org.spartan.utils.*;

@SuppressWarnings("javadoc") class Misc {
//@formatter:off
 // Suppliers: may be sorted.
 // Sort alphabetically, organize in columns, indent. VIM: /^\s*[^*\/][^*\/]/,/^\s*\/\//-!sort -u | column -t | sed "s/^/  /"
  public  char[]          array()      {  return  array.get();      }
  public  Document        document()   {  return  document.get();   }
  public  IMarker         marker()     {  return  marker.get();     }
  public  ITextSelection  selection()  {  return  selection.get();  }
  public  Range           range()      {  return  range.get();      }
  public  String          text()       {  return  text.get();       }
  // Bindings: must not be sorted 
  // Organize in columns, indent, but do not sort. VIM: /^\s*[^*\/][^*\/]/,/^\s*\/\//-!column -t| sed "s/^/  /"
  final  Property<IMarker>         marker     =  undefined();
  final  Property<ITextSelection>  selection  =  undefined();
  final  Property<Range>           range      =  function(()   ->  computeRange());
  final  Property<Document>        document   =  bind((String ¢)   ->  newDocument(¢)).to(text);
  final  Property<String>          text       =  bind((Document ¢)   ->  ¢.get()).to(document);
  final  Property<char[]>          array      =  bind((String ¢)   ->  ¢.toCharArray()).to(text);
// @formatter:on
  private Document newDocument(String ¢) {
    return new         Document(¢);
  }
  
  private Range computeRange() {
    return idiomatic.<Range> katching(() -> new Range(intValue(IMarker.CHAR_START), intValue(IMarker.CHAR_END)));
  }
  /** @param n the node which needs to be within the range of
   *          <code><b>m</b></code>
   * @return True if the node is within range */
  boolean applicable(final ASTNode n) {
    return marker() != null ? !isMarked(n) : !hasSelection() || !notSelected(n);
  }
  boolean containedIn(final ASTNode n) {
    return range().includedIn(Funcs.range(n));
  }
  boolean hasSelection() {
    return selection() != null && !selection().isEmpty() && selection().getLength() != 0;
  }
  int intValue(final String propertyName) throws CoreException {
    return ((Integer) marker().getAttribute(propertyName)).intValue();
  }
  /** determine whether a given node is included in the marker
   * @param n JD
   * @return boolean whether a parameter is included in the marker */
  boolean isMarked(final ASTNode n) {
    try {
      return n.getStartPosition() < intValue(IMarker.CHAR_START) || n.getLength() + n.getStartPosition() > intValue(IMarker.CHAR_END);
    } catch (final CoreException e) {
      e.printStackTrace();
      return true;
    }
  }
  boolean isSelected(final int offset) {
    return hasSelection() && offset >= selection().getOffset() && offset < selection().getLength() + selection().getOffset();
  }
  /** Determines if the node is outside of the selected text.
   * @return true if the node is not inside selection. If there is no selection
   *         at all will return false. */
  boolean notSelected(final ASTNode n) {
    return !isSelected(n.getStartPosition());
  }
  final boolean outOfRange(final ASTNode n) {
    return marker() != null ? !containedIn(n) : !hasSelection() || !notSelected(n);
  }
}
