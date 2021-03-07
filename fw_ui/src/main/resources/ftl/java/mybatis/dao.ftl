<#assign bootName = param.bootName!"com.wind"/>
<#assign author = param.author!"wind"/>
<#assign isPojo = param.isPojo!true/>
package ${bootName + ".dao"};

<#if importList??>
    <#list importList as import>
import ${import};
    </#list>
</#if>
import java.util.List;
import org.apache.ibatis.annotations.Param;
<#if isPojo>
import ${bootName + ".entity.po." + property};
import ${bootName + ".entity.vo." + property + "VO"};
import ${bootName + ".entity.dto." + property + "SearchDTO"};
<#else>
import ${bootName + ".entity." + property};
</#if>

/**
 * ${(remarks=='')?string(name, remarks)} dao
 * @author ${author}
 * @date ${.now?string("yyyy/MM/dd HH:mm:ss")}
 * @version V1.0
 */
@SqlMapper
public interface ${property}Mapper {

    /**
     * 添加
     * @param r
     * @return
     */
    int insert(${property} r);

    <#if primary??>
    /**
     * 根据id删除
     * @param id
     * @return
     */
    int deleteById(@Param("id") ${primary.classType} id);

    /**
     * 单条记录查询
     * @param id
     * @return
     */
    ${property}${isPojo?string('VO','')} findById(@Param("id") ${primary.classType} id);

    /**
     * 修改
     * @param r
     * @return
     */
    int update(${property} r);
    <#else>
    </#if>

    /**
     * 删除
     * @param r
     * @return
     */
    int delete(${property} r);

    /**
     * 列表查询
     * @param r
     * @return
     */
    List<${property}${isPojo?string('VO','')}> list(${property}${isPojo?string('SearchDTO','')} r);

}