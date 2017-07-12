package il.org.spartan.spartanizer.cmdline.good;

import il.org.spartan.external.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.plugin.*;

/** This is a command line program, which can be thought of as a spartan
 * compiler. For each {@code .java} file it find, it encounters, it creates a
 * corresponding {@code .javas} file, which is the spartanized version of the
 * original. The {@code .javas} will be in the same folder as the {@code .java}
 * file, and would be overwritten each time this program is run.
 * @author Matteo Orru'
 * @since 2017-06-25 */
public class Spartanize extends ASTInFilesVisitor {
  
  public Spartanize(final String[] args) {
    super(args);
  }
  
  public final TextualTraversals traversals = new TextualTraversals();
  
  public static void main(final String[] args) throws SecurityException, IllegalArgumentException {
    go(args);
    visit(args.length != 0 ? args : defaultArguments);
  }
  
  private static void go(String[] args) {
    new Spartanize(args).showLocations();
    new ASTInFilesVisitor(args) {/**/}.visitAll(new ASTTrotter() {
      //
            });
    
  }

  public void showLocations(){
    for(final String location: locations)
      System.out.println(location);
  }
  public static void visit(final String... args) {
    for (final String ¢ : External.Introspector.extract(args != null && args.length != 0 ? 
        args : defaultArguments, Spartanize.class)) {
      
      matteo(¢);
    }
  }
  private static void matteo(String ¢) {
    System.out.println(¢);
    //forget.it(¢);
  }
  
  public final String fixedPoint(final String from) {
    return traversals.fixed(from);
  }
}
