package com.cn.xmf.model.wx;
import com.cn.xmf.base.model.BaseEntitys;
import org.apache.commons.lang3.builder.ToStringBuilder;
 /**
 * 微信音乐Entity
 * @author rufei.cn
 * @version 2019-10-21
 */
public class Music extends BaseEntitys {
	
	private static final long serialVersionUID = 1L;
	/**
	*   类型
	*/
	private String type;
			
	/**
	*   音乐名称
	*/
	private String title;
			
	/**
	*   音乐地址
	*/
	private String url;
			
	
	
	public Music() {
		
	}


			
         public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
			
         public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
			
         public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
		 .append("type",  type)
		 .append("title",  title)
		 .append("url",  url)
		.toString();
	}
}