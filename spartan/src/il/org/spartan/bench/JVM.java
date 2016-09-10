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
        + "";
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

  /**
   * Is this an object of the same type and with the same field contents? 
   */
  @Override public boolean equals(final Object o) {
    return o == this || o instanceof JVM && equals((JVM) o);
  }

  public boolean equalsWoGC(final JVM o) {
    return seenClasses == o.seenClasses //
        && removedClasses == o.removedClasses //
        && loadedClasses == o.loadedClasses //
        && compileTime == o.compileTime;
  }

  @Override public int hashCode() {
    return (int) (seenClasses ^ seenClasses >>> 32) + 31 * ((int) (removedClasses ^ removedClasses >>> 32) + 31 * (loadedClasses
        + 31 * ((int) (gcTime ^ gcTime >>> 32) + 31 * ((int) (gcCycles ^ gcCycles >>> 32) + 31 * ((int) (compileTime ^ compileTime >>> 32) + 31)))));
  }

  public boolean jitChange(final JVM o) {
    return compileTime != o.compileTime;
  }

  @Override public String toString() {
    return new StringBuilder() //
        .append("JIT𝝉=" + Unit.MILLISECONDS.format(compileTime)) //
        .append(" #Classes=" + loadedClasses + "(current) " + removedClasses + "(removed) " + seenClasses + "(seen)")//
        .append(" HEAP=" + Unit.BYTES.format(heapSize)) // /
        .append(" #GC=" + gcCycles) //
        .append(" GC𝝉=" + Unit.MILLISECONDS.format(gcTime)) //
        + "";
  }

  private boolean equals(final JVM o) {
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

    public static long cycles(final GarbageCollectorMXBean b) {
      return b.getCollectionCount();
    }

    public static long cycles(final List<GarbageCollectorMXBean> bs) {
      long $ = 0;
      for (final GarbageCollectorMXBean g : bs)
        $ += cycles(g);
      return $;
    }

    public static String format() {
      return "GCs: " + format(ManagementFactory.getGarbageCollectorMXBeans());
    }

    public static String format(final Iterable<GarbageCollectorMXBean> bs) {
      final StringBuffer $ = new StringBuffer();
      final Separator s = new Separator(", ");
      for (final GarbageCollectorMXBean g : bs)
        $.append(s).append(format(g));
      return $ + "";
    }

    public static long time() {
      return time(ManagementFactory.getGarbageCollectorMXBeans());
    }

    public static long time(final GarbageCollectorMXBean b) {
      return b.getCollectionTime();
    }

    public static long time(final List<GarbageCollectorMXBean> bs) {
      long $ = 0;
      for (final GarbageCollectorMXBean g : bs)
        $ += time(g);
      return $;
    }

    static String format(final GarbageCollectorMXBean b) {
      return new StringBuffer().append(b.getName()) //
          .append((b.isValid() ? "" : "/invalid") + " ") //
          .append(b.getCollectionCount()) //
          .append("  ").append(Unit.MILLISECONDS.format(b.getCollectionTime())) //
          .append(" (").append(Separate.by(b.getMemoryPoolNames(), ",")) //
          .append(")") //
          + "";
    }
  }

  public static class MemoryManagement {
    public static String format() {
      return "Memory managers: " + format(ManagementFactory.getMemoryManagerMXBeans());
    }

    public static String format(final Iterable<MemoryManagerMXBean> bs) {
      final StringBuffer $ = new StringBuffer("");
      final Separator s = new Separator(", ");
      for (final MemoryManagerMXBean m : bs)
        $.append(s).append(format(m));
      return $ + "";
    }

    public static String format(final MemoryManagerMXBean b) {
      return new StringBuffer().append(b.getName()) //
          .append(b.isValid() ? "" : "/invalid") //
          .append("(").append(Separate.by(b.getMemoryPoolNames(), ",")).append(")") //
          + "";
    }
  }

  public static class TotalMemory {
    public static String format() {
      return "Total memory: " + format(ManagementFactory.getMemoryMXBean());
    }

    public static String format(final MemoryMXBean b) {
      return new StringBuffer() //
          .append("Zombies=").append(b.getObjectPendingFinalizationCount()).append("\t") //
          .append("Heap [").append(format(b.getHeapMemoryUsage())).append("]\n")//
          .append("\t\tNon Heap [").append(format(b.getNonHeapMemoryUsage())).append("] ")//
          + "";
    }

    public static String format(final MemoryUsage u) {
      return new StringBuffer() //
          .append("Init:").append(format(u.getInit())).append(" ")//
          .append("Max:").append(format(u.getMax())).append(" ")//
          .append("Committed:").append(format(u.getCommitted())).append(" ")//
          .append("Used:").append(format(u.getUsed())).append(" ")//
          + "";
    }

    public static long heapSize() {
      return ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed();
    }

    private static String format(final long m) {
      return Unit.BYTES.format(m);
    }
  }
}
