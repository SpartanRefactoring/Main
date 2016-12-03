package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** Removing redundant case branches in switch statement such as
 *
 * <pre>
* switch(x) {
* case a: x=1; break;
* case b: x=2; break;
* case c: y=1; x=2; break;
* default: x=1; break;
 * </pre>
 *
 * into
 *
 * <pre>
* switch(x) {
* case c: y=1;
* case b: x=2; break;
* case a: default: x=1; break;
 * </pre>
 *
 * @author Yuval Simon
 * @since 2016-11-26 */

// will add breaks and decompose cases that do not share all of their code ( case 1: x=6; case 2: y = 2; break; )
public class RemoveRedundantSwitchBranch extends ReplaceCurrentNode<SwitchStatement> implements TipperCategory.Collapse {
  @Override public ASTNode replacement(final SwitchStatement s) {
    @SuppressWarnings("unchecked") List<Statement> l1 = s.statements();
    @SuppressWarnings("unchecked") List<Statement> l2 = s.statements();
    List<CaseIndexes> ll = new ArrayList<>();
    // must somewhere ignore nested switch cases
    getCaseIndexes(l1, ll);
    removeCases(l2, ll);
    getNewCaseIndexes(ll);
    getNewBreakIndexes(l2, ll);
    getLengths(ll);
    CaseIndexesGraph graph = CaseIndexesGraph.buildGraph(l2, ll);
    String r = "switch(" + s.getExpression() + "){";
    graph.traverseAndUpdate(l1, l2, r);
    r += "}";
    return subject.statement(into.s(r)).toOneStatementOrNull();
  }

  
  private static void getCaseIndexes(List<Statement> ss, List<CaseIndexes> ll) {
    for (int indent = 0, ¢ = 0; ¢ < ss.size(); ++¢)
      if (indent == 0 && isStatContains(ss.get(¢), "case ") || isStatContains(ss.get(¢), "default")) {
        CaseIndexes p = new CaseIndexes();
        p.caseIndex = ¢;
        ll.add(p);
      } else if (isStatContains(ss.get(¢), "{"))
        ++indent;
      else if (isStatContains(ss.get(¢), "}"))
        --indent;
  }
  
  private static void removeCases(List<Statement> ss, List<CaseIndexes> ll) {
    for(int ¢ = ll.size() - 1; ¢ >= 0; --¢)
      ss.remove(ll.get(¢).caseIndex);
  }
  
  private static void getNewCaseIndexes(List<CaseIndexes> ll) {
    for(int ¢ = 0; ¢ < ll.size(); ++¢)
      ll.get(¢).caseAfterIndex = ll.get(¢).caseIndex - ¢;
  }
  
  private static void getNewBreakIndexes(List<Statement> ss, List<CaseIndexes> ll) {
    int firstToUpdate = 0;
    for(int ¢ = 0; ¢ < ss.size(); ++¢)
      if (isStatContains(ss.get(¢), "break"))
        for (; firstToUpdate < ll.size() && ¢ >= ll.get(firstToUpdate).caseAfterIndex; ++firstToUpdate)
          ll.get(firstToUpdate).breakAfterIndex = ¢;
    for (; firstToUpdate < ll.size(); ++firstToUpdate)
      ll.get(firstToUpdate).breakAfterIndex = ss.size();
  }
  
  private static void getLengths(List<CaseIndexes> ll) {
    for(CaseIndexes ¢ : ll)
      ¢.length = ¢.breakAfterIndex - ¢.caseAfterIndex;
  }
  

  
  @Override public String description(@SuppressWarnings("unused") final SwitchStatement __) {
    return "Merging cases which execute identical commands";
  }
  
  
  //must check also that the case have some command in it
  @Override protected boolean prerequisite(@SuppressWarnings("unused") final SwitchStatement __) {
    //TODO: yuval finish this
    return false;
  }
  
  private static boolean isStatContains(Statement s, String str) {
    return (s + "").contains(str);
  }
}

class CaseIndexes {
  int caseIndex;
  int caseAfterIndex;
  int breakIndex;
  int breakAfterIndex;
  int length;
  CaseIndexesNode node;
}

class CaseIndexesNode {
  List<CaseIndexes> l;
  List<CaseIndexesNode> edges;
  int inDegree;
  CaseIndexesNode() {
    l = new ArrayList<>();
    edges = new ArrayList<>();
    inDegree = 0;
  }
  
  void addEdge(CaseIndexesNode ¢) {
    edges.add(¢);
    ++¢.inDegree;
  }
}

class CaseIndexesGraph {
  List<CaseIndexesNode> vertices;
  
  CaseIndexesGraph() {
    vertices = new ArrayList<>();
  }
  
  static CaseIndexesGraph buildGraph(List<Statement> ss, List<CaseIndexes> ll) {
    CaseIndexesGraph $ = new CaseIndexesGraph();
    
    for(int i = 0; i < ll.size(); ++i) {
      if(ll.get(i).node != null)
        continue;
      CaseIndexesNode node = new CaseIndexesNode();
      node.l.add(ll.get(i));
      ll.get(i).node = node;
      $.vertices.add(node);
      for(int j = i + 1; j < ll.size(); ++j)
        if (sameCommands(ss, ll.get(i), ll.get(j))) {
          node.l.add(ll.get(j));
          ll.get(j).node = node;
        }
    }
    
    for(int i = 0; i < $.vertices.size(); ++i)
      for (int j = i + 1; j < $.vertices.size(); ++j) {
        CaseIndexes c1 = $.vertices.get(i).l.get(0);
        CaseIndexes c2 = $.vertices.get(j).l.get(0);
        if (containsCommands(ss, c1, c2))
          $.vertices.get(i).addEdge($.vertices.get(j));
        else if (containsCommands(ss, c2, c1))
          $.vertices.get(j).addEdge($.vertices.get(i));
      }
    
    return $;
  }
  
  // will traverse 2 levels of the graph, which are enough
  @SuppressWarnings("all")  //TODO: yuval remove and fix this line
  void traverseAndUpdate(List<Statement> l1, List<Statement> l2, String s) {
    for (CaseIndexesNode n : getSources()) {
      for (CaseIndexes ¢ : n.l)
        s += l1.get(¢.caseIndex);
      n.edges.sort(new Comparator<CaseIndexesNode>() {
        @Override public int compare(CaseIndexesNode o1, CaseIndexesNode o2) {
          return o2.l.get(0).caseAfterIndex + o1.l.get(0).breakAfterIndex - o1.l.get(0).caseAfterIndex - o2.l.get(0).breakAfterIndex;
        }
      });
      int last = 0;
      for (CaseIndexesNode no : n.edges) {
        for (int offset = n.l.get(0).length - no.l.get(0).length; last < offset;) {
          s += l2.get(n.l.get(0).caseAfterIndex + last);
          ++last;
        }
        for (CaseIndexes ¢ : no.l)
          s += l1.get(¢.caseIndex);
      }
      for (; last < n.l.get(0).length; ++last)
        s += l2.get(n.l.get(0).caseAfterIndex + last);
      s += "break;";
    }
  }
  
  private List<CaseIndexesNode> getSources() {
    List<CaseIndexesNode> $ = new ArrayList<>();
    for(CaseIndexesNode v : vertices)
      if(v.inDegree == 0)
        $.add(v);
    return $;
  }
  
//inclusive start and end
 private static boolean sameCommands(List<Statement> ss, CaseIndexes p1, CaseIndexes p2) {
   if(p1.length != p2.length)
     return false;
   for(int ¢ = 0; ¢ < p1.length; ++¢)
     if(!(ss.get(p1.caseAfterIndex + ¢) + "").equals((ss.get(p2.caseAfterIndex + ¢) + "")))
       return false;
   return true;
 }
 
 private static boolean containsCommands(List<Statement> ss, CaseIndexes p1, CaseIndexes p2) {
   if(p2.length >= p1.length)
     return false;
   for (int offset = p1.length - p2.length, ¢ = 0; ¢ < p2.length; ++¢)
    if (!(ss.get(p1.caseAfterIndex + offset + ¢) + "").equals((ss.get(p2.caseAfterIndex + ¢) + "")))
      return false;
   return true;
 }
}
