package com.github.misterchangray.controller.user;

import com.github.misterchangray.common.NormalResponse;
import com.github.misterchangray.common.PageInfo;
import com.github.misterchangray.common.annotation.Authentication;
import com.github.misterchangray.common.annotation.OperationLog;
import com.github.misterchangray.dao.entity.User;
import com.github.misterchangray.service.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户管理控制器
 *
 * 提供用户管理以下用能
 * -根据ID获取指定用户
 * -用户列表
 * -用户新增
 * -用户删除
 * -用户启用/停用
 * -用户编辑
 * -用户角色编辑
 * -检查用户信息是否存在
 *
 * @author Rui.Zhang/misterchangray@hotmail.com
 * @author Created on  3/23/2018.
 */
@Api(tags ="用户管理", description = "UserController")
@Controller
@RequestMapping("/v1/user")
public class UserController {
    @Autowired
    UserService userService;



    /**
     * 检查用户信息是否存在
     * @param username 用户名
     * @param phone 手机号
     * @param idcard 身份证
     * @param email 邮箱
     * @return
     */
    @ApiOperation(value = "用户信息校验", notes = "检查用户信息是否已经注册,true表示已经注册,false为未注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name="username", value = "用户名", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name="phone", value = "手机号", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name="idcard", value = "身份证", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name="email", value = "邮箱", required = false, paramType = "query", dataType = "string"),
    })
    @Authentication
    @RequestMapping(value = "/checkUserInfo", method = RequestMethod.POST)
    @ResponseBody
    public NormalResponse checkUser(@RequestParam(required = false) String username,
                                    @RequestParam(required = false) String phone,
                                    @RequestParam(required = false) String idcard,
                                    @RequestParam(required = false) String email) {

        return userService.checkUserInfo(username, email, phone, idcard);
    }

    /**
     * 编辑用户角色
     * @param userId 用户id
     * @param roleIds 角色Id集合
     * @return
     */
    @ApiOperation(value = "编辑用户角色", notes = "编辑用户下的角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userId", value = "用户ID", required = true, paramType = "path", dataType = "int"),
            @ApiImplicitParam(name="roleIds", value = "角色ID", required = true, paramType = "query", dataType = "int", allowMultiple = true),
    })
    @Authentication
    @RequestMapping(value="/{userId}/role", method = RequestMethod.PATCH)
    @ResponseBody
    public NormalResponse editUserRole(@PathVariable Integer userId, @RequestParam List<Integer> roleIds) {
        return userService.updateRole(userId, roleIds);
    }

    /**
     * 根据ID获取用户
     * @param userId
     * @return
     */
    @ApiOperation(value = "根据ID获取用户", notes = "根据ID获取用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userId", value = "用户ID", required = true, paramType = "path", dataType = "int"),
    })
    @Authentication
    @RequestMapping(value="/{userId}", method = RequestMethod.GET)
    @ResponseBody
    public NormalResponse getById(@PathVariable Integer userId) {
        return userService.getById(userId);
    }


    /**
     * 用户列表
     * @param limit
     * @return
     */
    @ApiOperation(value = "用户列表", notes = "获取用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="page", value = "页码", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name="limit", value = "每页条数", required = true, paramType = "query", dataType = "int"),
    })
    @Authentication
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public NormalResponse list(@RequestParam(required =  false) Integer page, @RequestParam(required = false) Integer limit) {
        User user = new User();

        return userService.list(user, PageInfo.newInstance(page, limit));
    }

    /**
     * 新增用户
     * @param user
     * @return
     */
    @ApiOperation(value = "新增用户", notes = "新增用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name="user", value = "用户实体JSON对象", required = true, paramType = "body", dataType = "com.github.misterchangray.dao.entity.User"),
    })
    @Authentication
    @OperationLog(businessName = "新增用户")
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public NormalResponse add(@RequestBody User user) {
        return userService.insert(user);
    }


    /**
     * 删除用户
     * @param id
     * @return
     */
    @ApiOperation(value = "删除用户", notes = "删除用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id", value = "根据ID删除用户", required = true, paramType = "path", dataType = "int"),
    })
    @Authentication
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public NormalResponse delete(@PathVariable(value = "id") Integer id) {
        User user = new User();
        user.setId(id);
        return userService.delete(user);
    }


    /**
     * 编辑用户
     * @param user
     * @return
     */
    @ApiOperation(value = "编辑用户", notes = "编辑用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name="user", value = "用户实体JSON对象", required = true, paramType = "body", dataType = "com.github.misterchangray.dao.entity.User"),
    })
    @Authentication
    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public NormalResponse edit(@RequestBody User user) {
       return userService.update(user);
    }



}
