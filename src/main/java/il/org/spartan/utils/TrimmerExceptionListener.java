package il.org.spartan.utils;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.tipping.*;

/** Exception listener for exceptions in the trimming process
 * @author oran1248 <tt>oran.gilboa1@gmail.com</tt>
 * @since 2017-04-06 */
public interface TrimmerExceptionListener {
  default void accept(Exception x, @SuppressWarnings("unused") Tipper<? extends ASTNode> __, @SuppressWarnings("unused") ASTNode n) {
    x.printStackTrace();
  };
  
  void accept(Exception ¢);
}
