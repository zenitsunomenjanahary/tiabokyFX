package com.example.bibliotheque.Repository;

import com.example.bibliotheque.Database.DatabaseConnection;
import com.example.bibliotheque.Model.Lecteur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LecteurRepository {
    //All CRUD DATABASE OPERATIONS
    public ObservableList<Lecteur> getLecteurs(){
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection connection = databaseConnection.getConnection();

        ObservableList<Lecteur> listLecteur = FXCollections.observableArrayList();
        String query = "SELECT * FROM lecteurs";
        try{
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                listLecteur.add(new Lecteur(
                        Integer.parseInt(resultSet.getString("numero")),
                        resultSet.getString("nom"),
                        resultSet.getString("prenom"),
                        resultSet.getDate("naissance"),
                        resultSet.getString("adresse"),
                        resultSet.getString("email"),
                        resultSet.getString("telephone"),
                        Integer.parseInt(resultSet.getString("pretActuel")),
                        Integer.parseInt(resultSet.getString("nombrePret"))
                ));
            }
        }catch (Exception exception){
            exception.printStackTrace();
            exception.getCause();
        }
        return listLecteur;
    }

    public static void addLecteur(String nom, String prenom, String naissance, String adresse, String email, String telephone) {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection connection = databaseConnection.getConnection();
        String query = "INSERT INTO lecteurs (nom, prenom, naissance, adresse, email, telephone) " +
                "VALUES ('"+ nom + "','"+ prenom +"','"+ naissance +"','"+ adresse +"','"+ email + "','"+ telephone +"') ";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute();
        }catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public Lecteur getLecteurByNumero(String numero) throws SQLException {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection connection = databaseConnection.getConnection();
        String query = "SELECT * from lecteurs WHERE numero="+ numero;
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery(query);
        Lecteur lecteur = null;

        while(resultSet.next()){
            lecteur = new Lecteur(
                    Integer.parseInt(resultSet.getString("numero")),
                    resultSet.getString("nom"),
                    resultSet.getString("prenom"),
                    resultSet.getDate("naissance"),
                    resultSet.getString("adresse"),
                    resultSet.getString("email"),
                    resultSet.getString("telephone"),
                    Integer.parseInt(resultSet.getString("pretActuel")),
                    Integer.parseInt(resultSet.getString("nombrePret"))
            );
        }

        return lecteur;
    }

    public void updateLecteur(String numero, String nom, String prenom, String naissance, String adresse, String email, String telephone) {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection connection = databaseConnection.getConnection();
        String query = "UPDATE lecteurs SET nom=?,prenom=?,naissance=?,adresse=?,telephone=?, email=? WHERE numero =" + numero;
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,nom);
            preparedStatement.setString(2,prenom);
            preparedStatement.setString(3,String.valueOf(naissance));
            preparedStatement.setString(4,adresse);
            preparedStatement.setString(5,telephone);
            preparedStatement.setString(6,email);

            preparedStatement.execute();
            Alert alert = new Alert(Alert.AlertType.INFORMATION,"Information du Lecteur Mis à jour!");
            alert.show();
        }catch (SQLException exception){
            exception.printStackTrace();
            exception.getCause();
        }
    }

    public void delete(Lecteur lecteur){
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection connection = databaseConnection.getConnection();
        String query = "DELETE FROM `lecteurs` WHERE numero = " + lecteur.getNumero();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute();
            Alert alert = new Alert(Alert.AlertType.INFORMATION,"Lecteur Supprimer ");
            alert.show();
        }catch (SQLException exception){
            exception.printStackTrace();
            exception.getCause();
        }
    }

    public void incrementerNbPret(String numero) throws SQLException {
        Lecteur lecteur = getLecteurByNumero(numero);
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection connection = databaseConnection.getConnection();
        String query = "UPDATE lecteurs SET pretActuel='"+ (lecteur.getPretActuel() + 1) +"',nombrePret='"+ (lecteur.getNombrePret() +1) +"'" +
                "WHERE numero='"+ numero +"'";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute();
        }catch (SQLException exception){
            exception.printStackTrace();
            exception.getCause();
        }
    }

    public ObservableList<String> getLecteursElligible() {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection connection = databaseConnection.getConnection();

        ObservableList<String> listLecteur = FXCollections.observableArrayList();
        String query = "SELECT * FROM lecteurs WHERE pretActuel < 3";
        try{
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                Lecteur lecteur = new Lecteur(
                            Integer.parseInt(resultSet.getString("numero")),
                        resultSet.getString("nom"),
                        resultSet.getString("prenom"),
                        resultSet.getDate("naissance"),
                        resultSet.getString("adresse"),
                        resultSet.getString("email"),
                        resultSet.getString("telephone"),
                        Integer.parseInt(resultSet.getString("pretActuel")),
                        Integer.parseInt(resultSet.getString("nombrePret"))
                        );
                listLecteur.add(lecteur.getNumero() + " " + lecteur.getNom() + " " + lecteur.getPrenom());
            }
        }catch (Exception exception){
            exception.printStackTrace();
            exception.getCause();
        }
        return listLecteur;
    }

    public void decrementerPretActuel(int numeroLecteur) {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection connection = databaseConnection.getConnection();
        String query = "UPDATE lecteurs SET pretActuel=(pretActuel-1) WHERE numero="+numeroLecteur;
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute();
        }catch (SQLException exception){
            exception.printStackTrace();
            exception.getCause();
        }
    }
}
