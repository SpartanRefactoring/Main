package il.org.spartan.refactoring.utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Comment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.TargetSourceRangeComputer.SourceRange;
import org.eclipse.text.edits.TextEditGroup;

/**
 * Center of comments analysis and manipulation.
 * Supports @DisableSpartanization option.
 * Supports comments mash into nodes.
 * 
 * @author Ori Roth
 * @since 2016-04-20
 */
public class Comments {
  private static String s;
  private static CompilationUnit cu;
  private static ASTRewrite r;
  private ASTNode bs;
  private ASTNode cr;
  private List<Comment> cl = new LinkedList<>();
  public Comments() {
    refresh();
  }
  private static void refresh() {
    s = Source.get();
    cu = Source.getCompilationUnit();
    r = Source.getASTRewrite();
  }
  /**
   * Disable Spartanization identifier, used by the programmer to indicate a
   * method/class/code line to not be spartanized
   */
  public final static String dsi = "@DisableSpartan";
  /**
   * Match row indexes of comment and a node, to see if a dsi inside the comment
   * would disable the node.
   * 
   * @param nlns
   *          start of node row index
   * @param nlne
   *          end of node row index
   * @param cer
   *          comment row index
   * @param t
   *          comment type
   * @return true iff a dsi in the comment disables the node
   */
  private static boolean matchRowIndexes(int nln, int cer, int t) {
    switch (t) {
    case ASTNode.LINE_COMMENT:
      return nln == cer;
    case ASTNode.BLOCK_COMMENT:
      return nln == cer || nln - 1 == cer;
    case ASTNode.JAVADOC:
      return nln == cer || nln - 1 == cer;
    default:
      return false;
    }
  }
  /**
   * Checks whether or not the spartanization of the current node is disabled.
   * There are two options: first, one should look at the parents of any node to
   * determine its status (implemented as isSpartanizationDisabledInAncestor).
   * Second, the go method of the visitor should return false upon reaching a
   * dsi, what wuld stop it from reaching the the nodes sons (currently
   * implemented in fillRewrite and collect in Trimmer)
   * 
   * @param n
   *          ASTNode
   * @return true iff the spartanization is disabled for this node and its sons.
   */
  @SuppressWarnings("unchecked")
  public static <N extends ASTNode> boolean isSpartanizationDisabled(N n) {
    refresh();
    // In a failure case, allow all spartanizations
    if (s == null)
      return false;
    if (cu == null)
      return false;
    int nln = cu.getLineNumber(n.getStartPosition()) - 1;
    for (Comment c : (List<Comment>) cu.getCommentList()) {
      CommentVisitor cv = new CommentVisitor();
      c.accept(cv);
      int cer = cv.getEndRow();
      if (matchRowIndexes(nln, cer, c.getNodeType()) && cv.getContent().contains(dsi))
        return true;
    }
    return false;
  }
  /**
   * Adds all comments associated with n to cl
   * 
   * @param n
   *          original node
   * @return list of comments
   */
  @SuppressWarnings("unchecked")
  public void add(ASTNode n) {
    // In a failure case, allow all spartanizations
    if (s == null)
      return;
    if (cu == null)
      return;
    if (r == null)
      return;
    SourceRange t = r.getExtendedSourceRangeComputer().computeSourceRange(n);
    int sp = t.getStartPosition();
    int ep = sp + t.getLength();
    for (Comment c : (List<Comment>) cu.getCommentList()) {
      int csp = c.getStartPosition();
      if (csp < sp)
        continue;
      else if (csp >= ep)
        break;
      cl.add(c);
    }
  }
  /**
   * Sets base - original node to be replaced
   * 
   * @param n
   */
  public void setBase(ASTNode n) {
    this.bs = n;
  }
  public ASTNode base() {
    return bs;
  }
  /**
   * Sets core - node to be mashed with the comments in cl
   * 
   * @param n
   */
  public void setCore(ASTNode cr) {
    this.cr = cr;
  }
  public ASTNode core() {
    return cr;
  }
  /**
   * Checks for whitespaces only string
   * 
   * @param s
   *          a String
   * @return true iff s contains only whitespaces
   */
  public boolean allWhiteSpaces(String s) {
    for (char c : s.toCharArray())
      if (!Character.isWhitespace(c))
        return false;
    return true;
  }
  /**
   * Assuming c is the only comment associated with cr, check whether it should
   * be at the end of cr or at its start.
   * TODO complete all cases
   * 
   * @param b
   *          base node
   * @param cr
   *          core node
   * @param c
   *          comment
   * @return true iff c should be placed at the end of cr
   */
  public boolean shouldMoveComentToEnd(ASTNode b, ASTNode cr, Comment c) {
    if (!c.isLineComment())
      return false;
    switch (cr.getNodeType()) {
    case ASTNode.IF_STATEMENT:
      IfStatement is = (IfStatement) cr;
      return Is.vacuous(is.getElseStatement()) && !(is.getThenStatement() instanceof Block);
    }
    return true;
  }
  /**
   * Mashing the comments with the previously declared node
   * 
   * @return placeholder node
   */
  public void mash(ASTNode b, ASTNode cr, TextEditGroup g) {
    // Assumes b is original node with real position
    if (cl.size() == 0 || b.getStartPosition() < 0) {
      r.replace(b, cr, g);
      return;
    }
    List<ASTNode> nl = new ArrayList<>();
    if (cl.size() == 1 && shouldMoveComentToEnd(b, cr, cl.get(0))) {
      Comment c = cl.get(0);
      String f = "";
      SourceRange sr = r.getExtendedSourceRangeComputer().computeSourceRange(b);
      int ep = sr.getStartPosition() + sr.getLength();
      String l = s.substring(ep).split("\n")[0];
      // new line fix after line comment
      f = (!c.isLineComment() || allWhiteSpaces(l)) ? "" : "\n";
      // gather whitespaces before the comment
      StringBuilder w = new StringBuilder("");
      int i = c.getStartPosition() - 1;
      while (Character.isWhitespace(s.charAt(i)))
        w.append(s.charAt(i--));
      nl.add(cr);
      nl.add(r.createStringPlaceholder(
          w.reverse().toString() + s.substring(c.getStartPosition(), c.getStartPosition() + c.getLength()) + f,
          c.getNodeType()));
    } else {
      for (Comment c : cl)
        nl.add(r.createStringPlaceholder(s.substring(c.getStartPosition(), c.getStartPosition() + c.getLength()) + "\n",
            c.getNodeType()));
      nl.add(cr);
    }
    r.replace(b, r.createGroupNode(nl.toArray(new ASTNode[nl.size()])), g);
  }
  public void mash(TextEditGroup g) {
    if (bs == null || cr == null)
      return;
    mash(bs, cr, g);
  }
  public int commentsCount() {
    return cl.size();
  }
}
