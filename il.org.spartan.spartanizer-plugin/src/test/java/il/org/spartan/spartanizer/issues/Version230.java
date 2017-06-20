package il.org.spartan.spartanizer.issues;

import static fluent.ly.azzert.*;
import static fluent.ly.is.*;
import static il.org.spartan.spartanizer.engine.ExpressionComparator.*;
import static il.org.spartan.spartanizer.engine.into.*;
import static il.org.spartan.spartanizer.testing.TestUtilsAll.*;
import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;
import org.junit.*;

import fluent.ly.*;
import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.testing.*;
import il.org.spartan.spartanizer.tippers.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.traversal.*;
import il.org.spartan.spartanizer.utils.*;

/** Unit tests for version 2.30
 * @author Yossi Gil
 * @since 2014-07-10 */
// TODO: Yossi Gil
@Ignore
@SuppressWarnings({ "static-method", "javadoc", "OverlyComplexClass" }) //
public final class Version230 {
  @Test public void actualExampleForSortAddition() {
    trimmingOf("1 + b.statements().indexOf(declarationStmt)")//
        .stays();
  }
  @Test public void actualExampleForSortAdditionInContext() {
    final String from = "2 + a<b";
    final WrapIntoComilationUnit w = WrapIntoComilationUnit.Expression;
    final String wrap = w.on(from);
    azzert.that(from, is(w.off(wrap)));
    final String unpeeled = trim.apply(new TraversalImplementation(), wrap);
    if (wrap.equals(unpeeled))
      azzert.fail("Nothing done on " + from);
    final String peeled = w.off(unpeeled);
    if (peeled.equals(from))
      azzert.that("No similification of " + from, from, is(not(peeled)));
    azzert.that("Simpification of " + from + " is just reformatting", tide.clean(from), not(tide.clean(peeled)));
    assertSimilar("a + 2<b", peeled);
  }
  @Test public void andWithCLASS_CONSTANT() {
    trimmingOf("(x>> 18)& MASK_BITS")//
        .stays();
    trimmingOf("(x>> 18)& MASK_6BITS")//
        .stays();
  }
  @Test public void annotationDoNotRemoveSingleMemberNotCalledValue() {
    trimmingOf("@SuppressWarnings(sky=\"blue\")void m(){}")//
        .stays();
  }
  @Test public void annotationDoNotRemoveValueAndSomethingElse() {
    trimmingOf("@SuppressWarnings(value=\"something\", x=2)void m(){}")//
        .stays();
  }
  @Test public void annotationRemoveEmptyParentheses() {
    trimmingOf("@Override()void m(){}")//
        .gives("@Override void m(){}");
  }
  @Test public void annotationRemoveValueFromMultipleAnnotations() {
    trimmingOf("@TargetApi(value=23)@SuppressWarnings(value=\"javadoc\")void m(){}").gives("@TargetApi(23)@SuppressWarnings(\"javadoc\")void m(){}")//
        .stays();
  }
  @Test public void annotationRemoveValueMemberArrayValue() {
    trimmingOf("@SuppressWarnings(value={ \"something\", \"something else\" })void m(){}")
        .gives("@SuppressWarnings({ \"something\", \"something else\" })void m(){}");
  }
  @Test public void annotationRemoveValueMemberSingleValue() {
    trimmingOf("@SuppressWarnings(value=\"something\")void m(){}")//
        .gives("@SuppressWarnings(\"something\")void m(){}");
  }
  @Test public void assignmentReturn0() {
    trimmingOf("a=3;return a;")//
        .gives("return a=3;");
  }
  @Test public void assignmentReturn1() {
    trimmingOf("a=3;return(a);")//
        .gives("return a=3;");
  }
  @Test public void assignmentReturn2() {
    trimmingOf("a +=3;return a;")//
        .gives("return a +=3;");
  }
  @Test public void assignmentReturn3() {
    trimmingOf("a *=3;return a;")//
        .gives("return a *=3;");
  }
  @Test public void assignmentReturniNo() {
    trimmingOf("b=a=3;return a;")//
        .stays();
  }
  @Test public void blockSimplifyVanilla() {
    trimmingOf("if(a){f();}")//
        .gives("if(a)f();");
  }
  @Test public void blockSimplifyVanillaSimplified() {
    trimmingOf("{f();}")//
        .gives("f();");
  }
  @Test public void booleanChangeValueOfToConstant() {
    trimmingOf("Boolean b=Boolean.valueOf(true);")//
        .gives("Boolean.valueOf(true);")//
        .stays();
  }
  @Test public void booleanChangeValueOfToConstantNotConstant() {
    trimmingOf("Boolean.valueOf(expected);")//
        .stays();
  }
  @Test public void bugInLastIfInMethod() {
    trimmingOf("@Override public void messageFinished(final LocalMessage myMessage, final int __, final int ofTotal){ "
        + "  if(!isMessageSuppressed(myMessage)){ " + //
        "  final List<LocalMessage> messages=new ArrayList<LocalMessage>(); messages.add(myMessage); "
        + "  stats.unreadMessageCount +=myMessage.isSet(Flag.SEEN)? 0 : 1; "
        + "  stats.flaggedMessageCount +=myMessage.isSet(Flag.FLAGGED)? 1 : 0; if(listener !=null) "
        + "  listener.listLocalMessagesAddMessages(account, null, messages); } }")//
            .gives(
                "@Override public void messageFinished(final LocalMessage myMessage,final int __,final int ofTotal){if(isMessageSuppressed(myMessage))return;final List<LocalMessage>messages=new ArrayList<LocalMessage>();messages.add(myMessage);stats.unreadMessageCount+=myMessage.isSet(Flag.SEEN)?0:1;stats.flaggedMessageCount+=myMessage.isSet(Flag.FLAGGED)?1:0;if(listener!=null)listener.listLocalMessagesAddMessages(account,null,messages);}");
  }
  @Test public void bugInLastIfInMethod2() {
    trimmingOf("public void f(){  if(!g(message)){   final List<LocalMessage> messages=new ArrayList<LocalMessage>(); messages.add(message); "
        + "  stats.unreadMessageCount +=message.isSet(Flag.SEEN)? 0 : 1; "
        + "  stats.flaggedMessageCount +=message.isSet(Flag.FLAGGED)? 1 : 0; if(listener !=null) "
        + "  listener.listLocalMessagesAddMessages(account, null, messages); } }")//
            .gives(
                "public void f(){if(g(message))return;final List<LocalMessage>messages=new ArrayList<LocalMessage>();messages.add(message);stats.unreadMessageCount+=message.isSet(Flag.SEEN)?0:1;stats.flaggedMessageCount+=message.isSet(Flag.FLAGGED)?1:0;if(listener!=null)listener.listLocalMessagesAddMessages(account,null,messages);}");
  }
  @Test public void bugInLastIfInMethod3() {
    trimmingOf("public void f(){  if(!g(a)){   final List<LocalMessage> messages=new ArrayList<LocalMessage>(); messages.add(message); "
        + "  stats.unreadMessageCount +=message.isSet(Flag.SEEN)? 0 : 1; "
        + "  stats.flaggedMessageCount +=message.isSet(Flag.FLAGGED)? 1 : 0; if(listener !=null) "
        + "  listener.listLocalMessagesAddMessages(account, null, messages); } }")//
            .gives(
                "public void f(){if(g(a))return;final List<LocalMessage>messages=new ArrayList<LocalMessage>();messages.add(message);stats.unreadMessageCount+=message.isSet(Flag.SEEN)?0:1;stats.flaggedMessageCount+=message.isSet(Flag.FLAGGED)?1:0;if(listener!=null)listener.listLocalMessagesAddMessages(account,null,messages);}");
  }
  @Test public void bugInLastIfInMethod4() {
    trimmingOf("public void f(){  if(!g){   final List<LocalMessage> messages=new ArrayList<LocalMessage>(); messages.add(message); "
        + "  stats.unreadMessageCount +=message.isSet(Flag.SEEN)? 0 : 1; "
        + "  stats.flaggedMessageCount +=message.isSet(Flag.FLAGGED)? 1 : 0; if(listener !=null) "
        + "  listener.listLocalMessagesAddMessages(account, null, messages); } }")//
            .gives(
                "public void f(){if(g)return;final List<LocalMessage>messages=new ArrayList<LocalMessage>();messages.add(message);stats.unreadMessageCount+=message.isSet(Flag.SEEN)?0:1;stats.flaggedMessageCount+=message.isSet(Flag.FLAGGED)?1:0;if(listener!=null)listener.listLocalMessagesAddMessages(account,null,messages);}");
  }
  @Test public void bugInLastIfInMethod5() {
    trimmingOf("public void f(){  if(!g){   final List<LocalMessage> messages=new ArrayList<LocalMessage>(); messages2.add(message); "
        + "  stats.unreadMessageCount +=message.isSet(Flag.SEEN)? 0 : 1;   stats.flaggedMessageCount +=message.isSet(Flag.FLAGGED)? 1 : 0; } }")//
            .gives(
                "public void f(){if(g)return;final List<LocalMessage>messages=new ArrayList<LocalMessage>();messages2.add(message);stats.unreadMessageCount+=message.isSet(Flag.SEEN)?0:1;stats.flaggedMessageCount+=message.isSet(Flag.FLAGGED)?1:0;}");
  }
  @Test public void bugInLastIfInMethod6() {
    trimmingOf("public void f(){  if(!g){  final int messages=3; "
        + "  messages2.add(message); stats.unreadMessageCount +=message.isSet(Flag.SEEN)? 0 : 1; "
        + "  stats.flaggedMessageCount +=message.isSet(Flag.FLAGGED)? 1 : 0; } }")//
            .gives(
                "public void f(){if(g)return;final int messages=3;messages2.add(message);stats.unreadMessageCount+=message.isSet(Flag.SEEN)?0:1;stats.flaggedMessageCount+=message.isSet(Flag.FLAGGED)?1:0;}");
  }
  @Test public void bugInLastIfInMethod7() {
    trimmingOf("public void f(){ " + //
        "  if(!g){  foo(); " + //
        "  bar(); } }")
            //
            .gives("public void f(){if(g)return;foo();bar();}");
  }
  @Test public void bugIntroducingMISSINGWord1() {
    trimmingOf("b.f(a)&& -1==As.g(f).h(c)? o(s, b, g(f)): !b.f(\".in\")? null : y(d, b)? null : o(b.z(u, variableDeclarationFragment), s, f)")
        //
        .gives("b.f(a)&& As.g(f).h(c)==-1 ? o(s,b,g(f)): b.f(\".in\")&& !y(d,b)? o(b.z(u,variableDeclarationFragment),s,f): null");
  }
  @Test public void bugIntroducingMISSINGWord1a() {
    trimmingOf("-1==As.g(f).h(c)")//
        .gives("As.g(f).h(c)==-1");
  }
  @Test public void bugIntroducingMISSINGWord1b() {
    trimmingOf("b.f(a)&& X ? o(s, b, g(f)): !b.f(\".in\")? null : y(d, b)? null : o(b.z(u, variableDeclarationFragment), s, f)")
        //
        .gives("b.f(a)&&X?o(s,b,g(f)):b.f(\".in\")&&!y(d,b)?o(b.z(u,variableDeclarationFragment),s,f):null");
  }
  @Test public void bugIntroducingMISSINGWord1c() {
    trimmingOf("Y ? o(s, b, g(f)): !b.f(\".in\")? null : y(d, b)? null : o(b.z(u, variableDeclarationFragment), s, f)")
        .gives("Y?o(s,b,g(f)):b.f(\".in\")&&!y(d,b)?o(b.z(u,variableDeclarationFragment),s,f):null");
  }
  @Test public void bugIntroducingMISSINGWord1d() {
    trimmingOf("Y ? Z : !b.f(\".in\")? null : y(d, b)? null : o(b.z(u, variableDeclarationFragment), s, f)")
        .gives("Y?Z:b.f(\".in\")&&!y(d,b)?o(b.z(u,variableDeclarationFragment),s,f):null");
  }
  @Test public void bugIntroducingMISSINGWord1e() {
    trimmingOf("Y ? Z : R ? null : S ? null : T")//
        .gives("Y?Z:!R&&!S?T:null");
  }
  @Test public void bugIntroducingMISSINGWord2() {
    trimmingOf(
        "name.endsWith(testSuffix)&& MakeAST.stringBuilder(f).indexOf(testKeyword)==2? objects(s, name, makeInFile(f)): !name.endsWith(\".in\")? null : dotOutExists(d, name)? null : objects(name.replaceAll(\"\\\\.in$\", Z2), s, f)")
            .gives(
                "name.endsWith(testSuffix)&&MakeAST.stringBuilder(f).indexOf(testKeyword)==2?objects(s,name,makeInFile(f)):name.endsWith(\".in\")&&!dotOutExists(d,name)?objects(name.replaceAll(\"\\\\.in$\",Z2),s,f):null");
  }
  @Test public void bugIntroducingMISSINGWord2a() {
    trimmingOf(
        "name.endsWith(testSuffix)&& MakeAST.stringBuilder(f).indexOf(testKeyword)==2? objects(s, name, makeInFile(f)): !name.endsWith(\".in\")? null : dotOutExists(d, name)? null : objects(name.replaceAll(\"\\\\.in$\", Z2), s, f)")
            .gives(
                "name.endsWith(testSuffix)&&MakeAST.stringBuilder(f).indexOf(testKeyword)==2?objects(s,name,makeInFile(f)):name.endsWith(\".in\")&&!dotOutExists(d,name)?objects(name.replaceAll(\"\\\\.in$\",Z2),s,f):null");
  }
  @Test public void bugIntroducingMISSINGWord2b() {
    trimmingOf(
        "name.endsWith(testSuffix)&& T ? objects(s, name, makeInFile(f)): !name.endsWith(\".in\")? null : dotOutExists(d, name)? null : objects(name.replaceAll(\"\\\\.in$\", Z2), s, f)")
            .gives(
                "name.endsWith(testSuffix)&& T ? objects(s,name,makeInFile(f)): name.endsWith(\".in\")&& !dotOutExists(d,name)?objects(name.replaceAll(\"\\\\.in$\",Z2),s,f):null");
  }
  @Test public void bugIntroducingMISSINGWord2c() {
    trimmingOf(
        "X && T ? objects(s, name, makeInFile(f)): !name.endsWith(\".in\")? null : dotOutExists(d, name)? null : objects(name.replaceAll(\"\\\\.in$\", Z2), s, f)")
            .gives(
                "X && T ? objects(s,name,makeInFile(f)): name.endsWith(\".in\")&& !dotOutExists(d,name)?objects(name.replaceAll(\"\\\\.in$\",Z2),s,f):null");
  }
  @Test public void bugIntroducingMISSINGWord2d() {
    trimmingOf("X && T ? E : Y ? null : dotOutExists(d, name)? null : objects(name.replaceAll(\"\\\\.in$\", Z2), s, f)")
        .gives("X && T ? E : !Y && !dotOutExists(d,name)? objects(name.replaceAll(\"\\\\.in$\",Z2),s,f): null");
  }
  @Test public void bugIntroducingMISSINGWord2e() {
    trimmingOf("X && T ? E : Y ? null : Z ? null : objects(name.replaceAll(\"\\\\.in$\", Z2), s, f)")
        .gives("X && T ? E : !Y && !Z ? objects(name.replaceAll(\"\\\\.in$\",Z2),s,f): null");
  }
  @Test public void bugIntroducingMISSINGWord2e1() {
    trimmingOf("X && T ? E : Y ? null : Z ? null : objects(name.replaceAll(x, Z2), s, f)")
        .gives("X && T ? E : !Y && !Z ? objects(name.replaceAll(x,Z2),s,f): null");
  }
  @Test public void bugIntroducingMISSINGWord2e2() {
    trimmingOf("X && T ? E : Y ? null : Z ? null : objects(name.replaceAll(g, Z2), s, f)")
        .gives("X && T ? E : !Y && !Z ? objects(name.replaceAll(g,Z2),s,f): null");
  }
  @Test public void bugIntroducingMISSINGWord2f() {
    trimmingOf("X && T ? E : Y ? null : Z ? null : F")//
        .gives("X&&T?E:!Y&&!Z?F:null");
  }
  @Test public void bugIntroducingMISSINGWord3() {
    trimmingOf(
        "name.endsWith(testSuffix)&& -1==MakeAST.stringBuilder(f).indexOf(testKeyword)? objects(s, name, makeInFile(f)): !name.endsWith(x)? null : dotOutExists(d, name)? null : objects(name.replaceAll(3, 56), s, f)")
            .gives(
                "name.endsWith(testSuffix)&&MakeAST.stringBuilder(f).indexOf(testKeyword)==-1?objects(s,name,makeInFile(f)):name.endsWith(x)&&!dotOutExists(d,name)?objects(name.replaceAll(3,56),s,f):null");
  }
  @Test public void bugIntroducingMISSINGWord3a() {
    trimmingOf("!name.endsWith(x)? null : dotOutExists(d, name)? null : objects(name.replaceAll(3, 56), s, f)")
        .gives("name.endsWith(x)&&!dotOutExists(d,name)?objects(name.replaceAll(3,56),s,f):null");
  }
  @Test public void bugIntroducingMISSINGWordTry1() {
    trimmingOf(
        "name.endsWith(testSuffix)&& -1==MakeAST.stringBuilder(f).indexOf(testKeyword)? objects(s, name, makeInFile(f)): !name.endsWith(\".in\")? null : dotOutExists(d, name)? null : objects(name.replaceAll(\"\\\\.in$\", Z2), s, f)")
            .gives(
                "name.endsWith(testSuffix)&& MakeAST.stringBuilder(f).indexOf(testKeyword)==-1?objects(s,name,makeInFile(f)):name.endsWith(\".in\")&&!dotOutExists(d,name)?objects(name.replaceAll(\"\\\\.in$\",Z2),s,f):null");
  }
  @Test public void bugIntroducingMISSINGWordTry2() {
    trimmingOf("!(intent.getBooleanExtra(EXTRA_FROM_SHORTCUT, false)&& !K9.FOLDER_NONE.equals(mAccount.getAutoExpandFolderName()))")
        .gives("!intent.getBooleanExtra(EXTRA_FROM_SHORTCUT,false)||K9.FOLDER_NONE.equals(mAccount.getAutoExpandFolderName())");
  }
  @Test public void bugIntroducingMISSINGWordTry3() {
    trimmingOf("!(f.g(X, false)&& !a.b.e(m.h()))")//
        .gives("!f.g(X,false)||a.b.e(m.h())");
  }
  @Test public void bugOfMissingTry() {
    trimmingOf("!(A && B && C && true && D)")//
        .gives("!A||!B||!C||false||!D");
  }
  @Test public void canonicalFragementExample1() {
    trimmingOf("int a;a=3;")//
        .using(new LocalUninitializedAssignmentToIt(), VariableDeclarationFragment.class) //
        .gives("int a=3;");
  }
  @Test public void canonicalFragementExample2() {
    trimmingOf("int a=2;if(b)a=3;")//
        .gives("int a=b ? 3 : 2;");
  }
  @Test public void canonicalFragementExample3() {
    trimmingOf("int a=2;a +=3;")//
        .gives("int a=2 + 3;");
  }
  @Test public void canonicalFragementExample5() {
    trimmingOf("int a=2;return 3 * a;")//
        .gives("return 3 * 2;");
  }
  @Test public void canonicalFragementExample6() {
    trimmingOf("int a=2;return a;")//
        .gives("return 2;");
  }
  @Test public void canonicalFragementExamplesWithExraFragmentsX() {
    trimmingOf("int a;if(x)a=3;else a++;")//
        .gives("int a;if(x)a=3;else++a;");
  }
  @Test public void chainComparison() {
    azzert.that(right(i("a==true==b==c")) + "", is("c"));
    trimmingOf("a==true==b==c")//
        .gives("a==b==c");
  }
  @Test public void chainCOmparisonTrueLast() {
    trimmingOf("a==b==c==true")//
        .gives("a==b==c");
  }
  @Test public void comaprisonWithBoolean1() {
    trimmingOf("s.equals(532)==true")//
        .gives("s.equals(532)");
  }
  @Test public void comaprisonWithBoolean2() {
    trimmingOf("s.equals(532)==false ")//
        .gives("!s.equals(532)");
  }
  @Test public void comaprisonWithBoolean3() {
    trimmingOf("(false==s.equals(532))")//
        .gives("(!s.equals(532))");
  }
  @Test public void comaprisonWithSpecific0() {
    trimmingOf("this !=a")//
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
    trimmingOf("null !=a")//
        .gives("a !=null");
  }
  @Test public void comaprisonWithSpecific2() {
    trimmingOf("null !=a")//
        .gives("a !=null");
    trimmingOf("this==a")//
        .gives("a==this");
    trimmingOf("null==a")//
        .gives("a==null");
    trimmingOf("this>=a")//
        .gives("a<=this");
    trimmingOf("null>=a")//
        .gives("a<=null");
    trimmingOf("this<=a")//
        .gives("a>=this");
    trimmingOf("null<=a")//
        .gives("a>=null");
  }
  @Test public void comaprisonWithSpecific2a() {
    trimmingOf("s.equals(532)==false")//
        .gives("!s.equals(532)");
  }
  @Test public void comaprisonWithSpecific3() {
    trimmingOf("(this==s.equals(532))")//
        .gives("(s.equals(532)==this)");
  }
  @Test public void comaprisonWithSpecific4() {
    trimmingOf("(0<a)")//
        .gives("(a>0)");
  }
  @Test public void comaprisonWithSpecificInParenthesis() {
    trimmingOf("(null==a)")//
        .gives("(a==null)");
  }
  @Test public void commonPrefixIfBranchesInFor() {
    trimmingOf("for(;;)if(a){i++;j++;j++;} else { i++;j++;i++;}")//
        .gives("for(;;){i++;j++;if(a)j++;else i++;}");
  }
  @Test public void commonSuffixIfBranches() {
    trimmingOf("if(a){  ++i; f(); } else { ++j; f(); }").gives("if(a) ++i; else  ++j;  f();");
  }
  @Test public void commonSuffixIfBranchesDisappearingElse() {
    trimmingOf("if(a){  ++i; f(); } else { f(); }")//
        .gives("if(a) ++i;  f();");
  }
  @Test public void commonSuffixIfBranchesDisappearingThen() {
    trimmingOf("if(a){  f(); } else { ++j; f(); }")//
        .gives("if(!a) ++j;  f();");
  }
  @Test public void commonSuffixIfBranchesDisappearingThenWithinIf() {
    trimmingOf("if(x)if(a){  f(); } else { ++j; f(); } else { h();++i;++j;++k;if(a)f();else g();}")
        .gives("if(x){ if(!a) ++j;  f();} else { h();++i;++j;++k;if(a)f();else g();}");
  }
  @Test public void compareWithBoolean00() {
    trimmingOf("a==true")//
        .gives("a");
  }
  @Test public void compareWithBoolean01() {
    trimmingOf("a==false")//
        .gives("!a");
  }
  @Test public void compareWithBoolean10() {
    trimmingOf("true==a")//
        .gives("a");
  }
  @Test public void compareWithBoolean100() {
    trimmingOf("a !=true")//
        .gives("!a");
  }
  @Test public void compareWithBoolean100a() {
    trimmingOf("(((a)))!=true")//
        .gives("!a");
  }
  @Test public void compareWithBoolean101() {
    trimmingOf("a !=false")//
        .gives("a");
  }
  @Test public void compareWithBoolean11() {
    trimmingOf("false==a")//
        .gives("!a");
  }
  @Test public void compareWithBoolean110() {
    trimmingOf("true !=a")//
        .gives("!a");
  }
  @Test public void compareWithBoolean111() {
    trimmingOf("false !=a")//
        .gives("a");
  }
  @Test public void compareWithBoolean2() {
    trimmingOf("false !=false")//
        .gives("false");
  }
  @Test public void compareWithBoolean3() {
    trimmingOf("false !=true")//
        .gives("true");
  }
  @Test public void compareWithBoolean4() {
    trimmingOf("false==false")//
        .gives("true");
  }
  @Test public void compareWithBoolean5() {
    trimmingOf("false==true")//
        .gives("false");
  }
  @Test public void compareWithBoolean6() {
    trimmingOf("false !=false")//
        .gives("false");
  }
  @Test public void compareWithBoolean7() {
    trimmingOf("true !=true")//
        .gives("false");
  }
  @Test public void compareWithBoolean8() {
    trimmingOf("true !=false")//
        .gives("true");
  }
  @Test public void compareWithBoolean9() {
    trimmingOf("true !=true")//
        .gives("false");
  }
  @Test public void comparison01() {
    trimmingOf("1+2+3<3")//
        .gives("6<3")//
        .stays();
  }
  @Test public void comparison02() {
    trimmingOf("f(2)<a")//
        .stays();
  }
  @Test public void comparison03() {
    trimmingOf("this==null")//
        .stays();
  }
  @Test public void comparison04() {
    trimmingOf("6-7<2+1")//
        .gives("-1<3");
  }
  @Test public void comparison05() {
    trimmingOf("a==11")//
        .stays();
  }
  @Test public void comparison06() {
    trimmingOf("1<102333")//
        .stays();
  }
  @Test public void comparison08() {
    trimmingOf("a==this")//
        .stays();
  }
  @Test public void comparison09() {
    trimmingOf("1+2<3&7+4>2+1")//
        .gives("3<3&11>3");
  }
  @Test public void comparison11() {
    trimmingOf("12==this")//
        .gives("this==12");
  }
  @Test public void comparison12() {
    trimmingOf("1+2<3&7+4>2+1||6-7<2+1")//
        .gives("3<3&11>3||-1<3")//
        .stays();
  }
  @Test public void comparison13() {
    trimmingOf("13455643294<22")//
        .stays();
  }
  @Test public void comparisonWithCharacterConstant() {
    trimmingOf("'d'==s.charAt(i)")//
        .gives("s.charAt(i)=='d'");
  }
  @Test public void compreaeExpressionToExpression() {
    trimmingOf("6 - 7<2 + 1 ")//
        .gives("-1<3");
  }
  @Test public void correctSubstitutionInIfAssignment() {
    trimmingOf("int a=2+3;if(a+b> a<<b)a=(((((a *7<<a)))));")//
        .gives("int a=2+3+b>2+3<<b?(2+3)*7<<2+3:2+3;");
  }
  @Test public void doNotConsolidateNewArrayActual() {
    trimmingOf("occupied=new boolean[capacity]; placeholder=new boolean[capacity];")//
        .stays();
  }
  @Test public void doNotConsolidateNewArraySimplifiedl() {
    trimmingOf("a=new int[1]; b=new int[1];")//
        .stays();
  }
  @Test public void doNotConsolidatePlainNew() {
    trimmingOf("a=new A(); b=new B();")//
        .stays();
  }
  @Test public void doNotInlineDeclarationWithAnnotationSimplified() {
    trimmingOf("@SuppressWarnings int $=(Class<T>)findClass(className);return $;}")//
        .stays();
  }
  @Test public void doNotInlineWithDeclaration() {
    trimmingOf("private Class<? extends T> retrieveClazz()throws ClassNotFoundException { NonNull(className); "
        + " @SuppressWarnings(\"unchecked\")final Class<T> $=(Class<T>)findClass(className);return $;}")//
            .stays();
  }
  @Test public void doNotIntroduceDoubleNegation() {
    trimmingOf("!Y ? null :!Z ? null : F")//
        .gives("Y&&Z?F:null");
  }
  @Test public void donotSorMixedTypes() {
    trimmingOf("if(2 * 3.1415 * 180> a || tipper.concat(sS)==1922 && tipper.length()> 3)return c> 5;")
        .gives("if(1130.94> a || tipper.concat(sS)==1922 && tipper.length()> 3)return c> 5;");
  }
  @Test public void dontELiminateCatchBlock() {
    trimmingOf("try { f();} catch(Exception e){ } finally {}")//
        .gives("try { f();} catch(Exception e){ }");
  }
  @Test public void dontSimplifyCatchBlock() {
    trimmingOf("try { {} ;{} } catch(Exception e){{} ;{} } finally {{} ;{}}").gives("try {}  catch(Exception e){}  finally {}");
  }
  @Test public void duplicatePartialIfBranches() {
    trimmingOf("if(a){ f();g();++i;} else { f();g();  --i;}")//
        .gives("f();g();if(a)++i;else  --i;");
  }
  @Test public void eliminateSwitch() {
    trimmingOf("switch(a){ default: } int x=5;f(x++);")//
        .gives("int x=5;f(x++);");
  }
  @Test public void emptyElse() {
    trimmingOf("if(x)b=3;else ;")//
        .gives("if(x)b=3;");
  }
  @Test public void emptyElseBlock() {
    trimmingOf("if(x)b=3;else { ;}")//
        .gives("if(x)b=3;");
  }
  @Test public void emptyIsNotChangedExpression() {
    trimmingOf("")//
        .stays();
  }
  @Test public void emptyIsNotChangedStatement() {
    trimmingOf("")//
        .stays();
  }
  @Test public void emptyThen1() {
    trimmingOf("if(b);else x();")//
        .gives("if(!b)x();");
  }
  @Test public void emptyThen2() {
    trimmingOf("if(b){;;} else {x();}")//
        .gives("if(!b)x();");
  }
  @Test public void factorOutAnd() {
    trimmingOf("(a || b)&&(a || c)")//
        .gives("a || b && c");
  }
  @Test public void factorOutOr() {
    trimmingOf("a && b || a && c")//
        .gives("a &&(b || c)");
  }
  @Test public void factorOutOr3() {
    trimmingOf("a && b && x && f()|| a && c && y ")//
        .gives("a &&(b && x && f()|| c && y)");
  }
  @Test public void forLoopBug() {
    trimmingOf("for(int i=0;i<s.length();++i)if(s.charAt(i)=='a')u +=2;else if(s.charAt(i)=='d')u -=1;return u;if(b)i=3;")//
        .gives("for(int ¢=0;¢<s.length();++¢)if(s.charAt(¢)=='a')u +=2;else if(s.charAt(¢)=='d')u-=1;return u;")//
        .gives("for(int ¢=0;¢<s.length();++¢)if(s.charAt(¢)=='a')u +=2;else if(s.charAt(¢)=='d')--u ;return u;")//
        .stays();
  }
  /** Introduced by Yogi on Tue-Apr-11-10:06:26-IDT-2017 (code automatically in
   * class 'JUnitTestMethodFacotry') */
  @Test public void return22aTrue() {
    trimmingOf("return (2 > 2 + a) == true;") //
        .using(new InfixComparisonBooleanLiteral(), InfixExpression.class) //
        .gives("return 2>2+a;") //
        .using(new InfixComparisonSpecific(), InfixExpression.class) //
        .gives("return 2+a<2;") //
        .using(new InfixAdditionSort(), InfixExpression.class) //
        .gives("return a+2<2;") //
        .using(new InfixSimplifyComparisionOfAdditions(), InfixExpression.class) //
        .gives("return a<2-2;") //
        .using(new InfixSubtractionEvaluate(), InfixExpression.class) //
        .gives("return a<0;") //
        .stays() //
    ;
  }
  @Test public void ifBarFooElseBazFooExtractDefinedSuffix() {
    trimmingOf("public static void f(){ int i=0;if(f()){ i +=1;System.h('!');System.h('!');  ++i;} else { i +=2;System.h('@');System.h('@');++i;} }")//
        .gives("public static void f(){ int i=0;if(f()){ i +=1;System.h('!');System.h('!');  } else { i +=2;System.h('@');System.h('@');} ++i;}");
  }
  @Test public void ifBarFooElseBazFooExtractUndefinedSuffix() {
    trimmingOf("public final static final void f(){ if(tr()){ int i=0;System.h(i + 0);++i;  } else { int i=1;System.h(i * 1);++i;} }");
  }
  @Test public void ifBugSecondTry() {
    trimmingOf("final int c=2;if(c==c + 1){ if(c==c + 2)return null;c=f().charAt(3);  } else if(Character.digit(c, 16)==-1)return null;return null;")
        .gives("final int c=2;if(c !=c + 1){ if(Character.digit(c, 16)==-1)return null; "
            + " } else { if(c==c + 2)return null;c=f().charAt(3);}  return null;");
  }
  @Test public void ifBugSimplified() {
    trimmingOf("if(x){ if(z)return null;c=f().charAt(3);} else if(y)return; ")
        .gives("if(!x){ if(y)return;} else { if(z)return null;  c=f().charAt(3);} ");
  }
  @Test public void ifBugWithPlainEmptyElse() {
    trimmingOf("if(z)f();else  ; ")//
        .gives("if(z)f(); ");
  }
  @Test public void ifDegenerateThenInIf() {
    trimmingOf("if(a)if(b){} else f();x();")//
        .gives("if(a)if(!b)f();x();");
  }
  @Test public void ifEmptyElsewWithinIf() {
    trimmingOf("if(a)if(b){;;;f();} else {}")//
        .gives("if(a&&b){;;;f();}");
  }
  @Test public void ifEmptyThenThrow() {
    trimmingOf("if(b){ /* empty */} else { throw new Excpetion(); }")//
        .gives("if(!b)throw new Excpetion();");
  }
  @Test public void ifEmptyThenThrowVariant() {
    trimmingOf("if(b){ /* empty */; } // no else\n   throw new Exception(); ")//
        .gives("throw new Exception();")//
        .stays();
  }
  @Test public void ifEmptyThenThrowVariant1() {
    trimmingOf("if(b){;} throw new Exception(); ")//
        .gives("throw new Exception();")//
        .stays()//
    ;
  }
  @Test public void ifEmptyThenThrowWitinIf() {
    trimmingOf("if(x)if(b){ /* empty */} else { throw new Excpetion(); } else { f();f();f();f();f();f();f();f();}")
        .gives("if(x){if(!b)throw new Excpetion();}else{f();f();f();f();f();f();f();f();}")//
        .stays();
  }
  @Test public void ifFunctionCall() {
    trimmingOf("if(x)f(a);else f(b);")//
        .gives("f(x ? a: b);");
  }
  @Test public void ifPlusPlusPost() {
    trimmingOf("if(x)a++;else b++;")//
        .gives("if(x)++a;else++b;");
  }
  @Test public void ifPlusPlusPostExpression() {
    trimmingOf("x? a++:b++")//
        .stays();
  }
  @Test public void ifPlusPlusPre() {
    trimmingOf("if(x)++a;else ++b;")//
        .stays();
  }
  @Test public void ifPlusPlusPreExpression() {
    trimmingOf("x? ++a:++b")//
        .stays();
  }
  @Test public void ifSequencerNoElseSequencer00() {
    trimmingOf("for(;;){if(a)return;break;}a=3;")//
        .stays();
  }
  @Test public void ifSequencerNoElseSequencer01() {
    trimmingOf("if(a)throw e;break;")//
        .stays();
  }
  @Test public void ifSequencerNoElseSequencer02() {
    trimmingOf("if(a)break;break;")//
        .gives("break;");
  }
  @Test public void ifSequencerNoElseSequencer03() {
    trimmingOf("if(a)continue;break;")//
        .stays();
  }
  @Test public void ifSequencerNoElseSequencer04() {
    trimmingOf("if(a)break;return 0;")//
        .gives("if(!a)return 0;break;");
  }
  @Test public void ifSequencerNoElseSequencer04a() {
    trimmingOf("for(;;){if(a)break;return;} a=3;")//
        .gives("for(;;){if(!a)return;break;}a=3;")//
        .stays();
  }
  @Test public void ifSequencerNoElseSequencer05() {
    trimmingOf("for(;;)if(a){x();return;} a=2;")//
        .stays();
  }
  @Test public void ifSequencerNoElseSequencer05a() {
    trimmingOf("for(;;){ if(a){x();return;} continue;a=3;}")//
        .gives("for(;;){ if(a){x();return;} continue;}")//
        .gives("for(;;){ if(a){x();return;} }")//
        .gives("for(;;)if(a){x();return;}") //
        .stays();
  }
  @Test public void ifSequencerNoElseSequencer05aa() {
    trimmingOf("if(a){x();return a;} continue;a=3;")//
        .gives("if(a){x();return a;} continue;")//
        .stays();
  }
  @Test public void ifSequencerNoElseSequencer05ab() {
    trimmingOf("synchronized(a){ if(a){x();return;} a=3;}")//
        .stays();
  }
  @Test public void ifSequencerNoElseSequencer06() {
    trimmingOf("if(a)throw e;break;")//
        .stays();
  }
  @Test public void ifSequencerNoElseSequencer07() {
    trimmingOf("if(a)break;throw e;")//
        .gives("if(!a)throw e;break;");
  }
  @Test public void ifSequencerNoElseSequencer08() {
    trimmingOf("if(a)throw e;continue;")//
        .stays();
  }
  @Test public void ifSequencerNoElseSequencer09() {
    trimmingOf("if(a)break;throw e;")//
        .gives("if(!a)throw e;break;");
  }
  @Test public void ifSequencerNoElseSequencer10() {
    trimmingOf("if(a)continue;return 0;")//
        .gives("if(!a)return 0;continue;");
  }
  @Test public void ifSequencerThenSequencer0() {
    trimmingOf("if(a)return 4;else break;")//
        .gives("if(a)return 4;break;");
  }
  @Test public void ifSequencerThenSequencer1() {
    trimmingOf("if(a)break;else return 2;")//
        .gives("if(!a)return 2;break;");
  }
  @Test public void ifSequencerThenSequencer3() {
    trimmingOf("if(a)return 10;else continue;")//
        .gives("if(a)return 10;continue;");
  }
  @Test public void ifSequencerThenSequencer4() {
    trimmingOf("if(a)continue;else return 2;")//
        .gives("if(!a)return 2;continue;");
  }
  @Test public void ifSequencerThenSequencer5() {
    trimmingOf("if(a)throw e;else break;")//
        .gives("if(a)throw e;break;");
  }
  @Test public void ifSequencerThenSequencer6() {
    trimmingOf("if(a)break;else throw e;")//
        .gives("if(!a)throw e;break;");
  }
  @Test public void ifSequencerThenSequencer7() {
    trimmingOf("if(a)throw e;else continue;")//
        .gives("if(a)throw e;continue;");
  }
  @Test public void ifSequencerThenSequencer8() {
    trimmingOf("if(a)break;else throw e;")//
        .gives("if(!a)throw e;break;");
  }
  @Test public void ifThrowFooElseThrowBar() {
    trimmingOf("if(a)throw foo;else throw bar;")//
        .gives("throw a ? foo : bar;");
  }
  @Test public void ifThrowNoElseThrow() {
    trimmingOf("if(!(e.getCause()instanceof Error))throw e; throw(Error)e.getCause();")
        .gives("throw !(e.getCause()instanceof Error)?e:(Error)e.getCause();");
  }
  @Test public void ifWithCommonNotInBlock() {
    trimmingOf("for(;;)if(a){i++;j++;f();} else { i++;j++;g();}")//
        .gives("for(;;){i++;j++;if(a)f();else g();}");
  }
  @Test public void ifWithCommonNotInBlockDegenerate() {
    trimmingOf("for(;;)if(a){i++;f();} else { i++;j++;}")//
        .gives("for(;;){i++;if(a)f();else j++;}");
  }
  @Test public void ifWithCommonNotInBlockiLongerElse() {
    trimmingOf("for(;;)if(a){i++;j++;f();} else { i++;j++;f();h();}")//
        .gives("for(;;){i++;j++;f();if(!a)h();}");
  }
  @Test public void ifWithCommonNotInBlockiLongerThen() {
    trimmingOf("for(;;)if(a){i++;j++;f();} else { i++;j++;}")//
        .gives("for(;;){i++;j++;if(a)f();}");
  }
  @Test public void ifWithCommonNotInBlockNothingLeft() {
    trimmingOf("for(;;)if(a){i++;j++;} else { i++;j++;}")//
        .gives("for(;;){i++;j++;}");
  }
  @Test public void infiniteLoopBug1() {
    trimmingOf("static boolean hasAnnotation(final VariableDeclarationFragment zet){ "
        + " return hasAnnotation((VariableDeclarationStatement)f.getParent()); }")//
            .stays();
  }
  @Test public void infiniteLoopBug2() {
    trimmingOf("static boolean hasAnnotation(final VariableDeclarationStatement n, int abcd){ return hasAnnotation(n.modifiers());  }")
        .gives("static boolean hasAnnotation(final VariableDeclarationStatement s, int abcd){  return hasAnnotation(s.modifiers());}");
  }
  @Test public void infiniteLoopBug3() {
    trimmingOf("boolean f(final VariableDeclarationStatement n){ return false;}")
        .gives("boolean f(final VariableDeclarationStatement s){ return false;}");
  }
  @Test public void infiniteLoopBug4() {
    trimmingOf("void f(final VariableDeclarationStatement n){}")//
        .gives("void f(final VariableDeclarationStatement s){ }");
  }
  @Test public void initializer101() {
    trimmingOf("int a=b;return a;")//
        .gives("return b;")//
        .stays();
  }
  @Test public void inline01() {
    trimmingOf("public int y(){ final Z u=new Z(6);S.h(u.j);return u;} ").gives("public int y(){ final Z $=new Z(6);S.h($.j);return $;} ");
  }
  @Test public void inlineInitializers() {
    trimmingOf("int b,a=2;return 3 * a * b;")//
        .gives("return 3*2*b;");
  }
  @Test public void inlineInitializersFirstStep() {
    trimmingOf("int b=4,a=2;return 3 * a * b;")//
        .gives("int a=2;return 3*a*4;");
  }
  /** START OF STABLING TESTS */
  @Test public void inlineintoInstanceCreation() {
    trimmingOf("public Statement methodBlock(FrameworkMethod m){ final Statement statement=methodBlock(m); "
        + " return new Statement(){ public void evaluate()throws Throwable { try {  statement.evaluate(); "
        + "  handleDataPointSuccess();} catch(AssumptionViolatedException e){  handleAssumptionViolation(e); "
        + " } catch(Throwable e){  reportParameterizedError(e, complete.getArgumentStrings(nullsOk()));}  } }; }")
            .gives("public Statement methodBlock(FrameworkMethod m){ final Statement $=methodBlock(m);return new Statement(){ "
                + " public void evaluate()throws Throwable { try {  $.evaluate(); "
                + "  handleDataPointSuccess();} catch(AssumptionViolatedException e){   handleAssumptionViolation(e);} catch(Throwable e){ "
                + "  reportParameterizedError(e, complete.getArgumentStrings(nullsOk()));} } }; }");
  }
  @Test public void inlineintoNextStatementWithSideEffects() {
    trimmingOf("int a=f();if(a)g(a);else h(u(a));")//
        .stays();
  }
  @Test public void inlineSingleUse07() {
    trimmingOf(
        " final Collection<Integer> outdated=an.empty.list();int x=6, y=7;S.h(x+y);final Collection<Integer> coes=an.empty.list();for(final Integer pi : coes){ if(pi.intValue()<x - y)outdated.add(pi);command();} S.h(coes.size());")
            .stays();
  }
  @Test public void inlineSingleUseKillingVariables() {
    trimmingOf("int $, xi=0, xj=0, yi=0, yj=0;if(xi> xj==yi> yj)$++;else $--;").gives("int $, xj=0, yi=0, yj=0;if(0>xj==yi>yj)$++;else $--;");
  }
  @Test public void inlineSingleUseKillingVariablesSimplified() {
    trimmingOf("int $=1,xi=0,xj=0,yi=0,yj=0;if(xi> xj==yi> yj)$++;else $--;").gives("int $=1,xj=0,yi=0,yj=0;if(0>xj==yi>yj)$++;else $--;")//
        .gives("int $=1,yi=0,yj=0; if(0>0==yi>yj)$++;else $--;")//
        .gives("int $=1,yj=0;  if(0>0==0>yj)$++;else $--;")//
        .gives("int $=1;  if(0>0==0>0)$++;else $--;")//
        .gives("int $=1;  if(0>0==0>0)++$;else--$;");
  }
  @Test public void inlineSingleUseTrivial() {
    trimmingOf("int $=1,yj=0;  if(0>0==yj<0)++$;else--$;")//
        .gives("int $=1;  if(0>0==0<0)++$;else--$;");
  }
  @Test public void inlineSingleUseVanilla() {
    trimmingOf("int a=f();if(a)f();")//
        .gives("if(f())f();");
  }
  @Test public void inlineSingleUseWithAssignment() {
    trimmingOf("int a=2;while(true)if(f())f(a);else a=2;")//
        .gives("for(int a=2;true;)if(f())f(a);else a=2;")//
        .gives("for(int a=2;;)if(f())f(a);else a=2;")//
        .stays();
  }
  @Test public void inlineSingleVariableintoPlusPlus() {
    trimmingOf("int $=0;if(a)++$;else --$;")//
        .stays();
  }
  @Test public void inliningWithVariableAssignedTo() {
    trimmingOf("int a=3,b=5;if(a==4)if(b==3)b=2;else{b=a;b=3;}else if(b==3)b=2;else{b=a*a;b=3;}")
        .gives("int b=5;if(3==4)if(b==3)b=2;else{b=3;b=3;}else if(b==3)b=2;else{b=3*3;b=3;}");
  }
  @Test public void isGreaterTrue() {
    final InfixExpression e = i("f(a,b,c,d,e)* f(a,b,c)");
    assert e != null;
    azzert.that(right(e) + "", is("f(a,b,c)"));
    azzert.that(left(e) + "", is("f(a,b,c,d,e)"));
    final Tipper<InfixExpression> s = Toolboxes.all().firstTipper(e);
    assert s != null;
    azzert.that(s, instanceOf(InfixMultiplicationSort.class));
    assert s.check(e);
    final Expression e1 = left(e), e2 = right(e);
    assert !has.nil(e1, e2);
    assert countOf.nodes(e1) > countOf.nodes(e2) + NODES_THRESHOLD;
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
    final Tipper<InfixExpression> s = Toolboxes.all().firstTipper(e);
    assert s != null;
    azzert.that(s, instanceOf(InfixMultiplicationSort.class));
    assert s.check(e);
    final Expression e1 = left(e), e2 = right(e);
    assert !has.nil(e1, e2);
    assert countOf.nodes(e1) <= countOf.nodes(e2) + NODES_THRESHOLD;
    assert moreArguments(e1, e2);
    assert longerFirst(e);
    assert s.check(e) : "e=" + e + " s=" + s;
    final ASTNode replacement = ((ReplaceCurrentNode<InfixExpression>) s).replacement(e);
    assert replacement != null;
    azzert.that(replacement + "", is("f(a,b,c) * f(a,b,c,d)"));
  }
  @Test public void issue06() {
    trimmingOf("a*-b")//
        .gives("-a * b");
  }
  @Test public void issue06B() {
    trimmingOf("x/a*-b/-c*- - - d / -d")//
        .gives("x/a * b/ c * d/d")//
        .gives("d*x/a*b/c/d");
  }
  @Test public void issue06C1() {
    trimmingOf("a*-b/-c*- - - d / d")//
        .gives("-a * b/ c * d/d");
  }
  @Test public void issue06C4() {
    trimmingOf("-a * b/ c ")//
        .stays();
  }
  @Test public void issue06D() {
    trimmingOf("a*b*c*d*-e")//
        .gives("-a*b*c*d*e")//
        .stays();
  }
  @Test public void issue06E() {
    trimmingOf("-a*b*c*d*f*g*h*i*j*k")//
        .stays();
  }
  @Test public void issue06F() {
    trimmingOf("x*a*-b*-c*- - - d * d")//
        .gives("-x*a*b*c*d*d")//
        .stays();
  }
  @Test public void issue06G() {
    trimmingOf("x*a*-b*-c*- - - d / d")//
        .gives("-x*a*b*c*d/d")//
        .stays();
  }
  @Test public void issue06H() {
    trimmingOf("x/a*-b/-c*- - - d ")//
        .gives("-x/a * b/ c * d");
  }
  @Test public void issue06I() {
    trimmingOf("41 * - 19")//
        .gives("-779 ");
  }
  @Test public void issue06J() {
    trimmingOf("41 * a * - 19")//
        .gives("-41*a*19")//
        .gives("-41*19*a");
  }
  @Test public void issue110_01() {
    trimmingOf("polite ? \"Eat your meal.\" : \"Eat your meal, please\"")//
        .gives("\"Eat your meal\" +(polite ? \".\" : \", please\")");
  }
  @Test public void issue110_02() {
    trimmingOf("polite ? \"Eat your meal.\" : \"Eat your meal\"")//
        .gives("\"Eat your meal\" +(polite ? \".\" : \"\")");
  }
  @Test public void issue110_03() {
    trimmingOf("polite ? \"thanks for the meal\" : \"I hated the meal\"")//
        .gives("!polite ? \"I hated the meal\": \"thanks for the meal\"")//
        .gives("(!polite ? \"I hated\" : \"thanks for\" )+ \" the meal\"");
  }
  @Test public void issue110_04() {
    trimmingOf("polite ? \"thanks.\" : \"I hated the meal.\"")//
        .gives("(polite ? \"thanks\" : \"I hated the meal\")+\".\"");
  }
  @Test public void issue110_05() {
    trimmingOf("a ? \"abracadabra\" : \"abba\"")//
        .gives("!a ? \"abba\" : \"abracadabra\"")//
        .stays();
  }
  @Test public void issue110_06() {
    trimmingOf("receiver==null ? \"Use \" + \"x\" : \"Use \" + receiver")//
        .gives("\"Use \"+(receiver==null ? \"x\" : receiver)")//
        .stays();
  }
  @Test public void issue110_07() {
    trimmingOf("receiver==null ? \"Use x\" : \"Use \" + receiver")//
        .gives("\"Use \"+(receiver==null ? \"x\" : \"\"+receiver)");
  }
  @Test public void issue110_08() {
    trimmingOf("receiver==null ? \"Use\" : receiver + \"Use\"")//
        .gives("(receiver==null ? \"\" : receiver+\"\")+ \"Use\"")//
        .stays();
  }
  @Test public void issue110_09() {
    trimmingOf("receiver==null ? \"user a\" : receiver + \"something a\"")//
        .gives("(receiver==null ? \"user\" : receiver+\"something\")+ \" a\"")//
        .stays();
  }
  @Test public void issue110_10() {
    trimmingOf("receiver==null ? \"Something Use\" : \"Something\" + receiver + \"Use\"")
        .gives("\"Something\"+(receiver==null ? \" Use\" : \"\"+receiver + \"Use\")")
        .gives("\"Something\"+((receiver==null ? \" \" : \"\"+receiver+\"\")+ \"Use\")");
  }
  @Test public void issue110_11() {
    trimmingOf("f()? \"first\" + d()+ \"second\" : \"first\" + g()+ \"third\"")
        .gives("\"first\" +(f()? \"\" + d()+ \"second\" : \"\" + g()+ \"third\")");
  }
  @Test public void issue110_12() {
    trimmingOf("f()? \"first\" + d()+ \"second\" : \"third\" + g()+ \"second\"")
        .gives("(f()? \"first\" + d()+ \"\": \"third\" + g()+\"\")+ \"second\"");
  }
  @Test public void issue110_13() {
    trimmingOf("f()? \"first is:\" + d()+ \"second\" : \"first are:\" + g()+ \"and second\"")
        .gives("\"first \" +(f()? \"is:\" + d()+ \"second\": \"are:\" + g()+ \"and second\")")
        .gives("\"first \" +((f()? \"is:\" + d()+ \"\": \"are:\" + g()+ \"and \")+ \"second\")");
  }
  @Test public void issue110_14() {
    trimmingOf("x==null ? \"Use isEmpty()\" : \"Use \" + x + \".isEmpty()\"").gives("\"Use \" +(x==null ? \"isEmpty()\" : \"\"+x + \".isEmpty()\")")
        .gives("\"Use \" +((x==null ? \"\" : \"\"+ x + \".\")+\"isEmpty()\")");
  }
  @Test public void issue110_15() {
    trimmingOf("$.setName(b.simpleName(booleanLiteral ? \"TRU\" : \"TALS\"));")//
        .stays();
  }
  @Test public void issue110_16() {
    trimmingOf("$.setName(b.simpleName(booleanLiteral ? \"TRUE\" : \"FALSE\"));")//
        .stays();
  }
  @Test public void issue110_17() {
    trimmingOf("$.setName(b.simpleName(booleanLiteral ? \"TRUE Story\" : \"FALSE Story\"));")
        .gives("$.setName(b.simpleName((booleanLiteral ? \"TRUE\" : \"FALSE\")+\" Story\"));");
  }
  @Test public void issue110_18() {
    trimmingOf("booleanLiteral==0 ? \"asss\" : \"assfad\"")//
        .stays();
  }
  @Test public void issue21a() {
    trimmingOf("a.equals(\"a\")")//
        .gives("\"a\".equals(a)");
  }
  @Test public void issue21b() {
    trimmingOf("a.equals(\"ab\")")//
        .gives("\"ab\".equals(a)");
  }
  @Test public void issue21d() {
    trimmingOf("a.equalsIgnoreCase(\"a\")")//
        .gives("\"a\".equalsIgnoreCase(a)");
  }
  @Test public void issue21e() {
    trimmingOf("a.equalsIgnoreCase(\"ab\")")//
        .gives("\"ab\".equalsIgnoreCase(a)");
  }
  @Test public void issue37SimplifiedVariant() {
    trimmingOf("int a=3;a +=31 * a;")//
        .gives("int a=3+31*3;");
  }
  @Test public void issue37WithSimplifiedBlock() {
    trimmingOf("if(a){ {} ;if(b)f();{} } else { g();f();++i;++j;}")//
        .gives("if(a){ if(b)f();} else { g();f();++i;++j;}");
  }
  @Test public void issue38() {
    trimmingOf("return o==null ? null : o==CONDITIONAL_AND ? CONDITIONAL_OR    : o==CONDITIONAL_OR ? CONDITIONAL_AND   : null;")//
        .stays();
  }
  @Test public void issue38Simplfiied() {
    trimmingOf(" o==CONDITIONAL_AND ? CONDITIONAL_OR   : o==CONDITIONAL_OR ? CONDITIONAL_AND   : null").stays();
  }
  @Test public void issue39base() {
    trimmingOf("if(name==null){ if(other.name !=null)return false; } else if(!name.equals(other.name))  return false; return true;")//
        .stays();
  }
  public void issue39baseDual() {
    trimmingOf("if(name !=null){ if(!name.equals(other.name))return false; } else if(other.name !=null)  return false; return true;")
        .gives("if(name==null){ if(other.name !=null)return false; } else if(!name.equals(other.name))  return false; return true;");
  }
  @Test(timeout = 100) public void issue39versionA() {
    trimmingOf("if(varArgs){ if(argumentTypes.length<parameterTypes.length - 1){ return false;} "
        + "} else if(parameterTypes.length !=argumentTypes.length){ return false; }")
            .gives("if(!varArgs){ if(parameterTypes.length !=argumentTypes.length){ return false;} "
                + "} else if(argumentTypes.length<parameterTypes.length - 1){ return false; }");
  }
  public void issue39versionAdual() {
    trimmingOf("if(!varArgs){ if(parameterTypes.length !=argumentTypes.length){ return false;} "
        + "} else if(argumentTypes.length<parameterTypes.length - 1){ return false; }")//
            .stays();
  }
  @Test public void issue41FunctionCall() {
    trimmingOf("int a=f();a +=2;")//
        .gives("int a=f()+2;");
  }
  @Test public void issue46() {
    trimmingOf("int f(){ x++;y++;if(a){ i++; j++; k++;} }")//
        .gives("int f(){++x;++y;if(!a)return;i++;j++;k++;}") //
        .gives("int f(){++x;++y;if(!a)return;++i;++j;++k;}") //
    ;
  }
  @Test public void issue49() {
    trimmingOf("int g(){ int f=0;for(int i: X)$ +=f(i);return f;}")//
        .gives("int g(){ int f=0;for(int ¢: X)$ +=f(¢);return f;}")//
        .stays();
  }
  @Test public void issue51() {
    trimmingOf("int f(){ int x=0;for(int i=0;i<10;++i)x +=i;return x;}").gives("int f(){ int $=0;for(int i=0;i<10;++i)$ +=i;return $;}")
        .gives("int f(){ int $=0;for(int ¢=0;¢<10;++¢)$ +=¢;return $;}")//
        .stays();
  }
  @Test public void issue51g() {
    trimmingOf("abstract abstract interface a{}")//
        .gives("abstract interface a {}")//
        .gives("interface a {}")//
        .stays();
  }
  @Test public void issue53() {
    trimmingOf("int[] is=f();for(int i: is)f(i);")//
        .gives("for(int i: f())f(i);")//
        .gives("for(int ¢: f())f(¢);")//
        .stays()//
    ;
  }
  @Test public void issue53a() {
    trimmingOf("int f(){ int x=0;for(int i=0;i<10;++i)x +=i;return x;}").gives("int f(){ int $=0;for(int i=0;i<10;++i)$ +=i;return $;}");
  }
  @Test public void issue54DoNonSideEffect() {
    trimmingOf("int a=f;do { b[i]=a;} while(b[i] !=a);")//
        .gives("do { b[i]=f;} while(b[i] !=f);");
  }
  @Test public void issue54DoNonSideEffectEmptyBody() {
    trimmingOf("int a=f();do ;while(a !=1);")//
        .stays();
  }
  @Test public void issue54DoWhile() {
    trimmingOf("int a=f();do { b[i]=2;++i;} while(b[i] !=a);")//
        .gives("int a=f();do { b[i++]=2;} while(b[i] !=a);");
  }
  @Test public void issue54DoWithBlock() {
    trimmingOf("int a=f();do { b[i]=a;++i;} while(b[i] !=a);")//
        .gives("int a=f();do { b[i++]=a;} while(b[i] !=a);");
  }
  @Test public void issue54doWithoutBlock() {
    trimmingOf("int a=f();do b[i]=a;while(b[i] !=a);")//
        .stays();
  }
  @Test public void issue54ForEnhanced() {
    trimmingOf("int a=f();for(int i: a)b[i]=x;")//
        .gives("for(int i: f())b[i]=x;");
  }
  @Test public void issue54ForEnhancedNonSideEffectLoopHeader() {
    trimmingOf("int a=f;for(int i: a)b[i]=b[i-1];")//
        .gives("for(int i: f)b[i]=b[i-1];");
  }
  @Test public void issue54ForEnhancedNonSideEffectWithBody() {
    trimmingOf("int a=f;for(int i: j)b[i]=a;")//
        .gives("for(int i:j)b[i]=f;");
  }
  @Test public void issue54ForPlainNonSideEffect() {
    trimmingOf("int a=f;for(int i=0;i<100;++i)b[i]=a;")//
        .gives("for(int i=0;i<100;++i)b[i]=f;");
  }
  @Test public void issue54ForPlainUseInConditionNonSideEffect() {
    trimmingOf("int a=f;for(int i=0;a<100;++i)b[i]=3;")//
        .gives("for(int i=0;f<100;++i)b[i]=3;");
  }
  @Test public void issue54ForPlainUseInInitializerNonSideEffect() {
    trimmingOf("int a=f;for(int i=a;i<100;i *=a)b[i]=3;")//
        .gives("for(int i=f;i<100;i *=f)b[i]=3;");
  }
  @Test public void issue54ForPlainUseInUpdatersNonSideEffect() {
    trimmingOf("int a=f;for(int i=0;i<100;i *=a)b[i]=3;")//
        .gives("for(int i=0;i<100;i *=f)b[i]=3;");
  }
  @Test public void issue54WhileNonSideEffect() {
    trimmingOf("int a=f;while(c)b[i]=a;")//
        .gives("while(c)b[i]=f;");
  }
  @Test public void issue54WhileScopeDoesNotInclude() {
    included("int a=f();while(c)b[i]=a;", VariableDeclarationFragment.class).notIn(new LocalInitializedStatementTerminatingScope());
  }
  @Test public void issue62b_1() {
    trimmingOf("int f(int ixx){ for(;ixx<100;ixx=ixx+1)if(false)return;return ixx;}")//
        .gives("int f(int ixx){ for(;ixx<100;ixx+=1){} return ixx;}");
  }
  @Test public void issue62c() {
    trimmingOf("int f(int ixx){ while(++ixx> 999)if(ixx>99)break;return ixx;}")//
        .stays();
  }
  @Test public void issue64a() {
    trimmingOf("void f(){ final int a=f();new Object(){ @Override public int hashCode(){ return a;} };}").stays();
  }
  @Test public void issue73a() {
    trimmingOf("void foo(StringBuilder sb){}")//
        .gives("void foo(StringBuilder b){}");
  }
  @Test public void issue73b() {
    trimmingOf("void foo(DataOutput dataOutput){}")//
        .gives("void foo(DataOutput o){}");
  }
  @Test public void linearTransformation() {
    trimmingOf("plain * the + kludge")//
        .gives("the*plain+kludge");
  }
  @Test public void literalVsLiteral() {
    trimmingOf("1<102333")//
        .stays();
  }
  @Test public void localAssignmentUpdateWithIncrement() {
    trimmingOf("int a=0;a+=++a;")//
        .stays();
  }
  @Test public void localAssignmentUpdateWithPostIncrement() {
    trimmingOf("int a=0;a+=a++;")//
        .stays();
  }
  @Test public void localAssignmentWithIncrement() {
    trimmingOf("int a=0;a=++a;")//
        .stays();
  }
  @Test public void localAssignmentWithPostIncrement() {
    trimmingOf("int a=0;a=a++;")//
        .stays();
  }
  @Test public void localIfAssignment() {
    trimmingOf("String u=s;if(s.equals(y))u=s + blah;S.h(u);").gives("String u=s.equals(y)? s + blah :s;S.h(u);");
  }
  @Test public void localIfAssignment3() {
    trimmingOf("int a=2;if(a !=2)a=3;")//
        .gives("int a=2 !=2 ? 3 : 2;");
  }
  @Test public void localIfAssignment4() {
    trimmingOf("int a=2;if(x)a=2*a;")//
        .gives("int a=x ? 2*2: 2;");
  }
  @Test public void localIfUsesLaterVariable() {
    trimmingOf("int a=0, b=0;if(b==3)a=4;")//
        .gives("int a=0;if(0==3)a=4;")//
        .gives("int a=0==3?4:0;");
  }
  @Test public void localInitializeRightShift() {
    trimmingOf("int a=3;a>>=2;")//
        .gives("int a=3>> 2;");
  }
  @Test public void localInitializerReturnAssignment() {
    trimmingOf("int a=3;return a=2 * a;")//
        .gives("return 2 * 3;");
  }
  @Test public void localInitializerReturnExpression() {
    trimmingOf("String tipper=Bob + Wants + To + \"Sleep \";return(right_now + tipper);").gives("return(right_now+(Bob+Wants+To+\"Sleep \"));");
  }
  @Test public void localInitializesRotate() {
    trimmingOf("int a=3;a>>>=2;")//
        .gives("int a=3>>> 2;");
  }
  @Test public void localInitializeUpdateAnd() {
    trimmingOf("int a=3;a&=2;")//
        .gives("int a=3 & 2;");
  }
  @Test public void localInitializeUpdateAssignment() {
    trimmingOf("int a=3;a +=2;")//
        .gives("int a=3+2;");
  }
  @Test public void localInitializeUpdateAssignmentFunctionCallWithReuse() {
    trimmingOf("int a=f();a +=2*f();")//
        .gives("int a=f()+2*f();");
  }
  @Test public void localInitializeUpdateAssignmentFunctionCallWIthReuse() {
    trimmingOf("int a=x;a +=a + 2*f();")//
        .gives("int a=x+(x+2*f());");
  }
  @Test public void localInitializeUpdateAssignmentIncrement() {
    trimmingOf("int a=++i;a +=j;")//
        .gives("int a=++i + j;");
  }
  @Test public void localInitializeUpdateAssignmentIncrementTwice() {
    trimmingOf("int a=++i;a +=a + j;")//
        .stays();
  }
  @Test public void localInitializeUpdateAssignmentWithReuse() {
    trimmingOf("int a=3;a +=2*a;")//
        .gives("int a=3+2*3;");
  }
  @Test public void localInitializeUpdateDividies() {
    trimmingOf("int a=3;a/=2;")//
        .gives("int a=3 / 2;");
  }
  @Test public void localInitializeUpdateLeftShift() {
    trimmingOf("int a=3;a<<=2;")//
        .gives("int a=3<<2;");
  }
  @Test public void localInitializeUpdateMinus() {
    trimmingOf("int a=3;a-=2;")//
        .gives("int a=3 - 2;");
  }
  @Test public void localInitializeUpdateModulo() {
    trimmingOf("int a=3;a%=2;")//
        .gives("int a=3 % 2;");
  }
  @Test public void localInitializeUpdatePlus() {
    trimmingOf("int a=3;a+=2;")//
        .gives("int a=3 + 2;");
  }
  @Test public void localInitializeUpdateTimes() {
    trimmingOf("int a=3;a*=2;")//
        .gives("int a=3 * 2;");
  }
  @Test public void localInitializeUpdateXor() {
    trimmingOf("int a=3;a^=2;")//
        .gives("int a=3 ^ 2;");
  }
  @Test public void localInitializeUpdatOr() {
    trimmingOf("int a=3;a|=2;")//
        .gives("int a=3 | 2;");
  }
  @Test public void localUpdateReturn() {
    trimmingOf("int a=3;return a +=2;")//
        .gives("return 3 + 2;");
  }
  @Test public void localUpdateReturnTwice() {
    trimmingOf("int a=3;return a +=2 * a;")//
        .gives("return 3 + 2 *3 ;");
  }
  @Test public void longChainComparison() {
    trimmingOf("a==b==c==d")//
        .stays();
  }
  @Test public void longChainParenthesisComparison() {
    trimmingOf("(a==b==c)==d")//
        .stays();
  }
  @Test public void longChainParenthesisNotComparison() {
    trimmingOf("(a==b==c)!=d")//
        .stays();
  }
  @Test public void longerChainParenthesisComparison() {
    trimmingOf("(a==b==c==d==e)==d")//
        .stays();
  }
  /* @Test public void massiveInlining(){
   * trimmingOf("int a,b,c;String tipper=zE4;if(2 * 3.1415 * 180> a || tipper.concat(sS)==1922 && tipper.length()> 3)return c> 5;"
   * )
   * .gives("int a,b,c;if(2 * 3.1415 * 180>a||zE4.concat(sS)==1922&&zE4.length()>3)return c>5;"
   * );} */
  @Test public void methodWithLastIf() {
    trimmingOf("int f(){ if(a){ f();g();h();}}")//
        .gives("int f(){ if(!a)return;f();g();h();}");
  }
  @Test public void nestedIf1() {
    trimmingOf("if(a)if(b)i++;")//
        .gives("if(a && b)i++;");
  }
  @Test public void nestedIf2() {
    trimmingOf("if(a)if(b)i++;else ;else ;")//
        .gives("if(a && b)i++;else ;");
  }
  @Test public void nestedIf3() {
    trimmingOf("if(x)if(a)if(b)i++;else ;else ;else { y++;f();g();z();}")//
        .gives("if(x)if(a&&b)i++;else;else{++y;f();g();z();}");
  }
  @Test public void nestedIf33() {
    trimmingOf("if(x){if(a&&b)i++;else;}else{++y;f();g();}")//
        .gives("if(x)if(a&&b)i++;else;else{++y;f();g();}")//
        .gives("if(x){if(a&&b)i++;}else{++y;f();g();}")//
        .gives("if(x){if(a&&b)++i;}else{++y;f();g();}");
  }
  @Test public void nestedIf33a() {
    trimmingOf("if(x){ if(a && b)i++;} else { y++;f();g();}")//
        .gives("if(x){if(a&&b)++i;} else{++y;f();g();}");
  }
  @Test public void nestedIf33b() {
    trimmingOf("if(x)if(a && b)i++;else;else { y++;f();g();}")//
        .gives("if(x){if(a&&b)i++;}else{++y;f();g();}");
  }
  @Test public void nestedIf3c() {
    trimmingOf("if(x)if(a && b)i++;else;else { y++;f();g();}")//
        .gives("if(x){if(a&&b)i++;} else {++y;f();g();}");
  }
  @Test public void nestedIf3d() {
    trimmingOf("if(x)if(a)if(b)i++;else ;else ;else { y++;f();g();z();}")//
        .gives("if(x)if(a&&b)i++;else;else{++y;f();g();z();}")//
        .gives("if(x){if(a&&b)i++;} else{++y;f();g();z();}")//
        .gives("if(x){if(a&&b)++i;} else{++y;f();g();z();}");
  }
  @Test public void nestedIf3e() {
    trimmingOf("if(x)if(a)if(b)i++;else ;else ;else { y++;f();g();z();}")//
        .gives("if(x)if(a&&b)i++;else;else{++y;f();g();z();}")//
        .gives("if(x){if(a&&b)i++;}else{++y;f();g();z();}");
  }
  @Test public void nestedIf3f() {
    trimmingOf("if(x){if(a&&b)i++;else;}else{++y;f();g();}")//
        .gives("if(x)if(a&&b)i++;else;else{++y;f();g();}")//
        .gives("if(x){if(a&&b)i++;}else{++y;f();g();}");
  }
  @Test public void nestedIf3f1() {
    trimmingOf("if(x)if(a&&b)i++;else;else{++y;f();g();}")//
        .gives("if(x){if(a&&b)i++;}else{++y;f();g();}");
  }
  @Test public void nestedIf3x() {
    trimmingOf("if(x)if(a)if(b)i++;else ;else ;else { y++;f();g();z();}")//
        .gives("if(x)if(a&&b)i++;else;else{++y;f();g();z();}")//
        .gives("if(x){if(a&&b)i++;}else{++y;f();g();z();}");
  }
  /* @Test public void nestedTernaryAlignment(){
   * trimmingOf("int b=3==4?5==3?2:3:5==3?2:3*3;")//
   * .gives("int b=3==4?5==3?2:3:5!=3?3*3:2;");} */
  @Test public void noChange() {
    trimmingOf("12")//
        .stays();
    trimmingOf("true")//
        .stays();
    trimmingOf("null")//
        .stays();
    trimmingOf("on*of*no*notion*notion")//
        .gives("no*of*on*notion*notion");
  }
  @Test public void noChange0() {
    trimmingOf("kludge + the * plain ")//
        .stays();
  }
  @Test public void noChange1() {
    trimmingOf("the * plain")//
        .stays();
  }
  @Test public void noChange2() {
    trimmingOf("plain + kludge")//
        .stays();
  }
  @Test public void noChangeA() {
    trimmingOf("true")//
        .stays();
  }
  @Test public void noinliningintoSynchronizedStatement() {
    trimmingOf("int a=f();synchronized(this){ int b=a;f(++b);}")//
        .stays();
  }
  @Test public void noinliningintoSynchronizedStatementEvenWithoutSideEffect() {
    trimmingOf("int a=f;synchronized(this){ int b=a;f(++b);}")//
        .stays();
  }
  @Test public void noinliningintoTryStatement() {
    trimmingOf("int a=f();try { int b=a;b(++b);} catch(Exception E){}")//
        .stays();
  }
  @Test public void noinliningintoTryStatementEvenWithoutSideEffect() {
    trimmingOf("int a=f;try { int b=a;f(++b);} catch(Exception E){}")//
        .stays();
  }
  @Test public void notOfAnd() {
    trimmingOf("!(A && B)")//
        .gives("!A || !B");
  }
  @Test public void oneMultiplication() {
    trimmingOf("f(a,b,c,d)* f(a,b,c)")//
        .gives("f(a,b,c)* f(a,b,c,d)");
  }
  @Test public void oneMultiplicationAlternate() {
    trimmingOf("f(a,b,c,d,e)* f(a,b,c)")//
        .gives("f(a,b,c)* f(a,b,c,d,e)");
  }
  @Test public void orFalse3ORTRUE() {
    trimmingOf("false || false || false")//
        .gives("false");
  }
  @Test public void orFalse4ORTRUE() {
    trimmingOf("false || false || false || false")//
        .gives("false");
  }
  @Test public void orFalseANDOf3WithoutBoolean() {
    trimmingOf("a && b && false")//
        .stays();
  }
  @Test public void orFalseANDOf3WithoutBooleanA() {
    trimmingOf("x && a && b")//
        .stays();
  }
  @Test public void orFalseANDOf3WithTrue() {
    trimmingOf("true && x && true && a && b")//
        .gives("x && a && b");
  }
  @Test public void orFalseANDOf3WithTrueA() {
    trimmingOf("a && b && true")//
        .gives("a && b");
  }
  @Test public void orFalseANDOf4WithoutBoolean() {
    trimmingOf("a && b && c && false")//
        .stays();
  }
  @Test public void orFalseANDOf4WithoutBooleanA() {
    trimmingOf("x && a && b && c")//
        .stays();
  }
  @Test public void orFalseANDOf4WithTrue() {
    trimmingOf("x && true && a && b && c")//
        .gives("x && a && b && c");
  }
  @Test public void orFalseANDOf4WithTrueA() {
    trimmingOf("a && b && c && true")//
        .gives("a && b && c");
  }
  @Test public void orFalseANDOf5WithoutBoolean() {
    trimmingOf("false && a && b && c && d")//
        .stays();
  }
  @Test public void orFalseANDOf5WithoutBooleanA() {
    trimmingOf("x && a && b && c && d")//
        .stays();
  }
  @Test public void orFalseANDOf5WithTrue() {
    trimmingOf("x && a && b && c && true && true && true && d")//
        .gives("x && a && b && c && d");
  }
  @Test public void orFalseANDOf5WithTrueA() {
    trimmingOf("true && a && b && c && d")//
        .gives("a && b && c && d");
  }
  @Test public void orFalseANDOf6WithoutBoolean() {
    trimmingOf("a && b && c && false && d && e")//
        .stays();
  }
  @Test public void orFalseANDOf6WithoutBooleanA() {
    trimmingOf("x && a && b && c && d && e")//
        .stays();
  }
  @Test public void orFalseANDOf6WithoutBooleanWithParenthesis() {
    trimmingOf("(x &&(a && b))&&(c &&(d && e))")//
        .stays();
  }
  @Test public void orFalseANDOf6WithTrue() {
    trimmingOf("x && a && true && b && c && d && e")//
        .gives("x && a && b && c && d && e");
  }
  @Test public void orFalseANDOf6WithTrueA() {
    trimmingOf("a && b && c && true && d && e")//
        .gives("a && b && c && d && e");
  }
  @Test public void orFalseANDOf6WithTrueWithParenthesis() {
    trimmingOf("x &&(true &&(a && b && true))&&(c &&(d && e))")//
        .gives("x && a && b && c && d && e");
  }
  @Test public void orFalseANDOf7WithMultipleTrueValue() {
    trimmingOf("(a &&(b && true))&&(c &&(d &&(e &&(true && true))))")//
        .gives("a &&b &&c &&d &&e ");
  }
  @Test public void orFalseANDOf7WithoutBooleanAndMultipleFalseValue() {
    trimmingOf("(a &&(b && false))&&(c &&(d &&(e &&(false && false))))")//
        .stays();
  }
  @Test public void orFalseANDOf7WithoutBooleanWithParenthesis() {
    trimmingOf("(a && b)&&(c &&(d &&(e && false)))")//
        .stays();
  }
  @Test public void orFalseANDOf7WithTrueWithParenthesis() {
    trimmingOf("true &&(a && b)&&(c &&(d &&(e && true)))")//
        .gives("a &&b &&c &&d &&e ");
  }
  @Test public void orFalseANDWithFalse() {
    trimmingOf("b && a")//
        .stays();
  }
  @Test public void orFalseANDWithoutBoolean() {
    trimmingOf("b && a")//
        .stays();
  }
  @Test public void orFalseANDWithTrue() {
    trimmingOf("true && b && a")//
        .gives("b && a");
  }
  @Test public void orFalseFalseOrFalse() {
    trimmingOf("false ||false")//
        .gives("false");
  }
  @Test public void orFalseORFalseWithSomething() {
    trimmingOf("true || a")//
        .stays();
  }
  @Test public void orFalseORFalseWithSomethingB() {
    trimmingOf("false || a || false")//
        .gives("a");
  }
  @Test public void orFalseOROf3WithFalse() {
    trimmingOf("x || false || b")//
        .gives("x || b");
  }
  @Test public void orFalseOROf3WithFalseB() {
    trimmingOf("false || a || b || false")//
        .gives("a || b");
  }
  @Test public void orFalseOROf3WithoutBoolean() {
    trimmingOf("a || b")//
        .stays();
  }
  @Test public void orFalseOROf3WithoutBooleanA() {
    trimmingOf("x || a || b")//
        .stays();
  }
  @Test public void orFalseOROf4WithFalse() {
    trimmingOf("x || a || b || c || false")//
        .gives("x || a || b || c");
  }
  @Test public void orFalseOROf4WithFalseB() {
    trimmingOf("a || b || false || c")//
        .gives("a || b || c");
  }
  @Test public void orFalseOROf4WithoutBoolean() {
    trimmingOf("a || b || c")//
        .stays();
  }
  @Test public void orFalseOROf4WithoutBooleanA() {
    trimmingOf("x || a || b || c")//
        .stays();
  }
  @Test public void orFalseOROf5WithFalse() {
    trimmingOf("x || a || false || c || d")//
        .gives("x || a || c || d");
  }
  @Test public void orFalseOROf5WithFalseB() {
    trimmingOf("a || b || c || d || false")//
        .gives("a || b || c || d");
  }
  @Test public void orFalseOROf5WithoutBoolean() {
    trimmingOf("a || b || c || d")//
        .stays();
  }
  @Test public void orFalseOROf5WithoutBooleanA() {
    trimmingOf("x || a || b || c || d")//
        .stays();
  }
  @Test public void orFalseOROf6WithFalse() {
    trimmingOf("false || x || a || b || c || d || e")//
        .gives("x || a || b || c || d || e");
  }
  @Test public void orFalseOROf6WithFalseWithParenthesis() {
    trimmingOf("x ||(a ||(false)|| b)||(c ||(d || e))")//
        .gives("x || a || b || c || d || e");
  }
  @Test public void orFalseOROf6WithFalseWithParenthesisB() {
    trimmingOf("(a || b)|| false ||(c || false ||(d || e || false))")//
        .gives("a || b || c || d || e");
  }
  @Test public void orFalseOROf6WithoutBoolean() {
    trimmingOf("a || b || c || d || e")//
        .stays();
  }
  @Test public void orFalseOROf6WithoutBooleanA() {
    trimmingOf("x || a || b || c || d || e")//
        .stays();
  }
  @Test public void orFalseOROf6WithoutBooleanWithParenthesis() {
    trimmingOf("(a || b)||(c ||(d || e))")//
        .stays();
  }
  @Test public void orFalseOROf6WithoutBooleanWithParenthesisA() {
    trimmingOf("x ||(a || b)||(c ||(d || e))")//
        .stays();
  }
  @Test public void orFalseOROf6WithTwoFalse() {
    trimmingOf("a || false || b || false || c || d || e")//
        .gives("a || b || c || d || e");
  }
  @Test public void orFalseORSomethingWithFalse() {
    trimmingOf("false || a || false")//
        .gives("a");
  }
  @Test public void orFalseORSomethingWithTrue() {
    trimmingOf("a || true")//
        .stays();
  }
  @Test public void orFalseORWithoutBoolean() {
    trimmingOf("b || a")//
        .stays();
  }
  @Test public void orFalseProductIsNotANDDivOR() {
    trimmingOf("2*a")//
        .stays();
  }
  @Test public void orFalseTrueAndTrueA() {
    trimmingOf("true && true")//
        .gives("true");
  }
  @Test public void paramAbbreviateBasic1() {
    trimmingOf("void m(XMLDocument xmlDocument, int abcd){xmlDocument.exec(p);}")//
        .gives("void m(XMLDocument d, int abcd){d.exec(p);}");
  }
  @Test public void paramAbbreviateBasic2() {
    trimmingOf("int m(StringBuilder builder, int abcd){if(builder.exec())builder.clear();")
        .gives("int m(StringBuilder b, int abcd){if(b.exec())b.clear();");
  }
  @Test public void paramAbbreviateCollision() {
    trimmingOf("void m(Expression exp, Expression expresssion){ }")//
        .gives("void m(Expression x, Expression expresssion){ }");
  }
  @Test public void paramAbbreviateConflictingWithLocal1() {
    trimmingOf("void m(String string){String s=null;string.substring(s, 2, 18);}").gives("void m(String string){string.substring(null,2,18);}");
  }
  @Test public void paramAbbreviateConflictingWithLocal1Simplified() {
    trimmingOf("void m(String string){String s=X;string.substring(s, 2, 18);}").gives("void m(String string){string.substring(X,2,18);}");
  }
  @Test public void paramAbbreviateConflictingWithLocal1SimplifiedFurther() {
    trimmingOf("void m(String string){String s=X;string.f(s);}")//
        .gives("void m(String string){string.f(X);}");
  }
  @Test public void paramAbbreviateConflictingWithLocal2() {
    trimmingOf("TCPConnection conn(TCPConnection tcpCon){ UDPConnection c=new UDPConnection(57);if(tcpCon.isConnected()) c.disconnect();}")
        .gives("TCPConnection conn(TCPConnection tcpCon){ if(tcpCon.isConnected())(new UDPConnection(57)).disconnect();}");
  }
  @Test public void paramAbbreviateConflictingWithMethodName() {
    trimmingOf("void m(BitmapManipulator bitmapManipulator, int __){bitmapManipulator.x().y();")//
        .stays();
  }
  @Test public void paramAbbreviateMultiple() {
    trimmingOf("void m(StringBuilder stringBuilder, XMLDocument xmlDocument, Dog dog, Dog cat){stringBuilder.clear();"
        + "xmlDocument.open(stringBuilder.toString());dog.eat(xmlDocument.asEdible(cat));}")
            .gives("void m(StringBuilder b, XMLDocument xmlDocument, Dog dog, Dog cat){b.clear();xmlDocument.open(b.toString());"
                + "dog.eat(xmlDocument.asEdible(cat));}");
  }
  @Test public void paramAbbreviateNestedMethod() {
    trimmingOf("void f(Iterator iterator){iterator=new Iterator<Object>(){int i=0;"
        + "@Override public boolean hasNext(){ return false;}@Override public Object next(){ return null;} };")
            .gives("void f(Iterator iterator){iterator=new Iterator<Object>(){int i;"
                + "@Override public boolean hasNext(){ return false;}@Override public Object next(){ return null;} };");
  }
  @Test public void parenthesizeOfpushdownTernary() {
    trimmingOf("a ? b+x+e+f:b+y+e+f")//
        .gives("b+(a ? x : y)+e+f");
  }
  @Test public void postDecreementReturn() {
    trimmingOf("a--;return a;")//
        .gives("--a;return a;");
  }
  @Test public void postDecremntInFunctionCall() {
    trimmingOf("f(a++, i--, b++, ++b);")//
        .stays();
  }
  @Test public void postfixToPrefix101() {
    trimmingOf("i++;")//
        .gives("++i;")//
        .stays();
  }
  @Test public void postfixToPrefixAvoidChangeOnLoopCondition() {
    trimmingOf("for(int ¢=i;++i;++¢);")//
        .stays();
  }
  @Test public void postfixToPrefixAvoidChangeOnVariableDeclaration() {
    trimmingOf("int s=2; x(s); int n=s++; S.out.print(n);") //
        .gives("int s=2; x(s); S.out.print(s++);");
  }
  @Test public void postIncrementInFunctionCall() {
    trimmingOf("f(i++);")//
        .stays();
  }
  @Test public void postIncrementReturn() {
    trimmingOf("a++;return a;")//
        .gives("++a;return a;");
  }
  @Test public void preDecreementReturn() {
    trimmingOf("--a.b.c;return a.b.c;")//
        .gives("return--a.b.c;");
  }
  @Test public void preDecrementReturn() {
    trimmingOf("--a;return a;")//
        .gives("return --a;");
  }
  @Test public void preDecrementReturn1() {
    trimmingOf("--this.a;return this.a;")//
        .gives("return --this.a;");
  }
  @Test public void prefixToPosfixIncreementSimple() {
    trimmingOf("i++")//
        .gives("++i");
  }
  @Test public void preIncrementReturn() {
    trimmingOf("++a;return a;")//
        .gives("return ++a;");
  }
  @Test public void pushdowConditionalActualExampleFirstPass() {
    trimmingOf("return determineEncoding(bytes)==Encoding.B ? f((ENC_WORD_PREFIX + mimeCharset + B), text, charset, bytes) "
        + ": f((ENC_WORD_PREFIX + mimeCharset + Q), text, charset, bytes) ;")
            .gives("return f( determineEncoding(bytes)==Encoding.B ? ENC_WORD_PREFIX+mimeCharset+B"
                + " : ENC_WORD_PREFIX+mimeCharset+Q,text,charset,bytes);");
  }
  @Test public void pushdowConditionalActualExampleSecondtest() {
    trimmingOf("return f( determineEncoding(bytes)==Encoding.B ? ENC_WORD_PREFIX+mimeCharset+B : ENC_WORD_PREFIX+mimeCharset+Q,text,charset,bytes);")
        .gives("return f( ENC_WORD_PREFIX + mimeCharset +(determineEncoding(bytes)==Encoding.B ?B : Q), text,charset,bytes);");
  }
  @Test public void pushdownNot2LevelNotOfFalse() {
    trimmingOf("!!false")//
        .gives("false");
  }
  @Test public void pushdownNot2LevelNotOfTrue() {
    trimmingOf("!!true")//
        .gives("true");
  }
  @Test public void pushdownNotActualExample() {
    trimmingOf("!inRange(m, e)")//
        .stays();
  }
  @Test public void pushdownNotDoubleNot() {
    trimmingOf("!!f()")//
        .gives("f()");
  }
  @Test public void pushdownNotDoubleNotDeeplyNested() {
    trimmingOf("!(((!f())))")//
        .gives("f()");
  }
  @Test public void pushdownNotDoubleNotNested() {
    trimmingOf("!(!f())")//
        .gives("f()");
  }
  @Test public void pushdownNotEND() {
    trimmingOf("a&&b")//
        .stays();
  }
  @Test public void pushdownNotMultiplication() {
    trimmingOf("a*b")//
        .stays();
  }
  @Test public void pushdownNotNotOfAND() {
    trimmingOf("!(a && b && c)")//
        .gives("!a || !b || !c");
  }
  @Test public void pushdownNotNotOfAND2() {
    trimmingOf("!(f()&& f(5))")//
        .gives("!f()|| !f(5)");
  }
  @Test public void pushdownNotNotOfANDNested() {
    trimmingOf("!(f()&&(f(5)))")//
        .gives("!f()|| !f(5)");
  }
  @Test public void pushdownNotNotOfEQ() {
    trimmingOf("!(3==5)")//
        .gives("3 !=5");
  }
  @Test public void pushdownNotNotOfEQNested() {
    trimmingOf("!((((3==5))))")//
        .gives("3 !=5");
  }
  @Test public void pushdownNotNotOfFalse() {
    trimmingOf("!false")//
        .gives("true");
  }
  @Test public void pushdownNotNotOfGE() {
    trimmingOf("!(3>=5)")//
        .gives("3<5");
  }
  @Test public void pushdownNotNotOfGT() {
    trimmingOf("!(3> 5)")//
        .gives("3<=5");
  }
  @Test public void pushdownNotNotOfLE() {
    trimmingOf("!(3<=5)")//
        .gives("3> 5");
  }
  @Test public void pushdownNotNotOfLT() {
    trimmingOf("!(3<5)")//
        .gives("3>=5");
  }
  @Test public void pushdownNotNotOfNE() {
    trimmingOf("!(3 !=5)")//
        .gives("3==5");
  }
  @Test public void pushdownNotNotOfOR() {
    trimmingOf("!(a || b || c)")//
        .gives("!a && !b && !c");
  }
  @Test public void pushdownNotNotOfOR2() {
    trimmingOf("!(f()|| f(5))")//
        .gives("!f()&& !f(5)");
  }
  @Test public void pushdownNotNotOfTrue() {
    trimmingOf("!true")//
        .gives("false");
  }
  @Test public void pushdownNotNotOfTrue2() {
    trimmingOf("!!true")//
        .gives("true");
  }
  @Test public void pushdownNotNotOfWrappedOR() {
    trimmingOf("!((a)|| b || c)")//
        .gives("!a && !b && !c");
  }
  @Test public void pushdownNotOR() {
    trimmingOf("a||b")//
        .stays();
  }
  @Test public void pushdownNotSimpleNot() {
    trimmingOf("!a")//
        .stays();
  }
  @Test public void pushdownNotSimpleNotOfFunction() {
    trimmingOf("!f(a)")//
        .stays();
  }
  @Test public void pushdownNotSummation() {
    trimmingOf("a+b")//
        .stays();
  }
  @Test public void pushdownTernaryActualExample() {
    trimmingOf("next<values().length")//
        .stays();
  }
  @Test public void pushdownTernaryActualExample2() {
    trimmingOf("!inRange(m, e)? true : inner.go(r, e)")//
        .gives("!inRange(m, e)|| inner.go(r, e)");
  }
  @Test public void pushdownTernaryAlmostIdentical2Addition() {
    trimmingOf("a ? b+d :b+ c")//
        .gives("b+(a ? d : c)");
  }
  @Test public void pushdownTernaryAlmostIdentical3Addition() {
    trimmingOf("a ? b+d +x:b+ c + x")//
        .gives("b+(a ? d : c)+ x");
  }
  @Test public void pushdownTernaryAlmostIdentical4AdditionLast() {
    trimmingOf("a ? b+d+e+y:b+d+e+x")//
        .gives("b+d+e+(a ? y : x)");
  }
  @Test public void pushdownTernaryAlmostIdentical4AdditionSecond() {
    trimmingOf("a ? b+x+e+f:b+y+e+f")//
        .gives("b+(a ? x : y)+e+f");
  }
  @Test public void pushdownTernaryAlmostIdenticalAssignment() {
    trimmingOf("a ?(b=c):(b=d)")//
        .gives("b=a ? c : d");
  }
  @Test public void pushdownTernaryAlmostIdenticalFunctionCall() {
    trimmingOf("a ? f(b):f(c)")//
        .gives("f(a ? b : c)");
  }
  @Test public void pushdownTernaryAlmostIdenticalMethodCall() {
    trimmingOf("a ? y.f(b):y.f(c)")//
        .gives("y.f(a ? b : c)");
  }
  @Test public void pushdownTernaryAlmostIdenticalTwoArgumentsFunctionCall1Div2() {
    trimmingOf("a ? f(b,x):f(c,x)")//
        .gives("f(a ? b : c,x)");
  }
  @Test public void pushdownTernaryAlmostIdenticalTwoArgumentsFunctionCall2Div2() {
    trimmingOf("a ? f(x,b):f(x,c)")//
        .gives("f(x,a ? b : c)");
  }
  @Test public void pushdownTernaryAMethodCallDistinctReceiver() {
    trimmingOf("a ? x.f(c): y.f(d)")//
        .stays();
  }
  @Test public void pushdownTernaryDifferentTargetFieldRefernce() {
    trimmingOf("a ? 1 + x.a : 1 + y.a")//
        .gives("1+(a ? x.a : y.a)");
  }
  @Test public void pushdownTernaryFieldReferneceShort() {
    trimmingOf("a ? R.b.c : R.b.d")//
        .stays();
  }
  @Test public void pushdownTernaryFunctionCall() {
    trimmingOf("a ? f(b,c): f(c)")//
        .gives("!a?f(c):f(b,c)");
  }
  @Test public void pushdownTernaryFX() {
    trimmingOf("a ? false : c")//
        .gives("!a && c");
  }
  @Test public void pushdownTernaryIdenticalAddition() {
    trimmingOf("a ? b+d :b+ d")//
        .gives("b+d");
  }
  @Test public void pushdownTernaryIdenticalAdditionWtihParenthesis() {
    trimmingOf("a ?(b+d):(b+ d)")//
        .gives("(b+d)");
  }
  @Test public void pushdownTernaryIdenticalAssignment() {
    trimmingOf("a ?(b=c):(b=c)")//
        .gives("(b=c)");
  }
  @Test public void pushdownTernaryIdenticalAssignmentVariant() {
    trimmingOf("a ?(b=c):(b=d)")//
        .gives("b=a?c:d");
  }
  @Test public void pushdownTernaryIdenticalFunctionCall() {
    trimmingOf("a ? f(b):f(b)")//
        .gives("f(b)");
  }
  @Test public void pushdownTernaryIdenticalIncrement() {
    trimmingOf("a ? b++ :b++")//
        .gives("b++");
  }
  @Test public void pushdownTernaryIdenticalMethodCall() {
    trimmingOf("a ? y.f(b):y.f(b)")//
        .gives("y.f(b)");
  }
  @Test public void pushdownTernaryintoConstructor1Div1Location() {
    trimmingOf("a.equal(b)? new S(new Integer(4)): new S(new Ineger(3))")//
        .gives("new S(a.equal(b)? new Integer(4): new Ineger(3))");
  }
  @Test public void pushdownTernaryintoConstructor1Div3() {
    trimmingOf("a.equal(b)? new S(new Integer(4),a,b): new S(new Ineger(3),a,b)")//
        .gives("new S(a.equal(b)? new Integer(4): new Ineger(3), a, b)");
  }
  @Test public void pushdownTernaryintoConstructor2Div3() {
    trimmingOf("a.equal(b)? new S(a,new Integer(4),b): new S(a, new Ineger(3), b)")//
        .gives("new S(a,a.equal(b)? new Integer(4): new Ineger(3),b)");
  }
  @Test public void pushdownTernaryintoConstructor3Div3() {
    trimmingOf("a.equal(b)? new S(a,b,new Integer(4)): new S(a,b,new Ineger(3))")//
        .gives("new S(a, b, a.equal(b)? new Integer(4): new Ineger(3))");
  }
  @Test public void pushdownTernaryintoConstructorNotSameArity() {
    trimmingOf("a ? new S(a,new Integer(4),b): new S(new Ineger(3))")//
        .gives("!a?new S(new Ineger(3)):new S(a,new Integer(4),b)             ");
  }
  @Test public void pushdownTernaryintoPrintln() {
    trimmingOf("if(s.equals(tipper))S.h(Hey + u);else S.h(Ho + x + a);").gives("S.h(s.equals(tipper)?Hey+u:Ho+x+a);");
  }
  @Test public void pushdownTernaryLongFieldRefernece() {
    trimmingOf("externalImage ? R.string.webview_contextmenu_image_download_action : R.string.webview_contextmenu_image_save_action")
        .gives("!externalImage ? R.string.webview_contextmenu_image_save_action : R.string.webview_contextmenu_image_download_action");
  }
  @Test public void pushdownTernaryMethodInvocationFirst() {
    trimmingOf("a?b():c")//
        .gives("!a?c:b()");
  }
  @Test public void pushdownTernaryNoBoolean() {
    trimmingOf("a?b:c")//
        .stays();
  }
  @Test public void pushdownTernaryNoReceiverReceiver() {
    trimmingOf("a<b ? f(): a.f()")//
        .stays();
  }
  @Test public void pushdownTernaryNotOnMINUS() {
    trimmingOf("a ? -c :-d")//
        .stays();
  }
  @Test public void pushdownTernaryNotOnMINUSMINUS1() {
    trimmingOf("a ? --c :--d")//
        .stays();
  }
  @Test public void pushdownTernaryNotOnMINUSMINUS2() {
    trimmingOf("a ? c-- :d--")//
        .stays();
  }
  @Test public void pushdownTernaryNotOnNOT() {
    trimmingOf("a ? !c :!d")//
        .stays();
  }
  @Test public void pushdownTernaryNotOnPLUS() {
    trimmingOf("a ? +x : +y")//
        .gives("a ? x : y")//
        .stays();
  }
  @Test public void pushdownTernaryNotOnPLUSPLUS() {
    trimmingOf("a ? x++ :y++")//
        .stays();
  }
  @Test public void pushdownTernaryNotSameFunctionInvocation() {
    trimmingOf("a?b(x):d(x)")//
        .stays();
  }
  @Test public void pushdownTernaryNotSameFunctionInvocation2() {
    trimmingOf("a?x.f(x):x.d(x)")//
        .stays();
  }
  @Test public void pushdownTernaryOnMethodCall() {
    trimmingOf("a ? y.f(c,b):y.f(c)")//
        .gives("!a?y.f(c):y.f(c,b)");
  }
  @Test public void pushdownTernaryParFX() {
    trimmingOf("a ? false:true")//
        .gives("!a && true");
  }
  @Test public void pushdownTernaryParTX() {
    trimmingOf("a ?true: c")//
        .gives("a || c");
  }
  @Test public void pushdownTernaryParXF() {
    trimmingOf("a ? b :false")//
        .gives("a && b");
  }
  @Test public void pushdownTernaryParXT() {
    trimmingOf("a ? b :true")//
        .gives("!a || b");
  }
  @Test public void pushdownTernaryReceiverNoReceiver() {
    trimmingOf("a<b ? a.f(): f()")//
        .gives("a>=b?f():a.f()");
  }
  @Test public void pushdownTernaryToClasConstrctor() {
    trimmingOf("a ? new B(a,b,c): new B(a,x,c)")//
        .gives("new B(a,a ? b : x ,c)");
  }
  @Test public void pushdownTernaryToClasConstrctorTwoDifferenes() {
    trimmingOf("a ? new B(a,b,c): new B(a,x,y)")//
        .stays();
  }
  @Test public void pushdownTernaryToClassConstrctorNotSameNumberOfArgument() {
    trimmingOf("a ? new B(a,b): new B(a,b,c)")//
        .stays();
  }
  @Test public void pushdownTernaryTX() {
    trimmingOf("a ? true : c")//
        .gives("a || c");
  }
  @Test public void pushdownTernaryXF() {
    trimmingOf("a ? b : false")//
        .gives("a && b");
  }
  @Test public void pushdownTernaryXT() {
    trimmingOf("a ? b : true")//
        .gives("!a || b");
  }
  @Test public void redundantButNecessaryBrackets1() {
    trimmingOf("if(windowSize !=INFINITE_WINDOW){ if(getN()==windowSize)eDA.addElementRolling(variableDeclarationFragment); "
        + " else if(getN()<windowSize)eDA.addElement(variableDeclarationFragment); } else { System.h('!'); "
        + " System.h('!');System.h('!');System.h('!');System.h('!');System.h('!');System.h('!'); "
        + " eDA.addElement(variableDeclarationFragment); }")//
            .stays();
  }
  @Test public void redundantButNecessaryBrackets2() {
    trimmingOf("if(windowSize !=INFINITE_WINDOW){ if(getN()==windowSize)eDA.addElementRolling(variableDeclarationFragment); "
        + "} else { System.h('!');System.h('!');System.h('!');System.h('!');System.h('!'); "
        + " System.h('!');System.h('!');eDA.addElement(variableDeclarationFragment); }")//
            .stays();
  }
  @Test public void redundantButNecessaryBrackets3() {
    trimmingOf("if(b1)if(b2)print1('!');else { if(b3)print3('#');} else {  print4('$');print4('$');print4('$');print4('$');print4('$');print4('$'); "
        + " print4('$');print4('$');print4('$');print4('$');print4('$'); }").gives(
            "if(b1)if(b2)print1('!');else  if(b3)print3('#'); else {  print4('$');print4('$');print4('$');print4('$');print4('$');print4('$'); "
                + " print4('$');print4('$');print4('$');print4('$');print4('$'); }");
  }
  @Test public void removeSuper() {
    trimmingOf("class T {T(){super();}}")//
        .gives("class T { T(){ }}");
  }
  @Test public void removeSuperWithArgument() {
    trimmingOf("class T { T(){ super(a);a();}}")//
        .stays();
  }
  @Test public void removeSuperWithReceiver() {
    trimmingOf("class X{X(Y o){o.super();}}")//
        .stays();
  }
  @Test public void removeSuperWithStatemen() {
    trimmingOf("class T { T(){ super();a++;}}")//
        .gives("class T { T(){ ++a;}}");
  }
  @Test public void renameToDollarActual() {
    trimmingOf("public static DeletePolicy fromInt(int initialSetting){  for(DeletePolicy policy: values()){ "
        + "  if(policy.setting==initialSetting){   return policy; } "
        + "  }  throw new IllegalArgumentException(\"DeletePolicy \" + initialSetting + \" unknown\");}")
            .gives("public static DeletePolicy fromInt(int initialSetting){  for(DeletePolicy $: values()){ "
                + "  if($.setting==initialSetting){   return $; } "
                + "  }  throw new IllegalArgumentException(\"DeletePolicy \" + initialSetting + \" unknown\");  }");
  }
  @Test public void renameToDollarEnhancedFor() {
    trimmingOf("int f(){ for(int a: as)return a;}")//
        .gives("int f(){for(int $:as)return $;}");
  }
  @Test public void renameUnusedVariableToDoubleUnderscore1() {
    trimmingOf("void f(int x){System.h(x);}")//
        .stays();
  }
  @Test public void renameUnusedVariableToDoubleUnderscore2() {
    trimmingOf("void f(int i){}")//
        .gives("void f(int __){}")//
        .stays();
  }
  @Test public void renameUnusedVariableToDoubleUnderscore3() {
    trimmingOf("void f(@SuppressWarnings({\"unused\"})int i){}")//
        .gives("void f(@SuppressWarnings({\"unused\"})int __){}")//
        .gives("void f(@SuppressWarnings(\"unused\")int __){}")//
        .stays();
  }
  @Test public void renameUnusedVariableToDoubleUnderscore4() {
    trimmingOf("void f(@SuppressWarnings({\"unused\"})int x){}")//
        .gives("void f(@SuppressWarnings(\"unused\")int x){}")//
        .stays();
  }
  @Test public void renameUnusedVariableToDoubleUnderscore5() {
    trimmingOf("void f(int i, @SuppressWarnings(\"unused\")int y){}")//
        .gives("void f(int __, @SuppressWarnings(\"unused\")int y){}");
  }
  @Test public void renameUnusedVariableToDoubleUnderscore6() {
    trimmingOf("void f(int i, @SuppressWarnings @SuppressWarnings(\"unused\")int y){}")
        .gives("void f(int __, @SuppressWarnings @SuppressWarnings(\"unused\")int y){}");
  }
  @Test public void renameVariableUnderscore1() {
    trimmingOf("void f(int _){System.h(_);}")//
        .gives("void f(int __){System.h(__);}");
  }
  @Test public void replaceInitializationInReturn() {
    trimmingOf("int a=3;return a + 4;")//
        .gives("return 3 + 4;");
  }
  @Test public void replaceTwiceInitializationInReturn() {
    trimmingOf("int a=3;return a + 4<<a;")//
        .gives("return 3 + 4<<3;");
  }
  @Test public void rightSimplificatioForNulNNVariableReplacement() {
    final InfixExpression e = i("null !=a");
    final Tipper<InfixExpression> w = Toolboxes.all().firstTipper(e);
    assert w != null;
    assert w.check(e);
    assert w.check(e);
    final ASTNode replacement = ((ReplaceCurrentNode<InfixExpression>) w).replacement(e);
    assert replacement != null;
    azzert.that(replacement + "", is("a != null"));
  }
  @Test public void rightSipmlificatioForNulNNVariable() {
    azzert.that(Toolboxes.all().firstTipper(i("null !=a")), instanceOf(InfixComparisonSpecific.class));
  }
  @Test public void sequencerFirstInElse() {
    trimmingOf("if(a){b++;c++;++d;} else { f++;g++;return x;}")//
        .gives("if(!a){f++;g++;return x;} b++;c++;++d;");
  }
  @Test public void shorterChainParenthesisComparison() {
    trimmingOf("a==b==c")//
        .stays();
  }
  @Test public void shorterChainParenthesisComparisonLast() {
    trimmingOf("b==a * b * c * d * e * f * g * h==a")//
        .stays();
  }
  @Test public void shortestBranchIfWithComplexNestedIf3() {
    trimmingOf("if(a){f();g();h();} else if(a)++i;else ++j;")//
        .stays();
  }
  @Test public void shortestBranchIfWithComplexNestedIf4() {
    trimmingOf("if(a){f();g();h();++i;} else if(a)++i;else j++;")//
        .gives("if(!a)if(a)++i;else j++;else{f();g();h();++i;}");
  }
  @Test public void shortestBranchIfWithComplexNestedIf5() {
    trimmingOf("if(a){f();g();h();++i;f();} else if(a)++i;else j++;")//
        .gives("if(!a)if(a)++i;else j++;else{f();g();h();++i;f();}");
  }
  @Test public void shortestBranchIfWithComplexNestedIf7() {
    trimmingOf("if(a){f();++i;g();h();++i;f();j++;} else if(a)++i;else j++;").gives("if(!a)if(a)++i;else j++;else{f();++i;g();h();++i;f();j++;}");
  }
  @Test public void shortestBranchIfWithComplexNestedIf8() {
    trimmingOf("if(a){f();++i;g();h();++i;u++;f();j++;} else if(a)++i;else j++;")
        .gives("if(!a)if(a)++i;else j++;else{f();++i;g();h();++i;u++;f();j++;}");
  }
  @Test public void shortestBranchIfWithComplexNestedIfPlain() {
    trimmingOf("if(a){f();g();h();} else { i++;j++;}")//
        .gives("if(!a){i++;j++;}else{f();g();h();}");
  }
  @Test public void shortestBranchIfWithComplexSimpler() {
    trimmingOf("if(a){f();g();h();} else i++;j++;")//
        .gives("if(!a)i++;else{f();g();h();}++j;");
  }
  @Test public void shortestBranchInIf() {
    trimmingOf("int a=0;if(s.equals(known)){ S.console();} else { a=3;} ").gives("int a=0;if(!s.equals(known))a=3;else S.console();");
  }
  @Test public void shortestFirstAlignment() {
    trimmingOf("n.isSimpleName()?(SimpleName)n : n.isQualifiedName()?((QualifiedName)n).getName(): null")//
        .stays();
  }
  @Test public void shortestFirstAlignmentShortened() {
    trimmingOf("n.isF()?(SimpleName)n   : n.isG()?((QualifiedName)n).getName() : null")//
        .stays();
  }
  @Test public void shortestFirstAlignmentShortenedFurther() {
    trimmingOf("n.isF()?(A)n : n.isG()?((B)n).f() : null")//
        .stays();
  }
  @Test public void shortestFirstAlignmentShortenedFurtherAndFurther() {
    trimmingOf("n.isF()?(A)n : n.isG()?(B)n : null")//
        .stays();
  }
  @Test public void shortestIfBranchFirst01() {
    trimmingOf("if(s.equals(0xDEAD)){ int u=0;for(int i=0;i<s.length();++i)if(s.charAt(i)=='a') u +=2;} else if(s.charAt(i)=='d')u -=1;return u;")
        .gives("if(!s.equals(0xDEAD)){ if(s.charAt(i)=='d')u-=1;} else { int u=0;"
            + " for(int i=0;i<s.length();++i)if(s.charAt(i)=='a')u+=2;} return u;");
  }
  @Test public void shortestIfBranchFirst02() {
    trimmingOf("if(!s.equals(0xDEAD)){ int u=0;for(int i=0;i<s.length();++i)if(s.charAt(i)=='a')u +=2;"
        + " else if(s.charAt(i)=='d')u -=1;return u;} else { return 8;}")
            .gives("if(s.equals(0xDEAD))return 8;int u=0;for(int i=0;i<s.length();++i)  if(s.charAt(i)=='a')u +=2;else if(s.charAt(i)=='d') "
                + "  u -=1;return u; ");
  }
  @Test public void shortestIfBranchFirst02a() {
    trimmingOf("if(!s.equals(0xDEAD)){ int u=0;for(int i=0;i<s.length();++i)  if(s.charAt(i)=='a')u +=2;else if(s.charAt(i)=='d') "
        + "  u -=1;return u;} return 8;")
            .gives("if(s.equals(0xDEAD))return 8;int u=0;for(int i=0;i<s.length();++i)  if(s.charAt(i)=='a')u +=2;else if(s.charAt(i)=='d') "
                + "  u -=1;return u; ");
  }
  @Test public void shortestIfBranchFirst02b() {
    trimmingOf("int u=0;for(int i=0;i<s.length();++i)if(s.charAt(i)=='a')u +=2;  else if(s.charAt(i)=='d')--u;return u; ")
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
    trimmingOf("if(a){ f();g();h();return a;} return c;").gives("if(!a)return c;f();g();h();return a;");
  }
  @Test public void shortestOperand01() {
    trimmingOf("x + y> z")//
        .stays();
  }
  @Test public void shortestOperand02() {
    trimmingOf("k=k + 4;if(2 * 6 + 4==k)return true;")//
        .gives("k+=4;if(2*6==k-4)return true;")//
        .gives("k+=4;if(12==k-4)return true;")//
        .gives("k+=4;if(k-4==12)return true;")//
        .gives("k+=4;if(k==12+4)return true;").gives("k+=4;if(k==16)return true;")//
        .stays();
  }
  @Test public void shortestOperand05() {
    trimmingOf("W s=new W(\"bob\");return s.l(hZ).l(\"-ba\").toString()==\"bob-ha-banai\";")
        .gives("return(new W(\"bob\")).l(hZ).l(\"-ba\").toString()==\"bob-ha-banai\";");
  }
  @Test public void shortestOperand10() {
    trimmingOf("return b==true;")//
        .gives("return b;");
  }
  @Test public void shortestOperand11() {
    trimmingOf("int h,u,m,a,n;return b==true && n + a> m - u || h> u;")//
        .gives("int h,u,m,a,n;return b&&a+n>m-u||h>u;");
  }
  @Test public void shortestOperand13a() {
    trimmingOf("(2> 2 + a)==true")//
        .gives("2>2 +a ");
  }
  @Test public void shortestOperand13b() {
    trimmingOf("(2)==true")//
        .gives("2 ");
  }
  @Test public void shortestOperand13c() {
    trimmingOf("2==true")//
        .gives("2 ");
  }
  @Test public void shortestOperand14() {
    trimmingOf("Integer tipper=new Integer(5);return(tipper.toString()==null);")//
        .gives("return((new Integer(5)).toString()==null);");
  }
  @Test public void shortestOperand17() {
    trimmingOf("5 ^ a.getNum()")//
        .gives("a.getNum()^ 5");
  }
  @Test public void shortestOperand19() {
    trimmingOf("k.get().operand()^ a.get()")//
        .gives("a.get()^ k.get().operand()");
  }
  @Test public void shortestOperand20() {
    trimmingOf("k.get()^ a.get()")//
        .gives("a.get()^ k.get()");
  }
  @Test public void shortestOperand22() {
    trimmingOf("return f(a,b,c,d,e)+ f(a,b,c,d)+ f(a,b,c)+ f(a,b)+ f(a)+ f();")//
        .stays();
  }
  @Test public void shortestOperand23() {
    trimmingOf("return f()+ \".\";}")//
        .stays();
  }
  @Test public void shortestOperand24() {
    trimmingOf("f(a,b,c,d)& 175 & 0")//
        .gives("f(a,b,c,d)& 0 & 175");
  }
  @Test public void shortestOperand25() {
    trimmingOf("f(a,b,c,d)& bob & 0 ")//
        .gives("bob & f(a,b,c,d)& 0");
  }
  @Test public void shortestOperand27() {
    trimmingOf("return f(a,b,c,d)+ f(a,b,c)+ f();} ")//
        .stays();
  }
  @Test public void shortestOperand28() {
    trimmingOf("return f(a,b,c,d)* f(a,b,c)* f();")//
        .gives("return f()*f(a,b,c)*f(a,b,c,d);");
  }
  @Test public void shortestOperand29() {
    trimmingOf("f(a,b,c,d)^ f()^ 0")//
        .gives("f()^ f(a,b,c,d)^ 0");
  }
  @Test public void shortestOperand30() {
    trimmingOf("f(a,b,c,d)& f()")//
        .gives("f()& f(a,b,c,d)");
  }
  @Test public void shortestOperand31() {
    trimmingOf("return f(a,b,c,d)| \".\";}")//
        .stays();
  }
  @Test public void shortestOperand32() {
    trimmingOf("return f(a,b,c,d)&& f();}")//
        .stays();
  }
  @Test public void shortestOperand33() {
    trimmingOf("return f(a,b,c,d)|| f();}")//
        .stays();
  }
  @Test public void shortestOperand34() {
    trimmingOf("return f(a,b,c,d)+ someVar;")//
        .stays();
  }
  @Test public void shortestOperand37() {
    trimmingOf("return sansJavaExtension(f)+ n + \".\"+ extension(f);")//
        .stays();
  }
  @Test public void simpleIntMethod() {
    trimmingOf("int f(){ int x=0;for(int i=0;i<10;++i)x +=i;return x;}")//
        .gives("int f(){ int $=0;for(int i=0;i<10;++i)$ +=i;return $;}");
  }
  @Test public void ignoreBooleanMethod() {
    trimmingOf("boolean f(){ int x=0;for(int i=0;i<10;++i)x +=i;return x;}")//
        .using(new MethodDeclarationRenameReturnToDollar(), MethodDeclaration.class) //
        .stays();
  }
  @Test public void ignoreVoidMethod() {
    trimmingOf("Void f(){ int x=0;for(int i=0;i<10;++i)x +=i;return x;}")//
        .using(new MethodDeclarationRenameReturnToDollar(), MethodDeclaration.class) //
        .stays();
  }
  @Test public void simplifyLogicalNegationNested() {
    trimmingOf("!((a || b==c)&&(d || !(!!c)))")//
        .gives("!a && b !=c || !d && c");
  }
  @Test public void simplifyLogicalNegationNested1() {
    trimmingOf("!(d || !(!!c))")//
        .gives("!d && c");
  }
  @Test public void simplifyLogicalNegationNested2() {
    trimmingOf("!(!d || !!!c)")//
        .gives("d && c");
  }
  @Test public void simplifyLogicalNegationOfAnd() {
    trimmingOf("!(f()&& f(5))")//
        .gives("!f()|| !f(5)");
  }
  @Test public void simplifyLogicalNegationOfEquality() {
    trimmingOf("!(3==5)")//
        .gives("3!=5");
  }
  @Test public void simplifyLogicalNegationOfGreater() {
    trimmingOf("!(3> 5)")//
        .gives("3<=5");
  }
  @Test public void simplifyLogicalNegationOfGreaterEquals() {
    trimmingOf("!(3>=5)")//
        .gives("3<5");
  }
  @Test public void simplifyLogicalNegationOfInequality() {
    trimmingOf("!(3 !=5)")//
        .gives("3==5");
  }
  @Test public void simplifyLogicalNegationOfLess() {
    trimmingOf("!(3<5)")//
        .gives("3>=5");
  }
  @Test public void simplifyLogicalNegationOfLessEquals() {
    trimmingOf("!(3<=5)")//
        .gives("3> 5");
  }
  @Test public void simplifyLogicalNegationOfMultipleAnd() {
    trimmingOf("!(a && b && c)")//
        .gives("!a || !b || !c");
  }
  @Test public void simplifyLogicalNegationOfMultipleOr() {
    trimmingOf("!(a || b || c)")//
        .gives("!a && !b && !c");
  }
  @Test public void simplifyLogicalNegationOfNot() {
    trimmingOf("!!f()")//
        .gives("f()");
  }
  @Test public void simplifyLogicalNegationOfOr() {
    trimmingOf("!(f()|| f(5))")//
        .gives("!f()&& !f(5)");
  }
  @Test public void sortAddition1() {
    trimmingOf("1 + 2 - 3 - 4 + 5 / 6 - 7 + 8 * 9 + A> k + 4")//
        .gives("8*9+1+2-3-4+5 / 6-7+A>k+4");
  }
  @Test public void sortAddition2() {
    trimmingOf("1 + 2<3 & 7 + 4> 2 + 1 || 6 - 7<2 + 1")//
        .gives("3<3&11>3||-1<3");
  }
  @Test public void sortAddition3() {
    trimmingOf("6 - 7<1 + 2")//
        .gives("-1<3")//
        .stays();
  }
  @Test public void sortAddition4() {
    trimmingOf("a + 11 + 2<3 & 7 + 4> 2 + 1")//
        .gives("7 + 4> 2 + 1 & a + 11 + 2<3");
  }
  @Test public void sortAdditionClassConstantAndLiteral() {
    trimmingOf("1+A<12")//
        .gives("A+1<12");
  }
  @Test public void sortAdditionFunctionClassConstantAndLiteral() {
    trimmingOf("1+A+f()<12")//
        .gives("f()+A+1<12");
  }
  @Test public void sortAdditionThreeOperands1() {
    trimmingOf("1.0+2222+3")//
        .gives("2226.0")//
        .stays();
  }
  @Test public void sortAdditionThreeOperands2() {
    trimmingOf("1.0+1+124+1")//
        .gives("127.0");
  }
  @Test public void sortAdditionThreeOperands3() {
    trimmingOf("1+2F+33+142+1")//
        .gives("1+2F+176")//
        .stays();
  }
  @Test public void sortAdditionThreeOperands4() {
    trimmingOf("1+2+'a'")//
        .gives("3+'a'");
  }
  @Test public void sortAdditionTwoOperands0CheckThatWeSortByLength_a() {
    trimmingOf("1111+211")//
        .gives("1322");
  }
  @Test public void sortAdditionTwoOperands0CheckThatWeSortByLength_b() {
    trimmingOf("211+1111")//
        .gives("1322")//
        .stays();
  }
  @Test public void sortAdditionTwoOperands1() {
    trimmingOf("1+2F")//
        .stays();
  }
  @Test public void sortAdditionTwoOperands2() {
    trimmingOf("2.0+1")//
        .gives("3.0");
  }
  @Test public void sortAdditionTwoOperands3() {
    trimmingOf("1+2L")//
        .gives("3L");
  }
  @Test public void sortAdditionTwoOperands4() {
    trimmingOf("2L+1")//
        .gives("3L");
  }
  @Test public void sortAdditionUncertain() {
    trimmingOf("1+a")//
        .stays();
  }
  @Test public void sortAdditionVariableClassConstantAndLiteral() {
    trimmingOf("1+A+a<12")//
        .gives("a+A+1<12");
  }
  @Test public void sortConstantMultiplication() {
    trimmingOf("a*2")//
        .gives("2*a");
  }
  @Test public void sortDivision() {
    trimmingOf("2.1/34.2/1.0")//
        .gives("2.1/1.0/34.2");
  }
  @Test public void sortDivisionLetters() {
    trimmingOf("x/b/a")//
        .gives("x/a/b");
  }
  @Test public void sortDivisionNo() {
    trimmingOf("2.1/3")//
        .stays();
  }
  @Test public void sortThreeOperands1() {
    trimmingOf("1.0*2222*3")//
        .gives("6666.0");
  }
  @Test public void sortThreeOperands2() {
    trimmingOf("1.0*11*124")//
        .gives("1364.0");
  }
  @Test public void sortThreeOperands3() {
    trimmingOf("2*2F*33*142")//
        .gives("2*2F*4686")//
        .stays();
  }
  @Test public void sortThreeOperands4() {
    trimmingOf("2*3*'a'")//
        .gives("6*'a'");
  }
  @Test public void sortTwoOperands0CheckThatWeSortByLength_a() {
    trimmingOf("1111*211")//
        .gives("234421");
  }
  @Test public void sortTwoOperands0CheckThatWeSortByLength_b() {
    trimmingOf("211*1111")//
        .gives("234421");
  }
  @Test public void sortTwoOperands1() {
    trimmingOf("1F*2F")//
        .stays();
  }
  @Test public void sortTwoOperands2() {
    trimmingOf("2.0*2")//
        .gives("4.0");
  }
  @Test public void sortTwoOperands3() {
    trimmingOf("2*3L")//
        .gives("6L");
  }
  @Test public void sortTwoOperands4() {
    trimmingOf("2L*1L")//
        .gives("2L");
  }
  @Test public void switchSimplifyCaseAfterDefault() {
    trimmingOf("switch(n.getNodeType()){case BREAK_STATEMENT:return 0;case CONTINUE_STATEMENT:return 1;"
        + "case RETURN_STATEMENT:return 2;case THROW_STATEMENT:return 3;default:return-1;}");
  }
  @Test public void switchSimplifyWithDefault2() {
    trimmingOf("switch(a){case \"-E\":optIndividualStatistics=true;break;case \"-N\":optDoNotOverwrite=true;break;"
        + "case \"-V\":optVerbose=true;break;case \"-l\":optStatsLines=true;break;case \"-r\":optStatsChanges=true;break;"
        + "default:if(!a.startsWith(\"-\"))optPath=a;try{if(a.startsWith(\"-C\"))optRounds=Integer.parseUnsignedInt(a.substring(2));}"
        + "catch(final NumberFormatException e){throw e;}break;}")
            .gives("switch(a){case \"-E\":optIndividualStatistics=true;break;case \"-N\":optDoNotOverwrite=true;break;"
                + "case \"-V\":optVerbose=true;break;case \"-l\":optStatsLines=true;break;case \"-r\":optStatsChanges=true;break;"
                + "default:if(!a.startsWith(\"-\"))optPath=a;try{if(a.startsWith(\"-C\"))optRounds=Integer.parseUnsignedInt(a.substring(2));}"
                + "catch(final NumberFormatException ¢){throw ¢;}break;}");
  }
  @Test public void synchronizedBraces() {
    trimmingOf("synchronized(variables){ for(final String key : variables.keySet())  $.variables.put(key, variables.get(key));}")//
        .stays();
  }
  @Test public void ternarize05() {
    trimmingOf("int u=0;if(s.equals(532))u +=6;else u +=9;").gives("int u=0;u+=s.equals(532)?6:9;");
  }
  @Test public void ternarize05a() {
    trimmingOf("int u=0;if(s.equals(532))u +=6;else u +=9;return u;").gives("int u=0;u+=s.equals(532)?6:9;return u;");
  }
  @Test public void ternarize07() {
    trimmingOf("String u;u=s;if(u.equals(532)==true)u=s + 0xABBA;S.h(u);").gives("String u=s ;if(u.equals(532))u=s + 0xABBA;S.h(u);");
  }
  @Test public void ternarize07a() {
    trimmingOf("String u;u=s;if(u==true)u=s + 0xABBA;S.h(u);").gives("String u=s;if(u)u=s+0xABBA;S.h(u);");
  }
  @Test public void ternarize07aa() {
    trimmingOf("String u=s;if(u==true)u=s+0xABBA;S.h(u);")//
        .gives("String u=s==true?s+0xABBA:s;S.h(u);");
  }
  @Test public void ternarize07b() {
    trimmingOf("String u=s ;if(u.equals(532)==true)u=s + 0xABBA;S.h(u);").gives("String u=s.equals(532)==true?s+0xABBA:s;S.h(u);");
  }
  @Test public void ternarize09() {
    trimmingOf("if(s.equals(532)){ return 6;}else { return 9;}")//
        .gives("return s.equals(532)?6:9;");
  }
  /* @Test public void ternarize10(){ trimmingOf("String u=s, foo=bar;" +
   * "if(u.equals(532)==true)u=s + 0xABBA;S.h(u);")
   * .gives("String u=s.equals(532)==true?s+0xABBA:s,foo=bar;S.h(u);");} */
  @Test public void ternarize12() {
    trimmingOf("String u=s;if(s.equals(532))u=u + 0xABBA;S.h(u);")//
        .gives("String u=s.equals(532)?s+0xABBA:s;S.h(u);");
  }
  @Test public void ternarize13() {
    trimmingOf("String u=m, foo;if(m.equals(f())==true)foo=M;")//
        .gives("String foo;if(m.equals(f())==true)foo=M;")//
        .gives("String foo;if(m.equals(f()))foo=M;");
  }
  @Test public void ternarize13Simplified() {
    trimmingOf("String r=m, f;if(m.e(f()))f=M;")//
        .gives("String f;if(m.e(f()))f=M;");
  }
  @Test public void ternarize13SimplifiedMore() {
    trimmingOf("if(m.equals(f())==true)foo=M;")//
        .gives("if(m.equals(f()))foo=M;");
  }
  @Test public void ternarize13SimplifiedMoreAndMore() {
    trimmingOf("f(m.equals(f())==true);foo=M;")//
        .gives("f(m.equals(f()));foo=M;");
  }
  @Test public void ternarize13SimplifiedMoreAndMoreAndMore() {
    trimmingOf("f(m.equals(f())==true);")//
        .gives("f(m.equals(f()));");
  }
  @Test public void ternarize13SimplifiedMoreVariant() {
    trimmingOf("if(m==true)foo=M;")//
        .gives("if(m)foo=M;");
  }
  @Test public void ternarize13SimplifiedMoreVariantShorter() {
    trimmingOf("if(m==true)f();")//
        .gives("if(m)f();");
  }
  @Test public void ternarize13SimplifiedMoreVariantShorterAsExpression() {
    trimmingOf("f(m==true);f();")//
        .gives("f(m);f();");
  }
  /* @Test public void ternarize16(){
   * trimmingOf("String u=m;int num2;if(m.equals(f()))num2=2;"). stays();} */
  /* @Test public void ternarize16a(){ trimmingOf("int n1, n2=0, n3; " +
   * " if(d)n2=2;")// .gives("int n1, n2=d ? 2: 0, n3;");} */
  public void ternarize18() {
    trimmingOf("final String u=s;System.h(s.equals(u)?tH3+u:h2A+u+0);")//
        .gives("System.h(s.equals(s)?tH3+u:h2A+s+0);");
  }
  @Test public void ternarize21() {
    trimmingOf("if(s.equals(532)){ S.h(gG);S.out.l(kKz);} f();")//
        .stays();
  }
  @Test public void ternarize21a() {
    trimmingOf("if(s.equals(known)){ S.out.l(gG);} else { S.out.l(kKz);}").gives("S.out.l(s.equals(known)?gG:kKz);");
  }
  @Test public void ternarize22() {
    trimmingOf("int a=0;if(s.equals(532)){ S.console();a=3;} f();")//
        .stays();
  }
  @Test public void ternarize26() {
    trimmingOf("int a=0;if(s.equals(532)){ a+=2;a-=2;} f();")//
        .stays();
  }
  @Test public void ternarize33() {
    trimmingOf("int a, b=0;if(b==3){ a=4;} ")//
        .gives("int a;if(0==3){a=4;}")//
        .gives("int a;if(0==3)a=4;")//
        .stays();
  }
  @Test public void ternarize35() {
    trimmingOf("int a,b=0,c=0;a=4;if(c==3){b=2;}")//
        .gives("int a=4,b=0,c=0;if(c==3)b=2;");
  }
  @Test public void ternarize36() {
    trimmingOf("int a,b=0,c=0;a=4;if(c==3){ b=2;a=6;} f();")//
        .gives("int a=4,b=0,c=0;if(c==3){b=2;a=6;} f();");
  }
  @Test public void ternarize38() {
    trimmingOf("int a, b=0;use(a,b);if(b==3){ a+=2+r();a-=6;} f();")//
        .stays();
  }
  @Test public void ternarize45() {
    trimmingOf("if(m.equals(f())==true)if(b==3){ return 3;return 7;} else if(b==3){ return 2;} a=7;")
        .gives("if(m.equals(f())){if(b==3){ return 3;return 7;} if(b==3){ return 2;} } a=7;");
  }
  @Test public void ternarize46() {
    trimmingOf("int a , b=0;if(m.equals(NG)==true)if(b==3){ return 3;} else {  a+=7;} else if(b==3){ return 2;} else { a=7;}")
        .gives("int a;if(m.equals(NG)==true)if(0==3){return 3;}else{a+=7;}else if(0==3){return 2;}else{a=7;}");
  }
  @Test public void ternarize49() {
    trimmingOf("if(s.equals(532)){ S.h(gG);S.out.l(kKz);} f();")//
        .stays();
  }
  @Test public void ternarize52() {
    trimmingOf("int a=0,b=0,c,d=0,e=0;use(a,b);if(a<b){c=d;c=e;} f();")//
        .gives("int a=0,b=0,c,d=0,e=0;use(a,b);if(a<b){c=e;}f();") //
        .gives("int a=0,b=0,e=0;use(a,b);if(a<b)c=e;f();") //
        .stays();
  }
  @Test public void ternarize54() {
    trimmingOf("int $=1,xi=0,xj=0,yi=0,yj=0;if(xi> xj==yi> yj)++$;else--$;")//
        .gives("int $=1,xj=0,yi=0,yj=0;if(0>xj==yi>yj)++$;else--$;");
  }
  @Test public void ternarize55() {
    trimmingOf("if(key.equals(markColumn))to.put(key, a.toString()); else to.put(key, missing(key, a)? Z2 : get(key, a));")
        .gives("to.put(key,key.equals(markColumn)?a.toString():missing(key,a)?Z2:get(key,a));");
  }
  @Test public void ternarize56() {
    trimmingOf("if(target==0){p.f(X);p.v(0);p.f(q + target);p.v(q * 100 / target);} f();")
        .gives("if(target==0){p.f(X);p.v(0);p.f(q+target);p.v(100*q / target);} f();");
  }
  @Test public void ternarizeintoSuperMethodInvocation() {
    trimmingOf("a ? super.f(a, b, c): super.f(a, x, c)")//
        .gives("super.f(a, a ? b : x, c)");
  }
  @Test public void ternaryPushdownOfReciever() {
    trimmingOf("a ? b.f():c.f()")//
        .gives("(a?b:c).f()");
  }
  /** Introduced by Yossi on Wed-Mar-22-21:27:17-IST-2017 (code automatically
   * generated in 'il.org.spartan.spartanizer.cmdline.anonymize.java') */
  @Test public void test_inta0b0cd0e0fabIfabcdceg() {
    trimmingOf("int a = 0, b = 0, c, d = 0, e = 0; f(a, b); if (a < b) { c = d; c = e; } g();") //
        .using(new AssignmentAndAssignmentToSameKill(), Assignment.class) //
        .gives("int a=0,b=0,c,d=0,e=0;f(a,b);if(a<b){c=e;}g();") //
        .using(new BlockSingletonEliminate(), Block.class) //
        .gives("int a=0,b=0,c,d=0,e=0;f(a,b);if(a<b)c=e;g();") //
        .gives("int a=0,b=0,e=0;f(a,b);if(a<b)c=e;g();") //
        .stays() //
    ;
  }
  /** Introduced by Yogi on Sat-Apr-01-16:37:37-IDT-2017 (code automatically in
   * class 'JUnitTestMethodFacotry') */
  @Test public void test_inta15Return7a() {
    trimmingOf("int a = 15; return 7 < a;") //
        .using(new LocalInitializedReturnExpression(), VariableDeclarationFragment.class) //
        .gives("return 7<15;") //
        .stays() //
    ;
  }
  /** Introduced by Yossi on Wed-Mar-22-21:22:15-IST-2017 (code automatically
   * generated in 'il.org.spartan.spartanizer.cmdline.anonymize.java') */
  @Test public void test_intaba3b5Ifa4Ifb3b2Elsebab3ElseIfb3b2Elsebaab3() {
    trimmingOf("int a, b; a = 3; b = 5; if (a == 4) if (b == 3) b = 2; else { b = a; b = 3; } else if (b == 3) b = 2; else { b = a * a; b = 3; }") //
        .using(new LocalUninitializedAssignmentToIt(), VariableDeclarationFragment.class) //
        .gives("int a=3,b;b=5;if(a==4)if(b==3)b=2;else{b=a;b=3;}else if(b==3)b=2;else{b=a*a;b=3;}") //
        .using(new LocalUninitializedAssignmentToIt(), VariableDeclarationFragment.class) //
        .gives("int a=3,b=5;if(a==4)if(b==3)b=2;else{b=a;b=3;}else if(b==3)b=2;else{b=a*a;b=3;}") //
        .using(new LocalInitializedStatementTerminatingScope(), VariableDeclarationFragment.class) //
        .gives("int b=5;if(3==4)if(b==3)b=2;else{b=3;b=3;}else if(b==3)b=2;else{b=3*3;b=3;}") //
        .using(new AssignmentAndAssignmentOfSameValue(), Assignment.class) //
        .gives("int b=5;if(3==4)if(b==3)b=2;else{b=b=3;}else if(b==3)b=2;else{b=3*3;b=3;}") //
        .using(new IfAssignToFooElseAssignToFoo(), IfStatement.class) //
        .gives("int b=5;if(3==4)b=b==3?2:(b=3);else if(b==3)b=2;else{b=3*3;b=3;}") //
        .using(new AssignmentAndAssignmentToSameKill(), Assignment.class) //
        .gives("int b=5;if(3==4)b=b==3?2:(b=3);else if(b==3)b=2;else{b=3;}") //
        .using(new IfAssignToFooElseAssignToFoo(), IfStatement.class) //
        .gives("int b=5;if(3==4)b=b==3?2:(b=3);else b=b==3?2:3;") //
        .using(new IfAssignToFooElseAssignToFoo(), IfStatement.class) //
        .gives("int b=5;b=3==4?b==3?2:(b=3):b==3?2:3;") //
        .using(new TernaryShortestFirst(), ConditionalExpression.class) //
        .gives("int b=5;b=3!=4?b==3?2:3:b==3?2:(b=3);") //
        .using(new TernaryShortestFirst(), ConditionalExpression.class) //
        .gives("int b=5;b=3!=4?b==3?2:3:b!=3?(b=3):2;") //
        .stays() //
    ;
  }
  /** Introduced by Yogi on Fri-Apr-07-19:53:21-IDT-2017 (code automatically in
   * class 'JUnitTestMethodFacotry') */
  @Test public void test_intaIntbForbIfFalseBreakReturnb() {
    trimmingOf("int a(int b) { for (;; ++b) if (false) break; return b; }") //
        .using(new IfTrueOrFalse(), IfStatement.class) //
        .gives("int a(int b){for(;;++b){}return b;}") //
        .using(new ForEmptyBlockToEmptyStatement(), ForStatement.class) //
        .gives("int a(int b){for(;;++b);return b;}") //
        .stays() //
    ;
  }
  @Test public void testPeel() {
    azzert.that(WrapIntoComilationUnit.Expression.off(WrapIntoComilationUnit.Expression.on("on * notion * of * no * nothion !=the * plain + kludge")),
        is("on * notion * of * no * nothion !=the * plain + kludge"));
  }
  @Test public void twoMultiplication1() {
    trimmingOf("f(a,b,c,d)* f()")//
        .gives("f()* f(a,b,c,d)");
  }

