package tech.fdiez.flowfullybackend.exception

class UsernameNotFoundException(username: String) : RuntimeException("User $username not found")

class UsernameAlreadyExists(username: String) : RuntimeException("User $username already exists")