package il.org.spartan.plugin;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.core.runtime.jobs.*;
import org.eclipse.ui.*;
import org.eclipse.ui.plugin.*;
import org.jetbrains.annotations.*;
import org.osgi.framework.*;

import il.org.spartan.plugin.old.*;
import il.org.spartan.plugin.preferences.revision.*;
import il.org.spartan.utils.*;

/** TODO Artium Nihamkin please add a description
 * @author Artium Nihamkin
 * @author Ofir Elmakias
 * @author Ori Roth
 * @since 2013/01/01
 * @since 2015/09/06 (Updated - auto initialization of the plugin)
 * @since 2.6 (Updated - apply nature to newly opened projects) */
public final class Plugin extends AbstractUIPlugin implements IStartup {
  private static final String NEW_PROJECT = "new_project";
  @Nullable private static Plugin plugin;
  private static boolean listening;
  private static final int SAFETY_DELAY = 100;

  @Nullable public static AbstractUIPlugin plugin() {
    return plugin;
  }

  private static void startSpartan() {
    RefreshAll.go();
  }

  public Plugin() {
    plugin = this;
  }

  /** Called whenever the plugin is first loaded into the workbench */
  @Override public void earlyStartup() {
    try {
      monitor.debug("EARLY STATRTUP: gUIBatchLaconizer");
      startSpartan();
    } catch (@NotNull final IllegalStateException ¢) {
      monitor.log(¢);
      return;
    }
    try {
      LibrariesManagement.initializeUserLibraries();
    } catch (@NotNull final CoreException ¢) {
      monitor.log(¢);
    }
  }

  @Override public void start(final BundleContext c) throws Exception {
    super.start(c);
    monitor.debug("START: GUIBatchLaconizer");
    try {
      startSpartan();
      addPartListener();
    } catch (@NotNull final IllegalStateException ¢) {
      monitor.log(¢);
    }
  }

  @Override public void stop(final BundleContext ¢) throws Exception {
    monitor.debug("STOP: spartnizer");
    plugin = null;
    super.stop(¢);
  }

  @Override protected void loadDialogSettings() {
    monitor.debug("LDS: gUIBatchLaconizer");
    super.loadDialogSettings();
  }

  @Override protected void refreshPluginActions() {
    monitor.debug("RPA: gUIBatchLaconizer");
    super.refreshPluginActions();
  }

  @Override protected void saveDialogSettings() {
    monitor.debug("SDS: gUIBatchLaconizer");
    super.saveDialogSettings();
  }

  private static void addPartListener() {
    if (listening)
      return;
    final IWorkspace w = ResourcesPlugin.getWorkspace();
    if (w == null)
      return;
    w.addResourceChangeListener(e -> {
      if (e == null || e.getDelta() == null || !PreferencesResources.NEW_PROJECTS_ENABLE_BY_DEFAULT_VALUE.get())
        return;
      try {
        @NotNull final MProject mp = new MProject();
        e.getDelta().accept(d -> {
          if (d == null || d.getResource() == null || !(d.getResource() instanceof IProject))
            return true;
          @NotNull final IProject p = (IProject) d.getResource();
          if (d.getKind() != IResourceDelta.ADDED)
            return true;
          mp.p = p;
          mp.type = NEW_PROJECT;
          return true;
        });
        // TODO Ori Roth: please clean this up
        if (mp.p != null)
          Job.createSystem(pm -> {
            try {
              if (mp.type.equals(NEW_PROJECT)) {
                eclipse.addNature(mp.p);
                mp.p.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
              }
            } catch (@NotNull final Exception ¢) {
              monitor.log(¢);
            }
          }).schedule(SAFETY_DELAY);
      } catch (@NotNull final CoreException ¢) {
        monitor.log(¢);
      }
    });
    listening = true;
  }

  /** TODO Ori Roth: not convinced it is required. --yg */
  static class MProject {
    public IProject p;
    public String type;
  }
}
