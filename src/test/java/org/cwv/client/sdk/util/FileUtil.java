package org.cwv.client.sdk.util;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    public static void main(String[] args) throws IOException {
        String path = "d:/txList.log";
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path)),"utf-8"));
        String line = br.readLine();
        
        int i=0;
        while(null != line) {
            i++;
            
            line=br.readLine();
        }

        System.out.println(i);
        
    }

    public static List<String> getContent(String path){
        List<String> list = new ArrayList();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path)),"utf-8"));
            String line = br.readLine();
            while(null != line && !"".equals(line.trim())) {
                list.add(line);
                line=br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

}
