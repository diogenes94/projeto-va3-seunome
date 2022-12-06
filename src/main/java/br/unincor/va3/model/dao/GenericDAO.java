package br.unincor.va3.model.dao;

import br.unincor.va3.configuracoes.HibernateUtil;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import org.hibernate.Session;

/**
 *
 * @author dioge
 */
public abstract class GenericDAO<T extends Serializable, ID> 
        implements Serializable {
    
    private Class<T> aClass;
    private EntityManager entityManager;
    private EntityManager entityManagerReadOnly;

    /* Construtor da classe */
    public GenericDAO() {
        reflectionGetTypeGenericAClass();
    }
    
    /**
     * Este método é reponsável pela geração de um entitymanager, 
     * que por sua vez será responsável de prover os recursos para uma conexão
     * estável com o banco.
     */
    protected EntityManager getEntityManager() {
        if(entityManager == null) {
            /* Criamos uma sessão a partir da configuração */
            Session session = HibernateUtil.getSessionFactory().openSession();
            /* Dizemos que ela não é read only */
            session.setDefaultReadOnly(false);
            /* Agora geramos o entity e a conexão está pronta para uso */
            EntityManagerFactory factory = session.getEntityManagerFactory();
            this.entityManager = factory.createEntityManager();
        }
        return entityManager;
    }
    
    protected EntityManager getEntityManagerReadOnly(){
        if(entityManagerReadOnly == null) {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.setDefaultReadOnly(true);
            EntityManagerFactory factory = session.getEntityManagerFactory();
            this.entityManagerReadOnly = factory.createEntityManager();
        }
        return entityManagerReadOnly;
    }
    
    /**
     * Método responsável pelo salvar, capaz de inserir ou atualizar
     * os valores do objeto configurado.
     */
    public T save(T value) {
        /* Inicia uma transação */
        getEntityManager().getTransaction().begin();
        /* Salva o objeto e o retorna com seu ID */
        T objectSave = getEntityManager().merge(value);
        
        /* Executa o commit para confirmar a operação com o banco */
        getEntityManager().getTransaction().commit();
        /* Fecha a conexão */
        getEntityManager().close();
        return objectSave;
    }
    
    /**
     * Remove qualquer objeto passado de acordo com a configuração da herança
     */
    public void delete(T value) {
        /* Acessa a traInicializa uma transação com o banco */
        getEntityManager().getTransaction().begin();        
        /* Remove o objeto passado */
        getEntityManager().remove(value);
        /* Faz o comit na transação */
        getEntityManager().getTransaction().commit();
        /* Encerra a conexão */
        getEntityManager().close();
    }
    
    /**
     * Cria uma pesquisa genérica para o ID
     */
    public T findById(ID id) {
        /**
         * Executa a chamada do entityManager e chama um método 
         * capaz de produzir uma consulta genérica por id de acordo 
         * com a classe passada, no caso aClass*/
        return getEntityManagerReadOnly().find(aClass, id);
    }
    
    /**
     * Método responsável por criar uma busca genérica de todos valores
     * @return lista com todos objetos da entidade
     */
    public List<T> findAll() {
        Query query = getEntityManagerReadOnly()
                .createQuery("from " + aClass.getSimpleName());
        return query.getResultList();
    }
    
    /**
     * Este método incializa o primeiro parametro da nossa classe GenericDao
     * Ele é responsável por identificar o tipo de T e atribuílo para 
     * a variável aClass que posteriormente é necessário para a criação
     * de métodos dinâmicos.
     */
    private void reflectionGetTypeGenericAClass() {
        aClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }    
    
}
