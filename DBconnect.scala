package jdbc

import java.sql.DriverManager
import java.sql.Connection

/**
 * 
 */
class ScalaJdbcConnectSelect{

    // connect to the database named "mysql" on the localhost
    val driver = "com.mysql.jdbc.Driver"
    val url = "jdbc:mysql://localhost:8889/SimpleBookKeeping"
    val username = "root"
    val password = "root"

    // there's probably a better way to do this
    var connection:Connection = null
  
 def setConnection: Boolean={
    var PassFail:Boolean = true
    try {
      // make the connection
      Class.forName(driver)
      connection = DriverManager.getConnection(url, username, password)
    } catch {
      PassFail = false
      case e=> e.printStackTrace
    }
  }   //setConnection end
  
  def ExecQuery(connection: Connection, Query: String)  = {
         // create the statement, and run the select query
      val statement = connection.createStatement()
      val resultSet = statement.executeQuery(Query)
/*"SELECT BookCodeDebitAcc, BookCodeCreditAcc FROM BookCodes " +
                                             "WHERE BookCode = 'GENEXP' ")
                                             */
      while ( resultSet.next() ) {
        val host = resultSet.getString("BookCodeDebitAcc")
        val user = resultSet.getString("BookCodeCreditAcc")
        println("host, user = " + host + ", " + user)
      }  
 }
 def CloseConnection Unit={  connection.close() }
  
}
