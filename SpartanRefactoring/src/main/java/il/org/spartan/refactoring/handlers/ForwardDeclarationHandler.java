package il.org.spartan.refactoring.handlers;

import il.org.spartan.refactoring.spartanizations.ForwardDeclaration;

/**
 * a handler for {@link ForwardDeclaration}
 *
 * @author Boris van Sosin <code><boris.van.sosin [at] gmail.com></code>
 * @author Yossi Gil <code><yossi.gil [at] gmail.com></code> (major refactoring
 *         2013/07/11)
 * @since 2013/07/01
 */
public class ForwardDeclarationHandler extends BaseHandler {
  /** Instantiates this class */
  public ForwardDeclarationHandler() {
    super(new ForwardDeclaration());
  }
}
