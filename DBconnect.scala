package jdbc

import java.sql.DriverManager
import java.sql.Connection

/**
 * Setting Database connection constants
 */
class DBConstants{
    def DRIVER: String = {"com.mysql.jdbc.Driver"}
    def URL: String = {"jdbc:mysql://localhost:8889/ProductAndBilling"}
    def USERNAME: String = {"root"}
    def PASSWORD: String = {"root"}
}


class ScalaJdbcConnectSelect{

    // connect to the database named "mysql" on the localhost
    // Declare constants
    val Constants = new DBConstants()
    val driver = Constants.DRIVER
    val url = Constants.URL
    val username = Constants.USERNAME
    val password = Constants.PASSWORD

    // there's probably a better way to do this
    var connection:Connection = null
  
 def setConnection: Unit={
  
    try {
      // make the connection
      Class.forName(driver)
      connection = DriverManager.getConnection(url, username, password)
    } catch {
      case e=> e.printStackTrace
    }
  }   //setConnection end
  
  def giveConnection: Connection = { connection }
    
  def ExecQuery(connection: Connection, Query: String): Unit = {
         // create the statement, and run the select query
      val statement = connection.createStatement()
      val resultSet = statement.executeQuery(Query)

      while ( resultSet.next() ) {
        val host = resultSet.getString("")
        val user = resultSet.getString("")
      }  
 }
 def CloseConnection: Unit={  connection.close() }
  
}
