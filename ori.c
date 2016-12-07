#include <stdio.h> 
  char buff[1 << 20];
main() {
  int c;
  int depth = 0;
  int length=0;

  while ((c = getchar())!=EOF) {
    switch (c) {
      case '\n': c = ' '; break;
      case '{': ++depth;length = 0; break;
      case '}': --depth;
    }
    buff[length++] = c;
                if (depth == 1 && c == '}') {
                  buff[length++] = '\0';
                  printf("/* LENGTH=%d */ %s\n",length, buff); 
                  length = 0;
                }


  }


}
