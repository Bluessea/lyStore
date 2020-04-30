# lyStore
乐优商城微服务
# 项目运行指南
- nginx

  - 注意nginx和Apache端口冲突，启动时确保关闭Apache服务

- mysql

- 前台商城页面

  - 通过IDEA打开ols_portal项目,在命令行输入`live-server --port=9002`即可

- 后台管理页面

  - 进入leyou-manage-web根目录,CMD运行`npm start` 或者通过VScode在命令行输入`npm run dev`

- 服务器端
  - linux 虚拟机地址 192.168.107.131

  - 切换用户:`su - suepr`,

  - 进入ES安装目录 cd /leyou/local/elasticsearch

  - 执行`./elasticsearch运行ES` 
  
  
