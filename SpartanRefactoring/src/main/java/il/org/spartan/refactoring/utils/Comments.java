package il.org.spartan.refactoring.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.Comment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.TargetSourceRangeComputer.SourceRange;
import org.eclipse.text.edits.TextEditGroup;

/**
 * Center of comments analysis and manipulation. Supports DisableSpartanization
 * option. Supports comments mash into nodes. Each Comments object gather: 1)
 * base - an {@ilnk ASTNode} about to be replaced 2) core - the {@link ASTNode}
 * that would come in its place 3) a list of comments meant to be added to the
 * core node. Then, by calling mash method, a final {@link ASTNode} is created
 * from the core and the comments, and base is replaced by it
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
  private final List<Comment> cl = new LinkedList<>();

  /**
   * Create a Comments object, with up to date source code and current
   * {@link CompilationUnit} in use
   */
  public Comments() {
    refresh();
  }
  private static void refresh() {
    s = Source.get();
    cu = Source.getCompilationUnit();
    r = Source.getASTRewrite();
  }
  /**
   * Clear base, core and comments
   */
  public void clear() {
    refresh();
    bs = null;
    cr = null;
    cl.clear();
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
   * @param nln start of node row index
   * @param csr comment start row index
   * @param cer comment end row index
   * @param t comment type
   * @return true iff a dsi in the comment disables the node
   */
  private static boolean matchRowIndexes(int nln, int csr, int cer, int ct, ASTNode n) {
    switch (ct) {
      case ASTNode.LINE_COMMENT:
        return nln == cer;
      case ASTNode.BLOCK_COMMENT:
        return nln == cer || nln - 1 == cer;
      case ASTNode.JAVADOC:
        if (n instanceof BodyDeclaration)
          // need spacial care because Javadoc is in BodyDeclaration's regular
          // expression
          return nln == csr;
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
   * dsi, what would stop it from reaching the the nodes sons (currently
   * implemented in fillRewrite and collect in Trimmer)
   *
   * @param n ASTNode
   * @return true iff the spartanization is disabled for this node and its sons.
   */
  @SuppressWarnings("unchecked") public static <N extends ASTNode> boolean isSpartanizationDisabled(N n) {
    refresh();
    // In a failure case, allow all spartanizations
    if (s == null)
      return false;
    if (cu == null)
      return false;
    final int nln = cu.getLineNumber(n.getStartPosition()) - 1;
    for (final Comment c : (List<Comment>) cu.getCommentList()) {
      final CommentVisitor cv = new CommentVisitor();
      c.accept(cv);
      if (matchRowIndexes(nln, cv.getStartRow(), cv.getEndRow(), c.getNodeType(), n) && cv.getContent().contains(dsi))
        return true;
    }
    return false;
  }
  /**
   * Adds all comments associated with n to cl
   *
   * @param n original node
   */
  @SuppressWarnings("unchecked") public void add(ASTNode n) {
    // In a failure case, allow all spartanizations
    if (s == null || cu == null || r == null)
      return;
    final SourceRange t = r.getExtendedSourceRangeComputer().computeSourceRange(n);
    final int sp = t.getStartPosition();
    final int ep = sp + t.getLength();
    for (final Comment c : (List<Comment>) cu.getCommentList()) {
      final int csp = c.getStartPosition();
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
    bs = n;
  }
  /**
   * Get current base, or null if no base defined
   *
   * @return current base {@link ASTNode}
   */
  public ASTNode base() {
    return bs;
  }
  /**
   * Sets core - node to be mashed with the comments in cl
   *
   * @param cr core node
   */
  public void setCore(ASTNode cr) {
    this.cr = cr;
  }
  /**
   * Get current core, or null if no core defined
   *
   * @return current core {@link ASTNode}
   */
  public ASTNode core() {
    return cr;
  }
  /**
   * Checks for whitespaces only string
   *
   * @param str a String
   * @return true iff s contains only whitespaces
   */
  public static boolean allWhiteSpaces(String str) {
    for (final char c : str.toCharArray())
      if (!Character.isWhitespace(c))
        return false;
    return true;
  }
  /**
   * Assuming c is the only comment associated with cr, check whether it should
   * be at the end of cr or at its start. TODO Ori: complete all cases
   *
   * @param b base node
   * @param cr core node
   * @param c comment
   * @return true iff c should be placed at the end of cr
   */
  public static boolean shouldMoveComentToEnd(ASTNode b, ASTNode cr, Comment c) {
    if (!c.isLineComment())
      return false;
    switch (cr.getNodeType()) {
      case ASTNode.IF_STATEMENT:
        final IfStatement is = (IfStatement) cr;
        return Is.vacuous(is.getElseStatement()) && !(is.getThenStatement() instanceof Block);
      default:
        break;
    }
    return true;
  }
  /**
   * @param i code character index
   * @return index of the start of i's row
   */
  public static int rowStartIndex(int i) {
    int j = i;
    while (j > 0 && s.charAt(j - 1) != '\n')
      --j;
    return j;
  }
  /**
   * Mashing the comments with the previously declared node
   *
   * @param b base node
   * @param c core node
   * @param g current {@link TextEditGroup}
   */
  @SuppressWarnings("unchecked") public void mash(ASTNode b, ASTNode c, TextEditGroup g) {
    // Assumes b is original node with real position
    if (cl.size() == 0 || b.getStartPosition() < 0) {
      r.replace(b, c, g);
      return;
    }
    final SourceRange sr = r.getExtendedSourceRangeComputer().computeSourceRange(b);
    final List<ASTNode> nl = new ArrayList<>();
    if (c instanceof Block) {
      final Block bl = (Block) c;
      Collections.reverse(cl);
      for (final Comment cm : cl)
        bl.statements().add(0,
            r.createStringPlaceholder(s.substring(cm.getStartPosition(), cm.getStartPosition() + cm.getLength()), ASTNode.BLOCK));
      nl.add(bl);
    } else {
      if (cl.size() == 1 && shouldMoveComentToEnd(b, c, cl.get(0))) {
        final Comment cm = cl.get(0);
        String f = "";
        // new line fix after line comment
        f = !cm.isLineComment() || allWhiteSpaces(s.substring(sr.getStartPosition() + sr.getLength()).split("\n")[0]) ? "" : "\n";
        nl.add(c);
        nl.add(r.createStringPlaceholder(" " + s.substring(cm.getStartPosition(), cm.getStartPosition() + cm.getLength()) + f,
            cm.getNodeType()));
      } else {
        for (final Comment cm : cl)
          nl.add(r.createStringPlaceholder(s.substring(cm.getStartPosition(), cm.getStartPosition() + cm.getLength())
              + (c instanceof Expression && !cm.isLineComment() ? "" : "\n"), cm.getNodeType()));
        nl.add(c);
      }
      if (c instanceof Statement && !allWhiteSpaces(s.substring(rowStartIndex(sr.getStartPosition()), sr.getStartPosition())))
        nl.add(0, r.createStringPlaceholder("", ASTNode.BLOCK));
    }
    r.replace(b, r.createGroupNode(nl.toArray(new ASTNode[nl.size()])), g);
  }
  /**
   * Mashing the comments with the previously declared node, using predefined
   * base and core
   *
   * @param g current {@link TextEditGroup}
   */
  public void mash(TextEditGroup g) {
    if (bs == null || cr == null)
      return;
    mash(bs, cr, g);
  }
  /**
   * Get current number of comment saved in this object (and about to be mashed
   * with the core)
   *
   * @return Comments count
   */
  public int commentsCount() {
    return cl.size();
  }
}
