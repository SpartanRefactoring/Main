package il.org.spartan.spartanizer.cmdline.good;

import static il.org.spartan.spartanizer.engine.nominal.Trivia.*;

import org.eclipse.jdt.core.dom.*;

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
    run(args);
    //visit(args.length != 0 ? args : defaultArguments);
  }
  
  private static void run(String[] args) {
    Spartanize s = new Spartanize(args);
    s.showLocations().visitAll(new ASTTrotter() {
           
      //
      @Override public boolean preVisit2(final ASTNode ¢) {
//        System.out.println("Folding: " + !isFolding());
//        System.out.println("go: " + go(¢));
//        System.out.println(!isFolding() && go(¢) != null);
//        return !isFolding() && go(¢) != null;
        return true;
      }
      
      @Override public boolean visit(final CompilationUnit ¢) {
        //++total;
//        if (!interesting(¢))
//          return true;
        //++interesting;
        record(squeeze(theSpartanizer.repetitively(removeComments(JUnitTestMethodFacotry.code(¢ + "")))) + "\n");
        return true;
      }
    });
    
  }

  public Spartanize showLocations(){
    for(final String location: locations)
      System.out.println("--------->" + location);
    return this;
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
