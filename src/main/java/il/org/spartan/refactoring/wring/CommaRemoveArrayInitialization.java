package il.org.spartan.refactoring.wring;

import org.junit.*;

@Ignore("issue")
public class CommaRemoveArrayInitialization {
  int a[] = { 1, 2, };
  int b[][] = { { 1, 2, }, a, null, };
  int c[][][] = {,};
  {
    a = b[0];
    b = c[0];
    b[2] = a;
    
  }
}
