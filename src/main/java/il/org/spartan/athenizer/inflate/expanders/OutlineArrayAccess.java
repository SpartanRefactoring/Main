package il.org.spartan.athenizer.inflate.expanders;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/**
 * converts
 * 
 * <pre>
 * arr[i++] = y;
 * 
 * arr[++i] = z;
 * <pre>
 * 
 * to
 * 
 * <pre>
 * arr[i] = y;
 * ++i;
 * 
 * ++i;
 * arr[i] = z;
 * <pre>
 * 
 * does not expand if right hand side includes access index operand, such as in arr[i]=i
 * 
 * @author YuvalSimon <tt>yuvaltechnion@gmail.com</tt>
 * @since 2016-12-25
 */
//TODO Yuval: expand also arr[i+=4], arr[(i=i+4)]
public class OutlineArrayAccess extends CarefulTipper<ArrayAccess> implements TipperCategory.InVain {
  @SuppressWarnings("unused")
  @Override public String description(ArrayAccess n) {
    return null;
  } 
}
