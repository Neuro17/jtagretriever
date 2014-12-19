package twitter;
import java.sql.*;

public class SupportedResultSetTypeTest {  // JDK 7 and above
	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		Class.forName("org.sqlite.JDBC");
		Connection conn = DriverManager.getConnection("jdbc:sqlite:sample.db");

		DatabaseMetaData md = conn.getMetaData();   // Get the database metadata
		// Verify ResultSet's type
		System.out.println("-- ResultSet Type --");
		System.out.println("Supports TYPE_FORWARD_ONLY: "
				+ md.supportsResultSetType(ResultSet.TYPE_FORWARD_ONLY));
		System.out.println("Supports TYPE_SCROLL_INSENSITIVE: "
				+ md.supportsResultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE));
		System.out.println("Supports TYPE_SCROLL_SENSITIVE: "
				+ md.supportsResultSetType(ResultSet.TYPE_SCROLL_SENSITIVE));

		// Verify ResultSet's concurrency
		System.out.println("-- ResultSet Concurrency --");
		System.out.println("Supports CONCUR_READ_ONLY: "
				+ md.supportsResultSetType(ResultSet.CONCUR_READ_ONLY));
		System.out.println("Supports CONCUR_UPDATABLE: "
				+ md.supportsResultSetType(ResultSet.CONCUR_UPDATABLE));

		// Verify ResultSet's cursor holdability
		System.out.println("-- ResultSet Cursor Holdability --");
		int holdability = md.getResultSetHoldability();  // Get the default holdability
		if (holdability == ResultSet.CLOSE_CURSORS_AT_COMMIT) {
			System.out.println("Default: CLOSE_CURSORS_AT_COMMIT");
		} else if (holdability == ResultSet.HOLD_CURSORS_OVER_COMMIT) {
			System.out.println("Default: ResultSet.HOLD_CURSORS_OVER_COMMIT");
		}
		System.out.println("Supports CLOSE_CURSORS_AT_COMMIT: "
				+ md.supportsResultSetType(ResultSet.CLOSE_CURSORS_AT_COMMIT));
		System.out.println("Supports HOLD_CURSORS_OVER_COMMIT: "
				+ md.supportsResultSetType(ResultSet.HOLD_CURSORS_OVER_COMMIT));
	}
}