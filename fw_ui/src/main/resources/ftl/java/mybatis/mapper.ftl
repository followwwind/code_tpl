<#assign lBracket = "{"/>
<#assign dlBracket = "${"/>
<#assign bootName = param.bootName!"com.wind"/>
<#assign package = bootName + ".entity"/>
<#assign isPojo = param.isPojo!true/>
<#assign typeName = (package + "." + isPojo?string('po.', '') + property)/>
<#assign listReturn = (package + "." + isPojo?string('vo.', '') + property + isPojo?string('VO', ''))/>
<#assign listParam = (package + "." + isPojo?string('dto.', '') + property + isPojo?string('SearchDTO', ''))/>
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="${bootName + ".dao." + property + "Mapper"}" >
    <resultMap id="BaseResultMap" type="${typeName}" >
    <#list columnList as column>
        <#if column.primary == true>
        <id column="${column.name}" property="${column.alias}" jdbcType="${replace(column.type)}" />
        <#else>
        <result column="${column.name}" property="${column.alias}" jdbcType="${replace(column.type)}" />
        </#if>
        </#list>
    </resultMap>

    <#--<sql id="Base_Column_List" >
        ${join(0, ",", 0)}
    </sql>-->

    <sql id="Column_List" >
        ${join(1, ",", 0)}
    </sql>

    <sql id="Column_Selective_List" >
        ${join(2, ",", 1)}
    </sql>

    <sql id="Column_Selective_And_List" >
        ${join(5, "and", 0)}
    </sql>

    <sql id="Column_Assign_List" >
        ${join(3, ",", 0)}
    </sql>

    <sql id="InsertBatch_List" >
        ${join(4, ",", 0)}
    </sql>

    <insert id="insert" parameterType="${typeName}" <#if primary??>useGeneratedKeys="true" keyProperty="${primary.name}"</#if>>
        insert into ${name} (
        <include refid="Column_List" />
        ) values (
        <include refid="Column_Assign_List" />
        )
    </insert>

    <!--<insert id="insertBatch" >-->
    <!--insert into ${name} (-->
    <!--<include refid="Column_List" />-->
    <!--) values-->
    <!--<foreach collection="list" item="item" separator=",">-->
    <!--(-->
    <!--<include refid="InsertBatch_List" />-->
    <!--)-->
    <!--</foreach>-->
    <!--</insert>-->

    <#if primary??>
    <delete id="deleteById">
        delete from ${name} where ${primary.name!""} = #${lBracket}id,jdbcType=${replace(primary.type)}}
    </delete>

    <select id="findById" ${isPojo?string('resultType="${listReturn}"','resultMap="BaseResultMap"')}>
        select
        ${join(6, ",", 0)}
        from ${name} r
        where r.${primary.name!""} = #${lBracket}id,jdbcType=${replace(primary.type)}}
    </select>

    <update id="update" parameterType="${typeName}" >
        update ${name}
        <set>
            <include refid="Column_Selective_List" />
        </set>
        where ${primary.name!""} = #${lBracket}${primary.alias},jdbcType=${replace(primary.type)}}
    </update>
    <#else>
    </#if>

    <delete id="delete" parameterType="${typeName}" >
        delete from ${name} where 1=1
        <include refid="Column_Selective_And_List" />
    </delete>

    <select id="list" resultType="${listReturn}" parameterType="${listParam}">
        select
        ${join(6, ",", 0)}
        from ${name} r
        where 1 = 1
        ${join(7, "and", 0)}
    </select>
</mapper>

<#function replace columnType>
    <#local b = columnType?replace(" UNSIGNED", "")>
    <#if b == "INT" || b == "MEDIUMINT" || b == "TINYINT">
        <#local b = "INTEGER">
    <#elseif b == "TINYTEXT">
        <#local b = "VARCHAR">
    <#elseif b == "YEAR">
        <#local b = "DATE">
    <#elseif b == "TEXT" || b == "MEDIUMTEXT" || b == "LONGTEXT">
        <#local b = "LONGVARCHAR">
    <#elseif b == "DATETIME">
        <#local b = "TIMESTAMP">
    <#elseif b == "TINYBLOB">
        <#local b = "BINARY">
    <#elseif b == "BLOB" || b == "MEDIUMBLOB" || b == "LONGBLOB">
        <#local b = "LONGVARBINARY">
    </#if>
    <#return b>
</#function>

