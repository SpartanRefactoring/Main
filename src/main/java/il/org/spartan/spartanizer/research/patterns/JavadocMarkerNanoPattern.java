package il.org.spartan.spartanizer.research.patterns;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;

/** @author Ori Marcovitch
 * @since 2016 */
public abstract class JavadocMarkerNanoPattern<N extends MethodDeclaration> extends NanoPatternTipper<N> {
  @Override public final boolean canTip(final N ¢) {
    final Javadoc j = ¢.getJavadoc();
    return (j == null || !(j + "").contains(javadoc())) && prerequisites(¢);
  }

  protected abstract boolean prerequisites(N ¢);

  @Override public final Tip tip(final N n) {
    return new Tip(description(n), n, this.getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final Javadoc j = n.getJavadoc();
        final String s = (j + "").replaceFirst("\\*\\/$", ((j + "").matches("(?s).*\n\\s*\\*\\/$") ? "" : "\n ") + "* " + javadoc() + "\n */");
        if (j != null)
          r.replace(j, r.createStringPlaceholder(s, ASTNode.JAVADOC), g);
        else
          r.replace(n,
              r.createGroupNode(new ASTNode[] { r.createStringPlaceholder("/**\n" + javadoc() + "\n*/\n", ASTNode.JAVADOC), r.createCopyTarget(n) }),
              g);
        Logger.logNP(n, javadoc());
      }
    };
  }

  protected abstract String javadoc();
}
