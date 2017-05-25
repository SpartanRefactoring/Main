#!/bin/sed -f 
# Skip lines containing Java comments 
/[/][/]\s*[:alpha:]\s*[:alpha]/ n
/\*\*/ n
/\/\*/ n
/^\s*\*/ n

# Remove space runs, spaces at beginning and end of line 
s/\s\s/ /g 
s/^\s*//g 
s/\s*$/ /g 

# Remove non grammatically essential spaces 
s/\([^[:alpha:]]\) \([^[:alpha:]]\)/\1\2/g
s/\([[:alpha:]]\) \([^[:alnum:]]\)/\1\2/g
s/\([^[:alnum:]]\) \([[:alpha:]]\)/\1\2/g
s/\([^[:alnum:]]\) \([[:alpha:]]\)/\1\2/g
