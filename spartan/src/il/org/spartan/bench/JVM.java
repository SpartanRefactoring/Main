/**
 *
 */
package il.org.spartan.bench;

import java.lang.management.*;
import java.util.*;

import il.org.spartan.*;
import il.org.spartan.utils.*;
import il.org.spartan.utils.Separator;

/* Records some characteristics of the JVM state.
 *
 * @author Yossi Gil
 *
 * @since 03/06/2011 */
public final class JVM {
  private static final boolean hasCompiler = ManagementFactory.getCompilationMXBean() != null;

  public static void gc() {
    final long initially = TotalMemory.heapSize();
    for (long before = initially;;) {
      System.gc();
      final long after = TotalMemory.heapSize();
      if (after >= before || after >= initially)
        return;
      before = after;
    }
  }

  public static boolean hasCompiler() {
    return hasCompiler;
  }

  public static String heapSize() {
    return Unit.BYTES.format(JVM.TotalMemory.heapSize());
  }

  public static void main(final String[] args) {
    System.out.println("# processors" + ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors());
    System.out.println("oprating system" + ManagementFactory.getOperatingSystemMXBean().getName());
    System.out.println("architecture" + ManagementFactory.getOperatingSystemMXBean().getArch());
    System.out.println(ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage());
    System.out.println(ManagementFactory.getOperatingSystemMXBean().getVersion());
    System.out.println(ManagementFactory.getRuntimeMXBean().getBootClassPath());
    System.out.println(ManagementFactory.getRuntimeMXBean().getClassPath());
    System.out.println(ManagementFactory.getRuntimeMXBean().getManagementSpecVersion());
    System.out.println(ManagementFactory.getRuntimeMXBean().getName());
    System.out.println(ManagementFactory.getRuntimeMXBean().getSpecName());
    System.out.println(ManagementFactory.getRuntimeMXBean().getSpecVendor());
    System.out.println(ManagementFactory.getRuntimeMXBean().getStartTime());
    System.out.println(ManagementFactory.getRuntimeMXBean().getUptime());
    System.out.println(ManagementFactory.getRuntimeMXBean().getVmName());
    System.out.println(ManagementFactory.getRuntimeMXBean().getVmVersion());
    System.out.println(ManagementFactory.getRuntimeMXBean().getUptime());
    System.out.println(ManagementFactory.getRuntimeMXBean().getInputArguments());
    System.out.println(ManagementFactory.getRuntimeMXBean().getSystemProperties());
    dump.go(ManagementFactory.getRuntimeMXBean());
  }

  public static String status() {
    return new StringBuilder() //
        .append(TotalMemory.format())//
        .append("\n") //
        .append(MemoryManagement.format())//
        .append("\n") //
        .append(GarbageCollectionSystem.format()) //
        .toString();
  }

  /** Number of classes currently loaded in the JVM */
  public final int loadedClasses;
  /** Count of the total number of classes that have been loaded by this JVM. */
  public final long seenClasses;
  /** Number of classes that have been unloaded by this JVM. */
  public final long removedClasses;
  /** Total time (in milliseconds) spent in compilation. */
  public final long compileTime;
  /** Garbage collection cycles carried so far by the JVM. */
  public final long gcCycles;
  /** Total time (in milliseconds) spent in garbage collection cycles. */
  public final long gcTime;
  /** Total time (in milliseconds) spent in garbage collection cycles. */
  public final long heapSize;

  public JVM() {
    this(ManagementFactory.getClassLoadingMXBean(), ManagementFactory.getCompilationMXBean());
  }

  private JVM(final ClassLoadingMXBean l, final CompilationMXBean c) {
    seenClasses = l.getTotalLoadedClassCount();
    removedClasses = l.getUnloadedClassCount();
    loadedClasses = l.getLoadedClassCount();
    compileTime = c == null || !c.isCompilationTimeMonitoringSupported() ? -1 : c.getTotalCompilationTime();
    gcCycles = GarbageCollectionSystem.cycles();
    gcTime = GarbageCollectionSystem.time();
    heapSize = TotalMemory.heapSize();
  }

  /** Is this an object of the same type and with the same field contents? */
  @Override public final boolean equals(final Object o) {
    return o == this || o instanceof JVM && equals((JVM) o);
  }

  public boolean equalsWoGC(final JVM o) {
    return seenClasses == o.seenClasses //
        && removedClasses == o.removedClasses //
        && loadedClasses == o.loadedClasses //
        && compileTime == o.compileTime;
  }

