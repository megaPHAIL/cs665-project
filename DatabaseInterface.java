import java.sql.*;
import org.apache.derby.jdbc.ClientDriver;
import java.util.Scanner;

public class DatabaseInterface {

	private Connection conn;
	private Scanner input;
	private Statement stmt;
	
	public DatabaseInterface() {
		input = new Scanner(System.in);
	}
	
	public void connect() {
		try {
			Driver d = new ClientDriver();
			String url = "jdbc:derby://localhost:1674/taskdb;create=false";
			conn = d.connect(url, null);

			// setup statement for executing queries
			stmt = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void departmentsByOrganization() {
		try {
			String name;
			System.out.print("Enter an organization name: ");
			name = input.nextLine();

			String qry = " select d.* "
						+ "from ORGANIZATION o, DEPARTMENT d "
						+ "where o.OID = d.OrgID "
						+ "and o.Name = '" + name + "'";
			ResultSet rs = stmt.executeQuery(qry);

			System.out.format("%-4s  %-10s  %n", "ID", "Name");

			while (rs.next()) 
				System.out.format("%-4d  %-10s  %n", rs.getInt("DID"), rs.getString("Name"));
			
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void currentProjects() {
		try {
			String name, qry;
			ResultSet rs;
			
			System.out.println("Choose category to search by:");
			System.out.println("1. Organization");
			System.out.println("2. Employee");
			switch (input.nextInt()) {
			case 1:
				System.out.print("Enter an organization name: ");
				input.nextLine();
				name = input.nextLine();

				qry = "select distinct p.* "
					+ "from ORGANIZATION o, DEPARTMENT d, EMPLOYEE e, TASK t, PROJECT p "
					+ "where o.OID = d.OrgID and d.DID = e.DeptID and e.EID = t.EmpID and t.ProjID = p.PID "
					+ "and o.Name = '" + name + "'";
				rs = stmt.executeQuery(qry);
				break;
				
			case 2:
				System.out.print("Enter an employee's last name: ");
				name = input.next();

				qry = "select distinct p.* "
					+ "from EMPLOYEE e, TASK t, PROJECT p "
					+ "where e.EID = t.EmpID and t.ProjID = p.PID "
					+ "and e.LastName = '" + name + "'";
				rs = stmt.executeQuery(qry);
				break;
				
			default:
				System.out.println("Invalid selection");
				return;
			}
			
			System.out.format("%-4s  %-20s  %-20s  %n", "ID", "Name", "Description");

			while (rs.next()) {
				System.out.format("%-4d  %-20s  %-20s  %n", rs.getInt("PID"), rs.getString("Name"), rs.getString("Description"));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void upcomingEvents() {
		try {
			String name;
			System.out.print("Enter an employee's last name: ");
			name = input.nextLine();

			String qry = " select v.* "
						+ "from EMPLOYEE e, EVENT_ASSIGNMENT a, EVENT v "
						+ "where e.EID = a.EmployeeID and a.EventID = v.EvID "
						+ "and e.LastName = '" + name + "'";
			ResultSet rs = stmt.executeQuery(qry);

			System.out.format("%-4s  %-10s  %-10s  %n", "ID", "Name", "Start");

			while (rs.next()) 
				System.out.format("%-4d  %-10s  %-10tD  %n", rs.getInt("EvID"), rs.getString("Name"), rs.getDate("StartTime"));
			
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void highestTaskTime() {
		try {
			String name;
			System.out.print("Enter an organization name: ");
			name = input.nextLine();
			
			String qry = " select e.EID, e.FirstName, e.LastName, avg(t.HoursWorked) as Average "
						+ "from ORGANIZATION o, DEPARTMENT d, EMPLOYEE e, TASK t "
						+ "where o.OID = d.OrgID and d.DID = e.DeptID and e.EID = t.EmpID and t.Complete = true "
						+ "and o.Name = '" + name + "' "
						+ "group by e.EID, e.LastName, e.FirstName "
						+ "order by Average";
			ResultSet rs = stmt.executeQuery(qry);

			System.out.format("%-4s  %-10s  %-10s  %-4s  %n", "ID", "First", "Last", "Average");

			while (rs.next())
				System.out.format("%-4d  %-10s  %-10s  %-4d  %n", rs.getInt("EID"), rs.getString("FirstName"),
						rs.getString("LastName"), rs.getInt("Average"));
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void averageTaskTimeByDepartment() {
		try {
			String name;
			System.out.print("Enter a Departmant Name: ");
			name = input.nextLine();

			String qry = " select avg(t.hoursworked) as Average "
						+ "from department d, employee e, task t "
						+ "where e.deptid = d.did "
						+ "and t.empid = e.eid "
						+ "and t.complete = true "
						+ "and d.name = '" + name + "'";
			ResultSet rs = stmt.executeQuery(qry);

			System.out.format("%-10s  %-4s  %n", "Department", "Average");

			while (rs.next()) {
				int avg = rs.getInt("Average");
				System.out.format("%-10s  %-4d  %n", name, avg);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void employeeDeadlines() {
        try {
			String qry = " select distinct e.FirstName, e.LastName, t.Name "
						+ "from employee e, task t"
						+ " where t.empid = e.eid"
						+ " and t.complete = true"
						+ " and t.deadline >= {fn TIMESTAMPADD(SQL_TSI_HOUR, t.HoursWorked, T.AssignDate)}";
			ResultSet rs = stmt.executeQuery(qry);

			System.out.format("%-10s  %-4s  %-10s%n", "First Name", "Last Name", "Task Name");

			while (rs.next()) {
				String fname = rs.getString("FIRSTNAME");
				String lname = rs.getString("LASTNAME");
				String tname = rs.getString("NAME");
				System.out.format("%-10s  %-4s  %-10s%n", fname, lname, tname);
			}
			rs.close();
        } catch (SQLException e) {
        	e.printStackTrace();
        }
	}
	
	public void overExpectedTime() {
		try {
			String task;
			System.out.print("Enter a Task Name: ");
			task = input.nextLine();

			String qry = " select distinct e.FirstName, e.LastName "
						+ "from employee e, task t "
						+ "where t.name = '" + task + "' "
						+ "and t.HoursWorked > t.HoursExpected";
			ResultSet rs = stmt.executeQuery(qry);

			System.out.format("%-10s  %-4s%n", "First Name", "Last Name");

			while (rs.next()) {
				String fname = rs.getString("FirstName");
				String lname = rs.getString("LastName");
				System.out.format("%-10s  %-4d  %n", fname, lname);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void projectTime() {
		try {
			String project;
			System.out.print("Enter a Project: ");
			project = input.nextLine();

			String qry = " select sum(t.HoursWorked) as TotalTime "
						+ "from task t, project p "
						+ "where ProjId = PID "
						+ "and p.Name = '" + project + "'";
			ResultSet rs = stmt.executeQuery(qry);

			// Step 3: loop through the result set if the name can be found
			System.out.format("%-10s  %-4s%n", "Project Name", "Total Time Spent");

			while (rs.next()) {
				int totaltime = rs.getInt("TotalTime");
				System.out.format("%-10s  %-4d%n", project, totaltime);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void disconnect() {
		try {
			input.close();
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}


