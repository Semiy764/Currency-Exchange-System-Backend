package org.example.repository.interfaces;
import org.example.enums.UserRole;
import org.example.model.User;
import java.util.List;

public interface UserRepsitory {
    User save(User user);

    User update(User user);

    void delete(int userId);

    User findById(int userId);

    User findByUsername(String username);

    List<User> findAll();

    boolean existsByUsername(String username);

    boolean existsById(int userId);

    List<User> findByRole(UserRole role);

    List<User> findActiveUsers();

    boolean isActive(int userId);

//    boolean existsByPhone(String phone); this is for customer
}
