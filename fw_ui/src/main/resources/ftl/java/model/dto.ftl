<#assign bootName = param.bootName!"com.wind"/>
<#assign author = param.author!"wind"/>
<#assign isExtendPojo = param.isExtendPojo!true/>
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
    <#if field.remarks != "" && field.remarks??>
    /** ${field.remarks}*/
    </#if>
    <#if field.javaType == "java.util.Date">
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="Asia/Shanghai")
    </#if>
    private ${field.classType} ${field.alias};

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