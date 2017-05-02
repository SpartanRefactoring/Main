package il.org.spartan.bench;

import java.lang.management.*;
import java.util.*;

import org.jetbrains.annotations.*;

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

  @NotNull public static String status() {
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

  private JVM(@NotNull final ClassLoadingMXBean l, @Nullable final CompilationMXBean c) {
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

  public boolean equalsWoGC(@NotNull final JVM o) {
    return seenClasses == o.seenClasses //
        && removedClasses == o.removedClasses //
        && loadedClasses == o.loadedClasses //
        && compileTime == o.compileTime;
  }

  @Override public int hashCode() {
    return (int) (seenClasses ^ seenClasses >>> 32) + 31 * ((int) (removedClasses ^ removedClasses >>> 32) + 31 * (loadedClasses
        + 31 * ((int) (gcTime ^ gcTime >>> 32) + 31 * ((int) (gcCycles ^ gcCycles >>> 32) + 31 * ((int) (compileTime ^ compileTime >>> 32) + 31)))));
  }

  public boolean jitChange(@NotNull final JVM o) {
    return compileTime != o.compileTime;
  }

  @Override @NotNull public String toString() {
    return new StringBuilder().append("JIT𝝉=" + Unit.MILLISECONDS.format(compileTime))
        .append(" #Classes=" + loadedClasses + "(current) " + removedClasses + "(removed) " + seenClasses + "(seen)")
        .append(" HEAP=" + Unit.BYTES.format(heapSize)).append(" #GC=" + gcCycles).append(" GC𝝉=" + Unit.MILLISECONDS.format(gcTime)) + "";
  }

  private boolean equals(@NotNull final JVM o) {
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

    public static long cycles(@NotNull final GarbageCollectorMXBean ¢) {
      return ¢.getCollectionCount();
    }

    public static long cycles(@NotNull final List<GarbageCollectorMXBean> bs) {
      long $ = 0;
      for (@NotNull final GarbageCollectorMXBean ¢ : bs)
        $ += cycles(¢);
      return $;
    }

    @NotNull public static String format() {
      return "GCs: " + format(ManagementFactory.getGarbageCollectorMXBeans());
    }

    @NotNull public static String format(@NotNull final Iterable<GarbageCollectorMXBean> bs) {
      @NotNull final StringBuffer $ = new StringBuffer();
      for (@NotNull final GarbageCollectorMXBean ¢ : bs)
        $.append(new Separator(", ")).append(format(¢));
      return $ + "";
    }

    public static long time() {
      return time(ManagementFactory.getGarbageCollectorMXBeans());
    }

    public static long time(@NotNull final GarbageCollectorMXBean ¢) {
      return ¢.getCollectionTime();
    }

    public static long time(@NotNull final List<GarbageCollectorMXBean> bs) {
      long $ = 0;
      for (@NotNull final GarbageCollectorMXBean ¢ : bs)
        $ += time(¢);
      return $;
    }

    @NotNull static String format(@NotNull final GarbageCollectorMXBean ¢) {
      return new StringBuffer().append(¢.getName()) //
          .append((¢.isValid() ? "" : "/invalid") + " ") //
          .append(¢.getCollectionCount()) //
          .append("  ").append(Unit.MILLISECONDS.format(¢.getCollectionTime())) //
          .append(" (").append(Separate.by(¢.getMemoryPoolNames(), ",")) //
          .append(")") //
          + "";
    }
  }

  public static class MemoryManagement {
    @NotNull public static String format() {
      return "Memory managers: " + format(ManagementFactory.getMemoryManagerMXBeans());
    }

    @NotNull public static String format(@NotNull final Iterable<MemoryManagerMXBean> bs) {
      @NotNull final StringBuffer $ = new StringBuffer("");
      @NotNull final Separator s = new Separator(", ");
      for (@NotNull final MemoryManagerMXBean ¢ : bs)
        $.append(s).append(format(¢));
      return $ + "";
    }

    @NotNull public static String format(@NotNull final MemoryManagerMXBean ¢) {
      return ¢.getName() + (¢.isValid() ? "" : "/invalid") + "(" + Separate.by(¢.getMemoryPoolNames(), ",") + ")";
    }
  }

  public static class TotalMemory {
    @NotNull public static String format() {
      return "Total memory: " + format(ManagementFactory.getMemoryMXBean());
    }

    @NotNull public static String format(@NotNull final MemoryMXBean ¢) {
      return new StringBuffer() //
          .append("Zombies=").append(¢.getObjectPendingFinalizationCount()).append("\t") //
          .append("Heap [").append(format(¢.getHeapMemoryUsage())).append("]\n")//
          .append("\t\tNon Heap [").append(format(¢.getNonHeapMemoryUsage())).append("] ")//
          + "";
    }

    @NotNull public static String format(@NotNull final MemoryUsage ¢) {
      return new StringBuffer() //
          .append("Init:").append(format(¢.getInit())).append(" ")//
          .append("Max:").append(format(¢.getMax())).append(" ")//
          .append("Committed:").append(format(¢.getCommitted())).append(" ")//
          .append("Used:").append(format(¢.getUsed())).append(" ")//
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
