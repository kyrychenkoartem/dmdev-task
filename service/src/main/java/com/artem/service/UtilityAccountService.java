package com.artem.service;

import com.artem.mapper.UtilityAccountMapper;
import com.artem.model.dto.UtilityAccountCreateDto;
import com.artem.model.dto.UtilityAccountReadDto;
import com.artem.model.dto.UtilityAccountUpdateDto;
import com.artem.repository.UtilityAccountRepository;
import com.artem.util.UserDetailsUtil;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UtilityAccountService implements UserPermissionService {

    private final UtilityAccountRepository utilityAccountRepository;
    private final UtilityAccountMapper utilityAccountMapper;

    public List<UtilityAccountReadDto> findAll() {
        return utilityAccountRepository.findAll().stream()
                .map(utilityAccountMapper::mapFrom)
                .toList();
    }

    public Optional<UtilityAccountReadDto> findById(Long id) {
        return utilityAccountRepository.findById(id)
                .map(utilityAccountMapper::mapFrom);
    }

    @Transactional
    public UtilityAccountReadDto create(UtilityAccountCreateDto createDto) {
        return Optional.of(createDto)
                .map(utilityAccountMapper::mapFrom)
                .map(utilityAccountRepository::save)
                .map(utilityAccountMapper::mapFrom)
                .orElseThrow();
    }

    @Transactional
    public Optional<UtilityAccountReadDto> update(Long id, UtilityAccountUpdateDto updateDto) {
        return utilityAccountRepository.findById(id)
                .map(account -> utilityAccountMapper.mapFrom(account, updateDto))
                .map(utilityAccountRepository::saveAndFlush)
                .map(utilityAccountMapper::mapFrom);
    }

    @Transactional
    public boolean delete(Long id) {
        return utilityAccountRepository.findById(id)
                .map(entity -> {
                    utilityAccountRepository.delete(entity);
                    utilityAccountRepository.flush();
                    return true;
                })
                .orElse(false);
    }

    @Override
    public boolean isUserOwner(Long utilityAccountId) {
        var currentUserId = UserDetailsUtil.getCurrentUserId();
        return utilityAccountRepository.findById(utilityAccountId)
                .map(utilityAccount -> utilityAccount.getAccount().getUser().getId().equals(currentUserId))
                .orElse(false);
    }
}
