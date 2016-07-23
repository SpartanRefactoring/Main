package il.org.spartan.refactoring.utils;

import il.org.spartan.utils.*;

import java.util.*;

import org.eclipse.jdt.annotation.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jdt.core.dom.rewrite.TargetSourceRangeComputer.SourceRange;
import org.eclipse.text.edits.*;

import static il.org.spartan.refactoring.utils.Funcs.*;

import static il.org.spartan.refactoring.utils.extract.*;

/**
 * Does {@link ASTNode} replacement operation, by duplication of nodes,
 * extraction of comments and rewriting the result using an {@link ASTRewrite}
 * and a {@link TextEditGroup}
 *
 * @author Ori Roth
 * @since 2016/05/13
 */
public class Scalpel {
  /**
   * Checks whether this node is inaccessible, i.e. created using this scalpel's
   * duplication
   *
   * @param n
   *          node
   * @return true iff n is artificial node created with this scalpel
   */
  public static boolean isInaccessible(final ASTNode n) {
    return n != null && Boolean.TRUE.equals(n.properties().get(INACCESSIBLE));
  }
  static String cut(final String s, final Range r) {
    return s.substring(r.from, r.to).replaceAll("\n(\t| )*", "\n");
  }
  static <N extends ASTNode> N mark(final N n) {
    n.setProperty(INACCESSIBLE, Boolean.TRUE);
    return n;
  }

  private static final String INACCESSIBLE = "inaccessible";

