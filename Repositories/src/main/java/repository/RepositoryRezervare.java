package repository;

import models.Rezervare;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RepositoryRezervare implements IRepository<Integer, Rezervare> {
    private JDBCUtils jdbcUtils;
   // private static final Logger logger= LogManager.getLogger();

    public RepositoryRezervare(Properties properties){
      //  logger.info("Initializing RepositoryRezervare");
        jdbcUtils=new JDBCUtils(properties);
    }
    @Override
    public int size(){
        Connection con=jdbcUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("select count(*) as [SIZE] from Rezervare")) {
            try(ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                   // logger.traceExit(result.getInt("SIZE"));
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
    public void save(Rezervare rez) {
        //logger.traceEntry("saving Rezervare: {} ",rez);
        Connection con=jdbcUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into Rezervare(CursaId,NumeClient,NrLocuriRezervate) values (?,?,?)")){
            //preStmt.setInt(1,rez.getRezervareid());
            preStmt.setInt(1,rez.getCursaid());
            preStmt.setString(2,rez.getNumeclient());
            preStmt.setInt(3,rez.getNrlocurirezervate());

            int result=preStmt.executeUpdate();
        }catch (SQLException ex){
          //  logger.error(ex);
            System.out.println("Error DB "+ex);
        }
       // logger.traceExit();

    }


    @Override
    public Rezervare findOne(Integer id){
       // logger.traceEntry("finding Rezervare with id {} ",id);
        Connection con=jdbcUtils.getConnection();

        try(PreparedStatement preStmt=con.prepareStatement("select * from Rezervare where RezervareId=?")){
            preStmt.setInt(1,id);
            try(ResultSet result=preStmt.executeQuery()) {
                if (result.next()) {

                    Integer idr=result.getInt("RezervareId");
                    Integer idc=result.getInt("CursaId");
                    String client=result.getString("NumeClient");
                    Integer nr=result.getInt("NrLocuriRezervate");

                    Rezervare rez=new Rezervare(idr,idc,client,nr);
                   // logger.traceExit(rez);
                    return rez;
                }
            }
        }catch (SQLException ex){
           // logger.error(ex);
            System.out.println("Error DB "+ex);
        }
       // logger.traceExit("Rezervare with id {} not found", id);

        return null;
    }

    @Override
    public Rezervare delete(Integer id){
       // logger.traceEntry("deleting Rezervare with id {}",id);
        Connection con=jdbcUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("delete from Rezervare where RezervareId=?")){
            preStmt.setInt(1,id);
            int result=preStmt.executeUpdate();
        }catch (SQLException ex){
           // logger.error(ex);
            System.out.println("Error DB "+ex);
        }
      //  logger.traceExit();
        return findOne(id);
    }

    @Override
    public List<Rezervare> getAll(){
        Connection con=jdbcUtils.getConnection();
        List<Rezervare> destinatii=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from Rezervare")) {
            try(ResultSet result=preStmt.executeQuery()) {
                while (result.next()) {
                    Integer idr=result.getInt("RezervareId");
                    Integer idc=result.getInt("CursaId");
                    String client=result.getString("NumeClient");
                    Integer nr=result.getInt("NrLocuriRezervate");

                    Rezervare rez=new Rezervare(idr,idc,client,nr);
                    destinatii.add(rez);
                }
            }
        } catch (SQLException e) {
           // logger.error(e);
            System.out.println("Error DB "+e);
        }
       // logger.traceExit(destinatii);
        return destinatii;
    }

    @Override
    public Rezervare update(Rezervare rez){
        //logger.traceEntry("updating Rezervare with: {} ",rez);
        Connection con=jdbcUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("update Rezervare set CursaId=?,NumeClient=?,NrLocuriRezervate=? where RezervareId=?")){

            preStmt.setInt(1,rez.getCursaid());
            preStmt.setString(2,rez.getNumeclient());
            preStmt.setInt(3,rez.getNrlocurirezervate());
            preStmt.setInt(4,rez.getRezervareid());

            int result=preStmt.executeUpdate();
           // logger.traceExit();
            return rez;
        }catch (SQLException ex){
          //  logger.error(ex);
            System.out.println("Error DB "+ex);
        }
      //  logger.traceExit();
        return null;

    }



}
