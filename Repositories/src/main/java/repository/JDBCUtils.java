package repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCUtils  implements Serializable{
    private Properties jdbcProps;
   // private static final Logger logger= LogManager.getLogger();
    public JDBCUtils(){}
    public JDBCUtils(Properties properties){
        jdbcProps=properties;

    }
    private Connection instance=null;
    private Connection getNewConnection(){
       // logger.traceEntry();

        String driver=jdbcProps.getProperty("FirmaTransport.jdbc.driver");
        String url=jdbcProps.getProperty("FirmaTransport.jdbc.url");

        //logger.info("trying to connect to database ... {}",url);

        Connection con=null;
        try {
            Class.forName(driver);
           // logger.info("Loaded driver ...{}",driver);

            con=DriverManager.getConnection(url);
        } catch (ClassNotFoundException e) {
           // logger.error(e);
            System.out.println("Error loading driver "+e);
        } catch (SQLException e) {
           // logger.error(e);
            System.out.println("Error getting connection "+e);
        }
        return con;
    }

    public Connection getConnection(){
       // logger.traceEntry();
        try {
            if (instance==null || instance.isClosed())
                instance=getNewConnection();

        } catch (SQLException e) {
           // logger.error(e);
            System.out.println("Error DB "+e);
        }
      //  logger.traceExit(instance);
        return instance;
    }
}