package il.org.spartan.spartanizer.assemble;

import org.eclipse.jdt.core.dom.*;

/** A fluent API class that wraps an {@link Expression} with parenthesis, if the
 * location in which this expression occurs requires such wrapping.
 * <p>
 * Typical usage is in the form <code>new Plan(expression).in(host)</code> where
 * <code>location</code> is the parent under which the expression is to be
 * placed.
 * @author Yossi Gil
 * @since 2015-08-20 */
public interface plant {
}