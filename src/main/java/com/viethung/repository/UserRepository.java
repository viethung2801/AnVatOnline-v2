package com.viethung.repository;

import com.viethung.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findUserByEmail(String email);

    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByCode(String code);

    /**
     * Kiểm tra khi update code đã tồn tại ở User nào khác không
     */
    long countByCodeAndIdNot(String code, UUID id);

    /**
     * Kiểm tra khi update email đã tồn tại ở User nào khác không
     */
    long countByEmailAndIdNot(String email, UUID id);
    /**
     * Kiểm tra khi update phoneNumber đã tồn tại ở User nào khác không
     */
    long countByPhoneNumberAndIdNot(String phoneNumber, UUID id);

    @Query("select distinct u from User u " +
            "where u.code like :keys or u.lastName like :keys " +
            "or u.firstName like :keys or u.email like :keys " +
            "or u.phoneNumber like :keys")
    Page<User> searchByKeys(String keys, Pageable pageable);

}
