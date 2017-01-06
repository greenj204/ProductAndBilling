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
    
  def CloseConnection: Unit={  connection.close() }
  
}

class getBaseConfig() {
    
    var ProductConfig = false
    var CustomerConfig = false
    val DBCon = new ScalaJdbcConnectSelect()
    
    DBCon.setConnection
    
    def queryBaseVals(): Unit={
    
        val getData =  new execQuery()
        
        val QueryString="SELECT ConfigCode,ConfigBoolean " +
                        "FROM Configuration " +
                        "WHERE ConFigCode IN ('PRODUCT', 'CUSTOMER' ) " +
                        "ORDER BY ConfigCode DESC"
        
        getData.execQuery(DBCon.giveConnection, QueryString)
        
        if ( getData.scrollCursor ) { ProductConfig = getData.getVal("ConfigBoolean") }
        if ( getData.scrollCursor ) { CustomerConfig = getData.getVal("ConfigBoolean") }
        
        DBCon.CloseConnection
    }
    
    def getProduct: Boolean= {ProductConfig}
    def getCustomer: Boolean= {CustomerConfig}
}

class execQuery(connection: Connection, SQLQuery: String) {

   val statement = connection.createStatement()
   val resultSet = statement.executeQuery(SQLQuery)
    
   def scrollCursor: Boolean={resultSet.next()}
   
   def getVal(ColumnValue: String): Any= {resultSet.getString(ColumnValue)}
    
 }
