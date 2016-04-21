package il.org.spartan.refactoring.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

/**
 * A class for analyzing a method.
 *
 * @author Yossi Gil
 * @since 2015-08-29
 */
public class MethodExplorer {
  final MethodDeclaration inner;
  /**
   * Instantiate this class
   *
   * @param inner JD
   */
  public MethodExplorer(final MethodDeclaration inner) {
    this.inner = inner;
  }
  /**
   * Computes the list of all local variable declarations found in a method.
   * {@link MethodDeclaration}.
   * <p>
   * This method correctly ignores declarations made within nested types. It
   * also correctly adds variables declared within plain and extended for loops,
   * just as local variables defined within a try and catch clauses.
   *
   * @return a list of {@link SimpleName} from the given method.
   */
  public List<SimpleName> localVariables() {
    final List<SimpleName> $ = new ArrayList<>();
    inner.accept(new IgnoreNestedMethods() {
      @Override public boolean visit(final CatchClause c) {
        return add(c.getException());
      }
      @Override public boolean visit(final EnhancedForStatement s) {
        return add(s.getParameter());
      }
      @Override public boolean visit(final ForStatement s) {
        return add(s.initializers());
      }
      @Override public boolean visit(final TryStatement s) {
        return add(s.resources());
      }
      @Override public boolean visit(final VariableDeclarationStatement s) {
        addFragments(s.fragments());
        return true;
      }
      private boolean add(final List<VariableDeclarationExpression> initializers) {
        for (final Object o : initializers)
          if (o instanceof VariableDeclarationExpression)
            addFragments(((VariableDeclarationExpression) o).fragments());
        return true;
      }
      private boolean add(final SingleVariableDeclaration d) {
        $.add(d.getName());
        return true;
      }
      private void addFragments(final List<VariableDeclarationFragment> fs) {
        for (final VariableDeclarationFragment f : fs)
          $.add(f.getName());
      }
    });
    return $;
  }
  /**
   * Computes the list of all return statements found in a
   * {@link MethodDeclaration}.
   * <p>
   * This method correctly ignores return statements found within nested types.
   *
   * @return a list of {@link ReturnStatement} from the given method.
   */
  public List<ReturnStatement> returnStatements() {
    final List<ReturnStatement> $ = new ArrayList<>();
    inner.accept(new IgnoreNestedMethods() {
      @Override public boolean visit(final ReturnStatement s) {
        $.add(s);
        return true;
      }
    });
    return $;
  }

  public abstract static class IgnoreNestedMethods extends ASTVisitor {
    @Override public final boolean visit(@SuppressWarnings("unused") final AnnotationTypeDeclaration _) {
      return false;
    }
    @Override public final boolean visit(@SuppressWarnings("unused") final AnonymousClassDeclaration _) {
      return false;
    }
    @Override public final boolean visit(@SuppressWarnings("unused") final EnumDeclaration _) {
      return false;
    }
    @Override public final boolean visit(@SuppressWarnings("unused") final TypeDeclaration _) {
      return false;
    }
  }
}
