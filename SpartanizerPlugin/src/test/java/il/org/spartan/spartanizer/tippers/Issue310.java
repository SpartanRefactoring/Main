package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** @author Alex Kopzon
 * @since 2016-09-23 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) @SuppressWarnings({ "static-method", "javadoc" }) public class Issue310 {
  @Ignore @Test public void OrisCode() { // is not parsing well
    trimmingOf(
        "int i;for (i=0; i < MAX_PASSES; ++i) {final IProgressService ps=wb.getProgressService();final AtomicInteger passNum=new AtomicInteger(i + 1);final AtomicBoolean cancled=new AtomicBoolean(false);try {ps.run(true,true,pm -> {"
            + "pm.beginTask(\"Spartanizing project '\" + javaProject.getElementName() + \"' - \"+ \"Pass \"+ passNum.get()+ \" out of maximum of \"+ MAX_PASSES,us.size());int n=0;final List<ICompilationUnit> dead=new ArrayList<>();for (final ICompilationUnit ¢ : us) {if (pm.isCanceled()) {cancled.set(true);break;"
            + "}pm.worked(1);pm.subTask(¢.getElementName() + \" \" + ++n+ \"/\"+ us.size());if (!a.apply(¢)) dead.add(¢);}us.removeAll(dead);pm.done();});}"
            + "catch (  final InvocationTargetException x) {LoggingManner.logEvaluationError(this,x);}catch (  final InterruptedException x) {LoggingManner.logEvaluationError(this,x);}if (cancled.get() || us.isEmpty()) break;}")
                .stays();
  }

  @Test public void OrisCode_check_a() {
    trimmingOf("int i = 0;for(;i < 10;++i) if(i=5)break;").gives("for(int i = 0;i < 10;++i) if(i=5)break;")
        .gives("for(int ¢ = 0;¢ < 10;++¢) if(¢=5)break;").stays();
  }

  @Test public void updaters_for_1() {
    trimmingOf("public boolean check(final ASTNode n) {" + "ASTNode p = n;" + "for (;p != null;) {" + "if (dns.contains(p))" + "return true;"
        + "p = p.getParent();" + "}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "for (ASTNode p = n;p != null;) {" + "if (dns.contains(p))" + "return true;"
                + "p = p.getParent();" + "}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "for (ASTNode p = n;p != null;p = p.getParent()) {" + "if (dns.contains(p))"
                + "return true;" + "}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "for (ASTNode p = n;p != null;p = p.getParent()) " + "if (dns.contains(p))"
                + "return true;" + "" + "return false;" + "}")
            .stays();
  }

  @Test public void updaters_for_2() {
    trimmingOf("public boolean check(final ASTNode n) {" + "ASTNode p = n;" + "for (;p != null;) {" + "if (dns.contains(p))" + "return true;"
        + "if (ens.contains(p))" + "return true;" + "p = p.getParent();" + "}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "for (ASTNode p = n;p != null;) {" + "if (dns.contains(p))" + "return true;"
                + "if (ens.contains(p))" + "return true;" + "p = p.getParent();" + "}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "for (ASTNode p = n;p != null;p = p.getParent()) {" + "if (dns.contains(p))"
                + "return true;" + "if (ens.contains(p))" + "return true;" + "}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "for (ASTNode p = n;p != null;p = p.getParent()) {"
                + "if (dns.contains(p) || ens.contains(p))" + "return true;" + "}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "for (ASTNode p = n;p != null;p = p.getParent()) "
                + "if (dns.contains(p) || ens.contains(p))" + "return true;" + "" + "return false;" + "}")
            .stays();
  }

  @Test public void updaters_for_3() {
    trimmingOf("").stays();
  }

  @Test public void updaters_for_4() {
    trimmingOf("public boolean check(final ASTNode n) {" + "for(ASTNode p = n;p != null;) {" + "if (dns.contains(p))" + "return true;" + "++i;++j;"
        + "}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "for (ASTNode p = n; p != null; ++i) {" + "if (dns.contains(p))" + "return true;++j;"
                + "}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "for (ASTNode p = n; p != null; ++i,++j) {" + "if (dns.contains(p))" + "return true;}"
                + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "for (ASTNode p = n; p != null; ++i,++j) " + "if (dns.contains(p))" + "return true;"
                + "return false;" + "}")
            .stays();
  }

  @Test public void updaters_ordering_check_1_b() {
    trimmingOf("for(int i = 0;;) {arr[i] = 0;++i;}").gives("for(int ¢ = 0;;) {arr[¢] = 0;++¢;}").gives("for(int ¢ = 0;;arr[¢] = 0) {++¢;}")
        .gives("for(int ¢ = 0;;arr[¢] = 0,++¢) {}").stays();
  }

  @Test public void updaters_ordering_check_2_right() {
    trimmingOf(
        "List<IExtendedModifier> modifiers = new ArrayList<>();IExtendedModifier m = modifiers.get(0);for(int i = 0;;) {m = modifiers.get(i);++i;}")
            .gives(
                "List<IExtendedModifier> modifiers = new ArrayList<>();IExtendedModifier m = modifiers.get(0);for(int ¢ = 0;;) {m = modifiers.get(¢);++¢;}")
            .gives(
                "List<IExtendedModifier> modifiers = new ArrayList<>();IExtendedModifier m = modifiers.get(0);for(int ¢ = 0;;m = modifiers.get(¢)) {++¢;}")
            .gives(
                "List<IExtendedModifier> modifiers = new ArrayList<>();IExtendedModifier m = modifiers.get(0);for(int ¢ = 0;;m = modifiers.get(¢),++¢) {}")
            .stays();
  }

  @Test public void updaters_while_1() {
    trimmingOf("public boolean check(final ASTNode n) {" + "ASTNode p = n;" + "while (p != null) {" + "if (dns.contains(p))" + "return true;"
        + "p = p.getParent();" + "}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "for (ASTNode p = n; p != null;) {" + "if (dns.contains(p))" + "return true;"
                + "p = p.getParent();}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "for (ASTNode p = n; p != null;p = p.getParent()) {" + "if (dns.contains(p))"
                + "return true;" + "}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "for (ASTNode p = n; p != null;p = p.getParent()) " + "if (dns.contains(p))"
                + "return true;" + "return false;" + "}")
            .stays();
  }

  @Test public void updaters_while_2() {
    trimmingOf("public boolean check(final ASTNode n) {" + "ASTNode p = n;" + "while (p != null) {" + "if (dns.contains(p))" + "return true;"
        + "if (ens.contains(p))" + "return true;" + "p = p.getParent();" + "}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "for (ASTNode p = n;p != null;) {" + "if (dns.contains(p))" + "return true;"
                + "if (ens.contains(p))" + "return true;" + "p = p.getParent();}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "for (ASTNode p = n;p != null;p = p.getParent()) {" + "if (dns.contains(p))"
                + "return true;" + "if (ens.contains(p))" + "return true;" + "}" + "return false;" + "}");
  }

  @Test public void updaters_while_3() {
    trimmingOf("public boolean check(final ASTNode n) {" + "ASTNode p = n;" + "while (p != null) {" + "if (dns.contains(p))" + "return true;" + "f();"
        + "}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "for (ASTNode p = n; p != null;) {" + "if (dns.contains(p))" + "return true;" + "f();}"
                + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "for (ASTNode p = n; p != null;f()) {" + "if (dns.contains(p))" + "return true;}"
                + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "for (ASTNode p = n; p != null;f()) " + "if (dns.contains(p))" + "return true;"
                + "return false;" + "}")
            .stays();
  }

  @Test public void updaters_while_4() {
    trimmingOf("public boolean check(final ASTNode n) {" + "ASTNode p = n;" + "while (p != null) {" + "if (dns.contains(p))" + "return true;" + "++i;"
        + "}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "for (ASTNode p = n; p != null;) {" + "if (dns.contains(p))" + "return true;" + "++i;}"
                + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "for (ASTNode p = n; p != null;++i) {" + "if (dns.contains(p))" + "return true;" + "}"
                + "return false;" + "}");
  }
}
