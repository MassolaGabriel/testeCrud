package br.com.stockanalyzer.crud.service;

import br.com.stockanalyzer.crud.model.Cliente;
import br.com.stockanalyzer.crud.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {
    @Autowired
    ClienteRepository clienteRepository;

    public List<Cliente> listar(){
        return clienteRepository.findAll();
    }

    public Optional<Cliente> listarClienteById(Integer id){
        return clienteRepository.findById(id);
    }

    public Cliente atualizarCliente(Integer id, Cliente clienteAtualizado){
        Optional<Cliente> clienteExistenteOptional = clienteRepository.findById(id);

        if (clienteExistenteOptional.isEmpty()){
            return null;
        }

        Cliente clienteExiste = clienteExistenteOptional.get();

        clienteExiste.setNome(clienteAtualizado.getNome());
        clienteExiste.setNome(clienteAtualizado.getEndereco());
        clienteExiste.setNome(String.valueOf(clienteAtualizado.getAtivo()));

        return clienteRepository.save(clienteExiste);
    }

    public Cliente cadastrarCliente(Cliente cliente){
        return clienteRepository.save(cliente);
    }

    public void excluirCliente(Integer id){
        clienteRepository.deleteById(id);
    }
}
