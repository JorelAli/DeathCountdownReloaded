package io.github.skepter.dcreloaded;

import java.io.File;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SQLite {
	private String DatabaseURL;
	private Connection Connection;

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

	public void open() {
		try {
			this.Connection = DriverManager.getConnection(this.DatabaseURL);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		if (this.Connection != null) {
			try {
				this.Connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void execute(String Query) {
		try {
			this.Connection.createStatement().execute(Query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ResultSet executeQuery(String Query) {
		Statement Statement = null;
		try {
			Statement = this.Connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ResultSet Result = null;
		try {
			return Statement.executeQuery(Query);
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				Statement.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}

	public PreparedStatement prepareStatement(String Query) {
		try {
			return this.Connection.prepareStatement(Query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
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
