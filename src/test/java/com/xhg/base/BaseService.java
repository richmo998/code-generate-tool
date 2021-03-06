package com.xhg.base;

import com.xhg.Application;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
//@Rollback//保证每次测试类执行完后数据库进行回滚，防止测试时产生脏数据
//@Transactional
public class BaseService {

    protected final static Logger logger = LoggerFactory.getLogger(BaseService.class);


}
