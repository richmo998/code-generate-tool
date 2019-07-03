## 自动生成代码工具
- 整体微服务运行
- 调整application.properties中关于数据库信息

- 调整com.xhg.util.generate.utils.CommonPageParser类中templateBasePath模板变量地址
  ```
  注意项目工具需放在纯英文路径下，否则有可能识别不了中文，导致生成代码失败；
  如果非要放在中文路径下，则需要修改上述templateBasePath变量，使用绝对路径来指定模板即可；
  ```

- 根据实际库名以及table名生成代码请求地址：http://localhost:5565/codeGenerate/code/create?db_name=db_dc_client&package_name=cn.wonhigh.bi.wx.cube&is_controller=true&table_name=applications_message,client_task_status_log,resume_hive_task_info