  @Override public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (compileTime ^ compileTime >>> 32);
    result = prime * result + (int) (gcCycles ^ gcCycles >>> 32);
    result = prime * result + (int) (gcTime ^ gcTime >>> 32);
    result = prime * result + loadedClasses;
    result = prime * result + (int) (removedClasses ^ removedClasses >>> 32);
    result = prime * result + (int) (seenClasses ^ seenClasses >>> 32);
    return result;
  }

  public final boolean jitChange(final JVM o) {
    return compileTime != o.compileTime;
  }

  @Override public String toString() {
    return new StringBuilder() //
        .append("JIT𝝉=" + Unit.MILLISECONDS.format(compileTime)) //
        .append(" #Classes=" + loadedClasses + "(current) " + removedClasses + "(removed) " + seenClasses + "(seen)")//
        .append(" HEAP=" + Unit.BYTES.format(heapSize)) // /
        .append(" #GC=" + gcCycles) //
        .append(" GC𝝉=" + Unit.MILLISECONDS.format(gcTime)) //
        .toString();
  }

  private final boolean equals(final JVM o) {
    return seenClasses == o.seenClasses //
        && removedClasses == o.removedClasses //
        && loadedClasses == o.loadedClasses //
        && compileTime == o.compileTime //
        && gcCycles == o.gcCycles //
        && gcTime == o.gcTime;
  }

  public static class GarbageCollectionSystem {
    public static long cycles() {
      return cycles(ManagementFactory.getGarbageCollectorMXBeans());
    }

    public static long cycles(final GarbageCollectorMXBean g) {
      return g.getCollectionCount();
    }

    public static long cycles(final List<GarbageCollectorMXBean> gs) {
      long $ = 0;
      for (final GarbageCollectorMXBean g : gs)
        $ += cycles(g);
      return $;
    }

    public static String format() {
      return "GCs: " + format(ManagementFactory.getGarbageCollectorMXBeans());
    }

    public static String format(final Iterable<GarbageCollectorMXBean> gs) {
      final StringBuffer $ = new StringBuffer();
      final Separator s = new Separator(", ");
      for (final GarbageCollectorMXBean g : gs)
        $.append(s).append(format(g));
      return $.toString();
    }

    public static long time() {
      return time(ManagementFactory.getGarbageCollectorMXBeans());
    }

    public static long time(final GarbageCollectorMXBean g) {
      return g.getCollectionTime();
    }

    public static long time(final List<GarbageCollectorMXBean> gs) {
      long $ = 0;
      for (final GarbageCollectorMXBean g : gs)
        $ += time(g);
      return $;
    }

    static String format(final GarbageCollectorMXBean g) {
      return new StringBuffer().append(g.getName()) //
          .append(g.isValid() ? " " : "/invalid ") //
          .append(g.getCollectionCount()) //
          .append("  ").append(Unit.MILLISECONDS.format(g.getCollectionTime())) //
          .append(" (").append(Separate.by(g.getMemoryPoolNames(), ",")) //
          .append(")").toString();
    }
  }

  public static class MemoryManagement {
    public static String format() {
      return "Memory managers: " + format(ManagementFactory.getMemoryManagerMXBeans());
    }

    public static String format(final Iterable<MemoryManagerMXBean> ms) {
      final StringBuffer $ = new StringBuffer("");
      final Separator s = new Separator(", ");
      for (final MemoryManagerMXBean m : ms)
        $.append(s).append(format(m));
      return $.append("").toString();
    }

    public static String format(final MemoryManagerMXBean m) {
      return new StringBuffer().append(m.getName()) //
          .append(m.isValid() ? "" : "/invalid") //
          .append("(").append(Separate.by(m.getMemoryPoolNames(), ",")).append(")") //
          .toString();
    }
  }

  public static class TotalMemory {
    public static String format() {
      return "Total memory: " + format(ManagementFactory.getMemoryMXBean());
    }

    public static String format(final MemoryMXBean m) {
      return new StringBuffer() //
          .append("Zombies=").append(m.getObjectPendingFinalizationCount()).append("\t") //
          .append("Heap [").append(format(m.getHeapMemoryUsage())).append("]\n")//
          .append("\t\tNon Heap [").append(format(m.getNonHeapMemoryUsage())).append("] ")//
          .toString();
    }

    public static String format(final MemoryUsage u) {
      return new StringBuffer() //
          .append("Init:").append(format(u.getInit())).append(" ")//
          .append("Max:").append(format(u.getMax())).append(" ")//
          .append("Committed:").append(format(u.getCommitted())).append(" ")//
          .append("Used:").append(format(u.getUsed())).append(" ")// ;
          .toString();
    }

    public static long heapSize() {
      return ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed();
    }

    private static String format(final long m) {
      return Unit.BYTES.format(m);
    }
  }
}
