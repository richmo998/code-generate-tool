package ${packageName}.service.impl;

import ${packageName}.service.${className}Service;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;


/**
 *
 * ${tableComment}
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:config/applicationContext.xml"})
@Transactional
public class ${className}ServiceJunitTest {

  	private final static Logger logger = LoggerFactory.getLogger(${className}ServiceJunitTest.class);

	private static Integer id;
	@Autowired
	private ${className}Service $!{lowerName}Service;
	

	
}
