package com.artem.service;

import com.artem.mapper.AccountMapper;
import com.artem.model.dto.AccountCreateDto;
import com.artem.model.dto.AccountReadDto;
import com.artem.model.dto.AccountUpdateDto;
import com.artem.model.entity.Account;
import com.artem.repository.AccountRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final UserService userService;

    public List<AccountReadDto> findAll() {
        return accountRepository.findAll().stream()
                .map(accountMapper::mapFrom)
                .toList();
    }

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

    public Long getId() {
        return accountRepository.findByUserId(userService.getId())
                .map(Account::getId)
                .orElseThrow(NoSuchElementException::new);
    }
}
