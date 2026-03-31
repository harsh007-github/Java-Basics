import java.sql.*;
public  class Main{
    static void main() {
        Create.createDept("D9", "GBM");
//        Create.createDept("D8", "MnA");
        Read.getAllDept();
        Update.updateDept("D1", "Mergers");
        Read.getAllDept();
        Delete.deleteDept("D5");
        Read.getAllDept();
    }
}

//"C:\Program Files\Java\jdk-25.0.2\bin\java.exe"
//"-javaagent:C:\Users\khand\AppData\Local\Programs\IntelliJ IDEA 2025.3.3\lib\idea_rt.jar=54609"
//-Dfile.encoding=UTF-8
//-Dsun.stdout.encoding=UTF-8
//-Dsun.stderr.encoding=UTF-8
//-classpath "E:\Harsh\Java\Java Basics\out\production\Java Basics"
//Main
