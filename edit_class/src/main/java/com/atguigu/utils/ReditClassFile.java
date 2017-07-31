package com.atguigu.utils;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import org.gjt.jclasslib.io.ClassFileWriter;
import org.gjt.jclasslib.structures.CPInfo;
import org.gjt.jclasslib.structures.ClassFile;
import org.gjt.jclasslib.structures.constants.ConstantUtf8Info;

/**
 * @author Administrator
 * jars:jclasslib.jar
 * func: 直接基于字节码编译工具包,替换修改.class文件
 */
public class ReditClassFile {
	public static void main(String[] args) throws Exception {

		String filePath = "class_path/BasicFileFilter.class"; // 使用相对路径
		FileInputStream fis = null;

		try {
			fis = new FileInputStream(filePath); // .class 读入内存
			DataInput di = new DataInputStream(fis); // 转换为java类型格式
			ClassFile cf = new ClassFile(); 
			cf.read(di);  // 以 classFile 格式为模板进行分析
			CPInfo[] infos = cf.getConstantPool(); // 获取常量池
			// hello
			int count = infos.length;

			for (int i = 0; i < count; i++) {
				if (infos[i] != null) {
					System.out.print("ID: " + i);
					System.out.print(" -> var_name ");
					System.out.print(infos[i].getVerbose());
					System.out.print(" : type ");
					System.out.println(infos[i].getTagVerbose());
					
					if (i == 12) { // bytecode viewer 中查看到的".myeclipse.properties" 对于的存储位置
						ConstantUtf8Info uInfo = (ConstantUtf8Info) infos[i];
						uInfo.setBytes(".myeclipse_2017.properties".getBytes());
						infos[i] = uInfo;
					}
				}
			}
			
			cf.setConstantPool(infos); // 替换内存信息
			ClassFileWriter.writeToFile(new File(filePath), cf); // 重新写回,覆盖源文件
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				fis.close();
			}
		}
	}
}
