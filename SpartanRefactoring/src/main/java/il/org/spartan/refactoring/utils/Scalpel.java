package il.org.spartan.refactoring.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
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
    comments = new ArrayList<>();
    used = new HashSet<>();
  }
  /**
   * @param n node
   * @return duplicated node with all original comments included
   */
  @SuppressWarnings("unchecked") public <N extends ASTNode> N duplicate(N n) {
    if (n == null)
      return null;
    if (u == null || r == null || s == null)
      return Funcs.duplicate(n);
    final int sp = u.getExtendedStartPosition(n);
    if (sp < 0)
      return Funcs.duplicate(n);
    used.addAll(extract(n));
    return (N) r.createStringPlaceholder(cut(s, sp, sp + u.getExtendedLength(n)), n.getNodeType());
  }
  /**
   * Duplicating statements from a list. The duplicated nodes have all original
   * comments included
   *
   * @param src source list
   * @return duplicated list
   */
  public <N extends ASTNode> List<N> duplicate(List<N> src) {
    final List<N> $ = new ArrayList<>();
    duplicateInto(src, $);
    return $;
  }
  /**
   * Duplicating statements from one list to another. The duplicated nodes have
   * all original comments included
   *
   * @param src source list
   * @param dst destination list
   */
  public <M extends ASTNode, N extends M> void duplicateInto(List<N> src, List<M> dst) {
    for (final M n : src)
      dst.add(duplicate(n));
  }
  /**
   * Duplicates a node with comments from another node. Used in order to merge
   * two equal nodes into one node, containing comments from both nodes
   *
   * @param n1 extra node
   * @param n2 base node
   * @return duplication of n2 with comments from both n1 and n2
   */
  @SuppressWarnings("unchecked") public <N extends ASTNode> N duplicateWith(N n1, N n2) {
    if (n2 == null)
      return null;
    if (n1 == null)
      return duplicate(n2);
    if (u == null || r == null || s == null)
      return Funcs.duplicate(n2);
    final int sp2 = u.getExtendedStartPosition(n2);
    if (sp2 < 0)
      return Funcs.duplicate(n2);
    used.addAll(extract(n2));
    final List<Comment> n1cs = extract(n1);
    used.addAll(n1cs);
    final StringBuilder sb = new StringBuilder();
    for (final Comment c : n1cs)
      sb.append(cut(s, c.getStartPosition(), c.getStartPosition() + c.getLength())).append("\n");
    return (N) r.createStringPlaceholder(sb.toString() + cut(s, sp2, sp2 + u.getExtendedLength(n2)), n2.getNodeType());
  }
  /**
   * Duplicate src1 with src2 into dst (see duplicateWith)
   *
   * @param src1 first source list
   * @param src2 second source list
   * @param dst destination list
   */
  public <M extends ASTNode, N extends M> void duplicateWithInto(List<N> src1, List<N> src2, List<M> dst) {
    for (int i = 0; i < src1.size(); ++i)
      dst.add(duplicateWith(src1.get(i), src2.get(i)));
  }
  /**
   * Merge comments of statements of equals lists
   *
   * @param l1 list
   * @param l2 list
   * @return l1 with comments from l2
   */
  public List<ASTNode> merge(List<ASTNode> l1, List<ASTNode> l2) {
    for (int i = l1.size() - 1; i >= 0; --i)
      l1.addAll(i, extract(l2.get(i)));
    return l1;
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
   * Ends operation without replacement, so the node is replaced with its
   * comments
   *
   * @return this scalpel
   */
  public Scalpel remove() {
    comments.removeAll(used);
    final Set<Comment> unique = new LinkedHashSet<>(comments);
    comments.clear();
    comments.addAll(unique);
    if (comments.isEmpty())
      r.remove(base, g);
    else {
      final List<ASTNode> $ = new ArrayList<>();
      for (final Comment c : comments)
        $.add(0, r.createStringPlaceholder(s.substring(c.getStartPosition(), c.getStartPosition() + c.getLength()), ASTNode.BLOCK));
      r.replace(base, r.createGroupNode($.toArray(new ASTNode[$.size()])), g);
    }
    for (final ASTNode a : additionals)
      r.remove(a, g);
    return this;
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
      if (comments.size() != 1 || !shouldMoveComentToEnd(comments.get(0), isCollapsed)) {
        for (final Comment c : comments)
          $.add(r.createStringPlaceholder(s.substring(c.getStartPosition(), c.getStartPosition() + c.getLength())
              + (replacement instanceof Statement || c.isLineComment() ? "\n" : ""), c.getNodeType()));
        $.add(replacement);
      } else {
        final Comment c = comments.get(0);
        final String f = !c.isLineComment() || allWhiteSpaces(s.substring(sr.getStartPosition() + sr.getLength()).split("\n")[0])
            ? "" : "\n";
        $.add(replacement);
        $.add(r.createStringPlaceholder(" " + s.substring(c.getStartPosition(), c.getStartPosition() + c.getLength()) + f,
            c.getNodeType()));
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
      if (csp >= ep)
        break;
      $.add(c);
    }
    return $;
  }
  private static String cut(String s, int sp, int ep) {
    return s.substring(sp, ep).replaceAll("\n(\t| )*", "\n");
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
        return true;
    }
  }
  private int rowStartIndex(int i) {
    int $ = i;
    while ($ > 0 && s.charAt($ - 1) != '\n')
      --$;
    return $;
  }
  @SuppressWarnings("unused") private int rowEndIndex(int i) {
    int $ = i;
    while ($ < s.length() && s.charAt($ + 1) != '\n')
      ++$;
    return $;
  }
  private static boolean allWhiteSpaces(String s) {
    for (final char c : s.toCharArray())
      if (!Character.isWhitespace(c))
        return false;
    return true;
  }
}
