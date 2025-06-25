package com.hss.modules.system.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hss.core.common.api.vo.Result;
import com.hss.modules.facility.entity.DeviceBI;
import com.hss.modules.facility.entity.DeviceHandover;
import com.hss.modules.facility.service.IDeviceBIService;
import com.hss.modules.facility.service.IDeviceHandoverService;
import com.hss.modules.system.entity.Ditu;
import com.hss.modules.system.service.IDituService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.UUID;

/**
 * @Description: 附件及图片上传
 * @Author: zpc
 * @Date: 2023-02-01
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "附件及图片上传")
@RestController
@RequestMapping("/system/filesUtil")
public class FilesUtilController {
    @Autowired
    private IDeviceHandoverService deviceHandoverService;

    @Autowired
    private IDeviceBIService deviceBIService;

    @Autowired
    private IDituService dituService;

    @Value(value = "${jeecg.path.upload}")
    private String relativePath;

    @Value(value = "${jeecg.path.img.url}")
    private String imgPath;

    @Value(value = "${jeecg.path.voice}")
    private String voicePath;

    @Value(value = "${jeecg.path.ditu}")
    private String dituPath;

    @ApiOperation(value = "图片上传", notes = "图片上传")
    @PostMapping("/upload")
    public Result<?> upload(@RequestParam(name = "multipartFile") MultipartFile multipartFile) throws IOException {
        //1.获取文件名
        String fileName = multipartFile.getOriginalFilename();

        //2.获取文件后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));

        //3.指定上传路径
        String filePath = relativePath;

        //4.重新生成文件名
        String targetFileName = UUID.randomUUID().toString().replace("-", "");

        //5. 保存文件
        multipartFile.transferTo(new File(filePath + targetFileName + suffixName));

        String returnUrl = this.imgPath + "/hss-boot/system/filesUtil/filePreviewOrDownload/" + targetFileName + suffixName;

        return Result.OK(returnUrl).success("上传成功");
    }

    @ApiOperation(value = "场景底图上传", notes = "场景底图上传")
    @PostMapping("/uploadDitu")
    public Result<?> uploadDitu(@RequestParam(name = "multipartFile") MultipartFile multipartFile) throws IOException {
        //1.获取文件全名
        String fileName = multipartFile.getOriginalFilename();

        //去掉后缀的文件名
        int index = fileName.lastIndexOf(".");
        String realName = index == -1 ? fileName : fileName.substring(0, index);

        //2.获取文件后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));

        //3.指定路径
        String filePath = dituPath;

        //4.重新生成文件名
        String targetFileName = UUID.randomUUID().toString().replace("-", "");

        multipartFile.transferTo(new File(filePath + realName + suffixName));
        String returnUrl = "/hss-boot/system/filesUtil/ditu/" + fileName;

        if (CollectionUtil.isEmpty(dituService.list())) {
            saveDitu(realName, suffixName, filePath, targetFileName, returnUrl);
        }
        else {
            LambdaQueryWrapper<Ditu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Ditu::getName, realName);
            dituService.remove(queryWrapper);
            saveDitu(realName, suffixName, filePath, targetFileName, returnUrl);
        }
        return Result.OK(returnUrl).success("上传成功");
    }

    private void saveDitu(String realName, String suffixName, String filePath, String targetFileName, String returnUrl) {
        Ditu ditu = new Ditu();
        ditu.setImgType(suffixName.replace(".", ""));
        ditu.setName(realName);
        ditu.setNewName(targetFileName);
        ditu.setImgUrl(returnUrl);
        ditu.setUpTime(new Date());
        ditu.setSaveUrl(filePath);
        dituService.save(ditu);
    }

    @ApiOperation(value = "交接管理-图片保存", notes = "交接管理-图片保存")
    @PostMapping(value = "/saveImg")
    public Result<?> saveImg(@RequestBody DeviceHandover deviceHandover) {
        DeviceHandover byId = deviceHandoverService.getById(deviceHandover.getId());
        byId.setImgUrls(deviceHandover.getImgUrls());
        deviceHandoverService.updateById(byId);
        return Result.OK(deviceHandover).success("保存成功");
    }

    @ApiOperation(value = "设施设备-图片保存", notes = "设施设备-图片保存")
    @PostMapping(value = "/saveImgDevice")
    public Result<?> saveImgDevice(@RequestBody DeviceBI deviceBI) {
        DeviceBI dev = deviceBIService.getById(deviceBI.getId());
        dev.setImgUrls(deviceBI.getImgUrls());
        deviceBIService.updateById(dev);
        return Result.OK(deviceBI).success("保存成功");
    }

    @ApiOperation(value = "查看和下载", notes = "查看和下载")
    @GetMapping("/filePreviewOrDownload/{fileName}")
    public void filePreviewOrDownload(@PathVariable("fileName") String fileName,HttpServletResponse response) throws Exception {
        String filePath = relativePath;
        File file = new File(filePath + fileName);
        FileInputStream fis = new FileInputStream(file);
        outPutFiles(response, fis);
    }

    @ApiOperation(value = "语音文件下载访问", notes = "语音文件下载访问")
    @GetMapping("/voice/{fileName}")
    public void voice(@PathVariable("fileName") String fileName,HttpServletResponse response) throws Exception {
        String filePath = voicePath;
        File file = new File(filePath + fileName);
        FileInputStream fis = new FileInputStream(file);
        outPutFiles(response, fis);
    }

    @ApiOperation(value = "场景底图查看或下载", notes = "场景底图查看或下载")
    @GetMapping("/ditu/{fileName}")
    public void ditu(@PathVariable("fileName") String fileName, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");
        response.setContentType("image/svg+xml;charset=utf-8");

        String filePath = dituPath;
        File file = new File(filePath + fileName);
        FileInputStream fis = new FileInputStream(file);

        outPutFiles(response, fis);
    }

    private void outPutFiles(HttpServletResponse response, FileInputStream fis) throws IOException {
        OutputStream os = response.getOutputStream();
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) != -1) {
            os.write(bytes, 0, length);
        }
        os.close();
        fis.close();
    }
}
