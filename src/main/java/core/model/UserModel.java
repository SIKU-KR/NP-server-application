package core.model;

import core.common.AppLogger;
import core.common.DBConnection;
import core.dto.response.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserModel {

    public Optional<User> checkUserExists(String username) {
        String sql = "SELECT * FROM User WHERE name = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new User(
                        resultSet.getString("name"),
                        resultSet.getInt("score")
                ));
            }
        } catch (SQLException e) {
            AppLogger.error(e.getMessage());
        }
        return Optional.empty();
    }

    public void createUser(String username) {
        String sql = "INSERT INTO User (name, score) VALUES (?, 0)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);
            statement.executeUpdate();
        } catch (SQLException e) {
            AppLogger.error(e.getMessage());
        }
    }

    public void deleteUser(String username) {
        String sql = "DELETE FROM User WHERE name = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);
            statement.executeUpdate();
        } catch (SQLException e) {
            AppLogger.error(e.getMessage());
        }
    }
}