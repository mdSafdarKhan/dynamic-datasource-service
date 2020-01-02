package com.example.dynamicdatasourceservice.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import com.example.dynamicdatasourceservice.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;
import org.springframework.stereotype.Component;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class Util {

	public static Map<String, Map<String, Object>> settings = new HashMap<>();
	public static Map<String, SessionFactory> sessionFactoryMap = new HashMap<>();

	@PostConstruct
	private static void init() {	// TODO This data will be initialized through data of spring cloud config server. For now it is managed locally.

		log.info("inside init");
		
		// Domain One Settings
		String domainOneName = "0:0:0:0:0:0:0:1";
		Map<String, Object> domainOneSettings = new HashMap<>();
		domainOneSettings.put(Environment.URL, "jdbc:mysql://localhost:3306/user_local");
		domainOneSettings.put(Environment.USER, "root");
		domainOneSettings.put(Environment.PASS, "jiyodigital");
		domainOneSettings.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
		domainOneSettings.put(Environment.HBM2DDL_AUTO, "update");
		domainOneSettings.put(Environment.SHOW_SQL, "true");
		domainOneSettings.put("hibernate.hikari.connectionTimeout", "20000");// Maximum waiting time for a connection from the pool
		domainOneSettings.put("hibernate.hikari.minimumIdle", "10");         // Minimum number of ideal connections in the pool
		domainOneSettings.put("hibernate.hikari.maximumPoolSize", "20");     // Maximum number of actual connection in the pool
		domainOneSettings.put("hibernate.hikari.idleTimeout", "300000");     // Maximum time that a connection is allowed to sit ideal in the pool

		settings.put(domainOneName, domainOneSettings);

		// Domain Two Settings
		String domainTwoName = "192.168.225.55";
		Map<String, Object> domainTwoSettings = new HashMap<>();
		domainTwoSettings.put(Environment.URL, "jdbc:mysql://localhost:3306/user_prod");
		domainTwoSettings.put(Environment.USER, "root");
		domainTwoSettings.put(Environment.PASS, "jiyodigital");
		domainTwoSettings.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
		domainTwoSettings.put(Environment.HBM2DDL_AUTO, "update");
		domainTwoSettings.put(Environment.SHOW_SQL, "true");
		domainTwoSettings.put("hibernate.hikari.connectionTimeout", "20000");// Maximum waiting time for a connection from the pool
		domainTwoSettings.put("hibernate.hikari.minimumIdle", "10");         // Minimum number of ideal connections in the pool
		domainTwoSettings.put("hibernate.hikari.maximumPoolSize", "20");     // Maximum number of actual connection in the pool
		domainTwoSettings.put("hibernate.hikari.idleTimeout", "300000");     // Maximum time that a connection is allowed to sit ideal in the pool

		settings.put(domainTwoName, domainTwoSettings);

		/*Map<String, Object> domain1DBDetails = new HashMap<>();
		String domain1Name = "0:0:0:0:0:0:0:1";
		domain1DBDetails.put("url", "jdbc:mysql://localhost/user_local");
		domain1DBDetails.put("username","root");
		domain1DBDetails.put("password", "jiyodigital");
		domain1DBDetails.put("driver", "com.mysql.jdbc.Driver");

		domain1DBDetails.put("maxPoolSize", 10);
		domain1DBDetails.put("connectionTestQuery", "SELECT 1");
		domain1DBDetails.put("poolName", "localhost hikari pool");

		configuration.put(domain1Name, domain1DBDetails);*/

		// Second
		/*Map<String, Object> domain2DBDetails = new HashMap<>();
		String domain2Name = "192.168.225.55";
		domain2DBDetails.put("url", "jdbc:mysql://localhost/user_prod");
		domain2DBDetails.put("username", "root");
		domain2DBDetails.put("password", "jiyodigital");
		domain2DBDetails.put("driver", "com.mysql.jdbc.Driver");

		domain2DBDetails.put("maxPoolSize", 10);
		domain2DBDetails.put("connectionTestQuery", "SELECT 1");
		domain2DBDetails.put("poolName", "localhost hikari pool");

		configuration.put(domain2Name, domain2DBDetails);*/

		log.info("exit init");
	}

	public static Session getSession(String domainName) throws Exception{
		log.info("inside getSession");
		log.info("domainName : " + domainName);

		if(sessionFactoryMap.containsKey(domainName)){
			log.info("session factory available");

			return sessionFactoryMap.get(domainName).openSession();
		}
		else if(settings.containsKey(domainName)){
			log.info("session factory not available, create new one");

			StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder();
			registryBuilder.applySettings(settings.get(domainName));
			StandardServiceRegistry serviceRegistry = registryBuilder.build();

			MetadataSources sources = new MetadataSources(serviceRegistry);
			sources.addAnnotatedClass(User.class);

			Metadata metadata = sources.getMetadataBuilder().build();
			SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();

			sessionFactoryMap.put(domainName, sessionFactory);

			return sessionFactory.openSession();
		}
		else{
			log.info("could not create session factory, setting not available");
			throw new Exception("could not create session factory, setting not available");
		}
	}
	
	/*public static HikariDataSource getDataSource(String domainName) throws Exception {
		log.info("inside getDataSource");
		log.info("domainName : " + domainName);
		log.info("configuration : " + configuration);
		log.info("datasourcePool : " + datasourcePool);
		
		if(datasourcePool.containsKey(domainName)) {
			log.info("datasource available");
			return datasourcePool.get(domainName);
		}
		else if(configuration.containsKey(domainName)) {
			log.info("create datasource, configuration available");
			HikariDataSource dataSource = createDataSource(domainName);
			datasourcePool.put(domainName, dataSource);
			return dataSource;
		}
		else {
			log.info("could not create datasource, configuration not available");
			throw new Exception("could not create datasource, configuration not available");
		}
	}*/
	
	/*private static HikariDataSource createDataSource(String domainName) {
		log.info("inside createDataSource");
		
		Map<String, Object> domainDBDetails = configuration.get(domainName);
		
		HikariConfig hikariConfig = new HikariConfig();
    	hikariConfig.setJdbcUrl(domainDBDetails.get("url").toString());
    	hikariConfig.setUsername(domainDBDetails.get("username").toString());
    	hikariConfig.setPassword(domainDBDetails.get("password").toString());
    	//hikariConfig.setDriverClassName(domainDBDetails.get("driver").toString());
    	//hikariConfig.setDataSourceClassName(domainDBDetails.get("driver").toString());
    	
    	hikariConfig.setMaximumPoolSize(Integer.valueOf(domainDBDetails.get("maxPoolSize").toString()));
    	hikariConfig.setConnectionTestQuery(domainDBDetails.get("connectionTestQuery").toString());
    	hikariConfig.setPoolName(domainDBDetails.get("poolName").toString());
    	
    	hikariConfig.addDataSourceProperty("dataSource.cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("dataSource.prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("dataSource.prepStmtCacheSqlLimit", "2048");
        hikariConfig.addDataSourceProperty("dataSource.useServerPrepStmts", "true");
        log.info("exit createDataSource");
        
        return new HikariDataSource(hikariConfig);
	}*/
	
	//public static Map<String, SessionFactory> sessionFactoryPool = new HashMap<>();
	
	/*
	 * public Session getSession(String domainName) { log.info("inside getSession");
	 * log.info("domainName : " + domainName);
	 * 
	 * if(sessionFactoryPool.containsKey(domainName)) {
	 * sessionFactoryPool.get(domainName); }
	 * 
	 * log.info("exit getSession"); }
	 */
}
