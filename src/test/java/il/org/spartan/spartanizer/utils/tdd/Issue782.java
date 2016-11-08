package il.org.spartan.spartanizer.utils.tdd;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

/** Tests of methods according to issue 778
 * @author Yonatan Zarecki
 * @author Roded Zats
 * @author Ziv Izhar
 * @since Nov 8, 2016 */
public class Issue782 {
  @SuppressWarnings("static-method") @Test public void checkCompiles(){
    assert true;
  }
  
  @SuppressWarnings("static-method") @Test public void returnsList(){
    assertNotNull(getAll.privateFields(null));
  }
  
}
