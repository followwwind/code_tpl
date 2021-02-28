<#assign bootName = param.bootName!"com.wind"/>
<#assign author = param.author!"wind"/>
<#assign isSwagger = param.isSwagger!false/>
<#assign isRest = param.isRest!true/>
<#assign isPojo = param.isPojo!true/>
package ${bootName + ".controller"};

<#if importList??>
    <#list importList as import>
import ${import};
    </#list>
</#if>
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.github.pagehelper.PageHelper;
import java.util.List;
import ${bootName + ".service." + property + "Service"};
<#if isPojo>
import ${bootName + ".entity.po." + property};
import ${bootName + ".entity.vo." + property + "VO"};
import ${bootName + ".entity.dto." + property + "DTO"};
import ${bootName + ".entity.dto." + property + "SearchDTO"};
<#else>
import ${bootName + ".entity." + property};
</#if>
<#if isSwagger>import io.swagger.annotations.Api;${"\n"}</#if><#t>
<#if isSwagger>import io.swagger.annotations.ApiOperation;${"\n"}</#if><#t>

/**
 * ${(remarks=='')?string(name, remarks)} controller
 * @author ${author}
 * @date ${.now?string("yyyy/MM/dd HH:mm:ss")}
 * @version V1.0
 */
@RestController
@RequestMapping(value = "api/${property?uncap_first}")
<#if isSwagger>@Api(value="${property?uncap_first}")${"\n"}</#if><#t>
public class ${property}Controller{
    <#assign service = property?uncap_first + "Service">
	
	private final Logger logger = LoggerFactory.getLogger(${property}Controller.class);
	
    @Autowired
    private ${property}Service ${service};

    /**
     * 添加记录接口
     * @param r
     * @return
     */
    <#if isRest>
    @PostMapping("/")
    <#else>
    @PostMapping("/save")
    </#if>
    <#if isSwagger>${"\t"}@ApiOperation(value="${property} 添加记录接口", notes="${property} 添加记录接口")")${"\n"}</#if><#t>
    public JsonResult save(@RequestBody ${property}${isPojo?string('DTO','')} r) {
        logger.info("${property}Controller.save param: r is {}", r);
        int i = ${service}.save(r);
        return new JsonResult(i > 0 ? HttpCode.OK : HttpCode.FAIL);
    }

    <#if primary??>
    /**
     * 删除记录接口
     * @param id
     * @return
     */
    <#if isRest>
    @DeleteMapping("/{id}")
    <#else>
    @GetMapping("/delete/{id}")
    </#if>
    <#if isSwagger>${"\t"}@ApiOperation(value="${property} 删除记录接口", notes="${property} 删除记录接口")${"\n"}</#if><#t>
    public JsonResult delete(@PathVariable("id") ${primary.classType} id) {
        logger.info("${property}Controller.delete param: id is {}", id);
        int i = ${service}.delete(id);
        return new JsonResult(i > 0 ? HttpCode.OK : HttpCode.FAIL);
    }

    /**
     * 单条记录查询接口
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    <#if isSwagger>${"\t"}@ApiOperation(value="${property} 单条记录查询接口", notes="${property} 单条记录查询接口")${"\n"}</#if><#t>
    public JsonResult get(@PathVariable("id") ${primary.classType} id) {
        logger.info("${property}Controller.get param: id is {}", id);
        ${property} entity = ${service}.get(id);
        return new JsonResult(HttpCode.OK, entity);
    }
    </#if>
    /**
     * 修改记录接口
     * @param r
     * @return
     */
    <#if isRest>
    @PutMapping("/")
    <#else>
    @PostMapping("/update")
    </#if>
    <#if isSwagger>${"\t"}@ApiOperation(value="${property} 修改记录接口", notes="${property} 修改记录接口")${"\n"}</#if><#t>
    public JsonResult update(@RequestBody ${property}${isPojo?string('DTO','')} r) {
        logger.info("${property}Controller.update param: r is {}", r);
        int i = ${service}.update(r);
        return new JsonResult(i > 0 ? HttpCode.OK : HttpCode.FAIL);
    }

    /**
     * 批量查询记录接口
     * @param r
     * @return
     */
    @PostMapping("/list")
    <#if isSwagger>${"\t"}@ApiOperation(value="${property} 批量查询记录接口", notes="${property} 批量查询记录接口")${"\n"}</#if><#t>
    public JsonResult list(@RequestBody ${property}${isPojo?string('SearchDTO','')} r) {
        logger.info("${property}Controller.list param: r is {}", r);
        List<${property}${isPojo?string('VO','')}> list = ${service}.list(r);
        return new JsonResult(HttpCode.OK, list);
    }

    /**
     * 分页查询记录接口
     * @param r
     * @return
     */
    @PostMapping("/page/list")
    <#if isSwagger>${"\t"}@ApiOperation(value="${property} 分页查询记录接口", notes="${property} 分页查询记录接口")${"\n"}</#if><#t>
    public JsonResult pageList(@RequestBody ${property}${isPojo?string('SearchDTO','')} r){
        logger.info("${property}Controller.pageList param: r is {}", r);
        PageHelper.startPage(r.getPageNumber(), r.getLineNumber());
        List<${property}${isPojo?string('VO','')}> list = ${service}.list(r);
        Page<${property}${isPojo?string('VO','')}> page = new Page<>(list);
        return new JsonResult(HttpCode.OK, page);
    }
}