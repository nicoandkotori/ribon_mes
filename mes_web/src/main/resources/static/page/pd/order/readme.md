# 委外前端文件说明

##### 1.dto文件夹--封装了一些DTO类

#### 2.entity文件夹--封装了一些实体类
  通过对象的方式操作属性，能屏蔽直接对具体字段赋值或取值的操作，同时增强可读性，提高可维护性
##### 3.function文件夹--封装了公共函数，多个页面中高频共用的函数都放这里，防止修改一个相同的功能而重复修改代码。
##### 4.util--封装了一些工具类

--JqgridRowHelper：封装了jqgrid中的高频操作
<br>
--EntityUtil：封装了一些实体类的转换函数

##### 5.ColModelConfig.js--存放新增和编辑页面中的ColModel
##### 6.IncludeFunction.js--多个html引入该js后，可以通过修改该文件统一引入js文件
##### 7.ConstConfig.js--常量配置文件