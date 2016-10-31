package il.org.spartan.spartanizer.cmdline;

/** Simplified version of command line client that uses spartizer applicator
 * @author Matteo Orru' */
public class CommandLineClient { // extends AbstractCommandLineSpartanizer{
  // TODO Matteo: Add instruction to parse command line
  // TODO Matteo: Add prompt help
  public static void main(final String[] args) {
    for (final String ¢ : args.length != 0 ? args : new String[] { "." })
      new CommandLineSpartanizer(¢).fire();
  }
  
  static void printPrompt() {
    System.out.println("Spartan Refactoring plugin command line");
    System.out.println("Usage: eclipse -application il.org.spartan.spartanizer.application -nosplash [OPTIONS] PATH");
    System.out.println("Executes the Spartan Refactoring Eclipse plug-in from the command line on all the Java source files "
        + "within the given PATH. Files are spartanized in place by default.");
    System.out.println("");
    System.out.println("Options:");
    System.out.println("  -N       Do not overwrite existing files (writes the Spartanized output to a new file in the same directory)");
    System.out.println("  -C<num>  Maximum number of Spartanizaion rounds for each file (default: 20)");
    System.out.println("  -E       Display statistics for each file separately");
    System.out.println("  -V       Be verbose");
    System.out.println("  -L       printout logs");
    System.out.println("");
    System.out.println("Print statistics:");
    System.out.println("  -l       Show the number of lines before and after spartanization");
    System.out.println("  -r       Show the number of Spartanizaion made in each round");
    System.out.println("");
    System.out.println("Output:");
    System.out.println("  -logPath Output dir for logs");
    System.out.println("");
  }
}