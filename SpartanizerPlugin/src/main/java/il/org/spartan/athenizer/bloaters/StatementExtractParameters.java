package il.org.spartan.athenizer.bloaters;

import static org.eclipse.jdt.core.dom.ASTNode.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;
import java.util.function.*;
import java.util.regex.*;
import java.util.stream.*;

import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.namespace.*;
import il.org.spartan.spartanizer.plugin.*;
import il.org.spartan.spartanizer.tipping.*;

/** An expander to extract complex arguments from {@link Statement}: {@code
 * f(1 + a[b ? 1 : 2]);
 * } ==> {@code
 * int i = 1 + a[b ? 1 : 2];
 * f(i);
 * } ==> {@code
 * int j = b ? 1 : 2;
 * int i = 1 + a[j];
 * f(i);
 * }
 * @author Ori Roth {@code ori.rothh@gmail.com}
 * @since 2017-01-10 */
public class StatementExtractParameters<S extends Statement> extends CarefulTipper<S>//
    implements TipperCategory.Bloater {
  private static final long serialVersionUID = 0x1A06EDFC5CBC0D5EL;
  private static final String KNOWN_TYPES_NAMES = "KNOWN_TYPES_NAMES";

  @Override public String description(@SuppressWarnings("unused") final Statement __) {
    return "Extract complex parameter from statement";
  }
  @Override public Tip tip(final Statement s) {
    final ASTNode root = s.getRoot();
    if (!s.getAST().hasResolvedBindings() || !(root instanceof CompilationUnit)
        || !(((CompilationUnit) root).getTypeRoot() instanceof ICompilationUnit))
      return null;
    final Expression $ = choose(candidates(s));
    if ($ == null || iz.assignment($))
      return null;
    final ITypeBinding binding = $.resolveTypeBinding();
    if (binding == null)
      return null;
    final List<ITypeBinding> allBindings = Bindings.getAllFrom(binding);
    if (captureRisk(allBindings))
      return null;
    final CompilationUnit u = az.compilationUnit(root);
    if (u == null)
      return null;
    final ImportRewrite ir = ImportRewrite.create(u, true);
    if (ir == null)
      return null;
    ir.setUseContextToFilterImplicitImports(true);
    ir.setFilterImplicitImports(true);
    final Type t = ir.addImport(binding, s.getAST());
    if (containsHazardousTypeArgument(t))
      return null;
    final Type tw = fixWildCardType(t);
    if (tw == null)
      return null;
    for (ITypeBinding b : allBindings)
      ir.addImport(b, s.getAST());
    IType[] types = null;
    IType[] topTypes = null;
    try {
      ir.rewriteImports(new NullProgressMonitor());
      final ICompilationUnit iu = ir.getCompilationUnit();
      types = iu.getAllTypes();
      topTypes = iu.getTypes();
    } catch (final CoreException ¢) {
      note.bug(¢);
      return null;
    }
    if (types == null || types.length == 0)
      return null;
    final Collection<String> createdImports = getCreatedImports(ir);
    final Collection<String> sameFile = getSameFile(createdImports, types);
    final Collection<String> samePackage = getSamePackage(createdImports, types[0]);
    final Collection<String> topLevel = getTopLevel(createdImports, topTypes);
    return ambiguousImports(createdImports, u) || //
        privateImportHazard(allBindings, sameFile) || //
        nonPublicImportHazard(allBindings, samePackage) ? null : //
            new Tip(description(s), myClass(), s) {
              @Override public void go(final ASTRewrite r, final TextEditGroup g) {
                fixAddedImports(s, createdImports, sameFile, samePackage, topLevel, g, r.getListRewrite(u, CompilationUnit.IMPORTS_PROPERTY));
                final Type tt = fixWildCardType(tw);
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
              void goNonBlockParent(final ASTNode p, final VariableDeclarationStatement x,
                  final Statement pleaseDoNotChangeThisVariableNameToSItCausesAHidingBug, final ASTRewrite r, final TextEditGroup g) {
                final Block b = p.getAST().newBlock();
                statements(b).add(x);
                statements(b).add(pleaseDoNotChangeThisVariableNameToSItCausesAHidingBug);
                r.replace(s, b, g);
              }
              void goBlockParent(final Block b, final VariableDeclarationStatement pleaseDoNotChangeThisVariableNameToSItCausesAHidingBug,
                  final Statement pleaseDoNotChangeThisVariableNameToSItCausesAHidingBug2, final ASTRewrite r, final TextEditGroup g) {
                final ListRewrite lr = r.getListRewrite(b, Block.STATEMENTS_PROPERTY);
                lr.insertBefore(pleaseDoNotChangeThisVariableNameToSItCausesAHidingBug, s, g);
                lr.insertBefore(pleaseDoNotChangeThisVariableNameToSItCausesAHidingBug2, s, g);
                lr.remove(s, g);
              }
            };
  }
  // TODO Ori Roth: extend (?)
  @SuppressWarnings("hiding") private static List<Expression> candidates(final Statement s) {
    final Collection<ASTNode> excludedParents = an.empty.list();
    // TODO Ori Roth: check *what* needed
    if (s instanceof ExpressionStatement)
      excludedParents.add(s);
    final List<Expression> $ = an.empty.list();
    s.accept(new ASTVisitor(true) {
      @Override @SuppressWarnings("unchecked") public boolean preVisit2(final ASTNode ¢) {
        if (¢ instanceof Expression) {
          final InfixExpression p = az.infixExpression(¢.getParent());
          if (p != null && (iz.conditionalAnd(p) || iz.conditionalOr(p)) && ¢ == p.getRightOperand())
            return false;
          consider($, (Expression) ¢);
        }
        switch (¢.getNodeType()) {
          case ANONYMOUS_CLASS_DECLARATION:
          case BLOCK:
          case DO_STATEMENT:
          case LAMBDA_EXPRESSION:
          case SUPER_CONSTRUCTOR_INVOCATION:
          case TYPE_DECLARATION_STATEMENT:
          case VARIABLE_DECLARATION_STATEMENT:
          case WHILE_STATEMENT:
            return false;
          case ENHANCED_FOR_STATEMENT:
            final EnhancedForStatement efs = (EnhancedForStatement) ¢;
            consider($, efs.getExpression());
            return false;
          case FOR_STATEMENT:
            final ForStatement fs = (ForStatement) ¢;
            consider($, fs.initializers());
            return false;
          case EXPRESSION_STATEMENT:
            if (((ExpressionStatement) ¢).getExpression() instanceof Assignment)
              excludedParents.add(((ExpressionStatement) ¢).getExpression());
            return true;
          default:
            return true;
        }
      }
      void consider(final Collection<Expression> $, final Expression x) {
        // TODO Ori Roth: check whether legitimate
        if (!excludedParents.contains(x.getParent()) && isComplicated(x))
          $.add(x);
      }
      void consider(final Collection<Expression> $, final Iterable<Expression> xs) {
        xs.forEach(λ -> consider($, λ));
      }
    });
    return $;
  }
  /** Manual addition of imports recorded in the {@link ImportRewrite}
   * object. */
  static void fixAddedImports(final Statement s, final Collection<String> createdImports, final Collection<String> sameFile,
      final Collection<String> samePackage, final Collection<String> topLevel, final TextEditGroup g, final ListRewrite ilr) {
    for (final String ci : createdImports) {
      if (sameFile.contains(ci) || samePackage.contains(ci) && topLevel.contains(ci))
        continue;
      final ImportDeclaration id = s.getAST().newImportDeclaration();
      id.setName(s.getAST().newName(ci));
      ilr.insertLast(id, g);
    }
  }
  /** Required due to bug in eclipse (seems so). Given
   * {@code T extends MyObject}, {@code T[]} turns with binding into
   * {@code ? extends E[]}. The problem is this __ is considered as
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
   * @param tipper
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
    $.accept(new ASTVisitor(true) {
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
    return the.firstOf(¢);
  }
  private static boolean captureRisk(final List<ITypeBinding> allBindings) {
    final Set<String> seenCaptures = new HashSet<>();
    for (ITypeBinding b : allBindings)
      for (ITypeBinding bb : b.getTypeArguments()) {
        final Matcher matcher = Pattern.compile("capture#(.*?)-of").matcher(bb + "");
        if (matcher.find())
          for (int i = 1; i <= matcher.groupCount(); ++i) {
            final String capture = matcher.group(i);
            if (seenCaptures.contains(capture))
              return true;
            seenCaptures.add(capture);
          }
      }
    return false;
  }
  private static boolean containsHazardousTypeArgument(final Type ¢) {
    return (¢ + "").contains("<?>");
  }
  private static List<String> getCreatedImports(ImportRewrite ¢) {
    final List<String> $ = an.empty.list();
    if (¢.getCreatedImports() != null)
      $.addAll(as.list(¢.getCreatedImports()));
    if (¢.getCreatedStaticImports() != null)
      $.addAll(as.list(¢.getCreatedStaticImports()));
    return $;
  }
  private static Collection<String> getSameFile(Collection<String> createdImports, IType[] ts) {
    final List<String> $ = Arrays.stream(ts).map(λ -> λ.getFullyQualifiedName()).collect(Collectors.toList());
    return createdImports.stream().filter(λ -> $.contains(λ)).collect(Collectors.toList());
  }
  private static Collection<String> getSamePackage(Collection<String> createdImports, IType rep) {
    final String $ = rep.getPackageFragment().getElementName();
    return createdImports.stream().filter(λ -> λ.startsWith($)).collect(Collectors.toList());
  }
  private static Collection<String> getTopLevel(Collection<String> createdImports, IType[] ts) {
    final List<String> $ = Arrays.stream(ts).map(λ -> λ.getFullyQualifiedName()).collect(Collectors.toList());
    return createdImports.stream().filter(λ -> $.contains(λ)).collect(Collectors.toList());
  }
  private static boolean ambiguousImports(Collection<String> createdImports, CompilationUnit u) {
    final List<String> usedNames = an.empty.list();
    if (property.has(u, KNOWN_TYPES_NAMES))
      usedNames.addAll(property.get(u, KNOWN_TYPES_NAMES));
    else {
      u.accept(new ASTVisitor() {
        @Override public void preVisit(ASTNode n) {
          if (!iz.type(n) || n.getParent() instanceof QualifiedType)
            return;
          final String s = extract.leftName(az.type(n));
          if (s != null)
            usedNames.add(s);
        }
        @Override public boolean visit(SimpleName n) {
          if (iz.bodyDeclaration(n.getParent()))
            usedNames.add(n.getIdentifier());
          return false;
        }
      });
      property.set(u, KNOWN_TYPES_NAMES, usedNames);
    }
    return !Collections.disjoint(createdImports.stream().map(λ -> λ.split("\\.")).map(λ -> λ[λ.length - 1]).collect(Collectors.toList()), usedNames);
  }
  private static boolean privateImportHazard(List<ITypeBinding> allBindings, Collection<String> sameFile) {
    return allBindings.stream().anyMatch(λ -> sameFile.contains(λ.getQualifiedName()) && Modifier.isPrivate(λ.getModifiers()));
  }
  private static boolean nonPublicImportHazard(List<ITypeBinding> allBindings, Collection<String> samePackage) {
    return allBindings.stream().anyMatch(λ -> samePackage.contains(λ.getQualifiedName()) && !Modifier.isPublic(λ.getModifiers()));
  }

  // TODO Ori Roth: move class to utility file
  protected class ASTMatcherSpecific extends ASTMatcher {
    final ASTNode toMatch;
    final Consumer<ASTNode> onMatch;

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
