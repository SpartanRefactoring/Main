package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** Removing redundant case branches in switch statement such as
*
* <pre>
* switch(x) { 
* case a: x=1; break;
* case b: x=2; break;
* default: x=1; break;
* </pre>
*
* into
*
* <pre>
* switch(x) {
* case b: x=2; break;
* case a: default: x=1; break;
* </pre>
*
* @author Yuval Simon
* @since 2016-11-26 */
public class RemoveRedundantSwitchBranch extends ReplaceCurrentNode<SwitchStatement> implements TipperCategory.Collapse {

  @Override public ASTNode replacement(@SuppressWarnings("unused") SwitchStatement __) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override public String description(@SuppressWarnings("unused") SwitchStatement __) {
    return "Merging cases which execute identical commands";
  }
  
  @Override protected boolean prerequisite(SwitchStatement s) {
    List<SwitchCase> cases = getCases(s);
    for(int i = 0; i < cases.size(); ++i)
      for(int j = i; j < cases.size(); ++j)   // TODO yuval: need to check that merging cases only if their last statement is break
        if(cases.get(i).subtreeMatch(new ASTMatcher(), cases.get(j)))
          return true;
    return false;
  }
  
  private static List<SwitchCase> getCases(SwitchStatement s) {
    List<SwitchCase> $ = new ArrayList<>();
    s.accept(new ASTVisitor() {
      @Override public boolean visit(SwitchCase node) {
        $.add(node);
        return super.visit(node);
      }
    });
    return $;
  }
}
