# Spartanizer? Huh?
The Spartanizer is:
- An eclipse plugin
- Offers in the problems view tips for simplifying your code 
- Make your code laconic: say much in few words.

The Spartanizer help you make a sequence small, nano-refactorings of your code, to make it shorter, and more conforming to a language of nano-patterns. The resulting code is not just shorter, it is more regular. The spartanization process tries to remove as many distracting details and variations from the code, stripping it to its bare bone.

This includes removal of piles of syntactic baggage, which is code that does not nothing, except for being there:  curly brackets around one statement, initializations which reiterate the default, modifiers which do not change the semantics, implicit call to `super()` which every constructor has, fancy, but uselessly long variable names, variables which never vary and contain temporaries and  many more. Overall, the Spartanizer has over 100 tippers.

# Contents

[List of all the tippers](https://github.com/SpartanRefactoring/Spartanizer/wiki/List-of-Tippers "List of the Tippers")

![spartanization](https://cloud.githubusercontent.com/assets/15183108/19212649/59d65e3e-8d5e-11e6-9940-ac7a070be7d6.gif)



# Video Demo

Click on the picture below to watch a video demonstration on YouTube.

[![IMAGE ALT TEXT](https://img.youtube.com/vi/33npJI-MZ1I/0.jpg)](https://www.youtube.com/watch_popup?v=49M55azHHM0 "Spartanization Demo")

# Installation

- Installation button (drag to your eclipse workspace)
<a href="http://marketplace.eclipse.org/marketplace-client-intro?mpc_install=2617709" class="drag" title="Drag to your running Eclipse workspace to install Spartan Refactoring"><img class="img-responsive" src="https://marketplace.eclipse.org/sites/all/themes/solstice/public/images/marketplace/btn-install.png" alt="Drag to your running Eclipse workspace to install Spartan Refactoring" /></a>
- This plugin's <a href="https://www.google.co.il/url?sa=t&rct=j&q=&esrc=s&source=web&cd=1&ved=0ahUKEwj7p7iPwL7PAhUrI8AKHW87AVsQFggaMAA&url=https%3A%2F%2Fmarketplace.eclipse.org%2Fcontent%2Fspartan-refactoring-0&usg=AFQjCNFaOBCLW8-CKYYnfLFCjakdWM1qjA&sig2=Z1zbbkq96-iECkhmMf5Qcw&bvm=bv.134495766,d.ZGg">page on market place</href>

# Development Status

 [![Build Status](https://travis-ci.org/SpartanRefactoring/Spartanizer.svg?branch=master)](https://travis-ci.org/SpartanRefactoring/Spartanizer)

[![codecov](https://codecov.io/gh/TechnionYP5777/SmartCity-ParkingManagement/branch/master/graph/badge.svg)](https://codecov.io/gh/SpartanRefactoring/Spartanizer)


![spartan resized](https://cloud.githubusercontent.com/assets/15859817/23854098/7f02ba4e-07f8-11e7-8bd9-8ebe2ccbe9e8.png)


# What is this?
The Spartanizer is an  Eclipse plugin that automatically applies the principles
of *[Spartan Programming]* to your Java code. It applies many different tippers,
   which are little rules that provide suggestions on how to shorten and
   simplify your code, e.g, by using fewer variables, factoring out common
   structures, more efficient use of control flow, etc. 

# Background
This project was conceived as an academic project in the [Technion - Israel
Institute of Technology], and was later developed for several years by
different students and members of the Computer Science faculty.

The refactorings made by this plug-in are based on the concept of Spartan
Programming, which suggests guidelines for writing short, clean code. There's a
lot of reading material on the subject in the [project wiki].

## Stable version
### Installing from a jar file
1. Download the jar file from the latest [Release].
2. Close Eclipse.
3. Put the jar file in eclipse/dropin folder.
4. Start Eclipse. 

## Compiling from source
#### Using Maven and Git in command line(recommended)
By assuming that the current directory is at relative path "?", Clone the repository by:

```
git clone https://github.com/SpartanRefactoring/Spartanizer.git
```
After cloning the repository, go into the ?/Spartanizer/SpartanRefactoring
directory and execute:

```
mvn package
```
The packaged plug-in (.jar file) will be created in the
`?/Spartanizer/SpartanRefactoring/target` directory.
Copy the .jar file into your eclipse directory, Eclipse/dropins or plugins, and
run Eclipse.

#### From inside Eclipse

1. Open Eclipse and go to "*Install New Software...*". From the list of install
   sites, pick *The Eclipse Project Updates* and make sure you've installed all
   the items from the categories **Eclipse Platform**, **Eclipse Platform SDK**
   and **Eclipse Plugin Development Tools**. Failure to install one of these
   will result in import errors.

2. Create a *run configuration* by running the project (Ctrl+F11), then elect
to run it as an Eclipse Application.
    * If a new instance of Eclipse doesn't launch, open the run configurations
    for the project and make sure that in the *Program to Run* box, "Run a
    product" is selected and the box next to it says
    "org.eclipse.platform.ide".

3. Go to *File -> Export...*. Under the *Plug-in Development category*, choose
   the *Deployable plug-ins and fragments*, and continue until the plug-in has
   been built successfully.

A *tipper*, in the context of this project, is a small object responsible for
converting one form of code into another, under two major assumptions:

1. The latter is shorter and/or more comprehensible than the former.

2. Both forms are semantically equivalent, meaning that both versions do
   exactly the same thing (yet the latter may perform more efficiently).

Consider this basic programmers' mistake:
```java
if(myString.length() < 5) {
    return true;
} else {
    return false;
}
```
Which can be shortened to:
```java
if(myString.length() < 5)
    return true;
return false;
```
Or even:
```java
return myString.length() < 5;
```
Spartan Refactoring first detects this problem by:
1. Finding a matching tipper,
2. Notifying the user that a spartanization can be made
3. Finally while preserving the programmer's intention, revises the `if` block
   all the way to the last example.

#### Method of operation
The plugin works by analyzing the [abstract syntax tree] (AST) generated by
Eclipse for each of the compilation units in the current Java project. After
the AST has been generated, and the user had asked to apply a set of tipper to
the code, it is traversed using an [ASTVisitor], and the tipper are applied one
by one to the tree.

After the traversal is complete, the transformations made to the AST are
written back to the source code.

## License
The project is available under the **[MIT License]**

[Release]: https://github.com/SpartanRefactoring/Spartanizer/releases/tag/2.6.3
[Spartan Programming]: http://blog.codinghorror.com/spartan-programming/
[project wiki]: https://github.com/SpartanRefactoring/spartan-refactoring/wiki/Spartan-Programming
[Technion - Israel Institute of Technology]: http://www.technion.ac.il/en/
[abstract syntax tree]: https://en.wikipedia.org/wiki/Abstract_syntax_tree
[ASTVisitor]: http://help.eclipse.org/mars/index.jsp?topic=%2Forg.eclipse.jdt.doc.isv%2Freference%2Fapi%2Forg%2Feclipse%2Fjdt%2Fcore%2Fdom%2FASTVisitor.html
[MIT License]: https://opensource.org/licenses/MIT


