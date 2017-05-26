package com.yss.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** 
 * Project Name:DataStreamUi 
 * File Name:StormSDKDownloadController.java 
 * Author:llduo
 * Date:2017年5月24日下午5:16:26 
 * Copyright (c) 2017, xglguan@163.com All Rights Reserved. 
 * 
 */
@RestController
public class StormSDKDownloadController {
	//private static final String source_directory = "/DataStreamUi/opt/download";
	private static final String source_directory = "D:/softs/";
    @RequestMapping(value="/filed",method=RequestMethod.GET)
    public String handleFileDown(@RequestParam("fileName") String fileName,HttpServletRequest request,HttpServletResponse response){
    	
    	if(null != fileName && !"".equals(fileName)){
    		/*String realPath = request.getServletContext().getRealPath(source_directory);*/
    		File file = new File(source_directory,fileName);
    		
    		if(file.exists()){
    			response.setContentType("application/force-download");
    			response.addHeader("Content-Disposition","attachment;fileName=" + fileName);
    			byte[] bytes = new byte[1024];
    			
    			FileInputStream fis = null;
    			BufferedInputStream bis = null;
    			
    			try {
					fis = new FileInputStream(file);
					bis = new BufferedInputStream(fis);
					
					OutputStream os = response.getOutputStream();
					
					int i = -1;
					while ((i = bis.read(bytes)) != -1){
						os.write(bytes,0,i);
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if(null != bis){
						try {
							bis.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if(null != fis){
						try {
							fis.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
    		}else{
    			throw new RuntimeException("the ["+fileName+"] file is not exists");
    		}
    		
    	}
    	return null;
    }
    

}
