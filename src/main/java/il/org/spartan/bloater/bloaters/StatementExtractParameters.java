package il.org.spartan.bloater.bloaters;

import static il.org.spartan.lisp.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;

import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.namespace.*;
import il.org.spartan.spartanizer.tipping.*;

/** An expander to extract complex parameters from {@link Statement}: {@code
 * f(1 + a[b ? 1 : 2]);
 * } ==> {@code
 * int i = 1 + a[b ? 1 : 2];
 * f(i);
 * } ==> {@code
 * int j = b ? 1 : 2;
 * int i = 1 + a[j];
 * f(i);
 * }
 * @author Ori Roth <tt>ori.rothh@gmail.com</tt>
 * @since 2017-01-10 */
public class StatementExtractParameters<S extends Statement> extends CarefulTipper<S>//
    implements TipperCategory.Bloater {
  private static final long serialVersionUID = 1875447962989759838L;

  @Override public String description(@SuppressWarnings("unused") final Statement __) {
    return "Extract complex parameter from statement";
  }

  @Override public Tip tip(final Statement s) {
    final ASTNode root = s.getRoot();
    if (!s.getAST().hasResolvedBindings() || !(root instanceof CompilationUnit)
        || !(((CompilationUnit) root).getTypeRoot() instanceof ICompilationUnit))
      return null;
    final Expression $ = choose(candidates(s));
    if ($ == null)
      return null;
    final ITypeBinding binding = $.resolveTypeBinding();
    if (binding == null)
      return null;
    final CompilationUnit u = az.compilationUnit(root);
    if (u == null)
      return null;
    // TODO Ori Roth: use library code
    final ImportRewrite ir = ImportRewrite.create(u, true);
    if (ir == null)
      return null;
    ir.setUseContextToFilterImplicitImports(true); // solves many issues
    ir.setFilterImplicitImports(true); // along with this of course
    final Type t = ir.addImport(binding, s.getAST());
    return t == null || $ instanceof Assignment ? // TODO Ori Roth: enable
                                                  // assignments extraction
        null : new Tip(description(s), s, getClass()) {
          @Override public void go(final ASTRewrite r, final TextEditGroup g) {
            fixAddedImports(s, ir, u, g, r.getListRewrite(u, CompilationUnit.IMPORTS_PROPERTY));
            final Type tt = fixWildCardType(t);
            final VariableDeclarationFragment f = s.getAST().newVariableDeclarationFragment();
            final String nn = scope.newName(s, tt);
            f.setName(make.from(s).identifier(nn));
            f.setInitializer(copy.of($));
            final VariableDeclarationStatement v = s.getAST().newVariableDeclarationStatement(f);
            v.setType(tt);
            final Statement ns = copy.of(s);
            s.subtreeMatch(new ASTMatcherSpecific($, λ -> r.replace(λ, make.from(s).identifier(nn), g)), ns);
            if (!(s.getParent() instanceof Block))
              goNonBlockParent(s.getParent(), v, ns, r, g);
            else
              goBlockParent((Block) s.getParent(), v, ns, r, g);
          }

          void goNonBlockParent(final ASTNode p, final VariableDeclarationStatement x, final Statement s, final ASTRewrite r,
              final TextEditGroup g) {
            // TODO Ori Roth: Use subject to block.
            final Block b = p.getAST().newBlock();
            statements(b).add(x);
            statements(b).add(s);
            // TODO Ori Roth: Why twice?
            r.replace(s, b, g);
            r.replace(s, b, g);
          }
        };
  }

  static void goBlockParent(final Block b, final VariableDeclarationStatement s, final Statement ns, final ASTRewrite r, final TextEditGroup g) {
    final ListRewrite lr = r.getListRewrite(b, Block.STATEMENTS_PROPERTY);
    lr.insertBefore(s, s, g);
    lr.insertBefore(ns, s, g);
    lr.remove(s, g);
  }

  // TODO Ori Roth: extend (?)
  @SuppressWarnings("hiding") private static List<Expression> candidates(final Statement s) {
    final Collection<ASTNode> excludedParents = new LinkedList<>();
    // TODO Ori Roth: check *what* needed
    if (s instanceof ExpressionStatement)
      excludedParents.add(s);
    final List<Expression> $ = new LinkedList<>();
    s.accept(new ASTVisitor() {
      @Override @SuppressWarnings("unchecked") public boolean preVisit2(final ASTNode ¢) {
        if (¢ instanceof Expression)
          consider($, (Expression) ¢);
        switch (¢.getNodeType()) {
          case ASTNode.ANONYMOUS_CLASS_DECLARATION:
          case ASTNode.BLOCK:
          case ASTNode.DO_STATEMENT:
          case ASTNode.LAMBDA_EXPRESSION:
          case ASTNode.SUPER_CONSTRUCTOR_INVOCATION:
          case ASTNode.TYPE_DECLARATION_STATEMENT:
          case ASTNode.VARIABLE_DECLARATION_STATEMENT:
          case ASTNode.WHILE_STATEMENT:
            return false;
          case ASTNode.ENHANCED_FOR_STATEMENT:
            final EnhancedForStatement efs = (EnhancedForStatement) ¢;
            consider($, efs.getExpression());
            return false;
          case ASTNode.FOR_STATEMENT:
            final ForStatement fs = (ForStatement) ¢;
            consider($, fs.initializers());
            return false;
          case ASTNode.EXPRESSION_STATEMENT:
            if (((ExpressionStatement) ¢).getExpression() instanceof Assignment)
              excludedParents.add(((ExpressionStatement) ¢).getExpression());
            return true;
          default:
            return true;
        }
      }

      void consider(final Collection<Expression> $, final Expression x) {
        if (!excludedParents.contains(x.getParent()) // TODO Ori Roth: check
                                                     // whether
                                                     // legitimate
            && isComplicated(x))
          $.add(x);
      }

      void consider(final Collection<Expression> $, final Iterable<Expression> xs) {
        xs.forEach(λ -> consider($, λ));
      }
    });
    return $;
  }

  /** Manual addition of imports recorded in the {@link ImportRewrite} object.
   * @param s
   * @param r
   * @param u
   * @param g
   * @param ilr */
  static void fixAddedImports(final Statement s, final ImportRewrite r, final CompilationUnit u, final TextEditGroup g, final ListRewrite ilr) {
    final Collection<String> idns = new LinkedList<>();
    if (r.getAddedImports() != null)
      idns.addAll(as.list(r.getAddedImports()));
    if (r.getAddedStaticImports() != null)
      idns.addAll(as.list(r.getAddedStaticImports()));
    outer: for (final String idn : idns) {
      // TODO Ori Roth: do it better
      for (final ImportDeclaration oid : imports(u)) // Tough
        if (idn.equals(oid.getName().getFullyQualifiedName()))
          continue outer;
      final ImportDeclaration id = s.getAST().newImportDeclaration();
      id.setName(s.getAST().newName(idn));
      ilr.insertLast(id, g);
    }
  }

  /** Required due to bug in eclipse (seams so). Given
   * {@code T extends MyObject}, {@code T[]} turns with binding into
   * {@code ? extends E[]}. The problem is this type is considered as
   * {@link ArrayType} rather than {@link WildcardType}! Thus the manual fix.
   * Real world example: <code>
   * class C<E extends Enum<?>> {
   *   ...
   *   protected E[] events() {
   *     return enumClass.getEnumConstants();
   *   }
   *   ...
   * }
   * </code> turns to <code>
   * class C<E extends Enum<?>> {
   *   ...
   *   protected E[] events() {
   *     ? extends E[] x = enumClass.getEnumConstants();
   *     return x;
   *   }
   *   ...
   * }
   * </code> and the {@link Type} {@code ? extends E[]} is considered as
   * {@link ArrayType} rather than {@link WildcardType}!
   * @param t
   * @return */
  static Type fixWildCardType(final Type $) {
    if ($ == null)
      return null;
    if ($ instanceof WildcardType)
      return copy.of(((WildcardType) $).getBound());
    // here is the manual work...
    final String s = $ + "";
    if (!s.startsWith("? extends "))
      return $;
    $.accept(new ASTVisitor() {
      boolean stop;

      @Override public boolean preVisit2(final ASTNode ¢) {
        return super.preVisit2(¢) && !stop;
      }

      @Override public boolean visit(@SuppressWarnings("hiding") final WildcardType $) {
        if (s.indexOf($ + "") != 0)
          return super.visit($);
        stop = true;
        if (!($.getParent() instanceof Type))
          return false;
        final Type pt = (Type) $.getParent();
        // TODO Ori Roth: more cases?
        if (pt instanceof ArrayType)
          ((ArrayType) pt).setElementType(copy.of($.getBound()));
        else if (pt instanceof ParameterizedType)
          ((ParameterizedType) pt).setType(copy.of($.getBound()));
        return false;
      }
    });
    return $;
  }

  static boolean isComplicated(final Expression ¢) {
    return iz.nodeTypeIn(¢, CLASS_INSTANCE_CREATION, METHOD_INVOCATION, INFIX_EXPRESSION, ASSIGNMENT, CONDITIONAL_EXPRESSION, LAMBDA_EXPRESSION);
  }

  private static Expression choose(final List<Expression> ¢) {
    return onlyOne(¢);
  }

  // TODO Ori Roth: move class to utility file
  protected class ASTMatcherSpecific extends ASTMatcher {
    ASTNode toMatch;
    Consumer<ASTNode> onMatch;

    public ASTMatcherSpecific(final ASTNode toMatch, final Consumer<ASTNode> onMatch) {
      this.toMatch = toMatch;
      this.onMatch = onMatch;
    }

    @Override public boolean match(final SimpleName node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final CharacterLiteral node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final TagElement node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final TextElement node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final MethodInvocation node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final FieldAccess node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final TypeLiteral node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final TypeDeclarationStatement node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final TypeDeclaration node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final TryStatement node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final ThrowStatement node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final ThisExpression node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final SynchronizedStatement node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final SwitchStatement node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final SwitchCase node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final SuperMethodInvocation node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final SuperFieldAccess node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final SuperConstructorInvocation node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final StringLiteral node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final SingleVariableDeclaration node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final SingleMemberAnnotation node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final SimpleType node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final ReturnStatement node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final QualifiedType node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final QualifiedName node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final PrimitiveType node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final PrefixExpression node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final PostfixExpression node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final ParenthesizedExpression node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final ParameterizedType node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final PackageDeclaration node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final NumberLiteral node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final NullLiteral node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final NormalAnnotation node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final Modifier node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final MethodRefParameter node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final MethodRef node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final MethodDeclaration node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final MemberValuePair node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final MemberRef node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final MarkerAnnotation node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final LineComment node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final LabeledStatement node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final Javadoc node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final Initializer node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final InfixExpression node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final ImportDeclaration node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final IfStatement node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final ForStatement node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final FieldDeclaration node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final ExpressionStatement node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final EnumConstantDeclaration node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final EnhancedForStatement node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final EmptyStatement node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final DoStatement node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final ContinueStatement node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final ConstructorInvocation node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final ConditionalExpression node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final CompilationUnit node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final ClassInstanceCreation node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final CatchClause node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final CastExpression node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final BreakStatement node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final BooleanLiteral node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final BlockComment node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final Block node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final Assignment node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final AssertStatement node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final ArrayType node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final ArrayInitializer node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final ArrayCreation node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final ArrayAccess node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final AnonymousClassDeclaration node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final AnnotationTypeMemberDeclaration node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final AnnotationTypeDeclaration node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final TypeParameter node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final VariableDeclarationExpression node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final VariableDeclarationFragment node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final VariableDeclarationStatement node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final WhileStatement node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final WildcardType node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final EnumDeclaration node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final UnionType node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final CreationReference node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final Dimension node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final ExpressionMethodReference node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final InstanceofExpression node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final IntersectionType node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final LambdaExpression node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final NameQualifiedType node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final SuperMethodReference node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(final TypeMethodReference node, final Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }
  }
}
