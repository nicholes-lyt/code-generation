package com.codegen.base;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.Configuration;

import com.codegen.util.GenUtils;
import com.codegen.util.JdbcUtils;

/**
 * 代码生成器
 * @author liyut
 *
 */
public class CodeAutoGenerate {
	
	// 数据库连接
	/*private static final String driver = "com.mysql.jdbc.Driver";
	private static final String pwd = "root";
	private static final String user = "root";
	private static final String url = "jdbc:mysql://localhost:3306/quanmin_sys" + "?user=" + user + "&password=" + pwd
			+ "&useUnicode=true&characterEncoding=UTF-8";*/
	
	private static Connection getConnection = null;
	
	public static void main(String[] args) throws Exception {
		Configuration config = GenUtils.getConfig();
		String tableName = config.getString("tablename");
		byte[] byt = GenUtils.generatorCodeFile(getTable(tableName),getTableColumns(tableName));
		assertFile("D:/自动生成代码", tableName+".zip",byt);
	}
	
	/**
	 * 获取表信息
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	private static Map<String, String> getTable(String tableName) throws Exception {
		try {
			getConnection = getConnections();
			String sql = " SELECT table_name tableName, ENGINE engine, table_comment tableComment, create_time createTime FROM information_schema.tables "
					+ " WHERE table_schema = (SELECT DATABASE()) AND table_name = '"+tableName+"' ";
			PreparedStatement pst = getConnection.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			Map<String, String> map = new HashMap<>();
			while(rs.next()) {
				map.put("tableName", rs.getString("tableName"));
				map.put("engine", rs.getString("engine"));
				map.put("tableComment", rs.getString("tableComment"));
				map.put("createTime", rs.getString("createTime"));
			}
			//System.out.println(map);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			getConnection.close();
		}
	}
	
	/**
	 * 获取表字段
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	private static List<Map<String, String>> getTableColumns(String tableName) throws Exception {
		try {
			getConnection = getConnections();
			String sql = " SELECT column_name columnName, data_type dataType, column_comment columnComment, column_key columnKey,extra,is_nullable isNullable FROM information_schema.columns "
					+ " WHERE table_name = '"+tableName+"' AND table_schema = (SELECT DATABASE()) ORDER BY ordinal_position ";
			PreparedStatement pst = getConnection.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			List<Map<String, String>> list = new ArrayList<>();
			while(rs.next()) {
				Map<String, String> map = new HashMap<>();
				map.put("columnName", rs.getString("columnName"));
				map.put("dataType", rs.getString("dataType"));
				map.put("columnComment", rs.getString("columnComment"));
				map.put("columnKey", rs.getString("columnKey"));
				map.put("extra", rs.getString("extra"));
				map.put("isNullable", rs.getString("isNullable"));
				list.add(map);
			}
			//System.out.println(new Gson().toJson(list));
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			getConnection.close();
		}
	}
	
	
	public static void assertFile(String directory, String fileName,byte bt[]) throws IOException {
        File tmpFile = new File(directory + File.separator + fileName );
        if (tmpFile.exists()) {
            if (tmpFile.isDirectory()) {
                throw new IOException("File '" + tmpFile + "' exists but is a directory");
            }
            if (!tmpFile.canWrite()) {
                throw new IOException("File '" + tmpFile + "' cannot be written to");
            }
        } else {
            File parent = tmpFile.getParentFile();
            if (parent != null) {
                if (!parent.mkdirs() && !parent.isDirectory()) {
                    throw new IOException("Directory '" + parent + "' could not be created");
                }
            }
        } 
        try {  
            FileOutputStream in = new FileOutputStream(tmpFile);  
            try {  
                in.write(bt, 0, bt.length);  
                in.close();  
                System.out.println("写入文件成功");  
            } catch (IOException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            }  
        } catch (FileNotFoundException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
    }
	
	/**
	 * 数据库连接
	 * @return
	 * @throws Exception 
	 */
	public static Connection getConnections() throws Exception {
		try {
			/*Class.forName(driver);
			getConnection = DriverManager.getConnection(url);*/
			return JdbcUtils.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
