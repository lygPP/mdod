package com.ztesoft.mdod;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ServletComponentScan   //扫描Servlet
@MapperScan("com.ztesoft.mdod.dao")
@ComponentScan
public class MdodApplication {
	public static void main(String[] args) {
        SpringApplication.run(MdodApplication.class, args);
    }
}
