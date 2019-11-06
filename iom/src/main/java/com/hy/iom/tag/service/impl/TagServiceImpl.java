package com.hy.iom.tag.service.impl;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hy.iom.mapper.oracle.TagMapper;
import com.hy.iom.tag.entity.RecordInfoTag;
import com.hy.iom.tag.entity.Tag;

import com.hy.iom.tag.service.TagService;

@Service
public class TagServiceImpl implements TagService{
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private TagMapper tagMapper;
	
	@Autowired
	public TagServiceImpl(TagMapper tagMapper) {
		this.tagMapper = tagMapper;
	}

	@Override
	public List<Tag> getAllTags(String parameter) {
		List<Tag> resultTags = null;
		try {
			resultTags = tagMapper.getAllTags(parameter);
		}catch (Exception e) {
			logger.info("SQL-模糊查询标签失败！\n" + e);
		}
		return resultTags;
	}

	@Override
	public int addTag(String parameter) {
		int result = 0 ;
		Tag resultTag = new Tag();
		resultTag.setId(UUID.randomUUID().toString());
		resultTag.setName(parameter);;
		try {
			result = tagMapper.addTag(resultTag);
		}catch (Exception e) {
			logger.info("SQL-插入标签失败！\n" + e);
		}
		return result;
	}

	@Override
	public int deleteTag(String id) {
		int result = 0 ;
		try {
			result = tagMapper.deleteTag(id);
		}catch (Exception e) {
			logger.info("SQL-删除标签失败！\n" + e);
		}
		return result;
	}

	@Override
	public int updateTag(String id,String name) {
		Tag tag = new Tag();
		tag.setId(id);
		tag.setName(name);
		int result = 0;
		try {
			result = tagMapper.updateTag(tag);
		}catch (Exception e) {
			logger.info("SQL-修改标签失败！\n" + e);
		}
		return result;
	}

	@Override
	public List<Tag> selectTagByName(String name) {
		List<Tag> resultTags = null;
		try {
			resultTags = tagMapper.selectTagByName(name);
		}catch (Exception e) {
			logger.info("SQL-查询标签失败！\n" + e);
		}
		return resultTags;
	}

	@Override
	public List<RecordInfoTag> SelectTagByUUID(String uuid) {
		List<RecordInfoTag>  recordInfoTags = null;
		try {
			recordInfoTags = tagMapper.SelectTagByUUID(uuid);
		}catch (Exception e) {
			logger.info("SQL-通过UUID查询标签失败！\n" + e);
		}
		return recordInfoTags;
	}

	@Override
	public int updateTagByUUID(RecordInfoTag recordInfoTag) {
		int result = 0 ;
		try {
			result = tagMapper.updateTagByUUID(recordInfoTag);
		}catch (Exception e) {
			logger.info("SQL-通过UUID修改标签失败！\n" + e);
		}
		return result;
	}

	@Override
	public int addTagByUUID(String uuid, String tagId) {
		RecordInfoTag recordInfoTag = new RecordInfoTag();
		recordInfoTag.setId(UUID.randomUUID().toString());
		recordInfoTag.setTagId(tagId);
		recordInfoTag.setUuid(uuid);
		int result = 0;
		try {
			result = tagMapper.addTagByUUID(recordInfoTag);
		}catch (Exception e) {
			logger.info("SQL-通过UUID添加标签失败！\n" + e);
		}
		return result;
	}

	@Override
	public List<Tag> selectTagById(String id) {
		List<Tag> tags = null;
		try {
			tags = tagMapper.selectTagById(id);
		}catch (Exception e) {
			logger.info("SQL-通过ID查询标签失败！\n" + e);
		}
		return tags;
	}

	@Override
	public boolean getAllRecordInfoTags(String id) {
		boolean tagsFlag = false;
		List<Map<String, Object>> recordInfoTags = tagMapper.getAllRecordInfoTags(id);
		if(recordInfoTags != null && recordInfoTags.size() > 0) {
			tagsFlag = true;
		}
		return tagsFlag;
	}
	

}
