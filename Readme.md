# Chapter 3: Ractive rest development and asynchronous systems

```java

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;

    @GetMapping
    public Flux<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping
    public Mono<ResponseEntity<User>> createUser(@RequestBody User user) {
        return userRepository.findByEmail(user.email())
                .flatMap(existingUser -> Mono.error(new EmailUniquenessException("Email already exists")))
                .then(userRepository.save(user)
                        .doOnNext(savedUser -> System.out.println("Saved User: " + savedUser)) // Side-effect
                        .map(savedUser -> ResponseEntity.status(HttpStatus.CREATED).body(savedUser))) // Convert to ResponseEntity
                .onErrorResume(e -> {
                    System.out.println("An exception has occurred: " + e.getMessage());
                    if (e instanceof EmailUniquenessException) {
                        return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).build());
                    } else {
                        return Mono.just(ResponseEntity
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .build());
                    }
                });
    }

    @GetMapping("/{id}")
    public Mono<User> getUserById(@PathVariable Long id) {
        return userRepository.findById(id);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteUserById(@PathVariable Long id) {
        return userRepository.deleteById(id);
    }
}
```