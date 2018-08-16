package com.ztesoft.mdod.util.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.ztesoft.mdod.util.StringUtil;

public class SftpUtil {
	
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(SftpUtil.class);
	
	public static final String NO_FILE = "No such file";  
	  
    private ChannelSftp sftp = null;  
  
    private Session sshSession = null;  
  
    private String username;  
  
    private String password;  
  
    private String host;  
  
    private int port;  
    
    private String ftpPath;
  
    public SftpUtil(String username, String password, String host, int port, String ftpPath) {  
        this.username = username;  
        this.password = password;  
        this.host = host;  
        this.port = port; 
        this.ftpPath = ftpPath;
    }  
    public SftpUtil(String username, String password, String host, int port) {
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = port;
    }

    /** 
     * 连接sftp服务器 
     * @return ChannelSftp sftp类型 
     * @throws 
     */  
    public ChannelSftp connect() {  
    	System.out.println("SftpUtil-->connect--sftp连接开始>>>>>>host=" + host + ">>>port" + port + ">>>username=" + username);
        JSch jsch = new JSch();  
            try {
				jsch.getSession(username, host, port);
				sshSession = jsch.getSession(username, host, port);  
	            System.out.println("sftp---Session created.");
	            sshSession.setPassword(password);  
	            Properties properties = new Properties();  
	            properties.put("StrictHostKeyChecking", "no");  
	            sshSession.setConfig(properties);  
	            sshSession.connect();
	            System.out.println("sftp---Session connected.");
	            Channel channel = sshSession.openChannel("sftp");  
	            channel.connect();  
	            System.out.println("Opening Channel.");  
	            sftp = (ChannelSftp) channel;  
	            System.out.println("sftp---Connected to " + host);
			} catch (JSchException e) {
				e.printStackTrace();
				System.out.println("SftpUtil-->connect异常 " + host);
			}
        return sftp;  
    }  
  
    /** 
     * 载单个文件 
     * @param directory       ：远程下载目录(以路径符号结束) 
     * @param remoteFileName  FTP服务器文件名称 如：xxx.txt ||xxx.txt.zip 
     * @param localFile       本地文件路径 如 D:\\xxx.txt 
     * @return 
     * @throws GoPayException 
     */  
    public File downloadFile(String directory, String remoteFileName,String localFile) {
    	System.out.println(">>>>>>>>SftpUtil-->downloadFile--sftp下载文件"+remoteFileName+"开始>>>>>>>>>>>>>");
        connect();
        File file = null;
        OutputStream output = null;
        try {
            file = new File(localFile);
            if (file.exists()){
                file.delete();
            }
            file.createNewFile();
            sftp.cd(directory);
            output = new FileOutputStream(file);
            sftp.get(remoteFileName, output);
            System.out.println("===DownloadFile:" + remoteFileName + " success from sftp.");
        }
        catch (SftpException e) {
        	e.printStackTrace();
            if (e.toString().equals(NO_FILE)) {
            	System.out.println(">>>>>>>>SftpUtil-->downloadFile--sftp下载文件失败" + directory +remoteFileName+ "不存在>>>>>>>>>>>>>");
            }
            System.out.println("sftp目录或者文件异常，检查fstp目录和文件");
        }
        catch (FileNotFoundException e) {
        	e.printStackTrace();
        	System.out.println("本地目录异常，请检查" + file.getPath() + e.getMessage());
        }
        catch (IOException e) {
        	e.printStackTrace();
        	System.out.println("创建本地文件失败" + file.getPath() + e.getMessage());
        }
        finally {
            if (output != null) {
                try {
                    output.close();
                }
                catch (IOException e) {
                	e.printStackTrace();
                	System.out.println("Close stream error.");
                }
            }
            disconnect();
        }
  
        System.out.println(">>>>>>>>SftpUtil-->downloadFile--sftp下载文件结束>>>>>>>>>>>>>");
        return file;
    }

