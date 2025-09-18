package br.com.catolica.AtividadeSpring.Repository;

import br.com.catolica.AtividadeSpring.Models.Aluno;
import br.com.catolica.AtividadeSpring.Models.Curso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
        curso.setId(rs.getInt("curso_id"));
        curso.setNome(rs.getString("curso_nome"));
        aluno.setCurso(curso);

        return aluno;
    };
    private int getOrCreateCursoId(String nomeCurso) {
        String sqlSelect = "SELECT id FROM cursos WHERE nome = ?";
        try {
            // Tenta buscar o ID do curso se ele já existe
            return jdbcTemplate.queryForObject(sqlSelect, new Object[]{nomeCurso}, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            // Se o curso não existe, insere e retorna o novo ID
            String sqlInsert = "INSERT INTO cursos (nome) VALUES (?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, nomeCurso);
                return ps;
            }, keyHolder);
            return keyHolder.getKey().intValue();
        }
    }

    public void salva(Aluno aluno) {
        int cursoId = getOrCreateCursoId(aluno.getCurso().getNome());
        String sql = "INSERT INTO alunos (nome, idade, matricula, curso_id, email, telefone) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, aluno.getNome(), aluno.getIdade(), aluno.getMatricula(), cursoId, aluno.getEmail(), aluno.getTelefone());
    }

    public List<Aluno> listaTodos() {
        String sql = "SELECT a.*, c.nome AS curso_nome FROM alunos a LEFT JOIN cursos c ON a.curso_id = c.id";
        return jdbcTemplate.query(sql, alunoRowMapper);
    }

    public Aluno buscaPorId(int id) {
        // CORREÇÃO: Trocado JOIN por LEFT JOIN
        String sql = "SELECT a.*, c.nome AS curso_nome FROM alunos a LEFT JOIN cursos c ON a.curso_id = c.id WHERE a.id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, alunoRowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
    public List<Aluno> buscaPorNome(String nome) {
        String sql = "SELECT a.*, c.nome AS curso_nome FROM alunos a LEFT JOIN cursos c ON a.curso_id = c.id WHERE LOWER(a.nome) LIKE LOWER(?)";
        String nomeParaBusca = "%" + nome + "%";
        return jdbcTemplate.query(sql, new Object[]{nomeParaBusca}, alunoRowMapper);
    }


    public int atualiza(int id, Aluno aluno) {
        int cursoId = getOrCreateCursoId(aluno.getCurso().getNome());
        String sql = "UPDATE alunos SET nome = ?, idade = ?, matricula = ?, curso_id = ?, email = ?, telefone = ? WHERE id = ?";
        return jdbcTemplate.update(sql,
                aluno.getNome(),
                aluno.getIdade(),
                aluno.getMatricula(),
                cursoId,
                aluno.getEmail(),
                aluno.getTelefone(),
                id);
    }

    public int deleta(int id) {
        String sql = "DELETE FROM alunos WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}