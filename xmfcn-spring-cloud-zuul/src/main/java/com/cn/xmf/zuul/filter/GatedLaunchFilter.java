/**
package com.cn.xmf.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import io.jmnarloch.spring.cloud.ribbon.support.RibbonFilterContextHolder;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

*/
/**
 * 金丝雀发布过滤器（灰度发布）
 *
 *//**

public class GatedLaunchFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 4;
    }

    @Override
    public boolean shouldFilter() {
        // 根据之前的过滤器结果处理，若前一个过滤器失败，则该过滤器直接跳过
        RequestContext requestContext = RequestContext.getCurrentContext();
        Boolean isSuccess = (Boolean) requestContext.get("isSuccess");
        if (isSuccess != null && !isSuccess) {
            return false;
        }
        return true;
    }

    @Override
    public Object run() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        String accessToken = requestContext.getRequest().getParameter("token");
        // 灰度发布示例
        RibbonFilterContextHolder.clearCurrentContext();
        // 路由规则判断示例
        if (accessToken.equals("1234567890")) {
            RibbonFilterContextHolder.getCurrentContext().add("gated-launch", "true");
        } else {
            RibbonFilterContextHolder.getCurrentContext().add("gated-launch", "false");
        }
        requestContext.set("isSuccess", true);// 设值，让下一个Filter看到上一个Filter的状态
        return null;
    }
}
*/
