package com.lord.web.controller.mis;

import com.lord.common.dto.Pager;
import com.lord.common.dto.QueryParams;
import com.lord.common.model.mis.MisUser;
import com.lord.common.service.mis.MisUserService;
import com.lord.utils.Preconditions;
import com.lord.utils.dto.Result;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 功能：后台用户mis_user的Api
 *
 * @author xiaocheng
 * @version 1.0
 * @Date 2017年05月09日 16:43:46
 */
@RestController
@Api(description = "后台用户API")
public class MisUserController {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MisUserService misUserService;

    @ApiOperation(value = "查询后台用户的列表")
    @RequestMapping(value = "/api/admin/mis/misUser/list", method = {RequestMethod.GET, RequestMethod.POST})
    public Result list(@ModelAttribute QueryParams queryParams) {
        MisUser param = new MisUser();
        if (queryParams != null) {
            //TODO:待修改
            param.setId(queryParams.getLongId());
        }
        Pager<MisUser> pager = misUserService.pageMisUser(param, queryParams.getPage(), queryParams.getPageSize());
        return Result.success("查询成功", pager);
    }

    @ApiOperation(value = "保存或更新后台用户")
    @RequestMapping(value = "/api/admin/mis/misUser/saveOrUpdate", method = RequestMethod.POST)
    public Result saveOrUpdate(@ModelAttribute MisUser pageObj) {
        MisUser dbObj = misUserService.saveOrUpdate(pageObj);
        return Result.success("保存成功", dbObj);
    }

    @ApiOperation(value="删除后台用户", notes="根据主键id，删除后台用户")
    @ApiImplicitParam(name = "ids", value = "主键Id", required = true, dataType = "Long", paramType = "query")
    @RequestMapping(value = "/api/admin/mis/misUser/remove", method = RequestMethod.GET)
    public Result remove(Long[] ids) {
        Preconditions.checkNotNull(ids, "ids不能为空");
        //misUserService.removeMisUser(ids);//逻辑删除
        misUserService.deleteMisUser(ids);//物理删除
        return Result.success("删除成功");
    }

    @ApiOperation(value="获取后台用户", notes="根据主键id，获取后台用户")
    @ApiImplicitParam(name = "id", value = "主键Id", required = true, dataType = "Long", paramType = "path")
    @RequestMapping(value = "/api/admin/mis/misUser/{id}", method = RequestMethod.GET)
    public Result get(@PathVariable Long id) {
        Preconditions.checkNotNull(id, "id不能为空");
        MisUser dbObj = misUserService.getMisUser(id);
        return Result.success("获取成功", dbObj);
    }

    @ApiOperation(value="更新后台用户的排序值", notes="根据主键id，更新后台用户的排序值")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键Id", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "orderValue", value = "排序值", required = true, dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/api/admin/mis/misUser/updateOrderValue", method = RequestMethod.GET)
    public Result updateOrderValue(Long id, Long orderValue)
    {
        Preconditions.checkNotNull(id, "id不能为空");
        Preconditions.checkNotNull(orderValue, "排序值orderValue不能为空");
        misUserService.updateOrderValue(id, orderValue);
        return Result.success("更新成功");
    }

    @ApiOperation(value="修改用户的密码", notes="根据主键id，修改用户的密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键Id", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "新密码", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/api/admin/mis/misUser/updatePassword", method = RequestMethod.GET)
    public Result updatePassword(Long id, String password)
    {
        Preconditions.checkNotNull(id, "id不能为空");
        Preconditions.checkArgument(StringUtils.isEmpty(password), "新密码不能为空");
        misUserService.updatePassword(id, password);
        return Result.success("修改用户的密码成功");
    }

    @ApiOperation(value = "判断记录是否存在", notes = "判断记录是否存在")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键Id", dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "rowName", value = "属性名", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "rowValue", value = "属性值", dataType = "String", paramType = "query")})
    @RequestMapping(value = "/api/admin/mis/misUser/isExist", method = RequestMethod.GET)
    public Result isExist(Long id, String rowName, String rowValue) {
        Preconditions.checkNotNull(rowName, "rowName不能为空");
        Preconditions.checkNotNull(rowValue, "rowValue不能为空");
        boolean isRepeat = misUserService.isExist(id, rowName, rowValue);
        if (isRepeat) {
            return Result.success("已经存在相同的记录", isRepeat);
        }
        return Result.failure("不存在相同的记录", isRepeat);
    }
}
