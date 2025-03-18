package co.empresa.productoservice.model.repositories;

import co.empresa.productoservice.model.entities.Producto;
import org.springframework.data.repository.CrudRepository;

/**
 * Interface que hereda de CrudRepository para realizar las
 * operaciones de CRUD sobre la entidad Producto
 */
public interface IProductoRepository extends CrudRepository<Producto, Long> {
}
