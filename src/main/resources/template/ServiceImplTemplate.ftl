package ${packageName}.service.impl;

import com.xhg.core.base.BaseServiceImpl;
import ${packageName}.dao.${className}Dao;
import ${packageName}.entity.${className}Entity;
import ${packageName}.service.${className}Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * ${tableComment}
 */
@Service
@Slf4j
public class  ${className}ServiceImpl extends BaseServiceImpl<${className}Entity, Integer> implements ${className}Service {

	@Autowired
	private ${className}Dao ${lowerName}Dao;

	@Autowired
	protected void setDao(${className}Dao dao) {
		super.setDao(dao);
	}

}
