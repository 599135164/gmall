# gmall

尚硅谷谷粒商城练习

#### 项目架构图

![架构图](https://github.com/599135164/gmall/blob/master/main.png)

| 首页     | 静态页面，包含了商品分类，搜索栏，商品广告位。 |
| -------- | ---------------------------------------------- |
| 全文搜索 | 通过搜索栏填入的关键字进行搜索，并列表展示     |
| 分类查询 | 根据首页的商品类目进行查询                     |
| 商品详情 | 商品的详细信息展示                             |
| 购物车   | 将有购买意向的商品临时存放的地方               |
| 单点登录 | 用户统一登录的管理                             |
| 结算     | 将购物车中勾选的商品初始化成要填写的订单       |
| 下单     | 填好的订单提交                                 |
| 支付服务 | 下单后，用户点击支付，负责对接第三方支付系统。 |
| 订单服务 | 负责确认订单是否付款成功，并对接仓储物流系统。 |
| 仓储物流 | 独立的管理系统，负责商品的库存。               |
| 后台管理 | 主要维护类目、商品、库存单元、广告位等信息。   |

#### SpringCloud与Dubbo的区别？

解决分布式的问题：

​	微服务：springboot + springcloud + mybatis + mybatis-plus + redis + mysql + es + nginx + activemq + 		mycat/sharding JDBC + docker + k8s/jenkins

服务治理 + 服务调用 dubbo

数据传输方式：

​	dubbo：二进制方式

​	spring cloud：HTTP

数据访问方式：

​	dubbo：		   rpc

​	spring cloud：Restful API

功能方面：

​	dubbo：服务的治理和调用

​	spring cloud  众多组件 与 spring source

本质来讲：

​	dubbo：jar包

​	spring cloud：spring source中的一员

开发角度：

​	dubbo：停止更新5年！依赖dubbo version！

​	spring cloud：一直更新维护。

分布式角度：

​	dubbo：传输数据方式优于spring cloud！