    /**
     * 以IO流形式下载文件
     * methon name downloadFile.
     * params [channelSftp, directory, remoteFileName].
     * return java.io.InputStream.
     * Created by Lib on 2018/01/24.
     */
    @SuppressWarnings("null")
	public InputStream downloadFile(ChannelSftp channelSftp, String directory, String remoteFileName){
        InputStream in = null;
        try {
            channelSftp.setFilenameEncoding("UTF-8"); // 中文支持
            channelSftp.cd(directory);
            in = channelSftp.get(remoteFileName);
            }catch (SftpException e) {
                e.printStackTrace();
                if (e.toString().equals(NO_FILE)) {
                    System.out.println(">>>>>>>>SftpUtil-->downloadFile--sftp下载文件失败" + directory +remoteFileName+ "不存在>>>>>>>>>>>>>");
                }
                System.out.println("sftp目录或者文件异常，检查fstp目录和文件");
                try {
                    in.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }
        return in;
    }


    /**
     * 上传单个文件
     *
     * @param directory      ：远程下载目录(以路径符号结束)
     * @param uploadFilePath 要上传的文件 如：D:\\test\\xxx.txt
     * @param fileName       FTP服务器文件名称 如：xxx.txt ||xxx.txt.zip
     * @throws GoPayException
     */
    public void uploadFile(String directory, String uploadFilePath, String fileName) {
        System.out.println(">>>>>>>>SftpUtil-->uploadFile--sftp上传文件开始>>>>>>>>>>>>>");
        FileInputStream in = null;
        connect();
        try {
            sftp.cd(directory);
        }
        catch (SftpException e) {
            try {
                sftp.mkdir(directory);
                sftp.cd(directory);
            }
            catch (SftpException e1) {
            	e1.printStackTrace();
            	System.out.println("sftp创建文件路径失败，路径为" + directory);
            }
  
        }
        File file = new File(uploadFilePath);
        try {
            in = new FileInputStream(file);
            sftp.put(in, fileName);
        }
        catch (FileNotFoundException e) {
        	e.printStackTrace();
        	System.out.println("文件不存在-->" + uploadFilePath);
        }
        catch (SftpException e) {
        	e.printStackTrace();
        	System.out.println("sftp异常-->");
        }
        finally {
            if (in != null){
                try {
                    in.close();
                }
                catch (IOException e) {
                	e.printStackTrace();
                	System.out.println("Close stream error.");
                }
            }
            disconnect();
        }
        System.out.println(">>>>>>>>SftpUtil-->uploadFile--sftp上传文件结束>>>>>>>>>>>>>");
    }

    /**
     * 批量上传文件
     * @param directory      ：远程下载目录(以路径符号结束)
     * @param uploadFilePath 要上传的文件 如：D:\\test\\xxx.txt
     * @param fileName       FTP服务器文件名称 如：xxx.txt ||xxx.txt.zip
     * @throws
     */
    public void uploadFile(LinkedHashSet<String> paths, String now) {
        System.out.println(">>>>>>>>SftpUtil-->uploadFile--sftp上传文件开始>>>>>>>>>>>>>");
        FileInputStream in = null;
        connect();
        
        String directory = "";
		String uploadFilePath = "";
		try{
			for(String path:paths){
				directory = ftpPath + "/"+path.split(",")[0]+"/day/"+now;
				uploadFilePath = path.split(",")[1];
				try {
		            sftp.cd(directory);
		        }
		        catch (SftpException e) {
		            try {
		                sftp.mkdir(directory);
		                sftp.cd(directory);
		            }
		            catch (SftpException e1) {
		            	e1.printStackTrace();
		            	System.out.println("sftp创建文件路径失败，路径为" + directory);
		            }
		  
		        }
				File file = new File(uploadFilePath);
		        try {
		            in = new FileInputStream(file);
		            sftp.put(in, file.getName());
		        }
		        catch (FileNotFoundException e) {
		        	e.printStackTrace();
		        	System.out.println("文件不存在-->" + uploadFilePath);
		        }
		        catch (SftpException e) {
		        	e.printStackTrace();
		        	System.out.println("sftp异常-->");
		        }
			}
		} catch(Exception e){
			e.printStackTrace();
		} finally{
			if (in != null){
                try {
                    in.close();
                }
                catch (IOException e) {
                	e.printStackTrace();
                	System.out.println("Close stream error.");
                }
            }
			disconnect();
		}
        System.out.println(">>>>>>>>SftpUtil-->uploadFile--sftp上传文件结束>>>>>>>>>>>>>");
    }

    /**
     * 列出目录下的文件
     * @param directory 要列出的目录
     * @param sftp
     * @return
     * @throws SftpException
     */
    @SuppressWarnings("rawtypes")
	public Vector listFiles(String directory, ChannelSftp sftp) throws SftpException{
        return sftp.ls(directory);
    }

    /**
     * 获得目录下的所有文件名
     * methon name listFiles.
     * params [directory].
     * return java.util.List<java.lang.String>.
     * Created by Lib on 2018/01/22.
     */
    @SuppressWarnings("rawtypes")
    public List<String> listFiles(ChannelSftp sftp, String directory) throws Exception {
        Vector fileList;
        List<String> fileNameList = new ArrayList<String>();
        fileList = sftp.ls(directory);
        Iterator it = fileList.iterator();
        while (it.hasNext()) {
            String fileName = ((ChannelSftp.LsEntry) it.next()).getFilename();
            if (".".equals(fileName) || "..".equals(fileName)) {
                continue;
            }
            fileNameList.add(fileName);
        }
        return fileNameList;
    }

    /**
     * 判断文件是否生成
     * methon name findFile.
     * params [sftp, directory, fileName].
     * return boolean.
     * Created by Lib on 2018/01/24.
     */
    public boolean findFile(ChannelSftp sftp, String directory, String fileName) throws SftpException {
        try {
            List<String> fileNameList = listFiles(sftp,directory);
            int exsitCount = 0;
            if (fileNameList.size()>0){
                for (String file : fileNameList) {
                    if (StringUtil.equals(fileName,file)){
                        exsitCount++;
                    }
                }
                if (exsitCount == fileNameList.size()) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * 判断文件是否存在
     * methon name isFileExist.
     * params [sftp, directory, fileName].
     * return boolean.
     * Created by Lib on 2018/01/24.
     */
    public boolean isFileExist(ChannelSftp sftp, String directory, String fileName) throws SftpException {
        try {
            List<String> fileNameList = listFiles(sftp,directory);
            if (fileNameList.size()>0){
                for (String file : fileNameList) {
                    if (StringUtil.equals(fileName,file)){
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * 同步完修改文件名称，避免下次继续同步
     * methon name renameFile.
     * params [channelSftp, fileName].
     * return void.
     * Created by Lib on 2018/01/24.
     */
    public void renameFile(ChannelSftp channelSftp,String fileName) throws SftpException {
        channelSftp.rename(fileName, fileName + "_bak");
    }

    /**
     * 文件重命名
     * methon name renameFile.
     * params [directory, oldFileNm, newFileNm].
     * return void.
     * Created by Lib on 2018/01/22.
     */
    public void renameFile(String directory, String oldFileNm, String newFileNm) throws Exception {
        logger.info(">>>>>>>>SftpUtil-->renameFile--sftp重命名"+oldFileNm+"开始>>>>>>>>>>>>>");
        sftp.cd(directory);
        sftp.rename(oldFileNm, newFileNm);
    }

    /**
     * 删除文件
     * methon name delete.
     * params [directory, deleteFile].
     * return void.
     * Created by Lib on 2018/01/22.
     */
    public void delete(String directory, String deleteFile) throws Exception {
        sftp.cd(directory);
        sftp.rm(deleteFile);
    }

    /** 
     * 关闭连接 
     */  
    public void disconnect() {
        if (this.sftp != null) {
            if (this.sftp.isConnected()) {
                this.sftp.disconnect();
                this.sftp = null;
                System.out.println("sftp is closed already");
            }
        }
        if (this.sshSession != null) {
            if (this.sshSession.isConnected()) {
                this.sshSession.disconnect();
                this.sshSession = null;
                System.out.println("sshSession is closed already");
            }
        }
    }

}
