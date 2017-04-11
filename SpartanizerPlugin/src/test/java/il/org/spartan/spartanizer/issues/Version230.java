package il.org.spartan.spartanizer.issues;

import static il.org.spartan.Utils.*;
import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.engine.ExpressionComparator.*;
import static il.org.spartan.spartanizer.engine.into.*;
import static il.org.spartan.spartanizer.testing.TestUtilsAll.*;
import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.testing.*;
import il.org.spartan.spartanizer.tippers.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.utils.*;

/** Unit tests for version 2.30
 * @author Yossi Gil
 * @since 2014-07-10 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc", "OverlyComplexClass" }) //
public final class Version230 {
  @Test public void actualExampleForSortAddition() {
    topDownTrimming("1 + b.statements().indexOf(declarationStmt)")//
        .stays();
  }

  @Test public void actualExampleForSortAdditionInContext() {
    final String from = "2 + a<b";
    final WrapIntoComilationUnit w = WrapIntoComilationUnit.Expression;
    final String wrap = w.on(from);
    azzert.that(from, is(w.off(wrap)));
    final String unpeeled = trim.apply(new Trimmer(), wrap);
    if (wrap.equals(unpeeled))
      azzert.fail("Nothing done on " + from);
    final String peeled = w.off(unpeeled);
    if (peeled.equals(from))
      azzert.that("No similification of " + from, from, is(not(peeled)));
    azzert.that("Simpification of " + from + " is just reformatting", tide.clean(from), not(tide.clean(peeled)));
    assertSimilar("a + 2<b", peeled);
  }

  @Test public void andWithCLASS_CONSTANT() {
    topDownTrimming("(x>> 18)& MASK_BITS")//
        .stays();
    topDownTrimming("(x>> 18)& MASK_6BITS")//
        .stays();
  }

  @Test public void annotationDoNotRemoveSingleMemberNotCalledValue() {
    topDownTrimming("@SuppressWarnings(sky=\"blue\")void m(){}")//
        .stays();
  }

  @Test public void annotationDoNotRemoveValueAndSomethingElse() {
    topDownTrimming("@SuppressWarnings(value=\"something\", x=2)void m(){}")//
        .stays();
  }

  @Test public void annotationRemoveEmptyParentheses() {
    topDownTrimming("@Override()void m(){}")//
        .gives("@Override void m(){}");
  }

  @Test public void annotationRemoveValueFromMultipleAnnotations() {
    topDownTrimming("@TargetApi(value=23)@SuppressWarnings(value=\"javadoc\")void m(){}").gives("@TargetApi(23)@SuppressWarnings(\"javadoc\")void m(){}")//
        .stays();
  }

  @Test public void annotationRemoveValueMemberArrayValue() {
    topDownTrimming("@SuppressWarnings(value={ \"something\", \"something else\" })void m(){}")
        .gives("@SuppressWarnings({ \"something\", \"something else\" })void m(){}");
  }

  @Test public void annotationRemoveValueMemberSingleValue() {
    topDownTrimming("@SuppressWarnings(value=\"something\")void m(){}")//
        .gives("@SuppressWarnings(\"something\")void m(){}");
  }

  @Test public void assignmentAssignmentChain1() {
    topDownTrimming("c=a=13;b=13;")//
        .gives("b=c=a=13;");
  }

  @Test public void assignmentAssignmentChain2() {
    topDownTrimming("a=13;b=c=13;")//
        .gives("b=c=a=13;");
  }

  @Test public void assignmentAssignmentChain3() {
    topDownTrimming("a=b=13;c=d=13;")//
        .gives("c=d=a=b=13;");
  }

  @Test public void assignmentAssignmentChain4() {
    topDownTrimming("a1=a2=a3=a4=13;b1=b2=b3=b4=b5=13;")//
        .gives("b1=b2=b3=b4=b5=a1=a2=a3=a4=13;");
  }

  @Test public void assignmentAssignmentChain5() {
    topDownTrimming("a1=(a2=(a3=(a4=13)));b1=b2=b3=((((b4=(b5=13)))));")//
        .gives("a1=(a2=(a3=(a4=13)));b1=b2=b3=(((b4=(b5=13))));") //
        .gives("a1=(a2=(a3=(a4=13)));b1=b2=b3=((b4=(b5=13)));") //
        .gives("a1=(a2=(a3=(a4=13)));b1=b2=b3=(b4=(b5=13));") //
        .stays()//
    ;
  }

  @Test public void assignmentAssignmentNew() {
    topDownTrimming("a=new B();b=new B();")//
        .stays();
  }

  @Test public void assignmentAssignmentNewArray() {
    topDownTrimming("a=new A[3];b=new A[3];")//
        .stays();
  }

  @Test public void assignmentAssignmentNull() {
    topDownTrimming("c=a=null;b=null;")//
        .stays();
  }

  @Test public void assignmentAssignmentSideEffect() {
    topDownTrimming("a=f();b=f();")//
        .stays();
  }

  @Test public void assignmentAssignmentVanilla() {
    topDownTrimming("a=13;b=13;")//
        .gives("b=a=13;");
  }

  @Test public void assignmentAssignmentVanilla0() {
    topDownTrimming("a=0;b=0;")//
        .gives("b=a=0;");
  }

  @Test public void assignmentAssignmentVanillaScopeIncludes() {
    included("a=3;b=3;", Assignment.class).in(new AssignmentAndAssignmentOfSameValue());
  }

  @Test public void assignmentAssignmentVanillaScopeIncludesNull() {
    included("a=null;b=null;", Assignment.class).notIn(new AssignmentAndAssignmentOfSameValue());
  }

  @Test public void assignmentReturn0() {
    topDownTrimming("a=3;return a;")//
        .gives("return a=3;");
  }

  @Test public void assignmentReturn1() {
    topDownTrimming("a=3;return(a);")//
        .gives("return a=3;");
  }

  @Test public void assignmentReturn2() {
    topDownTrimming("a +=3;return a;")//
        .gives("return a +=3;");
  }

  @Test public void assignmentReturn3() {
    topDownTrimming("a *=3;return a;")//
        .gives("return a *=3;");
  }

  @Test public void assignmentReturniNo() {
    topDownTrimming("b=a=3;return a;")//
        .stays();
  }

  @Test public void blockSimplifyVanilla() {
    topDownTrimming("if(a){f();}")//
        .gives("if(a)f();");
  }

  @Test public void blockSimplifyVanillaSimplified() {
    topDownTrimming("{f();}")//
        .gives("f();");
  }

  @Test public void booleanChangeValueOfToConstant() {
    topDownTrimming("Boolean b=Boolean.valueOf(true);")//
        .gives("Boolean.valueOf(true);")//
        .stays();
  }

  @Test public void booleanChangeValueOfToConstantNotConstant() {
    topDownTrimming("Boolean.valueOf(expected);")//
        .stays();
  }

  @Test public void bugInLastIfInMethod() {
    topDownTrimming("@Override public void messageFinished(final LocalMessage myMessage, final int __, final int ofTotal){ "
        + "  if(!isMessageSuppressed(myMessage)){ " + //
        "  final List<LocalMessage> messages=new ArrayList<LocalMessage>(); messages.add(myMessage); "
        + "  stats.unreadMessageCount +=myMessage.isSet(Flag.SEEN)? 0 : 1; "
        + "  stats.flaggedMessageCount +=myMessage.isSet(Flag.FLAGGED)? 1 : 0; if(listener !=null) "
        + "  listener.listLocalMessagesAddMessages(account, null, messages); } }")//
            .gives(
                "@Override public void messageFinished(final LocalMessage myMessage,final int __,final int ofTotal){if(isMessageSuppressed(myMessage))return;final List<LocalMessage>messages=new ArrayList<LocalMessage>();messages.add(myMessage);stats.unreadMessageCount+=myMessage.isSet(Flag.SEEN)?0:1;stats.flaggedMessageCount+=myMessage.isSet(Flag.FLAGGED)?1:0;if(listener!=null)listener.listLocalMessagesAddMessages(account,null,messages);}");
  }

  @Test public void bugInLastIfInMethod2() {
    topDownTrimming("public void f(){  if(!g(message)){   final List<LocalMessage> messages=new ArrayList<LocalMessage>(); messages.add(message); "
        + "  stats.unreadMessageCount +=message.isSet(Flag.SEEN)? 0 : 1; "
        + "  stats.flaggedMessageCount +=message.isSet(Flag.FLAGGED)? 1 : 0; if(listener !=null) "
        + "  listener.listLocalMessagesAddMessages(account, null, messages); } }")//
            .gives(
                "public void f(){if(g(message))return;final List<LocalMessage>messages=new ArrayList<LocalMessage>();messages.add(message);stats.unreadMessageCount+=message.isSet(Flag.SEEN)?0:1;stats.flaggedMessageCount+=message.isSet(Flag.FLAGGED)?1:0;if(listener!=null)listener.listLocalMessagesAddMessages(account,null,messages);}");
  }

  @Test public void bugInLastIfInMethod3() {
    topDownTrimming("public void f(){  if(!g(a)){   final List<LocalMessage> messages=new ArrayList<LocalMessage>(); messages.add(message); "
        + "  stats.unreadMessageCount +=message.isSet(Flag.SEEN)? 0 : 1; "
        + "  stats.flaggedMessageCount +=message.isSet(Flag.FLAGGED)? 1 : 0; if(listener !=null) "
        + "  listener.listLocalMessagesAddMessages(account, null, messages); } }")//
            .gives(
                "public void f(){if(g(a))return;final List<LocalMessage>messages=new ArrayList<LocalMessage>();messages.add(message);stats.unreadMessageCount+=message.isSet(Flag.SEEN)?0:1;stats.flaggedMessageCount+=message.isSet(Flag.FLAGGED)?1:0;if(listener!=null)listener.listLocalMessagesAddMessages(account,null,messages);}");
  }

  @Test public void bugInLastIfInMethod4() {
    topDownTrimming("public void f(){  if(!g){   final List<LocalMessage> messages=new ArrayList<LocalMessage>(); messages.add(message); "
        + "  stats.unreadMessageCount +=message.isSet(Flag.SEEN)? 0 : 1; "
        + "  stats.flaggedMessageCount +=message.isSet(Flag.FLAGGED)? 1 : 0; if(listener !=null) "
        + "  listener.listLocalMessagesAddMessages(account, null, messages); } }")//
            .gives(
                "public void f(){if(g)return;final List<LocalMessage>messages=new ArrayList<LocalMessage>();messages.add(message);stats.unreadMessageCount+=message.isSet(Flag.SEEN)?0:1;stats.flaggedMessageCount+=message.isSet(Flag.FLAGGED)?1:0;if(listener!=null)listener.listLocalMessagesAddMessages(account,null,messages);}");
  }

  @Test public void bugInLastIfInMethod5() {
    topDownTrimming("public void f(){  if(!g){   final List<LocalMessage> messages=new ArrayList<LocalMessage>(); messages2.add(message); "
        + "  stats.unreadMessageCount +=message.isSet(Flag.SEEN)? 0 : 1;   stats.flaggedMessageCount +=message.isSet(Flag.FLAGGED)? 1 : 0; } }")//
            .gives(
                "public void f(){if(g)return;final List<LocalMessage>messages=new ArrayList<LocalMessage>();messages2.add(message);stats.unreadMessageCount+=message.isSet(Flag.SEEN)?0:1;stats.flaggedMessageCount+=message.isSet(Flag.FLAGGED)?1:0;}");
  }

  @Test public void bugInLastIfInMethod6() {
    topDownTrimming("public void f(){  if(!g){  final int messages=3; "
        + "  messages2.add(message); stats.unreadMessageCount +=message.isSet(Flag.SEEN)? 0 : 1; "
        + "  stats.flaggedMessageCount +=message.isSet(Flag.FLAGGED)? 1 : 0; } }")//
            .gives(
                "public void f(){if(g)return;final int messages=3;messages2.add(message);stats.unreadMessageCount+=message.isSet(Flag.SEEN)?0:1;stats.flaggedMessageCount+=message.isSet(Flag.FLAGGED)?1:0;}");
  }

  @Test public void bugInLastIfInMethod7() {
    topDownTrimming("public void f(){ " + //
        "  if(!g){  foo(); " + //
        "  bar(); } }")
            //
            .gives("public void f(){if(g)return;foo();bar();}");
  }

  @Test public void bugIntroducingMISSINGWord1() {
    topDownTrimming("b.f(a)&& -1==As.g(f).h(c)? o(s, b, g(f)): !b.f(\".in\")? null : y(d, b)? null : o(b.z(u, variableDeclarationFragment), s, f)")
        //
        .gives("b.f(a)&& As.g(f).h(c)==-1 ? o(s,b,g(f)): b.f(\".in\")&& !y(d,b)? o(b.z(u,variableDeclarationFragment),s,f): null");
  }

  @Test public void bugIntroducingMISSINGWord1a() {
    topDownTrimming("-1==As.g(f).h(c)")//
        .gives("As.g(f).h(c)==-1");
  }

  @Test public void bugIntroducingMISSINGWord1b() {
    topDownTrimming("b.f(a)&& X ? o(s, b, g(f)): !b.f(\".in\")? null : y(d, b)? null : o(b.z(u, variableDeclarationFragment), s, f)")
        //
        .gives("b.f(a)&&X?o(s,b,g(f)):b.f(\".in\")&&!y(d,b)?o(b.z(u,variableDeclarationFragment),s,f):null");
  }

  @Test public void bugIntroducingMISSINGWord1c() {
    topDownTrimming("Y ? o(s, b, g(f)): !b.f(\".in\")? null : y(d, b)? null : o(b.z(u, variableDeclarationFragment), s, f)")
        .gives("Y?o(s,b,g(f)):b.f(\".in\")&&!y(d,b)?o(b.z(u,variableDeclarationFragment),s,f):null");
  }

  @Test public void bugIntroducingMISSINGWord1d() {
    topDownTrimming("Y ? Z : !b.f(\".in\")? null : y(d, b)? null : o(b.z(u, variableDeclarationFragment), s, f)")
        .gives("Y?Z:b.f(\".in\")&&!y(d,b)?o(b.z(u,variableDeclarationFragment),s,f):null");
  }

  @Test public void bugIntroducingMISSINGWord1e() {
    topDownTrimming("Y ? Z : R ? null : S ? null : T")//
        .gives("Y?Z:!R&&!S?T:null");
  }

  @Test public void bugIntroducingMISSINGWord2() {
    topDownTrimming(
        "name.endsWith(testSuffix)&& MakeAST.stringBuilder(f).indexOf(testKeyword)==2? objects(s, name, makeInFile(f)): !name.endsWith(\".in\")? null : dotOutExists(d, name)? null : objects(name.replaceAll(\"\\\\.in$\", Z2), s, f)")
            .gives(
                "name.endsWith(testSuffix)&&MakeAST.stringBuilder(f).indexOf(testKeyword)==2?objects(s,name,makeInFile(f)):name.endsWith(\".in\")&&!dotOutExists(d,name)?objects(name.replaceAll(\"\\\\.in$\",Z2),s,f):null");
  }

  @Test public void bugIntroducingMISSINGWord2a() {
    topDownTrimming(
        "name.endsWith(testSuffix)&& MakeAST.stringBuilder(f).indexOf(testKeyword)==2? objects(s, name, makeInFile(f)): !name.endsWith(\".in\")? null : dotOutExists(d, name)? null : objects(name.replaceAll(\"\\\\.in$\", Z2), s, f)")
            .gives(
                "name.endsWith(testSuffix)&&MakeAST.stringBuilder(f).indexOf(testKeyword)==2?objects(s,name,makeInFile(f)):name.endsWith(\".in\")&&!dotOutExists(d,name)?objects(name.replaceAll(\"\\\\.in$\",Z2),s,f):null");
  }

  @Test public void bugIntroducingMISSINGWord2b() {
    topDownTrimming(
        "name.endsWith(testSuffix)&& T ? objects(s, name, makeInFile(f)): !name.endsWith(\".in\")? null : dotOutExists(d, name)? null : objects(name.replaceAll(\"\\\\.in$\", Z2), s, f)")
            .gives(
                "name.endsWith(testSuffix)&& T ? objects(s,name,makeInFile(f)): name.endsWith(\".in\")&& !dotOutExists(d,name)?objects(name.replaceAll(\"\\\\.in$\",Z2),s,f):null");
  }

  @Test public void bugIntroducingMISSINGWord2c() {
    topDownTrimming(
        "X && T ? objects(s, name, makeInFile(f)): !name.endsWith(\".in\")? null : dotOutExists(d, name)? null : objects(name.replaceAll(\"\\\\.in$\", Z2), s, f)")
            .gives(
                "X && T ? objects(s,name,makeInFile(f)): name.endsWith(\".in\")&& !dotOutExists(d,name)?objects(name.replaceAll(\"\\\\.in$\",Z2),s,f):null");
  }

  @Test public void bugIntroducingMISSINGWord2d() {
    topDownTrimming("X && T ? E : Y ? null : dotOutExists(d, name)? null : objects(name.replaceAll(\"\\\\.in$\", Z2), s, f)")
        .gives("X && T ? E : !Y && !dotOutExists(d,name)? objects(name.replaceAll(\"\\\\.in$\",Z2),s,f): null");
  }

  @Test public void bugIntroducingMISSINGWord2e() {
    topDownTrimming("X && T ? E : Y ? null : Z ? null : objects(name.replaceAll(\"\\\\.in$\", Z2), s, f)")
        .gives("X && T ? E : !Y && !Z ? objects(name.replaceAll(\"\\\\.in$\",Z2),s,f): null");
  }

  @Test public void bugIntroducingMISSINGWord2e1() {
    topDownTrimming("X && T ? E : Y ? null : Z ? null : objects(name.replaceAll(x, Z2), s, f)")
        .gives("X && T ? E : !Y && !Z ? objects(name.replaceAll(x,Z2),s,f): null");
  }

  @Test public void bugIntroducingMISSINGWord2e2() {
    topDownTrimming("X && T ? E : Y ? null : Z ? null : objects(name.replaceAll(g, Z2), s, f)")
        .gives("X && T ? E : !Y && !Z ? objects(name.replaceAll(g,Z2),s,f): null");
  }

  @Test public void bugIntroducingMISSINGWord2f() {
    topDownTrimming("X && T ? E : Y ? null : Z ? null : F")//
        .gives("X&&T?E:!Y&&!Z?F:null");
  }

  @Test public void bugIntroducingMISSINGWord3() {
    topDownTrimming(
        "name.endsWith(testSuffix)&& -1==MakeAST.stringBuilder(f).indexOf(testKeyword)? objects(s, name, makeInFile(f)): !name.endsWith(x)? null : dotOutExists(d, name)? null : objects(name.replaceAll(3, 56), s, f)")
            .gives(
                "name.endsWith(testSuffix)&&MakeAST.stringBuilder(f).indexOf(testKeyword)==-1?objects(s,name,makeInFile(f)):name.endsWith(x)&&!dotOutExists(d,name)?objects(name.replaceAll(3,56),s,f):null");
  }

  @Test public void bugIntroducingMISSINGWord3a() {
    topDownTrimming("!name.endsWith(x)? null : dotOutExists(d, name)? null : objects(name.replaceAll(3, 56), s, f)")
        .gives("name.endsWith(x)&&!dotOutExists(d,name)?objects(name.replaceAll(3,56),s,f):null");
  }

  @Test public void bugIntroducingMISSINGWordTry1() {
    topDownTrimming(
        "name.endsWith(testSuffix)&& -1==MakeAST.stringBuilder(f).indexOf(testKeyword)? objects(s, name, makeInFile(f)): !name.endsWith(\".in\")? null : dotOutExists(d, name)? null : objects(name.replaceAll(\"\\\\.in$\", Z2), s, f)")
            .gives(
                "name.endsWith(testSuffix)&& MakeAST.stringBuilder(f).indexOf(testKeyword)==-1?objects(s,name,makeInFile(f)):name.endsWith(\".in\")&&!dotOutExists(d,name)?objects(name.replaceAll(\"\\\\.in$\",Z2),s,f):null");
  }

  @Test public void bugIntroducingMISSINGWordTry2() {
    topDownTrimming("!(intent.getBooleanExtra(EXTRA_FROM_SHORTCUT, false)&& !K9.FOLDER_NONE.equals(mAccount.getAutoExpandFolderName()))")
        .gives("!intent.getBooleanExtra(EXTRA_FROM_SHORTCUT,false)||K9.FOLDER_NONE.equals(mAccount.getAutoExpandFolderName())");
  }

  @Test public void bugIntroducingMISSINGWordTry3() {
    topDownTrimming("!(f.g(X, false)&& !a.b.e(m.h()))")//
        .gives("!f.g(X,false)||a.b.e(m.h())");
  }

  @Test public void bugOfMissingTry() {
    topDownTrimming("!(A && B && C && true && D)")//
        .gives("!A||!B||!C||false||!D");
  }

  @Test public void canonicalFragementExample1() {
    topDownTrimming("int a;a=3;")//
        .using(VariableDeclarationFragment.class, new LocalUnintializedAssignmentToSame()) //
        .gives("int a=3;");
  }

  @Test public void canonicalFragementExample2() {
    topDownTrimming("int a=2;if(b)a=3;")//
        .gives("int a=b ? 3 : 2;");
  }

  @Test public void canonicalFragementExample3() {
    topDownTrimming("int a=2;a +=3;")//
        .gives("int a=2 + 3;");
  }

  @Test public void canonicalFragementExample4() {
    topDownTrimming("int a=2;a=3 * a;")//
        .gives("int a=3 * 2;");
  }

  @Test public void canonicalFragementExample5() {
    topDownTrimming("int a=2;return 3 * a;")//
        .gives("return 3 * 2;");
  }

  @Test public void canonicalFragementExample6() {
    topDownTrimming("int a=2;return a;")//
        .gives("return 2;");
  }

  @Test public void canonicalFragementExamples() {
    topDownTrimming("int a;a=3;")//
        .gives("int a=3;");
    topDownTrimming("int a=2;if(b)a=3;")//
        .gives("int a=b ? 3 : 2;");
    topDownTrimming("int a=2;a +=3;")//
        .gives("int a=2 + 3;");
    topDownTrimming("int a=2;a=3 * a;")//
        .gives("int a=3 * 2;");
    topDownTrimming("int a=2;return 3 * a;")//
        .gives("return 3 * 2;");
    topDownTrimming("int a=2;return a;")//
        .gives("return 2;");
  }

  @Test public void canonicalFragementExamplesWithExraFragments() {
    topDownTrimming("int a=2;a=3 * a * b;")//
        .gives("int a=3 * 2 * b;");
    topDownTrimming("int a=2;a=3 * a;")//
        .gives("int a=3 * 2;");
    topDownTrimming("int a=2;a +=3;")//
        .gives("int a=2 + 3;");
    topDownTrimming("int a=2;a +=b;")//
        .gives("int a=2 + b;");
    topDownTrimming("int a=2, b=11;a=3 * a * b;")//
        .gives("int a=2;a=3*a*11;")//
        .gives("int a=3*2*11;");//
    topDownTrimming("int a=2, b=1;a +=b;")//
        .gives("int a=2;a+=1;")//
        .gives("int a=2+1;");
    topDownTrimming("int a=2,b=1;if(b)a=3;")//
        .gives("int a=2;if(1)a=3;")//
        .gives("int a=1?3:2;");
    topDownTrimming("int a=2, b=1;return a + 3 * b;")//
        .gives("int b=1;return 2+3*b;");
    topDownTrimming("int a=2, b;a=3 * a * b;")//
        .gives("int a=2, b;a *=3 * b;")//
        .stays();
    topDownTrimming("int a=2, b;a +=b;")//
        .stays();
    topDownTrimming("int a=2, b;return a + 3 * b;")//
        .gives("return 2 + 3*b;");
    topDownTrimming("int a=2;if(x)a=3*a;")//
        .gives("int a=x?3*2:2;");
    topDownTrimming("int a=2;return 3 * a * a;")//
        .gives("return 3 * 2 * 2;");
    topDownTrimming("int a=2;return 3 * a * b;")//
        .gives("return 3 * 2 * b;");
    topDownTrimming("int a=2;return a;")//
        .gives("return 2;");
    topDownTrimming("int a,b=2;a=b;")//
        .gives("int a;a=2;")//
        .gives("int a=2;");
    topDownTrimming("int a;if(x)a=3;else a++;")//
        .gives("int a;if(x)a=3;else++a;");
    topDownTrimming("int b=5,a=2,c=4;return 3 * a * b * c;")//
        .gives("int a=2,c=4;return 3*a*5*c;");
    topDownTrimming("int b=5,a=2,c;return 3 * a * b * c;")//
        .gives("int a=2;return 3 * a * 5 * c;");
  }

  @Test public void canonicalFragementExamplesWithExraFragmentsX() {
    topDownTrimming("int a;if(x)a=3;else a++;")//
        .gives("int a;if(x)a=3;else++a;");
  }

  @Test public void chainComparison() {
    azzert.that(right(i("a==true==b==c")) + "", is("c"));
    topDownTrimming("a==true==b==c")//
        .gives("a==b==c");
  }

  @Test public void chainCOmparisonTrueLast() {
    topDownTrimming("a==b==c==true")//
        .gives("a==b==c");
  }

  @Test public void comaprisonWithBoolean1() {
    topDownTrimming("s.equals(532)==true")//
        .gives("s.equals(532)");
  }

  @Test public void comaprisonWithBoolean2() {
    topDownTrimming("s.equals(532)==false ")//
        .gives("!s.equals(532)");
  }

  @Test public void comaprisonWithBoolean3() {
    topDownTrimming("(false==s.equals(532))")//
        .gives("(!s.equals(532))");
  }

  @Test public void comaprisonWithSpecific0() {
    topDownTrimming("this !=a")//
        .gives("a !=this");
  }

  @Test public void comaprisonWithSpecific0Legibiliy00() {
    final InfixExpression e = i("this !=a");
    assert in(e.getOperator(), Operator.EQUALS, Operator.NOT_EQUALS);
    assert !iz.booleanLiteral(right(e));
    assert !iz.booleanLiteral(left(e));
    assert in(e.getOperator(), Operator.EQUALS, Operator.NOT_EQUALS);
  }

  @Test public void comaprisonWithSpecific1() {
    topDownTrimming("null !=a")//
        .gives("a !=null");
  }

  @Test public void comaprisonWithSpecific2() {
    topDownTrimming("null !=a")//
        .gives("a !=null");
    topDownTrimming("this==a")//
        .gives("a==this");
    topDownTrimming("null==a")//
        .gives("a==null");
    topDownTrimming("this>=a")//
        .gives("a<=this");
    topDownTrimming("null>=a")//
        .gives("a<=null");
    topDownTrimming("this<=a")//
        .gives("a>=this");
    topDownTrimming("null<=a")//
        .gives("a>=null");
  }

  @Test public void comaprisonWithSpecific2a() {
    topDownTrimming("s.equals(532)==false")//
        .gives("!s.equals(532)");
  }

  @Test public void comaprisonWithSpecific3() {
    topDownTrimming("(this==s.equals(532))")//
        .gives("(s.equals(532)==this)");
  }

  @Test public void comaprisonWithSpecific4() {
    topDownTrimming("(0<a)")//
        .gives("(a>0)");
  }

  @Test public void comaprisonWithSpecificInParenthesis() {
    topDownTrimming("(null==a)")//
        .gives("(a==null)");
  }

  @Test public void commonPrefixIfBranchesInFor() {
    topDownTrimming("for(;;)if(a){i++;j++;j++;} else { i++;j++;i++;}")//
        .gives("for(;;){i++;j++;if(a)j++;else i++;}");
  }

  @Test public void commonSuffixIfBranches() {
    topDownTrimming("if(a){  ++i; f(); } else { ++j; f(); }").gives("if(a) ++i; else  ++j;  f();");
  }

  @Test public void commonSuffixIfBranchesDisappearingElse() {
    topDownTrimming("if(a){  ++i; f(); } else { f(); }")//
        .gives("if(a) ++i;  f();");
  }

  @Test public void commonSuffixIfBranchesDisappearingThen() {
    topDownTrimming("if(a){  f(); } else { ++j; f(); }")//
        .gives("if(!a) ++j;  f();");
  }

  @Test public void commonSuffixIfBranchesDisappearingThenWithinIf() {
    topDownTrimming("if(x)if(a){  f(); } else { ++j; f(); } else { h();++i;++j;++k;if(a)f();else g();}")
        .gives("if(x){ if(!a) ++j;  f();} else { h();++i;++j;++k;if(a)f();else g();}");
  }

  @Test public void compareWithBoolean00() {
    topDownTrimming("a==true")//
        .gives("a");
  }

  @Test public void compareWithBoolean01() {
    topDownTrimming("a==false")//
        .gives("!a");
  }

  @Test public void compareWithBoolean10() {
    topDownTrimming("true==a")//
        .gives("a");
  }

  @Test public void compareWithBoolean100() {
    topDownTrimming("a !=true")//
        .gives("!a");
  }

  @Test public void compareWithBoolean100a() {
    topDownTrimming("(((a)))!=true")//
        .gives("!a");
  }

  @Test public void compareWithBoolean101() {
    topDownTrimming("a !=false")//
        .gives("a");
  }

  @Test public void compareWithBoolean11() {
    topDownTrimming("false==a")//
        .gives("!a");
  }

  @Test public void compareWithBoolean110() {
    topDownTrimming("true !=a")//
        .gives("!a");
  }

  @Test public void compareWithBoolean111() {
    topDownTrimming("false !=a")//
        .gives("a");
  }

  @Test public void compareWithBoolean2() {
    topDownTrimming("false !=false")//
        .gives("false");
  }

  @Test public void compareWithBoolean3() {
    topDownTrimming("false !=true")//
        .gives("true");
  }

  @Test public void compareWithBoolean4() {
    topDownTrimming("false==false")//
        .gives("true");
  }

  @Test public void compareWithBoolean5() {
    topDownTrimming("false==true")//
        .gives("false");
  }

  @Test public void compareWithBoolean6() {
    topDownTrimming("false !=false")//
        .gives("false");
  }

  @Test public void compareWithBoolean7() {
    topDownTrimming("true !=true")//
        .gives("false");
  }

  @Test public void compareWithBoolean8() {
    topDownTrimming("true !=false")//
        .gives("true");
  }

  @Test public void compareWithBoolean9() {
    topDownTrimming("true !=true")//
        .gives("false");
  }

  @Test public void comparison01() {
    topDownTrimming("1+2+3<3")//
        .gives("6<3")//
        .stays();
  }

  @Test public void comparison02() {
    topDownTrimming("f(2)<a")//
        .stays();
  }

  @Test public void comparison03() {
    topDownTrimming("this==null")//
        .stays();
  }

  @Test public void comparison04() {
    topDownTrimming("6-7<2+1")//
        .gives("-1<3");
  }

  @Test public void comparison05() {
    topDownTrimming("a==11")//
        .stays();
  }

  @Test public void comparison06() {
    topDownTrimming("1<102333")//
        .stays();
  }

  @Test public void comparison08() {
    topDownTrimming("a==this")//
        .stays();
  }

  @Test public void comparison09() {
    topDownTrimming("1+2<3&7+4>2+1")//
        .gives("3<3&11>3");
  }

  @Test public void comparison11() {
    topDownTrimming("12==this")//
        .gives("this==12");
  }

  @Test public void comparison12() {
    topDownTrimming("1+2<3&7+4>2+1||6-7<2+1")//
        .gives("3<3&11>3||-1<3")//
        .stays();
  }

  @Test public void comparison13() {
    topDownTrimming("13455643294<22")//
        .stays();
  }

  @Test public void comparisonWithCharacterConstant() {
    topDownTrimming("'d'==s.charAt(i)")//
        .gives("s.charAt(i)=='d'");
  }

  @Test public void compreaeExpressionToExpression() {
    topDownTrimming("6 - 7<2 + 1 ")//
        .gives("-1<3");
  }

  @Test public void correctSubstitutionInIfAssignment() {
    topDownTrimming("int a=2+3;if(a+b> a<<b)a=(((((a *7<<a)))));")//
        .gives("int a=2+3+b>2+3<<b?(2+3)*7<<2+3:2+3;");
  }

  @Test public void delcartionIfAssignmentNotPlain() {
    topDownTrimming("int a=0;if(y)a+=3;")//
        .gives("int a=y ? 0 + 3 : 0;");
  }

  @Test public void doNotConsolidateNewArrayActual() {
    topDownTrimming("occupied=new boolean[capacity]; placeholder=new boolean[capacity];")//
        .stays();
  }

  @Test public void doNotConsolidateNewArraySimplifiedl() {
    topDownTrimming("a=new int[1]; b=new int[1];")//
        .stays();
  }

  @Test public void doNotConsolidatePlainNew() {
    topDownTrimming("a=new A(); b=new B();")//
        .stays();
  }

  @Test public void doNotInlineDeclarationWithAnnotationSimplified() {
    topDownTrimming("@SuppressWarnings int $=(Class<T>)findClass(className);return $;}")//
        .stays();
  }

  @Test public void doNotInlineWithDeclaration() {
    topDownTrimming("private Class<? extends T> retrieveClazz()throws ClassNotFoundException { nonnull(className); "
        + " @SuppressWarnings(\"unchecked\")final Class<T> $=(Class<T>)findClass(className);return $;}")//
            .stays();
  }

  @Test public void doNotIntroduceDoubleNegation() {
    topDownTrimming("!Y ? null :!Z ? null : F")//
        .gives("Y&&Z?F:null");
  }

  @Test public void donotSorMixedTypes() {
    topDownTrimming("if(2 * 3.1415 * 180> a || tipper.concat(sS)==1922 && tipper.length()> 3)return c> 5;")
        .gives("if(1130.94> a || tipper.concat(sS)==1922 && tipper.length()> 3)return c> 5;");
  }

  @Test public void dontELiminateCatchBlock() {
    topDownTrimming("try { f();} catch(Exception e){ } finally {}")//
        .gives("try { f();} catch(Exception e){ }");
  }

  @Test public void dontSimplifyCatchBlock() {
    topDownTrimming("try { {} ;{} } catch(Exception e){{} ;{} } finally {{} ;{}}").gives("try {}  catch(Exception e){}  finally {}");
  }

  @Test public void duplicatePartialIfBranches() {
    topDownTrimming("if(a){ f();g();++i;} else { f();g();  --i;}")//
        .gives("f();g();if(a)++i;else  --i;");
  }

  @Test public void eliminateSwitch() {
    topDownTrimming("switch(a){ default: } int x=5;f(x++);")//
        .gives("int x=5;f(x++);");
  }

  @Test public void emptyElse() {
    topDownTrimming("if(x)b=3;else ;")//
        .gives("if(x)b=3;");
  }

  @Test public void emptyElseBlock() {
    topDownTrimming("if(x)b=3;else { ;}")//
        .gives("if(x)b=3;");
  }

  @Test public void emptyIsNotChangedExpression() {
    topDownTrimming("")//
        .stays();
  }

  @Test public void emptyIsNotChangedStatement() {
    topDownTrimming("")//
        .stays();
  }

  @Test public void emptyThen1() {
    topDownTrimming("if(b);else x();")//
        .gives("if(!b)x();");
  }

  @Test public void emptyThen2() {
    topDownTrimming("if(b){;;} else {x();}")//
        .gives("if(!b)x();");
  }

  @Test public void factorOutAnd() {
    topDownTrimming("(a || b)&&(a || c)")//
        .gives("a || b && c");
  }

  @Test public void factorOutOr() {
    topDownTrimming("a && b || a && c")//
        .gives("a &&(b || c)");
  }

  @Test public void factorOutOr3() {
    topDownTrimming("a && b && x && f()|| a && c && y ")//
        .gives("a &&(b && x && f()|| c && y)");
  }

  @Test public void forLoopBug() {
    topDownTrimming("for(int i=0;i<s.length();++i)if(s.charAt(i)=='a')u +=2;else if(s.charAt(i)=='d')u -=1;return u;if(b)i=3;")//
        .gives("for(int ¢=0;¢<s.length();++¢)if(s.charAt(¢)=='a')u +=2;else if(s.charAt(¢)=='d')u-=1;return u;")//
        .gives("for(int ¢=0;¢<s.length();++¢)if(s.charAt(¢)=='a')u +=2;else if(s.charAt(¢)=='d')--u ;return u;")//
        .stays();
  }

  /** Introduced by Yogi on Tue-Apr-11-10:06:26-IDT-2017 (code automatically in
   * class 'JUnitTestMethodFacotry') */
  @Test public void return22aTrue() {
    topDownTrimming("return (2 > 2 + a) == true;") //
        .using(InfixExpression.class, new InfixComparisonBooleanLiteral()) //
        .gives("return 2>2+a;") //
        .using(InfixExpression.class, new InfixComparisonSpecific()) //
        .gives("return 2+a<2;") //
        .using(InfixExpression.class, new InfixAdditionSort()) //
        .gives("return a+2<2;") //
        .using(InfixExpression.class, new InfixSimplifyComparisionOfAdditions()) //
        .gives("return a<2-2;") //
        .using(InfixExpression.class, new InfixSubtractionEvaluate()) //
        .gives("return a<0;") //
        .stays() //
    ;
  }

  @Test public void ifBarFooElseBazFooExtractDefinedSuffix() {
    topDownTrimming("public static void f(){ int i=0;if(f()){ i +=1;System.h('!');System.h('!');  ++i;} else { i +=2;System.h('@');System.h('@');++i;} }")//
        .gives("public static void f(){ int i=0;if(f()){ i +=1;System.h('!');System.h('!');  } else { i +=2;System.h('@');System.h('@');} ++i;}");
  }

  @Test public void ifBarFooElseBazFooExtractUndefinedSuffix() {
    topDownTrimming("public final static final void f(){ if(tr()){ int i=0;System.h(i + 0);++i;  } else { int i=1;System.h(i * 1);++i;} }");
  }

  @Test public void ifBugSecondTry() {
    topDownTrimming("final int c=2;if(c==c + 1){ if(c==c + 2)return null;c=f().charAt(3);  } else if(Character.digit(c, 16)==-1)return null;return null;")
        .gives("final int c=2;if(c !=c + 1){ if(Character.digit(c, 16)==-1)return null; "
            + " } else { if(c==c + 2)return null;c=f().charAt(3);}  return null;");
  }

  @Test public void ifBugSimplified() {
    topDownTrimming("if(x){ if(z)return null;c=f().charAt(3);} else if(y)return; ")
        .gives("if(!x){ if(y)return;} else { if(z)return null;  c=f().charAt(3);} ");
  }

  @Test public void ifBugWithPlainEmptyElse() {
    topDownTrimming("if(z)f();else  ; ")//
        .gives("if(z)f(); ");
  }

  @Test public void ifDegenerateThenInIf() {
    topDownTrimming("if(a)if(b){} else f();x();")//
        .gives("if(a)if(!b)f();x();");
  }

  @Test public void ifEmptyElsewWithinIf() {
    topDownTrimming("if(a)if(b){;;;f();} else {}")//
        .gives("if(a&&b){;;;f();}");
  }

  @Test public void ifEmptyThenThrow() {
    topDownTrimming("if(b){ /* empty */} else { throw new Excpetion(); }")//
        .gives("if(!b)throw new Excpetion();");
  }

  @Test public void ifEmptyThenThrowVariant() {
    topDownTrimming("if(b){ /* empty */; } // no else\n   throw new Exception(); ")//
        .gives("throw new Exception();")//
        .stays();
  }

  @Test public void ifEmptyThenThrowVariant1() {
    topDownTrimming("if(b){;} throw new Exception(); ")//
        .gives("throw new Exception();")//
        .stays()//
    ;
  }

  @Test public void ifEmptyThenThrowWitinIf() {
    topDownTrimming("if(x)if(b){ /* empty */} else { throw new Excpetion(); } else { f();f();f();f();f();f();f();f();}")
        .gives("if(x){if(!b)throw new Excpetion();}else{f();f();f();f();f();f();f();f();}")//
        .stays();
  }

  @Test public void ifFunctionCall() {
    topDownTrimming("if(x)f(a);else f(b);")//
        .gives("f(x ? a: b);");
  }

  @Test public void ifPlusPlusPost() {
    topDownTrimming("if(x)a++;else b++;")//
        .gives("if(x)++a;else++b;");
  }

  @Test public void ifPlusPlusPostExpression() {
    topDownTrimming("x? a++:b++")//
        .stays();
  }

  @Test public void ifPlusPlusPre() {
    topDownTrimming("if(x)++a;else ++b;")//
        .stays();
  }

  @Test public void ifPlusPlusPreExpression() {
    topDownTrimming("x? ++a:++b")//
        .stays();
  }

  @Test public void ifSequencerNoElseSequencer00() {
    topDownTrimming("for(;;){if(a)return;break;}a=3;")//
        .stays();
  }

  @Test public void ifSequencerNoElseSequencer01() {
    topDownTrimming("if(a)throw e;break;")//
        .stays();
  }

  @Test public void ifSequencerNoElseSequencer02() {
    topDownTrimming("if(a)break;break;")//
        .gives("break;");
  }

  @Test public void ifSequencerNoElseSequencer03() {
    topDownTrimming("if(a)continue;break;")//
        .stays();
  }

  @Test public void ifSequencerNoElseSequencer04() {
    topDownTrimming("if(a)break;return 0;")//
        .gives("if(!a)return 0;break;");
  }

  @Test public void ifSequencerNoElseSequencer04a() {
    topDownTrimming("for(;;){if(a)break;return;} a=3;")//
        .gives("for(;;){if(!a)return;break;}a=3;")//
        .stays();
  }

  @Test public void ifSequencerNoElseSequencer05() {
    topDownTrimming("for(;;)if(a){x();return;} a=2;")//
        .stays();
  }

  @Test public void ifSequencerNoElseSequencer05a() {
    topDownTrimming("for(;;){ if(a){x();return;} continue;a=3;}")//
        .gives("for(;;){ if(a){x();return;} continue;}")//
        .stays();
  }

  @Test public void ifSequencerNoElseSequencer05aa() {
    topDownTrimming("if(a){x();return a;} continue;a=3;")//
        .gives("if(a){x();return a;} continue;")//
        .stays();
  }

  @Test public void ifSequencerNoElseSequencer05ab() {
    topDownTrimming("synchronized(a){ if(a){x();return;} a=3;}")//
        .stays();
  }

  @Test public void ifSequencerNoElseSequencer06() {
    topDownTrimming("if(a)throw e;break;")//
        .stays();
  }

  @Test public void ifSequencerNoElseSequencer07() {
    topDownTrimming("if(a)break;throw e;")//
        .gives("if(!a)throw e;break;");
  }

  @Test public void ifSequencerNoElseSequencer08() {
    topDownTrimming("if(a)throw e;continue;")//
        .stays();
  }

  @Test public void ifSequencerNoElseSequencer09() {
    topDownTrimming("if(a)break;throw e;")//
        .gives("if(!a)throw e;break;");
  }

  @Test public void ifSequencerNoElseSequencer10() {
    topDownTrimming("if(a)continue;return 0;")//
        .gives("if(!a)return 0;continue;");
  }

  @Test public void ifSequencerThenSequencer0() {
    topDownTrimming("if(a)return 4;else break;")//
        .gives("if(a)return 4;break;");
  }

  @Test public void ifSequencerThenSequencer1() {
    topDownTrimming("if(a)break;else return 2;")//
        .gives("if(!a)return 2;break;");
  }

  @Test public void ifSequencerThenSequencer3() {
    topDownTrimming("if(a)return 10;else continue;")//
        .gives("if(a)return 10;continue;");
  }

  @Test public void ifSequencerThenSequencer4() {
    topDownTrimming("if(a)continue;else return 2;")//
        .gives("if(!a)return 2;continue;");
  }

  @Test public void ifSequencerThenSequencer5() {
    topDownTrimming("if(a)throw e;else break;")//
        .gives("if(a)throw e;break;");
  }

  @Test public void ifSequencerThenSequencer6() {
    topDownTrimming("if(a)break;else throw e;")//
        .gives("if(!a)throw e;break;");
  }

  @Test public void ifSequencerThenSequencer7() {
    topDownTrimming("if(a)throw e;else continue;")//
        .gives("if(a)throw e;continue;");
  }

  @Test public void ifSequencerThenSequencer8() {
    topDownTrimming("if(a)break;else throw e;")//
        .gives("if(!a)throw e;break;");
  }

  @Test public void ifThrowFooElseThrowBar() {
    topDownTrimming("if(a)throw foo;else throw bar;")//
        .gives("throw a ? foo : bar;");
  }

  @Test public void ifThrowNoElseThrow() {
    topDownTrimming("if(!(e.getCause()instanceof Error))throw e; throw(Error)e.getCause();")
        .gives("throw !(e.getCause()instanceof Error)?e:(Error)e.getCause();");
  }

  @Test public void ifWithCommonNotInBlock() {
    topDownTrimming("for(;;)if(a){i++;j++;f();} else { i++;j++;g();}")//
        .gives("for(;;){i++;j++;if(a)f();else g();}");
  }

  @Test public void ifWithCommonNotInBlockDegenerate() {
    topDownTrimming("for(;;)if(a){i++;f();} else { i++;j++;}")//
        .gives("for(;;){i++;if(a)f();else j++;}");
  }

  @Test public void ifWithCommonNotInBlockiLongerElse() {
    topDownTrimming("for(;;)if(a){i++;j++;f();} else { i++;j++;f();h();}")//
        .gives("for(;;){i++;j++;f();if(!a)h();}");
  }

  @Test public void ifWithCommonNotInBlockiLongerThen() {
    topDownTrimming("for(;;)if(a){i++;j++;f();} else { i++;j++;}")//
        .gives("for(;;){i++;j++;if(a)f();}");
  }

  @Test public void ifWithCommonNotInBlockNothingLeft() {
    topDownTrimming("for(;;)if(a){i++;j++;} else { i++;j++;}")//
        .gives("for(;;){i++;j++;}");
  }

  @Test public void infiniteLoopBug1() {
    topDownTrimming("static boolean hasAnnotation(final VariableDeclarationFragment zet){ "
        + " return hasAnnotation((VariableDeclarationStatement)f.getParent()); }")//
            .stays();
  }

  @Test public void infiniteLoopBug2() {
    topDownTrimming("static boolean hasAnnotation(final VariableDeclarationStatement n, int abcd){ return hasAnnotation(n.modifiers());  }")
        .gives("static boolean hasAnnotation(final VariableDeclarationStatement s, int abcd){  return hasAnnotation(s.modifiers());}");
  }

  @Test public void infiniteLoopBug3() {
    topDownTrimming("boolean f(final VariableDeclarationStatement n){ return false;}")
        .gives("boolean f(final VariableDeclarationStatement s){ return false;}");
  }

  @Test public void infiniteLoopBug4() {
    topDownTrimming("void f(final VariableDeclarationStatement n){}")//
        .gives("void f(final VariableDeclarationStatement s){ }");
  }

  @Test public void initializer101() {
    topDownTrimming("int a=b;return a;")//
        .gives("return b;")//
        .stays();
  }

  @Test public void inline01() {
    topDownTrimming("public int y(){ final Z u=new Z(6);S.h(u.j);return u;} ").gives("public int y(){ final Z $=new Z(6);S.h($.j);return $;} ");
  }

  @Test public void inlineInitializers() {
    topDownTrimming("int b,a=2;return 3 * a * b;")//
        .gives("return 3*2*b;");
  }

  @Test public void inlineInitializersFirstStep() {
    topDownTrimming("int b=4,a=2;return 3 * a * b;")//
        .gives("int a=2;return 3*a*4;");
  }

  /** START OF STABLING TESTS */
  @Test public void inlineintoInstanceCreation() {
    topDownTrimming("public Statement methodBlock(FrameworkMethod m){ final Statement statement=methodBlock(m); "
        + " return new Statement(){ public void evaluate()throws Throwable { try {  statement.evaluate(); "
        + "  handleDataPointSuccess();} catch(AssumptionViolatedException e){  handleAssumptionViolation(e); "
        + " } catch(Throwable e){  reportParameterizedError(e, complete.getArgumentStrings(nullsOk()));}  } }; }")
            .gives("public Statement methodBlock(FrameworkMethod m){ final Statement $=methodBlock(m);return new Statement(){ "
                + " public void evaluate()throws Throwable { try {  $.evaluate(); "
                + "  handleDataPointSuccess();} catch(AssumptionViolatedException e){   handleAssumptionViolation(e);} catch(Throwable e){ "
                + "  reportParameterizedError(e, complete.getArgumentStrings(nullsOk()));} } }; }");
  }

  @Test public void inlineintoNextStatementWithSideEffects() {
    topDownTrimming("int a=f();if(a)g(a);else h(u(a));")//
        .stays();
  }

  @Test public void inlineSingleUse07() {
    topDownTrimming(
        " final Collection<Integer> outdated=new ArrayList<>();int x=6, y=7;S.h(x+y);final Collection<Integer> coes=new ArrayList<>();for(final Integer pi : coes){ if(pi.intValue()<x - y)outdated.add(pi);command();} S.h(coes.size());")
            .stays();
  }

  @Test public void inlineSingleUseKillingVariable() {
    topDownTrimming("int a,b=2;a=b;")//
        .gives("int a;a=2;");
  }

  @Test public void inlineSingleUseKillingVariables() {
    topDownTrimming("int $, xi=0, xj=0, yi=0, yj=0;if(xi> xj==yi> yj)$++;else $--;").gives("int $, xj=0, yi=0, yj=0;if(0>xj==yi>yj)$++;else $--;");
  }

  @Test public void inlineSingleUseKillingVariablesSimplified() {
    topDownTrimming("int $=1,xi=0,xj=0,yi=0,yj=0;if(xi> xj==yi> yj)$++;else $--;").gives("int $=1,xj=0,yi=0,yj=0;if(0>xj==yi>yj)$++;else $--;")//
        .gives("int $=1,yi=0,yj=0; if(0>0==yi>yj)$++;else $--;")//
        .gives("int $=1,yj=0;  if(0>0==0>yj)$++;else $--;")//
        .gives("int $=1;  if(0>0==0>0)$++;else $--;")//
        .gives("int $=1;  if(0>0==0>0)++$;else--$;");
  }

  @Test public void inlineSingleUseTrivial() {
    topDownTrimming("int $=1,yj=0;  if(0>0==yj<0)++$;else--$;")//
        .gives("int $=1;  if(0>0==0<0)++$;else--$;");
  }

  @Test public void inlineSingleUseVanilla() {
    topDownTrimming("int a=f();if(a)f();")//
        .gives("if(f())f();");
  }

  @Test public void inlineSingleUseWithAssignment() {
    topDownTrimming("int a=2;while(true)if(f())f(a);else a=2;")//
        .gives("for(int a=2;true;)if(f())f(a);else a=2;")//
        .gives("for(int a=2;;)if(f())f(a);else a=2;")//
        .stays();
  }

  @Test public void inlineSingleVariableintoPlusPlus() {
    topDownTrimming("int $=0;if(a)++$;else --$;")//
        .stays();
  }

  @Test public void inliningWithVariableAssignedTo() {
    topDownTrimming("int a=3,b=5;if(a==4)if(b==3)b=2;else{b=a;b=3;}else if(b==3)b=2;else{b=a*a;b=3;}")
        .gives("int b=5;if(3==4)if(b==3)b=2;else{b=3;b=3;}else if(b==3)b=2;else{b=3*3;b=3;}");
  }

  @Test public void isGreaterTrue() {
    final InfixExpression e = i("f(a,b,c,d,e)* f(a,b,c)");
    assert e != null;
    azzert.that(right(e) + "", is("f(a,b,c)"));
    azzert.that(left(e) + "", is("f(a,b,c,d,e)"));
    final Tipper<InfixExpression> s = Toolbox.defaultInstance().firstTipper(e);
    assert s != null;
    azzert.that(s, instanceOf(InfixMultiplicationSort.class));
    assert s.check(e);
    final Expression e1 = left(e), e2 = right(e);
    assert !hasNull(e1, e2);
    assert count.nodes(e1) > count.nodes(e2) + NODES_THRESHOLD;
    assert moreArguments(e1, e2);
    assert longerFirst(e);
    assert s.check(e) : "e=" + e + " s=" + s;
    final ASTNode replacement = ((ReplaceCurrentNode<InfixExpression>) s).replacement(e);
    assert replacement != null;
    azzert.that(replacement + "", is("f(a,b,c) * f(a,b,c,d,e)"));
  }

  @Test public void isGreaterTrueButAlmostNot() {
    final InfixExpression e = i("f(a,b,c,d)* f(a,b,c)");
    assert e != null;
    azzert.that(right(e) + "", is("f(a,b,c)"));
    azzert.that(left(e) + "", is("f(a,b,c,d)"));
    final Tipper<InfixExpression> s = Toolbox.defaultInstance().firstTipper(e);
    assert s != null;
    azzert.that(s, instanceOf(InfixMultiplicationSort.class));
    assert s.check(e);
    final Expression e1 = left(e), e2 = right(e);
    assert !hasNull(e1, e2);
    assert count.nodes(e1) <= count.nodes(e2) + NODES_THRESHOLD;
    assert moreArguments(e1, e2);
    assert longerFirst(e);
    assert s.check(e) : "e=" + e + " s=" + s;
    final ASTNode replacement = ((ReplaceCurrentNode<InfixExpression>) s).replacement(e);
    assert replacement != null;
    azzert.that(replacement + "", is("f(a,b,c) * f(a,b,c,d)"));
  }

  @Test public void issue06() {
    topDownTrimming("a*-b")//
        .gives("-a * b");
  }

  @Test public void issue06B() {
    topDownTrimming("x/a*-b/-c*- - - d / -d")//
        .gives("x/a * b/ c * d/d")//
        .gives("d*x/a*b/c/d");
  }

  @Test public void issue06C1() {
    topDownTrimming("a*-b/-c*- - - d / d")//
        .gives("-a * b/ c * d/d");
  }

  @Test public void issue06C4() {
    topDownTrimming("-a * b/ c ")//
        .stays();
  }

  @Test public void issue06D() {
    topDownTrimming("a*b*c*d*-e")//
        .gives("-a*b*c*d*e")//
        .stays();
  }

  @Test public void issue06E() {
    topDownTrimming("-a*b*c*d*f*g*h*i*j*k")//
        .stays();
  }

  @Test public void issue06F() {
    topDownTrimming("x*a*-b*-c*- - - d * d")//
        .gives("-x*a*b*c*d*d")//
        .stays();
  }

  @Test public void issue06G() {
    topDownTrimming("x*a*-b*-c*- - - d / d")//
        .gives("-x*a*b*c*d/d")//
        .stays();
  }

  @Test public void issue06H() {
    topDownTrimming("x/a*-b/-c*- - - d ")//
        .gives("-x/a * b/ c * d");
  }

  @Test public void issue06I() {
    topDownTrimming("41 * - 19")//
        .gives("-779 ");
  }

  @Test public void issue06J() {
    topDownTrimming("41 * a * - 19")//
        .gives("-41*a*19")//
        .gives("-41*19*a");
  }

  @Test public void issue110_01() {
    topDownTrimming("polite ? \"Eat your meal.\" : \"Eat your meal, please\"")//
        .gives("\"Eat your meal\" +(polite ? \".\" : \", please\")");
  }

  @Test public void issue110_02() {
    topDownTrimming("polite ? \"Eat your meal.\" : \"Eat your meal\"")//
        .gives("\"Eat your meal\" +(polite ? \".\" : \"\")");
  }

  @Test public void issue110_03() {
    topDownTrimming("polite ? \"thanks for the meal\" : \"I hated the meal\"")//
        .gives("!polite ? \"I hated the meal\": \"thanks for the meal\"")//
        .gives("(!polite ? \"I hated\" : \"thanks for\" )+ \" the meal\"");
  }

  @Test public void issue110_04() {
    topDownTrimming("polite ? \"thanks.\" : \"I hated the meal.\"")//
        .gives("(polite ? \"thanks\" : \"I hated the meal\")+\".\"");
  }

  @Test public void issue110_05() {
    topDownTrimming("a ? \"abracadabra\" : \"abba\"")//
        .gives("!a ? \"abba\" : \"abracadabra\"")//
        .stays();
  }

  @Test public void issue110_06() {
    topDownTrimming("receiver==null ? \"Use \" + \"x\" : \"Use \" + receiver")//
        .gives("\"Use \"+(receiver==null ? \"x\" : receiver)")//
        .stays();
  }

  @Test public void issue110_07() {
    topDownTrimming("receiver==null ? \"Use x\" : \"Use \" + receiver")//
        .gives("\"Use \"+(receiver==null ? \"x\" : \"\"+receiver)");
  }

  @Test public void issue110_08() {
    topDownTrimming("receiver==null ? \"Use\" : receiver + \"Use\"")//
        .gives("(receiver==null ? \"\" : receiver+\"\")+ \"Use\"")//
        .stays();
  }

  @Test public void issue110_09() {
    topDownTrimming("receiver==null ? \"user a\" : receiver + \"something a\"")//
        .gives("(receiver==null ? \"user\" : receiver+\"something\")+ \" a\"")//
        .stays();
  }

  @Test public void issue110_10() {
    topDownTrimming("receiver==null ? \"Something Use\" : \"Something\" + receiver + \"Use\"")
        .gives("\"Something\"+(receiver==null ? \" Use\" : \"\"+receiver + \"Use\")")
        .gives("\"Something\"+((receiver==null ? \" \" : \"\"+receiver+\"\")+ \"Use\")");
  }

  @Test public void issue110_11() {
    topDownTrimming("f()? \"first\" + d()+ \"second\" : \"first\" + g()+ \"third\"")
        .gives("\"first\" +(f()? \"\" + d()+ \"second\" : \"\" + g()+ \"third\")");
  }

  @Test public void issue110_12() {
    topDownTrimming("f()? \"first\" + d()+ \"second\" : \"third\" + g()+ \"second\"")
        .gives("(f()? \"first\" + d()+ \"\": \"third\" + g()+\"\")+ \"second\"");
  }

  @Test public void issue110_13() {
    topDownTrimming("f()? \"first is:\" + d()+ \"second\" : \"first are:\" + g()+ \"and second\"")
        .gives("\"first \" +(f()? \"is:\" + d()+ \"second\": \"are:\" + g()+ \"and second\")")
        .gives("\"first \" +((f()? \"is:\" + d()+ \"\": \"are:\" + g()+ \"and \")+ \"second\")");
  }

  @Test public void issue110_14() {
    topDownTrimming("x==null ? \"Use isEmpty()\" : \"Use \" + x + \".isEmpty()\"").gives("\"Use \" +(x==null ? \"isEmpty()\" : \"\"+x + \".isEmpty()\")")
        .gives("\"Use \" +((x==null ? \"\" : \"\"+ x + \".\")+\"isEmpty()\")");
  }

  @Test public void issue110_15() {
    topDownTrimming("$.setName(b.simpleName(booleanLiteral ? \"TRU\" : \"TALS\"));")//
        .stays();
  }

  @Test public void issue110_16() {
    topDownTrimming("$.setName(b.simpleName(booleanLiteral ? \"TRUE\" : \"FALSE\"));")//
        .stays();
  }

  @Test public void issue110_17() {
    topDownTrimming("$.setName(b.simpleName(booleanLiteral ? \"TRUE Story\" : \"FALSE Story\"));")
        .gives("$.setName(b.simpleName((booleanLiteral ? \"TRUE\" : \"FALSE\")+\" Story\"));");
  }

  @Test public void issue110_18() {
    topDownTrimming("booleanLiteral==0 ? \"asss\" : \"assfad\"")//
        .stays();
  }

  @Test public void issue21a() {
    topDownTrimming("a.equals(\"a\")")//
        .gives("\"a\".equals(a)");
  }

  @Test public void issue21b() {
    topDownTrimming("a.equals(\"ab\")")//
        .gives("\"ab\".equals(a)");
  }

  @Test public void issue21d() {
    topDownTrimming("a.equalsIgnoreCase(\"a\")")//
        .gives("\"a\".equalsIgnoreCase(a)");
  }

  @Test public void issue21e() {
    topDownTrimming("a.equalsIgnoreCase(\"ab\")")//
        .gives("\"ab\".equalsIgnoreCase(a)");
  }

  @Test public void issue37Simplified() {
    topDownTrimming("int a=3;a=31 * a;")//
        .gives("int a=31 * 3;");
  }

  @Test public void issue37SimplifiedVariant() {
    topDownTrimming("int a=3;a +=31 * a;")//
        .gives("int a=3+31*3;");
  }

  @Test public void issue37WithSimplifiedBlock() {
    topDownTrimming("if(a){ {} ;if(b)f();{} } else { g();f();++i;++j;}")//
        .gives("if(a){ if(b)f();} else { g();f();++i;++j;}");
  }

  @Test public void issue38() {
    topDownTrimming("return o==null ? null : o==CONDITIONAL_AND ? CONDITIONAL_OR    : o==CONDITIONAL_OR ? CONDITIONAL_AND   : null;")//
        .stays();
  }

  @Test public void issue38Simplfiied() {
    topDownTrimming(" o==CONDITIONAL_AND ? CONDITIONAL_OR   : o==CONDITIONAL_OR ? CONDITIONAL_AND   : null").stays();
  }

  @Test public void issue39base() {
    topDownTrimming("if(name==null){ if(other.name !=null)return false; } else if(!name.equals(other.name))  return false; return true;")//
        .stays();
  }

  public void issue39baseDual() {
    topDownTrimming("if(name !=null){ if(!name.equals(other.name))return false; } else if(other.name !=null)  return false; return true;")
        .gives("if(name==null){ if(other.name !=null)return false; } else if(!name.equals(other.name))  return false; return true;");
  }

  @Test(timeout = 100) public void issue39versionA() {
    topDownTrimming("if(varArgs){ if(argumentTypes.length<parameterTypes.length - 1){ return false;} "
        + "} else if(parameterTypes.length !=argumentTypes.length){ return false; }")
            .gives("if(!varArgs){ if(parameterTypes.length !=argumentTypes.length){ return false;} "
                + "} else if(argumentTypes.length<parameterTypes.length - 1){ return false; }");
  }

  public void issue39versionAdual() {
    topDownTrimming("if(!varArgs){ if(parameterTypes.length !=argumentTypes.length){ return false;} "
        + "} else if(argumentTypes.length<parameterTypes.length - 1){ return false; }")//
            .stays();
  }

  @Test public void issue41FunctionCall() {
    topDownTrimming("int a=f();a +=2;")//
        .gives("int a=f()+2;");
  }

  @Test public void issue43() {
    topDownTrimming("String tipper=Z2;tipper=tipper.f(A).f(b)+ tipper.f(c);return(tipper + 3);")
        .gives("String tipper=Z2.f(A).f(b)+ Z2.f(c);return(tipper + 3);");
  }

  @Test public void issue46() {
    topDownTrimming("int f(){ x++;y++;if(a){ i++; j++; k++;} }")//
        .gives("int f(){++x;++y;if(!a)return;i++;j++;k++;}") //
        .gives("int f(){++x;++y;if(!a)return;++i;++j;++k;}") //
    ;
  }

  @Test public void issue49() {
    topDownTrimming("int g(){ int f=0;for(int i: X)$ +=f(i);return f;}")//
        .gives("int g(){ int f=0;for(int ¢: X)$ +=f(¢);return f;}")//
        .stays();
  }

  @Test public void issue51() {
    topDownTrimming("int f(){ int x=0;for(int i=0;i<10;++i)x +=i;return x;}").gives("int f(){ int $=0;for(int i=0;i<10;++i)$ +=i;return $;}")
        .gives("int f(){ int $=0;for(int ¢=0;¢<10;++¢)$ +=¢;return $;}")//
        .stays();
  }

  @Test public void issue51g() {
    topDownTrimming("abstract abstract interface a{}")//
        .gives("abstract interface a {}")//
        .gives("interface a {}")//
        .stays();
  }

  @Test public void issue53() {
    topDownTrimming("int[] is=f();for(int i: is)f(i);")//
        .gives("for(int i: f())f(i);")//
        .gives("for(int ¢: f())f(¢);")//
        .stays()//
    ;
  }

  @Test public void issue53a() {
    topDownTrimming("int f(){ int x=0;for(int i=0;i<10;++i)x +=i;return x;}").gives("int f(){ int $=0;for(int i=0;i<10;++i)$ +=i;return $;}");
  }

  @Test public void issue54DoNonSideEffect() {
    topDownTrimming("int a=f;do { b[i]=a;} while(b[i] !=a);")//
        .gives("do { b[i]=f;} while(b[i] !=f);");
  }

  @Test public void issue54DoNonSideEffectEmptyBody() {
    topDownTrimming("int a=f();do ;while(a !=1);")//
        .stays();
  }

  @Test public void issue54DoWhile() {
    topDownTrimming("int a=f();do { b[i]=2;++i;} while(b[i] !=a);")//
        .gives("int a=f();do { b[i++]=2;} while(b[i] !=a);");
  }

  @Test public void issue54DoWithBlock() {
    topDownTrimming("int a=f();do { b[i]=a;++i;} while(b[i] !=a);")//
        .gives("int a=f();do { b[i++]=a;} while(b[i] !=a);");
  }

  @Test public void issue54doWithoutBlock() {
    topDownTrimming("int a=f();do b[i]=a;while(b[i] !=a);")//
        .stays();
  }

  @Test public void issue54ForEnhanced() {
    topDownTrimming("int a=f();for(int i: a)b[i]=x;")//
        .gives("for(int i: f())b[i]=x;");
  }

  @Test public void issue54ForEnhancedNonSideEffectLoopHeader() {
    topDownTrimming("int a=f;for(int i: a)b[i]=b[i-1];")//
        .gives("for(int i: f)b[i]=b[i-1];");
  }

  @Test public void issue54ForEnhancedNonSideEffectWithBody() {
    topDownTrimming("int a=f;for(int i: j)b[i]=a;")//
        .gives("for(int i:j)b[i]=f;");
  }

  @Test public void issue54ForPlainNonSideEffect() {
    topDownTrimming("int a=f;for(int i=0;i<100;++i)b[i]=a;")//
        .gives("for(int i=0;i<100;++i)b[i]=f;");
  }

  @Test public void issue54ForPlainUseInConditionNonSideEffect() {
    topDownTrimming("int a=f;for(int i=0;a<100;++i)b[i]=3;")//
        .gives("for(int i=0;f<100;++i)b[i]=3;");
  }

  @Test public void issue54ForPlainUseInInitializerNonSideEffect() {
    topDownTrimming("int a=f;for(int i=a;i<100;i *=a)b[i]=3;")//
        .gives("for(int i=f;i<100;i *=f)b[i]=3;");
  }

  @Test public void issue54ForPlainUseInUpdatersNonSideEffect() {
    topDownTrimming("int a=f;for(int i=0;i<100;i *=a)b[i]=3;")//
        .gives("for(int i=0;i<100;i *=f)b[i]=3;");
  }

  @Test public void issue54WhileNonSideEffect() {
    topDownTrimming("int a=f;while(c)b[i]=a;")//
        .gives("while(c)b[i]=f;");
  }

  @Test public void issue54WhileScopeDoesNotInclude() {
    included("int a=f();while(c)b[i]=a;", VariableDeclarationFragment.class).notIn(new LocalVariableIntializedStatementTerminatingScope());
  }

  @Test public void issue62b_1() {
    topDownTrimming("int f(int ixx){ for(;ixx<100;ixx=ixx+1)if(false)return;return ixx;}")//
        .gives("int f(int ixx){ for(;ixx<100;ixx+=1){} return ixx;}");
  }

  @Test public void issue62c() {
    topDownTrimming("int f(int ixx){ while(++ixx> 999)if(ixx>99)break;return ixx;}")//
        .stays();
  }

  @Test public void issue64a() {
    topDownTrimming("void f(){ final int a=f();new Object(){ @Override public int hashCode(){ return a;} };}").stays();
  }

  @Test public void issue73a() {
    topDownTrimming("void foo(StringBuilder sb){}")//
        .gives("void foo(StringBuilder b){}");
  }

  @Test public void issue73b() {
    topDownTrimming("void foo(DataOutput dataOutput){}")//
        .gives("void foo(DataOutput o){}");
  }

  @Test public void linearTransformation() {
    topDownTrimming("plain * the + kludge")//
        .gives("the*plain+kludge");
  }

  @Test public void literalVsLiteral() {
    topDownTrimming("1<102333")//
        .stays();
  }

  @Test public void localAssignmentUpdateWithIncrement() {
    topDownTrimming("int a=0;a+=++a;")//
        .stays();
  }

  @Test public void localAssignmentUpdateWithPostIncrement() {
    topDownTrimming("int a=0;a+=a++;")//
        .stays();
  }

  @Test public void localAssignmentWithIncrement() {
    topDownTrimming("int a=0;a=++a;")//
        .stays();
  }

  @Test public void localAssignmentWithPostIncrement() {
    topDownTrimming("int a=0;a=a++;")//
        .stays();
  }

  @Test public void localIfAssignment() {
    topDownTrimming("String u=s;if(s.equals(y))u=s + blah;S.h(u);").gives("String u=s.equals(y)? s + blah :s;S.h(u);");
  }

  @Test public void localIfAssignment3() {
    topDownTrimming("int a=2;if(a !=2)a=3;")//
        .gives("int a=2 !=2 ? 3 : 2;");
  }

  @Test public void localIfAssignment4() {
    topDownTrimming("int a=2;if(x)a=2*a;")//
        .gives("int a=x ? 2*2: 2;");
  }

  @Test public void localIfUpdateAssignment() {
    topDownTrimming("String u=s;if(s.equals(y))u +=s + blah;S.h(u);").gives("String u=s.equals(y)? s +(s + blah):s;S.h(u);");
  }

  @Test public void localIfUsesLaterVariable() {
    topDownTrimming("int a=0, b=0;if(b==3)a=4;")//
        .gives("int a=0;if(0==3)a=4;")//
        .gives("int a=0==3?4:0;");
  }

  @Test public void localInitializeRightShift() {
    topDownTrimming("int a=3;a>>=2;")//
        .gives("int a=3>> 2;");
  }

  @Test public void localInitializerReturnAssignment() {
    topDownTrimming("int a=3;return a=2 * a;")//
        .gives("return 2 * 3;");
  }

  @Test public void localInitializerReturnExpression() {
    topDownTrimming("String tipper=Bob + Wants + To + \"Sleep \";return(right_now + tipper);").gives("return(right_now+(Bob+Wants+To+\"Sleep \"));");
  }

  @Test public void localInitializesRotate() {
    topDownTrimming("int a=3;a>>>=2;")//
        .gives("int a=3>>> 2;");
  }

  @Test public void localInitializeUpdateAnd() {
    topDownTrimming("int a=3;a&=2;")//
        .gives("int a=3 & 2;");
  }

  @Test public void localInitializeUpdateAssignment() {
    topDownTrimming("int a=3;a +=2;")//
        .gives("int a=3+2;");
  }

  @Test public void localInitializeUpdateAssignmentFunctionCallWithReuse() {
    topDownTrimming("int a=f();a +=2*f();")//
        .gives("int a=f()+2*f();");
  }

  @Test public void localInitializeUpdateAssignmentFunctionCallWIthReuse() {
    topDownTrimming("int a=x;a +=a + 2*f();")//
        .gives("int a=x+(x+2*f());");
  }

  @Test public void localInitializeUpdateAssignmentIncrement() {
    topDownTrimming("int a=++i;a +=j;")//
        .gives("int a=++i + j;");
  }

  @Test public void localInitializeUpdateAssignmentIncrementTwice() {
    topDownTrimming("int a=++i;a +=a + j;")//
        .stays();
  }

  @Test public void localInitializeUpdateAssignmentWithReuse() {
    topDownTrimming("int a=3;a +=2*a;")//
        .gives("int a=3+2*3;");
  }

  @Test public void localInitializeUpdateDividies() {
    topDownTrimming("int a=3;a/=2;")//
        .gives("int a=3 / 2;");
  }

  @Test public void localInitializeUpdateLeftShift() {
    topDownTrimming("int a=3;a<<=2;")//
        .gives("int a=3<<2;");
  }

  @Test public void localInitializeUpdateMinus() {
    topDownTrimming("int a=3;a-=2;")//
        .gives("int a=3 - 2;");
  }

  @Test public void localInitializeUpdateModulo() {
    topDownTrimming("int a=3;a%=2;")//
        .gives("int a=3 % 2;");
  }

  @Test public void localInitializeUpdatePlus() {
    topDownTrimming("int a=3;a+=2;")//
        .gives("int a=3 + 2;");
  }

  @Test public void localInitializeUpdateTimes() {
    topDownTrimming("int a=3;a*=2;")//
        .gives("int a=3 * 2;");
  }

  @Test public void localInitializeUpdateXor() {
    topDownTrimming("int a=3;a^=2;")//
        .gives("int a=3 ^ 2;");
  }

  @Test public void localInitializeUpdatOr() {
    topDownTrimming("int a=3;a|=2;")//
        .gives("int a=3 | 2;");
  }

  @Test public void localUpdateReturn() {
    topDownTrimming("int a=3;return a +=2;")//
        .gives("return 3 + 2;");
  }

  @Test public void localUpdateReturnNone() {
    topDownTrimming("int a=f();return a +=2 * a;")//
        .stays();
  }

  @Test public void localUpdateReturnTwice() {
    topDownTrimming("int a=3;return a +=2 * a;")//
        .gives("return 3 + 2 *3 ;");
  }

  @Test public void longChainComparison() {
    topDownTrimming("a==b==c==d")//
        .stays();
  }

  @Test public void longChainParenthesisComparison() {
    topDownTrimming("(a==b==c)==d")//
        .stays();
  }

  @Test public void longChainParenthesisNotComparison() {
    topDownTrimming("(a==b==c)!=d")//
        .stays();
  }

  @Test public void longerChainParenthesisComparison() {
    topDownTrimming("(a==b==c==d==e)==d")//
        .stays();
  }

  /* @Test public void massiveInlining(){
   * trimmingOf("int a,b,c;String tipper=zE4;if(2 * 3.1415 * 180> a || tipper.concat(sS)==1922 && tipper.length()> 3)return c> 5;"
   * )
   * .gives("int a,b,c;if(2 * 3.1415 * 180>a||zE4.concat(sS)==1922&&zE4.length()>3)return c>5;"
   * );} */
  @Test public void methodWithLastIf() {
    topDownTrimming("int f(){ if(a){ f();g();h();}}")//
        .gives("int f(){ if(!a)return;f();g();h();}");
  }

  @Test public void nestedIf1() {
    topDownTrimming("if(a)if(b)i++;")//
        .gives("if(a && b)i++;");
  }

  @Test public void nestedIf2() {
    topDownTrimming("if(a)if(b)i++;else ;else ;")//
        .gives("if(a && b)i++;else ;");
  }

  @Test public void nestedIf3() {
    topDownTrimming("if(x)if(a)if(b)i++;else ;else ;else { y++;f();g();z();}")//
        .gives("if(x)if(a&&b)i++;else;else{++y;f();g();z();}");
  }

  @Test public void nestedIf33() {
    topDownTrimming("if(x){if(a&&b)i++;else;}else{++y;f();g();}")//
        .gives("if(x)if(a&&b)i++;else;else{++y;f();g();}")//
        .gives("if(x){if(a&&b)i++;}else{++y;f();g();}")//
        .gives("if(x){if(a&&b)++i;}else{++y;f();g();}");
  }

  @Test public void nestedIf33a() {
    topDownTrimming("if(x){ if(a && b)i++;} else { y++;f();g();}")//
        .gives("if(x){if(a&&b)++i;} else{++y;f();g();}");
  }

  @Test public void nestedIf33b() {
    topDownTrimming("if(x)if(a && b)i++;else;else { y++;f();g();}")//
        .gives("if(x){if(a&&b)i++;}else{++y;f();g();}");
  }

  @Test public void nestedIf3c() {
    topDownTrimming("if(x)if(a && b)i++;else;else { y++;f();g();}")//
        .gives("if(x){if(a&&b)i++;} else {++y;f();g();}");
  }

  @Test public void nestedIf3d() {
    topDownTrimming("if(x)if(a)if(b)i++;else ;else ;else { y++;f();g();z();}")//
        .gives("if(x)if(a&&b)i++;else;else{++y;f();g();z();}")//
        .gives("if(x){if(a&&b)i++;} else{++y;f();g();z();}")//
        .gives("if(x){if(a&&b)++i;} else{++y;f();g();z();}");
  }

  @Test public void nestedIf3e() {
    topDownTrimming("if(x)if(a)if(b)i++;else ;else ;else { y++;f();g();z();}")//
        .gives("if(x)if(a&&b)i++;else;else{++y;f();g();z();}")//
        .gives("if(x){if(a&&b)i++;}else{++y;f();g();z();}");
  }

  @Test public void nestedIf3f() {
    topDownTrimming("if(x){if(a&&b)i++;else;}else{++y;f();g();}")//
        .gives("if(x)if(a&&b)i++;else;else{++y;f();g();}")//
        .gives("if(x){if(a&&b)i++;}else{++y;f();g();}");
  }

  @Test public void nestedIf3f1() {
    topDownTrimming("if(x)if(a&&b)i++;else;else{++y;f();g();}")//
        .gives("if(x){if(a&&b)i++;}else{++y;f();g();}");
  }

  @Test public void nestedIf3x() {
    topDownTrimming("if(x)if(a)if(b)i++;else ;else ;else { y++;f();g();z();}")//
        .gives("if(x)if(a&&b)i++;else;else{++y;f();g();z();}")//
        .gives("if(x){if(a&&b)i++;}else{++y;f();g();z();}");
  }

  /* @Test public void nestedTernaryAlignment(){
   * trimmingOf("int b=3==4?5==3?2:3:5==3?2:3*3;")//
   * .gives("int b=3==4?5==3?2:3:5!=3?3*3:2;");} */
  @Test public void noChange() {
    topDownTrimming("12")//
        .stays();
    topDownTrimming("true")//
        .stays();
    topDownTrimming("null")//
        .stays();
    topDownTrimming("on*of*no*notion*notion")//
        .gives("no*of*on*notion*notion");
  }

  @Test public void noChange0() {
    topDownTrimming("kludge + the * plain ")//
        .stays();
  }

  @Test public void noChange1() {
    topDownTrimming("the * plain")//
        .stays();
  }

  @Test public void noChange2() {
    topDownTrimming("plain + kludge")//
        .stays();
  }

  @Test public void noChangeA() {
    topDownTrimming("true")//
        .stays();
  }

  @Test public void noinliningintoSynchronizedStatement() {
    topDownTrimming("int a=f();synchronized(this){ int b=a;f(++b);}")//
        .stays();
  }

  @Test public void noinliningintoSynchronizedStatementEvenWithoutSideEffect() {
    topDownTrimming("int a=f;synchronized(this){ int b=a;f(++b);}")//
        .stays();
  }

  @Test public void noinliningintoTryStatement() {
    topDownTrimming("int a=f();try { int b=a;b(++b);} catch(Exception E){}")//
        .stays();
  }

  @Test public void noinliningintoTryStatementEvenWithoutSideEffect() {
    topDownTrimming("int a=f;try { int b=a;f(++b);} catch(Exception E){}")//
        .stays();
  }

  @Test public void notOfAnd() {
    topDownTrimming("!(A && B)")//
        .gives("!A || !B");
  }

  @Test public void oneMultiplication() {
    topDownTrimming("f(a,b,c,d)* f(a,b,c)")//
        .gives("f(a,b,c)* f(a,b,c,d)");
  }

  @Test public void oneMultiplicationAlternate() {
    topDownTrimming("f(a,b,c,d,e)* f(a,b,c)")//
        .gives("f(a,b,c)* f(a,b,c,d,e)");
  }

  @Test public void orFalse3ORTRUE() {
    topDownTrimming("false || false || false")//
        .gives("false");
  }

  @Test public void orFalse4ORTRUE() {
    topDownTrimming("false || false || false || false")//
        .gives("false");
  }

  @Test public void orFalseANDOf3WithoutBoolean() {
    topDownTrimming("a && b && false")//
        .stays();
  }

  @Test public void orFalseANDOf3WithoutBooleanA() {
    topDownTrimming("x && a && b")//
        .stays();
  }

  @Test public void orFalseANDOf3WithTrue() {
    topDownTrimming("true && x && true && a && b")//
        .gives("x && a && b");
  }

  @Test public void orFalseANDOf3WithTrueA() {
    topDownTrimming("a && b && true")//
        .gives("a && b");
  }

  @Test public void orFalseANDOf4WithoutBoolean() {
    topDownTrimming("a && b && c && false")//
        .stays();
  }

  @Test public void orFalseANDOf4WithoutBooleanA() {
    topDownTrimming("x && a && b && c")//
        .stays();
  }

  @Test public void orFalseANDOf4WithTrue() {
    topDownTrimming("x && true && a && b && c")//
        .gives("x && a && b && c");
  }

  @Test public void orFalseANDOf4WithTrueA() {
    topDownTrimming("a && b && c && true")//
        .gives("a && b && c");
  }

  @Test public void orFalseANDOf5WithoutBoolean() {
    topDownTrimming("false && a && b && c && d")//
        .stays();
  }

  @Test public void orFalseANDOf5WithoutBooleanA() {
    topDownTrimming("x && a && b && c && d")//
        .stays();
  }

  @Test public void orFalseANDOf5WithTrue() {
    topDownTrimming("x && a && b && c && true && true && true && d")//
        .gives("x && a && b && c && d");
  }

  @Test public void orFalseANDOf5WithTrueA() {
    topDownTrimming("true && a && b && c && d")//
        .gives("a && b && c && d");
  }

  @Test public void orFalseANDOf6WithoutBoolean() {
    topDownTrimming("a && b && c && false && d && e")//
        .stays();
  }

  @Test public void orFalseANDOf6WithoutBooleanA() {
    topDownTrimming("x && a && b && c && d && e")//
        .stays();
  }

  @Test public void orFalseANDOf6WithoutBooleanWithParenthesis() {
    topDownTrimming("(x &&(a && b))&&(c &&(d && e))")//
        .stays();
  }

  @Test public void orFalseANDOf6WithTrue() {
    topDownTrimming("x && a && true && b && c && d && e")//
        .gives("x && a && b && c && d && e");
  }

  @Test public void orFalseANDOf6WithTrueA() {
    topDownTrimming("a && b && c && true && d && e")//
        .gives("a && b && c && d && e");
  }

  @Test public void orFalseANDOf6WithTrueWithParenthesis() {
    topDownTrimming("x &&(true &&(a && b && true))&&(c &&(d && e))")//
        .gives("x && a && b && c && d && e");
  }

  @Test public void orFalseANDOf7WithMultipleTrueValue() {
    topDownTrimming("(a &&(b && true))&&(c &&(d &&(e &&(true && true))))")//
        .gives("a &&b &&c &&d &&e ");
  }

  @Test public void orFalseANDOf7WithoutBooleanAndMultipleFalseValue() {
    topDownTrimming("(a &&(b && false))&&(c &&(d &&(e &&(false && false))))")//
        .stays();
  }

  @Test public void orFalseANDOf7WithoutBooleanWithParenthesis() {
    topDownTrimming("(a && b)&&(c &&(d &&(e && false)))")//
        .stays();
  }

  @Test public void orFalseANDOf7WithTrueWithParenthesis() {
    topDownTrimming("true &&(a && b)&&(c &&(d &&(e && true)))")//
        .gives("a &&b &&c &&d &&e ");
  }

  @Test public void orFalseANDWithFalse() {
    topDownTrimming("b && a")//
        .stays();
  }

  @Test public void orFalseANDWithoutBoolean() {
    topDownTrimming("b && a")//
        .stays();
  }

  @Test public void orFalseANDWithTrue() {
    topDownTrimming("true && b && a")//
        .gives("b && a");
  }

  @Test public void orFalseFalseOrFalse() {
    topDownTrimming("false ||false")//
        .gives("false");
  }

  @Test public void orFalseORFalseWithSomething() {
    topDownTrimming("true || a")//
        .stays();
  }

  @Test public void orFalseORFalseWithSomethingB() {
    topDownTrimming("false || a || false")//
        .gives("a");
  }

  @Test public void orFalseOROf3WithFalse() {
    topDownTrimming("x || false || b")//
        .gives("x || b");
  }

  @Test public void orFalseOROf3WithFalseB() {
    topDownTrimming("false || a || b || false")//
        .gives("a || b");
  }

  @Test public void orFalseOROf3WithoutBoolean() {
    topDownTrimming("a || b")//
        .stays();
  }

  @Test public void orFalseOROf3WithoutBooleanA() {
    topDownTrimming("x || a || b")//
        .stays();
  }

  @Test public void orFalseOROf4WithFalse() {
    topDownTrimming("x || a || b || c || false")//
        .gives("x || a || b || c");
  }

  @Test public void orFalseOROf4WithFalseB() {
    topDownTrimming("a || b || false || c")//
        .gives("a || b || c");
  }

  @Test public void orFalseOROf4WithoutBoolean() {
    topDownTrimming("a || b || c")//
        .stays();
  }

  @Test public void orFalseOROf4WithoutBooleanA() {
    topDownTrimming("x || a || b || c")//
        .stays();
  }

  @Test public void orFalseOROf5WithFalse() {
    topDownTrimming("x || a || false || c || d")//
        .gives("x || a || c || d");
  }

  @Test public void orFalseOROf5WithFalseB() {
    topDownTrimming("a || b || c || d || false")//
        .gives("a || b || c || d");
  }

  @Test public void orFalseOROf5WithoutBoolean() {
    topDownTrimming("a || b || c || d")//
        .stays();
  }

  @Test public void orFalseOROf5WithoutBooleanA() {
    topDownTrimming("x || a || b || c || d")//
        .stays();
  }

  @Test public void orFalseOROf6WithFalse() {
    topDownTrimming("false || x || a || b || c || d || e")//
        .gives("x || a || b || c || d || e");
  }

  @Test public void orFalseOROf6WithFalseWithParenthesis() {
    topDownTrimming("x ||(a ||(false)|| b)||(c ||(d || e))")//
        .gives("x || a || b || c || d || e");
  }

  @Test public void orFalseOROf6WithFalseWithParenthesisB() {
    topDownTrimming("(a || b)|| false ||(c || false ||(d || e || false))")//
        .gives("a || b || c || d || e");
  }

  @Test public void orFalseOROf6WithoutBoolean() {
    topDownTrimming("a || b || c || d || e")//
        .stays();
  }

  @Test public void orFalseOROf6WithoutBooleanA() {
    topDownTrimming("x || a || b || c || d || e")//
        .stays();
  }

  @Test public void orFalseOROf6WithoutBooleanWithParenthesis() {
    topDownTrimming("(a || b)||(c ||(d || e))")//
        .stays();
  }

  @Test public void orFalseOROf6WithoutBooleanWithParenthesisA() {
    topDownTrimming("x ||(a || b)||(c ||(d || e))")//
        .stays();
  }

  @Test public void orFalseOROf6WithTwoFalse() {
    topDownTrimming("a || false || b || false || c || d || e")//
        .gives("a || b || c || d || e");
  }

  @Test public void orFalseORSomethingWithFalse() {
    topDownTrimming("false || a || false")//
        .gives("a");
  }

  @Test public void orFalseORSomethingWithTrue() {
    topDownTrimming("a || true")//
        .stays();
  }

  @Test public void orFalseORWithoutBoolean() {
    topDownTrimming("b || a")//
        .stays();
  }

  @Test public void orFalseProductIsNotANDDivOR() {
    topDownTrimming("2*a")//
        .stays();
  }

  @Test public void orFalseTrueAndTrueA() {
    topDownTrimming("true && true")//
        .gives("true");
  }

  @Test public void overridenDeclaration() {
    topDownTrimming("int a=3;a=f()? 3 : 4;")//
        .gives("int a=f()? 3: 4;");
  }

  @Test public void paramAbbreviateBasic1() {
    topDownTrimming("void m(XMLDocument xmlDocument, int abcd){xmlDocument.exec(p);}")//
        .gives("void m(XMLDocument d, int abcd){d.exec(p);}");
  }

  @Test public void paramAbbreviateBasic2() {
    topDownTrimming("int m(StringBuilder builder, int abcd){if(builder.exec())builder.clear();")
        .gives("int m(StringBuilder b, int abcd){if(b.exec())b.clear();");
  }

  @Test public void paramAbbreviateCollision() {
    topDownTrimming("void m(Expression exp, Expression expresssion){ }")//
        .gives("void m(Expression x, Expression expresssion){ }");
  }

  @Test public void paramAbbreviateConflictingWithLocal1() {
    topDownTrimming("void m(String string){String s=null;string.substring(s, 2, 18);}").gives("void m(String string){string.substring(null,2,18);}");
  }

  @Test public void paramAbbreviateConflictingWithLocal1Simplified() {
    topDownTrimming("void m(String string){String s=X;string.substring(s, 2, 18);}").gives("void m(String string){string.substring(X,2,18);}");
  }

  @Test public void paramAbbreviateConflictingWithLocal1SimplifiedFurther() {
    topDownTrimming("void m(String string){String s=X;string.f(s);}")//
        .gives("void m(String string){string.f(X);}");
  }

  @Test public void paramAbbreviateConflictingWithLocal2() {
    topDownTrimming("TCPConnection conn(TCPConnection tcpCon){ UDPConnection c=new UDPConnection(57);if(tcpCon.isConnected()) c.disconnect();}")
        .gives("TCPConnection conn(TCPConnection tcpCon){ if(tcpCon.isConnected())(new UDPConnection(57)).disconnect();}");
  }

  @Test public void paramAbbreviateConflictingWithMethodName() {
    topDownTrimming("void m(BitmapManipulator bitmapManipulator, int __){bitmapManipulator.x().y();")//
        .stays();
  }

  @Test public void paramAbbreviateMultiple() {
    topDownTrimming("void m(StringBuilder stringBuilder, XMLDocument xmlDocument, Dog dog, Dog cat){stringBuilder.clear();"
        + "xmlDocument.open(stringBuilder.toString());dog.eat(xmlDocument.asEdible(cat));}")
            .gives("void m(StringBuilder b, XMLDocument xmlDocument, Dog dog, Dog cat){b.clear();xmlDocument.open(b.toString());"
                + "dog.eat(xmlDocument.asEdible(cat));}");
  }

  @Test public void paramAbbreviateNestedMethod() {
    topDownTrimming("void f(Iterator iterator){iterator=new Iterator<Object>(){int i=0;"
        + "@Override public boolean hasNext(){ return false;}@Override public Object next(){ return null;} };")
            .gives("void f(Iterator iterator){iterator=new Iterator<Object>(){int i;"
                + "@Override public boolean hasNext(){ return false;}@Override public Object next(){ return null;} };");
  }

  @Test public void parenthesizeOfpushdownTernary() {
    topDownTrimming("a ? b+x+e+f:b+y+e+f")//
        .gives("b+(a ? x : y)+e+f");
  }

  @Test public void postDecreementReturn() {
    topDownTrimming("a--;return a;")//
        .gives("--a;return a;");
  }

  @Test public void postDecremntInFunctionCall() {
    topDownTrimming("f(a++, i--, b++, ++b);")//
        .stays();
  }

  @Test public void postfixToPrefix101() {
    topDownTrimming("i++;")//
        .gives("++i;")//
        .stays();
  }

  @Test public void postfixToPrefixAvoidChangeOnLoopCondition() {
    topDownTrimming("for(int ¢=i;++i;++¢);")//
        .stays();
  }

  @Test public void postfixToPrefixAvoidChangeOnVariableDeclaration() {
    topDownTrimming("int s=2; x(s); int n=s++; S.out.print(n);") //
        .gives("int s=2; x(s); S.out.print(s++);");
  }

  @Test public void postIncrementInFunctionCall() {
    topDownTrimming("f(i++);")//
        .stays();
  }

  @Test public void postIncrementReturn() {
    topDownTrimming("a++;return a;")//
        .gives("++a;return a;");
  }

  @Test public void preDecreementReturn() {
    topDownTrimming("--a.b.c;return a.b.c;")//
        .gives("return--a.b.c;");
  }

  @Test public void preDecrementReturn() {
    topDownTrimming("--a;return a;")//
        .gives("return --a;");
  }

  @Test public void preDecrementReturn1() {
    topDownTrimming("--this.a;return this.a;")//
        .gives("return --this.a;");
  }

  @Test public void prefixToPosfixIncreementSimple() {
    topDownTrimming("i++")//
        .gives("++i");
  }

  @Test public void preIncrementReturn() {
    topDownTrimming("++a;return a;")//
        .gives("return ++a;");
  }

  @Test public void pushdowConditionalActualExampleFirstPass() {
    topDownTrimming("return determineEncoding(bytes)==Encoding.B ? f((ENC_WORD_PREFIX + mimeCharset + B), text, charset, bytes) "
        + ": f((ENC_WORD_PREFIX + mimeCharset + Q), text, charset, bytes) ;")
            .gives("return f( determineEncoding(bytes)==Encoding.B ? ENC_WORD_PREFIX+mimeCharset+B"
                + " : ENC_WORD_PREFIX+mimeCharset+Q,text,charset,bytes);");
  }

  @Test public void pushdowConditionalActualExampleSecondtest() {
    topDownTrimming("return f( determineEncoding(bytes)==Encoding.B ? ENC_WORD_PREFIX+mimeCharset+B : ENC_WORD_PREFIX+mimeCharset+Q,text,charset,bytes);")
        .gives("return f( ENC_WORD_PREFIX + mimeCharset +(determineEncoding(bytes)==Encoding.B ?B : Q), text,charset,bytes);");
  }

  @Test public void pushdownNot2LevelNotOfFalse() {
    topDownTrimming("!!false")//
        .gives("false");
  }

  @Test public void pushdownNot2LevelNotOfTrue() {
    topDownTrimming("!!true")//
        .gives("true");
  }

  @Test public void pushdownNotActualExample() {
    topDownTrimming("!inRange(m, e)")//
        .stays();
  }

  @Test public void pushdownNotDoubleNot() {
    topDownTrimming("!!f()")//
        .gives("f()");
  }

  @Test public void pushdownNotDoubleNotDeeplyNested() {
    topDownTrimming("!(((!f())))")//
        .gives("f()");
  }

  @Test public void pushdownNotDoubleNotNested() {
    topDownTrimming("!(!f())")//
        .gives("f()");
  }

  @Test public void pushdownNotEND() {
    topDownTrimming("a&&b")//
        .stays();
  }

  @Test public void pushdownNotMultiplication() {
    topDownTrimming("a*b")//
        .stays();
  }

  @Test public void pushdownNotNotOfAND() {
    topDownTrimming("!(a && b && c)")//
        .gives("!a || !b || !c");
  }

  @Test public void pushdownNotNotOfAND2() {
    topDownTrimming("!(f()&& f(5))")//
        .gives("!f()|| !f(5)");
  }

  @Test public void pushdownNotNotOfANDNested() {
    topDownTrimming("!(f()&&(f(5)))")//
        .gives("!f()|| !f(5)");
  }

  @Test public void pushdownNotNotOfEQ() {
    topDownTrimming("!(3==5)")//
        .gives("3 !=5");
  }

  @Test public void pushdownNotNotOfEQNested() {
    topDownTrimming("!((((3==5))))")//
        .gives("3 !=5");
  }

  @Test public void pushdownNotNotOfFalse() {
    topDownTrimming("!false")//
        .gives("true");
  }

  @Test public void pushdownNotNotOfGE() {
    topDownTrimming("!(3>=5)")//
        .gives("3<5");
  }

  @Test public void pushdownNotNotOfGT() {
    topDownTrimming("!(3> 5)")//
        .gives("3<=5");
  }

  @Test public void pushdownNotNotOfLE() {
    topDownTrimming("!(3<=5)")//
        .gives("3> 5");
  }

  @Test public void pushdownNotNotOfLT() {
    topDownTrimming("!(3<5)")//
        .gives("3>=5");
  }

  @Test public void pushdownNotNotOfNE() {
    topDownTrimming("!(3 !=5)")//
        .gives("3==5");
  }

  @Test public void pushdownNotNotOfOR() {
    topDownTrimming("!(a || b || c)")//
        .gives("!a && !b && !c");
  }

  @Test public void pushdownNotNotOfOR2() {
    topDownTrimming("!(f()|| f(5))")//
        .gives("!f()&& !f(5)");
  }

  @Test public void pushdownNotNotOfTrue() {
    topDownTrimming("!true")//
        .gives("false");
  }

  @Test public void pushdownNotNotOfTrue2() {
    topDownTrimming("!!true")//
        .gives("true");
  }

  @Test public void pushdownNotNotOfWrappedOR() {
    topDownTrimming("!((a)|| b || c)")//
        .gives("!a && !b && !c");
  }

  @Test public void pushdownNotOR() {
    topDownTrimming("a||b")//
        .stays();
  }

  @Test public void pushdownNotSimpleNot() {
    topDownTrimming("!a")//
        .stays();
  }

  @Test public void pushdownNotSimpleNotOfFunction() {
    topDownTrimming("!f(a)")//
        .stays();
  }

  @Test public void pushdownNotSummation() {
    topDownTrimming("a+b")//
        .stays();
  }

  @Test public void pushdownTernaryActualExample() {
    topDownTrimming("next<values().length")//
        .stays();
  }

  @Test public void pushdownTernaryActualExample2() {
    topDownTrimming("!inRange(m, e)? true : inner.go(r, e)")//
        .gives("!inRange(m, e)|| inner.go(r, e)");
  }

  @Test public void pushdownTernaryAlmostIdentical2Addition() {
    topDownTrimming("a ? b+d :b+ c")//
        .gives("b+(a ? d : c)");
  }

  @Test public void pushdownTernaryAlmostIdentical3Addition() {
    topDownTrimming("a ? b+d +x:b+ c + x")//
        .gives("b+(a ? d : c)+ x");
  }

  @Test public void pushdownTernaryAlmostIdentical4AdditionLast() {
    topDownTrimming("a ? b+d+e+y:b+d+e+x")//
        .gives("b+d+e+(a ? y : x)");
  }

  @Test public void pushdownTernaryAlmostIdentical4AdditionSecond() {
    topDownTrimming("a ? b+x+e+f:b+y+e+f")//
        .gives("b+(a ? x : y)+e+f");
  }

  @Test public void pushdownTernaryAlmostIdenticalAssignment() {
    topDownTrimming("a ?(b=c):(b=d)")//
        .gives("b=a ? c : d");
  }

  @Test public void pushdownTernaryAlmostIdenticalFunctionCall() {
    topDownTrimming("a ? f(b):f(c)")//
        .gives("f(a ? b : c)");
  }

  @Test public void pushdownTernaryAlmostIdenticalMethodCall() {
    topDownTrimming("a ? y.f(b):y.f(c)")//
        .gives("y.f(a ? b : c)");
  }

  @Test public void pushdownTernaryAlmostIdenticalTwoArgumentsFunctionCall1Div2() {
    topDownTrimming("a ? f(b,x):f(c,x)")//
        .gives("f(a ? b : c,x)");
  }

  @Test public void pushdownTernaryAlmostIdenticalTwoArgumentsFunctionCall2Div2() {
    topDownTrimming("a ? f(x,b):f(x,c)")//
        .gives("f(x,a ? b : c)");
  }

  @Test public void pushdownTernaryAMethodCallDistinctReceiver() {
    topDownTrimming("a ? x.f(c): y.f(d)")//
        .stays();
  }

  @Test public void pushdownTernaryDifferentTargetFieldRefernce() {
    topDownTrimming("a ? 1 + x.a : 1 + y.a")//
        .gives("1+(a ? x.a : y.a)");
  }

  @Test public void pushdownTernaryFieldReferneceShort() {
    topDownTrimming("a ? R.b.c : R.b.d")//
        .stays();
  }

  @Test public void pushdownTernaryFunctionCall() {
    topDownTrimming("a ? f(b,c): f(c)")//
        .gives("!a?f(c):f(b,c)");
  }

  @Test public void pushdownTernaryFX() {
    topDownTrimming("a ? false : c")//
        .gives("!a && c");
  }

  @Test public void pushdownTernaryIdenticalAddition() {
    topDownTrimming("a ? b+d :b+ d")//
        .gives("b+d");
  }

  @Test public void pushdownTernaryIdenticalAdditionWtihParenthesis() {
    topDownTrimming("a ?(b+d):(b+ d)")//
        .gives("(b+d)");
  }

  @Test public void pushdownTernaryIdenticalAssignment() {
    topDownTrimming("a ?(b=c):(b=c)")//
        .gives("(b=c)");
  }

  @Test public void pushdownTernaryIdenticalAssignmentVariant() {
    topDownTrimming("a ?(b=c):(b=d)")//
        .gives("b=a?c:d");
  }

  @Test public void pushdownTernaryIdenticalFunctionCall() {
    topDownTrimming("a ? f(b):f(b)")//
        .gives("f(b)");
  }

  @Test public void pushdownTernaryIdenticalIncrement() {
    topDownTrimming("a ? b++ :b++")//
        .gives("b++");
  }

  @Test public void pushdownTernaryIdenticalMethodCall() {
    topDownTrimming("a ? y.f(b):y.f(b)")//
        .gives("y.f(b)");
  }

  @Test public void pushdownTernaryintoConstructor1Div1Location() {
    topDownTrimming("a.equal(b)? new S(new Integer(4)): new S(new Ineger(3))")//
        .gives("new S(a.equal(b)? new Integer(4): new Ineger(3))");
  }

  @Test public void pushdownTernaryintoConstructor1Div3() {
    topDownTrimming("a.equal(b)? new S(new Integer(4),a,b): new S(new Ineger(3),a,b)")//
        .gives("new S(a.equal(b)? new Integer(4): new Ineger(3), a, b)");
  }

  @Test public void pushdownTernaryintoConstructor2Div3() {
    topDownTrimming("a.equal(b)? new S(a,new Integer(4),b): new S(a, new Ineger(3), b)")//
        .gives("new S(a,a.equal(b)? new Integer(4): new Ineger(3),b)");
  }

  @Test public void pushdownTernaryintoConstructor3Div3() {
    topDownTrimming("a.equal(b)? new S(a,b,new Integer(4)): new S(a,b,new Ineger(3))")//
        .gives("new S(a, b, a.equal(b)? new Integer(4): new Ineger(3))");
  }

  @Test public void pushdownTernaryintoConstructorNotSameArity() {
    topDownTrimming("a ? new S(a,new Integer(4),b): new S(new Ineger(3))")//
        .gives("!a?new S(new Ineger(3)):new S(a,new Integer(4),b)             ");
  }

  @Test public void pushdownTernaryintoPrintln() {
    topDownTrimming("if(s.equals(tipper))S.h(Hey + u);else S.h(Ho + x + a);").gives("S.h(s.equals(tipper)?Hey+u:Ho+x+a);");
  }

  @Test public void pushdownTernaryLongFieldRefernece() {
    topDownTrimming("externalImage ? R.string.webview_contextmenu_image_download_action : R.string.webview_contextmenu_image_save_action")
        .gives("!externalImage ? R.string.webview_contextmenu_image_save_action : R.string.webview_contextmenu_image_download_action");
  }

  @Test public void pushdownTernaryMethodInvocationFirst() {
    topDownTrimming("a?b():c")//
        .gives("!a?c:b()");
  }

  @Test public void pushdownTernaryNoBoolean() {
    topDownTrimming("a?b:c")//
        .stays();
  }

  @Test public void pushdownTernaryNoReceiverReceiver() {
    topDownTrimming("a<b ? f(): a.f()")//
        .stays();
  }

  @Test public void pushdownTernaryNotOnMINUS() {
    topDownTrimming("a ? -c :-d")//
        .stays();
  }

  @Test public void pushdownTernaryNotOnMINUSMINUS1() {
    topDownTrimming("a ? --c :--d")//
        .stays();
  }

  @Test public void pushdownTernaryNotOnMINUSMINUS2() {
    topDownTrimming("a ? c-- :d--")//
        .stays();
  }

  @Test public void pushdownTernaryNotOnNOT() {
    topDownTrimming("a ? !c :!d")//
        .stays();
  }

  @Test public void pushdownTernaryNotOnPLUS() {
    topDownTrimming("a ? +x : +y")//
        .gives("a ? x : y")//
        .stays();
  }

  @Test public void pushdownTernaryNotOnPLUSPLUS() {
    topDownTrimming("a ? x++ :y++")//
        .stays();
  }

  @Test public void pushdownTernaryNotSameFunctionInvocation() {
    topDownTrimming("a?b(x):d(x)")//
        .stays();
  }

  @Test public void pushdownTernaryNotSameFunctionInvocation2() {
    topDownTrimming("a?x.f(x):x.d(x)")//
        .stays();
  }

  @Test public void pushdownTernaryOnMethodCall() {
    topDownTrimming("a ? y.f(c,b):y.f(c)")//
        .gives("!a?y.f(c):y.f(c,b)");
  }

  @Test public void pushdownTernaryParFX() {
    topDownTrimming("a ? false:true")//
        .gives("!a && true");
  }

  @Test public void pushdownTernaryParTX() {
    topDownTrimming("a ?true: c")//
        .gives("a || c");
  }

  @Test public void pushdownTernaryParXF() {
    topDownTrimming("a ? b :false")//
        .gives("a && b");
  }

  @Test public void pushdownTernaryParXT() {
    topDownTrimming("a ? b :true")//
        .gives("!a || b");
  }

  @Test public void pushdownTernaryReceiverNoReceiver() {
    topDownTrimming("a<b ? a.f(): f()")//
        .gives("a>=b?f():a.f()");
  }

  @Test public void pushdownTernaryToClasConstrctor() {
    topDownTrimming("a ? new B(a,b,c): new B(a,x,c)")//
        .gives("new B(a,a ? b : x ,c)");
  }

  @Test public void pushdownTernaryToClasConstrctorTwoDifferenes() {
    topDownTrimming("a ? new B(a,b,c): new B(a,x,y)")//
        .stays();
  }

  @Test public void pushdownTernaryToClassConstrctorNotSameNumberOfArgument() {
    topDownTrimming("a ? new B(a,b): new B(a,b,c)")//
        .stays();
  }

  @Test public void pushdownTernaryTX() {
    topDownTrimming("a ? true : c")//
        .gives("a || c");
  }

  @Test public void pushdownTernaryXF() {
    topDownTrimming("a ? b : false")//
        .gives("a && b");
  }

  @Test public void pushdownTernaryXT() {
    topDownTrimming("a ? b : true")//
        .gives("!a || b");
  }

  @Test public void redundantButNecessaryBrackets1() {
    topDownTrimming("if(windowSize !=INFINITE_WINDOW){ if(getN()==windowSize)eDA.addElementRolling(variableDeclarationFragment); "
        + " else if(getN()<windowSize)eDA.addElement(variableDeclarationFragment); } else { System.h('!'); "
        + " System.h('!');System.h('!');System.h('!');System.h('!');System.h('!');System.h('!'); "
        + " eDA.addElement(variableDeclarationFragment); }")//
            .stays();
  }

  @Test public void redundantButNecessaryBrackets2() {
    topDownTrimming("if(windowSize !=INFINITE_WINDOW){ if(getN()==windowSize)eDA.addElementRolling(variableDeclarationFragment); "
        + "} else { System.h('!');System.h('!');System.h('!');System.h('!');System.h('!'); "
        + " System.h('!');System.h('!');eDA.addElement(variableDeclarationFragment); }")//
            .stays();
  }

  @Test public void redundantButNecessaryBrackets3() {
    topDownTrimming("if(b1)if(b2)print1('!');else { if(b3)print3('#');} else {  print4('$');print4('$');print4('$');print4('$');print4('$');print4('$'); "
        + " print4('$');print4('$');print4('$');print4('$');print4('$'); }").gives(
            "if(b1)if(b2)print1('!');else  if(b3)print3('#'); else {  print4('$');print4('$');print4('$');print4('$');print4('$');print4('$'); "
                + " print4('$');print4('$');print4('$');print4('$');print4('$'); }");
  }

  @Test public void removeSuper() {
    topDownTrimming("class T {T(){super();}}")//
        .gives("class T { T(){ }}");
  }

  @Test public void removeSuperWithArgument() {
    topDownTrimming("class T { T(){ super(a);a();}}")//
        .stays();
  }

  @Test public void removeSuperWithReceiver() {
    topDownTrimming("class X{X(Y o){o.super();}}")//
        .stays();
  }

  @Test public void removeSuperWithStatemen() {
    topDownTrimming("class T { T(){ super();a++;}}")//
        .gives("class T { T(){ ++a;}}");
  }

  @Test public void renameToDollarActual() {
    topDownTrimming("public static DeletePolicy fromInt(int initialSetting){  for(DeletePolicy policy: values()){ "
        + "  if(policy.setting==initialSetting){   return policy; } "
        + "  }  throw new IllegalArgumentException(\"DeletePolicy \" + initialSetting + \" unknown\");}")
            .gives("public static DeletePolicy fromInt(int initialSetting){  for(DeletePolicy $: values()){ "
                + "  if($.setting==initialSetting){   return $; } "
                + "  }  throw new IllegalArgumentException(\"DeletePolicy \" + initialSetting + \" unknown\");  }");
  }

  @Test public void renameToDollarEnhancedFor() {
    topDownTrimming("int f(){ for(int a: as)return a;}")//
        .gives("int f(){for(int $:as)return $;}");
  }

  @Test public void renameUnusedVariableToDoubleUnderscore1() {
    topDownTrimming("void f(int x){System.h(x);}")//
        .stays();
  }

  @Test public void renameUnusedVariableToDoubleUnderscore2() {
    topDownTrimming("void f(int i){}")//
        .gives("void f(int __){}")//
        .stays();
  }

  @Test public void renameUnusedVariableToDoubleUnderscore3() {
    topDownTrimming("void f(@SuppressWarnings({\"unused\"})int i){}")//
        .gives("void f(@SuppressWarnings({\"unused\"})int __){}")//
        .gives("void f(@SuppressWarnings(\"unused\")int __){}")//
        .stays();
  }

  @Test public void renameUnusedVariableToDoubleUnderscore4() {
    topDownTrimming("void f(@SuppressWarnings({\"unused\"})int x){}")//
        .gives("void f(@SuppressWarnings(\"unused\")int x){}")//
        .stays();
  }

  @Test public void renameUnusedVariableToDoubleUnderscore5() {
    topDownTrimming("void f(int i, @SuppressWarnings(\"unused\")int y){}")//
        .gives("void f(int __, @SuppressWarnings(\"unused\")int y){}");
  }

  @Test public void renameUnusedVariableToDoubleUnderscore6() {
    topDownTrimming("void f(int i, @SuppressWarnings @SuppressWarnings(\"unused\")int y){}")
        .gives("void f(int __, @SuppressWarnings @SuppressWarnings(\"unused\")int y){}");
  }

  @Test public void renameVariableUnderscore1() {
    topDownTrimming("void f(int _){System.h(_);}")//
        .gives("void f(int __){System.h(__);}");
  }

  @Test public void replaceInitializationInReturn() {
    topDownTrimming("int a=3;return a + 4;")//
        .gives("return 3 + 4;");
  }

  @Test public void replaceTwiceInitializationInReturn() {
    topDownTrimming("int a=3;return a + 4<<a;")//
        .gives("return 3 + 4<<3;");
  }

  @Test public void rightSimplificatioForNulNNVariableReplacement() {
    final InfixExpression e = i("null !=a");
    final Tipper<InfixExpression> w = Toolbox.defaultInstance().firstTipper(e);
    assert w != null;
    assert w.check(e);
    assert w.check(e);
    final ASTNode replacement = ((ReplaceCurrentNode<InfixExpression>) w).replacement(e);
    assert replacement != null;
    azzert.that(replacement + "", is("a != null"));
  }

  @Test public void rightSipmlificatioForNulNNVariable() {
    azzert.that(Toolbox.defaultInstance().firstTipper(i("null !=a")), instanceOf(InfixComparisonSpecific.class));
  }

  @Test public void sequencerFirstInElse() {
    topDownTrimming("if(a){b++;c++;++d;} else { f++;g++;return x;}")//
        .gives("if(!a){f++;g++;return x;} b++;c++;++d;");
  }

  @Test public void shorterChainParenthesisComparison() {
    topDownTrimming("a==b==c")//
        .stays();
  }

  @Test public void shorterChainParenthesisComparisonLast() {
    topDownTrimming("b==a * b * c * d * e * f * g * h==a")//
        .stays();
  }

  @Test public void shortestBranchIfWithComplexNestedIf3() {
    topDownTrimming("if(a){f();g();h();} else if(a)++i;else ++j;")//
        .stays();
  }

  @Test public void shortestBranchIfWithComplexNestedIf4() {
    topDownTrimming("if(a){f();g();h();++i;} else if(a)++i;else j++;")//
        .gives("if(!a)if(a)++i;else j++;else{f();g();h();++i;}");
  }

  @Test public void shortestBranchIfWithComplexNestedIf5() {
    topDownTrimming("if(a){f();g();h();++i;f();} else if(a)++i;else j++;")//
        .gives("if(!a)if(a)++i;else j++;else{f();g();h();++i;f();}");
  }

  @Test public void shortestBranchIfWithComplexNestedIf7() {
    topDownTrimming("if(a){f();++i;g();h();++i;f();j++;} else if(a)++i;else j++;").gives("if(!a)if(a)++i;else j++;else{f();++i;g();h();++i;f();j++;}");
  }

  @Test public void shortestBranchIfWithComplexNestedIf8() {
    topDownTrimming("if(a){f();++i;g();h();++i;u++;f();j++;} else if(a)++i;else j++;")
        .gives("if(!a)if(a)++i;else j++;else{f();++i;g();h();++i;u++;f();j++;}");
  }

  @Test public void shortestBranchIfWithComplexNestedIfPlain() {
    topDownTrimming("if(a){f();g();h();} else { i++;j++;}")//
        .gives("if(!a){i++;j++;}else{f();g();h();}");
  }

  @Test public void shortestBranchIfWithComplexSimpler() {
    topDownTrimming("if(a){f();g();h();} else i++;j++;")//
        .gives("if(!a)i++;else{f();g();h();}++j;");
  }

  @Test public void shortestBranchInIf() {
    topDownTrimming("int a=0;if(s.equals(known)){ S.console();} else { a=3;} ").gives("int a=0;if(!s.equals(known))a=3;else S.console();");
  }

  @Test public void shortestFirstAlignment() {
    topDownTrimming("n.isSimpleName()?(SimpleName)n : n.isQualifiedName()?((QualifiedName)n).getName(): null")//
        .stays();
  }

  @Test public void shortestFirstAlignmentShortened() {
    topDownTrimming("n.isF()?(SimpleName)n   : n.isG()?((QualifiedName)n).getName() : null")//
        .stays();
  }

  @Test public void shortestFirstAlignmentShortenedFurther() {
    topDownTrimming("n.isF()?(A)n : n.isG()?((B)n).f() : null")//
        .stays();
  }

  @Test public void shortestFirstAlignmentShortenedFurtherAndFurther() {
    topDownTrimming("n.isF()?(A)n : n.isG()?(B)n : null")//
        .stays();
  }

  @Test public void shortestIfBranchFirst01() {
    topDownTrimming("if(s.equals(0xDEAD)){ int u=0;for(int i=0;i<s.length();++i)if(s.charAt(i)=='a') u +=2;} else if(s.charAt(i)=='d')u -=1;return u;")
        .gives("if(!s.equals(0xDEAD)){ if(s.charAt(i)=='d')u-=1;} else { int u=0;"
            + " for(int i=0;i<s.length();++i)if(s.charAt(i)=='a')u+=2;} return u;");
  }

  @Test public void shortestIfBranchFirst02() {
    topDownTrimming("if(!s.equals(0xDEAD)){ int u=0;for(int i=0;i<s.length();++i)if(s.charAt(i)=='a')u +=2;"
        + " else if(s.charAt(i)=='d')u -=1;return u;} else { return 8;}")
            .gives("if(s.equals(0xDEAD))return 8;int u=0;for(int i=0;i<s.length();++i)  if(s.charAt(i)=='a')u +=2;else if(s.charAt(i)=='d') "
                + "  u -=1;return u; ");
  }

  @Test public void shortestIfBranchFirst02a() {
    topDownTrimming("if(!s.equals(0xDEAD)){ int u=0;for(int i=0;i<s.length();++i)  if(s.charAt(i)=='a')u +=2;else if(s.charAt(i)=='d') "
        + "  u -=1;return u;} return 8;")
            .gives("if(s.equals(0xDEAD))return 8;int u=0;for(int i=0;i<s.length();++i)  if(s.charAt(i)=='a')u +=2;else if(s.charAt(i)=='d') "
                + "  u -=1;return u; ");
  }

  @Test public void shortestIfBranchFirst02b() {
    topDownTrimming("int u=0;for(int i=0;i<s.length();++i)if(s.charAt(i)=='a')u +=2;  else if(s.charAt(i)=='d')--u;return u; ")
        .gives("int u=0;for(int ¢=0;¢<s.length();++¢)if(s.charAt(¢)=='a')u +=2;  else if(s.charAt(¢)=='d')--u;return u; ").stays();
  }

  @Test public void shortestIfBranchFirst02c() {
    final VariableDeclarationFragment f = findFirst.variableDeclarationFragment(WrapIntoComilationUnit.Statement
        .intoCompilationUnit(" int u=0;for(int i=0;i<s.length();++i)if(s.charAt(i)=='a')   u +=2;else if(s.charAt(i)=='d')u -=1;return u; "));
    assert f != null;
    azzert.that(f, iz(" u=0"));
    azzert.that(extract.nextStatement(f), iz(" for(int i=0;i<s.length();++i)if(s.charAt(i)=='a')u +=2;  else if(s.charAt(i)=='d')u -=1; "));
  }

  @Test public void shortestIfBranchWithFollowingCommandsSequencer() {
    topDownTrimming("if(a){ f();g();h();return a;} return c;").gives("if(!a)return c;f();g();h();return a;");
  }

  @Test public void shortestOperand01() {
    topDownTrimming("x + y> z")//
        .stays();
  }

  @Test public void shortestOperand02() {
    topDownTrimming("k=k + 4;if(2 * 6 + 4==k)return true;")//
        .gives("k+=4;if(2*6==k-4)return true;")//
        .gives("k+=4;if(12==k-4)return true;")//
        .gives("k+=4;if(k-4==12)return true;")//
        .gives("k+=4;if(k==12+4)return true;").gives("k+=4;if(k==16)return true;")//
        .stays();
  }

  @Test public void shortestOperand05() {
    topDownTrimming("W s=new W(\"bob\");return s.l(hZ).l(\"-ba\").toString()==\"bob-ha-banai\";")
        .gives("return(new W(\"bob\")).l(hZ).l(\"-ba\").toString()==\"bob-ha-banai\";");
  }

  @Test public void shortestOperand10() {
    topDownTrimming("return b==true;")//
        .gives("return b;");
  }

  @Test public void shortestOperand11() {
    topDownTrimming("int h,u,m,a,n;return b==true && n + a> m - u || h> u;")//
        .gives("int h,u,m,a,n;return b&&a+n>m-u||h>u;");
  }

  @Test public void shortestOperand13a() {
    topDownTrimming("(2> 2 + a)==true")//
        .gives("2>2 +a ");
  }

  @Test public void shortestOperand13b() {
    topDownTrimming("(2)==true")//
        .gives("2 ");
  }

  @Test public void shortestOperand13c() {
    topDownTrimming("2==true")//
        .gives("2 ");
  }

  @Test public void shortestOperand14() {
    topDownTrimming("Integer tipper=new Integer(5);return(tipper.toString()==null);")//
        .gives("return((new Integer(5)).toString()==null);");
  }

  @Test public void shortestOperand17() {
    topDownTrimming("5 ^ a.getNum()")//
        .gives("a.getNum()^ 5");
  }

  @Test public void shortestOperand19() {
    topDownTrimming("k.get().operand()^ a.get()")//
        .gives("a.get()^ k.get().operand()");
  }

  @Test public void shortestOperand20() {
    topDownTrimming("k.get()^ a.get()")//
        .gives("a.get()^ k.get()");
  }

  @Test public void shortestOperand22() {
    topDownTrimming("return f(a,b,c,d,e)+ f(a,b,c,d)+ f(a,b,c)+ f(a,b)+ f(a)+ f();")//
        .stays();
  }

  @Test public void shortestOperand23() {
    topDownTrimming("return f()+ \".\";}")//
        .stays();
  }

  @Test public void shortestOperand24() {
    topDownTrimming("f(a,b,c,d)& 175 & 0")//
        .gives("f(a,b,c,d)& 0 & 175");
  }

  @Test public void shortestOperand25() {
    topDownTrimming("f(a,b,c,d)& bob & 0 ")//
        .gives("bob & f(a,b,c,d)& 0");
  }

  @Test public void shortestOperand27() {
    topDownTrimming("return f(a,b,c,d)+ f(a,b,c)+ f();} ")//
        .stays();
  }

  @Test public void shortestOperand28() {
    topDownTrimming("return f(a,b,c,d)* f(a,b,c)* f();")//
        .gives("return f()*f(a,b,c)*f(a,b,c,d);");
  }

  @Test public void shortestOperand29() {
    topDownTrimming("f(a,b,c,d)^ f()^ 0")//
        .gives("f()^ f(a,b,c,d)^ 0");
  }

  @Test public void shortestOperand30() {
    topDownTrimming("f(a,b,c,d)& f()")//
        .gives("f()& f(a,b,c,d)");
  }

  @Test public void shortestOperand31() {
    topDownTrimming("return f(a,b,c,d)| \".\";}")//
        .stays();
  }

  @Test public void shortestOperand32() {
    topDownTrimming("return f(a,b,c,d)&& f();}")//
        .stays();
  }

  @Test public void shortestOperand33() {
    topDownTrimming("return f(a,b,c,d)|| f();}")//
        .stays();
  }

  @Test public void shortestOperand34() {
    topDownTrimming("return f(a,b,c,d)+ someVar;")//
        .stays();
  }

  @Test public void shortestOperand37() {
    topDownTrimming("return sansJavaExtension(f)+ n + \".\"+ extension(f);")//
        .stays();
  }

  @Test public void simpleBooleanMethod() {
    topDownTrimming("boolean f(){ int x=0;for(int i=0;i<10;++i)x +=i;return x;}").gives("boolean f(){ int $=0;for(int i=0;i<10;++i)$ +=i;return $;}");
  }

  @Test public void simplifyLogicalNegationNested() {
    topDownTrimming("!((a || b==c)&&(d || !(!!c)))")//
        .gives("!a && b !=c || !d && c");
  }

  @Test public void simplifyLogicalNegationNested1() {
    topDownTrimming("!(d || !(!!c))")//
        .gives("!d && c");
  }

  @Test public void simplifyLogicalNegationNested2() {
    topDownTrimming("!(!d || !!!c)")//
        .gives("d && c");
  }

  @Test public void simplifyLogicalNegationOfAnd() {
    topDownTrimming("!(f()&& f(5))")//
        .gives("!f()|| !f(5)");
  }

  @Test public void simplifyLogicalNegationOfEquality() {
    topDownTrimming("!(3==5)")//
        .gives("3!=5");
  }

  @Test public void simplifyLogicalNegationOfGreater() {
    topDownTrimming("!(3> 5)")//
        .gives("3<=5");
  }

  @Test public void simplifyLogicalNegationOfGreaterEquals() {
    topDownTrimming("!(3>=5)")//
        .gives("3<5");
  }

  @Test public void simplifyLogicalNegationOfInequality() {
    topDownTrimming("!(3 !=5)")//
        .gives("3==5");
  }

  @Test public void simplifyLogicalNegationOfLess() {
    topDownTrimming("!(3<5)")//
        .gives("3>=5");
  }

  @Test public void simplifyLogicalNegationOfLessEquals() {
    topDownTrimming("!(3<=5)")//
        .gives("3> 5");
  }

  @Test public void simplifyLogicalNegationOfMultipleAnd() {
    topDownTrimming("!(a && b && c)")//
        .gives("!a || !b || !c");
  }

  @Test public void simplifyLogicalNegationOfMultipleOr() {
    topDownTrimming("!(a || b || c)")//
        .gives("!a && !b && !c");
  }

  @Test public void simplifyLogicalNegationOfNot() {
    topDownTrimming("!!f()")//
        .gives("f()");
  }

  @Test public void simplifyLogicalNegationOfOr() {
    topDownTrimming("!(f()|| f(5))")//
        .gives("!f()&& !f(5)");
  }

  @Test public void sortAddition1() {
    topDownTrimming("1 + 2 - 3 - 4 + 5 / 6 - 7 + 8 * 9 + A> k + 4")//
        .gives("8*9+1+2-3-4+5 / 6-7+A>k+4");
  }

  @Test public void sortAddition2() {
    topDownTrimming("1 + 2<3 & 7 + 4> 2 + 1 || 6 - 7<2 + 1")//
        .gives("3<3&11>3||-1<3");
  }

  @Test public void sortAddition3() {
    topDownTrimming("6 - 7<1 + 2")//
        .gives("-1<3")//
        .stays();
  }

  @Test public void sortAddition4() {
    topDownTrimming("a + 11 + 2<3 & 7 + 4> 2 + 1")//
        .gives("7 + 4> 2 + 1 & a + 11 + 2<3");
  }

  @Test public void sortAdditionClassConstantAndLiteral() {
    topDownTrimming("1+A<12")//
        .gives("A+1<12");
  }

  @Test public void sortAdditionFunctionClassConstantAndLiteral() {
    topDownTrimming("1+A+f()<12")//
        .gives("f()+A+1<12");
  }

  @Test public void sortAdditionThreeOperands1() {
    topDownTrimming("1.0+2222+3")//
        .gives("2226.0")//
        .stays();
  }

  @Test public void sortAdditionThreeOperands2() {
    topDownTrimming("1.0+1+124+1")//
        .gives("127.0");
  }

  @Test public void sortAdditionThreeOperands3() {
    topDownTrimming("1+2F+33+142+1")//
        .gives("1+2F+176")//
        .stays();
  }

  @Test public void sortAdditionThreeOperands4() {
    topDownTrimming("1+2+'a'")//
        .gives("3+'a'");
  }

  @Test public void sortAdditionTwoOperands0CheckThatWeSortByLength_a() {
    topDownTrimming("1111+211")//
        .gives("1322");
  }

  @Test public void sortAdditionTwoOperands0CheckThatWeSortByLength_b() {
    topDownTrimming("211+1111")//
        .gives("1322")//
        .stays();
  }

  @Test public void sortAdditionTwoOperands1() {
    topDownTrimming("1+2F")//
        .stays();
  }

  @Test public void sortAdditionTwoOperands2() {
    topDownTrimming("2.0+1")//
        .gives("3.0");
  }

  @Test public void sortAdditionTwoOperands3() {
    topDownTrimming("1+2L")//
        .gives("3L");
  }

  @Test public void sortAdditionTwoOperands4() {
    topDownTrimming("2L+1")//
        .gives("3L");
  }

  @Test public void sortAdditionUncertain() {
    topDownTrimming("1+a")//
        .stays();
  }

  @Test public void sortAdditionVariableClassConstantAndLiteral() {
    topDownTrimming("1+A+a<12")//
        .gives("a+A+1<12");
  }

  @Test public void sortConstantMultiplication() {
    topDownTrimming("a*2")//
        .gives("2*a");
  }

  @Test public void sortDivision() {
    topDownTrimming("2.1/34.2/1.0")//
        .gives("2.1/1.0/34.2");
  }

  @Test public void sortDivisionLetters() {
    topDownTrimming("x/b/a")//
        .gives("x/a/b");
  }

  @Test public void sortDivisionNo() {
    topDownTrimming("2.1/3")//
        .stays();
  }

  @Test public void sortThreeOperands1() {
    topDownTrimming("1.0*2222*3")//
        .gives("6666.0");
  }

  @Test public void sortThreeOperands2() {
    topDownTrimming("1.0*11*124")//
        .gives("1364.0");
  }

  @Test public void sortThreeOperands3() {
    topDownTrimming("2*2F*33*142")//
        .gives("2*2F*4686")//
        .stays();
  }

  @Test public void sortThreeOperands4() {
    topDownTrimming("2*3*'a'")//
        .gives("6*'a'");
  }

  @Test public void sortTwoOperands0CheckThatWeSortByLength_a() {
    topDownTrimming("1111*211")//
        .gives("234421");
  }

  @Test public void sortTwoOperands0CheckThatWeSortByLength_b() {
    topDownTrimming("211*1111")//
        .gives("234421");
  }

  @Test public void sortTwoOperands1() {
    topDownTrimming("1F*2F")//
        .stays();
  }

  @Test public void sortTwoOperands2() {
    topDownTrimming("2.0*2")//
        .gives("4.0");
  }

  @Test public void sortTwoOperands3() {
    topDownTrimming("2*3L")//
        .gives("6L");
  }

  @Test public void sortTwoOperands4() {
    topDownTrimming("2L*1L")//
        .gives("2L");
  }

  @Test public void switchSimplifyCaseAfterDefault() {
    topDownTrimming("switch(n.getNodeType()){case BREAK_STATEMENT:return 0;case CONTINUE_STATEMENT:return 1;"
        + "case RETURN_STATEMENT:return 2;case THROW_STATEMENT:return 3;default:return-1;}");
  }

  @Test public void switchSimplifyWithDefault2() {
    topDownTrimming("switch(a){case \"-E\":optIndividualStatistics=true;break;case \"-N\":optDoNotOverwrite=true;break;"
        + "case \"-V\":optVerbose=true;break;case \"-l\":optStatsLines=true;break;case \"-r\":optStatsChanges=true;break;"
        + "default:if(!a.startsWith(\"-\"))optPath=a;try{if(a.startsWith(\"-C\"))optRounds=Integer.parseUnsignedInt(a.substring(2));}"
        + "catch(final NumberFormatException e){throw e;}break;}")
            .gives("switch(a){case \"-E\":optIndividualStatistics=true;break;case \"-N\":optDoNotOverwrite=true;break;"
                + "case \"-V\":optVerbose=true;break;case \"-l\":optStatsLines=true;break;case \"-r\":optStatsChanges=true;break;"
                + "default:if(!a.startsWith(\"-\"))optPath=a;try{if(a.startsWith(\"-C\"))optRounds=Integer.parseUnsignedInt(a.substring(2));}"
                + "catch(final NumberFormatException ¢){throw ¢;}break;}");
  }

  @Test public void synchronizedBraces() {
    topDownTrimming("synchronized(variables){ for(final String key : variables.keySet())  $.variables.put(key, variables.get(key));}")//
        .stays();
  }

  @Test public void ternarize05() {
    topDownTrimming("int u=0;if(s.equals(532))u +=6;else u +=9;").gives("int u=0;u+=s.equals(532)?6:9;");
  }

  @Test public void ternarize05a() {
    topDownTrimming("int u=0;if(s.equals(532))u +=6;else u +=9;return u;").gives("int u=0;u+=s.equals(532)?6:9;return u;");
  }

  @Test public void ternarize07() {
    topDownTrimming("String u;u=s;if(u.equals(532)==true)u=s + 0xABBA;S.h(u);").gives("String u=s ;if(u.equals(532))u=s + 0xABBA;S.h(u);");
  }

  @Test public void ternarize07a() {
    topDownTrimming("String u;u=s;if(u==true)u=s + 0xABBA;S.h(u);").gives("String u=s;if(u)u=s+0xABBA;S.h(u);");
  }

  @Test public void ternarize07aa() {
    topDownTrimming("String u=s;if(u==true)u=s+0xABBA;S.h(u);")//
        .gives("String u=s==true?s+0xABBA:s;S.h(u);");
  }

  @Test public void ternarize07b() {
    topDownTrimming("String u=s ;if(u.equals(532)==true)u=s + 0xABBA;S.h(u);").gives("String u=s.equals(532)==true?s+0xABBA:s;S.h(u);");
  }

  @Test public void ternarize09() {
    topDownTrimming("if(s.equals(532)){ return 6;}else { return 9;}")//
        .gives("return s.equals(532)?6:9;");
  }

  /* @Test public void ternarize10(){ trimmingOf("String u=s, foo=bar;" +
   * "if(u.equals(532)==true)u=s + 0xABBA;S.h(u);")
   * .gives("String u=s.equals(532)==true?s+0xABBA:s,foo=bar;S.h(u);");} */
  @Test public void ternarize12() {
    topDownTrimming("String u=s;if(s.equals(532))u=u + 0xABBA;S.h(u);")//
        .gives("String u=s.equals(532)?s+0xABBA:s;S.h(u);");
  }

  @Test public void ternarize13() {
    topDownTrimming("String u=m, foo;if(m.equals(f())==true)foo=M;")//
        .gives("String foo;if(m.equals(f())==true)foo=M;")//
        .gives("String foo;if(m.equals(f()))foo=M;");
  }

  @Test public void ternarize13Simplified() {
    topDownTrimming("String r=m, f;if(m.e(f()))f=M;")//
        .gives("String f;if(m.e(f()))f=M;");
  }

  @Test public void ternarize13SimplifiedMore() {
    topDownTrimming("if(m.equals(f())==true)foo=M;")//
        .gives("if(m.equals(f()))foo=M;");
  }

  @Test public void ternarize13SimplifiedMoreAndMore() {
    topDownTrimming("f(m.equals(f())==true);foo=M;")//
        .gives("f(m.equals(f()));foo=M;");
  }

  @Test public void ternarize13SimplifiedMoreAndMoreAndMore() {
    topDownTrimming("f(m.equals(f())==true);")//
        .gives("f(m.equals(f()));");
  }

  @Test public void ternarize13SimplifiedMoreVariant() {
    topDownTrimming("if(m==true)foo=M;")//
        .gives("if(m)foo=M;");
  }

  @Test public void ternarize13SimplifiedMoreVariantShorter() {
    topDownTrimming("if(m==true)f();")//
        .gives("if(m)f();");
  }

  @Test public void ternarize13SimplifiedMoreVariantShorterAsExpression() {
    topDownTrimming("f(m==true);f();")//
        .gives("f(m);f();");
  }

  @Test public void ternarize14() {
    topDownTrimming("String u=m,foo=GY;print(x);if(u.equals(f())==true){foo=M;int k=2;k=8;S.h(foo);}f();")
        .gives("String u=m,foo=GY;print(x);if(u.equals(f())){foo=M;int k=8;S.h(foo);}f();");
  }
  /* @Test public void ternarize16(){
   * trimmingOf("String u=m;int num2;if(m.equals(f()))num2=2;"). stays();} */

  /* @Test public void ternarize16a(){ trimmingOf("int n1, n2=0, n3; " +
   * " if(d)n2=2;")// .gives("int n1, n2=d ? 2: 0, n3;");} */
  public void ternarize18() {
    topDownTrimming("final String u=s;System.h(s.equals(u)?tH3+u:h2A+u+0);")//
        .gives("System.h(s.equals(s)?tH3+u:h2A+s+0);");
  }

  @Test public void ternarize21() {
    topDownTrimming("if(s.equals(532)){ S.h(gG);S.out.l(kKz);} f();")//
        .stays();
  }

  @Test public void ternarize21a() {
    topDownTrimming("if(s.equals(known)){ S.out.l(gG);} else { S.out.l(kKz);}").gives("S.out.l(s.equals(known)?gG:kKz);");
  }

  @Test public void ternarize22() {
    topDownTrimming("int a=0;if(s.equals(532)){ S.console();a=3;} f();")//
        .stays();
  }

  @Test public void ternarize26() {
    topDownTrimming("int a=0;if(s.equals(532)){ a+=2;a-=2;} f();")//
        .stays();
  }

  @Test public void ternarize33() {
    topDownTrimming("int a, b=0;if(b==3){ a=4;} ")//
        .gives("int a;if(0==3){a=4;}")//
        .gives("int a;if(0==3)a=4;")//
        .stays();
  }

  @Test public void ternarize35() {
    topDownTrimming("int a,b=0,c=0;a=4;if(c==3){b=2;}")//
        .gives("int a=4,b=0,c=0;if(c==3)b=2;");
  }

  @Test public void ternarize36() {
    topDownTrimming("int a,b=0,c=0;a=4;if(c==3){ b=2;a=6;} f();")//
        .gives("int a=4,b=0,c=0;if(c==3){b=2;a=6;} f();");
  }

  @Test public void ternarize38() {
    topDownTrimming("int a, b=0;use(a,b);if(b==3){ a+=2+r();a-=6;} f();")//
        .stays();
  }

  @Test public void ternarize45() {
    topDownTrimming("if(m.equals(f())==true)if(b==3){ return 3;return 7;} else if(b==3){ return 2;} a=7;")
        .gives("if(m.equals(f())){if(b==3){ return 3;return 7;} if(b==3){ return 2;} } a=7;");
  }

  @Test public void ternarize46() {
    topDownTrimming("int a , b=0;if(m.equals(NG)==true)if(b==3){ return 3;} else {  a+=7;} else if(b==3){ return 2;} else { a=7;}")
        .gives("int a;if(m.equals(NG)==true)if(0==3){return 3;}else{a+=7;}else if(0==3){return 2;}else{a=7;}");
  }

  @Test public void ternarize49() {
    topDownTrimming("if(s.equals(532)){ S.h(gG);S.out.l(kKz);} f();")//
        .stays();
  }

  @Test public void ternarize52() {
    topDownTrimming("int a=0,b=0,c,d=0,e=0;use(a,b);if(a<b){c=d;c=e;} f();")//
        .gives("int a=0,b=0,c,d=0,e=0;use(a,b);if(a<b){c=e;}f();") //
        .gives("int a=0,b=0,e=0;use(a,b);if(a<b)c=e;f();") //
        .stays();
  }

  @Test public void ternarize54() {
    topDownTrimming("int $=1,xi=0,xj=0,yi=0,yj=0;if(xi> xj==yi> yj)++$;else--$;")//
        .gives("int $=1,xj=0,yi=0,yj=0;if(0>xj==yi>yj)++$;else--$;");
  }

  @Test public void ternarize55() {
    topDownTrimming("if(key.equals(markColumn))to.put(key, a.toString()); else to.put(key, missing(key, a)? Z2 : get(key, a));")
        .gives("to.put(key,key.equals(markColumn)?a.toString():missing(key,a)?Z2:get(key,a));");
  }

  @Test public void ternarize56() {
    topDownTrimming("if(target==0){p.f(X);p.v(0);p.f(q + target);p.v(q * 100 / target);} f();")
        .gives("if(target==0){p.f(X);p.v(0);p.f(q+target);p.v(100*q / target);} f();");
  }

  @Test public void ternarizeintoSuperMethodInvocation() {
    topDownTrimming("a ? super.f(a, b, c): super.f(a, x, c)")//
        .gives("super.f(a, a ? b : x, c)");
  }

  @Test public void ternaryPushdownOfReciever() {
    topDownTrimming("a ? b.f():c.f()")//
        .gives("(a?b:c).f()");
  }

  /** Introduced by Yossi on Wed-Mar-22-21:27:17-IST-2017 (code automatically
   * generated in 'il.org.spartan.spartanizer.cmdline.anonymize.java') */
  @Test public void test_inta0b0cd0e0fabIfabcdceg() {
    topDownTrimming("int a = 0, b = 0, c, d = 0, e = 0; f(a, b); if (a < b) { c = d; c = e; } g();") //
        .using(Assignment.class, new AssignmentAndAssignmentOfSameVariable()) //
        .gives("int a=0,b=0,c,d=0,e=0;f(a,b);if(a<b){c=e;}g();") //
        .using(Block.class, new BlockSingleton()) //
        .gives("int a=0,b=0,c,d=0,e=0;f(a,b);if(a<b)c=e;g();") //
        .gives("int a=0,b=0,e=0;f(a,b);if(a<b)c=e;g();") //
        .stays() //
    ;
  }

  /** Introduced by Yogi on Sat-Apr-01-16:37:37-IDT-2017 (code automatically in
   * class 'JUnitTestMethodFacotry') */
  @Test public void test_inta15Return7a() {
    topDownTrimming("int a = 15; return 7 < a;") //
        .using(VariableDeclarationFragment.class, new LocalInitializedReturnExpression()) //
        .gives("return 7<15;") //
        .stays() //
    ;
  }

  /** Introduced by Yossi on Wed-Mar-22-21:22:15-IST-2017 (code automatically
   * generated in 'il.org.spartan.spartanizer.cmdline.anonymize.java') */
  @Test public void test_intaba3b5Ifa4Ifb3b2Elsebab3ElseIfb3b2Elsebaab3() {
    topDownTrimming("int a, b; a = 3; b = 5; if (a == 4) if (b == 3) b = 2; else { b = a; b = 3; } else if (b == 3) b = 2; else { b = a * a; b = 3; }") //
        .using(VariableDeclarationFragment.class, new LocalUnintializedAssignmentToSame()) //
        .gives("int a=3,b;b=5;if(a==4)if(b==3)b=2;else{b=a;b=3;}else if(b==3)b=2;else{b=a*a;b=3;}") //
        .using(VariableDeclarationFragment.class, new LocalUnintializedAssignmentToSame()) //
        .gives("int a=3,b=5;if(a==4)if(b==3)b=2;else{b=a;b=3;}else if(b==3)b=2;else{b=a*a;b=3;}") //
        .using(VariableDeclarationFragment.class, new LocalVariableIntializedStatementTerminatingScope()) //
        .gives("int b=5;if(3==4)if(b==3)b=2;else{b=3;b=3;}else if(b==3)b=2;else{b=3*3;b=3;}") //
        .using(Assignment.class, new AssignmentAndAssignmentOfSameValue()) //
        .gives("int b=5;if(3==4)if(b==3)b=2;else{b=b=3;}else if(b==3)b=2;else{b=3*3;b=3;}") //
        .using(IfStatement.class, new IfAssignToFooElseAssignToFoo()) //
        .gives("int b=5;if(3==4)b=b==3?2:(b=3);else if(b==3)b=2;else{b=3*3;b=3;}") //
        .using(Assignment.class, new AssignmentAndAssignmentOfSameVariable()) //
        .gives("int b=5;if(3==4)b=b==3?2:(b=3);else if(b==3)b=2;else{b=3;}") //
        .using(IfStatement.class, new IfAssignToFooElseAssignToFoo()) //
        .gives("int b=5;if(3==4)b=b==3?2:(b=3);else b=b==3?2:3;") //
        .using(IfStatement.class, new IfAssignToFooElseAssignToFoo()) //
        .gives("int b=5;b=3==4?b==3?2:(b=3):b==3?2:3;") //
        .using(ConditionalExpression.class, new TernaryShortestFirst()) //
        .gives("int b=5;b=3!=4?b==3?2:3:b==3?2:(b=3);") //
        .using(ConditionalExpression.class, new TernaryShortestFirst()) //
        .gives("int b=5;b=3!=4?b==3?2:3:b!=3?(b=3):2;") //
        .stays() //
    ;
  }

  /** Introduced by Yogi on Fri-Apr-07-19:53:21-IDT-2017 (code automatically in
   * class 'JUnitTestMethodFacotry') */
  @Test public void test_intaIntbForbIfFalseBreakReturnb() {
    topDownTrimming("int a(int b) { for (;; ++b) if (false) break; return b; }") //
        .using(IfStatement.class, new IfTrueOrFalse()) //
        .gives("int a(int b){for(;;++b){}return b;}") //
        .using(ForStatement.class, new ForEmptyBlockToEmptyStatement()) //
        .gives("int a(int b){for(;;++b);return b;}") //
        .stays() //
    ;
  }

  @Test public void testPeel() {
    azzert.that(WrapIntoComilationUnit.Expression.off(WrapIntoComilationUnit.Expression.on("on * notion * of * no * nothion !=the * plain + kludge")),
        is("on * notion * of * no * nothion !=the * plain + kludge"));
  }

  @Test public void twoMultiplication1() {
    topDownTrimming("f(a,b,c,d)* f()")//
        .gives("f()* f(a,b,c,d)");
  }

  @Test public void twoOpportunityExample() {
    azzert.that(countOpportunities(new Trimmer(), (CompilationUnit) makeAST.COMPILATION_UNIT
        .from(WrapIntoComilationUnit.Expression.on("on * notion * of * no * nothion !=the * plain + kludge"))), is(1));
    azzert.that(countOpportunities(new Trimmer(), (CompilationUnit) makeAST.COMPILATION_UNIT
        .from(WrapIntoComilationUnit.Expression.on("on * notion * of * no * nothion !=the * plain + kludge"))), is(1));
  }

  @Test public void unsafeBlockSimlify() {
    topDownTrimming("public void testParseInteger(){ String source=\"10\";use(source);{  BigFraction c=properFormat.parse(source);assert c !=null; "
        + " azzert.wizard.assertEquals(BigInteger.TEN, c.getNumerator());  azzert.wizard.assertEquals(BigInteger.ONE, c.getDenominator());} { "
        + " BigFraction c=improperFormat.parse(source);assert c !=null;  azzert.wizard.assertEquals(BigInteger.TEN, c.getNumerator()); "
        + " azzert.wizard.assertEquals(BigInteger.ONE, c.getDenominator());} }")//
            .stays();
  }

  @Test public void useOutcontextToManageStringAmbiguity() {
    topDownTrimming("1+2+s<3")//
        .gives("3+s<3");
  }

  @Test public void vanillaShortestFirstConditionalNoChange() {
    topDownTrimming("literal ? CONDITIONAL_OR : CONDITIONAL_AND")//
        .stays();
  }

  @Test public void xorSortClassConstantsAtEnd() {
    topDownTrimming("f(a,b,c,d)^ BOB")//
        .stays();
  }

  @Ignore
  static class NotWorking {
    @Test public void issue74d() {
      topDownTrimming("int[] a=new int[] {2,3};")//
          .gives("");
    }
  }
}
