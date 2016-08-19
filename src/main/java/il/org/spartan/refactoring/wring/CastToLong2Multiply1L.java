package il.org.spartan.refactoring.wring;

import org.eclipse.jdt.core.dom.*;

public class CastToLong2Multiply1L extends Wring<CastExpression> implements Kind.Canonicalization {
  @Override String description(CastExpression e) {
    return null;
  }
}
