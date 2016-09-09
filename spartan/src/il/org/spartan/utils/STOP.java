// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spartan.utils;

import static org.junit.Assert.*;

import il.org.spartan.streotypes.*;

/** A utility class, serving as the system global manager of the policy of
 * stopping execution due to "Design By Contract" bugs.
 * @author Yossi Gil, 2008/06/20 */
@Module public enum STOP {
  ;
  /** Handler for program exit requests. Default behavior: JUnit failure. */
  private static StopHandler stopHandler = stopFail();

  /** Terminate the program with a specified exit code.
   * @param exitCode * the exit code associated with the termination */
  public static void stop(final int exitCode) {
    stopHandler.stop(exitCode);
  }

  /** Terminate the program with a specified error message
   * @param s the error message associated with the termination */
  public static void stop(final String s) {
    stopHandler.stop(s);
  }

  /** A never-returning method to be used for dealing with assertions that
   * should stop the program run.
   * @param t the exception to be associated with this termination */
  public static void stop(final Throwable t) {
    stop(t, "Program must stop due to this error: ");
  }

  /** A never-returning method to be used for dealing with assertions that
   * should stop the program run.
   * @param t the exception to be associated with this termination
   * @param s a more detailed description of the error */
  public static void stop(final Throwable t, final String s) {
    System.err.println(s);
    t.printStackTrace();
    stop(-1);
  }

  /** Set the termination policy to program exit.
   * @return a {@link StopHandler} object specifying this policy. (It is safe to
   *         ignore this returned value) */
  public static StopHandler stopExit() {
    return stopHandler = new StopHandler() {
      @Override public void stop(final int exitCode) {
        throw new AssertionError("Stop " + exitCode);
      }

      @Override public void stop(final String s) {
        System.out.println(s);
        stop(-1);
      }
    };
  }

  /** Set the termination policy to JUnit failure
   * @return a {@link StopHandler} object specifying this policy. (It is safe to
   *         ignore this returned value) */
  public static StopHandler stopFail() {
    return stopHandler = new StopHandler() {
      @Override public void stop(final int exitCode) {
        fail("Design by contract failue, code = " + exitCode);
      }

      @Override public void stop(final String s) {
        fail("Design by contract failue: " + s);
      }
    };
  }

  /** Set the termination policy to throwing of a {@link Runnable} failure
   * @return a {@link StopHandler} object specifying this policy. (It is safe to
   *         ignore this returned value) */
  public static StopHandler stopRuntimeException() {
    return stopHandler = new StopHandler() {
      @Override public void stop(final int exitCode) {
        throw new RuntimeException("Stop called, exit code=" + exitCode);
      }

      @Override public void stop(final String s) {
        throw new RuntimeException("Stop called:" + s);
      }
    };
  }

  /** An interface representing a stopping policy.
   * @author Yossi Gil, 2008/06/21 */
  public static interface StopHandler {
    /** What to do in case termination with an associated exit code was
     * requested
     * @param exitCode the exit code associated with the termination */
    public void stop(int exitCode);

    /** What to do in case terminations with a specified error message was
     * requested
     * @param s the error message associated with the termination */
    public void stop(String s);
  }
}
