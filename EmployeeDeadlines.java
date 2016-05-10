//  File:  GetProjectTime.java

import java.sql.*;
import org.apache.derby.jdbc.ClientDriver;
import java.util.Scanner;

public class EmployeeDeadlines
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

         Scanner scannerObject = new Scanner(System.in);

         Statement stmt = conn.createStatement();
         String qry = "select distinct(e.FirstName), e.LastName, t.Name "
                      + "from employee e, task t"
                      + " where t.empid = e.eid"
                      + " and t.complete = true"
                      + " and t.deadline >= {fn TIMESTAMPADD(SQL_TSI_HOUR, t.HoursWorked, T.AssignDate)}";
         ResultSet rs = stmt.executeQuery(qry);

         // Step 3: loop through the result set if the name can be found
         System.out.format("%-10s  %4s  %-12s%n", "First Name", "Last Name", "Task Name");

         while (rs.next())
         {
            String fname = rs.getString("FIRSTNAME");
            String lname = rs.getString("LASTNAME");
            String tname = rs.getString("NAME");
            System.out.format("%-10s  %4s  %10s%n", fname,lname, tname);
	     }
	     rs.close();
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
