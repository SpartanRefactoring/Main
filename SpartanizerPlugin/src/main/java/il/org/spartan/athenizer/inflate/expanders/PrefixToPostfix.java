package il.org.spartan.athenizer.inflate.expanders;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/**
 * convert
 * <pre>
 * ++i
 * <pre>
 * to
 * <pre>
 * i++
 * <pre>
 * 
 * Test case is {@link Issue1005}
 * 
 * @author YuvalSimon <tt>yuvaltechnion@gmail.com</tt>
 * @since 2016-12-24
 */
public class PrefixToPostfix extends ReplaceCurrentNode<ExpressionStatement> implements TipperCategory.Idiomatic {

  @Override
  @SuppressWarnings("unused") public ASTNode replacement(ExpressionStatement __) {
    return null;
  }

  @Override
  @SuppressWarnings("unused") public String description(ExpressionStatement __) {
    return null;
  }
  
}
