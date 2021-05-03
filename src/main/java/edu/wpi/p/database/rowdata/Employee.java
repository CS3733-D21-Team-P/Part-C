package edu.wpi.p.database.rowdata;

public class Employee {
    private String Name;
    private String EmployeeID;
    private String Position;
    private String Salary;
    private String AssignedSR;

    public Employee(String Name, String EmployeeID, String Position, String Salary, String AssignedSR) {
        this.Name = Name;
        this.EmployeeID = EmployeeID;
        this.Position = Position;
        this.Salary = Salary;
        this.AssignedSR = AssignedSR;
    }

    public String getAssignedSR() {
        return AssignedSR;
    }

    public void setAssignedSR(String assignedSR) {
        AssignedSR = assignedSR;
    }

    public String getSalary() {
        return Salary;
    }

    public void setSalary(String salary) {
        Salary = salary;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmployeeID() {
        return EmployeeID;
    }

    public void setEmployeeID(String employeeID) {
        EmployeeID = employeeID;
    }

    public String getPosition() {
        return Position;
    }

    public void setPosition(String position) {
        Position = position;
    }
}
