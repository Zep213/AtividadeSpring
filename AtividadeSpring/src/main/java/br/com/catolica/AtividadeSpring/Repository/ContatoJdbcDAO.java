package br.com.catolica.AtividadeSpring.Repository;

import br.com.catolica.AtividadeSpring.Models.Aluno;
import br.com.catolica.AtividadeSpring.Models.Curso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ContatoJdbcDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Aluno> alunoRowMapper = (rs, rowNum) -> {
        Aluno aluno = new Aluno();
        aluno.setId(rs.getInt("id"));
        aluno.setNome(rs.getString("nome"));
        aluno.setIdade(rs.getInt("idade"));
        aluno.setMatricula(rs.getInt("matricula"));
        aluno.setEmail(rs.getString("email"));
        aluno.setTelefone(rs.getString("telefone"));
        Curso curso = new Curso();
        curso.setNome(rs.getString("curso"));
        aluno.setCurso(curso);

        return aluno;
    };

    public void salva(Aluno aluno) {
        String sql = "INSERT INTO alunos (nome, idade, matricula, curso, email, telefone) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, aluno.getNome(), aluno.getIdade(), aluno.getMatricula(), aluno.getCurso().getNome(), aluno.getEmail(), aluno.getTelefone());
    }

    public List<Aluno> listaTodos() {
        String sql = "SELECT * FROM alunos";
        return jdbcTemplate.query(sql, alunoRowMapper);
    }

    public Aluno buscaPorId(int id) {
        String sql = "SELECT * FROM alunos WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, alunoRowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
    public List<Aluno> buscaPorNome(String nome) {
        // Lower para ignorar se sao maiusculas ou nao e % para buscas parciais
        String sql = "SELECT * FROM alunos WHERE LOWER(nome) LIKE LOWER(?)";
        String nomeParaBusca = "%" + nome + "%";
        return jdbcTemplate.query(sql, new Object[]{nomeParaBusca}, alunoRowMapper);
    }


    public int atualiza(int id, Aluno aluno) {
        String sql = "UPDATE alunos SET nome = ?, idade = ?, matricula = ?, curso = ?, email = ?, telefone = ? WHERE id = ?";
        return jdbcTemplate.update(sql,
                aluno.getNome(),
                aluno.getIdade(),
                aluno.getMatricula(),
                aluno.getCurso().getNome(),
                aluno.getEmail(),
                aluno.getTelefone(),
                id);
    }

    public int deleta(int id) {
        String sql = "DELETE FROM alunos WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}