package com.xhg.controller;

import com.alibaba.fastjson.JSON;
import com.xhg.base.BaseService;
import com.xhg.util.generate.CodeGenerateCommponent;
import com.xhg.util.generate.constant.CodeResourceConstant;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.List;
import java.util.Random;

public class CodeControllerTest extends BaseService {

    @Autowired
    private CodeController codeController;
    @Autowired
    private CodeGenerateCommponent codeGenerateCommponent;

    @Test
    public void createTest() {
        /*String tableName = "t_activity_activity,t_activity_activity2";
        String[] tableArr = tableName.split(",");

        Random random = new Random();
        String sourcePath = CodeResourceConstant.SOURCE_PATH + File.separator + random.nextLong() + File.separator;
        for (String table : tableArr) {
            codeGenerateCommponent.codeGenerate("recycledb_dev", table, "com.xhg.demo", sourcePath);
        }*/


        codeController.create(null, "recycledb_dev", "t_cycling_bu_2c_order_extend", "com.xhg.demo", "true");

    }

    //@Test
    public void databaseList() {
        List<String> list = codeController.databaseList();
        System.out.println(JSON.toJSONString(list));
    }
}
