package fluent.ly;

import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;

/** TODO Yossi Gil: document class 
 * 
 * @author Yossi Gil
 * @since 2017-04-28 */
public interface list {

  interface O extends UnaryOperator<List<Expression>> {/**/}

  class PrependOrAppend extends list.Z {
    public PrependOrAppend() {
      this(null);
    }
  
    public PrependOrAppend(final list.Z z) {
      super(z);
    }
  
    public list.ToCallExpected append(final Expression x) {
      os.add(λ -> {
        λ.add(x);
        return λ;
      });
      return new list.ToCallExpected(this);
    }
  
    public list.ToCallExpected prepend(final Expression x) {
      os.add(λ -> {
        λ.add(0, x);
        return λ;
      });
      return new list.ToCallExpected(this);
    }
  }

  class ToCallExpected extends list.Z {
    public ToCallExpected(final list.Z z) {
      super(z);
    }
  
    public PrependOrAppend to() {
      return new PrependOrAppend(this);
    }
  
    public List<Expression> to(final List<Expression> xs) {
      List<Expression> $ = new ArrayList<>(xs);
      for (final O ¢ : os)
        $ = ¢.apply($);
      return $;
    }
  }

  class Z {
    public final List<O> os;
  
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

  static list.ToCallExpected prepend(final Expression ¢) {
    return new list.PrependOrAppend().prepend(¢);
  }

  static list.ToCallExpected append(final Expression ¢) {
    return new list.PrependOrAppend().append(¢);
  }}
