package com.skeletonhexa.domain.repository;

import com.skeletonhexa.domain.entities.Admin;

public interface AdminRepository {
    Admin findById(int id);
    Admin findByUsername(String username);
    Admin findByEmail(String email);
    boolean authenticate(String username, String password);
    boolean save(Admin admin);
    boolean update(Admin admin);
    boolean delete(int id);
}