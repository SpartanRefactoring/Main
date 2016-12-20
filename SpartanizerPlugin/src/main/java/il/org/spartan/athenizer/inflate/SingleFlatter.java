package il.org.spartan.athenizer.inflate;

import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.utils.*;
/*
 * TODO Roth: please write author, since and a line of description
 * As far as I see this is the trimmer for the expanders --RR
 */
public class SingleFlatter {
  CompilationUnit compilationUnit;
  OperationsProvider operationsProvider;
  TextSelection textSelection;

  private SingleFlatter() {}

  public static SingleFlatter in(CompilationUnit ¢) {
    SingleFlatter $ = new SingleFlatter();
    $.compilationUnit = ¢;
    return $;
  }

  public SingleFlatter from(OperationsProvider ¢) {
    operationsProvider = ¢;
    return this;
  }

  public SingleFlatter limit(TextSelection ¢) {
    textSelection = ¢;
    return this;
  }

  @SuppressWarnings({ "unchecked", "rawtypes" }) public boolean go(final Function<List<Operation<?>>, Operation<?>> flatterChooser,
      final ASTRewrite r, final TextEditGroup g) {
    if (compilationUnit == null || operationsProvider == null)
      return false;
    List<Operation<?>> operations = new LinkedList<>();
    disabling.scan(compilationUnit);
    compilationUnit.accept(new DispatchingVisitor() {
      @Override protected <N extends ASTNode> boolean go(final N n) {
        if (!inRange(n) || disabling.on(n))
          return true;
        Tipper<N> w = null;
        try {
          w = operationsProvider.getTipper(n);
        } catch (final Exception ¢) {
          monitor.debug(this, ¢);
        }
        if (w == null)
          return true;
        operations.add(Operation.of(n, w));
        return true;
      }
    });
    if (operations.isEmpty())
      return false;
    final Operation $ = flatterChooser.apply(operations);
    try {
      $.tipper.tip($.node).go(r, g);
    } catch (final Exception ¢) {
      monitor.debug(this, ¢);
    }
    return true;
  }

  boolean inRange(ASTNode ¢) {
    final int $ = ¢.getStartPosition();
    return textSelection == null || $ >= textSelection.getOffset() && $ < textSelection.getLength() + textSelection.getOffset();
  }
  
  /* @param startChar1 - starting char of first interval
  *
  * @param lenth1 - length of first interval
  *
  * @param startChar2 - starting char of second interval
  *
  * @param length2 - length of second interval SPARTANIZED - should use
  * Athenizer one day to understand it */
 static boolean intervalsIntersect(final int startChar1, final int length1, final int startChar2, final int length2) {
   return length1 != 0 && length2 != 0 && (startChar1 < startChar2 ? length1 + startChar1 > startChar2
       : startChar1 != startChar2 ? length2 + startChar2 > startChar1 : length1 > 0 && length2 > 0);
 }

  protected static class Operation<N extends ASTNode> {
    public final N node;
    public final Tipper<N> tipper;

    private Operation(N n, Tipper<N> t) {
      node = n;
      tipper = t;
    }

    /** [[SuppressWarningsSpartan]] */
    public static <N extends ASTNode> Operation<N> of(N node, Tipper<N> tipper) {
      return new Operation<>(node, tipper);
    }
  }
}
