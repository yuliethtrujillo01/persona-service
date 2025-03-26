package co.empresa.productoservice.model.repositories;

import co.empresa.productoservice.model.entities.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface que hereda de JpaRepository para realizar las
 * operaciones de CRUD paginacion y ordenamiento sobre la entidad Producto
 */
public interface IProductoRepository extends JpaRepository<Producto, Long> {
}
