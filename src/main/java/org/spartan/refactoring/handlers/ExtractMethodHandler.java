package org.spartan.refactoring.handlers;

import org.spartan.refactoring.spartanizations.ExtractMethod;

/**
 * a handler for {@link ExtractMethod}
 *
 * @author Ofir Elmakias <code><elmakias [at] outlook.com></code>
 * @since 2015/08/01
 */
public class ExtractMethodHandler extends BaseHandler {
  /** Instantiates this class */
  public ExtractMethodHandler() {
    super(new ExtractMethod());
  }
}
