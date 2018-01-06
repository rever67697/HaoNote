Gson解析给字段取别名,如json字段名为package,可以这样写:

~~~~java
@SerializedName("package")
private String WxPackage;
~~~~

