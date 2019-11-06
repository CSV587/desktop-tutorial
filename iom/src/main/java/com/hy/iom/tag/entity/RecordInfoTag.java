package com.hy.iom.tag.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecordInfoTag {

	private String id ;				//标签关联表id
	private String uuid;			//关联表t_iom_recordinfo中的id属性
	private String tagId;			//关联表t_iom_tag中的id
	private String tagName;	
	@Override
	public String toString() {
		return "RecordInfoTag [id=" + id + ", uuid=" + uuid + ", tagId=" + tagId + "]";
	}
	
}
