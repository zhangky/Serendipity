package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import RS.Info;

public class MySQL {
	private Connection con = null;

	public MySQL() {
		Statement st = null;
		ResultSet rs = null;
		try {
			con = DriverManager.getConnection(Info.DB_URL, Info.DB_USER,
					Info.DB_PASSWORD);
			st = con.createStatement();
			rs = st.executeQuery("SELECT VERSION()");

			if (rs.next()) {
				System.out.println(rs.getString(1));
			}
			st.close();

		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
	}

	public ResultSet query(String query) throws Exception {
		Statement st = null;
		ResultSet rs = null;
		st = con.createStatement();
		rs = st.executeQuery(query);
		return rs;
	}
}
