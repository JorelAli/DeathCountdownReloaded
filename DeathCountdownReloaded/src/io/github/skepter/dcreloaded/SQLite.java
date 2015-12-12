package io.github.skepter.dcreloaded;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SQLite {
	private String DatabaseURL;
	private Connection connection;

	public SQLite(File DatabaseFile) {
		if (!DatabaseFile.getParentFile().exists()) {
			DatabaseFile.getParentFile().mkdir();
		}
		this.DatabaseURL = ("jdbc:sqlite:" + DatabaseFile.getAbsolutePath());
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			System.out.println("No SQLite JDBC Driver available!");
			e.printStackTrace();
		}
	}

	public void open() throws SQLException {
		this.connection = DriverManager.getConnection(this.DatabaseURL);
	}

	public void close() throws SQLException {
		if (this.connection != null)
			this.connection.close();
	}

	public void execute(String Query) throws SQLException {
		this.connection.createStatement().execute(Query);
	}

	public ResultSet executeQuery(String Query) {
		Statement Statement = null;
		try {
			Statement = this.connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			return Statement.executeQuery(Query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			Statement.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return null;
	}

	public PreparedStatement prepareStatement(String Query) throws SQLException {
		return this.connection.prepareStatement(Query);
	}

	public ArrayList<String> resultToArray(ResultSet result, String data) {
		ArrayList<String> arr = new ArrayList<String>();
		try {
			while (result.next()) {
				arr.add(result.getString(data));
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try {
			result.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return arr;
	}

	public String resultToString(ResultSet result, String data) {
		try {
			if (result.next()) {
				return result.getString(data);
			}
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
