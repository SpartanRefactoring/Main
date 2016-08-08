package il.org.spartan.refactoring.utils;

import java.util.*;

import org.eclipse.jdt.annotation.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jdt.core.dom.rewrite.TargetSourceRangeComputer.*;
import org.eclipse.text.edits.*;

/**
 * Does {@link ASTNode} replacement operation, by duplication of nodes,
 * extraction of comments and rewriting the result using an
 * {@link ASTRewrite}rewrite and a {@link TextEditGroup}
 *
 * @author Ori Roth
 * @since 2016/05/13
 */
public class Scalpel {
  private final CompilationUnit compilationUnit;
  private final String text;
  private final ASTRewrite rewrite;
  private final TextEditGroup editGroup;
  private ASTNode base = null;
  private ASTNode[] additionals;
  private ASTNode replacement = null;
  private final List<Comment> comments = new ArrayList<>();
  private final Set<Comment> used = new LinkedHashSet<>();

  protected Scalpel(final CompilationUnit compilationUnit, final String text, final ASTRewrite rewrite,
      final TextEditGroup editGroup) {
    this.compilationUnit = compilationUnit;
    this.text = text;
    this.rewrite = rewrite;
    this.editGroup = editGroup;
  }
  /**
   * @param n node
   * @return duplicated node with all original comments included
   */
  @SuppressWarnings({ "unchecked" }) public <@Nullable N extends ASTNode> N duplicate(final N n) {
    if (n == null)
      return null;
    if (compilationUnit == null || rewrite == null || text == null)
      return Funcs.duplicate(n);
    final int sp = compilationUnit.getExtendedStartPosition(n);
    if (sp < 0)
      return Funcs.duplicate(n);
    used.addAll(extract(n));
    return mark((N) rewrite.createStringPlaceholder(cut(text, sp, sp + compilationUnit.getExtendedLength(n)), n.getNodeType()));
  }
  /**
   * Duplicating statements from a list. The duplicated nodes have all original
   * comments included
   *
   * @param src source list
   * @return duplicated list
   */
  public <N extends ASTNode> List<N> duplicate(final List<N> src) {
    final List<N> $ = new ArrayList<>();
    duplicateInto(src, $);
    return $;
  }
  /**
   * Duplicating statements from one list to another. The duplicated nodes have
   * all original comments included
   *
   * @param from source list
   * @param to destination list
   */
  public <M extends ASTNode, N extends M> void duplicateInto(final List<N> from, final List<M> to) {
    for (final @Nullable M n : from)
      if (n != null)
        to.add(duplicate(n));
  }
  /**
   * Duplicates a node with comments from other nodes. Used in order to merge
   * multiple equal nodes into one node, containing comments from both nodes
   *
   * @param ns
   * @return a duplicate node containing all comments
   */
  @SuppressWarnings("unchecked") public <@Nullable N extends ASTNode> N duplicateWith(final N... ns) {
    if (ns == null)
      return null;
    if (compilationUnit == null || rewrite == null || text == null)
      return Funcs.duplicate(ns[ns.length - 1]);
    final StringBuilder sb = new StringBuilder();
    final N n = ns[ns.length - 1];
    int cc = 0;
    for (final N element : ns) {
      final List<Comment> cl = extract(element);
      used.addAll(cl);
      for (final Comment c : cl) {
        sb.append(cut(text, compilationUnit.getExtendedStartPosition(c),
            compilationUnit.getExtendedStartPosition(c) + compilationUnit.getExtendedLength(c))).append("\n");
        ++cc;
      }
    }
    assert n != null;
    @Nullable final String string = n.toString();
    assert string != null;
    return mark(cc != 1 ? (N) rewrite.createStringPlaceholder(sb.append(string).toString().trim(), n.getNodeType())
        : (N) rewrite.createStringPlaceholder(string.trim() + " " + sb.toString().trim(), n.getNodeType()));
  }
  /**
   * As duplicateWith, but for multiple lists of elements
   *
   * @param nls lists of equal statements
   * @return list of statements with comments from all lists
   */
  @SuppressWarnings("unchecked") public <N extends ASTNode> List<N> duplicateWith(final List<N>... nls) {
    final List<N> $ = new ArrayList<>();
    if (nls.length == 0)
      return $;
    for (int i = 0; i < nls[0].size(); ++i) {
      final List<N> c = new ArrayList<>();
      for (final List<N> nl : nls)
        c.add(nl.get(i));
      $.add((N) duplicateWith(c.toArray(new ASTNode[c.size()])));
    }
    return $;
  }
  /**
   * Merge comments of statements of equals lists
   *
   * @param l1 list
   * @param l2 list
   * @return l1 with comments from l2
   */
  public List<ASTNode> merge(final List<ASTNode> l1, final List<ASTNode> l2) {
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
  public Scalpel operate(final ASTNode b, final ASTNode... ns) {
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
  public Scalpel replaceWith(final ASTNode n) {
    return replaceWith(n, false);
  }
  /**
   * Commit surgery: replace base node with replacements, while removing
   * additional nodes and adding predefined comments to the replacements
   *
   * @param ns replacements nodes
   * @return this scalpel
   */
  public Scalpel replaceWith(final ASTNode... ns) {
    return replaceWith(rewrite == null ? null : rewrite.createGroupNode(ns), true);
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
      rewrite.remove(base, editGroup);
    else {
      final List<ASTNode> $ = new ArrayList<>();
      for (final Comment c : comments)
        $.add(0, rewrite.createStringPlaceholder(text.substring(c.getStartPosition(), c.getStartPosition() + c.getLength()),
            ASTNode.BLOCK));
      rewrite.replace(base, rewrite.createGroupNode($.toArray(new ASTNode[$.size()])), editGroup);
    }
    for (final ASTNode a : additionals)
      rewrite.remove(a, editGroup);
    return this;
  }
  /**
   * Adding comments of node to preserved comments
   *
   * @param n node
   * @return this scalpel
   */
  public Scalpel addComments(final ASTNode n) {
    comments.addAll(0, extract(n));
    return this;
  }
  /**
   * Checks whether this node is inaccessible, i.e. created using this
   * scalpel'text duplication
   *
   * @param n node
   * @return true iff n is artificial node created with this scalpel
   */
  public static boolean isInaccessible(final ASTNode n) {
    return n != null && Boolean.TRUE.equals(n.properties().get("inaccessible"));
  }
  @SuppressWarnings("unchecked") private Scalpel replaceWith(final ASTNode n, final boolean isCollapsed) {
    replacement = n;
    if (rewrite == null)
      return this;
    if (text == null || base == null || replacement == null || compilationUnit == null) {
      rewrite.replace(base, replacement, editGroup);
      for (final ASTNode a : additionals)
        rewrite.remove(a, editGroup);
      return this;
    }
    comments.removeAll(used);
    final Set<Comment> unique = new LinkedHashSet<>(comments);
    comments.clear();
    comments.addAll(unique);
    final SourceRange sr = rewrite.getExtendedSourceRangeComputer().computeSourceRange(base);
    final List<ASTNode> $ = new ArrayList<>();
    if (replacement instanceof Block && !isCollapsed) {
      final Block bl = (Block) replacement;
      Collections.reverse(comments);
      for (final Comment c : comments)
        bl.statements().add(0, rewrite.createStringPlaceholder(text.substring(compilationUnit.getExtendedStartPosition(c),
            compilationUnit.getExtendedStartPosition(c) + compilationUnit.getExtendedLength(c)), ASTNode.BLOCK));
      $.add(bl);
    } else {
      if (comments.size() != 1 || !shouldMoveCommentToEnd(comments.get(0), isCollapsed)) {
        for (final Comment c : comments)
          $.add(rewrite.createStringPlaceholder(text.substring(compilationUnit.getExtendedStartPosition(c),
              compilationUnit.getExtendedStartPosition(c) + compilationUnit.getExtendedLength(c))
              + (replacement instanceof Statement || c.isLineComment() ? "\n" : ""), c.getNodeType()));
        $.add(replacement);
      } else {
        final Comment c = comments.get(0);
        final String f = !c.isLineComment() || allWhiteSpaces(text.substring(sr.getStartPosition() + sr.getLength()).split("\n")[0])
            ? "" : "\n";
        $.add(replacement);
        final SourceRange csr = rewrite.getExtendedSourceRangeComputer().computeSourceRange(c);
        $.add(rewrite.createStringPlaceholder(
            " " + text.substring(csr.getStartPosition(), csr.getStartPosition() + csr.getLength()) + f, c.getNodeType()));
      }
      if (replacement instanceof Statement
          && !allWhiteSpaces(text.substring(rowStartIndex(sr.getStartPosition()), sr.getStartPosition())))
        $.add(0, rewrite.createStringPlaceholder("", ASTNode.BLOCK));
    }
    rewrite.replace(base, rewrite.createGroupNode($.toArray(new ASTNode[$.size()])), editGroup);
    for (final ASTNode a : additionals)
      rewrite.remove(a, editGroup);
    return this;
  }
  @SuppressWarnings("unchecked") private <N extends ASTNode> List<Comment> extract(final N n) {
    final List<Comment> $ = new ArrayList<>();
    if (text == null || compilationUnit == null || rewrite == null)
      return $;
    final int sp = compilationUnit.getExtendedStartPosition(n);
    final int ep = sp + compilationUnit.getExtendedLength(n);
    for (final Comment c : (List<Comment>) compilationUnit.getCommentList()) {
      final int csp = compilationUnit.getExtendedStartPosition(c);
      if (csp < sp)
        continue;
      if (csp >= ep)
        break;
      $.add(c);
    }
    return $;
  }
  private static String cut(final String s, final int sp, final int ep) {
    return s.substring(sp, ep).replaceAll("\n(\t| )*", "\n");
  }
  private boolean shouldMoveCommentToEnd(final Comment c, final boolean isCollapsed) {
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
  private int rowStartIndex(final int i) {
    int $ = i;
    while ($ > 0 && text.charAt($ - 1) != '\n')
      --$;
    return $;
  }
  private static boolean allWhiteSpaces(final String s) {
    for (final char c : s.toCharArray())
      if (!Character.isWhitespace(c))
        return false;
    return true;
  }
  @SuppressWarnings("boxing") static <N extends ASTNode> N mark(final N n) {
    n.setProperty("inaccessible", true);
    return n;
  }
}
