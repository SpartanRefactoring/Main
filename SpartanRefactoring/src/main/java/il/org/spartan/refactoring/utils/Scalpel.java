package il.org.spartan.refactoring.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
public class Scalpel {
  private final CompilationUnit u;
  private final String s;
  private final ASTRewrite r;
  private final TextEditGroup g;
  private ASTNode base;
  private ASTNode[] additionals;
  private ASTNode replacement;
  private final List<Comment> comments;
  private final Set<Comment> used;

  protected Scalpel(CompilationUnit u, String s, ASTRewrite r, TextEditGroup g) {
    this.u = u;
    this.s = s;
    this.r = r;
    this.g = g;
    base = replacement = null;
    additionals = null;
    comments = new LinkedList<>();
    used = new HashSet<>();
  }
  /**
   * @param n node
   * @return duplicated node with all original comments included
   */
  @SuppressWarnings("unchecked") public <N extends ASTNode> N duplicate(N n) {
    if (u == null || r == null || s == null)
      return Funcs.duplicate(n);
    final int sp = u.getExtendedStartPosition(n);
    if (sp < 0)
      return Funcs.duplicate(n);
    used.addAll(extract(n));
    return (N) r.createStringPlaceholder(cut(s, sp, sp + u.getExtendedLength(n)), n.getNodeType());
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
   * Prepare this scalpel to replace b. Remembers comments of both b and nodes
   * in ns
   *
   * @param b base node
   * @param ns nodes
   * @return this scalpel
   */
  public Scalpel operate(ASTNode b, ASTNode... ns) {
    base = b;
    additionals = ns;
    comments.clear();
    comments.addAll(extract(b));
    for (final ASTNode n : ns)
      comments.addAll(extract(n));
    return this;
  }
  /**
   * Commit surgery: replace base node with replacement, while removing
   * additional nodes and adding predefined comments to the replacement
   *
   * @param n replacement node
   * @return this scalpel
   */
  public Scalpel replaceWith(ASTNode n) {
    return replaceWith(n, false);
  }
  /**
   * Commit surgery: replace base node with replacements, while removing
   * additional nodes and adding predefined comments to the replacements
   *
   * @param ns replacements nodes
   * @return this scalpel
   */
  public Scalpel replaceWith(ASTNode... ns) {
    return replaceWith(r == null ? null : r.createGroupNode(ns), true);
  }
  /**
   * Adding comments of node to preserved comments
   *
   * @param n node
   * @return this scalpel
   */
  public Scalpel addComments(ASTNode n) {
    comments.addAll(0, extract(n));
    return this;
  }
  @SuppressWarnings("unchecked") private Scalpel replaceWith(ASTNode n, boolean isCollapsed) {
    replacement = n;
    if (r == null)
      return this;
    if (s == null || base == null || replacement == null) {
      r.replace(base, replacement, g);
      for (final ASTNode a : additionals)
        r.remove(a, g);
      return this;
    }
    comments.removeAll(used);
    final Set<Comment> unique = new LinkedHashSet<>(comments);
    comments.clear();
    comments.addAll(unique);
    final SourceRange sr = r.getExtendedSourceRangeComputer().computeSourceRange(base);
    final List<ASTNode> $ = new ArrayList<>();
    if (replacement instanceof Block && !isCollapsed) {
      final Block bl = (Block) replacement;
      Collections.reverse(comments);
      for (final Comment c : comments)
        bl.statements().add(0,
            r.createStringPlaceholder(s.substring(c.getStartPosition(), c.getStartPosition() + c.getLength()), ASTNode.BLOCK));
      $.add(bl);
    } else {
      if (comments.size() == 1 && shouldMoveComentToEnd(comments.get(0), isCollapsed)) {
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
    for (final ASTNode a : additionals)
      r.remove(a, g);
    return this;
  }
  @SuppressWarnings("unchecked") private <N extends ASTNode> List<Comment> extract(N n) {
    final List<Comment> $ = new ArrayList<>();
    if (s == null || u == null || r == null)
      return $;
    final int sp = u.getExtendedStartPosition(n);
    final int ep = sp + u.getExtendedLength(n);
    for (final Comment c : (List<Comment>) u.getCommentList()) {
      final int csp = u.getExtendedStartPosition(c);
      if (csp < sp)
        continue;
      else if (csp >= ep)
        break;
      $.add(c);
    }
    return $;
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
  private boolean shouldMoveComentToEnd(Comment c, boolean isCollapsed) {
    if (!c.isLineComment() || replacement == null || isCollapsed)
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
  @SuppressWarnings("unused") private int rowEndIndex(int i) {
    int j = i;
    while (j < s.length() && s.charAt(j + 1) != '\n')
      ++j;
    return j;
  }
  private static boolean allWhiteSpaces(String s) {
    for (final char c : s.toCharArray())
      if (!Character.isWhitespace(c))
        return false;
    return true;
  }
}
