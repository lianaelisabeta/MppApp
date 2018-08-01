package repository;

import models.Cursa;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RepositoryCursa implements IRepository<Integer, Cursa> {
    private JDBCUtils jdbcUtils;
   // private static final Logger logger= LogManager.getLogger();

    public RepositoryCursa(Properties properties){
       // logger.info("Initializing RepositoryCursa");
        jdbcUtils=new JDBCUtils(properties);
    }

    @Override
    public int size(){
        Connection con=jdbcUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("select count(*) as [SIZE] from Cursa")) {
            try(ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    //logger.traceExit(result.getInt("SIZE"));
                    return result.getInt("SIZE");
                }
            }
        }catch(SQLException ex){
           // logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        return 0;
    }

    @Override
    public void save(Cursa cursa) {
       // logger.traceEntry("saving Cursa: {} ",cursa);
        Connection con=jdbcUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into Cursa values (?,?,?,?,?)")){
            preStmt.setInt(1,cursa.getCursaid());
            preStmt.setInt(2,cursa.getDestinatieid());
            preStmt.setDate(3, Date.valueOf(cursa.getData().toString()));
            preStmt.setTime(4,cursa.getOra());
            preStmt.setInt(5,cursa.getNrlocuri());


            int result=preStmt.executeUpdate();
        }catch (SQLException ex){
           // logger.error(ex);
            System.out.println("Error DB "+ex);
        }
       // logger.traceExit();

    }
    @Override
    public Cursa findOne(Integer id){
       // logger.traceEntry("finding Cursa with id {} ",id);
        Connection con=jdbcUtils.getConnection();

        try(PreparedStatement preStmt=con.prepareStatement("select * from Cursa where CursaId=?")){
            preStmt.setInt(1,id);
            try(ResultSet result=preStmt.executeQuery()) {
                if (result.next()) {

                    Integer idc=result.getInt("CursaId");
                    Integer idd=result.getInt("DestinatieId");
                    Date date=result.getDate("Data");
                    Time ora=result.getTime("Data");
                    Integer nrloc=result.getInt("NrLocuri");

                   Cursa cursa= new Cursa(idc,idd,date,ora,nrloc);
                   // logger.traceExit(cursa);
                    return cursa;
                }
            }
        }catch (SQLException ex){
            //logger.error(ex);
            System.out.println("Error DB "+ex);
        }
      //  logger.traceExit("Cursa with id {} not found", id);

        return null;
    }

    @Override
    public Cursa delete(Integer id){
       //// logger.traceEntry("deleting Cursa with id {}",id);
        Connection con=jdbcUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("delete from Cursa where CursaId=?")){
            preStmt.setInt(1,id);
            int result=preStmt.executeUpdate();
        }catch (SQLException ex){
            //logger.error(ex);
            System.out.println("Error DB "+ex);
        }
       // logger.traceExit();
        return findOne(id);
    }

    @Override
    public List<Cursa> getAll(){
        Connection con=jdbcUtils.getConnection();
        List<Cursa> curse=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from Cursa")) {
            try(ResultSet result=preStmt.executeQuery()) {
                while (result.next()) {
                    Integer idc=result.getInt("CursaId");
                    Integer idd=result.getInt("DestinatieId");

                    Date date=result.getDate("Data");

                    Time ora=result.getTime("Data");
                    Integer nrloc=result.getInt("NrLocuri");

                    Cursa cursa= new Cursa(idc,idd,date,ora,nrloc);
                    curse.add(cursa);
                }
            }
        } catch (SQLException e) {
           // logger.error(e);
            System.out.println("Error DB "+e);
        }
       // logger.traceExit(curse);
        return curse;
    }

    @Override
    public Cursa update(Cursa cursa){
       // logger.traceEntry("updating Cursa with: {} ",cursa);
        Connection con=jdbcUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("update Cursa set DestinatieId=?,Data=?,NrLocuri=? where CursaId=?")){

            preStmt.setInt(1,cursa.getDestinatieid());
            Date date=cursa.getData();
            date.setTime(cursa.getOra().getTime());
            preStmt.setDate(2,cursa.getData());
            //preStmt.setTime(3,cursa.getOra());
            preStmt.setInt(3,cursa.getNrlocuri());
            preStmt.setInt(4,cursa.getCursaid());

            int result=preStmt.executeUpdate();
            //logger.traceExit();
            return cursa;
        }catch (SQLException ex){
           // logger.error(ex);
            System.out.println("Error DB "+ex);
        }
       // logger.traceExit();
        return null;

    }



}
