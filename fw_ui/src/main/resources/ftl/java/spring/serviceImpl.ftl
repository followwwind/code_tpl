<#assign bootName = param.bootName!"com.wind"/>
<#assign author = param.author!"wind"/>
<#assign isPojo = param.isPojo!true/>
<#assign isFluentValid = param.isFluentValid!true/>
package ${bootName + ".service.impl"};

<#if importList??>
    <#list importList as import>
import ${import};
    </#list>
</#if>
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import ${bootName + ".util.BeanUtil"};
import ${bootName + ".dao." + property + "Mapper"};
import ${bootName + ".service." + property + "Service"};
<#if isPojo>
import ${bootName + ".entity.po." + property};
import ${bootName + ".entity.vo." + property + "VO"};
import ${bootName + ".entity.dto." + property + "DTO"};
import ${bootName + ".entity.dto." + property + "SearchDTO"};
<#else>
import ${bootName + ".entity." + property};
</#if>
<#if isFluentValid>
import com.baidu.unbiz.fluentvalidator.annotation.FluentValid;
</#if>

/**
 * ${(remarks=='')?string(name, remarks)} service
 * @author ${author}
 * @date ${.now?string("yyyy/MM/dd HH:mm:ss")}
 * @version V1.0
 */
@Service
public class ${property}ServiceImpl implements ${property}Service{

	private Logger logger = LoggerFactory.getLogger(${property}ServiceImpl.class);

    @Autowired
    private ${property}Mapper mapper;

    @Override
    public int save(${isFluentValid?string('@FluentValid(groups = Add.class) ', ' ')}${property}${isPojo?string('DTO','')} r) {
    	logger.info("${property}ServiceImpl.save param: r is {}", r);
        ${property} entity = new ${property}();
        BeanUtil.copy(r, entity);
        return mapper.insert(entity);
    }

    <#if primary??>
    @Override
    public int delete(${primary.classType} id) {
    	logger.info("${property}ServiceImpl.delete param: id is {}", id);
        return mapper.deleteById(id);
    }

    @Override
    public ${property} get(${primary.classType} id) {
    	logger.info("${property}ServiceImpl.get param: id is {}", id);
        return mapper.findById(id);
    }

    @Override
    public int update(${isFluentValid?string('@FluentValid(groups = Update.class) ', ' ')}${property}${isPojo?string('DTO','')} r) {
    	logger.info("${property}ServiceImpl.update param: r is {}", r);
        ${property} entity = new ${property}();
        BeanUtil.copy(r, entity);
        return mapper.update(entity);
    }
    </#if>

    @Override
    public List<${property}${isPojo?string('VO','')}> list(${property}${isPojo?string('SearchDTO','')} r) {
    	logger.info("${property}ServiceImpl.list param: r is {}", r);
        return mapper.list(r);
    }
}