<#function join type sign flag>
<#-- 声明局部变量 -->
    <#local str = "">
    <#if flag == 1>
        <#local str += "<trim prefix=\"\" suffix=\"\" suffixOverrides=\"" + sign + "\" prefixOverrides=\"" + sign + "\">\n\t\t\t">
    </#if>
    <#list columnList as column>
        <#local s = "">
        <#if type == 0>
            <#local s = column.name>
            <#if column_index < (columnList?size - 1)>
                <#local s += sign>
            </#if>
            <#if column_index != 0 && column_index % 7 == 0>
                <#local s += "\n\t\t\t">
            </#if>
        <#elseif type == 1>
            <#local s = column.name>
            <#if column_index < (columnList?size - 1)>
                <#local s += sign>
            </#if>
            <#if column_index != 0 && column_index % 7 == 0>
                <#local s += "\n\t\t\t">
            </#if>
        <#elseif type == 2>
            <#if column.classType == "String">
                <#local s = "<if test=\"" + column.alias + "!= null and " + column.alias + "!=''\" >\n\t\t\t">
            <#else>
                <#local s = "<if test=\"" + column.alias + "!= null\" >\n\t\t\t">
            </#if>
<#--            <#local s = "<if test=\"" + column.alias + "!= null\" >\n\t\t\t">-->
            <#if flag == 1><#local s += "\t"></#if>
            <#local s += sign + " " + column.name + " = #" + lBracket + column.alias
            + ",jdbcType=" + replace(column.type) +"}\n\t\t">
            <#if flag == 1><#local s += "\t"></#if>
            <#local s += "</if>\n\t\t">
            <#if flag == 1 && column_index < (columnList?size - 1)><#local s += "\t"></#if>
        <#elseif type == 3>
            <#local s += "#" + lBracket + column.alias
            + ",jdbcType=" + replace(column.type) +"}">
            <#if column_index < (columnList?size - 1)>
                <#local s += sign>
            </#if>
            <#if column_index != 0 && column_index % 4 == 0>
                <#local s += "\n\t\t">
            </#if>
        <#elseif type == 4>
            <#local s += "#" + lBracket + "item." + column.alias
            + ",jdbcType=" + replace(column.type) +"}">
            <#if column_index < (columnList?size - 1)>
                <#local s += sign>
            </#if>
            <#if column_index != 0 && column_index % 4 == 0>
                <#local s += "\n\t\t">
            </#if>
        <#elseif type == 5>
            <#if column.classType == "String">
                <#local s = "<if test=\"" + column.alias + "!= null and " + column.alias + "!=''\" >\n\t\t\t">
            <#else>
                <#local s = "<if test=\"" + column.alias + "!= null\" >\n\t\t\t">
            </#if>
            <#if flag == 1><#local s += "\t"></#if>
            <#local s += sign + " " + column.name + " = #" + lBracket + column.alias
            + ",jdbcType=" + replace(column.type) +"}\n\t\t">
            <#if flag == 1><#local s += "\t"></#if>
            <#local s += "</if>\n\t\t">
            <#if flag == 1 && column_index < (columnList?size - 1)><#local s += "\t"></#if>
        <#elseif type == 6>
            <#if column.alias == column.name>
                <#local s = "r." + column.alias>
            <#else>
                <#local s = "r." + column.name + " " +column.alias>
            </#if>
            <#if column_index < (columnList?size - 1)>
                <#local s += sign>
            </#if>
            <#if column_index != 0 && column_index % 7 == 0>
                <#local s += "\n\t\t\t">
            </#if>
        <#elseif type == 7>
            <#if column.classType == "String">
                <#local s = "<if test=\"" + column.alias + "!= null and " + column.alias + "!=''\" >\n\t\t\t">
            <#else>
                <#local s = "<if test=\"" + column.alias + "!= null\" >\n\t\t\t">
            </#if>
            <#if flag == 1><#local s += "\t"></#if>
            <#if column.classType == "String">
                <#local s += sign + " r." + column.name + " like concat('%', #" + lBracket + column.alias
                + ",jdbcType=" + replace(column.type) +"}, '%')\n\t\t">
            <#else>
                <#local s += sign + " r." + column.name + " = #" + lBracket + column.alias
                + ",jdbcType=" + replace(column.type) +"}\n\t\t">
            </#if>
            <#if flag == 1><#local s += "\t"></#if>
            <#local s += "</if>\n\t\t">
            <#if flag == 1 && column_index < (columnList?size - 1)><#local s += "\t"></#if>
        </#if>
        <#local str += s>
    </#list>
    <#if flag == 1>
        <#local str += "</trim>">
    </#if>
    <#return str>
</#function>