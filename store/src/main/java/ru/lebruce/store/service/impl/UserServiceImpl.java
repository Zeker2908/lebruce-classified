package ru.lebruce.store.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.lebruce.store.domain.dto.UpdateUserRequest;
import ru.lebruce.store.domain.model.Role;
import ru.lebruce.store.domain.model.User;
import ru.lebruce.store.exception.UserAlreadyExistsException;
import ru.lebruce.store.exception.UserNotFoundException;
import ru.lebruce.store.repository.UserRepository;
import ru.lebruce.store.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String USER_NOT_FOUND_MESSAGE = "Пользователь не найден";
    private static final String USER_ALREADY_EXISTS_MESSAGE = "Пользователь с таким email уже существует";
    private static final String AUTHENTICATION_NOT_FOUND_MESSAGE = "Пользователь не аутентифицирован";


    private final UserRepository repository;


    @Override
    public List<User> findAllUsers() {
        return repository.findAll();
    }

    @Override
    public User saveUser(User user) {
        return repository.save(user);
    }

    /**
     * Получение пользователя по почте
     * <p>
     *
     * @return пользователь
     */

    @Override
    public User updateUser(UpdateUserRequest userRequest) {
        User user = getCurrentUser();

        updateIfPresent(userRequest.getFirstName(), user::setFirstName);
        updateIfPresent(userRequest.getLastName(), user::setLastName);

        return repository.save(user);
    }

    private void updateIfPresent(String value, Consumer<String> updater) {
        Optional.ofNullable(value)
                .filter(v -> !v.isEmpty())
                .ifPresent(updater);
    }


    @Override
    @Transactional
    public void deleteUser(String username) {
        if (!repository.existsByUsername(username)) {
            throw new UserNotFoundException(USER_NOT_FOUND_MESSAGE);
        }
        repository.deleteByUsername(username);
    }

    /**
     * Создание пользователя
     * <p>
     *
     * @return созданный пользователь
     */
    @Override
    public User create(User user) {
        if (repository.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistsException(USER_ALREADY_EXISTS_MESSAGE);
        }

        return saveUser(user);
    }

    /**
     * Получение пользователя по имени пользователя
     * <p>
     *
     * @return пользователь
     */
    @Override
    public User getByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND_MESSAGE));

    }

    /**
     * Получение пользователя по имени пользователя
     * <p>
     * Нужен для Spring Security
     *
     * @return пользователь
     */
    @Override
    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    /**
     * Получение текущего пользователя
     * <p>
     *
     * @return текущий пользователь
     */
    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            return repository.findByUsername(userDetails.getUsername()).orElseThrow(() ->
                    new UserNotFoundException(USER_NOT_FOUND_MESSAGE));
        } else {
            throw new AuthenticationCredentialsNotFoundException(AUTHENTICATION_NOT_FOUND_MESSAGE);
        }
    }


    @Override
    public boolean checkAdmin() {
        User user = getCurrentUser();
        return user.getRole().name().equals("ROLE_ADMIN");
    }


    /**
     * Выдача прав администратора текущему пользователю
     * <p>
     * Нужен для демонстрации
     */
    @Override
    public void getAdmin(String username) {
        var user = getByUsername(username);
        user.setRole(Role.ROLE_ADMIN);
        saveUser(user);
    }

    @Override
    public boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }

}
