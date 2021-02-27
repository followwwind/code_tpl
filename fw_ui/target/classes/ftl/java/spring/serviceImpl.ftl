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
import com.github.pagehelper.PageHelper;
import java.util.List;
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
    public JsonResult save(${isFluentValid?string('@FluentValid(groups = Add.class) ', ' ')}${property}${isPojo?string('DTO','')} r) {
    	logger.info("${property}ServiceImpl.save param: r is {}", r);
        ${property} entity = new ${property}();
        BeanUtil.copy(r, entity);
        int i = mapper.insert(entity);
        return new JsonResult(i > 0 ? HttpCode.OK : HttpCode.FAIL);
    }

    <#if primary??>
    @Override
    public JsonResult delete(${primary.classType} id) {
    	logger.info("${property}ServiceImpl.delete param: id is {}", id);
        int i = mapper.deleteById(id);
        return new JsonResult(i > 0 ? HttpCode.OK : HttpCode.FAIL);
    }

    @Override
    public JsonResult get(${primary.classType} id) {
    	logger.info("${property}ServiceImpl.get param: id is {}", id);
        ${property} r = mapper.findById(id);
        return new JsonResult(HttpCode.OK, r);
    }

    @Override
    public JsonResult update(${isFluentValid?string('@FluentValid(groups = Update.class) ', ' ')}${property}${isPojo?string('DTO','')} r) {
    	logger.info("${property}ServiceImpl.update param: r is {}", r);
        ${property} entity = new ${property}();
        BeanUtil.copy(r, entity);
        int i = mapper.update(entity);
        return new JsonResult(i > 0 ? HttpCode.OK : HttpCode.FAIL);
    }
    </#if>

    @Override
    public JsonResult list(${property}${isPojo?string('SearchDTO','')} r) {
    	logger.info("${property}ServiceImpl.list param: r is {}", r);
        List<${property}${isPojo?string('VO','')}> list = mapper.list(r);
        return new JsonResult(HttpCode.OK, list);
    }

    @Override
    public JsonResult pageList(${property}${isPojo?string('SearchDTO','')} r){
    	logger.info("${property}ServiceImpl.pageList param: r is {}", r);
        PageHelper.startPage(r.getPageNumber(), r.getLineNumber());
        List<${property}${isPojo?string('VO','')}> list = mapper.list(r);
        Page<${property}${isPojo?string('VO','')}> page = new Page<>(list);
        return new JsonResult(HttpCode.OK, page);
    }
}
