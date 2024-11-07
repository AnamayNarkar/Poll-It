package com.implementation.JournalApp.service;

import java.util.Arrays;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.implementation.JournalApp.repository.UserRepository;
import com.implementation.JournalApp.entity.UserEntity;
import com.implementation.JournalApp.exception.custom.InternalServerErrorException;
import com.implementation.JournalApp.exception.custom.ValidationException;
import com.implementation.JournalApp.dto.UserDto;

@Service
public class UserService {

        @Autowired
        private UserRepository userRepository;

        public UserEntity createUser(UserDto userDto) {
                try {
                        UserEntity userEntity = new UserEntity(userDto.getUsername(), userDto.getPassword(), new Vector<>(Arrays.asList("USER")));
                        userEntity = userRepository.save(userEntity);
                        return userEntity;
                } catch (Exception e) {
                        throw new InternalServerErrorException("Error creating user: " + e.getMessage());
                }
        }

        public UserEntity loginUser(UserDto userDto) {
                try {
                        UserEntity userEntity = userRepository.findByusername(userDto.getUsername());
                        if (userEntity != null && userEntity.getPassword().equals(userDto.getPassword())) {
                                return userEntity;
                        } else {
                                throw new ValidationException("Invalid username or password");
                        }
                } catch (ValidationException ve) {
                        throw ve;
                } catch (Exception e) {
                        throw new InternalServerErrorException("Error logging in user: " + e.getMessage());
                }
        }

        public UserEntity getUser(String username) {
                try {
                        UserEntity userEntity = userRepository.findByusername(username);
                        if (userEntity != null) {
                                return userEntity;
                        } else {
                                throw new ValidationException("User not found");
                        }
                } catch (ValidationException ve) {
                        throw ve;
                } catch (Exception e) {
                        throw new InternalServerErrorException("Error retrieving user: " + e.getMessage());
                }
        }

        public void deleteUser(String username) {
                try {
                        UserEntity userEntity = userRepository.findByusername(username);
                        if (userEntity != null) {
                                userRepository.delete(userEntity);
                        } else {
                                throw new ValidationException("User not found, cannot delete");
                        }
                } catch (ValidationException ve) {
                        throw ve;
                } catch (Exception e) {
                        throw new InternalServerErrorException("Error deleting user: " + e.getMessage());
                }
        }

        public void updateUser(UserDto userDto) {
                try {
                        UserEntity userEntity = userRepository.findByusername(userDto.getUsername());
                        if (userEntity != null) {
                                userEntity.setPassword(userDto.getPassword());
                                userRepository.save(userEntity);
                        } else {
                                throw new ValidationException("User not found, cannot update");
                        }
                } catch (ValidationException ve) {
                        throw ve;
                } catch (Exception e) {
                        throw new InternalServerErrorException("Error updating user: " + e.getMessage());
                }
        }

        public Vector<String> getRoles(String username) {
                try {
                        UserEntity userEntity = userRepository.findByusername(username);
                        if (userEntity != null) {
                                return userEntity.getRoles();
                        } else {
                                throw new ValidationException("User not found");
                        }
                } catch (ValidationException ve) {
                        throw ve;
                } catch (Exception e) {
                        throw new InternalServerErrorException("Error retrieving roles: " + e.getMessage());
                }
        }
}