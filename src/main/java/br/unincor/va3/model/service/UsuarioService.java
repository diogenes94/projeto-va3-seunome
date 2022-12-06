/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.unincor.va3.model.service;

import br.unincor.va3.model.UsuarioException;
import br.unincor.va3.model.dao.UsuarioDao;
import br.unincor.va3.model.domain.Usuario;

/**
 *
 * @author dioge
 */
public class UsuarioService {
    
    public Usuario salvar(Usuario usuario) {
        UsuarioDao usuarioDao = new UsuarioDao();
        if(usuario.getId() != null 
                && usuarioDao.getUsuarioPorEmail(usuario.getEmail()) != null) {
            throw new UsuarioException("Já existe este e-mail cadastrado!");
        }
        
        if(usuario.getId() != null
                && usuarioDao.getUsuarioPorCpf(usuario.getCpf()) != null) {
            throw new UsuarioException("Já existe um usuário cadastrado com este CPF");
        }
        return usuarioDao.save(usuario);
    } 
}
