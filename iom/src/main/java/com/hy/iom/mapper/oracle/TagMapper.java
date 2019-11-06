package com.hy.iom.mapper.oracle;

import java.util.List;
import java.util.Map;

import com.hy.iom.mapper.IomMapper;
import com.hy.iom.tag.entity.RecordInfoTag;
import com.hy.iom.tag.entity.Tag;

public interface TagMapper extends IomMapper<Tag> {

	List<Tag> getAllTags(String parameter);

	int addTag(Tag resultTag);

	int deleteTag(String id);
	
	int updateTag(Tag tag);

	List<Tag> selectTagByName(String name);

	List<RecordInfoTag> SelectTagByUUID(String uuid);

	int updateTagByUUID(RecordInfoTag recordInfoTag);

	int addTagByUUID(RecordInfoTag recordInfoTag);

	String getTagNameById(String id);

	List<Tag> selectTagById(String id);

	List<Map<String, Object>> getAllRecordInfoTags(String id);


}
