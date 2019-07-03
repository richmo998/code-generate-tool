package com.xhg.util.generate;


import com.xhg.util.generate.bean.ColumnData;
import com.xhg.util.generate.utils.CommonPageParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
@Slf4j
public class CodeGenerateCommponent {

    private static String idCriteria = "#{id}";

    @Autowired
    private CreateBeanCommponent createBeanCommponent;
    @Value("${config.source.root.package}")
    private String rootPackage;
    @Value("${config.source.root.test.package}")
    private String rootTestPackage;


    public void codeGenerate(String dbName, String tableName, String packageName, String isController, String sourcePath) {
        rootPackage = rootPackage.replace("/", File.separator);
        rootTestPackage = rootTestPackage.replace("/", File.separator);

        String className = createBeanCommponent.getTablesNameToClassName(tableName);
        String lowerName = className.substring(0, 1).toLowerCase() + className.substring(1);
        String srcPath = sourcePath + rootPackage + File.separator;
        String srcTestPath = sourcePath + rootTestPackage + File.separator;

//        String pckPath = srcPath + File.separator + packageName.replace(".", File.separator) + File.separator;
        String pckPath = srcPath + packageName.replace(".", File.separator) ;
        String testPckPath = srcTestPath + File.separator + packageName.replace(".", File.separator) + File.separator;

        String testPath = testPckPath + File.separator + "service" + File.separator + "impl" + File.separator;
        String entityPath = pckPath + File.separator + "entity" + File.separator;
        String dtoPath = pckPath + File.separator + "dto" + File.separator;
        String controllerPath = pckPath + File.separator + "controller" + File.separator;
        String daoPath = pckPath + File.separator + "dao" + File.separator;
        String servicePath = pckPath + File.separator + "service" + File.separator;
        String serviceImplPath = pckPath + File.separator + "service" + File.separator + "impl" + File.separator;
        String mapperPath = pckPath + File.separator + "mapper" + File.separator;
        String mapperDaoPath = pckPath + File.separator + "mapper" + File.separator;

        String testObject = className + "ServiceJunitTest.java";
        String entityObject = className + "Entity.java";
        String dtoObject = className + "DTO.java";
        String controllerObject = className + "Controller.java";
        String daoObject = className + "Dao.java";
        String serviceObject = className + "Service.java";
        String serviceImplObject = className + "ServiceImpl.java";
        String mapperObject = className + "Mapper.xml";
        String mapperDaoObject = className + "Dao.xml";


        VelocityContext context = new VelocityContext();
        context.put("className", className);
        context.put("lowerName", lowerName);
        context.put("tableName", tableName);
        context.put("packageName", packageName);
        //context.put("author", author);
        context.put("idCriteria", idCriteria);
        try {
            context.put("entityFeilds", createBeanCommponent.getEntityFeilds(dbName, tableName));
            context.put("dtoFeilds", createBeanCommponent.getDtoFeilds(dbName, tableName));
            context.put("importPackages", createBeanCommponent.getImportPackages());
            context.put("beanStr", createBeanCommponent.getBeanStr(dbName, tableName));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            List<ColumnData> columnList = createBeanCommponent.getColumnDatas(dbName, tableName);
            if (columnList == null || columnList.size() == 0) {
                throw new Exception(tableName + " 表不存在");
            }
            context.put("columnDatas", columnList);
            context.put("tableComment", columnList.get(0).getTableComment());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        CommonPageParser.WriterPage(context, "EntityTemplate.ftl", entityPath, entityObject);
        CommonPageParser.WriterPage(context, "DtoTemplate.ftl", dtoPath, dtoObject);
        CommonPageParser.WriterPage(context, "DaoTemplate.ftl", daoPath, daoObject);
        CommonPageParser.WriterPage(context, "ServiceTemplate.ftl", servicePath, serviceObject);
        CommonPageParser.WriterPage(context, "ServiceImplTemplate.ftl", serviceImplPath, serviceImplObject);
        CommonPageParser.WriterPage(context, "MapperTemplate.xml", mapperPath, mapperObject);
        CommonPageParser.WriterPage(context, "MapperDaoTemplate.xml", mapperDaoPath, mapperDaoObject);
        if (StringUtils.isNotBlank(isController) && Boolean.TRUE.toString().equalsIgnoreCase(isController)) {
            CommonPageParser.WriterPage(context, "ControllerTemplate.ftl", controllerPath, controllerObject);
        }
        //CommonPageParser.WriterPage(context, "base/BaseDaoTemplate.ftl", daoPath + "base\\", "BaseDao.java");
        //CommonPageParser.WriterPage(context, "base/BaseService.ftl", servicePath + "base\\", "BaseService.java");
        //CommonPageParser.WriterPage(context, "base/BaseServiceImpl.ftl", serviceImplPath + "base\\", "BaseServiceImpl.java");
        //CommonPageParser.WriterPage(context, "/junit/ServiceJunitTemplate.ftl", testPath, testObject);
        log.info("----------------------------代码生成完毕----------------------------");
    }
}