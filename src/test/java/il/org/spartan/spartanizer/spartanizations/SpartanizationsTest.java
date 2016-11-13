package il.org.spartan.spartanizer.spartanizations;

public class SpartanizationsTest {
  // private static IJavaProject javaProject;
  // private static IPackageFragmentRoot srcRoot;
  // private static IPackageFragment pack;
  // Spartanization[] s = new Spartanization[] {new Trimmer()};
  // String fileTestPath =
  // "/home/matteo/MUTATION_TESTING/test-spartan-cl/projects/commons-lang/"+
  // "src/main/java/org/apache/commons/lang3/builder/" +
  // "ToStringStyle.java";
  //
  // @Test public void testSpartanizationNumber() {
  // assertEquals(1, s.length);
  // }
  //
  // @Test public void testSpartanizationContent() {
  // Spartanization $ = s[0];
  // fail("Test not finished!");
  // }
  //
  // @Test public void testSpartanizationsNumber() {
  // Spartanization $ = s[0];
  //
  // fail("Test not finished!");
  // }
  //
  // @Test public void testCollectSuggestions(){
  // File f = new File(fileTestPath);
  // Spartanization $ = s[0];
  // ICompilationUnit iu = null;
  // CompilationUnit u = null;
  // // CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(f);
  //// assert u != null : "compilation unit is null!";
  // try {
  // prepareTempIJavaProject();
  // iu = openCompilationUnit(f);
  // $.setCompilationUnit(iu);
  // $.setMarker(null);
  // int suggestions = $.countSuggestions();
  //// List<Rewrite> suggestions = null;
  //// suggestions = $.collectSuggesions(u);
  //// System.err.println("number of suggestions: " + suggestions.size());
  // System.err.println("number of suggestions: " + suggestions);
  // assertTrue(suggestions> 0);
  // assertEquals(6, suggestions);
  // } catch (IOException | CoreException e) {
  // e.printStackTrace();
  // }
  // }
  //
  // static ICompilationUnit openCompilationUnit(final File f) throws
  // IOException, JavaModelException {
  // final String source = FileUtils.read(f);
  // setPackage(getPackageNameFromSource(source));
  // return pack.createCompilationUnit(f.getName(), source, false, null);
  // }
  //
  // static String getPackageNameFromSource(final String source) {
  // final ASTParser p = ASTParser.newParser(ASTParser.K_COMPILATION_UNIT);
  // p.setSource(source.toCharArray());
  // return getPackageNameFromSource(new Wrapper<>(""), p.createAST(null));
  // }
  //
  // private static String getPackageNameFromSource(final Wrapper<String> $,
  // final ASTNode n) {
  // n.accept(new ASTVisitor() {
  // @Override public boolean visit(final PackageDeclaration ¢) {
  // $.set(¢.getName() + "");
  // return false;
  // }
  // });
  // return $.get();
  // }
  //
  // static void setPackage(final String name) throws JavaModelException {
  // pack = srcRoot.createPackageFragment(name, false, null);
  // }
  //
  // static void prepareTempIJavaProject() throws CoreException {
  //// @SuppressWarnings("unused") IWorkspace ws =
  // ResourcesPlugin.getWorkspace();
  // final IProject p =
  // ResourcesPlugin.getWorkspace().getRoot().getProject("spartanTestTemp");
  // if (p.exists())
  // p.delete(true, null);
  // p.create(null);
  // p.open(null);
  // final IProjectDescription d = p.getDescription();
  // d.setNatureIds(new String[] { JavaCore.NATURE_ID });
  // p.setDescription(d, null);
  // javaProject = JavaCore.create(p);
  // final IFolder binFolder = p.getFolder("bin");
  // final IFolder sourceFolder = p.getFolder("src");
  // srcRoot = javaProject.getPackageFragmentRoot(sourceFolder);
  // binFolder.create(false, true, null);
  // sourceFolder.create(false, true, null);
  // javaProject.setOutputLocation(binFolder.getFullPath(), null);
  // final IClasspathEntry[] buildPath = new IClasspathEntry[1];
  // buildPath[0] = JavaCore.newSourceEntry(srcRoot.getPath());
  // javaProject.setRawClasspath(buildPath, null);
  // }
}
