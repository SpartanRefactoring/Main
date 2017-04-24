package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

/** Tests for the GitHub issue thus numbered
 * @author Alex Kopzon
 * @since 2016-09-23 */

@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0311 {
  @Test public void a() {
    trimminKof("while(zq<ng.l()&&m.f(zq)){int x=m.zq();int eA=m.z();int lE=eA-x;p(lE);I[] ch=y(x,lE);if((c==null)||c.k(ch))st.$(ch);zq=eA;}")//
        .gives("while(zq<ng.l()&&m.f(zq)){int x=m.zq(),eA=m.z();int lE=eA-x;p(lE);I[] ch=y(x,lE);if((c==null)||c.k(ch))st.$(ch);zq=eA;}") //
        .gives("while(zq<ng.l()&&m.f(zq)){int x=m.zq(),eA=m.z(),lE=eA-x;p(lE);I[] ch=y(x,lE);if((c==null)||c.k(ch))st.$(ch);zq=eA;}")//
        .stays();
  }

  @Test public void b() {
    trimminKof("y=1;while(sg.t(y)!=')'){int d=gT(sg.ss(y));$ +=size(d);y +=consumed(d);}").stays();
  }

  @Test public void ca() {
    trimminKof("for(int i=0;i<20;++i){F f=q.f();a3xZ(s.l(c),not(r4(f)));" + //
        "c[i]=f;new F(f,\"a.txt\").g();a(f.e());}")//
            .stays();
  }

  @Test public void cb() {
    trimminKof("for(int i=0;i<20;++i){a3xZ(s.l(c),not(r4(f)));" + //
        "c[i]=f;new F(f,\"a.txt\").g();a(f.e());}")//
            .gives("for(int ¢=0;¢<20;++¢){a3xZ(s.l(c),not(r4(f)));" + //
                "c[¢]=f;new F(f,\"a.txt\").g();a(f.e());}")
            .stays();
  }

  @Test public void challenge_for_i_initialization_expression_3a() {
    trimminKof("boolean b;for(;b=true;)$.ap(line).ap(ls);")//
        .gives("for(boolean b=true;b;)$.ap(line).ap(ls);")//
        .gives("for(boolean ¢=true;¢;)$.ap(line).ap(ls);")//
        .stays();
  }

  @Test public void challenge_for_i_initialization_expression_3b() {
    trimminKof("boolean b;for(;b=true;)$.ap(line).ap(ls);")//
        .gives("for(boolean b=true;b;)$.ap(line).ap(ls);")//
        .gives("for(boolean ¢=true;¢;)$.ap(line).ap(ls);")//
        .stays();
  }

  @Test public void challenge_for_i_initialization_expression_3c() {
    trimminKof("boolean b;for(;(b==true);)$.ap(line).ap(ls);")//
        .gives("for(boolean b;(b==true);)$.ap(line).ap(ls);")//
        .gives("for(boolean ¢;(¢==true);)$.ap(line).ap(ls);")//
        .gives("for(boolean ¢;(¢);)$.ap(line).ap(ls);")//
    ;
  }

  @Test public void d() {
    trimminKof("static S eE(I ¢){S $=¢.e2();" + //
        "while($ instanceof I)$=((I)$).e2();return $;}")//
            .stays();
  }

  @Test public void e_Modifiers_in_1() {
    trimminKof("public S bv(){S a=\"\";M m=R.mp(\"[A-Z]\").m(q);" + //
        "while(m.f())a +=m.u();return a.e2();}")//
            .gives("public S bv(){S $=\"\";M m=R.mp(\"[A-Z]\").m(q);" + //
                "while(m.f())$ +=m.u();return $.e2();}")//
            .gives("public S bv(){S $=\"\";" + //
                "for(M m=R.mp(\"[A-Z]\").m(q);m.f();)$ +=m.u();return $.e2();}");
  }

  @Test public void e_Modifiers_in_2a() {
    trimminKof("c(int i){int p=i;while(p<10)++p;return false;}")//
        .gives("c(int i){for(int p=i;p<10;)++p;return false;}")//
        .stays();
  }

  @Test public void e_Modifiers_in_2b() {
    trimminKof("c(int i){int p=i;while(p<10)++i;return false;}")//
        .gives("c(int i){for(int p=i;p<10;)++i;return false;}")//
        .stays();
  }

  @Test public void for_1() {
    trimminKof("c(N n){N p=n;for(;p!=null;){if(dns.c(p))return true;++i;}return false;}")
        .gives("c(N n){for(N p=n;p!=null;){if(dns.c(p))return true;++i;}return false;}")//
        .stays();
  }

  @Test public void for_2() {
    trimminKof("c(int i){int p=i;for(;p<10;)++p;return false;}")//
        .gives("c(int i){for(int p=i;p<10;)++p;return false;}")//
        .stays();
  }

  @Test public void for_3a() {
    trimminKof("c(int i){int p=i,a=0;for(;p<10;){++p;--a;}return false;}").gives("c(int i){for(int p=i,a=0;p<10;){++p;--a;}return false;}")
        .gives("c(int i){for(int p=i,a=0;p<10;--a){++p;}return false;}")//
        .gives("c(int i){for(int p=i,a=0;p<10;--a)++p;return false;}")//
        .stays();
  }

  @Test public void for_3b() {
    trimminKof("c(int i){int p=i,a=0;for(;p<10;){++p;--a;k+=p+a;}return false;}")
        .gives("c(int i){for(int p=i,a=0;p<10;){++p;--a;k+=p+a;}return false;}")//
        .stays();
  }

  @Test public void for_5() {
    trimminKof("c(int i){int p=i;for(int k=2;p<10;){++nE;++p;}return false;}").gives("c(int i){for(int p=i,k=2;p<10;){++nE;++p;}return false;}")
        .gives("c(int i){for(int p=i,k=2;p<10;++p){++nE;}return false;}").gives("c(int i){for(int p=i,k=2;p<10;++p)++nE;return false;}")//
        .stays();
  }

  @Test public void h() {
    trimminKof("int i=0;while(i<p7.size()-1)if(p7.g(i).t()!=N.Y ||p7.g(i+1).t()!=N.Y)" + //
        "++i;else{c=true;G l=x.getAST().newG();" + //
        "l.u(((G)p7.g(i)).v()+((G)p7.g(i+1)).v());p7.v(i);p7.v(i);p7.$(i,l);}")//
            .gives("for(int i=0;i<p7.size()-1;)if(p7.g(i).t()!=N.Y ||p7.g(i+1).t()!=N.Y)" + //
                "++i;else{c=true;G l=x.getAST().newG();" + //
                "l.u(((G)p7.g(i)).v()+((G)p7.g(i+1)).v());p7.v(i);p7.v(i);p7.$(i,l);}")
            .stays();
  }

  @Test public void i_initialization_expression_1() {
    trimminKof("S line;while((line=reader.readLine())!=null)$.ap(line).ap(ls);").gives("for(S line=reader.readLine();line!=null;)$.ap(line).ap(ls);")//
        .stays();
  }

  @Test public void i_initialization_expression_2a() {
    trimminKof("S line;while(null!=(line=reader.readLine()))$.ap(line).ap(ls);").gives("for(S line=reader.readLine();null!=line;)$.ap(line).ap(ls);")
        .gives("for(S line=reader.readLine();line!=null;)$.ap(line).ap(ls);")//
        .stays();
  }

  @Test public void i_initialization_expression_2b() {
    trimminKof("int line;while(0<(line=1))++line;")//
        .gives("for(int line=1;0<line;)++line;")//
        .gives("for(int line=1;line> 0;)++line;")//
        .stays();
  }

  @Test public void i_initialization_expression_2c() {
    trimminKof("int line;while(0<(line=1)){--a;++line;}")//
        .gives("for(int line=1;0<line;){--a;++line;}")//
        .gives("for(int line=1;0<line;++line){--a;}")//
        .gives("for(int line=1;line> 0;++line)--a;")//
        .stays();
  }

  @Test public void i_initialization_expression_2d() {
    trimminKof("int line;while(0<(line=1)){a=line;++line;}")//
        .gives("for(int line=1;0<line;){a=line;++line;}")//
        .gives("for(int line=1;0<line;++line){a=line;}")//
        .gives("for(int line=1;line> 0;++line)a=line;")//
        .stays();
  }

  @Test public void i_initialization_expression_3a() {
    trimminKof("boolean b;while(b=true)$.ap(line).ap(ls);")//
        .gives("for(boolean b=true;b;)$.ap(line).ap(ls);")//
        .gives("for(boolean ¢=true;¢;)$.ap(line).ap(ls);")//
        .stays();
  }

  @Test public void i_initialization_expression_3b() {
    trimminKof("boolean b;while(b=true)$.ap(line).ap(ls);")//
        .gives("for(boolean b=true;b;)$.ap(line).ap(ls);")//
        .gives("for(boolean ¢=true;¢;)$.ap(line).ap(ls);")//
        .stays();
  }

  @Test public void i_initialization_expression_3e() {
    trimminKof("boolean a,b,c;while((b=true)&&(a=true)&&(c=true))$.ap(line).ap(ls);")
        .gives("for(boolean a=true,b=true,c=true;b&&a&&c;)$.ap(line).ap(ls);").gives("for(boolean ¢=true,b=true,c=true;b&&¢&&c;)$.ap(line).ap(ls);")//
        .stays();
  }

  @Test public void i_initialization_expression_4() {
    trimminKof("boolean a,b,c;while((b=true)&&(a=true)&&(d=true))$.ap(c).ap(ls);")
        .gives("for(boolean a=true,b=true,c;b&&a&&(d=true);)$.ap(c).ap(ls);").gives("for(boolean ¢=true,b=true,c;b&&¢&&(d=true);)$.ap(c).ap(ls);")//
        .stays();
  }

  @Test public void j() {
    trimminKof("public S bv(){S a=\"\";M m=R.mp(\"[A-Z]\").m(q);while(m.f())a +=m.u();return a.e2();}")
        .gives("public S bv(){S $=\"\";M m=R.mp(\"[A-Z]\").m(q);while(m.f())$ +=m.u();return $.e2();}")
        .gives("public S bv(){S $=\"\";for(M m=R.mp(\"[A-Z]\").m(q);m.f();)$ +=m.u();return $.e2();}")
        .gives("public S bv(){S $=\"\";for(M ¢=R.mp(\"[A-Z]\").m(q);¢.f();)$ +=¢.u();return $.e2();}")//
        .stays();
  }

  @Test public void k() {
    trimminKof("static void eA(boolean b,L<E> xs){for(E ¢=f(b,xs);;){if(¢==null)return;xs.v(¢);}}")//
        .stays();
  }

  @Test public void l() {
    trimminKof("")//
        .stays();
  }

  @Test public void t03a() {
    trimminKof("S t(S g){B sb=new B(g);int l=sb.l();for(int i=0;i<l;++i)if(sb.t(i)=='.')sb.s(i,'/');return sb+\"\";")
        .gives("S t(S g){B $=new B(g);int l=$.l();for(int i=0;i<l;++i)if($.t(i)=='.')$.s(i,'/');return $+\"\";")
        .gives("S t(S g){B $=new B(g);int l=$.l();for(int ¢=0;¢<l;++¢)if($.t(¢)=='.')$.s(¢,'/');return $+\"\";")
        .gives("S t(S g){B $=new B(g);for(int l=$.l(),¢=0;¢<l;++¢)if($.t(¢)=='.')$.s(¢,'/');return $+\"\";")//
        .stays();
  }

  @Test public void t03c() {
    trimminKof("S t(S s){int $=0,one=1;while($<one){if($==0)$=7;++$;}return $;}")
        .gives("S t(S __){int $=0,one=1;for(;$<one;++$){if($==0)$=7;}return $;}")
        .gives("S t(S __){int $=0,one=1;for(;$<one;++$)if($==0)$=7;return $;}")//
        .stays();
  }

  @Test public void t03d() {
    trimminKof("S t(S g){int $=0,one=1;while($<one){if($==0)$=7;++$;}return g;}")//
        .gives("S t(S g){for(int $=0,one=1;$<one;){if($==0)$=7;++$;}return g;}")
        .gives("S t(S g){for(int $=0,one=1;$<one;++$){if($==0)$=7;}return g;}").gives("S t(S g){for(int $=0,one=1;$<one;++$)if($==0)$=7;return g;}")//
        .stays();
  }

  @Test public void t04() {
    trimminKof("c(N n){N p=n;while(p!=null){if(dns.c(p))continue;p=p.e();}return false;}")
        .gives("c(N n){for(N p=n;p!=null;){if(dns.c(p))continue;p=p.e();}return false;}")
        .gives("c(N n){for(N p=n;p!=null;){if(!dns.c(p))p=p.e();}return false;}")
        .gives("c(N n){for(N p=n;p!=null;)if(!dns.c(p))p=p.e();return false;}")//
        .stays();
  }

  @Test public void t05() {
    trimminKof("static S eE(I ¢){S $=¢.e2();while($ instanceof I)$=((I)$).e2();return $;}")//
        .stays();
  }

  @Test public void t06a() {
    trimminKof("c(N n){N p=n;while(p!=null)f();return false;}")//
        .gives("c(N n){for(N p=n;p!=null;)f();return false;}")//
        .stays();
  }

  @Test public void t06b() {
    trimminKof("c(N n){N p=n;while(p!=null){f();g();h();}return false;}")//
        .gives("c(N n){for(N p=n;p!=null;){f();g();h();}return false;}")//
        .stays();
  }

  @Test public void t06c() {
    trimminKof("c(int i){int p=i;while(p!=null){++p;--i;h(p);}return false;}").gives("c(int i){for(int p=i;p!=null;){++p;--i;h(p);}return false;}")//
        .stays();
  }

  @Test public void while_1() {
    trimminKof("c(N n){N p=n;while(p!=null){if(dns.c(p))return true;++i;}return false;}")
        .gives("c(N n){for(N p=n;p!=null;){if(dns.c(p))return true;++i;}return false;}").stays();
  }

  @Test public void while_2() {
    trimminKof("c(int i){int p=i;while(p<10)++p;return false;}")//
        .gives("c(int i){for(int p=i;p<10;)++p;return false;}")//
        .stays();
  }

  @Test public void while_3() {
    trimminKof("c(int i){int p=i,a=0;f(++a);while(p<10)++p;return false;}")//
        .stays();
  }

  @Test public void while_4() {
    trimminKof("c(N i){N p=i;int a=5;f(++a);while(p<10)p=p.e();return false;}")//
        .stays();
  }

  @Test public void with_aay_a() {
    trimminKof("int[] a=new int[]{1,2,3,4,5};for(int i=0;;){a[i]=0;++i;}")//
        .gives("for(int i=0;;){(new int[]{1,2,3,4,5})[i]=0;++i;}")//
        .gives("for(int ¢=0;;){(new int[]{1,2,3,4,5})[¢]=0;++¢;}")//
        .gives("for(int ¢=0;;++¢){(new int[]{1,2,3,4,5})[¢]=0;}")//
        .gives("for(int ¢=0;;++¢)(new int[]{1,2,3,4,5})[¢]=0;")//
        .stays();
  }
}
