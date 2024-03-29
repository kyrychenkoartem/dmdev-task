package com.artem.service;

import com.artem.mapper.BankAccountMapper;
import com.artem.model.dto.BankAccountCreateDto;
import com.artem.model.dto.BankAccountReadDto;
import com.artem.model.dto.BankAccountUpdateDto;
import com.artem.model.entity.BankAccount;
import com.artem.repository.BankAccountRepository;
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
public class BankAccountService implements UserPermissionService {

    private final BankAccountRepository bankAccountRepository;
    private final BankAccountMapper bankAccountMapper;

    public List<BankAccountReadDto> findAll() {
        return bankAccountRepository.findAll().stream()
                .map(bankAccountMapper::mapFrom)
                .toList();
    }

    public Optional<BankAccountReadDto> findByUserId(Long userId) {
        return bankAccountRepository.findAllByUserId(userId).stream()
                .map(bankAccountMapper::mapFrom)
                .findFirst();
    }

    public List<BankAccountReadDto> findAllIdsByUserId(Long userId) {
        return bankAccountRepository.findAllByUserId(userId).stream()
                .map(bankAccountMapper::mapFrom)
                .toList();
    }

    @PreAuthorize("hasAuthority('ADMIN') or @bankAccountService.isUserOwner(#id)")
    public Optional<BankAccountReadDto> findById(Long id) {
        return bankAccountRepository.findById(id)
                .map(bankAccountMapper::mapFrom);
    }

    @Transactional
    public BankAccountReadDto create(BankAccountCreateDto createDto) {
        return Optional.of(createDto)
                .map(bankAccountMapper::mapFrom)
                .map(bankAccountRepository::save)
                .map(bankAccountMapper::mapFrom)
                .orElseThrow();
    }

    @Transactional
    public Optional<BankAccountReadDto> update(Long id, BankAccountUpdateDto updateDto) {
        return bankAccountRepository.findById(id)
                .map(account -> bankAccountMapper.mapFrom(account, updateDto))
                .map(bankAccountRepository::saveAndFlush)
                .map(bankAccountMapper::mapFrom);
    }

    @Transactional
    public boolean delete(Long id) {
        return bankAccountRepository.findById(id)
                .map(entity -> {
                    bankAccountRepository.delete(entity);
                    bankAccountRepository.flush();
                    return true;
                })
                .orElse(false);
    }

    @Override
    public boolean isUserOwner(Long bankAccountId) {
        var currentUserId = UserDetailsUtil.getCurrentUserId();
        return bankAccountRepository.findById(bankAccountId)
                .map(bankAccount -> bankAccount.getAccount().getUser().getId().equals(currentUserId))
                .orElse(false);
    }
}
