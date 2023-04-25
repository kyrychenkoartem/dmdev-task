package com.artem.service;

import com.artem.mapper.UserMapper;
import com.artem.model.dto.UserCreateDto;
import com.artem.model.dto.UserFilter;
import com.artem.model.dto.UserReadDto;
import com.artem.model.dto.UserUpdateDto;
import com.artem.model.entity.User;
import com.artem.repository.QPredicate;
import com.artem.repository.UserRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import static com.artem.model.entity.QUser.user;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ImageService imageService;

    public List<UserReadDto> findAll() {
        return userRepository.findAll().stream()
                .map(userMapper::mapFrom)
                .toList();
    }

    public Page<UserReadDto> findAll(UserFilter filter, Pageable pageable) {
        var predicate = QPredicate.builder()
                .add(filter.firstName(), user.firstName::containsIgnoreCase)
                .add(filter.lastName(), user.lastName::containsIgnoreCase)
                .add(filter.birthDate(), user.birthDate::before)
                .buildAnd();
        return userRepository.findAll(predicate, pageable)
                .map(userMapper::mapFrom);
    }

    public Optional<UserReadDto> findById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::mapFrom);
    }

    @Transactional
    public UserReadDto create(UserCreateDto createDto) {
        return Optional.of(createDto)
                .map(dto -> {
                    uploadImage(dto.image());
                    return userMapper.mapFrom(dto);
                })
                .map(userRepository::save)
                .map(userMapper::mapFrom)
                .orElseThrow();
    }

    @Transactional
    public Optional<UserReadDto> update(Long id, UserUpdateDto updateDto) {
        return userRepository.findById(id)
                .map(user -> {
                    uploadImage(updateDto.image());
                    return userMapper.mapFrom(user, updateDto);
                })
                .map(userRepository::saveAndFlush)
                .map(userMapper::mapFrom);
    }

    @Transactional
    public boolean delete(Long id) {
        return userRepository.findById(id)
                .map(entity -> {
                    userRepository.delete(entity);
                    userRepository.flush();
                    return true;
                })
                .orElse(false);
    }

    @SneakyThrows
    private void uploadImage(MultipartFile image) {
        if (!image.isEmpty()) {
            imageService.upload(image.getOriginalFilename(), image.getInputStream());
        }
    }

    public Optional<byte[]> findAvatar(Long id) {
        return userRepository.findById(id)
                .map(User::getImage)
                .filter(StringUtils::hasText)
                .flatMap(imageService::get);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        Collections.singleton(user.getRole())
                ))
                .orElseThrow(() -> new UsernameNotFoundException("Failed to retrieve user: " + email));
    }

    public Long getId() {
        var userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByEmail(userDetails.getUsername())
                .map(User::getId)
                .orElseThrow(() -> new UsernameNotFoundException(userDetails.getUsername()));
    }
}
