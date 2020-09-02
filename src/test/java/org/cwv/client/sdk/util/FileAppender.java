package org.cwv.client.sdk.util;

import java.io.*;
import java.util.List;

public class FileAppender {
    
    private BufferedWriter bw;

    
    private FileAppender(String filePath, boolean append){
        try {
            File file = new File(filePath);
            // 如果文件夹不存在则创建
            if (!file.getParentFile().exists()&&!file.isDirectory()){
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            
            this.bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,append),"utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static FileAppender getAppender(String filePath,boolean append){
        return new FileAppender(filePath,append);
    }


    public synchronized FileAppender append(String line){
        try {
            bw.write(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }
    
    public synchronized FileAppender appendLine(String line){
        try {
            bw.write(line);
            bw.newLine();
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }
    
    public synchronized FileAppender batchAppendLine(List<String> list){
        try {
            for(String line:list){
                bw.write(line);
                bw.newLine();
            }
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public synchronized void flush(){
        try {
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public synchronized void release(){
        try {
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        FileAppender fa = FileAppender.getAppender("d:/1.log",true);
        for(int i=0;i<10;i++){
            fa.appendLine(i+"aaaaaa\r\n");
        }
        
        
        
    }
    
}
