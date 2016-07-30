package il.org.spartan.refactoring.builder;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;

import il.org.spartan.utils.*;

/**
 * @author Artium Nihamkin
 * @since 2013/07/01
 */
public class Nature implements IProjectNature {
  /** ID of this project nature */
  public static final String NATURE_ID = "org.spartan.refactoring.NatureID";
  /** The project to which we relate */
  private IProject project;

  /* (non-Javadoc)
   *
   * @see org.eclipse.core.resources.IProjectNature#configure() */
  @Override public void configure() throws CoreException {
    final IProjectDescription d = project.getDescription();
    final ICommand[] cs = d.getBuildSpec();
    for (final ICommand c : cs)
      if (c.getBuilderName().equals(Builder.BUILDER_ID))
        return;
    final ICommand c = d.newCommand();
    c.setBuilderName(Builder.BUILDER_ID);
    d.setBuildSpec(Utils.append(cs, c));
    project.setDescription(d, null);
  }
  /* (non-Javadoc)
   *
   * @see org.eclipse.core.resources.IProjectNature#deconfigure() */
  @Override public void deconfigure() throws CoreException {
    final IProjectDescription description = getProject().getDescription();
    final ICommand[] cs = description.getBuildSpec();
    for (int i = 0; i < cs.length; ++i)
      if (cs[i].getBuilderName().equals(Builder.BUILDER_ID)) {
        description.setBuildSpec(Utils.delete(cs, i));
        project.setDescription(description, null);
        return;
      }
  }
  @Override public IProject getProject() {
    return project;
  }
  @Override public void setProject(final IProject p) {
    project = p;
  }
}
