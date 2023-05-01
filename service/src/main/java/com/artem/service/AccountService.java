package com.artem.service;

import com.artem.mapper.AccountMapper;
import com.artem.model.dto.AccountCreateDto;
import com.artem.model.dto.AccountReadDto;
import com.artem.model.dto.AccountUpdateDto;
import com.artem.repository.AccountRepository;
import com.artem.util.UserDetailsUtil;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService implements UserPermissionService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public List<AccountReadDto> findAll() {
        return accountRepository.findAll().stream()
                .map(accountMapper::mapFrom)
                .toList();
    }

    @PreAuthorize("hasAuthority('ADMIN') or @accountService.isUserOwner(#id)")
    public Optional<AccountReadDto> findById(Long id) {
        return accountRepository.findById(id)
                .map(accountMapper::mapFrom);
    }

    public Optional<AccountReadDto> findByUserId(Long userId) {
        return accountRepository.findByUserId(userId)
                .map(accountMapper::mapFrom);
    }

    @Transactional
    public AccountReadDto create(AccountCreateDto createDto) {
        return Optional.of(createDto)
                .map(accountMapper::mapFrom)
                .map(accountRepository::save)
                .map(accountMapper::mapFrom)
                .orElseThrow();
    }

    @Transactional
    public Optional<AccountReadDto> update(Long id, AccountUpdateDto updateDto) {
        return accountRepository.findById(id)
                .map(account -> accountMapper.mapFrom(account, updateDto))
                .map(accountRepository::saveAndFlush)
                .map(accountMapper::mapFrom);
    }

    @Transactional
    public boolean delete(Long id) {
        return accountRepository.findById(id)
                .map(entity -> {
                    accountRepository.delete(entity);
                    accountRepository.flush();
                    return true;
                })
                .orElse(false);
    }

    @Override
    public boolean isUserOwner(Long accountId) {
        var currentUserId = UserDetailsUtil.getCurrentUserId();
        var maybeAccount = accountRepository.findById(accountId);
        boolean isPresent = false;
        if (maybeAccount.isPresent()) {
            isPresent = maybeAccount.get().getUser().getId().equals(currentUserId);
        }
        return isPresent;
    }
}
















