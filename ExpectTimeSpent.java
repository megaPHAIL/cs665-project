//  File:  GetProjectTime.java

import java.sql.*;
import org.apache.derby.jdbc.ClientDriver;
import java.util.Scanner;

public class ExpectTimeSpent
{
   public static void main(String[] args)
   {
      Connection conn = null;
      try
      {
         // Step 1:  connect to database server
         Driver d = new ClientDriver();
         String url = "jdbc:derby://localhost:1674/taskdb"
                       + ";create=false";
         conn = d.connect(url, null);

         // Step 2: build and execute the query
         // First, get the Task Name.
         String task;
         Scanner scannerObject = new Scanner(System.in);
         System.out.print("Enter a Task Name: ");
         task = scannerObject.next( );

         Statement stmt = conn.createStatement();
         String qry = "select distinct(e.FirstName), e.LastName "
                      + "from employee e, task t"
                      + " where t.name = '" + task + "'"
                      + " and t.HoursWorked > t.HoursExpected";
         ResultSet rs = stmt.executeQuery(qry);

         // Step 3: loop through the result set if the name can be found
         System.out.format("%-10s  %4s%n", "First Name", "Last Name");

         while (rs.next())
         {
            String fname = rs.getString("FirstName");
            String lname = rs.getString("LastName");
            System.out.format("%-10s  %4d  %n", fname,lname);
         rs.close();
	     }
      }
      catch(SQLException e)
      {
         e.printStackTrace();
      }
      finally
      {
         // Step 4: Disconnect from the server
         try
         {
            if(conn != null)
               conn.close();
         }
         catch(SQLException e)
         {
            e.printStackTrace();
         }
      }
   }
}
