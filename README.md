# <a name="introduction"></a>Spartanizer? What is this?

The Spartanizer is:
- An Eclipse plugin
- Offers in the problems view tips for simplifying your code 
- Make your code laconic: say much in few words.

<!-- <img style="float: right;" src="https://www.spartan.org.il/images/logo-header.png"/> -->

The Spartanizer applies the principles of *[Spartan Programming]* to your Java code. It applies many different _tippers_, which are little rules that provide suggestions on how to shorten and
simplify your code, e.g, by using fewer variables, factoring out common structures, more efficient use of control flow, etc. 

The Spartanizer help you make a sequence small, nano-refactorings of your code, to make it shorter, and more conforming to a language of nano-patterns. The resulting code is not just shorter, it is more regular. The spartanization process tries to remove as many distracting details and variations from the code, stripping it to its bare bone.

This includes removal of piles of _syntactic baggage_, which is code that does nothing, except for being there:  curly brackets around one statement, initializations which reiterate the default, modifiers which do not change the semantics, implicit call to `super()` which every constructor has, fancy, but uselessly long variable names, variables which never vary and contain temporaries and  many more. Overall, the Spartanizer has over 100 tippers.

The Spartanizer is an  Eclipse plugin that automatically applies the principles of *[Spartan Programming]* to your Java code. It applies many different tippers, which are little rules that provide suggestions on how to shorten and simplify your code, e.g, by using fewer variables, 
factoring out common structures, more efficient use of control flow, etc.

This project was conceived as an academic project in the [Technion - Israel
Institute of Technology], and was later developed for several years by
different students and members of the Computer Science faculty.

The refactorings made by this plug-in are based on the concept of Spartan
Programming, which suggests guidelines for writing short, clean code. There's a
lot of reading material on the subject in the [project wiki].

# Contents

* [Introduction](#introduction)
* [Video Demo](#video-demo)
* [Installation Instructions](#installation)
* [Background](https://github.com/SpartanRefactoring/Spartanizer/wiki/Background "Background")
* [User Manual](https://github.com/SpartanRefactoring/Spartanizer/wiki/User-Manual "User Manual")
* [Theoretical Background](https://github.com/SpartanRefactoring/Spartanizer/wiki/Theoretical-Background "Theoretical Background")
* [Developer Guide](https://github.com/SpartanRefactoring/Spartanizer/wiki/Developer-Guide "Developer Guide")
* [List of all the tippers](https://github.com/SpartanRefactoring/Spartanizer/wiki/List-of-Tippers "List of the Tippers")

# <a name="video-demo"></a>Video Demo

![spartanization](https://cloud.githubusercontent.com/assets/15183108/19212649/59d65e3e-8d5e-11e6-9940-ac7a070be7d6.gif)

Click on the picture below to watch a video demonstration on YouTube.

[![IMAGE ALT TEXT](https://img.youtube.com/vi/33npJI-MZ1I/0.jpg)](https://www.youtube.com/watch_popup?v=49M55azHHM0 "Spartanization Demo")

# <a name="installation"></a>Installation<a name="installation"></a>

- Installation button (drag to your eclipse workspace)
<a href="http://marketplace.eclipse.org/marketplace-client-intro?mpc_install=2617709" class="drag" title="Drag to your running Eclipse workspace to install Spartan Refactoring"><img class="img-responsive" src="https://marketplace.eclipse.org/sites/all/themes/solstice/public/images/marketplace/btn-install.png" alt="Drag to your running Eclipse workspace to install Spartan Refactoring" /></a>

- This plugin's <a href="https://www.google.co.il/url?sa=t&rct=j&q=&esrc=s&source=web&cd=1&ved=0ahUKEwj7p7iPwL7PAhUrI8AKHW87AVsQFggaMAA&url=https%3A%2F%2Fmarketplace.eclipse.org%2Fcontent%2Fspartan-refactoring-0&usg=AFQjCNFaOBCLW8-CKYYnfLFCjakdWM1qjA&sig2=Z1zbbkq96-iECkhmMf5Qcw&bvm=bv.134495766,d.ZGg">page on market place</href>

More detailed instructions at the following [link](https://github.com/SpartanRefactoring/Spartanizer/wiki/Installation-Instructions).

# References

- Y. Gil and M. Orrú, “Code spartanization: One Rational Approach for Resolving Religious Style Wars” in Proc. of SAC’17, the 32nd ACM Symposium on Applied Computing, Marrakesh, Morocco, April 3–7 2017.

- Y. Gil and M. Orrù, "The Spartanizer: Massive automatic refactoring," 
2017 IEEE 24th International Conference on Software Analysis, Evolution and Reengineering (SANER), Klagenfurt, 2017, pp. 477-481. doi: 10.1109/SANER.2017.7884657
[URL on IEEExplore](http://ieeexplore.ieee.org/stamp/stamp.jsp?tp=&arnumber=7884657&isnumber=7884596)

- [The Spartanizer on IEEE Software Blog](http://blog.ieeesoftware.org/2017/03/the-spartanizer.html "IEEE Software Blog")

## Development Status

 [![Build Status](https://travis-ci.org/SpartanRefactoring/Spartanizer.svg?branch=master)](https://travis-ci.org/SpartanRefactoring/Spartanizer) [![codecov](https://codecov.io/gh/TechnionYP5777/SmartCity-ParkingManagement/branch/master/graph/badge.svg)](https://codecov.io/gh/SpartanRefactoring/Spartanizer)

<!-- ![spartan resized](https://cloud.githubusercontent.com/assets/15859817/23854098/7f02ba4e-07f8-11e7-8bd9-8ebe2ccbe9e8.png) -->

## A (Typical) Spartan Developer

<img style="float: right;" src="https://cloud.githubusercontent.com/assets/15859817/23854098/7f02ba4e-07f8-11e7-8bd9-8ebe2ccbe9e8.png">

## License
The project is available under the **[MIT License]**

[Release]: https://github.com/SpartanRefactoring/Spartanizer/releases/tag/2.6.3
[Spartan Programming]: http://blog.codinghorror.com/spartan-programming/
[project wiki]: https://github.com/SpartanRefactoring/spartan-refactoring/wiki/Spartan-Programming
[Technion - Israel Institute of Technology]: http://www.technion.ac.il/en/
[abstract syntax tree]: https://en.wikipedia.org/wiki/Abstract_syntax_tree
[ASTVisitor]: http://help.eclipse.org/mars/index.jsp?topic=%2Forg.eclipse.jdt.doc.isv%2Freference%2Fapi%2Forg%2Feclipse%2Fjdt%2Fcore%2Fdom%2FASTVisitor.html
[MIT License]: https://opensource.org/licenses/MIT


