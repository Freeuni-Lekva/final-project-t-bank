import com.example.T_Bank.DAO.DatabaseConstants;
import org.apache.commons.dbcp2.BasicDataSource;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class BaseTest {
    public static BasicDataSource dataSource;
    public static Connection connection;

    public BaseTest() {

    }

    public Connection getConnection() throws SQLException {
        dataSource = new BasicDataSource();

        dataSource.setUrl("jdbc:mysql://localhost:3306/" + DatabaseConstants.testDBName);
        dataSource.setUsername(DatabaseConstants.databaseUsername);
        dataSource.setPassword(DatabaseConstants.databasePassword);
        connection = null;
        connection = dataSource.getConnection();

        return connection;
    }

    public void resetDB() throws FileNotFoundException, SQLException {
        InputStream in = new BufferedInputStream(new FileInputStream("create_test_db.sql"));
        Scanner scanner = new Scanner(in).useDelimiter("/\\*[\\s\\S]*?\\*/|--[^\\r\\n]*|;");
        Statement statement = null;

        try {
            statement = connection.createStatement();

            while (scanner.hasNext()) {
                String line = scanner.next().trim();

                if (!line.isEmpty())
                    statement.execute(line);
            }
        } finally {
            if (statement != null)
                statement.close();
        }
    }
}
