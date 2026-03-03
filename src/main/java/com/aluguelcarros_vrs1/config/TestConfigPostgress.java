package com.aluguelcarros_vrs1.config;

import com.aluguelcarros_vrs1.domain.Aluguel;
import com.aluguelcarros_vrs1.domain.Carro;
import com.aluguelcarros_vrs1.data.carro.DadosCadastroCarro;
import com.aluguelcarros_vrs1.domain.Cliente;
import com.aluguelcarros_vrs1.data.cliente.DadosCadastroCliente;
import com.aluguelcarros_vrs1.data.cliente.DadosEndereco;
import com.aluguelcarros_vrs1.domain.User;
import com.aluguelcarros_vrs1.repositories.AluguelRepository;
import com.aluguelcarros_vrs1.repositories.CarroRepository;
import com.aluguelcarros_vrs1.repositories.ClienteRepository;
import com.aluguelcarros_vrs1.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@Profile("testpostgress")
public class TestConfigPostgress implements CommandLineRunner{

    @Autowired
    private CarroRepository carroRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private AluguelRepository aluguelRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @SuppressWarnings("null")
    public void run(String... args) throws Exception {
        if (userRepository.count() < 1 || carroRepository.count() < 1 || aluguelRepository.count() < 1 || clienteRepository.count() < 1) {
            User user1 = new User();
            user1.setEmail("logintestes@hotmail.com");
            user1.setUsername("dev");
            user1.setPassword(passwordEncoder.encode("senhatestes"));
            userRepository.save(user1);

            User user2 = new User();
            user2.setEmail("puff@gmail.com");
            user2.setUsername("puff");
            user2.setPassword(passwordEncoder.encode("PuffDev123"));
            userRepository.save(user2);

            DadosCadastroCarro dados1 = new DadosCadastroCarro("Toyota Corolla", 150.0f, 3);
            DadosCadastroCarro dados2 = new DadosCadastroCarro("Honda Civic", 140.0f, 2);
            DadosCadastroCarro dados3 = new DadosCadastroCarro("Ford Mustang", 250.0f, 1);
            DadosCadastroCarro dados4 = new DadosCadastroCarro("Volkswagen Golf", 120.0f, 4);
            DadosCadastroCarro dados5 = new DadosCadastroCarro("Hyundai HB20", 100.0f, 5);

            Carro c1 = new Carro(dados1);
            Carro c2 = new Carro(dados2);
            Carro c3 = new Carro(dados3);
            Carro c4 = new Carro(dados4);
            Carro c5 = new Carro(dados5);

            carroRepository.saveAll(Arrays.asList(c1, c2, c3, c4, c5));

            DadosEndereco dadosEnd1 = new DadosEndereco("Avenida Paulista", "1000", "Centro", "Apto 101", "01310-100", "São Paulo", "SP");
            DadosEndereco dadosEnd2 = new DadosEndereco("Rua Vergueiro", "2500", "Vila Mariana", "Apto 205", "04011-002", "São Paulo", "SP");
            DadosEndereco dadosEnd3 = new DadosEndereco("Praça da Liberdade", "150", "Liberdade", "Casa", "01506-000", "São Paulo", "SP");
            DadosEndereco dadosEnd4 = new DadosEndereco("Rua Bandeira Paulista", "500", "Pinheiros", "Apto 1202", "05417-020", "São Paulo", "SP");

            DadosCadastroCliente dadosCliente1 = new DadosCadastroCliente("João Silva", "joao@email.com", "11987654321", "123.456.789-00", LocalDate.of(2015, 1, 12), dadosEnd1);
            DadosCadastroCliente dadosCliente2 = new DadosCadastroCliente("Maria Santos", "maria@email.com", "11987654322", "123.456.789-01", LocalDate.of(2015, 4, 13), dadosEnd2);
            DadosCadastroCliente dadosCliente3 = new DadosCadastroCliente("Carlos Oliveira", "carlos@email.com", "11987654323", "123.456.789-02", LocalDate.of(2015, 5, 15), dadosEnd3);
            DadosCadastroCliente dadosCliente4 = new DadosCadastroCliente("Ana Costa", "ana@email.com", "11987654324", "123.456.789-03", LocalDate.of(2015, 11, 20), dadosEnd4);

            Cliente cl1 = new Cliente(dadosCliente1);
            Cliente cl2 = new Cliente(dadosCliente2);
            Cliente cl3 = new Cliente(dadosCliente3);
            Cliente cl4 = new Cliente(dadosCliente4);

            clienteRepository.saveAll(Arrays.asList(cl1, cl2, cl3, cl4));

            Aluguel a1 = new Aluguel(null, LocalDate.now(), LocalDate.now().plusDays(5), cl1, c1, new ArrayList<>(List.of(Aluguel.AluguelStatus.ATIVO)), Instant.now(), Instant.now(), user1.getUsername());
            Aluguel a2 = new Aluguel(null, LocalDate.now(), LocalDate.now().plusDays(15), cl2, c2, new ArrayList<>(List.of(Aluguel.AluguelStatus.ATIVO)), Instant.now(), Instant.now(), user1.getUsername());
            Aluguel a3 = new Aluguel(null, LocalDate.of(2026, 1, 24), LocalDate.of(2026, 1, 28), cl3, c3, new ArrayList<>(List.of(Aluguel.AluguelStatus.ENCERRADO, Aluguel.AluguelStatus.INATIVO)), Instant.now(), Instant.now(), user2.getUsername());
            Aluguel a4 = new Aluguel(null, LocalDate.now(), LocalDate.now().plusDays(35), cl4, c5, new ArrayList<>(List.of(Aluguel.AluguelStatus.ATIVO)), Instant.now(), Instant.now(), user1.getUsername());
            Aluguel a5 = new Aluguel(null, LocalDate.now().plusDays(5), LocalDate.now().plusDays(20), cl1, c4, new ArrayList<>(List.of(Aluguel.AluguelStatus.PENDENTE, Aluguel.AluguelStatus.ATIVO)), Instant.now(), Instant.now(), user2.getUsername());

            aluguelRepository.saveAll(Arrays.asList(a1, a2, a3, a4, a5));
        }
    }

}
