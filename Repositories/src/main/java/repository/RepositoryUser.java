package repository;

import models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class RepositoryUser implements IRepository<String, User> {
private JDBCUtils jdbcUtils;
//private static final Logger logger= LogManager.getLogger();

public RepositoryUser(Properties properties){
       // logger.info("Initializing RepositoryDestinatie");
        jdbcUtils=new JDBCUtils(properties);
        }

@Override
public void save(User user) {
       // logger.traceEntry("saving new User: {} ",user);
        Connection con=jdbcUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into User values (?,?,?)")){

            preStmt.setString(1,user.getId());
            preStmt.setString(2,user.getName());
            preStmt.setString(3,user.getPassword());

        int result=preStmt.executeUpdate();
        }catch (SQLException ex){
       // logger.error(ex);
        System.out.println("Error DB "+ex);
        }
        //logger.traceExit();

        }


@Override
public User findOne(String id){
       // logger.traceEntry("finding User with id {} ",id);
        Connection con=jdbcUtils.getConnection();

        try(PreparedStatement preStmt=con.prepareStatement("select * from User where UserId=?")){
        preStmt.setString(1,id);
        try(ResultSet result=preStmt.executeQuery()) {
        if (result.next()) {

        String uid=result.getString("UserId") ;
        String name=result.getString("Name");
        String pass=result.getString("Password");

        User user=new User(uid,name,pass);


        //logger.traceExit(user);
        return user;
        }
        }
        }catch (SQLException ex){
       // logger.error(ex);
        System.out.println("Error DB "+ex);
        }
       // logger.traceExit("User with id {} not found", id);

        return null;
        }

@Override
public User delete(String id){
       // logger.traceEntry("deleting User with id {}",id);
        Connection con=jdbcUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("delete from User where UserId=?")){
        preStmt.setString(1,id);
        int result=preStmt.executeUpdate();
        }catch (SQLException ex){
       // logger.error(ex);
        System.out.println("Error DB "+ex);
        }
        //logger.traceExit();
        return findOne(id);
        }

@Override
public List<User> getAll(){
        Connection con=jdbcUtils.getConnection();
        List<User> users =new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from User")) {
        try(ResultSet result=preStmt.executeQuery()) {
        while (result.next()) {

            String uid=result.getString("UserId");
            String name=result.getString("Name");
            String pass=result.getString("Password");

        User user=new User(uid,name,pass);
        users.add(user);
        }
        }
        } catch (SQLException e) {
        //logger.error(e);
        System.out.println("Error DB "+e);
        }
        //logger.traceExit(users);
        return users;
        }

@Override
public User update(User u){
        //logger.traceEntry("updating User with: {} ",u);
        Connection con=jdbcUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("update User set Name=?,Password=? where UserId=?")){

            preStmt.setString(3,u.getId());
            preStmt.setString(1,u.getName());
            preStmt.setString(2,u.getPassword());


        int result=preStmt.executeUpdate();
        //logger.traceExit();
        return u;
        }catch (SQLException ex){
        //logger.error(ex);
        System.out.println("Error DB "+ex);
        }
        //logger.traceExit();
        return null;

        }



@Override
    public int size(){
    return getAll().size();
}



        }

