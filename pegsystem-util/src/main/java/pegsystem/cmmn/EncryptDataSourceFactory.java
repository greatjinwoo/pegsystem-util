package pegsystem.cmmn;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.misc.BASE64Decoder;


/**
 * jndi lookup으로 datasource 객체 생성시 
 * 암호화 된 url, username, password를 복호화하는 ObjectFactory 확장 Class.
 * 
 * 암/복호화 : BASE64
 */
public class EncryptDataSourceFactory implements ObjectFactory {
	
	protected static final int UNKNOWN_TRANSACTIONISOLATION = -1;
    private final static String PROP_DEFAULTAUTOCOMMIT = "defaultAutoCommit";
    private final static String PROP_DEFAULTREADONLY = "defaultReadOnly";
    private final static String PROP_DEFAULTTRANSACTIONISOLATION = "defaultTransactionIsolation";
    private final static String PROP_DEFAULTCATALOG = "defaultCatalog";
    private final static String PROP_DRIVERCLASSNAME = "driverClassName";
    private final static String PROP_MAXACTIVE = "maxActive";
    private final static String PROP_MAXIDLE = "maxIdle";
    private final static String PROP_MINIDLE = "minIdle";
    private final static String PROP_INITIALSIZE = "initialSize";
    private final static String PROP_MAXWAIT = "maxWait";
    private final static String PROP_TESTONBORROW = "testOnBorrow";
    private final static String PROP_TESTONRETURN = "testOnReturn";
    private final static String PROP_TIMEBETWEENEVICTIONRUNSMILLIS = "timeBetweenEvictionRunsMillis";
    private final static String PROP_NUMTESTSPEREVICTIONRUN = "numTestsPerEvictionRun";
    private final static String PROP_MINEVICTABLEIDLETIMEMILLIS = "minEvictableIdleTimeMillis";
    private final static String PROP_TESTWHILEIDLE = "testWhileIdle";
    private final static String PROP_PASSWORD = "password";
    private final static String PROP_URL = "url";
    private final static String PROP_USERNAME = "username";
    private final static String PROP_VALIDATIONQUERY = "validationQuery";
    private final static String PROP_ACCESSTOUNDERLYINGCONNECTIONALLOWED = "accessToUnderlyingConnectionAllowed";
    private final static String PROP_REMOVEABANDONED = "removeAbandoned";
    private final static String PROP_REMOVEABANDONEDTIMEOUT = "removeAbandonedTimeout";
    private final static String PROP_LOGABANDONED = "logAbandoned";
    private final static String PROP_POOLPREPAREDSTATEMENTS = "poolPreparedStatements";
    private final static String PROP_MAXOPENPREPAREDSTATEMENTS = "maxOpenPreparedStatements";
    private final static String PROP_CONNECTIONPROPERTIES = "connectionProperties";
    private final static String[] ALL_PROPERTIES = { PROP_DEFAULTAUTOCOMMIT, 
          											 PROP_DEFAULTREADONLY, PROP_DEFAULTTRANSACTIONISOLATION, PROP_DEFAULTCATALOG, 
          											 PROP_DRIVERCLASSNAME, PROP_MAXACTIVE, PROP_MAXIDLE, PROP_MINIDLE, 
          											 PROP_INITIALSIZE, PROP_MAXWAIT, PROP_TESTONBORROW, PROP_TESTONRETURN,
          											 PROP_TIMEBETWEENEVICTIONRUNSMILLIS, PROP_NUMTESTSPEREVICTIONRUN,
          											 PROP_MINEVICTABLEIDLETIMEMILLIS, PROP_TESTWHILEIDLE, PROP_PASSWORD,
          											 PROP_URL, PROP_USERNAME, PROP_VALIDATIONQUERY, 
          											 PROP_ACCESSTOUNDERLYINGCONNECTIONALLOWED, PROP_REMOVEABANDONED,
          											 PROP_REMOVEABANDONEDTIMEOUT, PROP_LOGABANDONED, 
          											 PROP_POOLPREPAREDSTATEMENTS, PROP_MAXOPENPREPAREDSTATEMENTS,
          											 PROP_CONNECTIONPROPERTIES };

