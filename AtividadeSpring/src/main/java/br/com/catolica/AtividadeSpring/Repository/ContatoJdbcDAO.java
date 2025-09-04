package br.com.catolica.AtividadeSpring.Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ContatoJdbcDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;
}
