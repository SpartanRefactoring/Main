package il.org.spartan.spartanizer.utils.tdd;

import static org.junit.Assert.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;


/** Tests of methods according to issue 778
 * @author Netanel Felcher
 * @author Moshe Eliasof
 * @since Nov 7, 2016 */


public class Issue778 {
  @SuppressWarnings("static-method") @Test public void test0(){
    getAll2.methods(null);
  }
}