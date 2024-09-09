package com.aluguelcarros_vrs1.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aluguelcarros_vrs1.domain.usuario.DadosAutenticacao;
import com.aluguelcarros_vrs1.domain.usuario.Usuario;
import com.aluguelcarros_vrs1.infra.security.DadosTokenJWT;
import com.aluguelcarros_vrs1.infra.security.TokenService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/autenticacao")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;
    
    /**
     * Endpoint para realizar login de um usuário.
     * @param dados Dados de autenticação (login e senha) do usuário.
     * @return Resposta com o token JWT gerado para o usuário.
     */
    @PostMapping("/login")
    public ResponseEntity<DadosTokenJWT> efetuarLogin(@RequestBody @Valid DadosAutenticacao dados) {
        // Cria um token de autenticação com as credenciais fornecidas
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
        
        // Autentica o token e obtém o resultado
        var authentication = manager.authenticate(authenticationToken);
        
        // Gera um token JWT para o usuário autenticado
        var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());
        
        // Retorna a resposta com o token JWT
        return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));
    }
}
