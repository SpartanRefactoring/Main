package il.org.spartan.spartanizer.tippers;

import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.spartanizer.ast.navigate.*;

/** @author Yossi Gil
 * @since 2017-04-25 */
public abstract class InfixExpressionPattern extends NodePattern<InfixExpression> {
  private static final long serialVersionUID = 1;

  public static ToCallExpected append(final Expression x) {
    return new PrependOrAppend().append(x);
  }

  public static ToCallExpected prepend(final Expression x) {
    return new PrependOrAppend().prepend(x);
  }

  protected int arity;
  protected Expression left;
  protected List<Expression> operands;
  protected Operator operator;
  protected List<Expression> rest;
  protected Expression right;

  public InfixExpressionPattern() {
    property("Left", () -> left = current.getLeftOperand());
    property("Right", () -> right = current.getRightOperand());
    property("Operator", () -> operator = current.getOperator());
    property("Rest", () -> rest = step.extendedOperands(current));
    property("Operands", () -> operands = prepend(left).to().prepend(right).to(rest));
    andAlso("Arity", () -> (arity = operands.size()) == 2);
  }

  interface O extends UnaryOperator<List<Expression>> {/**/}

  static class PrependOrAppend extends Z {
    public PrependOrAppend() {
      this(null);
    }

    public PrependOrAppend(final Z z) {
      super(z);
    }

    ToCallExpected append(final Expression x) {
      os.add(t -> {
        t.add(x);
        return t;
      });
      return new ToCallExpected(this);
    }

    ToCallExpected prepend(final Expression x) {
      os.add(t -> {
        t.add(0, x);
        return t;
      });
      return new ToCallExpected(this);
    }
  }

  static class ToCallExpected extends Z {
    public ToCallExpected(final Z z) {
      super(z);
    }

    public PrependOrAppend to() {
      return new PrependOrAppend(this);
    }

    public List<Expression> to(final List<Expression> xs) {
      List<Expression> $ = new ArrayList<>(xs);
      for (final O o : os)
        $ = o.apply($);
      return $;
    }
  }

  abstract static class Z {
    final List<O> os;

    public Z() {
      os = new ArrayList<>();
    }

    public Z(final List<O> os) {
      this.os = os;
    }

    public Z(final Z z) {
      this(z != null ? z.os : new ArrayList<>());
    }
  }
}
