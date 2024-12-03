package com.implementation.PollingApp.service;

import java.util.Arrays;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.implementation.PollingApp.repository.UserRepository;
import com.implementation.PollingApp.entity.UserEntity;
import com.implementation.PollingApp.exception.custom.InternalServerErrorException;
import com.implementation.PollingApp.exception.custom.ValidationException;
import com.implementation.PollingApp.dto.UserLoginDTO;
import com.implementation.PollingApp.dto.UserRegistrationDto;

@Service
public class UserService {

        @Autowired
        private UserRepository userRepository;

        public UserEntity createUser(UserRegistrationDto userDto) {
                try {

                        System.out.println("Creating user: " + userDto.getUsername());

                        if (userRepository.findByUsername(userDto.getUsername()) != null) {
                                throw new ValidationException("Username already exists");
                        }

                        if (userRepository.findByEmail(userDto.getEmail()) != null) {
                                throw new ValidationException("Email already exists");
                        }

                        UserEntity userEntity = new UserEntity(userDto.getUsername(), userDto.getEmail(), userDto.getPassword(), new Vector<>(Arrays.asList("USER")));

                        userEntity = userRepository.save(userEntity);

                        System.out.println("User created successfully");

                        return userEntity;
                } catch (Exception e) {
                        throw new InternalServerErrorException("Error creating user: " + e.getMessage());
                }
        }

        public UserEntity loginUser(UserLoginDTO userDto) {
                try {
                        UserEntity userEntity = userRepository.findByUsername(userDto.getUsername());
                        if (userEntity != null && userEntity.getPassword().equals(userDto.getPassword())) {
                                return userEntity;
                        } else {
                                System.out.println("Invalid username or password");
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
                        UserEntity userEntity = userRepository.findByUsername(username);
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
                        UserEntity userEntity = userRepository.findByUsername(username);
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

        public void updateUser(UserLoginDTO userDto) {
                try {
                        UserEntity userEntity = userRepository.findByUsername(userDto.getUsername());
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
                        UserEntity userEntity = userRepository.findByUsername(username);
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