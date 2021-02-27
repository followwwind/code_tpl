<#assign bootName = param.bootName!"com.wind"/>
<#assign author = param.author!"wind"/>
package ${bootName + '.util'};

import java.io.Serializable;
import java.util.List;

/**
 * page分页工具类
 * @author ${author}
 * @date ${.now?string("yyyy/MM/dd HH:mm:ss")}
 * @version V1.0
 */
public class Page<T> implements Serializable {
    /**
     * 页码
     */
    private Integer pageNumber = 1;

    /**
     * 页长
     */
    private Integer lineNumber = 10;

    /**
     * 总条数
     */
    private Integer totalCount;

    /**
     * 数据list
     */
    private List<T> data;

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
