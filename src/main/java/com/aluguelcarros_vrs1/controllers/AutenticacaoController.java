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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/autenticacao")
@Tag(name = " 1/Autenticação", description = "Endpoints para autenticação de usuários")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;
    
    
    @Operation(summary = "Realiza login de um usuário", description = "Autentica um usuário e retorna um token JWT válido. Para utilizar os demais endpoints, é necessário informar o token JWT no botão 'Authorize' logo na parte inicial dessa página do Swagger. Login para testes: logintestes@hotmail.com, senha: senhatestes")
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
