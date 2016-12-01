package il.org.spartan.spartanizer.tippers;


import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;


import org.junit.*;
import org.junit.runners.*;

/** @author Raviv Rachmiel
 * @since 25-11-2016 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue305 {
  
 
  
  @Test public void forTestNoChange() {
    trimmingOf("for (String line = r.readLine(); line != null; line = r.readLine(), $.append(line).append(System.lineSeparator()));").stays();
    assert true;
  }
  

  
  
  @Test public void forTestChangeBasic() {
    trimmingOf("for(int ¢=0;¢<5;++¢); return true;").gives("for(int ¢=0;;++¢) if(¢>=5) return true;").stays();
    assert true;
  }
  
  @Test public void mainTest() {
    trimmingOf("for (String line = r.readLine(); line != null; line = r.readLine(), $.append(line).append(System.lineSeparator())) ; return $ + \"\";").gives("for (String line = r.readLine();; line = r.readLine(), $.append(line).append(System.lineSeparator())) if(line==null) return $ + \"\";").stays();
  }
  

}
