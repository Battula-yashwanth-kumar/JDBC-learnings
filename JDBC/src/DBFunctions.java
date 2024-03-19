import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.sql.*;

public class DBFunctions {
    public Connection connect_to_db(String dbname, String user, String pass) {
        Connection con = null;
        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + dbname, user, pass);
            if (con != null) {
                System.out.println("DB Connected");
            } else {
                System.out.println("DB is not Connected");
            }

        } catch (Exception e) {
            System.out.println(e);

        }
        return con;
    }

    public void createTable(Connection con, String table_name) {
        Statement statement;
        try {
            String query = "Create table " + table_name + "(empid SERIAL,name varchar(200), address varchar(200),primary key(empid))";
            statement = con.createStatement();
            statement.executeUpdate(query);
            System.out.println("Table Created");

        } catch (Exception e) {
            System.out.println(e);

        }
    }

    public void insertData(Connection con, String table, String name, String address) {
        Statement statement;
        try {
            String query = String.format("insert into %s(name,address) values('%s','%s');", table, name, address);
            statement = con.createStatement();
            statement.executeUpdate(query);
            System.out.println("Row Inserted");
        } catch (Exception e) {
            System.out.println(e);

        }
    }

    public void insertDynamicDataUsingPrepareStatement(Connection con) throws SQLException, IOException {
        String q = "insert into %s (name,address) values(?,?)";
        PreparedStatement smt;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter table name");
        String table = br.readLine();
        System.out.println("Enter name");
        String name = br.readLine();
        System.out.println("Enter address");
        String address = br.readLine();

        // Create the query string by replacing %s with the table name
        String query = String.format(q, table);
        smt = con.prepareStatement(query);

        smt.setString(1, name);
        smt.setString(2, address);

        smt.executeUpdate();
        System.out.println("Inserted...");
    }

    public void createImages(Connection con) {
        try {
            String q = "create table images(id SERIAL , pic blob, primary key(id))";
            Statement statement = con.createStatement();
            statement.executeUpdate(q);

        } catch (Exception e) {

        }
    }

    public void createLongImages(Connection con) {
        try {
            String q = "create table images(id SERIAL , pic longblob, primary key(id))";
            Statement statement = con.createStatement();
            statement.executeUpdate(q);

        } catch (Exception e) {

        }
    }

    public void images(Connection con) {
        try {
            String q = "insert into images(pic) values(?)";
            PreparedStatement psmt = con.prepareStatement(q);
            FileInputStream fis = new FileInputStream("mypic.jpg");
            psmt.setBinaryStream(1, fis, fis.available());
            psmt.executeUpdate();
            System.out.println("done ...");
        } catch (Exception e) {
            System.out.println("Error !!");

        }
    }

    public void longimages(Connection con) throws IOException, SQLException {
        String q = "insert into images(pic) values(?)";
        PreparedStatement psmt = con.prepareStatement(q);
        JFileChooser jfc = new JFileChooser();
        jfc.showOpenDialog(null);
        File file = jfc.getSelectedFile() '
        FileInputStream fis = new FileInputStream(file);
        psmt.setBinaryStream(1, fis, fis.available());
        psmt.executeUpdate();

        JOptionPane.showMessageDialog(null, "Success");
    }

    public ImageIcon getImage(Connection con, int id) {
        ImageIcon icon = null;
        try {
            String q = "select pic from images where id=?";
            PreparedStatement smt = con.prepareStatement(q);
            smt.setInt(1, id);
            ResultSet set = smt.executeQuery();
            if (set.next()) {
                Blob b = set.getBlob("pic");
                InputStream is = b.getBinaryStream();
                Image image = ImageIO.read(is);
                icon = new ImageIcon(image);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return icon;
    }

    public void read_Data(Connection con, String table_name) {
        Statement statement;
        ResultSet re = null;
        try {
            String q = String.format("select * from %s", table_name);
            statement = con.createStatement();
            re = statement.executeQuery(q);
            while (re.next()) {
                System.out.println(re.getString("empid"));
                System.out.println(re.getString("name"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update_name(Connection con, String table_name, String old_name, String newname) {
        Statement statement;
        try {
            String q = String.format("update %s set name='%s' where name='%s'", table_name, old_name, newname);
            statement = con.createStatement();
            statement.executeUpdate(q);
            System.out.println("Data Updated");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void search_by_id(Connection con, String table_name, int id) {
        Statement statement;
        ResultSet rs = null;
        try {
            String q = String.format("select * from %s where empid=%s", table_name);
            statement = con.createStatement();
            rs = statement.executeQuery(q);
            while (rs.next()) {
                System.out.println(rs.getString("empid"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete_row_by_id(Connection con, String table_name, int id){
        Statement statement;
        try{
            String q =String.format("delete from %s where empid=%s",table_name,id);
            statement=con.createStatement();
            statement.executeUpdate(q);
            System.out.println("Data Deleted");
        }catch (Exception e){
            System.out.println(e);

        }
    }

    public void delete_table(Connection con, String table_name){
        Statement statement;
        try{
            String q = String.format("drop table %s",table_name);
            statement=con.createStatement();
            statement.executeUpdate(q);
            System.out.println("Table Deleted Successfully");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



}
