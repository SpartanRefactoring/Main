package il.org.spartan.spartanizer.cmdline;

import il.org.spartan.plugin.*;

@SuppressWarnings("unused") public abstract class AbstractCommandLineSpartanizer {
  // TODO Matteo: useless here? look at CommandLine$Applicator
  // static List<Class<? extends BodyDeclaration>> selectedNodeTypes =
  // as.list(MethodDeclaration.class);
  static AbstractGUIApplicator getSpartanizer(final String tipperName) {
    return Tips2.get(tipperName);
  }

  protected String folder = "/tmp/";
  protected String inputPath;

  public abstract void apply();

  void fire() {
    apply();
    // reportSpectrum();
    // reportCoverage();
    // runEssence();
    // runWordCount();
  }
}