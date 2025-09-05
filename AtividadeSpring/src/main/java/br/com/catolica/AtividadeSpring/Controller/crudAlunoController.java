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
    public Aluno atualizaAluno(@PathVariable int id, @RequestBody Aluno alunoAtualizado) {
        for (Aluno a : listaAlunos) {
            if (a.getId() == id) {
                a.setNome(alunoAtualizado.getNome());
                a.setIdade(alunoAtualizado.getIdade());
                a.setMatricula(alunoAtualizado.getMatricula());
                a.setEmail(alunoAtualizado.getEmail());
                a.setCurso(alunoAtualizado.getCurso());
                a.setTelefone(alunoAtualizado.getTelefone());
                return a;
            }
        }
        return null;
    }
    @DeleteMapping("/{id}") //Remove o aluno
    public String removeAluno(@PathVariable int id) {
        for (Aluno a : listaAlunos) {
            if (a.getId() == id) {
                listaAlunos.remove(a);
                return "Aluno com ID " + id + " removido com sucesso.";
            }
        }
        return "Aluno com ID " + id + " não encontrado.";
    }

}