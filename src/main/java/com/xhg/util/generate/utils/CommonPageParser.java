package com.xhg.util.generate.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.util.ResourceUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Properties;

public class CommonPageParser {
    private static VelocityEngine ve;
    private static final String CONTENT_ENCODING = "UTF-8";
    private static final Log log = LogFactory.getLog(CommonPageParser.class);

    static {
        try {
//            /Users/richmo/work/百丽/编码小工具积 累/code-gen/src/main/resources/template
//            String templateBasePath = "/Users/richmo/work/百丽/编码小工具积累/code-gen/src/main/resources/template/";
            String templateBasePath = ResourceUtils.getURL(ResourceUtils.CLASSPATH_URL_PREFIX + "template").getPath();

            Properties properties = new Properties();
            properties.setProperty("resource.loader", "file");
            properties.setProperty("file.resource.loader.description", "Velocity File Resource Loader");
            properties.setProperty("file.resource.loader.path", templateBasePath);
            properties.setProperty("file.resource.loader.cache", "true");
            properties.setProperty("file.resource.loader.modificationCheckInterval", "30");
            properties.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.Log4JLogChute");
            properties.setProperty("runtime.log.logsystem.log4j.logger", "org.apache.velocity");
            properties.setProperty("directive.set.null.allowed", "true");
            VelocityEngine velocityEngine = new VelocityEngine();
            velocityEngine.init(properties);
            ve = velocityEngine;
        } catch (Exception e) {
            log.error(e);
        }
    }

    public static void WriterPage(VelocityContext context, String templateName, String fileDirPath, String targetFile) {
        try {
            File file = new File(fileDirPath + targetFile);
            if (!file.exists()) {
                new File(file.getParent()).mkdirs();
            }

            Template template = ve.getTemplate(templateName, CONTENT_ENCODING);
            FileOutputStream fos = new FileOutputStream(file);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos, CONTENT_ENCODING));
            template.merge(context, writer);
            writer.flush();
            writer.close();
            fos.close();


            log.info("-----" + file.getAbsolutePath());
        } catch (Exception e) {
            log.error(e);
        }
    }
}