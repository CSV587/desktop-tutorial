package com.hy.iom.tag.service;

import java.util.List;

import com.hy.iom.tag.entity.RecordInfoTag;
import com.hy.iom.tag.entity.Tag;

public interface TagService {

	List<Tag> getAllTags(String name);

	int addTag(String name);

	int deleteTag(String uuid);

	List<Tag> selectTagByName(String name);

	List<RecordInfoTag> SelectTagByUUID(String uuid);

	int updateTagByUUID(RecordInfoTag recordInfoTag);
	
	int addTagByUUID(String uuid, String tagId);

	int updateTag(String uuid,String name);

	List<Tag> selectTagById(String id);

	boolean getAllRecordInfoTags(String id);

}
