/**
 * This package is responsible for CRUD operations related to users.
 * 
 * I'm not sure how exactly we should represent users in the program, as I feel
 * directly interacting with the UserEntity allows too much freedom to change
 * values, so I'm using AbstractUser as a proxy that is easier to work with,
 * albeit less flexible. I'm not sure if we'll keep the subclasses of 
 * AbstractUser, as I'm not sure how helpful they'll be for polymorphism and
 * other stuff.
 * 
 * UserEntity is harder to work with than AbstractUser
 * 
 * @author Matt Crow <mattcrow19@gmail.com>
 */
package com.compilercharisma.chameleonbusinessstudio.users;
