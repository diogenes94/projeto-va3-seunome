/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.unincor.va3.model.dao;

import br.unincor.va3.model.domain.Estado;
import br.unincor.va3.model.domain.Usuario;
import javax.persistence.Query;

/**
 *
 * @author dioge
 */
public class UsuarioDao extends GenericDAO<Usuario, Long> {
    
    public Usuario getUsuarioPorEmail(String email) {
        String sql = "from Usuario u where u.email = :email";
        
        Query query = getEntityManagerReadOnly()
                .createQuery(sql, Usuario.class)
                .setParameter("email", email);
        var result = query.getResultList();
        return result.isEmpty() ? null : (Usuario) result.get(0);
    }
    
    public Usuario getUsuarioPorCpf(String cpf) {
        String sql = "from Usuario u where u.cpf = :cpf";
        
        Query query = getEntityManagerReadOnly()
                .createQuery(sql, Usuario.class)
                .setParameter("cpf", cpf);
        var result = query.getResultList();
        return result.isEmpty() ? null : (Usuario) result.get(0);
    }
    
    public static void main(String[] args) {
        System.out.println(new UsuarioDao().getUsuarioPorEmail("prof.diogenes.francisco@unincor.edu.br"));
    }
    public static void main2(String[] args) {
        Usuario usuario = new Usuario();
        usuario.setNome("Diogenes");
        usuario.setCpf("124556");
        usuario.setEstado(Estado.MG);
        usuario.setEmail("prof.diogenes.francisco@unincor.edu.br");
        
        UsuarioDao usuarioDao = new UsuarioDao();
        
        System.out.println(usuarioDao.save(usuario));
    }
    
}
