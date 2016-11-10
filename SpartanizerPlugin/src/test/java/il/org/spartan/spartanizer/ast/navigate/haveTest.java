package il.org.spartan.spartanizer.ast.navigate;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

public class haveTest {
  
  @Test public void booleanLiteralTest(){   
    assert have.booleanLiteral((Expression) wizard.ast("t = true; f = false;"));
    assert !have.booleanLiteral((Expression) wizard.ast("x = y + z;"));
  }
  
  @Test public void booleanLiteralIterableTest(){
    List<Expression> xt = new ArrayList<>();
    xt.add((Expression) wizard.ast("t = true; f = false;"));
    Iterable<Expression> iterable = xt;
    assert have.booleanLiteral(xt);
    
    List<Expression> xf = new ArrayList<>();
    xf.add((Expression) wizard.ast("x = y + z;"));
    iterable = xf;
    assert !have.booleanLiteral(xf);
    
  }
  
  @Test public void numericLiteralTest(){   
    assert have.numericLiteral((Expression) wizard.ast("1"));
    assert !have.numericLiteral((Expression) wizard.ast("x"));
  }
}