    private static final Logger logger = LoggerFactory.getLogger(EncryptDataSourceFactory.class);
//    private static String dsFactoryName = null;
    
	@Override
	public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable<?, ?> environment) throws Exception {
		
//		dsFactoryName = name.toString();
		
		if((obj == null) || !(obj instanceof Reference)) return null;

		Reference ref = (Reference) obj;
		if(!"javax.sql.DataSource".equals(ref.getClassName())) return null;
		
		Properties properties = new Properties();
		for(int i=0; i<ALL_PROPERTIES.length; i++) {
			String propertyName = ALL_PROPERTIES[i];
			RefAddr ra = ref.get(propertyName);
			
			if(ra != null) {
				String propertyValue = ra.getContent().toString();
				properties.setProperty(propertyName, propertyValue);
			}
		}
		
		return createDataSource(properties);
	}
	
	public static DataSource createDataSource(Properties properties) throws Exception {
		BasicDataSource dataSource = new BasicDataSource();
		String value = null;
//		StringBuilder trace = null;
		
		value = properties.getProperty(PROP_DRIVERCLASSNAME);
		if(value != null) dataSource.setDriverClassName(value);

		value = properties.getProperty(PROP_URL);
		if(value != null) dataSource.setUrl(decryptDBCPProperty(value));
		
		value = properties.getProperty(PROP_USERNAME);
		if(value != null) dataSource.setUsername(decryptDBCPProperty(value));
		
		value = properties.getProperty(PROP_PASSWORD);
		if(value != null) dataSource.setPassword(decryptDBCPProperty(value));
		
		value = properties.getProperty(PROP_DEFAULTAUTOCOMMIT);
		if(value != null) dataSource.setDefaultAutoCommit(Boolean.valueOf(value));
		
		value = properties.getProperty(PROP_DEFAULTREADONLY);
		if(value != null) dataSource.setDefaultReadOnly(Boolean.valueOf(value));
		

        value = properties.getProperty(PROP_DEFAULTTRANSACTIONISOLATION);
        if (value != null) {
           int level = UNKNOWN_TRANSACTIONISOLATION;
           if ("NONE".equalsIgnoreCase(value)) level = Connection.TRANSACTION_NONE;
           else if ("READ_COMMITTED".equalsIgnoreCase(value)) level = Connection.TRANSACTION_READ_COMMITTED;
           else if ("READ_UNCOMMITTED".equalsIgnoreCase(value)) level = Connection.TRANSACTION_READ_UNCOMMITTED;
           else if ("REPEATABLE_READ".equalsIgnoreCase(value)) level = Connection.TRANSACTION_REPEATABLE_READ;
           else if ("SERIALIZABLE".equalsIgnoreCase(value)) level = Connection.TRANSACTION_SERIALIZABLE;
           else {
        	   try {
        		   level = Integer.parseInt(value);
        	   } catch (NumberFormatException e) {
        		   logger.info("Could not parse defaultTransactionIsolation: " + value);
        		   logger.info("WARNING: defaultTransactionIsolation not set");
        		   logger.info("using default value of database driver");
        		   level = UNKNOWN_TRANSACTIONISOLATION;
        	   }
           }
           
           dataSource.setDefaultTransactionIsolation(level);
        }
		
        value = properties.getProperty(PROP_DEFAULTCATALOG);
		if(value != null) dataSource.setDefaultCatalog(value);
		
		value = properties.getProperty(PROP_MAXACTIVE);
		if(value != null) dataSource.setMaxActive(Integer.parseInt(value));
		
		value = properties.getProperty(PROP_MAXIDLE);
		if(value != null) dataSource.setMaxIdle(Integer.parseInt(value));
		
		value = properties.getProperty(PROP_MINIDLE);
		if(value != null) dataSource.setMinIdle(Integer.parseInt(value));
		
		value = properties.getProperty(PROP_INITIALSIZE);
		if(value != null) dataSource.setInitialSize(Integer.parseInt(value));
		
		value = properties.getProperty(PROP_MAXWAIT);
		if(value != null) dataSource.setMaxWait(Long.parseLong(value));
		
		value = properties.getProperty(PROP_TESTONBORROW);
		if(value != null) dataSource.setTestOnBorrow(Boolean.parseBoolean(value));
		
		value = properties.getProperty(PROP_TESTONRETURN);
		if(value != null) dataSource.setTestOnReturn(Boolean.parseBoolean(value));
		
		value = properties.getProperty(PROP_TIMEBETWEENEVICTIONRUNSMILLIS);
		if(value != null) dataSource.setTimeBetweenEvictionRunsMillis(Long.parseLong(value));
		
		value = properties.getProperty(PROP_NUMTESTSPEREVICTIONRUN);
		if(value != null) dataSource.setNumTestsPerEvictionRun(Integer.parseInt(value));
		
		value = properties.getProperty(PROP_MINEVICTABLEIDLETIMEMILLIS);
		if(value != null) dataSource.setMinEvictableIdleTimeMillis(Long.parseLong(value));
		
		value = properties.getProperty(PROP_TESTWHILEIDLE);
		if(value != null) dataSource.setTestWhileIdle(Boolean.parseBoolean(value));
		
		value = properties.getProperty(PROP_VALIDATIONQUERY);
		if(value != null) dataSource.setValidationQuery(value);
		
		value = properties.getProperty(PROP_ACCESSTOUNDERLYINGCONNECTIONALLOWED);
		if(value != null) dataSource.setAccessToUnderlyingConnectionAllowed(Boolean.parseBoolean(value));
		
		value = properties.getProperty(PROP_REMOVEABANDONED);
		if(value != null) dataSource.setRemoveAbandoned(Boolean.parseBoolean(value));
		
		value = properties.getProperty(PROP_REMOVEABANDONEDTIMEOUT);
		if(value != null) dataSource.setRemoveAbandonedTimeout(Integer.parseInt(value));
		
		value = properties.getProperty(PROP_LOGABANDONED);
		if(value != null) dataSource.setLogAbandoned(Boolean.parseBoolean(value));
		
		value = properties.getProperty(PROP_POOLPREPAREDSTATEMENTS);
		if(value != null) dataSource.setPoolPreparedStatements(Boolean.parseBoolean(value));
		
		value = properties.getProperty(PROP_MAXOPENPREPAREDSTATEMENTS);
		if(value != null) dataSource.setMaxOpenPreparedStatements(Integer.parseInt(value));
		
		value = properties.getProperty(PROP_CONNECTIONPROPERTIES);
		if(value != null) {
			Properties p = getProperties(value);
			Enumeration<?> e = p.propertyNames();
			while(e.hasMoreElements()) {
				String propertyName = (String) e.nextElement();
				dataSource.addConnectionProperty(propertyName, p.getProperty(propertyName));
			}
		}
		
		return dataSource;
	}
	
	
	private static Properties getProperties(String propText) throws Exception {
		Properties p = new Properties();
		if(propText != null) {
			p.load(new ByteArrayInputStream(propText.replace(';', '\n').getBytes()));
		}
		
		return p;
	}
	
	/**
	 * 암호화 된 DBCP Property를 복호화 한다.
	 * 
	 * @param encryptStr String 암호화 된 Config 값
	 * @return String 복호화 된 Config 값
	 */
	private static String decryptDBCPProperty(String encryptStr) {
		
		String decryptStr = null;
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			decryptStr = new String(decoder.decodeBuffer(encryptStr));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return decryptStr;
	}

}
