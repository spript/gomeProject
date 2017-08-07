package com.gomeplus.ams.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.gomeplus.adm.biz.web.AjaxResponse;
import com.gomeplus.adm.common.util.FileUtil;
import com.gomeplus.adm.common.util.RandomStringUtil;

@RestController
public class ImageUploadController {
	private Logger logger = LogManager.getLogger(getClass());
	
	/**
	 * 上传素材图片
	 * @param request
	 * @param file 上传的图片
	 * @return
	 */
	@RequestMapping(value = "/api/image/upload", method = RequestMethod.POST)
	public AjaxResponse upload(HttpServletRequest request, @RequestParam(value="file", required=true) MultipartFile file) {
		HashMap<String, Object> data = new HashMap<String, Object>();

		String imageTmpName = System.getProperty("java.io.tmpdir") + File.separator + RandomStringUtil.getRandomString(10)
				+ new Date().getTime();
		File image = new File(imageTmpName);
		String fileType;
		try {
			file.transferTo(image);
			fileType = file.getContentType();
			// 上传文件至CDN
			String size = Long.toString(file.getSize());
			String imageUrl;
			try {
				imageUrl = FileUtil.sendFileToCDN(size, image);
				// 将生成的临时文件删除
				image.delete();

			} catch (Exception exception) {
				logger.error("file.transferTo IllegalStateException", exception);
				return AjaxResponse.error(400, "上传图片失败！" + exception.getMessage());
			}

			data.put("imageUrl", imageUrl);
			data.put("size", size);
			data.put("fileType", fileType);
			String fileName = file.getOriginalFilename();
			data.put("fileName", fileName.substring(0, fileName.lastIndexOf('.')));

		} catch (IllegalStateException exception) {
			logger.error("file.transferTo IllegalStateException", exception);
			return AjaxResponse.error(400, "上传图片失败！" + exception.getMessage());
		} catch (IOException exception) {
			logger.error("file.transferTo IOException", exception);
			return AjaxResponse.error(400, "上传图片失败！" + exception.getMessage());
		}
		return AjaxResponse.success(data, "上传图片成功！");
	}
}
