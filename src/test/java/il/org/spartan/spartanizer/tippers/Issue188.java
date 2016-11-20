package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;
import org.junit.*;

/** @author Dor Ma'ayan
 * @since 20-11-2016 */
@SuppressWarnings("static-method")
@Ignore
public class Issue188 {
  @Test public void test0() {
    trimmingOf("try{}catch(Exception e){return a;}catch(ExceptionNull e){return a;}finally{return b;}")//
        .gives("try{}catch(Exception|ExceptionNull e){return a;}finally{return b;}").stays();
  }

  @Test public void test1() {
    trimmingOf("try{}catch(Exception e){int a;return a;}catch(ExceptionNull e){int a;return a;}finally{return b;}")//
        .gives("try{}catch(Exception|ExceptionNull e){int a;return a;}finally{return b;}").stays();
  }
}
