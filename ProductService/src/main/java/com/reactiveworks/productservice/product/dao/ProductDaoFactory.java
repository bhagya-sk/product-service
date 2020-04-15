package com.reactiveworks.productservice.product.dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.reactiveworks.productservice.dao.IProductDao;
import com.reactiveworks.productservice.db.exceptions.DBOperationFailureException;
import com.reactiveworks.productservice.product.dao.implementation.ProductDaoCSVImpl;
import com.reactiveworks.productservice.product.dao.implementation.ProductDaoMysqlImpl;

/**
 * Factory class for ProductDao.
 */
public class ProductDaoFactory {

	private static final String PROPERTY_FILE = "dbtype.properties";
	private static final Logger LOGGER_OBJ = Logger.getLogger("ProductDaoFactory.class");
	private static final String DB_TYPE = "dbtype";
	private static final String CSV = "csv";
	private static final String MYSQL = "mysql";

	public static Properties properties = null;

	/**
	 * Creates the object ProductDao implementation class.
	 * 
	 * @return the ProductDao implementation class object.
	 * @throws DBOperationFailureException when operation on the database fails.
	 */
	public static IProductDao getInstance() throws DBOperationFailureException {
		LOGGER_OBJ.debug("execution of getInstance() started");
		IProductDao productDaoObj = null;
		if (properties == null) {
			try (InputStream input = ProductDaoFactory.class.getClassLoader().getResourceAsStream(PROPERTY_FILE)) {

				properties = new Properties();
				properties.load(input);

			} catch (IOException exp) {
				LOGGER_OBJ.error("not able to read the properties file " + PROPERTY_FILE);
				throw new DBOperationFailureException("not able to read the file " + PROPERTY_FILE, exp);
			}
		}

		if (((String) properties.get(DB_TYPE)).equalsIgnoreCase(CSV)) {
			productDaoObj = new ProductDaoCSVImpl();
		} else if (((String) properties.get(DB_TYPE)).equalsIgnoreCase(MYSQL)) {
			productDaoObj = new ProductDaoMysqlImpl();
		} else {
			LOGGER_OBJ.debug(properties.get(DB_TYPE) + " implementation does not exist.");
			productDaoObj = new ProductDaoCSVImpl(); // default stockTrade Dao Object
		}
		LOGGER_OBJ.debug("execution of getInstance() completed");
		return productDaoObj;
	}

	/**
	 * Creates the object ProductDao implementation class.
	 * 
	 * @return the ProductDao implementation class object.
	 * @throws DBOerationFailureException when operation on the database fails.
	 */
	public static IProductDao getInstance(String dbType) throws DBOperationFailureException {
		LOGGER_OBJ.debug("execution of getInstance() started");
		IProductDao productDaoObj = null;

		if(dbType!=null) {
			if (dbType.equalsIgnoreCase(CSV)) {
				productDaoObj = new ProductDaoCSVImpl();
			} else if (dbType.equalsIgnoreCase(MYSQL)) {
				productDaoObj = new ProductDaoMysqlImpl();
			} else {
				LOGGER_OBJ.debug(dbType + " implementation does not exist.");
				productDaoObj = new ProductDaoCSVImpl(); // default stockTrade Dao Object
			}
		}
		
		LOGGER_OBJ.debug("execution of getInstance() completed");
		return productDaoObj;
	}
}
