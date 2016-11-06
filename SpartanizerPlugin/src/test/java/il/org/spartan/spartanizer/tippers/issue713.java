package il.org.spartan.spartanizer.tippers;

import org.junit.*;
import java.util.*;

import il.org.spartan.spartanizer.utils.tdd.*;

/**
 * see issue #713 for more details
 * @author Inbal Zukerman
 * @author Elia Traore
 * @since 2016-11-06
 */

public class issue713 {
  @SuppressWarnings("static-method") @Test public void doesCompile(){
    assert true;
  }
  
  @SuppressWarnings("static-method") @Test public void returnsList(){
    @SuppressWarnings("unused") List<String> lst = getAll.publicFields(null);
  }
}
