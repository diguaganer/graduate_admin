package cc.niushuai.graduate.controller;


import cc.niushuai.graduate.commons.enumresource.StateEnum;
import cc.niushuai.graduate.commons.exception.MyException;
import cc.niushuai.graduate.commons.utils.*;
import cc.niushuai.graduate.config.log.Log;
import cc.niushuai.graduate.entity.admin.Area;
import cc.niushuai.graduate.service.AreaService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author niushuai
 * @email qqniushuai951101@gmail.com
 * @date 2018-08-11 10:52:35
 */
@Controller
@RequestMapping("area")
public class AreaController {
    @Autowired
    private AreaService areaService;

    @RequestMapping("/list")
    public String list() {
        return "area/list";
    }
    
    /**
     * 列表
     */
    @ResponseBody
    @RequestMapping("/listData")
    @RequiresPermissions("area:list")
    public ResultUtil listData(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        List<Area> areaList = areaService.queryList(query);
        int total = areaService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(areaList, total, query.getLimit(), query.getPage());

        return ResultUtil.ok().put("page", pageUtil);
    }

    /**
     * 获取下级地区
     */
    @ResponseBody
    @RequiresPermissions("area:list")
    @RequestMapping("normalList/{parentAreaId}")
    public ResultUtil normalList(@PathVariable String parentAreaId) {
        List<EnumBean> list = new ArrayList<>();
        HashMap<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("state", StateEnum.ENABLE.getCode());
        paraMap.put("parentAreaId", parentAreaId);
        List<Area> areaList = areaService.getAreaListByIsShow(paraMap);
        if (areaList != null && areaList.size() > 0) {
            for (int i = 0; i < areaList.size(); i++) {
                EnumBean bean = new EnumBean();
                bean.setCode(areaList.get(i).getAreaId());
                bean.setValue(areaList.get(i).getAreaName());
                list.add(bean);
            }
        }
        return ResultUtil.ok().put("data", list);
    }

    /**
     * 跳转到新增页面
     **/
    @RequestMapping("/add")
    @RequiresPermissions("area:save")
    public String add() {
        return "area/add";
    }

    /**
     * 跳转到修改页面
     **/
    @RequestMapping("/edit/{id}")
    @RequiresPermissions("area:update")
    public String edit(HttpServletRequest request, Model model, @PathVariable("id") String id) {
        Area area = areaService.queryObject(id);
        model.addAttribute("model", area);
        return "area/edit";
    }

    /**
     * 信息
     */
    @ResponseBody
    @RequestMapping("/info/{areaId}")
    @RequiresPermissions("area:info")
    public ResultUtil info(@PathVariable("areaId") String areaId) {
        Area area = areaService.queryObject(areaId);

        return ResultUtil.ok().put("area", area);
    }

    /**
     * 保存
     */
    @ResponseBody
    @RequestMapping("/save")
    @RequiresPermissions("area:save")
    public ResultUtil save(@RequestBody Area area) {
        verifyForm(area);
        //随机生成十位字符做为菜单标识
        String area_id = RandomCharUtil.getNumberRand();
        //算出子菜单的等级
        area.setAreaId(area_id);
        //设置拼音
        String pinyin= ChineseToEnglishUtil.getPingYin(area.getAreaName().trim()).trim();
        area.setEnName(pinyin);
        area.setWordIndex(pinyin.substring(0, 1).toUpperCase());
        //设置排序
        area.setSortNo(0);
        //设置 area_level;//lev+1
        area.setLevelArea("0");
        area.setIsCity("0");
        String[] parentIds=area.getParentAreaIds();
        String parentId=parentIds[parentIds.length-1];
        if("".equals(parentId)){
            parentId=parentIds[parentIds.length-2];
        }
        area.setParentAreaId(parentId.substring(parentId.length()-10,parentId.length()));
        //设置行政级别（上级行政级别+1）
        Area parent=areaService.queryObject(parentId.substring(parentId.length()-10,parentId.length()));
        int parent_level=parent.getAreaLevel();
        if (parent_level==-1){
            parent_level=0;
        }
        if (parent_level<4){
            parent_level=parent_level+1;
        }
        area.setAreaLevel(parent_level);
        area.setState(area.getState());
        areaService.save(area);

        return ResultUtil.ok();
    }
    //验证表码
    private void verifyForm(Area area) {
        int count;
        Map<String, Object> params=new HashMap<>();
        params.put("areaName",area.getAreaName());
        count= areaService.getCount(params);
        if(count>0){
            throw new MyException("地区名称已存在");
        }
        params.clear();
        params.put("xzCode",area.getXzCode());
        count= areaService.getCount(params);
        if(count>0){
            throw new MyException("行政编码已存在");
        }

    }

    /**
     * 修改
     */
    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions("area:update")
    public ResultUtil update(@RequestBody Area area) {
        Area oldArea = areaService.queryObject(area.getAreaId());
        String oldAreaName=oldArea.getAreaName();
        String oldXzCode=oldArea.getXzCode();
        int count;
        if(!oldAreaName.equals(area.getAreaName())){
            Map<String, Object> params=new HashMap<>();
            params.put("areaName",area.getAreaName());
            count= areaService.getCount(params);
            if(count>0){
                throw new MyException("地区名称已存在");
            }
        }
        if(!area.getXzCode().equals(oldXzCode)){
            Map<String, Object> params=new HashMap<>();
            params.put("xzCode",area.getXzCode());
            count= areaService.getCount(params);
            if(count>0){
                throw new MyException("行政编码已存在");
            }
        }
        areaService.update(area);

        return ResultUtil.ok();
    }

    /**
     * 删除
     */
    @ResponseBody
    @RequestMapping("/delete")
    @RequiresPermissions("area:delete")
    public ResultUtil delete(@RequestBody String[] areaIds) {
        areaService.deleteBatch(areaIds);

        return ResultUtil.ok();
    }

    /**
     * 启用
     */
    @ResponseBody
    @Log("启用地区")
    @RequestMapping("/enable")
    @RequiresPermissions("area:update")
    public ResultUtil enable(@RequestBody String[] ids){
        String stateValue= StateEnum.ENABLE.getCode();
        areaService.updateState(ids,stateValue);
        return ResultUtil.ok();
    }
    /**
     * 禁用
     */
    @ResponseBody
    @Log("禁用地区")
    @RequestMapping("/disable")
    @RequiresPermissions("area:update")
    public ResultUtil disable(@RequestBody String[] ids){
        String stateValue=StateEnum.DIASABLE.getCode();
        areaService.updateState(ids,stateValue);
        return ResultUtil.ok();
    }
}
