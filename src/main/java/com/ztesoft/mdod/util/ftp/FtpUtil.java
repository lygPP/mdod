package com.ztesoft.mdod.util.ftp;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.util.List;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by lam on 2017/7/25.
 */
public class FtpUtil {
	
    private static Logger logger = LoggerFactory.getLogger(FtpUtil.class);

    /**
     * 获取FTPClient对象
     * @param ftpHost FTP主机服务器
     * @param ftpPort FTP端口 默认为21
     * @param ftpUserName FTP登录用户名
     * @param ftpPassword FTP 登录密码
     * @return
     */
    public static FTPClient getFTPClient(String ftpHost, int ftpPort, String ftpUserName,
                                         String ftpPassword) {
        FTPClient ftpClient = null;
        try {
            ftpClient = new FTPClient();
            ftpClient.connect(ftpHost, ftpPort);// 连接FTP服务器
            ftpClient.login(ftpUserName, ftpPassword);// 登陆FTP服务器
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
            	if (logger.isInfoEnabled()) {
            		logger.info("未连接到FTP，用户名或密码错误,host:" + ftpHost + ",port" + ftpPort + ",user" + ftpUserName + ",password" + ftpPassword);
    			}
                ftpClient.disconnect();
            }
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
        } catch (SocketException e) {
            e.printStackTrace();
            if (logger.isInfoEnabled()) {
            	logger.info("FTP的IP地址可能错误，请正确配置,host:" + ftpHost + ",port" + ftpPort + ",user" + ftpUserName + ",password" + ftpPassword);
			}
        } catch (IOException e) {
            e.printStackTrace();
            if (logger.isInfoEnabled()) {
        		logger.info("FTP的端口错误,请正确配置,host:" + ftpHost + ",port" + ftpPort + ",user" + ftpUserName + ",password" + ftpPassword);
			}
        }
        return ftpClient;
    }


    /**
     * 从服务器的FTP路径下上读取文件
     *
     * @param fileName
     * @return
     */
    @SuppressWarnings("null")
	public static InputStream downloadFile(FTPClient ftpClient, String fileName) {
        InputStream in = null;
        try {
//            ftpClient.setControlEncoding("UTF-8"); // 中文支持
//            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            in = ftpClient.retrieveFileStream(fileName);
//            completePendingCommand避免下载文件后工作目录变空
//            ftpClient.completePendingCommand();
        } catch (FileNotFoundException e) {
            logger.error("没有找到" + fileName + "文件");
            e.printStackTrace();
        } catch (SocketException e) {
            logger.error("连接FTP失败.");
            e.printStackTrace();
        } catch (IOException e) {
            logger.error("文件读取错误。");
            try {
                in.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        return in;
    }

    //判断文件是否生成
    public static boolean findFile(FTPClient ftpClient, List<String> files) throws IOException {
        FTPFile[] ftpFiles = ftpClient.listFiles();

        int exsitCount = 0;

        for (String fileName : files) {
            for (FTPFile file : ftpFiles) {
                if (file.getName().equals(fileName))
                    exsitCount++;
            }
        }
        if (exsitCount == files.size()) {
            return true;
        }
        return false;
    }
    
    //判断文件是否生成
    public static boolean findFile(FTPClient ftpClient, String fileName) throws IOException {
        FTPFile[] ftpFiles = ftpClient.listFiles();
        for (FTPFile file : ftpFiles) {
            if (file.getName().equals(fileName))
                return true;
        }
        return false;
    }

    //同步完修改文件名称，避免下次继续同步
    public static void renameFile(FTPClient ftpClient, String fileName) throws IOException {
        ftpClient.rename(fileName, fileName + "_bak");
    }
}
