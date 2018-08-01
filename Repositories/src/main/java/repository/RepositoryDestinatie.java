package repository;

import models.Destinatie;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RepositoryDestinatie implements IRepository<Integer, Destinatie> {
    private JDBCUtils jdbcUtils;
   // private static final Logger logger= LogManager.getLogger();

    public RepositoryDestinatie(Properties properties){
       // logger.info("Initializing RepositoryDestinatie");
        jdbcUtils=new JDBCUtils(properties);
    }
    @Override
    public int size(){
        Connection con=jdbcUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("select count(*) as [SIZE] from Destinatie")) {
            try(ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                   // logger.traceExit(result.getInt("SIZE"));
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
    public void save(Destinatie dest) {
       // logger.traceEntry("saving Destinatie: {} ",dest);
        Connection con=jdbcUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into Destinatie values (?,?,?,?)")){
            preStmt.setInt(1,dest.getDestinatieid());
            preStmt.setString(2,dest.getLocalitate());
            preStmt.setString(3,dest.getJudet());
            preStmt.setString(4,dest.getTara());

            int result=preStmt.executeUpdate();
        }catch (SQLException ex){
           //// logger.error(ex);
            System.out.println("Error DB "+ex);
        }
       // logger.traceExit();

    }


    @Override
    public Destinatie findOne(Integer id){
        //logger.traceEntry("finding Destinatie with id {} ",id);
        Connection con=jdbcUtils.getConnection();

        try(PreparedStatement preStmt=con.prepareStatement("select * from Destinatie where DestinatieId=?")){
            preStmt.setInt(1,id);
            try(ResultSet result=preStmt.executeQuery()) {
                if (result.next()) {

                    Integer idd=result.getInt("DestinatieId");
                    String loc=result.getString("Localitate");
                    String jud=result.getString("Judet");
                    String tara=result.getString("Tara");

                    Destinatie dest= new Destinatie(idd,loc,jud,tara);
                   // logger.traceExit(dest);
                    return dest;
                }
            }
        }catch (SQLException ex){
           // logger.error(ex);
            System.out.println("Error DB "+ex);
        }
       // logger.traceExit("Destinatie with id {} not found", id);

        return null;
    }

    @Override
    public Destinatie delete(Integer id){
       // logger.traceEntry("deleting Destinatie with id {}",id);
        Connection con=jdbcUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("delete from Destinatie where DestinatieId=?")){
            preStmt.setInt(1,id);
            int result=preStmt.executeUpdate();
        }catch (SQLException ex){
           // logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        //logger.traceExit();
        return findOne(id);
    }

    @Override
    public List<Destinatie> getAll(){
        Connection con=jdbcUtils.getConnection();
        List<Destinatie> destinatii=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from Destinatie")) {
            try(ResultSet result=preStmt.executeQuery()) {
                while (result.next()) {
                    Integer idd=result.getInt("DestinatieId");
                    String loc=result.getString("Localitate");
                    String jud=result.getString("Judet");
                    String tara=result.getString("Tara");
                    Destinatie dest= new Destinatie(idd,loc,jud,tara);
                    destinatii.add(dest);
                }
            }
        } catch (SQLException e) {
           // logger.error(e);
            System.out.println("Error DB "+e);
        }
        //logger.traceExit(destinatii);
        return destinatii;
    }

    @Override
    public Destinatie update(Destinatie d){
      //  logger.traceEntry("updating Destinatie with: {} ",d);
        Connection con=jdbcUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("update Destinatie set Localitate=?,Judet=?,Tara=? where DestinatieId=?")){

            preStmt.setString(1,d.getLocalitate());
            preStmt.setString(2,d.getJudet());
            preStmt.setString(3,d.getTara());
            preStmt.setInt(4,d.getDestinatieid());

            int result=preStmt.executeUpdate();
           // logger.traceExit();
            return d;
        }catch (SQLException ex){
           // logger.error(ex);
            System.out.println("Error DB "+ex);
        }
       // logger.traceExit();
        return null;

    }







}
