package com.aluguelcarros_vrs1.config;

import java.time.LocalDate;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.aluguelcarros_vrs1.domain.aluguel.Aluguel;
import com.aluguelcarros_vrs1.domain.aluguel.AluguelRepository;
import com.aluguelcarros_vrs1.domain.carro.Carro;
import com.aluguelcarros_vrs1.domain.carro.CarroRepository;
import com.aluguelcarros_vrs1.domain.carro.DadosCadastroCarro;
import com.aluguelcarros_vrs1.domain.cliente.Cliente;
import com.aluguelcarros_vrs1.domain.cliente.ClienteRepository;
import com.aluguelcarros_vrs1.domain.cliente.DadosCadastroCliente;
import com.aluguelcarros_vrs1.domain.endereco.DadosEndereco;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner{

    @Autowired
    private CarroRepository carroRepository;
    
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private AluguelRepository aluguelRepository;

    @Override
    @SuppressWarnings("null")
    public void run(String... args) throws Exception {
        // Criando carros de teste usando DTO
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

        // Salvando carros
        carroRepository.saveAll(Arrays.asList(c1, c2, c3, c4, c5));

        // Criando endereços de teste usando DTO
        DadosEndereco dadosEnd1 = new DadosEndereco("Avenida Paulista", "1000", "Centro", "Apto 101", "01310-100", "São Paulo", "SP");
        DadosEndereco dadosEnd2 = new DadosEndereco("Rua Vergueiro", "2500", "Vila Mariana", "Apto 205", "04011-002", "São Paulo", "SP");
        DadosEndereco dadosEnd3 = new DadosEndereco("Praça da Liberdade", "150", "Liberdade", "Casa", "01506-000", "São Paulo", "SP");
        DadosEndereco dadosEnd4 = new DadosEndereco("Rua Bandeira Paulista", "500", "Pinheiros", "Apto 1202", "05417-020", "São Paulo", "SP");

        // Criando clientes de teste usando DTO
        DadosCadastroCliente dadosCliente1 = new DadosCadastroCliente("João Silva", "joao@email.com", "11987654321", "123.456.789-00", dadosEnd1);
        DadosCadastroCliente dadosCliente2 = new DadosCadastroCliente("Maria Santos", "maria@email.com", "11987654322", "123.456.789-01", dadosEnd2);
        DadosCadastroCliente dadosCliente3 = new DadosCadastroCliente("Carlos Oliveira", "carlos@email.com", "11987654323", "123.456.789-02", dadosEnd3);
        DadosCadastroCliente dadosCliente4 = new DadosCadastroCliente("Ana Costa", "ana@email.com", "11987654324", "123.456.789-03", dadosEnd4);

        Cliente cl1 = new Cliente(dadosCliente1);
        Cliente cl2 = new Cliente(dadosCliente2);
        Cliente cl3 = new Cliente(dadosCliente3);
        Cliente cl4 = new Cliente(dadosCliente4);

        // Salvando clientes
        clienteRepository.saveAll(Arrays.asList(cl1, cl2, cl3, cl4));

        // Criando aluguéis de teste
        Aluguel a1 = new Aluguel(null, LocalDate.of(2026, 1, 20), LocalDate.of(2026, 1, 25), true, cl1, c1);
        Aluguel a2 = new Aluguel(null, LocalDate.of(2026, 1, 21), LocalDate.of(2026, 1, 28), true, cl2, c3);
        Aluguel a3 = new Aluguel(null, LocalDate.of(2026, 1, 22), LocalDate.of(2026, 1, 24), true, cl3, c2);
        Aluguel a4 = new Aluguel(null, LocalDate.of(2025, 12, 15), LocalDate.of(2025, 12, 20), false, cl4, c5);
        Aluguel a5 = new Aluguel(null, LocalDate.of(2026, 2, 1), LocalDate.of(2026, 2, 10), true, cl1, c4);

        // Salvando aluguéis
        aluguelRepository.saveAll(Arrays.asList(a1, a2, a3, a4, a5));
    }


}
