package help.lixin.sharding.resource.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

//@Mapper
public interface UserMapper {
	/**
	* 根据id列表查询多个用户
	* @param userIds 用户id列表 * @return
	*/
//	@Select({"<script>", " select",
//	" * ",
//	" from t_user t ",
//	" where t.user_id in",
//	"<foreach collection='userIds' item='id' open='(' separator=',' close=')'>", "#{id}",
//	"</foreach>",
//	"</script>"
//	})
	List<Map> selectUserbyIds(@Param("userIds")List<Long> userIds);
}
