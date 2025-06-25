package com.hss.modules.scada.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.system.query.QueryGenerator;
import com.hss.modules.scada.entity.GsChangJingZDYTP;
import com.hss.modules.scada.service.IGsChangJingZDYTPService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * @author zpc
 * @version 1.0
 * @description: 场景 场景自定义图片上传
 * @date 2024/3/19 14:48
 */
@Slf4j
@Api(tags = "场景 场景自定义图片上传")
@RestController
@RequestMapping("/api/scada/pic")
@CrossOrigin
public class GsSceneBackgroundImageController extends HssController<GsChangJingZDYTP, IGsChangJingZDYTPService> {
    @Autowired
    private IGsChangJingZDYTPService gsChangJingZDYTPService;
    //图片保存路径
    private static String imgsave_disc = "d:/by";
    //图片保存目录
    private static String imgsave_dirpath = "/Upload/Images";

    /**
     * 分页列表查询
     *
     * @param gsChangJingZDYTP
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "场景 场景自定义图片上传-分页列表查询", notes = "场景 场景自定义图片上传-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GsChangJingZDYTP gsChangJingZDYTP,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<GsChangJingZDYTP> queryWrapper = QueryGenerator.initQueryWrapper(gsChangJingZDYTP, req.getParameterMap());
        Page<GsChangJingZDYTP> page = new Page<GsChangJingZDYTP>(pageNo, pageSize);
        IPage<GsChangJingZDYTP> pageList = gsChangJingZDYTPService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param gsChangJingZDYTP
     * @return
     */
    @ApiOperation(value = "场景 场景自定义图片上传-添加", notes = "场景 场景自定义图片上传-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GsChangJingZDYTP gsChangJingZDYTP) {
        gsChangJingZDYTPService.save(gsChangJingZDYTP);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param gsChangJingZDYTP
     * @return
     */
    @ApiOperation(value = "场景 场景自定义图片上传-编辑", notes = "场景 场景自定义图片上传-编辑")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<?> edit(@RequestBody GsChangJingZDYTP gsChangJingZDYTP) {
        gsChangJingZDYTPService.updateById(gsChangJingZDYTP);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "场景 场景自定义图片上传-通过id删除", notes = "场景 场景自定义图片上传-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        gsChangJingZDYTPService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @ApiOperation(value = "场景 场景自定义图片上传-批量删除", notes = "场景 场景自定义图片上传-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<HashMap<String, String>> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        String[] arr_id = ids.split(",");
        for (int i = 0; i < arr_id.length; i++) {
            String id = arr_id[i];
            GsChangJingZDYTP gsChangJingZDYTP = gsChangJingZDYTPService.getById(id);
            String path = imgsave_disc + gsChangJingZDYTP.getPicurl();
            File file = new File(path);
            if (file.exists()) file.delete();
        }
        this.gsChangJingZDYTPService.removeByIds(Arrays.asList(arr_id));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "场景 场景自定义图片上传-通过id查询", notes = "场景 场景自定义图片上传-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        GsChangJingZDYTP gsChangJingZDYTP = gsChangJingZDYTPService.getById(id);
        return Result.OK(gsChangJingZDYTP);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param gsChangJingZDYTP
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GsChangJingZDYTP gsChangJingZDYTP) {
        return super.exportXls(request, gsChangJingZDYTP, GsChangJingZDYTP.class, "场景 场景自定义图片上传");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, GsChangJingZDYTP.class);
    }

    @ApiOperation(value = "场景-我得图库列表", notes = "场景-我得图库列表")
    @GetMapping("/getMyMoudleData")
    public Result<List<GsChangJingZDYTP>> getMyMoudleData(GsChangJingZDYTP gsChangJingZDYTP, HttpServletRequest req) {
        List<GsChangJingZDYTP> list = new ArrayList<>();
        QueryWrapper<GsChangJingZDYTP> queryWrapper = QueryGenerator.initQueryWrapper(gsChangJingZDYTP, req.getParameterMap());
        list = gsChangJingZDYTPService.list(queryWrapper);
        return Result.OK(list);
    }

    @ApiOperation(value = "场景-读取图片", notes = "场景-读取图片")
    @GetMapping("/Upload/Images/{filePath}")
    public void getFile(@PathVariable String filePath, HttpServletRequest request, HttpServletResponse response) {
        FileInputStream in;
        try {
            request.setCharacterEncoding("utf-8");
            //页面img标签中src中传入的图片地址路径
            String path = imgsave_disc + imgsave_dirpath + "/" + filePath;
            //String path = "d:/by/Upload/Images/" + filePath;
            String filePathEcode = new String(path.trim().getBytes(), "UTF-8");
            response.setContentType("application/octet-stream;charset=UTF-8");
            //图片读取路径
            in = new FileInputStream(filePathEcode);
            //得到文件大小
            int len = in.available();
            //创建存放文件内容的数组
            byte[] data = new byte[len];
            in.read(data);
            in.close();
            //把图片写出去
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            outputStream.write(data);
            //将缓存区的数据进行输出
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ApiOperation(value = "场景-添加组件", notes = "场景-添加组件")
    @PostMapping("/savePIC")
    public Result<HashMap<String, String>> savePIC(MultipartFile imagefile) {
        //获取文件名
        String fileName = imagefile.getOriginalFilename();
        //获取文件后缀
        String suffixName = fileName.substring(fileName.lastIndexOf('.'));
        switch (suffixName) {
            case ".jpg":
            case ".png":
            case ".gif":
            case ".jpeg":
            case ".svg":
                break;
            default:
                return Result.error("只允许上传[*.jpg,*.png,*.gif,*.jpeg]");
        }
        //重新生成文件名
        String filename = UUID.randomUUID() + suffixName;
        try {
            imagefile.transferTo(new File(imgsave_disc + imgsave_dirpath + "/" + filename));
        } catch (IOException ex) {
            return Result.error(ex.getMessage());
        }
        GsChangJingZDYTP entity = new GsChangJingZDYTP();
        entity.setId(UUID.randomUUID().toString());
        entity.setCreatedTime(new Date());
        entity.setPicurl(imgsave_dirpath + "/" + filename);
        gsChangJingZDYTPService.save(entity);

        HashMap<String, String> data = new HashMap<>();
        data.put("id", entity.getId());

        return Result.OK(data);
    }
}