  final TraversalImplementation trimmer = new TraversalImplementation();

  @Test public void twoOpportunityExample() {
    azzert.that(countOpportunities(trimmer, (CompilationUnit) makeAST.COMPILATION_UNIT
        .from(WrapIntoComilationUnit.Expression.on("on * notion * of * no * nothion !=the * plain + kludge"))), is(1));
    azzert.that(countOpportunities(trimmer, (CompilationUnit) makeAST.COMPILATION_UNIT
        .from(WrapIntoComilationUnit.Expression.on("on * notion * of * no * nothion !=the * plain + kludge"))), is(1));
  }
  @Test public void unsafeBlockSimlify() {
    trimmingOf("public void testParseInteger(){ String source=\"10\";use(source);{  BigFraction c=properFormat.parse(source);assert c !=null; "
        + " azzert.wizard.assertEquals(BigInteger.TEN, c.getNumerator());  azzert.wizard.assertEquals(BigInteger.ONE, c.getDenominator());} { "
        + " BigFraction c=improperFormat.parse(source);assert c !=null;  azzert.wizard.assertEquals(BigInteger.TEN, c.getNumerator()); "
        + " azzert.wizard.assertEquals(BigInteger.ONE, c.getDenominator());} }")//
            .stays();
  }
  @Test public void useOutcontextToManageStringAmbiguity() {
    trimmingOf("1+2+s<3")//
        .gives("3+s<3");
  }
  @Test public void vanillaShortestFirstConditionalNoChange() {
    trimmingOf("literal ? CONDITIONAL_OR : CONDITIONAL_AND")//
        .stays();
  }
  @Test public void xorSortClassConstantsAtEnd() {
    trimmingOf("f(a,b,c,d)^ BOB")//
        .stays();
  }
}
