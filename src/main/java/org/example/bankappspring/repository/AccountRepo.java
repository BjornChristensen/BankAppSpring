package org.example.bankappspring.repository;

import org.example.bankappspring.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Repository
public class AccountRepo {
  @Autowired
  DataSource dataSource;

  public ArrayList<Account> getAllAccounts() {
    ArrayList<Account> list = new ArrayList<>();
    String sql = "SELECT * FROM account";
    try (Connection con = dataSource.getConnection()) {
      PreparedStatement ps = con.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        int accNo = rs.getInt("accountNo");
        String owner = rs.getString("owner");
        double balance = rs.getDouble("balance");
        list.add(new Account(accNo, owner, balance));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return list;
  }

  public Account getAccount(int accountNo) {
    Account acc=null;
    String sql = "SELECT * FROM account WHERE accountNo=?";
    try (Connection con = dataSource.getConnection()) {
      PreparedStatement ps = con.prepareStatement(sql);
      ps.setInt(1, accountNo);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        int accNo = rs.getInt("accountNo");
        String owner = rs.getString("owner");
        double balance = rs.getDouble("balance");
        acc=new Account(accNo, owner, balance);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return acc;
  }

  public void deposit(int accountNo, double amount){
    String sql="UPDATE account SET balance=balance+? WHERE accountNo=?;";
    try (Connection con = dataSource.getConnection()) {
      PreparedStatement ps = con.prepareStatement(sql);
      ps.setDouble(1, amount);
      ps.setInt(2, accountNo);
      ps.executeUpdate();
    } catch (SQLException e) { e.printStackTrace(); }
  }

  public void withdraw(int accountNo, double amount){
    String sql="UPDATE account SET balance=balance-? WHERE accountNo=?;";
    try (Connection con = dataSource.getConnection()) {
      PreparedStatement ps = con.prepareStatement(sql);
      ps.setDouble(1, amount);
      ps.setInt(2, accountNo);
      ps.executeUpdate();
    } catch (SQLException e) { e.printStackTrace(); }
  }

  public void transfer(int accountNoFrom, int accountNoTo, double amount){
    String sql1="UPDATE account SET balance=balance-? WHERE accountNo=?;";
    String sql2="UPDATE account SET balance=balance+? WHERE accountNo=?;";
    try (Connection con = dataSource.getConnection()) {
      PreparedStatement ps1 = con.prepareStatement(sql1);
      ps1.setDouble(1, amount);
      ps1.setInt(2, accountNoFrom);
      ps1.executeUpdate();

      PreparedStatement ps2 = con.prepareStatement(sql2);
      ps2.setDouble(1, amount);
      ps2.setInt(2, accountNoTo);
      ps2.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}