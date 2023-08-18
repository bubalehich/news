package ru.clevertec.auth.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.clevertec.auth.entity.Role;
import ru.clevertec.auth.repository.RoleRepository;

@Service
@AllArgsConstructor
@Slf4j
public class RoleService {
    private final RoleRepository repository;

    public Role getByName(String name) {
        return repository.findByName(name).orElseThrow(()
                -> new EntityNotFoundException("Role not found."));
    }
}
