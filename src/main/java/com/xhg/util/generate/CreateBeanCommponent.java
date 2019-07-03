package com.xhg.util.generate;

import com.xhg.service.impl.DataSourceCommponent;
import com.xhg.util.generate.bean.ColumnData;
import com.xhg.util.generate.utils.CommUtil;
import com.xhg.util.generate.utils.TableConvert;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class CreateBeanCommponent {

    @Autowired
    private DataSourceCommponent dataSourceCommponent;

    String SQLTables = "show tables";

    private Set<String> importPackageSet = new HashSet<>();
    private static List<String> ignoreFieldList = new ArrayList<>(
            Arrays.asList("id", "createdTime", "updatedTime", "createdUserId", "updatedUserId", "isEnable,", "enable",
                    "created_time", "updated_time", "created_user_id", "updated_user_id")
    );

    public String getImportPackages() {
        StringBuffer sbImportPackage = new StringBuffer();
        for (String str : importPackageSet) {
            sbImportPackage.append(str);
        }
        return sbImportPackage.toString();
    }


    public List<String> getTables(String dbName) throws SQLException {
        Connection con = dataSourceCommponent.getDs(dbName).getConnection();
        PreparedStatement ps = con.prepareStatement(SQLTables);
        ResultSet rs = ps.executeQuery();
        List list = new ArrayList();
        while (rs.next()) {
            String tableName = rs.getString(1);
            list.add(tableName);
        }
        rs.close();
        ps.close();
        con.close();
        return list;
    }

    public List<ColumnData> getColumnDatas(String dbName, String tableName) throws SQLException {
        StringBuffer sql = new StringBuffer();
        sql.append("select ");
        sql.append("t1.table_comment, ");
        sql.append("t2.column_name ,t2.data_type,column_comment,0,0,t2.character_maximum_length,t2.is_nullable nullable ");
        sql.append("from information_schema.tables t1 ");
        sql.append("left join information_schema.columns t2 on t2.table_name = t1.table_name and t2.table_schema = t1.table_schema ");
        sql.append("where t1.table_name = '").append(tableName).append("' and t1.table_schema =  '").append(dbName).append("'");


        Connection con = dataSourceCommponent.getDs(dbName).getConnection();
        PreparedStatement ps = con.prepareStatement(sql.toString());
        List columnList = new ArrayList();
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String tableComment = rs.getString(1);
            String name = rs.getString(2);
            String type = rs.getString(3);
            String comment = rs.getString(4);
            String precision = rs.getString(5);
            String scale = rs.getString(6);
            String charmaxLength = rs.getString(7) == null ? "" : rs.getString(7);
            String nullable = TableConvert.getNullAble(rs.getString(8));
            type = getType(type, precision, scale, name);

            ColumnData cd = new ColumnData();
            cd.setTableComment(tableComment);
            cd.setColumnName(name);
            cd.setDataType(type);
            cd.setColumnType(rs.getString(2));
            cd.setColumnComment(comment);
            cd.setPrecision(precision);
            cd.setScale(scale);
            cd.setCharmaxLength(charmaxLength);
            cd.setNullable(nullable);
            formatFieldClassType(cd);
            columnList.add(cd);
        }
        rs.close();
        ps.close();
        con.close();
        return columnList;
    }


    public String getVo2DtoFeilds(String dbName, String tableName, String source, String destiny) throws SQLException {
        List<ColumnData> dataList = getColumnDatas(dbName, tableName);
        StringBuffer getset = new StringBuffer();
        int dateCount = 0;
        for (ColumnData d : dataList) {
            if (d.getDataType() == "Date") {
                dateCount++;
            }
        }
        if (dateCount > 0) {
            getset.append("\r\t\t");
            getset.append("DateFormat dtFormat = new SimpleDateFormat(\"yyyy-MM-dd\");");
        }

        for (ColumnData d : dataList) {
            String name = CommUtil.formatName(d.getColumnName());
            String maxChar = name.substring(0, 1).toUpperCase();
            String method = maxChar + name.substring(1, name.length());

            getset.append("\r\t\t");
            if (d.getDataType() != "String" && !name.equals("updatedAccountId") && !name.equals("createdAccountId")) {
                switch (d.getDataType()) {
                    case "Boolean":
                        getset.append("if (StringUtils.isNotEmpty(" + source + ".get" + method + "()" + ")");
                        getset.append(" && StringUtils.isNotBlank(" + source + ".get" + method + "()" + "))" + "{\r\t\t\t");
                        getset.append(destiny + ".set" + method + "(Boolean.parseBoolean(" + source + ".get" + method + "()));");
                        getset.append("\r\t\t}");
                        break;
                    case "Double":
                        getset.append("if (StringUtils.isNotEmpty(" + source + ".get" + method + "()" + ")");
                        getset.append(" && StringUtils.isNotBlank(" + source + ".get" + method + "()" + "))" + "{\r\t\t\t");
                        getset.append(destiny + ".set" + method + "(Double.parseDouble(" + source + ".get" + method + "()));");
                        getset.append("" + "\r\t\t}");
                        break;
                    case "Integer":
                        getset.append("if (StringUtils.isNotEmpty(" + source + ".get" + method + "()" + ")");
                        getset.append(" && StringUtils.isNotBlank(" + source + ".get" + method + "()" + "))" + "{\r\t\t\t");
                        getset.append(destiny + ".set" + method + "(Integer.parseInt(" + source + ".get" + method + "()));");
                        getset.append("" + "\r\t\t}");
                        break;
                    case "Date":
                        getset.append("if (StringUtils.isNotEmpty(" + source + ".get" + method + "()" + ")");
                        getset.append(" && StringUtils.isNotBlank(" + source + ".get" + method + "()" + "))" + "{\r\t\t\t" + "try " + "{\r\t\t\t\t");
                        getset.append(destiny + ".set" + method + "(dtFormat.parse(" + source + ".get" + method + "()));\r\t\t\t}\r\t\t\t");
                        getset.append("catch (Exception e) {\r\t\t\t\t" + "e.printStackTrace();" + "				\r\t\t\t}");
                        getset.append("\r\t\t}");
                        break;
                    case "BigDecimal":
                        getset.append("if (StringUtils.isNotEmpty(" + source + ".get" + method + "()" + ")");
                        getset.append(" && StringUtils.isNotBlank(" + source + ".get" + method + "()" + "))" + "{\r\t\t\t");
                        getset.append(destiny + ".set" + method + "(new BigDecimal(" + source + ".get" + method + "()));");
                        getset.append("\r\t\t}");
                        break;
                    case "Long":
                        getset.append("if (StringUtils.isNotEmpty(" + source + ".get" + method + "()" + ")");
                        getset.append("	&& StringUtils.isNotBlank(" + source + ".get" + method + "()" + "))" + "{\r\t\t\t");
                        getset.append(destiny + ".set" + method + "(Long.parseLong(" + source + ".get" + method + "()));");
                        getset.append("\r\t\t}");
                        break;
                    case "Float":
                        getset.append("if (StringUtils.isNotEmpty(" + source + ".get" + method + "()" + ")");
                        getset.append("	&& StringUtils.isNotBlank(" + source + ".get" + method + "()" + "))" + "{\r\t\t\t");
                        getset.append(destiny + ".set" + method + "(Float.parseFloat(" + source + ".get" + method + "()));");
                        getset.append("\r\t\t}");
                        break;
                    default:
                        getset.append(destiny + ".set" + method + "(" + source + ".get" + method + "());");
                        break;
                }
            } else {
                getset.append(destiny + ".set" + method + "(" + source + ".get" + method + "());");
            }
        }
        return getset.toString();
    }

    public String getDtoFeilds(String dbName, String tableName) throws SQLException {
        List<ColumnData> dataList = getColumnDatas(dbName, tableName);
        StringBuffer str = new StringBuffer();
        for (ColumnData d : dataList) {
            String name = CommUtil.formatName(d.getColumnName());
            if (ignoreFieldList.contains(name)) {
                continue;
            }
            String type = d.getDataType();
            String comment = d.getColumnComment();
            str.append("\t/**\n").append("\t * ").append(comment).append("\n").append("\t */\n");
            str.append("\t@ApiModelProperty(value = \"").append(comment).append("\")\n");
            str.append("\tprivate ").append(type + " ").append(name).append(";\n\n");
        }
        if (StringUtils.isNotBlank(str.toString())) {
            return str.toString().substring(1);
        }
        return StringUtils.EMPTY;
    }

    public String getEntityFeilds(String dbName, String tableName) throws SQLException {
        List<ColumnData> dataList = getColumnDatas(dbName, tableName);
        StringBuffer str = new StringBuffer();
        for (ColumnData d : dataList) {
            String name = CommUtil.formatName(d.getColumnName());
            if (ignoreFieldList.contains(name)) {
                continue;
            }
            String type = d.getDataType();
            String comment = d.getColumnComment();
            str.append("\t/**\n").append("\t * ").append(comment).append("\n").append("\t */\n");
            str.append("\tprivate ").append(type + " ").append(name).append(";\n\n");
        }
        return str.toString();
    }

    private String formatTableName(String name) {
        String[] split = name.split("_");
        if (split.length > 1) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < split.length; i++) {
                if (split[i].equalsIgnoreCase("t") || split[i].equalsIgnoreCase("ft")
                        || split[i].equalsIgnoreCase("bu") || split[i].equalsIgnoreCase("ifr")
                        || split[i].equalsIgnoreCase("db")|| split[i].equalsIgnoreCase("2c")
                        || split[i].equalsIgnoreCase("2b")) {
                    continue;
                }
                if ((i == 0 || i == 1) && ("activity".equalsIgnoreCase(split[i]) || "cycling".equalsIgnoreCase(split[i]))) {
                    continue;
                }


                String tempName = split[i].substring(0, 1).toUpperCase() + split[i].substring(1);
                sb.append(tempName);
            }

            String className = sb.toString();
            /*if (String.valueOf(className.charAt(0)).matches("\\d")) {
                className = className.substring(1);
            }*/
            return className.substring(0, 1).toUpperCase() + className.substring(1);
        }
        return StringUtils.EMPTY;
    }

    private void formatFieldClassType(ColumnData columnt) {
        String fieldType = columnt.getColumnType();
        String scale = columnt.getScale();

        if ("N".equals(columnt.getNullable())) {
            columnt.setOptionType("required:true");
        }
        if (("datetime".equals(fieldType)) || ("time".equals(fieldType))) {
            columnt.setClassType("easyui-datetimebox");
        } else if ("date".equals(fieldType)) {
            columnt.setClassType("easyui-datebox");
        } else if ("int".equals(fieldType)) {
            columnt.setClassType("easyui-numberbox");
        } else if ("number".equals(fieldType)) {
            if ((StringUtils.isNotBlank(scale)) && (Integer.parseInt(scale) > 0)) {
                columnt.setClassType("easyui-numberbox");
                if (StringUtils.isNotBlank(columnt.getOptionType())) {
                    columnt.setOptionType(columnt.getOptionType() + "," + "precision:2,groupSeparator:','");
                } else {
                    columnt.setOptionType("precision:2,groupSeparator:','");
                }
            } else {
                columnt.setClassType("easyui-numberbox");
            }
        } else if (("float".equals(fieldType)) || ("double".equals(fieldType)) || ("decimal".equals(fieldType))) {
            columnt.setClassType("easyui-numberbox");
            if (StringUtils.isNotBlank(columnt.getOptionType())) {
                columnt.setOptionType(columnt.getOptionType() + "," + "precision:2,groupSeparator:','");
            } else {
                columnt.setOptionType("precision:2,groupSeparator:','");
            }
        } else {
            columnt.setClassType("easyui-validatebox");
        }
    }

    public String getType(String dataType, String precision, String scale, String name) {
        dataType = dataType.toLowerCase();
        if (dataType.contains("char") || dataType.contains("text")) {
            dataType = "String";
        } else if (dataType.contains("bit")) {
            dataType = "Boolean";
            if (!ignoreFieldList.contains(name)) {
                importPackageSet.add("import java.lang.Boolean;" + "\r");
            }
        } else if (dataType.contains("bigint")) {
            dataType = "Long";
            if (!ignoreFieldList.contains(name)) {
                importPackageSet.add("import java.lang.Long;" + "\r");
            }
        } else if (dataType.contains("int")) {
            dataType = "Integer";
            if (!ignoreFieldList.contains(name)) {
                importPackageSet.add("import java.lang.Integer;" + "\r");
            }
        } else if (dataType.contains("float")) {
            dataType = "Float";
            if (!ignoreFieldList.contains(name)) {
                importPackageSet.add("import java.lang.Float;" + "\r");
            }
        } else if (dataType.contains("double")) {
            dataType = "Double";
            if (!ignoreFieldList.contains(name)) {
                importPackageSet.add("import java.lang.Double;" + "\r");
            }
        } else if (dataType.contains("number")) {
            if ((StringUtils.isNotBlank(scale)) && (Integer.parseInt(scale) > 0)) {
                dataType = "BigDecimal";
                if (!ignoreFieldList.contains(name)) {
                    importPackageSet.add("import java.math.BigDecimal;" + "\r");
                }
            } else if ((StringUtils.isNotBlank(precision)) && (Integer.parseInt(precision) > 6)) {
                dataType = "Long";
                if (!ignoreFieldList.contains(name)) {
                    importPackageSet.add("import java.lang.Long;" + "\r");
                }
            } else {
                dataType = "Integer";
                if (!ignoreFieldList.contains(name)) {
                    importPackageSet.add("import java.lang.Integer;" + "\r");
                }
            }
        } else if (dataType.contains("decimal")) {
            dataType = "BigDecimal";
            if (!ignoreFieldList.contains(name)) {
                importPackageSet.add("import java.math.BigDecimal;" + "\r");
            }
        } else if (dataType.contains("date")) {
            dataType = "Date";
            if (!ignoreFieldList.contains(name)) {
                importPackageSet.add("import java.util.Date;" + "\r");
            }
        } else if (dataType.contains("time")) {
            dataType = "Timestamp";
            if (!ignoreFieldList.contains(name)) {
                importPackageSet.add("import java.sql.Timestamp;" + "\r\t");
            }
        } else if (dataType.contains("clob")) {
            dataType = "Clob";
            if (!ignoreFieldList.contains(name)) {
                importPackageSet.add("import java.sql.Clob;" + "\r\t");
            }
        } else {
            dataType = "Object";
            if (!ignoreFieldList.contains(name)) {
                importPackageSet.add("import java.lang.Object;" + "\r\t");
            }
        }
        return dataType;
    }

    public String getTablesNameToClassName(String tableName) {
        String tempTables = formatTableName(tableName);
        return tempTables;
    }

    public String getBeanStr(String dbName, String tableName) throws SQLException {
        List<ColumnData> dataList = getColumnDatas(dbName, tableName);
        StringBuffer str = new StringBuffer();


        for (int i = 0; i < dataList.size(); i++) {
            String name = CommUtil.formatName(dataList.get(i).getColumnName());
            String maxChar = name.substring(0, 1).toUpperCase();
            String method = "get" + maxChar + name.substring(1, name.length()) + "()";

            if (i == 0) {
                str.append(" " + name + ":\"+").append(method + "+");
            } else if (i == dataList.size() - 1) {
                str.append("\" " + name + ":\"+").append(method + "+\"");
            } else {
                str.append("\" " + name + ":\"+").append(method + "+");
            }
        }

        return str.toString();
    }


}