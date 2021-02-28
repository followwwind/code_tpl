<#assign bootName = param.bootName!"com.wind"/>
<#assign author = param.author!"wind"/>
<#assign isExtendPojo = param.isExtendPojo!true/>
package ${bootName + ".entity.po"};

<#if importList??>
    <#list importList as import>
import ${import};
    </#list>
</#if>

/**
 * ${(remarks=='')?string(name, remarks)} model
 * @author ${author}
 * @date ${.now?string("yyyy/MM/dd HH:mm:ss")}
 * @version V1.0
 */
public class ${property}${isExtendPojo?string(' extends BasePO', '')} {

<#if columnList??>
    <#list columnList as field>
    <#if field.remarks != "" && field.remarks??>
    /** ${field.remarks}*/
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