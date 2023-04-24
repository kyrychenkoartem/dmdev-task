package com.artem.service;

import com.artem.mapper.BankAccountMapper;
import com.artem.model.dto.BankAccountCreateDto;
import com.artem.model.dto.BankAccountReadDto;
import com.artem.model.dto.BankAccountUpdateDto;
import com.artem.model.entity.BankAccount;
import com.artem.repository.BankAccountRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final BankAccountMapper bankAccountMapper;
    private final UserService userService;

    public List<BankAccountReadDto> findAll() {
        return bankAccountRepository.findAll().stream()
                .map(bankAccountMapper::mapFrom)
                .toList();
    }

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

    public List<Long> getId() {
       return bankAccountRepository.findAllByUserId(userService.getId()).stream()
                .map(BankAccount::getId)
                .toList();
    }
}
