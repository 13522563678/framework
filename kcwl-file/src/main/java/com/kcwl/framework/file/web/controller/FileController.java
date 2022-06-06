package com.kcwl.framework.file.web.controller;

import com.kcwl.framework.entity.ApiResult;
import com.kcwl.framework.file.biz.service.IFileService;
import com.kcwl.framework.utils.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import static com.kcwl.framework.file.FileConstants.FILE_SEPARATOR;
import static com.kcwl.framework.file.FileConstants.POINT;

/**
 * @author 姚华成
 * @date 2018-06-09
 */
@Api(tags = "文件管理")
@Controller
public class FileController {

    private IFileService fileService;

    /**
     * ContentType
     */
    public final Map<String, String> extMaps = new HashMap<>();

    private static final String DEFAULT_FILE_CONTENT_TYPE = "application/octet-stream";

    public FileController() {
        initExt();
    }

    private void initExt() {
        // image
        extMaps.put("png", "image/png");
        extMaps.put("gif", "image/gif");
        extMaps.put("bmp", "image/bmp");
        extMaps.put("ico", "image/x-ico");
        extMaps.put("jpeg", "image/jpeg");
        extMaps.put("jpg", "image/jpeg");
        // 压缩文件
        extMaps.put("zip", "application/zip");
        extMaps.put("rar", "application/x-rar");
        // doc
        extMaps.put("pdf", "application/pdf");
        extMaps.put("ppt", "application/vnd.ms-powerpoint");
        extMaps.put("xls", "application/vnd.ms-excel");
        extMaps.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        extMaps.put("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
        extMaps.put("doc", "application/msword");
        extMaps.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        extMaps.put("txt", "text/plain");
        // 音频
        extMaps.put("mp4", "video/mp4");
        extMaps.put("flv", "video/x-flv");
    }

    @ResponseBody
    @PostMapping("/api/file/fileUpload")
    @ApiOperation("文件上传")
    public ApiResult<String> upload(@RequestParam("file") @Validated @NotNull MultipartFile file) throws IOException {
        return ApiResult.success(fileService.upload(file.getOriginalFilename(), file.getInputStream(), null, true));
    }

    @GetMapping("/api/file/fileDownload")
    public void download(@RequestParam(name = "filepath") String filepath,
                         @RequestParam(name = "downloadFilename", required = false) String downloadFilename,
                         HttpServletResponse response) throws IOException {
        fileService.download(filepath, response.getOutputStream());
        if (StringUtil.hasText(downloadFilename)) {
            downloadFilename = fileService.getOriginalFilename(filepath);
        }
        String suffix = getFilenameSuffix(downloadFilename);
        String contentType = extMaps.get(suffix);
        if (StringUtil.isEmpty(contentType)) {
            contentType = DEFAULT_FILE_CONTENT_TYPE;
        }
        // 设置响应头
        // 文件编码 处理文件名中的 '+'、' ' 特殊字符
        String encoderFilename = URLEncoder.encode(downloadFilename, "UTF-8")
                .replace("+", "%20").replace("%2B", "+");
        response.setHeader("Content-Disposition", "attachment;filepath=\"" + encoderFilename + "\"");
        response.setContentType(contentType + ";charset=UTF-8");
        response.setHeader("Accept-Ranges", "bytes");
    }

    /**
     * 获取文件名称的后缀
     *
     * @param filename 文件名 或 文件路径
     * @return 文件后缀
     */
    private String getFilenameSuffix(String filename) {
        String suffix = null;
        if (StringUtil.hasText(filename)) {
            int index = filename.lastIndexOf(FILE_SEPARATOR);
            if (index >= 0) {
                filename = filename.substring(index + 1);
            }
            index = filename.lastIndexOf(POINT);
            if (index >= 0) {
                suffix = filename.substring(index + 1);
            }
        }
        return suffix;
    }
}
