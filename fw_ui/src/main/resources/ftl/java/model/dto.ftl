<#assign bootName = param.bootName!"com.wind"/>
<#assign author = param.author!"wind"/>
<#assign isExtendPojo = param.isExtendPojo!true/>
<#assign isFluentValid = param.isFluentValid!true/>
package ${bootName + ".entity.dto"};

<#if importList??>
    <#list importList as import>
import ${import};
    </#list>
</#if>
<#if importList?? && importList?seq_contains("java.util.Date")>
import com.fasterxml.jackson.annotation.JsonFormat;
</#if>

/**
 * ${(remarks=='')?string(name, remarks)} model
 * @author ${author}
 * @date ${.now?string("yyyy/MM/dd HH:mm:ss")}
 * @version V1.0
 */
public class ${property}DTO${isExtendPojo?string(' extends BaseDTO', '')} {

<#if columnList??>
    <#list columnList as field>
    <#if field.javaType != "java.util.Date" || !(['createTime', 'updateTime']?seq_contains(field.alias))>
    <#if field.remarks != "" && field.remarks??>
    /** ${field.remarks}*/
    </#if>
    <#if isFluentValid>
        <#if field.javaType == "java.lang.String">
    @NotBlank(message = "${field.alias}不能为空", groups = {Add.class})
        <#elseif field.primary == true>
    @NotNull(message = "${field.alias}不能为空", groups = Update.class)
        <#else>
    @NotNull(message = "${field.alias}不能为空", groups = {Add.class})
        </#if>
    </#if>
    <#if field.javaType == "java.util.Date">
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="Asia/Shanghai")
    </#if>
    private ${field.classType} ${field.alias};
    </#if>

    </#list>
</#if>
<#if columnList??>
    <#list columnList as field>
    <#if field_index != 0>

    </#if>
    public void set${field.property}(${field.classType} ${field.alias}){
        this.${field.alias} = ${field.alias};
    }

    public ${field.classType} get${field.property}(){
        return this.${field.alias};
    }
    </#list>
</#if>
}