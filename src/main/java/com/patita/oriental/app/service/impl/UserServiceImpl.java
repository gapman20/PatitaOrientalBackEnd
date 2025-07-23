package com.patita.oriental.app.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.patita.oriental.app.model.Product;
import com.patita.oriental.app.model.User;
import com.patita.oriental.app.reposity.ProductRepository;
import com.patita.oriental.app.reposity.UserRepository;
import com.patita.oriental.app.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	UserRepository userRepository;
	ProductRepository productRepository; //***change

	public UserServiceImpl(UserRepository userRepository, ProductRepository productRepository) {//***change only ProductRepository productRepository
		this.userRepository = userRepository;
		this.productRepository = productRepository;//***change
	}
	

	@Override
	public Iterable<User> findAll() {
		return userRepository.findAll();
	}

	@Override
	public User findById(Long id) {
		Optional<User> userOpt = userRepository.findById(id);
		if( userOpt.isEmpty()) { // no hay objeto, la variable contiene null
			throw new IllegalStateException("User does not exist wih id " + id);
		}
		User existingUser = userOpt.get();
		return existingUser;
	}

	@Override
	public User save(User user) {
		user.setId(null);
		User newUser = userRepository.save(user);
		return newUser;
	}

	@Override
	public User update(Long id, User user) {
		User existingUser = findById(id);
		// Solo se modifica los atributos permitidos
		existingUser.setName(user.getName());
	    existingUser.setLastName(user.getLastName());
	    existingUser.setEmail(user.getEmail());
	    existingUser.setPhoneNumber(user.getPhoneNumber());
	    existingUser.setAddress(user.getAddress());
	    existingUser.setPostalCode(user.getPostalCode());
	    existingUser.setPassword(user.getPassword());
	    existingUser.setImageUrl(user.getImageUrl());
	    existingUser.setActive(user.isActive());
	    existingUser.setFavorites(user.getFavorites()); // Si los favoritos pueden cambiar
	    //existingUser.setUpdatedAt(LocalDateTime.now()); // Actualizamos timestamp

		// existingRole.setId( role.getId() ); NO se permite modificar
		User updatedUser = userRepository.save( existingUser );
		return updatedUser;
	}

	@Override
	public void deleteById(Long id) {
		User existingUser = findById(id);
		existingUser.setActive(false);; // Borrado lógico
		userRepository.save( existingUser );
	}
	
	@Override
	public Iterable<User> getActiveUsers() {
		return userRepository.findByIsActive(true);
	}
	
	@Override
	public User addFavoriteProduct(Long userId, Long productId) {
	    User user = userRepository.findById(userId)
	        .orElseThrow(() -> new IllegalStateException("User not found with id " + userId));

	    Product product = productRepository.findById(productId)
	        .orElseThrow(() -> new IllegalStateException("Product not found with id " + productId));

	    user.getFavorites().add(product); // Añadir producto a favoritos
	    return userRepository.save(user); // Guardar usuario actualizado
	}
	
	@Override
	public User removeFavoriteProduct(Long userId, Long productId) {
	    User user = userRepository.findById(userId)
	        .orElseThrow(() -> new IllegalStateException("User not found with id " + userId));

	    Product product = productRepository.findById(productId)
	        .orElseThrow(() -> new IllegalStateException("Product not found with id " + productId));

	    user.getFavorites().remove(product);
	    return userRepository.save(user);
	}
}
