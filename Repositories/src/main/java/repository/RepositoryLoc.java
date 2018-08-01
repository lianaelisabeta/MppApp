package repository;

import models.Loc;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RepositoryLoc implements IRepository<Integer, Loc> {
    private JDBCUtils jdbcUtils;
   // private static final Logger logger= LogManager.getLogger();

    public RepositoryLoc(Properties properties){
        //logger.info("Initializing RepositoryLoc");
        jdbcUtils=new JDBCUtils(properties);
    }
    @Override
    public int size(){
        Connection con=jdbcUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("select count(*) as [SIZE] from Loc")) {
            try(ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    //logger.traceExit(result.getInt("SIZE"));
                    return result.getInt("SIZE");
                }
            }
        }catch(SQLException ex){
            //logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        return 0;
    }
    @Override
    public void save(Loc loc) {
        //logger.traceEntry("saving Loc: {} ",loc);
        Connection con=jdbcUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into Loc(NrLoc,NumeClient,CursaId) values (?,?,?)")){
            //preStmt.setInt(1,loc.getLocid());
            preStmt.setInt(1,loc.getNrloc());
            preStmt.setString(2,loc.getNumeclient());
            preStmt.setInt(3,loc.getCursaid());


            int result=preStmt.executeUpdate();
        }catch (SQLException ex){
            //logger.error(ex);
            System.out.println("Error DB "+ex);

        }
       // logger.traceExit();

    }

    @Override
    public Loc findOne(Integer id){
        //logger.traceEntry("finding Loc with id {} ",id);
        Connection con=jdbcUtils.getConnection();

        try(PreparedStatement preStmt=con.prepareStatement("select * from Loc where LocId=?")){
            preStmt.setInt(1,id);
            try(ResultSet result=preStmt.executeQuery()) {
                if (result.next()) {

                    Integer idl=result.getInt("LocId");
                    Integer nrloc= result.getInt("NrLoc");
                    String client=result.getString("NumeClient");
                    Integer idc=result.getInt("CursaId");


                    Loc loc= new Loc(idl,nrloc,client,idc);
                    //logger.traceExit(loc);
                    return loc;
                }
            }
        }catch (SQLException ex){
            //logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        //logger.traceExit("Loc with id {} not found", id);

        return null;
    }

    @Override
    public Loc delete(Integer id){
        //logger.traceEntry("deleting Loc with id {}",id);
        Connection con=jdbcUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("delete from Loc where LocId=?")){
            preStmt.setInt(1,id);
            int result=preStmt.executeUpdate();
        }catch (SQLException ex){
            //logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        //logger.traceExit();
        return findOne(id);
    }
    @Override
    public List<Loc> getAll(){
        Connection con=jdbcUtils.getConnection();
        List<Loc> locuri=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from Loc")) {
            try(ResultSet result=preStmt.executeQuery()) {
                while (result.next()) {
                    Integer idl=result.getInt("LocId");
                    Integer nrloc= result.getInt("NrLoc");
                    String client=result.getString("NumeClient");
                    Integer idc=result.getInt("CursaId");


                    Loc loc= new Loc(idl,nrloc,client,idc);
                    locuri.add(loc);
                }
            }
        } catch (SQLException e) {
            //logger.error(e);
            System.out.println("Error DB "+e);
        }
        //logger.traceExit(locuri);
        return locuri;
    }
    @Override
    public Loc update(Loc loc){
        //logger.traceEntry("updating Loc with: {} ",loc);
        Connection con=jdbcUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("update Loc set NrLoc=?,NumeClient=?,CursaId=? where LocId=?")){

            preStmt.setInt(1,loc.getNrloc());
            preStmt.setString(2,loc.getNumeclient());
            preStmt.setInt(3,loc.getCursaid());

            preStmt.setInt(4,loc.getLocid());


            int result=preStmt.executeUpdate();
            //logger.traceExit();
            return loc;
        }catch (SQLException ex){
            //logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        //logger.traceExit();
        return null;

    }

}
