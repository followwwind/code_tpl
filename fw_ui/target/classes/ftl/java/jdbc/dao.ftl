<#assign bootName = param.bootName!"com.wind"/>
<#assign author = param.author!"wind"/>
package ${bootName + '.dao'};

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import ${bootName + ".entity." + property};
import ${bootName + ".util.DbUtil"};
import ${bootName + ".util.Page"};

/**
 * ${(remarks=='')?string(name, remarks)} dao
 * @author ${author}
 * @date ${.now?string("yyyy/MM/dd HH:mm:ss")}
 * @version V1.0
 */
public class ${property}Dao {

    /**
     * 添加
     * @param r
     * @return
     */
    public int insert(${property} r) {
        String sql = "insert into ${name} values (${join(columnList?size)})";
        if(r == null){
            return -1;
        }
        return DbUtil.executeUpdate(sql, ps -> {
            try {
            <#list columnList as column>
                <#assign columnType = getType(column.type)>
                <#if columnType == "Timestamp">
                Date ${column.alias} = r.get${column.property}();
                ps.set${columnType}(${column_index + 1}, new Timestamp(${column.alias} != null ? ${column.alias}.getTime() : System.currentTimeMillis()));
                <#elseif columnType == "String">
                ps.set${columnType}(${column_index + 1}, r.get${column.property}());
                <#else>
                ps.set${(column.nullable==0&&column.primary==false)?string(columnType, "Object")}(${column_index + 1}, r.get${column.property}());
                </#if>
            </#list>
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 删除
     * @param r
     * @return
     */
    public int delete(${property} r){
        String joinSql = "";
        if(r != null){
            joinSql += joinSql(r, " and ", "", false);
        }
        int i = -1;
        if(!"".equals(joinSql)){
            String sql = "delete from ${name} where 1=1" + joinSql;
            return DbUtil.executeUpdate(sql, ps -> {});
        }
        return i;
    }

    /**
     * 列表查询
     * @param r
     * @return
     */
    public List<${property}> list(${property} r) {
        String sql = "select * from ${name} where 1 = 1 ";
        String joinSql = "";
        if(r != null){
            joinSql += joinSql(r, " and ", "", true);
        }
        sql += joinSql;
        return DbUtil.executeQuery(sql, ps -> {}, ${property}.class);
    }

    /**
    * 分页列表查询
    * @param pageNumber
    * @param lineNumber
    * @param r
    * @return
    */
    public Page<${property}> pageList(${property} r, int pageNumber, int lineNumber) {
        String sql = "select * from ${name} where 1 = 1 ";
        String joinSql = "";
        if(r != null){
            joinSql += joinSql(r, " and ", "", true);
        }
        sql += joinSql;
        return DbUtil.pageQuery(sql, ps -> {}, ${property}.class, pageNumber, lineNumber);
    }

    <#if primary??>
    /**
     * 更新
     * @param r
     * @return
     */
    public int update(${property} r) {
        String sql = "update ${name} ";
        if(r == null){
            return -1;
        }
        String setSql = "set " + joinSql(r, "", ", ", false);
        ${primary.classType} id = r.get${primary.property}();
        if(id != null && setSql.contains(", ")){
            sql += setSql.substring(0, setSql.length() - 2);
            sql += " where ${primary.name} = ?";
            return DbUtil.executeUpdate(sql, ps -> {
                try {
                    ps.set${getType(primary.type)}(1, id);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }
        return -1;
    }
    </#if>

    /**
     * 拼接sql
     * @param r
     * @param prefix
     * @param suffix
     * @param isQuery
     * @return
     */
    public String joinSql(${property} r, String prefix, String suffix, boolean isQuery){
        String joinSql = "";
        <#list columnList as column>
        ${column.classType} ${column.alias} = r.get${column.property}();
        <#assign columnType = getType(column.type)>
        <#if columnType == "String">
        if(isQuery && ${column.alias} != null && !"".equals(${column.alias})){
            joinSql += prefix + "${column.alias} like concat('%', " + ${column.alias} + ", '%')" +suffix;
        }else if(!isQuery && ${column.alias} != null){
            joinSql += prefix + "${column.alias} = " + ${column.alias} + suffix;
        }
        <#else>
        if(${column.alias} != null){
            joinSql += prefix + "${column.alias} = " + ${column.alias} + suffix;
        }
        </#if>
        </#list>
        return joinSql;
    }
}

<#function join size>
    <#local b = "">
    <#list 1..size as num>
        <#if num == size>
            <#local b += "?">
        <#else>
            <#local b += "?, ">
        </#if>
    </#list>
    <#return b>
</#function>

<#function getType columnType>
    <#local b = "">
    <#if columnType == "VARCHAR">
        <#local b += "String"/>
    <#elseif columnType == "INT">
        <#local b += "Int"/>
    <#elseif columnType == "TINYINT">
        <#local b += "Int"/>
    <#elseif columnType == "BIGINT">
        <#local b += "Long"/>
    <#elseif columnType == "TIMESTAMP">
        <#local b += "Timestamp"/>
    <#elseif columnType == "DATETIME">
        <#local b += "Timestamp"/>
    <#elseif columnType == "TEXT">
        <#local b += "String"/>
    <#elseif columnType == "DOUBLE">
        <#local b += "Double"/>
    <#elseif columnType == "DATE">
        <#local b += "Date"/>
    </#if>
    <#return b>
</#function>

