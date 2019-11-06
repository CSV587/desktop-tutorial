package com.hy.iom.tag.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hy.iom.common.page.PageResult;
import com.hy.iom.common.page.TableHead;
import com.hy.iom.mapper.oracle.TagMapper;
import com.hy.iom.tag.entity.RecordInfoTag;
import com.hy.iom.tag.entity.Tag;
import com.hy.iom.tag.service.TagService;
import com.hy.iom.tag.utils.TagUtils;

/**
 * @author yuzhiping
 * @date 2019年1月28日 
 * Description：
 *		 标签类控制器
 */
@RestController
@RequestMapping("/tagController")
public class TagController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private TagService tagService;
	private TagMapper tagMapper;

	@Autowired
	public TagController(TagService tagService,TagMapper tagMapper) {
		this.tagService = tagService;
		this.tagMapper = tagMapper;
	}
	
	/**
	 * 查询标签
	 *
	 * @param current
	 * @param pageSize
	 * @return
	 *
	 */
	@RequestMapping(value = "/getAllTags",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public PageResult<?> getAllTags(@RequestBody Tag tag) {
		logger.info("TagName = " + tag.getName());
		List<Tag> tags =  tagService.getAllTags(tag.getName());
        List<TableHead> tableHeads = new ArrayList<>();
        tableHeads.add(new TableHead("id", "标签id"));
        tableHeads.add(new TableHead("name", "标签名称"));
        return PageResult.success("success", tags, tableHeads,tags.size());
	}
	
	/**
	 * 添加标签
	 *
	 * @param name
	 * @return
	 *
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "addTag",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public TagUtils addTag(@RequestBody Tag tag) {
		logger.info("TagName = " + tag.getName());
		//如果改标签已经存在，则不添加
		List<Tag> resultTags = tagService.selectTagByName(tag.getName());
		if(resultTags.size() > 0 ) {
			return TagUtils.getFailureResult("该标签已经存在");
		}
		int result = tagService.addTag(tag.getName()); ;
		if(result == 0) {
			return TagUtils.getFailureResult("操作失败");
		}
		return TagUtils.getSuccessResult("操作成功");
	}
	
	/**
	 * 删除标签
	 *
	 * @param id
	 * @return
	 *
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="deleteTag",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public TagUtils deleteTag(@RequestBody Tag tag) {
		logger.info("id = " + tag.getId());
		//查询正在被使用的所有的标签
		boolean tagsFlag = tagService.getAllRecordInfoTags(tag.getId());
		if(tagsFlag) {
			return TagUtils.getFailureResult("标签正在使用");
		}
		int result = tagService.deleteTag(tag.getId());
		if(result == 0) {
			return TagUtils.getFailureResult("操作失败");
		}
		return TagUtils.getSuccessResult("操作成功");
	}
	
	/**
	 * 通过uuid来查询tag名称
	 *
	 * @param recordInfotag
	 * @return
	 *
	 */
	@RequestMapping(value = "/getTagsById",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public PageResult<?> getTagsById(@RequestBody RecordInfoTag recordInfotag) {
		logger.info("uuid = " + recordInfotag.getUuid());
		List<RecordInfoTag> recordInfoTags = tagService.SelectTagByUUID(recordInfotag.getUuid());
		for(RecordInfoTag recordInfoTag : recordInfoTags) {
			String tagName = getTagNames(recordInfoTag.getTagId());
			recordInfoTag.setTagName(tagName);
		}
		List<TableHead> tableHeads = new ArrayList<>();
		tableHeads.add(new TableHead("id", "标签关联表ID"));
		tableHeads.add(new TableHead("uuid", "呼叫列表ID"));
        tableHeads.add(new TableHead("tagId", "标签ID"));
        tableHeads.add(new TableHead("tagName", "标签名称"));
        return PageResult.success("success", recordInfoTags, tableHeads,recordInfoTags.size());
	}
	
	/**
	 * 修改标签
	 *
	 * @param id
	 * @param name
	 * @return
	 *
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="updateTag",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public TagUtils updateTag(@RequestBody Tag tag) {
		logger.info("id = " + tag.getId() + ";name = " + tag.getName() );
		List<Tag> tags = tagService.selectTagById(tag.getId());
		int result = 0;
		if(tags != null  && tags.size() > 0) {
			//先判定标签是否存在
			Tag stag = tags.get(0);
			if(tag.getName().equals(stag.getName())) {
				//保存同名标签
				result = tagService.updateTag(tag.getId(),tag.getName());
			}else {
				//修改标签
				List<Tag> resultTags = tagService.selectTagByName(tag.getName());
				if(resultTags.size() > 0 ) {
					return TagUtils.getFailureResult("该标签已经存在");
				}
				result = tagService.updateTag(tag.getId(),tag.getName());
			}
		}else {
			//该标签不存在
			return TagUtils.getFailureResult("该标签已经被删除");
		}
		if(result == 0) {
			return TagUtils.getFailureResult("操作失败");
		}
		return TagUtils.getSuccessResult("操作成功");
	}
	
	/**
	 * 通过UUID来修改标签结果
	 *
	 * @param uuid   		指的是recordinfo表中的id
	 * @param tagId
	 * @param tagName
	 * @return
	 *
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="updateTagByUUID",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public TagUtils updateTagByUUID(@RequestBody Map<String,String[]> map) {
		RecordInfoTag recordInfoTag = new RecordInfoTag();
		//前台用的是数组传递uuid和tagId
		String[] uuids = map.get("uuid");
		String[] tagIds = map.get("tagId");
		String uuid = StringUtils.join(uuids, ",");
		String tagId = "";
		if(tagIds != null) {
			tagId = StringUtils.join(tagIds, ",");
		}	
		logger.info("uuid = " + uuid + "; tagId = " + tagId);
		List<RecordInfoTag> recordInfoTags = tagService.SelectTagByUUID(uuid);
		int result = 0;
		if(recordInfoTags.size() > 0) {
			//存在就update
			recordInfoTag.setUuid(uuid);
			recordInfoTag.setTagId(tagId);
			result = tagService.updateTagByUUID(recordInfoTag);
		}else {
			//不存在就insert
			result = tagService.addTagByUUID(uuid,tagId);
		}
		if(result == 0) {
			return TagUtils.getFailureResult("操作失败");
		}
		return TagUtils.getSuccessResult("操作成功");
	}
	
	public String getTagNames(String tagId) {
		String tagName = "";
		if(tagId != null) {
			String[] tagIds = tagId.split(",");
	    	for(int i = 0 ; i < tagIds.length ; i++) {
	    		try {
	    			String name = tagMapper.getTagNameById(tagIds[i]);
	    			if(i == tagIds.length - 1) {
	    				if(name != null) {
	    					tagName += name;
	    				}
	    			}else {
	    				if(name != null) {
	    					tagName += name + ",";
	    				}
	    			}
	    		}catch (Exception e) {
					logger.info("SQL-通过id查询标签失败！" + e);
				}
	    	}
		}
    	return tagName;
    }
}
