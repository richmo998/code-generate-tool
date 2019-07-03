package com.xhg.controller;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ZipUtil;
import com.xhg.service.IDatabaseService;
import com.xhg.util.DownloadUtil;
import com.xhg.util.generate.CodeGenerateCommponent;
import com.xhg.util.generate.constant.CodeResourceConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/codeGenerate/code")
@Slf4j
public class CodeController {

    @Autowired
    private CodeGenerateCommponent codeGenerateCommponent;
    @Autowired
    private IDatabaseService databaseService;

    @CrossOrigin
    @GetMapping(value = "/create")
    public String create(HttpServletResponse response,
                         @RequestParam(name = "db_name") String dbName,
                         @RequestParam("table_name") String tableName,
                         @RequestParam("package_name") String packageName,
                         @RequestParam(value = "is_controller", required = false) String isController) {
        if (StringUtils.isBlank(dbName)) {
            return "库名为空";
        }
        if (StringUtils.isBlank(tableName)) {
            return "表名为空";
        }
        if (StringUtils.isBlank(packageName)) {
            return "包为空";
        }
        if (!tableName.matches("^[\\w,]+$")) {
            return "表名为空";
        }
        if (!packageName.matches("^[\\w.]+$")) {
            return "包名格式错误";
        }

        Random random = new Random();
//        String sourcePath = CodeResourceConstant.SOURCE_PATH + random.nextLong() + File.separator;
        String sourcePath = CodeResourceConstant.SOURCE_PATH + "generated-code" + File.separator;
        try {

            String[] tableArr = tableName.split(",");
            for (String table : tableArr) {
                codeGenerateCommponent.codeGenerate(dbName, table, packageName, isController, sourcePath);
            }

            ZipUtil.zip(sourcePath + "src");
            DownloadUtil.download(response, sourcePath + "src.zip", tableArr[0]);
            FileUtil.del(sourcePath);

            return "下载成功";
        } catch (Exception e) {
            e.printStackTrace();
            FileUtil.del(sourcePath);
            return "表不存在";
        }
    }

    @CrossOrigin
    @GetMapping(value = "/databaseList")
    public List<String> databaseList() {
        return databaseService.getDbs();
    }

    public static void main(String[] args) {
        System.out.println("dsafdsa1-[]".matches("^[\\w,]+$"));
    }
}
