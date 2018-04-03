## implement与api的区别

在android studio3.0.0 之后 , api与compile指令完全等同,没区别.即此moudle添加的依赖,该项目的所有moudle都可以访问(都可以使用该依赖库).

而implement的特点是:外部moudle无法访问该依赖,即使用该指令添加的依赖只有此moudle可以访问,其他moudle无法访问.

使用implement指令的编译速度比使用api指令的编译速度快.