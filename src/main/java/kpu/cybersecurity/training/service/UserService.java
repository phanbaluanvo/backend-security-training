package kpu.cybersecurity.training.service;

import kpu.cybersecurity.training.domain.dto.request.ReqUserDTO;
import kpu.cybersecurity.training.domain.dto.response.ResUserDTO;
import kpu.cybersecurity.training.domain.entity.User;
import kpu.cybersecurity.training.domain.enums.Role;
import kpu.cybersecurity.training.repository.UserRepository;
import kpu.cybersecurity.training.util.exception.UniqueException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResUserDTO createUser(ReqUserDTO reqUserDTO) throws UniqueException {
        if (userRepository.existsByUserIdAndEmail(reqUserDTO.getUserId(), reqUserDTO.getEmail())) {
            throw new UniqueException("UserID or Email already exists.");
        }

        User user = new User();
        user.fromRequestDto(reqUserDTO);
        user.setPassword(passwordEncoder.encode(reqUserDTO.getPassword()));

        User savedUser = userRepository.save(user);
        return savedUser.toResponseDto();
    }

    public ResUserDTO getUserDTOByUserID(String userId) {
        return userRepository.getUserByUserId(userId)
                .map(User::toResponseDto)
                .orElseThrow(()->new UsernameNotFoundException("User ID not found"));
    }

    public User getUserByUserID(String userId) {
        return userRepository.getUserByUserId(userId)
                .orElseThrow(()->new UsernameNotFoundException("User ID not found"));
    }

    public ResUserDTO modifyUser(ReqUserDTO dto) throws UniqueException {
        if(!userRepository.existsById(dto.getUserId())) {
            throw new UsernameNotFoundException("User ID not exist");
        }

        User user = userRepository.getReferenceById(dto.getUserId());

        if(!user.getEmail().equals(dto.getEmail())) {
            if(userRepository.existsByEmail(dto.getEmail())) {
                throw new UniqueException("Email is already exists");
            }
        }

        user.fromRequestDto(dto);

        user = userRepository.save(user);

        return user.toResponseDto();
    }

    public void updateUserToken(String token, String userId) {
        User currentUser = this.getUserByUserID(userId);
        if(currentUser != null) {
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }
    }

    public User getUserByRefreshTokenAndUserId(String token, String userId) {
        return this.userRepository.findByRefreshTokenAndUserId(token, userId)
                .orElseThrow(() -> new BadCredentialsException("Invalid Refresh Token"));
    }
}
