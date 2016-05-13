package il.org.spartan.refactoring.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Comment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.TargetSourceRangeComputer.SourceRange;
import org.eclipse.text.edits.TextEditGroup;

/**
 * Does {@link ASTNode} replacement operation, by duplication of nodes,
 * extraction of comments and rewriting the result using an {@link ASTRewrite}r
 * and a {@link TextEditGroup}
 *
 * @author Ori Roth
 * @since 2016/05/13
 */
public class Surgeon {
  private final CompilationUnit u;
  private final String s;
  private final ASTRewrite r;
  private ASTNode base;
  private ASTNode replacement;
  private List<Comment> comments;
  private final TextEditGroup g;

  protected Surgeon(CompilationUnit u, ASTRewrite r, TextEditGroup g) {
    this.u = u;
    s = Source.get(u);
    this.r = r;
    base = replacement = null;
    comments = new LinkedList<>();
    this.g = g;
  }
  /**
   * @param n node
   * @return comments included in that node
   */
  @SuppressWarnings("unchecked") public <N extends ASTNode> List<Comment> extract(N n) {
    final List<Comment> $ = new ArrayList<>();
    if (s == null || u == null || r == null)
      return $;
    final SourceRange t = r.getExtendedSourceRangeComputer().computeSourceRange(n);
    final int sp = t.getStartPosition();
    final int ep = sp + t.getLength();
    for (final Comment c : (List<Comment>) u.getCommentList()) {
      final int csp = c.getStartPosition();
      if (csp < sp)
        continue;
      else if (csp >= ep)
        break;
      $.add(c);
    }
    return $;
  }
  /**
   * @param nl nodes
   * @return comments included in those nodes
   */
  public <N extends ASTNode> List<Comment> extract(Collection<N> nl) {
    final List<Comment> $ = new ArrayList<>();
    for (final N n : nl)
      $.addAll(extract(n));
    return $;
  }
  /**
   * Make this surgeon forget comments included in collection
   *
   * @param cl comments to be removed from this surgeon
   */
  public void forget(Collection<Comment> cl) {
    comments.removeAll(cl);
  }
  /**
   * @param n node
   * @return duplicated node with all original comments included
   */
  @SuppressWarnings("unchecked") public <N extends ASTNode> N duplicate(N n) {
    if (u == null || r == null || s == null)
      return Funcs.duplicate(n);
    forget(extract(n));
    final int sp = n.getStartPosition();
    return (N) r.createStringPlaceholder(cut(s, sp, sp + n.getLength()), n.getNodeType());
  }
  /**
   * Duplicating statements from one list to another. The duplicated nodes have
   * all original comments included
   *
   * @param src source list
   * @param dst destination list
   */
  public void duplicateInto(List<Statement> src, List<Statement> dst) {
    for (final Statement n : src)
      dst.add(duplicate(n));
  }
  /**
   * Prepare this surgeon for replacing n
   *
   * @param n node
   * @return this surgeon
   */
  public Surgeon operate(ASTNode n) {
    base = n;
    comments = extract(n);
    return this;
  }
  /**
   * Commit surgery: replace base node with replacement, while adding predefined
   * comments to the replacement
   *
   * @param n replacement node
   * @return this surgeon
   */
  @SuppressWarnings("unchecked") public Surgeon replaceWith(ASTNode n) {
    replacement = n;
    if (base == null || replacement == null || r == null) {
      if (r != null)
        r.replace(base, replacement, g);
      return this;
    }
    final SourceRange sr = r.getExtendedSourceRangeComputer().computeSourceRange(base);
    final List<ASTNode> $ = new ArrayList<>();
    if (replacement instanceof Block) {
      final Block bl = (Block) replacement;
      Collections.reverse(comments);
      for (final Comment c : comments)
        bl.statements().add(0,
            r.createStringPlaceholder(s.substring(c.getStartPosition(), c.getStartPosition() + c.getLength()), ASTNode.BLOCK));
      $.add(bl);
    } else {
      if (comments.size() == 1 && shouldMoveComentToEnd(comments.get(0))) {
        final Comment c = comments.get(0);
        String f = "";
        f = !c.isLineComment() || allWhiteSpaces(s.substring(sr.getStartPosition() + sr.getLength()).split("\n")[0]) ? "" : "\n";
        $.add(replacement);
        $.add(r.createStringPlaceholder(" " + s.substring(c.getStartPosition(), c.getStartPosition() + c.getLength()) + f,
            c.getNodeType()));
      } else {
        for (final Comment c : comments)
          $.add(r.createStringPlaceholder(s.substring(c.getStartPosition(), c.getStartPosition() + c.getLength())
              + (!(replacement instanceof Statement) && !c.isLineComment() ? "" : "\n"), c.getNodeType()));
        $.add(replacement);
      }
      if (replacement instanceof Statement
          && !allWhiteSpaces(s.substring(rowStartIndex(sr.getStartPosition()), sr.getStartPosition())))
        $.add(0, r.createStringPlaceholder("", ASTNode.BLOCK));
    }
    r.replace(base, r.createGroupNode($.toArray(new ASTNode[$.size()])), g);
    return this;
  }
  private static String cut(String s, int sp, int ep) {
    final String $ = s.substring(sp, ep);
    if ($.indexOf('\n') < 0)
      return $;
    int l = sp - 1;
    while (l >= 0 && s.charAt(l) == '\t')
      --l;
    return $.replaceAll("\n\t{" + (sp - l - 1) + "}", "\n");
  }
  private boolean shouldMoveComentToEnd(Comment c) {
    if (!c.isLineComment() || replacement == null)
      return false;
    switch (replacement.getNodeType()) {
      case ASTNode.IF_STATEMENT:
        final IfStatement is = (IfStatement) replacement;
        return Is.vacuous(is.getElseStatement()) && !(is.getThenStatement() instanceof Block);
      case ASTNode.SWITCH_STATEMENT:
        return false;
      case ASTNode.FOR_STATEMENT:
        final ForStatement fs = (ForStatement) replacement;
        return !(fs.getBody() instanceof Block);
      default:
        break;
    }
    return true;
  }
  private int rowStartIndex(int i) {
    int j = i;
    while (j > 0 && s.charAt(j - 1) != '\n')
      --j;
    return j;
  }
  private static boolean allWhiteSpaces(String s) {
    for (final char c : s.toCharArray())
      if (!Character.isWhitespace(c))
        return false;
    return true;
  }
}
