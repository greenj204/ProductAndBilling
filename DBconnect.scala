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

class SystemConguration {
    
    val ServiceConfig = mutable.Map.empty[String, Int]
    val ConfigString = mutable.Map.empty[String, String]
    val ConfigInt = mutable.Map.empty[String, Int]
    val ConfigBool = mutable.Map.empty[String, Boolean]
    val ConfigFloat = mutable.Map.empty[String, Float]
    
    def getService(ServiceCode: String): Boolean=(ServiceConfig(ServiceCode))
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
   
   def getVal(ColumnValue: String): Any= {resultSet.getString(ColumnValue)}
    
}

class getBaseConfig() {
    
    val Service = new SystemConfiguration()
    var ConfigInit = false
    val DBCon = new ScalaJdbcConnectSelect()

    
   def getServices(): Unit={
        
       val Services = new SystemConfiguration()
       val getData =  new execQuery()
            
       val QueryString="SELECT ConfigCode,ConfigBoolean " +
                       "FROM Configuration " +
                       "WHERE ConFigCode IN ('PRODUCT', 'CUSTOMER' ) " +
                       "ORDER BY ConfigCode DESC"
            
       DBCon.setConnection
        
       if ( DBCon.isConnected ) {
          getData.execQuery(DBCon.getConnection, QueryString)
        
          if ( getData.scrollCursor ) { 
             Services.ServiceConfig("PRODUCT") = getData.getVal("ConfigBoolean")
             if ( getData.scrollCursor ) {
                 Services.SericeConfig("CUSTOMER") = getData.getVal("ConfigBoolean")
                 ConfigInit=true}
             }   // scrollCursor
          }      // scrollCursor 
      }          // isConnected
   }

   def CustomerService: Boolean= { Services.ServiceConfig("CUSTOMER")}
   def ProductService: Boolean = { Services.ServiceConfig("PRODUCT")}
}

