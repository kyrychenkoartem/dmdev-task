package com.artem.service;

import com.artem.mapper.BankAccountMapper;
import com.artem.model.dto.BankAccountCreateDto;
import com.artem.model.dto.BankAccountReadDto;
import com.artem.model.dto.BankAccountUpdateDto;
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
        var maybeBankAccount = bankAccountRepository.findById(bankAccountId);
        boolean isPresent = false;
        if (maybeBankAccount.isPresent()) {
            isPresent = maybeBankAccount.get().getAccount().getUser().getId().equals(currentUserId);
        }
        return isPresent;
    }
}
