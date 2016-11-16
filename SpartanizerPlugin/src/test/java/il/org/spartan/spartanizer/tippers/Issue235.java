package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;
/**
 * 
 * @author kobybs
 * @author Dor Ma'ayan
 */
@SuppressWarnings("static-method")
@Ignore
public class Issue235 {
  @Test public void test0() {
    trimmingOf("try{} catch(Exception e) { } finally {}")//
    .gives("(try{} catch(Exception e) { }");
  }
  
  @Test public void test1() {
    trimmingOf("try{ return i; } catch(Exception e) { throw e; } finally {}")//
    .gives("(try{ return i; } catch(Exception e) { throw e; }");
  }
  
  @Test public void test2() {
    trimmingOf("try{ return i; } catch(Exception e) { throw e; } finally { return 7;}")//
    .stays();
  }
}
