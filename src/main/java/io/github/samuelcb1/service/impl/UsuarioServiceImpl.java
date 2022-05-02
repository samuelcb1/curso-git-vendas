package io.github.samuelcb1.service.impl;

import io.github.samuelcb1.domain.entity.Usuario;
import io.github.samuelcb1.domain.repository.UsuarioRepository;
import io.github.samuelcb1.exeption.SenhaInvalidaExeption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioServiceImpl implements UserDetailsService {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UsuarioRepository repository;

    @Transactional
    public Usuario salvar(Usuario usuario){
        return repository.save(usuario);
    }

    public UserDetails autenticar(Usuario usuario){
        UserDetails user = loadUserByUsername(usuario.getLogin());
        boolean senhasBaten = encoder.matches(usuario.getSenha(), user.getPassword());
        if (senhasBaten){
            return user;
        }
        throw new SenhaInvalidaExeption();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = repository.findByLogin(username)
                .orElseThrow( () -> new UsernameNotFoundException("Usuario não encontrado na base de Dados."));

            String[] roles = usuario.isAdmin() ?
                    new String[]{"ADMIN","USER"} : new String[] {"USER"};

        return User
                .builder()
                .username(usuario.getLogin())
                .password(usuario.getSenha())
                .roles(roles)
                .build();
    }
}
