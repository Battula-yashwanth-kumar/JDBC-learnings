import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, IOException {
        DBFunctions db = new DBFunctions();
       Connection con = db.connect_to_db("JDBClearning","postgres","jhwhcjdhw");
//        db.createTable(con,"employee");
//        db.insertData(con,"employee","Yash","BRj fjecje");
        db.insertDynamicDataUsingPrepareStatement(con);
    }
}