package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.factory.misc.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.tipping.categories.*;
import il.org.spartan.spartanizer.utils.tdd.*;

/** Anonyze all the relevant parameters in the method declaration </br>
 * For example transform: </br>
 * <code>
 * void f(int a,int b, int c) {}
 * </code> </br>
 * Into: </br>
 * <code>
 * void f(int _1, int _2, int _3) {}
 * </code> </br>
 * @author Dor Ma'ayan
 * @since 2017-05-24 */
public class AnonymizeAllParameters extends EagerTipper<MethodDeclaration> implements Nominal.Result {
  private static final long serialVersionUID = 1;

  @Override public String description(@SuppressWarnings("unused") final MethodDeclaration __) {
    return "Anonyze all the relevant parameters in the method declaration";
  }
  @Override public Tip tip(final MethodDeclaration d) {
    final List<SingleVariableDeclaration> ret = extract.methodArguments(d);
    final List<Name> lst = getAll.names(d.getBody());
    return new Tip(description(d), getClass(), d) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        int i = 1;
        for (final SingleVariableDeclaration arg : ret)
          if (!lst.contains(arg.getName()))
            rename(arg.getName(), d.getAST().newSimpleName("_" + i++), d, r, g);
      }
    };
  }
}
