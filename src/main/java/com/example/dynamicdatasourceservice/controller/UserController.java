package com.example.dynamicdatasourceservice.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.dynamicdatasourceservice.model.User;
import com.example.dynamicdatasourceservice.util.Util;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.hibernate.HikariConnectionProvider;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/doctor")
public class UserController {

    

    /*@GetMapping
    public Object getDoctor(HttpServletRequest request) throws Exception{

        String remoteAddress = request.getRemoteAddr();
        log.info("remoteAddress: " + remoteAddress);

        HikariDataSource dataSource = Util.getDataSource(remoteAddress);
        
        List<String> emails = new ArrayList<>();
        
        try(Connection con = dataSource.getConnection()){
        	PreparedStatement pst = con.prepareStatement("from users");
            ResultSet rs = pst.executeQuery();
            while(rs.next()) {
            	emails.add(rs.getString("email"));
            }
        }
        
		*//*
		 * Configuration configuration = new Configuration(); SessionFactory
		 * sessionFactory = configuration.buildSessionFactory(new
		 * StandardServiceRegistryBuilder()
		 * .applySettings(configuration.getProperties())
		 * .applySetting(Environment.DATASOURCE, dataSource) .build()); Session session
		 * = sessionFactory.openSession(); Query query =
		 * session.createNativeQuery("select * from users");
		 * 
		 * log.info("size : " + query.list().size());
		 *//*
                
        return null;
    }*/

    @GetMapping("/test")
    public Object getUsers(HttpServletRequest request) throws Exception {
    	log.info("inside getUsers");

		String domainName = request.getRemoteAddr();
		log.info("domainName: " + domainName);

		List<User> users = null;
		try(Session session = Util.getSession(domainName)){
			users = session.createQuery("FROM User").list();
			log.info("size : " + users.size());
		}

    	log.info("exit getUsers");
    	return users;
    }
}



