package il.org.spartan.zoomer.inflate.zoomers;

import static il.org.spartan.lisp.*;

import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.namespace.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.utils.*;

/** An expander to extract complex parameters from {@link Statement}: <code>
 * f(1 + a[b ? 1 : 2]);
 * </code> ==> <code>
 * int i = 1 + a[b ? 1 : 2];
 * f(i);
 * </code> ==> <code>
 * int j = b ? 1 : 2;
 * int i = 1 + a[j];
 * f(i);
 * </code>
 * @author Ori Roth <tt>ori.rothh@gmail.com</tt>
 * @since 2017-01-10 */
public class StatementExtractParameters<S extends Statement> extends CarefulTipper<S> implements TipperCategory.Expander {
  protected static List<Integer> COMPLEX_TYPES = Arrays.asList(Integer.valueOf(ASTNode.CLASS_INSTANCE_CREATION),
      Integer.valueOf(ASTNode.METHOD_INVOCATION), Integer.valueOf(ASTNode.INFIX_EXPRESSION), Integer.valueOf(ASTNode.ASSIGNMENT),
      Integer.valueOf(ASTNode.CONDITIONAL_EXPRESSION), Integer.valueOf(ASTNode.LAMBDA_EXPRESSION));

  @Override public String description(@SuppressWarnings("unused") Statement __) {
    return "Extract complex parameter from statement";
  }

  @Override public Tip tip(Statement s) {
    if (!s.getAST().hasResolvedBindings() || !(s.getRoot() instanceof CompilationUnit)
        || !(((CompilationUnit) s.getRoot()).getTypeRoot() instanceof ICompilationUnit))
      return null;
    Expression $ = choose(candidates(s));
    if ($ == null)
      return null;
    ITypeBinding b = $.resolveTypeBinding();
    if (b == null)
      return null;
    Type t;
    try {
      // TODO Roth: use library code
      ImportRewrite ir = ImportRewrite.create((ICompilationUnit) ((CompilationUnit) s.getRoot()).getTypeRoot(), false);
      if (ir == null)
        return null;
      t = ir.addImport(b, s.getAST());
    } catch (JavaModelException ¢) {
      monitor.log(¢);
      return null;
    }
    return t == null || $ instanceof Assignment ? // TODO Roth: enable
                                                  // assignments extraction
        null : new Tip(description(s), s, getClass()) {
          @Override public void go(ASTRewrite r, TextEditGroup g) {
            Type tt = !b.isArray() ? t : s.getAST().newArrayType(t, b.getDimensions());
            VariableDeclarationFragment f = s.getAST().newVariableDeclarationFragment();
            Binding bd = new Binding();
            String nn = scope.newName(s, tt);
            f.setName(s.getAST().newSimpleName(nn));
            f.setInitializer(copy.of($));
            VariableDeclarationStatement v = s.getAST().newVariableDeclarationStatement(f);
            v.setType(tt);
            Statement ns = copy.of(s);
            s.subtreeMatch(new ASTMatcherSpecific($, ne -> r.replace(ne, s.getAST().newSimpleName(nn), g)), ns);
            if (!(s.getParent() instanceof Block))
              goNonBlockParent(s.getParent(), v, ns, r, g);
            else
              goBlockParent((Block) s.getParent(), v, ns, r, g);
          }

          /** [[SuppressWarningsSpartan]] */
          @SuppressWarnings("unchecked") void goNonBlockParent(ASTNode p, VariableDeclarationStatement v, Statement ns, ASTRewrite r,
              TextEditGroup g) {
            Block nb = p.getAST().newBlock();
            nb.statements().add(v);
            nb.statements().add(ns);
            r.replace(s, nb, g);
          }

          /** [[SuppressWarningsSpartan]] */
          void goBlockParent(Block p, VariableDeclarationStatement v, Statement ns, ASTRewrite r, TextEditGroup g) {
            ListRewrite lr = r.getListRewrite(p, Block.STATEMENTS_PROPERTY);
            lr.insertBefore(v, s, g);
            lr.insertBefore(ns, s, g);
            lr.remove(s, g);
          }
        };
  }

