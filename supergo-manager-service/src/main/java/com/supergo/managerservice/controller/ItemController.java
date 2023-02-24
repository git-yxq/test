package com.supergo.managerservice.controller;

import com.supergo.http.HttpResult;
import com.supergo.page.PageResult;
import com.supergo.pojo.Item;
import com.supergo.managerservice.service.ItemService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
//声明该类需要生成文档
@Api(value="商品控制器",protocols = "http")
@RestController
@RequestMapping("/item")
public class ItemController {
    @Autowired
    private ItemService itemService;
    //分页查询
    //参数1: 查询条件
    // @ApiOperation:声明当前接口的功能
    @ApiOperation(value = "带有查询条件商品分页列表查询")
    // 声明接口参数
    @ApiImplicitParams({
            /**
             * name:描述参数名
             * paramType:参数类型
             * value:参数功能描述
             * required:是否为必填参数
             * dataType:数据类型
             */
            @ApiImplicitParam(name = "page",paramType = "path",value = "当前页数",required = true,dataType = "int"),
            @ApiImplicitParam(name = "size",paramType = "path",value = "每页显示条数",required = true,dataType = "int")
    })
    // 请求响应的描述
    @ApiResponses({
            /**
             * code: 响应状态吗
             * message: 状态码描述
             */
        @ApiResponse(code=200,message = "查询商品成功"),
        @ApiResponse(code=500,message = "您操作有误!导致后台发生错误!"),
        @ApiResponse(code=400,message = "请求参数出现错误!")
    })
    @PostMapping("/query/{page}/{size}")
    public HttpResult findPage(@RequestBody(required = false) Item item, @PathVariable int page, @PathVariable int size) {
        System.out.println(item);
        //分页条件查询
        PageResult result = itemService.findPage(page, size, item);
        return HttpResult.ok(result);
    }
}