  protected Scalpel(final CompilationUnit u, final String s, final ASTRewrite r, final TextEditGroup g) {
    compilationUnit = u;
    string = s;
    astRewrite = r;
    textEditGroup = g;
  }
  /**
   * Adding comments of node to preserved comments
   *
   * @param n
   *          node
   * @return this scalpel
   */
  public Scalpel addComments(final ASTNode n) {
    comments.addAll(0, comments(n));
    return this;
  }
  /**
   * Duplicating statements from a list. The duplicated nodes have all original
   * comments included
   *
   * @param src
   *          source list
   * @return duplicated list
   */
  public <N extends ASTNode> List<N> duplicate(final List<N> src) {
    final List<N> $ = new ArrayList<>();
    duplicateInto(src, $);
    return $;
  }
  /**
   * @param n
   *          node
   * @return duplicated node with all original comments included
   */
  public <@Nullable N extends ASTNode> N duplicate(final N n) {
    if (n == null)
      return null;
    if (compilationUnit == null || astRewrite == null || string == null)
      return Funcs.duplicate(n);
    final Range r = extendedRange(n);
    if (r == null)
      return Funcs.duplicate(n);
    used.addAll(comments(n));
    return mark(cast(n, r));
  }
  /**
   * Duplicating statements from one list to another. The duplicated nodes have
   * all original comments included
   *
   * @param from
   *          source list
   * @param to
   *          destination list
   */
  public <M extends ASTNode, N extends M> void duplicateInto(final List<N> from, final List<M> to) {
    for (final @Nullable M n : from)
      if (n != null)
        to.add(duplicate(n));
  }
  /**
   * As duplicateWith, but for multiple lists of elements
   *
   * @param nss
   *          lists of equal statements
   * @return list of statements with comments from all lists
   */
  @SuppressWarnings("unchecked") public <N extends ASTNode> List<N> duplicateWith(final List<N>... nss) {
    final List<N> $ = new ArrayList<>();
    if (nss.length == 0)
      return $;
    for (int i = 0; i < nss[0].size(); ++i) {
      final List<N> c = new ArrayList<>();
      for (final List<N> nl : nss)
        c.add(nl.get(i));
      $.add((N) duplicateWith(c.toArray(new ASTNode[c.size()])));
    }
    return $;
  }
  /**
   * Duplicates a node with comments from other nodes. Used in order to merge
   * multiple equal nodes into one node, containing comments from both nodes
   *
   * @param ns
   *          JD
   * @return a duplicate node containing all comments
   */
  @SuppressWarnings("unchecked") public <@Nullable N extends ASTNode> N duplicateWith(final N... ns) {
    if (ns == null)
      return null;
    if (compilationUnit == null || astRewrite == null || string == null)
      return Funcs.duplicate(ns[ns.length - 1]);
    final StringBuilder sb = new StringBuilder();
    final N n = ns[ns.length - 1];
    int cc = 0;
    for (final N element : ns) {
      final List<Comment> cl = comments(element);
      used.addAll(cl);
      for (final Comment c : cl) {
        sb.append(cut(string, extendedRange(c))).append("\n");
        ++cc;
      }
    }
    assert n != null;
    @Nullable final String s = n.toString();
    assert s != null;
    return mark(cc != 1 ? (N) astRewrite.createStringPlaceholder(sb.append(s).toString().trim(), n.getNodeType()) : (N) astRewrite.createStringPlaceholder(s.trim() + " " + sb.toString().trim(),
        n.getNodeType()));
  }
  /**
   * Merge comments of statements of equals lists
   *
   * @param l1
   *          list
   * @param l2
   *          list
   * @return l1 with comments from l2
   */
  public List<ASTNode> merge(final List<ASTNode> l1, final List<ASTNode> l2) {
    for (int i = l1.size() - 1; i >= 0; --i)
      l1.addAll(i, comments(l2.get(i)));
    return l1;
  }
  /**
   * Prepare this scalpel to replace b. Remembers comments of both b and nodes
   * in ns
   *
   * @param b
   *          base node
   * @param ns
   *          nodes
   * @return this scalpel
   */
  public Scalpel operate(final ASTNode b, final ASTNode... ns) {
    base = b;
    additionals = ns;
    comments.clear();
    comments.addAll(comments(b));
    for (final ASTNode n : ns)
      comments.addAll(comments(n));
    return this;
  }
  /**
   * Ends operation without replacement, so the node is replaced with its
   * comments
   *
   * @return this
   */
  public Scalpel remove() {
    comments.removeAll(used);
    final Set<Comment> unique = new LinkedHashSet<>(comments);
    comments.clear();
    comments.addAll(unique);
    if (comments.isEmpty())
      astRewrite.remove(base, textEditGroup);
    else {
      final List<ASTNode> $ = new ArrayList<>();
      for (final Comment c : comments)
        $.add(0, astRewrite.createStringPlaceholder(string.substring(c.getStartPosition(), c.getStartPosition() + c.getLength()), ASTNode.BLOCK));
      astRewrite.replace(base, astRewrite.createGroupNode($.toArray(new ASTNode[$.size()])), textEditGroup);
    }
    for (final ASTNode a : additionals)
      astRewrite.remove(a, textEditGroup);
    return this;
  }
  /**
   * Commit surgery: replace base node with replacement, while removing
   * additional nodes and adding predefined comments to the replacement
   *
   * @param n
   *          replacement node
   * @return this scalpel
   */
  public Scalpel replaceWith(final ASTNode n) {
    return replaceWith(n, false);
  }
  /**
   * Commit surgery: replace base node with replacements, while removing
   * additional nodes and adding predefined comments to the replacements
   *
   * @param ns
   *          replacements nodes
   * @return this
   */
  public Scalpel replaceWith(final ASTNode... ns) {
    return replaceWith(astRewrite == null ? null : astRewrite.createGroupNode(ns), true);
  }
  @SuppressWarnings("unchecked") private <N extends ASTNode> @Nullable N cast(final N n, final Range r) {
    return (N) astRewrite.createStringPlaceholder(cut(string, r), n.getNodeType());
  }
  private <N extends ASTNode> List<Comment> comments(final N n) {
    final List<Comment> $ = new ArrayList<>();
    if (string == null || compilationUnit == null || astRewrite == null)
      return $;
    final Range r = extendedRange(n);
    for (final Comment c : expose.comments(compilationUnit)) {
      final int from = extendedRange(c).from;
      if (from < r.from)
        continue;
      if (from >= r.to)
        break;
      $.add(c);
    }
    return $;
  }
  @SuppressWarnings("unchecked") private Scalpel replaceWith(final ASTNode n, final boolean isCollapsed) {
    replacement = n;
    if (astRewrite == null)
      return this;
    if (string == null || base == null || replacement == null || compilationUnit == null) {
      astRewrite.replace(base, replacement, textEditGroup);
      for (final ASTNode a : additionals)
        astRewrite.remove(a, textEditGroup);
      return this;
    }
    comments.removeAll(used);
    final Set<Comment> unique = new LinkedHashSet<>(comments);
    comments.clear();
    comments.addAll(unique);
    final SourceRange sr = astRewrite.getExtendedSourceRangeComputer().computeSourceRange(base);
    final List<ASTNode> $ = new ArrayList<>();
    final Block bl = asBlock(replacement);
    if (bl != null && !isCollapsed) {
      Collections.reverse(comments);
      for (final Comment c : comments)
        bl.statements().add(
            0,
            astRewrite.createStringPlaceholder(string.substring(compilationUnit.getExtendedStartPosition(c), compilationUnit.getExtendedStartPosition(c) + compilationUnit.getExtendedLength(c)),
                ASTNode.BLOCK));
      $.add(bl);
    } else {
      if (comments.size() != 1 || !shouldMoveCommenToEnd(comments.get(0), isCollapsed)) {
        for (final Comment c : comments)
          $.add(astRewrite.createStringPlaceholder(string.substring(compilationUnit.getExtendedStartPosition(c), compilationUnit.getExtendedStartPosition(c) + compilationUnit.getExtendedLength(c))
              + (replacement instanceof Statement || c.isLineComment() ? "\n" : ""), c.getNodeType()));
        $.add(replacement);
      } else {
        final Comment c = comments.get(0);
        final String f = !c.isLineComment() || allWhites(string.substring(sr.getStartPosition() + sr.getLength()).split("\n")[0]) ? "" : "\n";
        $.add(replacement);
        final SourceRange csr = astRewrite.getExtendedSourceRangeComputer().computeSourceRange(c);
        $.add(astRewrite.createStringPlaceholder(" " + string.substring(csr.getStartPosition(), csr.getStartPosition() + csr.getLength()) + f, c.getNodeType()));
      }
      if (replacement instanceof Statement && !allWhites(string.substring(rowStartIndex(sr.getStartPosition()), sr.getStartPosition())))
        $.add(0, astRewrite.createStringPlaceholder("", ASTNode.BLOCK));
    }
    astRewrite.replace(base, astRewrite.createGroupNode($.toArray(new ASTNode[$.size()])), textEditGroup);
    for (final ASTNode a : additionals)
      astRewrite.remove(a, textEditGroup);
    return this;
  }
  private int rowStartIndex(final int i) {
    int $ = i;
    while ($ > 0 && string.charAt($ - 1) != '\n')
      --$;
    return $;
  }
  final Range extendedRange(final ASTNode n) {
    if (compilationUnit == null)
      return null;
    final int from = compilationUnit.getExtendedStartPosition(n);
    final int to = from + compilationUnit.getExtendedLength(n);
    return from < 0 || from > to ? null : new Range(from, from + to);
  }
  boolean shouldMoveCommenToEnd(final Comment c, final boolean isCollapsed) {
    if (!c.isLineComment() || replacement == null || isCollapsed)
      return false;
    switch (replacement.getNodeType()) {
      case ASTNode.IF_STATEMENT:
        final IfStatement is = (IfStatement) replacement;
        return Is.vacuous(elze(is)) && !Is.block(then(is));
      case ASTNode.SWITCH_STATEMENT:
        return false;
      case ASTNode.FOR_STATEMENT:
        final ForStatement fs = (ForStatement) replacement;
        return !(fs.getBody() instanceof Block);
      default:
        return true;
    }
  }

  private ASTNode[] additionals;
  private final ASTRewrite astRewrite;
  private ASTNode base;
  private final List<Comment> comments = new ArrayList<>();
  private final CompilationUnit compilationUnit;
  private ASTNode replacement;
  private final String string;
  private final TextEditGroup textEditGroup;
  private final Set<Comment> used = new HashSet<>();
}
