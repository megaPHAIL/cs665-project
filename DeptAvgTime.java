//  File:  InputStudentMajor.java

//  This is the modified program from Figure 8-2 on page 181 of the textbook.
//  But this time the user will enter a major and all students having that
//  major will have their student ID (SId) and the name displayed.
//
//  Once again, a format method will be used in place of the println method
//  and this program must execute on the server hosting the database server.

//  This file in the the directory ~kdjackso/cs665

import java.sql.*;
import org.apache.derby.jdbc.ClientDriver;
import java.util.Scanner;

public class DeptAvgTime
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
         String Dname;
         Scanner scannerObject = new Scanner(System.in);
         System.out.print("Enter a Departmant Name: ");
         Dname = scannerObject.next( );

         Statement stmt = conn.createStatement();
         String qry = "select avg(t.hoursworked) as Average "
                      + "from department d, employee e, task t "
                      + "where e.deptid = d.did "
                      + "and t.empid = e.eid "
                      + "and t.complete = true "
                      + "and d.name = '" + Dname + "'";
         ResultSet rs = stmt.executeQuery(qry);

         // Step 3: loop through the result set if the name can be found
         System.out.format("%-10s  %4s  %n", "Department", "Average");

         while (rs.next())
         {
            int avg = rs.getInt("Average");
            System.out.format("%-10s  %4d  %n", Dname, avg);
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
