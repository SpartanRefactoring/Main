package il.org.spartan.bench;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.CompilationMXBean;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryManagerMXBean;
import java.lang.management.MemoryUsage;
import java.util.List;

import fluent.ly.dump;
import il.org.spartan.utils.Separate;
import il.org.spartan.utils.Separator;

/*
 * Records some characteristics of the JVM state.
 *
 * @author Yossi Gil
 *
 * @since 03/06/2011
 */
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
    dump.of(ManagementFactory.getRuntimeMXBean());
  }

  public static String status() {
    return TotalMemory.format() + "\n" + MemoryManagement.format() + "\n" + GarbageCollectionSystem.format();
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
  @Override public boolean equals(final Object ¢) {
    return ¢ == this || ¢ instanceof JVM && equals((JVM) ¢);
  }

  public boolean equalsWoGC(final JVM o) {
    return seenClasses == o.seenClasses //
        && removedClasses == o.removedClasses //
        && loadedClasses == o.loadedClasses //
        && compileTime == o.compileTime;
  }

  @Override public int hashCode() {
    return (int) (seenClasses ^ seenClasses >>> 32) + 31
        * ((int) (removedClasses ^ removedClasses >>> 32) + 31 * (loadedClasses + 31 * ((int) (gcTime ^ gcTime >>> 32)
            + 31 * ((int) (gcCycles ^ gcCycles >>> 32) + 31 * ((int) (compileTime ^ compileTime >>> 32) + 31)))));
  }

  public boolean jitChange(final JVM o) {
    return compileTime != o.compileTime;
  }

  @Override public String toString() {
    return "JIT𝝉=" + Unit.MILLISECONDS.format(compileTime) + " #Classes=" + loadedClasses + "(current) "
        + removedClasses + "(removed) " + seenClasses + "(seen) HEAP=" + Unit.BYTES.format(heapSize) + " #GC="
        + gcCycles + " GC𝝉=" + Unit.MILLISECONDS.format(gcTime);
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

    public static long cycles(final GarbageCollectorMXBean ¢) {
      return ¢.getCollectionCount();
    }

    public static long cycles(final List<GarbageCollectorMXBean> bs) {
      long $ = 0;
      for (final GarbageCollectorMXBean ¢ : bs)
        $ += cycles(¢);
      return $;
    }

    public static String format() {
      return "GCs: " + format(ManagementFactory.getGarbageCollectorMXBeans());
    }

    public static String format(final Iterable<GarbageCollectorMXBean> bs) {
      final StringBuffer $ = new StringBuffer();
      for (final GarbageCollectorMXBean ¢ : bs)
        $.append(new Separator(", ")).append(format(¢));
      return $ + "";
    }

    public static long time() {
      return time(ManagementFactory.getGarbageCollectorMXBeans());
    }

    public static long time(final GarbageCollectorMXBean ¢) {
      return ¢.getCollectionTime();
    }

    public static long time(final List<GarbageCollectorMXBean> bs) {
      long $ = 0;
      for (final GarbageCollectorMXBean ¢ : bs)
        $ += time(¢);
      return $;
    }

    static String format(final GarbageCollectorMXBean ¢) {
      return ¢.getName() + (¢.isValid() ? "" : "/invalid") + " " + ¢.getCollectionCount() + "  "
          + Unit.MILLISECONDS.format(¢.getCollectionTime()) + " (" + Separate.by(¢.getMemoryPoolNames(), ",") + ")";
    }
  }

  public static class MemoryManagement {
    public static String format() {
      return "Memory managers: " + format(ManagementFactory.getMemoryManagerMXBeans());
    }

    public static String format(final Iterable<MemoryManagerMXBean> bs) {
      final StringBuffer $ = new StringBuffer("");
      final Separator s = new Separator(", ");
      for (final MemoryManagerMXBean ¢ : bs)
        $.append(s).append(format(¢));
      return $ + "";
    }

    public static String format(final MemoryManagerMXBean ¢) {
      return ¢.getName() + (¢.isValid() ? "" : "/invalid") + "(" + Separate.by(¢.getMemoryPoolNames(), ",") + ")";
    }
  }

  public static class TotalMemory {
    public static String format() {
      return "Total memory: " + format(ManagementFactory.getMemoryMXBean());
    }

    public static String format(final MemoryMXBean ¢) {
      return "Zombies=" + ¢.getObjectPendingFinalizationCount() + "\tHeap [" + format(¢.getHeapMemoryUsage())
      + "]\n\t\tNon Heap [" + format(¢.getNonHeapMemoryUsage()) + "] ";
    }

    public static String format(final MemoryUsage ¢) {
      return "Init:" + format(¢.getInit()) + " Max:" + format(¢.getMax()) + " Committed:" + format(¢.getCommitted())
      + " Used:" + format(¢.getUsed()) + " ";
    }

    public static long heapSize() {
      return ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed();
    }

    private static String format(final long m) {
      return Unit.BYTES.format(m);
    }
  }
}
