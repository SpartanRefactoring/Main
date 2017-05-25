package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

/** Unit tests for the GitHub issue thus numbered
 * @author kobybs
 * @author Dor Ma'ayan
 * @since 16-11-2016 */
@SuppressWarnings("static-method")
public class Issue0235 {
  @Test public void test0() {
    trimmingOf("try{ f(); } catch(Exception e) { } finally {}")//
        .gives("try{ f(); } catch(Exception e) { }")//
        .stays();
  }
  @Test public void test1() {
    trimmingOf("try{ return i; } catch(Exception e) { throw e; } finally {}")//
        .gives("try{ return i; } catch(Exception e) { throw e; }")//
        .gives("try{ return i; } catch(Exception ¢) { throw ¢; }")//
        .stays();
  }
  @Test public void test2() {
    trimmingOf("try{ return i; } catch(Exception e) { throw e; } finally { return 7;}")//
        .gives("try{ return i; } catch(Exception ¢) { throw ¢; } finally { return 7;}")//
        .stays();//
  }
  @Test public void test3() {
    trimmingOf("try{ return i; } finally { return 7;}")//
        .stays();
  }
  @Test public void test4() {
    trimmingOf("try{ return i; } finally { }")//
        .gives("{return i;}")//
        .gives("return i;")//
        .stays();
  }
  @Test public void test5() {
    trimmingOf("try{ try{ return i; } catch(Exception e){} finally { }} catch(Exception e){} finally { } ")//
        .gives("try{ try{ return i; } catch(Exception e){} finally { } } catch(Exception e){} ")
        .gives("try{ try{ return i; } catch(Exception e){} } catch(Exception e){}");
  }
}
