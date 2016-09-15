package il.org.spartan.spartanizer.engine;

import static il.org.spartan.spartanizer.ast.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.*;

/** A class for analyzing a method.
 * @author Yossi Gil
 * @since 2015-08-29 */
public class MethodExplorer {
  @SuppressWarnings("unused") public abstract static class IgnoreNestedMethods extends ASTVisitor {
    @Override public final boolean visit(final AnnotationTypeDeclaration ____) {
      return false;
    }

    @Override public final boolean visit(final AnonymousClassDeclaration ____) {
      return false;
    }

    @Override public final boolean visit(final EnumDeclaration ____) {
      return false;
    }

    @Override public final boolean visit(final TypeDeclaration ____) {
      return false;
    }
  }

  final MethodDeclaration inner;

  /** Instantiate this class
   * @param inner JD */
  public MethodExplorer(final MethodDeclaration inner) {
    this.inner = inner;
  }

  /** Computes the list of all local variable declarations found in a method.
   * {@link MethodDeclaration}.
   * <p>
   * This method correctly ignores declarations made within nested types. It
   * also correctly adds variables declared within plain and extended for loops,
   * just as local variables defined within a try and catch clauses.
   * @return a list of {@link SimpleName} from the given method. */
  public List<SimpleName> localVariables() {
    final List<SimpleName> $ = new ArrayList<>();
    inner.accept(new IgnoreNestedMethods() {
      boolean add(final List<? extends Expression> xs) {
        for (final Expression ¢ : xs)
          addFragments(fragments(az.variableDeclarationExpression(¢)));
        return true;
      }

      boolean add(final SingleVariableDeclaration ¢) {
        $.add(¢.getName());
        return true;
      }

      void addFragments(final List<VariableDeclarationFragment> fs) {
        for (final VariableDeclarationFragment ¢ : fs)
          $.add(¢.getName());
      }

      @Override public boolean visit(final CatchClause ¢) {
        return add(¢.getException());
      }

      @Override public boolean visit(final EnhancedForStatement ¢) {
        return add(¢.getParameter());
      }

      @Override public boolean visit(final ForStatement ¢) {
        return add(initializers(¢));
      }

      @Override public boolean visit(final TryStatement ¢) {
        return add(resources(¢));
      }

      @Override public boolean visit(final VariableDeclarationStatement ¢) {
        addFragments(fragments(¢));
        return true;
      }
    });
    return $;
  }

  /** Computes the list of all return statements found in a
   * {@link MethodDeclaration}.
   * <p>
   * This method correctly ignores return statements found within nested types.
   * @return a list of {@link ReturnStatement} from the given method. */
  public List<ReturnStatement> returnStatements() {
    final List<ReturnStatement> $ = new ArrayList<>();
    inner.accept(new IgnoreNestedMethods() {
      @Override public boolean visit(final ReturnStatement ¢) {
        $.add(¢);
        return true;
      }
    });
    return $;
  }
}
