package ${packageName}.controller;

import ${packageName}.service.${className}Service;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * ${tableComment}
 *
 */
@RestController
@RequestMapping("/${lowerName}")
@Api(value = "${tableComment}", description = "${tableComment}")
@Slf4j
public class ${className}Controller {

	@Autowired
	private ${className}Service ${lowerName}Service; 
	


}
