package co.empresa.productoservice.controllers;

import co.empresa.productoservice.model.entities.Producto;
import co.empresa.productoservice.model.services.IProductoService;
import jakarta.validation.Valid;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/producto-service")
public class ProductoRestController {

    // Declaramos como final el servicio para mejorar la inmutabilidad
    private final IProductoService productoService;

    private static final String ERROR = "error";
    private static final String MENSAJE = "mensaje";
    private static final String PRODUCTO = "producto";
    private static final String PRODUCTOS = "productos";

    // Inyección de dependencia del servicio que proporciona servicios de CRUD
    public ProductoRestController(IProductoService productoService) {
        this.productoService = productoService;
    }

    /**
     * Listar todos los productos.
     */
    @GetMapping("/productos")
    public ResponseEntity<Map<String, Object>> getProductos() {
        Map<String, Object> response = new HashMap<>();

        try {
            List<Producto> productos = productoService.findAll();

            if (productos.isEmpty()) {
                response.put(MENSAJE, "No hay productos en la base de datos.");
                response.put(PRODUCTOS, productos); // para que sea siempre el mismo campo
                return ResponseEntity.status(HttpStatus.OK).body(response); // 200 pero lista vacía
            }

            response.put(PRODUCTOS, productos);
            return ResponseEntity.ok(response);

        } catch (DataAccessException e) {
            response.put(MENSAJE, "Error al consultar la base de datos.");
            response.put(ERROR, e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Listar productos con paginación.
     */
    @GetMapping("/producto/page/{page}")
    public ResponseEntity<Object> index(@PathVariable Integer page) {
        Map<String, Object> response = new HashMap<>();
        Pageable pageable = PageRequest.of(page, 4);

        try {
            Page<Producto> productos = productoService.findAll(pageable);

            if (productos.isEmpty()) {
                response.put(MENSAJE, "No hay productos en la página solicitada.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            return ResponseEntity.ok(productos);

        } catch (DataAccessException e) {
            response.put(MENSAJE, "Error al consultar la base de datos.");
            response.put(ERROR, e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (IllegalArgumentException e) {
            response.put(MENSAJE, "Número de página inválido.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    /**
     * Crear un nuevo producto pasando el objeto en el cuerpo de la petición, usando validaciones
     */
    @PostMapping("/productos")
    public ResponseEntity<Map<String, Object>> save(@Valid @RequestBody Producto producto, BindingResult result) {
        Map<String, Object> response = new HashMap<>();

        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
                    .toList();

            response.put("errors", errors);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            // Guardar el producto en la base de datos
            Producto nuevoProducto = productoService.save(producto);

            response.put(MENSAJE, "El producto ha sido creado con éxito!");
            response.put(PRODUCTO, nuevoProducto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (DataAccessException e) {
            response.put(MENSAJE, "Error al insertar el producto en la base de datos.");
            response.put(ERROR, e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    /**
     * Eliminar un producto pasando el objeto en el cuerpo de la petición.
     */
    @DeleteMapping("/productos")
    public ResponseEntity<Map<String, Object>> delete(@RequestBody Producto producto) {
        Map<String, Object> response = new HashMap<>();
        try {
            Producto productoExistente = productoService.findById(producto.getId());
            if (productoExistente == null) {
                response.put(MENSAJE, "El producto ID: " + producto.getId() + " no existe en la base de datos.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            productoService.delete(producto);
            response.put(MENSAJE, "El producto ha sido eliminado con éxito!");
            return ResponseEntity.ok(response);
        } catch (DataAccessException e) {
            response.put(MENSAJE, "Error al eliminar el producto de la base de datos.");
            response.put(ERROR, e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Actualizar un producto pasando el objeto en el cuerpo de la petición.
     * @param producto: Objeto Producto que se va a actualizar
     */
    @PutMapping("/productos")
    public ResponseEntity<Map<String, Object>> update(@Valid @RequestBody Producto producto, BindingResult result) {
        Map<String, Object> response = new HashMap<>();

        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
                    .toList();

            response.put("errors", errors);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            // Verificar si el producto existe antes de actualizar
            if (productoService.findById(producto.getId()) == null) {
                response.put(MENSAJE, "Error: No se pudo editar, el producto ID: " + producto.getId() + " no existe en la base de datos.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            // Guardar directamente el producto actualizado en la base de datos
            Producto productoActualizado = productoService.save(producto);

            response.put(MENSAJE, "El producto ha sido actualizado con éxito!");
            response.put(PRODUCTO, productoActualizado);
            return ResponseEntity.ok(response);

        } catch (DataAccessException e) {
            response.put(MENSAJE, "Error al actualizar el producto en la base de datos.");
            response.put(ERROR, e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Obtener un producto por su ID.
     */
    @GetMapping("/productos/{id}")
    public ResponseEntity<Map<String, Object>> findById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        try {
            Producto producto = productoService.findById(id);

            if (producto == null) {
                response.put(MENSAJE, "El producto ID: " + id + " no existe en la base de datos.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            response.put(MENSAJE, "El producto ha sido actualizado con éxito!");
            response.put(PRODUCTO, producto);
            return ResponseEntity.ok(response);

        } catch (DataAccessException e) {
            response.put(MENSAJE, "Error al consultar la base de datos.");
            response.put(ERROR, e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
