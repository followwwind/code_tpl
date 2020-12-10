package ${packageName!"com.wind.dao"};

<#if importList??>
    <#list importList as import>
import ${import};
    </#list>
</#if>
import java.util.List;


/**
 * ${remarks!""} mapper接口
 * @author wind
 */
//@SqlMapper
public interface ${alias}Mapper{

    /**
     * 添加纪录
     * @param r
     * @return
     */
    int save(${alias} r);

    /**
     * 通过id删除记录
     * @param id
     * @return
     */
    int deleteById(${primary.classType} id);

    /**
     * 通过id查询记录
     * @param id
     * @return
     */
    ${alias} findById(${primary.classType} id);

    /**
     * 条件批量查询记录
     * @param r
     * @return
     */
    List<${alias}> findList(R r);

    /**
     * 通过id更新记录
     * @param r
     * @return
     */
    int update(${alias} r);
}