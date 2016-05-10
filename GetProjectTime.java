//  File:  GetProjectTime.java

import java.sql.*;
import org.apache.derby.jdbc.ClientDriver;
import java.util.Scanner;

public class GetProjectTime
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
         // First, get the major.
         String project;
         Scanner scannerObject = new Scanner(System.in);
         System.out.print("Enter a Project: ");
         project = scannerObject.next( );

         Statement stmt = conn.createStatement();
         String qry = "select sum(t.HoursWorked) as TotalTime "
                      + "from task t, project p "
                      + "where ProjId = PID "
                      + "and p.Name = '" + project + "'";
         ResultSet rs = stmt.executeQuery(qry);

         // Step 3: loop through the result set if the name can be found
         System.out.format("%-10s  %4s%n", "Project Name", "Total Time Spent");

         while (rs.next())
         {
            int totaltime = rs.getInt("TotalTime");
            System.out.format("%-10s  %4d%n", project, totaltime);
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
