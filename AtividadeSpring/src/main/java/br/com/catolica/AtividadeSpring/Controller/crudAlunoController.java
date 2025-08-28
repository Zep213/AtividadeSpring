package br.com.catolica.AtividadeSpring.Controller;

import br.com.catolica.AtividadeSpring.Models.Aluno;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/aluno")
public class crudAlunoController {

    private static List<Aluno> listaAlunos = new ArrayList<>();
    private int contador = 1;

    @PostMapping
    public Aluno criarAluno(@RequestBody Aluno aluno){
        aluno.setId(contador++);
        listaAlunos.add(aluno);
        return aluno;
    }

    @GetMapping
    public List<Aluno> listarAlunos(){
        return listaAlunos;
    }

    @GetMapping("/{id}")
    public Object buscarPorId(@PathVariable int id) {

        for (Aluno a : listaAlunos) {
            if (a.getId() == id) {
                return a;
            }
        }
        return "Aluno com ID " + id + " não encontrado.";
    }

    //TODO:FAZER A BUSCA POR NOME(NAO SEI SE TEM QUE SER EXATAMENTE POR NOME)

    @PutMapping("/{id}")
    public Aluno atualizarAluno(@PathVariable int id, @RequestBody Aluno alunoAtualizado) {
        for (Aluno a : listaAlunos) {
            if (a.getId() == id) {
                a.setNome(alunoAtualizado.getNome());
                a.setIdade(alunoAtualizado.getIdade());
                return a;
            }
        }
        return null;
    }
    @DeleteMapping("/{id}")
    public String removerAluno(@PathVariable int id) {

        for (Aluno a : listaAlunos) {
            if (a.getId() == id) {
                listaAlunos.remove(a);
                return "Aluno com ID " + id + " removido com sucesso.";
            }
        }
        return "Aluno com ID " + id + " não encontrado.";
    }

}
