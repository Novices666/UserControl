# 项目介绍
本系统是一个基于`ant design pro`+`SpringBoot`+`MySQL`的全栈项目，实现基本的注册、登录、查询等功能

---

# 部署本项目
推荐使用宝塔进行部署（省时省力，本文以宝塔部署为例进行介绍）
部署前请确保服务器已安装 JDK11+MySQL5.7+Nginx（安装方式请参考开发日志）
## 部署前端

1. 将项目拉取到本地，直接打包（在package.json下点击build）
2. 在宝塔中新建PHP项目，并新建数据库、
3. 上传前端打包好的dist文件夹内的全部文件到新建php项目根目录
4. 在php项目设置的配置文件下新增内容，注意端口号
5. 访问前端页面，查看是否部署成功
```yaml
location /api {
    proxy_pass http://127.0.0.1:8081/api;
    proxy_set_header   X-Forwarded-Proto $scheme;
    proxy_set_header   Host              $http_host;
    proxy_set_header   X-Real-IP         $remote_addr;
}
```

6. 绑定域名（非必要项）
## 部署后端

1. 修改`application.yml`的相关配置信息，并切换到`prod`配置
2. 删除或忽略测试模块，对项目进行打包（本项目使用`maven`构建工具）
3. 上传后端打包好的jar文件到服务器，在宝塔java项目管理处按如图所示新建java-springboot项目；建议删除自动生成的项目执行命令中的`--server.port=8081`（因为配置文件中已指定，同时也方便检查是否部署成功）

![image.png](https://cdn.nlark.com/yuque/0/2023/png/35819403/1699839786299-dcf56d90-a216-4f03-b73e-888d915f730e.png#averageHue=%23f4f4f4&clientId=u6e7db573-bc80-4&id=Mf5Li&originHeight=867&originWidth=792&originalType=binary&ratio=1&rotation=0&showTitle=false&size=91490&status=done&style=none&taskId=u64979403-4e9f-4fc2-9c4f-76bf066e907&title=)

4. 访问一个api路径测试部署是否成功

---

# 开始开发
请在引用或基于本项目的开源信息中，添加本项目地址！

1. 拉取本项目到本地
2. 后端
   1. 使用IDEA打开java文件夹
   2. 修改`application.yml`相关配置，切换到`dev`配置
   3. 文件内容说明
```yaml
│ pom.xml		#项目依赖配置文件
├─sql
│      install.sql	 #数据库初始化
├─src
   ├─main
      ├─java
      │  └─cc
      │      └─novices
      │          └─usercontrol
      │              │  UserControlApplication.java		#项目入口
      │              │  
      │              ├─commons
      │              │      BusinessException.java		#自定义异常类
      │              │      GlobalExceptionHandler.java		#全局异常处理器
      │              │      Result.java		#统一返回类
      │              │      ResultEnum.java		#统一返回类型枚举
      │              │      
      │              ├─constant
      │              │      UserConstant.java		#常量
      │              │      
      │              ├─controller
      │              │      UserController.java
      │              │      
      │              ├─mapper
      │              │      UserMapper.java		
      │              │      
      │              ├─model
      │              │  └─domain
      │              │      │  User.java		#数据库实体类
      │              │      │  
      │              │      └─request
      │              │              UserLoginRequest.java		#登录所需参数实体类
      │              │              UserRegisterRequest.java		#注册所需参数实体类
      │              │              
      │              ├─service
      │              │  │  UserService.java
      │              │  │  
      │              │  └─impl
      │              │          UserServiceImpl.java
      │              │          
      │              └─utils
      └─resources
          │  application.yml		#项目配置
          │  
          └─mapper
                  UserMapper.xml
```

3. 前端
   1. 使用WebStorm或VSCode打开UserControl文件夹
   2. 在项目根目录执行`yarn`或`npm install`安装项目依赖
   3. 文件内容说明
```yaml
│  package.json		#项目依赖及启动器
│      
├─config
│      config.dev.ts		#开发时配置
│      config.ts		#通用配置
│      defaultSettings.ts		#默认主题配置
│      proxy.ts		#代理配置
│      routes.ts		#路由配置
│
├─mock		#前端模拟数据
│          
├─public
│          
└─src
    │  access.ts		#权限管理
    │  app.tsx		#初始化及入口
    │  global.less
    │  global.tsx		
    │  manifest.json		#主要信息配置文件
    │  service-worker.js
    │  typings.d.ts
    ├─components		#组件文件
    │          
    ├─constant		#常量文件
    │      
    ├─e2e
    │      baseLayout.e2e.spec.ts
    │      
    ├─pages		#页面文件
    │              
    ├─plugins		#插件
    │      requestHandler.ts		#全局响应处理文件
    │      
    └─services
        └─ant-design-pro
                api.ts		#接口文件
                index.ts
                login.ts
                typings.d.ts		#类型管理文件
```
# 开发日志
[2023-11-19 增加接口，解决跨域](./Doc/2023-11-19.md)
[2023-11-17 修改为分布式登录](./Doc/2023-11-17.md)
[2023-11-16 整合接口文档](./Doc/2023-11-16.md)
[2023-11-12 部署及上线](./Doc/2023-11-12.md)
[2023-11-11 分装统一返回类型](./Doc/2023-11-11.md)
[2023-11-10 完善注册及查询接口](./Doc/2023-11-10.md)
[2023-11-09 添加逻辑删除](./Doc/2023-11-09.md)
[2023-11-08 完成后端与mysql的对接](./Doc/2023-11-08.md)
[2023-11-07 项目初始化](./Doc/2023-11-07.md)
[2023-11-07 接口文档](./Doc/2023-11-16.md)