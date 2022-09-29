package com.web.basicinfo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.web.basicinfo.entity.OaEnumItem;
import com.web.basicinfo.entity.OaInventoryApply;
import com.web.basicinfo.entity.OaInventoryApplyDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OaInventoryApplyDetailMapper extends BaseMapper<OaInventoryApplyDetail> {

    List<OaInventoryApplyDetail> getListBySync(@Param("main") OaInventoryApplyDetail mainDTO,@Param("database") String database,@Param("izError")Integer izError);

    int updateInvCode(@Param("main") OaInventoryApplyDetail record,@Param("database") String database);

    int updateError(@Param("id") Long id, @Param("error") String error, @Param("database") String database);
}