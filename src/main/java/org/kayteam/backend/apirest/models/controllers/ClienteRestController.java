package org.kayteam.backend.apirest.models.controllers;

import jakarta.validation.Valid;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.kayteam.backend.apirest.models.entity.Cliente;
import org.kayteam.backend.apirest.models.services.IClienteService;
import org.kayteam.backend.apirest.models.services.IUploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api")
public class ClienteRestController {

    @Autowired
    private IClienteService clienteService;
    @Autowired
    private IUploadFileService uploadFileService;

    @GetMapping("/clientes")
    public List<Cliente> index() {
        return clienteService.findAll();
    }

    @GetMapping("/clientes/page/{page}")
    public Page<Cliente> index(@PathVariable Integer page) {
        return clienteService.findAll(PageRequest.of(page, 4));
    }

    @GetMapping("/clientes/{id}")
    public ResponseEntity<?> show(@PathVariable Long id) {
        Cliente cliente;

        try {
            cliente = clienteService.findById(id);
        } catch (DataAccessException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Se ha producido un error al obtener el usuario con la ID " + id + ".");
            response.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (cliente == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "El cliente con el ID:" + id + " no se encuentra registrado.");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(cliente, HttpStatus.OK);

    }

    @PostMapping("/clientes")
    public ResponseEntity<?> create(@Valid @RequestBody Cliente cliente, BindingResult result) {
        Cliente clienteSaved;
        Map<String, Object> response = new HashMap<>();

        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(fieldError -> "El campo '" + fieldError.getField() + "' " + fieldError.getDefaultMessage())
                    .collect(Collectors.toList());

            response.put("errors", errors);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            clienteSaved = clienteService.save(cliente);
        } catch (DataAccessException e) {
            response.put("mensaje", "Se ha producido un error al registrar el nuevo cliente.");
            response.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "¡El cliente ha sido registrado con éxito!");
        response.put("cliente", clienteSaved);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/clientes/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> update(@Valid @RequestBody Cliente cliente, BindingResult result, @PathVariable Long id) {
        Cliente clienteActual = clienteService.findById(id);

        if (clienteActual == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "El cliente con el ID:" + id + " no se encuentra registrado.");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Cliente clienteUpdated;

        Map<String, Object> response = new HashMap<>();

        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(fieldError -> "El campo '" + fieldError.getField() + "' " + fieldError.getDefaultMessage())
                    .collect(Collectors.toList());

            response.put("errors", errors);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            clienteActual.setApellido(cliente.getApellido());
            clienteActual.setNombre(cliente.getNombre());
            clienteActual.setEmail(cliente.getEmail());

            clienteUpdated = clienteService.save(clienteActual);
        } catch (DataAccessException e) {
            response.put("mensaje", "Se ha producido un error al actualizar los datos del cliente.");
            response.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "¡El cliente ha sido registrado con éxito!");
        response.put("cliente", clienteUpdated);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/clientes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        try {
            clienteService.delete(id);
        } catch (DataAccessException e) {
            response.put("mensaje", "Se ha producido un error al eliminar el cliente.");
            response.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "¡!El cliente ha sido eliminado con éxito");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/clientes/upload")
    public ResponseEntity<?> upload(@RequestParam("archivo") MultipartFile archivo, @RequestParam("id") Long id) {
        Map<String, Object> response = new HashMap<>();
        Cliente cliente = clienteService.findById(id);

        String fileName;

        if (!archivo.isEmpty()) {
            try {
                String[] originalFileName = Objects.requireNonNull(archivo.getOriginalFilename()).split("\\.");
                fileName = "profiles/pp_" + id + "." + originalFileName[originalFileName.length - 1];
                uploadFileService.saveFile(archivo, fileName);
            } catch (IOException e) {
                e.printStackTrace();
                response.put("mensaje", "Se ha producido un error al guardar la imágen.");
                response.put("error", e.getMessage());
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            uploadFileService.deleteFile(cliente.getFoto());

            cliente.setFoto(fileName);
            clienteService.save(cliente);

            response.put("cliente", cliente);
            response.put("mensaje", "La imágen fue subida correctamente.");
        }

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @ResponseBody
    @GetMapping(value = "/uploads/pp/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getProfilePhoto(@PathVariable Long id) {
        Cliente cliente = clienteService.findById(id);
        Resource photo = null;

        Map<String, Object> response = new HashMap<>();

        if(cliente != null){
            try {
                photo = uploadFileService.loadFile(cliente.getFoto());
            } catch (MalformedURLException e) {
                try {
                    photo = uploadFileService.loadFile("profiles/no-pp.png");
                } catch (MalformedURLException ex) {
                }
            }
        }else{
            try {
                photo = uploadFileService.loadFile("profiles/no-pp.png");
            } catch (MalformedURLException ex) {
            }
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + photo.getFilename());
        try{
            return photo.getContentAsByteArray();
        }catch (IOException e){
        }
        return null;
    }
}
