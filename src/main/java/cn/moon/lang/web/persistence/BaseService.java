package cn.moon.lang.web.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public abstract class BaseService<T> {



    @Autowired
    protected BaseRepository<T> repository;


    public T findOne(String id) {
       return repository.findById(id).orElse(null);
    }

    public T save(T t) {
     return   repository.save(t);
    }


    public List<T> findAll(){
        return repository.findAll();
    }

    public Page<T> findAll(Pageable pageable) {
     return    repository.findAll(pageable);
    }


    public List<T> findAll(Sort sort) {
        return    repository.findAll(sort);
    }




    public Page<T> findAll(Specification<T> s, Pageable pageable) {
        return repository.findAll(s, pageable);
    }

    public List<T> findAll(Specification<T> s, Sort sort) {
        return repository.findAll(s, sort);
    }

    public void deleteById(String id) {
        repository.deleteById(id);
    }
}