  // TODO Roth: extend (?)
  @SuppressWarnings("hiding") private static List<Expression> candidates(Statement s) {
    List<Expression> $ = new LinkedList<>();
    List<ASTNode> excludedParents = new LinkedList<>();
    // TODO Roth: check *what* needed
    if (s instanceof ExpressionStatement)
      excludedParents.add(s);
    s.accept(new ASTVisitor() {
      @Override @SuppressWarnings("unchecked") public boolean preVisit2(ASTNode ¢) {
        if (¢ instanceof Expression)
          consider($, (Expression) ¢);
        switch (¢.getNodeType()) {
          case ASTNode.BLOCK:
          case ASTNode.TYPE_DECLARATION_STATEMENT:
          case ASTNode.ANONYMOUS_CLASS_DECLARATION:
          case ASTNode.WHILE_STATEMENT:
          case ASTNode.DO_STATEMENT:
          case ASTNode.SUPER_CONSTRUCTOR_INVOCATION:
          case ASTNode.VARIABLE_DECLARATION_STATEMENT: // tipper recursion
                                                       // terminator
            return false;
          case ASTNode.FOR_STATEMENT:
            ForStatement fs = (ForStatement) ¢;
            consider($, fs.initializers()); // RISKY (for (int i, int j=i+1,
                                            // ...))
            return false;
          case ASTNode.ENHANCED_FOR_STATEMENT:
            EnhancedForStatement efs = (EnhancedForStatement) ¢;
            consider($, efs.getExpression());
            return false;
          case ASTNode.EXPRESSION_STATEMENT: // TODO Roth: check if legitimate
            if (((ExpressionStatement) ¢).getExpression() instanceof Assignment)
              excludedParents.add(((ExpressionStatement) ¢).getExpression());
            return true;
          default:
            return true;
        }
      }

      void consider(List<Expression> $, Expression x) {
        if (!excludedParents.contains(x.getParent()) // TODO Roth: check whether
                                                     // legitimate
            && isComplicated(x))
          $.add(x);
      }

      void consider(List<Expression> $, List<Expression> xs) {
        for (Expression ¢ : xs)
          consider($, ¢);
      }
    });
    return $;
  }

  // TODO Roth: extend (?)
  static boolean isComplicated(Expression ¢) {
    return COMPLEX_TYPES.contains(Integer.valueOf(¢.getNodeType()));
  }

  private static Expression choose(List<Expression> ¢) {
    return ¢ == null || ¢.isEmpty() ? null : ¢.get(0);
  }

  // TODO Roth: move class to utility file
  protected class ASTMatcherSpecific extends ASTMatcher {
    ASTNode toMatch;
    Consumer<ASTNode> onMatch;

    public ASTMatcherSpecific(ASTNode toMatch, Consumer<ASTNode> onMatch) {
      this.toMatch = toMatch;
      this.onMatch = onMatch;
    }

    @Override public boolean match(SimpleName node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(CharacterLiteral node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(TagElement node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(TextElement node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(MethodInvocation node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(FieldAccess node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(TypeLiteral node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(TypeDeclarationStatement node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(TypeDeclaration node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(TryStatement node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(ThrowStatement node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(ThisExpression node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(SynchronizedStatement node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(SwitchStatement node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(SwitchCase node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(SuperMethodInvocation node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(SuperFieldAccess node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(SuperConstructorInvocation node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(StringLiteral node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(SingleVariableDeclaration node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(SingleMemberAnnotation node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(SimpleType node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(ReturnStatement node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(QualifiedType node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(QualifiedName node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(PrimitiveType node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(PrefixExpression node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(PostfixExpression node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(ParenthesizedExpression node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(ParameterizedType node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(PackageDeclaration node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(NumberLiteral node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(NullLiteral node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(NormalAnnotation node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(Modifier node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(MethodRefParameter node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(MethodRef node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(MethodDeclaration node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(MemberValuePair node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(MemberRef node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(MarkerAnnotation node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(LineComment node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(LabeledStatement node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(Javadoc node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(Initializer node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(InfixExpression node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(ImportDeclaration node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(IfStatement node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(ForStatement node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(FieldDeclaration node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(ExpressionStatement node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(EnumConstantDeclaration node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(EnhancedForStatement node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(EmptyStatement node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(DoStatement node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(ContinueStatement node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(ConstructorInvocation node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(ConditionalExpression node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(CompilationUnit node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(ClassInstanceCreation node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(CatchClause node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(CastExpression node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(BreakStatement node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(BooleanLiteral node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(BlockComment node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(Block node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(Assignment node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(AssertStatement node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(ArrayType node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(ArrayInitializer node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(ArrayCreation node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(ArrayAccess node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(AnonymousClassDeclaration node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(AnnotationTypeMemberDeclaration node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(AnnotationTypeDeclaration node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(TypeParameter node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(VariableDeclarationExpression node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(VariableDeclarationFragment node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(VariableDeclarationStatement node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(WhileStatement node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(WildcardType node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(EnumDeclaration node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(UnionType node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(CreationReference node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(Dimension node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(ExpressionMethodReference node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(InstanceofExpression node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(IntersectionType node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(LambdaExpression node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(NameQualifiedType node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(SuperMethodReference node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }

    @Override public boolean match(TypeMethodReference node, Object other) {
      if (node != toMatch || !(other instanceof ASTNode))
        return super.match(node, other);
      onMatch.accept((ASTNode) other);
      return false;
    }
  }
}
