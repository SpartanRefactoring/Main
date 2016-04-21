package il.org.spartan.refactoring.spartanizations;

import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.refactoring.utils.Collect;
import il.org.spartan.refactoring.utils.Is;
import il.org.spartan.refactoring.utils.Rewrite;

/**
 * @author Artium Nihamkin (original)
 * @author Boris van Sosin <code><boris.van.sosin [at] gmail.com></code> (v2)
 * @author Tomer Zeltzer <code><tomerr90 [at] gmail.com></code> (v3)
 */
// TODO: There <b>must</b> be an option to
// * disable this warning in selected places. Consider this example: <pre>
// public
// * static &lt;T&gt; void swap(final T[] ts, final int i, final int j) { final
// T
// * t = ts[i]; ts[i] = ts[j]; ts[j] = t; } </pre> You should not inline the
// * variable t, and you should not move forward its declaration! Some
// * alternatives for disabling the warning are: First, a dedicated annotation
// of
// * name such as $Resident or
// * @Unmovable, or some other word that is single, and easy to understand.
// <pre>
// * public static &lt;T&gt; void swap(final T[] ts, final int i, final int j) {
// * &#064;Resident final T t = ts[i]; ts[i] = ts[j]; ts[j] = t; } </pre>
// Augment
// * the @SuppressWarning annotation <pre> public static &lt;T&gt; void
// swap(final
// * T[] ts, final int i, final int j) {
// * &#064;SuppressWarning(&quot;unmovable&quot;) final T t = ts[i]; ts[i] =
// * ts[j]; ts[j] = t; } </pre> Require comment <pre> public static &lt;T&gt;
// void
// * swap(final T[] ts, final int i, final int j) { final T t = ts[i]; // Don't
// * move! ts[i] = ts[j]; ts[j] = t; } </pre>
// * @since 2013/01/01
// */
public class InlineSingleUse extends Spartanization {
  /** Instantiates this class */
  public InlineSingleUse() {
    super("Inline Single Use");
  }
  @Override protected final void fillRewrite(final ASTRewrite r, final CompilationUnit u, final IMarker m) {
    u.accept(new ASTVisitor() {
      @Override public boolean visit(final VariableDeclarationFragment f) {
        if (!inRange(m, f) || !(f.getParent() instanceof VariableDeclarationStatement))
          return true;
        final SimpleName n = f.getName();
        final VariableDeclarationStatement parent = (VariableDeclarationStatement) f.getParent();
        final List<SimpleName> uses = Collect.usesOf(n).in(parent.getParent());
        if (uses.size() == 1 && (Is._final(parent) || numOfOccur(Collect.DEFINITIONS, n, parent.getParent()) == 1)) {
          r.replace(uses.get(0), f.getInitializer(), null);
          r.remove(parent.fragments().size() != 1 ? f : parent, null);
        }
        return true;
      }
    });
  }
  @Override protected ASTVisitor collect(final List<Rewrite> $) {
    return new ASTVisitor() {
      @Override public boolean visit(final VariableDeclarationFragment node) {
        if (!(node.getParent() instanceof VariableDeclarationStatement))
          return false;
        final SimpleName n = node.getName();
        final VariableDeclarationStatement parent = (VariableDeclarationStatement) node.getParent();
        if (numOfOccur(Collect.USES_SEMANTIC, n, parent.getParent()) == 1 && (Is._final(parent) || //
            numOfOccur(Collect.DEFINITIONS, n, parent.getParent()) == 1))
          $.add(new Rewrite("", node) {
            @Override public void go(final ASTRewrite r, final TextEditGroup g) {
              // TODO Auto-generated method stub
            }
          });
        return true;
      }
    };
  }
  static int numOfOccur(final Collect typeOfOccur, final SimpleName of, final ASTNode in) {
    return typeOfOccur == null || of == null || in == null ? -1 : typeOfOccur.of(of).in(in).size();
  }
}
