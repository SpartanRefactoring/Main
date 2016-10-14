package il.org.spartan.spartanizer.research.patterns;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.engine.*;

/** @author Ori Marcovitch
 * @since 2016 */
public abstract class JavadocMarkerNanoPattern<N extends MethodDeclaration> extends NanoPatternTipper<N> {
  @Override protected final boolean prerequisite(final N ¢) {
    final Javadoc j = ¢.getJavadoc();
    return (j == null || !(j + "").contains(javadoc())) && morePrerequisites(¢);
  }

  protected abstract boolean morePrerequisites(N ¢);

  @Override public final Tip tip(final N n) {
    return new Tip(description(n), n, this.getClass()) {
      @SuppressWarnings("unused") @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final Javadoc j = n.getJavadoc();
        final String s = (j + "").replaceFirst("\\*\\/$", ((j + "").matches("(?s).*\n\\s*\\*\\/$") ? "" : "\n ") + "* " + javadoc() + "\n */");
        if (j != null)
          r.replace(j, r.createStringPlaceholder(s, ASTNode.JAVADOC), g);
        else
          r.replace(n,
              r.createGroupNode(new ASTNode[] { r.createStringPlaceholder("/**\n" + javadoc() + "\n*/\n", ASTNode.JAVADOC), r.createCopyTarget(n) }),
              g);
      }
    };
  }

  protected abstract String javadoc();
}
