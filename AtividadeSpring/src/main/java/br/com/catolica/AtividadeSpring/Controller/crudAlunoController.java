package br.com.catolica.AtividadeSpring.Controller;

import br.com.catolica.AtividadeSpring.Models.Aluno;
import br.com.catolica.AtividadeSpring.Repository.ContatoJdbcDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity; // Importar ResponseEntity
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/aluno")
public class crudAlunoController {

    @Autowired
    private ContatoJdbcDAO contatoJdbcDAO;

    @PostMapping
    public Aluno criaAluno(@RequestBody Aluno aluno){
        contatoJdbcDAO.salva(aluno);
        return aluno;
    }

    @GetMapping
    public List<Aluno> listaAlunos(){
        return contatoJdbcDAO.listaTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> buscaPorId(@PathVariable int id) { // Alterado para ResponseEntity
        Aluno aluno = contatoJdbcDAO.buscaPorId(id);
        if (aluno != null) {
            return ResponseEntity.ok(aluno); // Retorna o aluno com status 200 OK
        } else {
            // Retorna a mensagem de erro com status 404 Not Found
            return ResponseEntity.status(404).body("Aluno com ID " + id + " não encontrado.");
        }
    }


    private static List<Aluno> listaAlunos = new ArrayList<>();

    @GetMapping("/busca")
    public List<Aluno> encontraNome(@RequestParam(value = "nome") String nome) {
        // Se nenhum nome for fornecido ou estiver vazio, poderia retornar todos os alunos
        if (nome == null || nome.trim().isEmpty()) {
            return contatoJdbcDAO.listaTodos();
        }
        return contatoJdbcDAO.buscaPorNome(nome);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Aluno> atualizaAluno(@PathVariable int id, @RequestBody Aluno alunoAtualizado) {
        // Verifica se o aluno existe antes de atualizar
        if (contatoJdbcDAO.buscaPorId(id) == null) {
            return ResponseEntity.notFound().build();
        }
        alunoAtualizado.setId(id); // Garante que o ID do objeto é o mesmo da URL
        contatoJdbcDAO.atualiza(id, alunoAtualizado);
        return ResponseEntity.ok(alunoAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> removeAluno(@PathVariable int id) {
        int result = contatoJdbcDAO.deleta(id);
        if (result > 0) {
            return ResponseEntity.ok("Aluno com ID " + id + " removido com sucesso.");
        }
        return ResponseEntity.status(404).body("Aluno com ID " + id + " não encontrado.");
    }
}