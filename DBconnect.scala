package jdbc

import java.sql.DriverManager
import java.sql.Connection
import scala.collections.mutable

/**
 * Setting Database connection constants
 */
class DBConstants{
    def DRIVER: String = {"com.mysql.jdbc.Driver"}
    def URL: String = {"jdbc:mysql://localhost:8889/ProductAndBilling"}
    def USERNAME: String = {"root"}
    def PASSWORD: String = {"root"}
}

class SystemConfiguration{
    
    val ServiceConfig = mutable.Map.empty[String, Boolean]
    val ConfigString = mutable.Map.empty[String, String]
    val ConfigInt = mutable.Map.empty[String, Int]
    val ConfigBool = mutable.Map.empty[String, Boolean]
    val ConfigFloat = mutable.Map.empty[String, Float]
    
    val PRODUCT = "PRODUCT"
    val CUSTOMER = "CUSTOMER"
    val PROJECT = "PROJECT"
    val FINANCE = "FINANCE"
    
    def getService(ServiceCode: String): Boolean={ServiceConfig(ServiceCode)}
}

class ScalaJdbcConnectSelect{

    // connect to the database named "mysql" on the localhost
    // Declare constants
    val Constants = new DBConstants()
    val driver = Constants.DRIVER
    val url = Constants.URL
    val username = Constants.USERNAME
    val password = Constants.PASSWORD
    var connected = false

    // there's probably a better way to do this
    var connection:Connection = null
  
 def setConnection: Unit={
  
    try {
      // make the connection
      Class.forName(driver)
      connection = DriverManager.getConnection(url, username, password)
      connected = true
    } catch {
      case e=> e.printStackTrace
    }
  }   //setConnection end
    
 
  def getConnection: Connection = { connection }
  def isConnected: Boolean = { connected }
    
  def CloseConnection: Unit={  connection.close() }
 
}

class execQuery(connection: Connection, SQLQuery: String) {
    
   val statement = connection.createStatement()
   val resultSet = statement.executeQuery(SQLQuery)
    
   def scrollCursor: Boolean={resultSet.next()}
   
   def getBoolean(ColumnValue: String): Boolean= {resultSet.getBoolean(ColumnValue)}
   def getString(ColumnValue: String): String= {resultSet.getString(ColumnValue) }
    
} 


class getBaseConfig() {
    
    val Services = new SystemConfiguration()
    var ConfigInit = false
    val DBCon = new ScalaJdbcConnectSelect()

    
   def getServices(): Unit={
      
       var String ConfigCode
       val QueryString="SELECT ConfigCode,ConfigBoolean " +
                       "FROM Configuration " +
                       "WHERE ConFigCode IN ('" + Services.PRODUCT + "','" +
                                                  Services.CUSTOMER + "' ) " +
                       "ORDER BY ConfigCode DESC"
            
       DBCon.setConnection
        
       if ( DBCon.isConnected ) {
          val getData = new execQuery(DBCon.getConnection, QueryString)
        
          while ( getData.scrollCursor ) {
             ConfigCode = getData.getString("ConfigCode")
             if ( ConfigCode == Services.PRODUCT ) {
                 Services.ServiceConfig(Services.PRODUCT) = getData.getBoolean("ConfigBoolean") 
             }
             if ( ConfigCode == Services.CUSTOMER ) {
                 Services.ServiceConfig(Services.CUSTOMER) = getData.getBoolean("ConfigBoolean")
             }    // scrollCursor
          }       // scrollCursor 
       }          // isConnected
    
       DBCon.CloseConnection
       ConfigInit = true
      
   }

   def CustomerService: Boolean= { Services.ServiceConfig(Services.CUSTOMER)}
   def ProductService: Boolean = { Services.ServiceConfig(Services.PRODUCT)}
}

