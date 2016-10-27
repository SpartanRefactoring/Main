package il.org.spartan.spartanizer.cmdline;

/** Simplified version of command line client that uses spartizer applicator
 * @author Matteo Orru' */
public class CommandLineClient { // extends AbstractCommandLineSpartanizer{
  
  // TODO Matteo: Add instruction to parse command line
  // TODO Matteo: Add prompt help
  
  public static void main(final String[] args) {
    Reports.intialize();
    for (final String ¢ : args.length != 0 ? args : new String[] { "." })
      new CommandLineSpartanizer(¢).fire();
  }
}