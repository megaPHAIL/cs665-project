import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		System.out.println("Task Management Database");
		System.out.println("------------------------");
		
		DatabaseInterface db = new DatabaseInterface();
		try {
			db.connect();

			Scanner input = new Scanner(System.in);
			int choice = 1;
			while (choice != 0) {
				System.out.println("Choose the number of a query to run (0 to quit):");
				System.out.println("1. Departments in an organization");
				System.out.println("2. Current projects by organization / employee");
				System.out.println("3. Upcoming events for employee");
				System.out.println("4. Highest task completion time by organization");
				System.out.println("5. Time spent on project");
				System.out.println("6. Employees over expected time");
				System.out.println("7. Employees that have met all deadlines");
				System.out.println("8. Average task completion time by department");

				choice = input.nextInt();
				switch (choice) {
				case 1:
					db.departmentsByOrganization();
					break;

				case 2:
					db.currentProjects();
					break;

				case 3:
					db.upcomingEvents();
					break;

				case 4:
					db.highestTaskTime();
					break;

				case 5:
					db.projectTime();
					break;

				case 6:
					db.overExpectedTime();
					break;

				case 7:
					db.employeeDeadlines();
					break;

				case 8:
					db.averageTaskTimeByDepartment();
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.disconnect();
		}
	}
}
