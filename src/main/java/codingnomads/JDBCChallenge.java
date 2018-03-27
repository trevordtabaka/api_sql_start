package codingnomads;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

/**
 * Create a new, simple, relational DB and integrate with that DB here in this class
 */
public class JDBCChallenge {

    private Connection connection = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    public static void main(String[] args) {
        JDBCChallenge jdbcChallenge = new JDBCChallenge();

//        try{
//            jdbcChallenge.readDataBase();
//        }catch (Exception e){
//            System.out.println("couldnt read database");
//        }
//        jdbcChallenge.insert("Caden", "Mackenzie", "caden@codingnomads.co", "2018-03-21", "2018-03-21");
        try{
            jdbcChallenge.readDataBase();
        }catch (Exception e){
            System.out.println("couldnt read database");
        }
//        jdbcChallenge.delete("Caden", "Mackenzie");
        jdbcChallenge.joinQuery("Trevor", "Tabaka");
        jdbcChallenge.joinQuery("Dave", "Jones");
        try{
            jdbcChallenge.readDataBase();
        }catch (Exception e){
            System.out.println("couldnt read database");
        }
    }


    // demonstrate a join query and map the results to another custom object
    public void joinQuery(String firstName, String lastName){

        //Call open() to get connection to database
        open();

        // Statements allow to issue SQL queries to the database
        try {
            statement = connection.createStatement();
            // Result set get the result of the SQL query
            preparedStatement = connection.prepareStatement("select c.f_name, c.l_name, p.name " +
                    "from clients c " +
                    "join clients_product cp " +
                    "on c.id = cp.client_id " +
                    "join product p " +
                    "on p.id = cp.product_id " +
                    "where c.f_name = ? AND c.l_name = ? ; ");
            // Parameters start with 1
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);

            resultSet = preparedStatement.executeQuery();


            mapResultSetToClientsProducts(resultSet);
        } catch (Exception e) {
            System.out.println("didnt join query right");
            e.printStackTrace();
        }finally {
            close();
        }


    }

    // insert a record from the db
    public void insert(String firstName, String lastName, String email, String joinDate, String lastActive) {
        //Call open() to get connection to database
        open();
        try {
            // Statements allow to issue SQL queries to the database
            statement = connection.createStatement();
            // PreparedStatements can use variables and are more efficient
            preparedStatement = connection
                    .prepareStatement("insert into  Client_List.clients (f_name, l_name, email, join_date, last_active) " +
                            "values (?, ?, ?,?,?)");
            // Parameters start with 1
            preparedStatement.setString(1, firstName);

            preparedStatement.setString(2, lastName);

            preparedStatement.setString(3, email);
            preparedStatement.setString(4, joinDate);
            preparedStatement.setString(5, lastActive);

            preparedStatement.executeUpdate();
        }catch (Exception sqlEx){
            System.out.println("Couldnt insert a record to database");
            System.out.println(sqlEx.toString());

        }finally {
            close();
        }

    }

    // delete a record from the db
    public void delete(String firstName, String lastName) {
        //Call open() to get connection to database
        open();
        try{
            //Remove again the insert comment
            preparedStatement = connection
                    .prepareStatement("delete from Client_List.clients where f_name = ? AND l_name = ? ; ");
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.executeUpdate();

        }catch (Exception sqlEx){
            System.out.println("Couldnt delete a record to database");
            System.out.println(sqlEx.toString());
        }finally {
            close();
        }




    }

    private ClientsProducts mapResultSetToClientsProducts(ResultSet resultSet) throws SQLException {
        // ResultSet is initially before the first data set
        ClientsProducts clientsProducts = new ClientsProducts();

        if(resultSet.next()){
            clientsProducts.setFirstName(resultSet.getString("f_name"));
            clientsProducts.setLastName(resultSet.getString("l_name"));
            clientsProducts.getProducts().add(resultSet.getString("name"));

        }
        while(resultSet.next()){

            clientsProducts.getProducts().add(resultSet.getString("name"));
        }

        System.out.println(clientsProducts.toString());
        return clientsProducts;

    }

    private ArrayList<Client> mapResultSetToClients(ResultSet resultSet) throws SQLException {
//
        ArrayList<Client> retList = new ArrayList();

//        // ResultSet is initially before the first data set
        while (resultSet.next()) {
            Client c = new Client();
            c.setId(resultSet.getInt("id"));
            c.setFirstName(resultSet.getString("f_name"));
            c.setLastName(resultSet.getString("l_name"));
            c.setEmail(resultSet.getString("email"));
            c.setJoinDate(resultSet.getString("join_date"));
            c.setLastActive(resultSet.getString("last_active"));

            retList.add(c);
        }
//
        return retList;
//    }

    }

    public void readDataBase()
            throws Exception {
        try {

            //Call open() to get connection to database
            open();


            // Statements allow to issue SQL queries to the database
            statement = connection.createStatement();

            // Result set get the result of the SQL query
            resultSet = statement.executeQuery("select * from Client_List.clients;");

            //writeResultSet(resultSet);
            ArrayList<Client> client = mapResultSetToClients(resultSet);

            for (Client c : client){
                System.out.println(c.toString());
            }

            preparedStatement = connection
                    .prepareStatement("SELECT * from Client_List.clients");
            resultSet = preparedStatement.executeQuery();

            writeResultSet(resultSet);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close();
        }

    }

    private void writeResultSet(ResultSet resultSet) throws SQLException {
        // ResultSet is initially before the first data set
        while (resultSet.next()) {
            // It is possible to get the columns via name
            // also possible to get the columns via the column number
            // which starts at 1
            // e.g. resultSet.getSTring(2);
            String id = resultSet.getString("id");
            String f_name = resultSet.getString("f_name");
            String l_name = resultSet.getString("l_name");
            String email = resultSet.getString("email");
            String joinDate = resultSet.getString("join_date");
            String lastActive = resultSet.getString("last_active");



            System.out.println("Client: " + f_name + " " + l_name);
            System.out.println("id: " + id);
            System.out.println("email: " + email);
            System.out.println("join date: " + joinDate);
            System.out.println("last active: " + lastActive);
            System.out.println("---------------------------------");
            System.out.println("---------------------------------");

        }
    }

    // You need to close the resultSet
    private void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }

            if (statement != null) {
                statement.close();
            }

            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {

        }
    }
    private void open(){
        // This will load the MySQL driver, each DB has its own driver
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        // Setup the connection with the DB
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/Client_List?" +
                    "user=root&password=Airhockey1&useSSL=false");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}