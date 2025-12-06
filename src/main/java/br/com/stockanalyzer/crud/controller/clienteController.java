package br.com.stockanalyzer.crud.controller;

import br.com.stockanalyzer.crud.model.Cliente;
import br.com.stockanalyzer.crud.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clientes")
public class clienteController {

    @Autowired
    public ClienteService clienteService;

    @GetMapping
    public List<Cliente> listarTodosClientes(){
        return clienteService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> listarClientePorId(@PathVariable Integer id) {
        Optional<Cliente> cliente = clienteService.listarClienteById(id);

        return cliente.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Cliente cadastrarCliente(@RequestBody Cliente novoCliente){
        return clienteService.cadastrarCliente(novoCliente);
    }
}
