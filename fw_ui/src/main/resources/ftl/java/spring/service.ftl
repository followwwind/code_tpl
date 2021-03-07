<#assign bootName = param.bootName!"com.wind"/>
<#assign author = param.author!"wind"/>
<#assign isPojo = param.isPojo!true/>
package ${bootName + ".service"};

<#if importList??>
    <#list importList as import>
import ${import};
    </#list>
</#if>
import java.util.List;
<#if isPojo>
import ${bootName + ".entity.po." + property};
import ${bootName + ".entity.vo." + property + "VO"};
import ${bootName + ".entity.dto." + property + "DTO"};
import ${bootName + ".entity.dto." + property + "SearchDTO"};
<#else>
import ${bootName + ".entity." + property};
</#if>

/**
 * ${(remarks=='')?string(name, remarks)} service
 * @author ${author}
 * @date ${.now?string("yyyy/MM/dd HH:mm:ss")}
 * @version V1.0
 */
public interface ${property}Service{

    /**
     * 添加
     * @param r
     * @return
     */
    int save(${property}${isPojo?string('DTO','')} r);

    <#if primary??>
    /**
     * 根据id删除
     * @param id
     * @return
     */
    int delete(${primary.classType} id);

    /**
     * 单条记录查询
     * @param id
     * @return
     */
    ${property}${isPojo?string('VO','')} get(${primary.classType} id);

    /**
     * 修改
     * @param r
     * @return
     */
    int update(${property}${isPojo?string('DTO','')} r);
    </#if>

    /**
     * 批量查询记录
     * @param r
     * @return
     */
    List<${property}${isPojo?string('VO','')}> list(${property}${isPojo?string('SearchDTO','')} r);
}