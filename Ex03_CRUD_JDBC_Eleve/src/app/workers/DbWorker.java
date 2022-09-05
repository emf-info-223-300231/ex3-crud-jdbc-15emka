package app.workers;

import app.beans.Personne;
import app.exceptions.MyDBException;
import app.helpers.DateTimeLib;
import app.helpers.SystemLib;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DbWorker implements DbWorkerItf {

    private Connection dbConnexion;
    private List<Personne> listePersonnes;
    private int index = 0;

    /**
     * Constructeur du worker
     */
    public DbWorker() {
    }

    @Override
    public void connecterBdMySQL(String nomDB) throws MyDBException {
        final String url_local = "jdbc:mysql://localhost:3306/" + nomDB;
        final String url_remote = "jdbc:mysql://172.23.85.187:3306/" + nomDB;
        final String user = "223"; //"root";
        final String password = "emf123";
        //LAPEMFB37-21.edu.net.fr.ch

        System.out.println("url:" + url_remote);
        try {
            dbConnexion = DriverManager.getConnection(url_remote, user, password);
        } catch (SQLException ex) {
            throw new MyDBException(SystemLib.getFullMethodName(), ex.getMessage());
        }
    }

    @Override
    public void connecterBdHSQLDB(String nomDB) throws MyDBException {
        final String url = "jdbc:hsqldb:file:" + nomDB + ";shutdown=true";
        final String user = "SA";
        final String password = "";
        System.out.println("url:" + url);
        try {
            dbConnexion = DriverManager.getConnection(url, user, password);
        } catch (SQLException ex) {
            throw new MyDBException(SystemLib.getFullMethodName(), ex.getMessage());
        }
    }

    @Override
    public void connecterBdAccess(String nomDB) throws MyDBException {
        final String url = "jdbc:ucanaccess://" + nomDB;
        System.out.println("url=" + url);
        try {
            dbConnexion = DriverManager.getConnection(url);
        } catch (SQLException ex) {
            throw new MyDBException(SystemLib.getFullMethodName(), ex.getMessage());
        }
    }

    @Override
    public void deconnecter() throws MyDBException {
        try {
            if (dbConnexion != null) {
                dbConnexion.close();
            }
        } catch (SQLException ex) {
            throw new MyDBException(SystemLib.getFullMethodName(), ex.getMessage());
        }
    }

    @Override
    public List<Personne> lirePersonnes() throws MyDBException {
        listePersonnes = new ArrayList<>();
        try {
            Statement st = dbConnexion.createStatement();
            ResultSet rs
                    = st.executeQuery("select PK_PERS, Nom, Prenom, Date_naissance, No_rue, Rue, NPA, Ville, Actif, Salaire, date_modif from t_personne");

            while (rs.next()) {
                Personne newPersonne = new Personne(rs.getInt("PK_PERS"), 
                        rs.getString("Nom"), 
                        rs.getString("Prenom"),
                        rs.getDate("Date_naissance"), 
                        rs.getInt("No_rue"), rs.getString("Rue"), 
                        rs.getInt("NPA"),
                        rs.getString("Ville"), 
                        rs.getBoolean("Actif"), 
                        rs.getDouble("Salaire"), 
                        rs.getDate("date_modif"));
                listePersonnes.add(newPersonne);
            }
        } catch (SQLException ex) {
            throw new MyDBException(SystemLib.getFullMethodName(), ex.getMessage());
        }
        return listePersonnes;
    }

    @Override
    public void effacer(Personne p) throws MyDBException {
        String prep = "DELETE FROM t_personne WHERE PK_PERS=?";

        try ( PreparedStatement ps = dbConnexion.prepareStatement(prep)) {

            ps.setInt(1, p.getPkPers());

            int nb = ps.executeUpdate();

            if (nb != 1) {
                System.out.println("Erreur de mise à jour !!!");
            }
        } catch (SQLException ex) {
            throw new MyDBException(SystemLib.getFullMethodName(), ex.getMessage());
        }
    }

    @Override
    public Personne lire(int i) throws MyDBException {
        return listePersonnes.get(i);
    }

    @Override
    public void modifier(Personne p) throws MyDBException {
        if (p != null) {
            String prep = "UPDATE t_personne SET Prenom=?, Nom=?, Date_naissance=?, No_rue=?, "
                    + "Rue=?, NPA=?, Ville=?, Actif=?, Salaire=?, date_modif=?, no_modif=? WHERE PK_PERS=?";

            try ( PreparedStatement ps = dbConnexion.prepareStatement(prep)) {

                ps.setString(1, p.getPrenom());
                ps.setString(2, p.getNom());
                ps.setDate(3, new java.sql.Date(p.getDateNaissance().getTime()));
                ps.setInt(4, p.getNoRue());
                ps.setString(5, p.getRue());
                ps.setInt(6, p.getNpa());
                ps.setString(7, p.getLocalite());
                ps.setBoolean(8, p.isActif());
                ps.setDouble(9, p.getSalaire());
                ps.setDate(10, new java.sql.Date(p.getDateModif().getTime()));
                ps.setInt(11, 1);
                ps.setInt(12, p.getPkPers());

                int nb = ps.executeUpdate();

                if (nb != 1) {
                    System.out.println("Erreur de mise à jour !!!");
                }
            } catch (SQLException ex) {
                throw new MyDBException(SystemLib.getFullMethodName(), ex.getMessage());
            }
        }
    }

    @Override
    public void creer(Personne p) throws MyDBException {
        if (p != null) {
            String prep="INSERT INTO t_personne (Nom, Prenom, Date_naissance, No_rue, Rue, "
                    + "NPA, Ville, Actif, Salaire, date_modif) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            try (PreparedStatement ps= dbConnexion.prepareStatement(prep)){
                
                ps.setString(1, p.getNom());
                ps.setString(2, p.getPrenom());
                ps.setDate(3, new java.sql.Date(p.getDateNaissance().getTime()));
                ps.setInt(4, p.getNoRue());
                ps.setString(5, p.getRue());
                ps.setInt(6, p.getNpa());
                ps.setString(7, p.getLocalite());
                ps.setBoolean(8, p.isActif());
                ps.setDouble(9, p.getSalaire());
                ps.setDate(10, new java.sql.Date(p.getDateModif().getTime()));
                
                
                int nb = ps.executeUpdate();
                
                if(nb!=1){
                    System.out.println("Erreur de mise à jour");
                }
                
                
                
            } catch (SQLException ex) {
                throw new MyDBException(SystemLib.getFullMethodName(), ex.getMessage());
            }
            
        }
    }

}
