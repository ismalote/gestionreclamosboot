package com.gestionreclamos.web.app.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uade.administracion.controlador.Controlador;
import com.uade.administracion.daos.PersonaDAO;
import com.uade.administracion.daos.ReclamoDAO;
import com.uade.administracion.exceptions.EdificioException;
import com.uade.administracion.exceptions.PersonaException;
import com.uade.administracion.exceptions.ReclamoException;
import com.uade.administracion.exceptions.UnidadException;
import com.uade.administracion.modelo.Persona;
import com.uade.administracion.modelo.Reclamo;
import com.uade.administracion.modelo.UbicacionReclamo;
import com.uade.administracion.views.ReclamoView;

@Controller
public class HomeController {

	private String dni;
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("titulo", "Ingresar DNI");

		return "home";
	}

	@GetMapping("/misReclamos")
	public String misReclamos(@RequestParam("dni") String dni, Model model) throws PersonaException {

		model.addAttribute("titulo", "Mis Reclamos");
		this.dni = dni;

		return "misReclamos";
	}

	@GetMapping("/agregarReclamo")
	public String agregarReclamo(Model model)
			throws EdificioException, UnidadException, ReclamoException, PersonaException {

		model.addAttribute("titulo", "Agregar reclamo");
		model.addAttribute("edificios", Controlador.getInstancia().getEdificios());

		return "agregarReclamo";
	}

	@GetMapping("/agregarImagen")
	public String upload(Model model) throws EdificioException, UnidadException, ReclamoException, PersonaException {

		model.addAttribute("titulo", "Upload de foto");

		return "formImagenReclamo";
	}

	@PostMapping(value = "/formImagenReclamo")
	public String reclamoForm(Model model, @RequestParam("idReclamo") String idReclamo,
			@RequestParam("imagen") MultipartFile imagen)
			throws EdificioException, UnidadException, ReclamoException, PersonaException, IOException {
		Reclamo reclamo = ReclamoDAO.getInstancia().findByID(Integer.parseInt(idReclamo));

		if (!imagen.isEmpty()) {
			reclamo.setImagen(imagen.getBytes());
		}

		reclamo.update();

		return "redirect:/";
	}

	@GetMapping(value = "/formPersona")
	public String saveReclamo(Model model, @RequestParam("dniPersona") String dniPersona)
			throws EdificioException, UnidadException, ReclamoException, PersonaException {
		Persona persona = PersonaDAO.getInstancia().findByID(dniPersona);

		model.addAttribute("persona", "Agregar Reclamo");

		return "formReclamo";
	}

	@PostMapping(value = "/add", consumes = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody String addReclamo(@RequestBody String reclamoJson)
			throws EdificioException, UnidadException, ReclamoException, PersonaException, JsonProcessingException {
		ReclamoView reclamo;
		System.out.println(reclamoJson);
		try {
			ObjectMapper mapper = new ObjectMapper();
			reclamo = mapper.readValue(reclamoJson, ReclamoView.class);
			reclamo = Controlador.getInstancia().agregarReclamo(reclamo.getPersonaView().getDocumento(),
					reclamo.getEdificioView().getCodigo(), UbicacionReclamo.valueOf(reclamo.getUbicacion()),
					reclamo.getDescripcion(),
					(reclamo.getUnidadView() != null) ? reclamo.getUnidadView().getPiso() : null,
					(reclamo.getUnidadView() != null) ? reclamo.getUnidadView().getNumero() : null,
					reclamo.getImagen());
			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(reclamo);
		} catch (Exception e) {
			e.printStackTrace();
			return "Hubo un error al intentar crear el reclamo.";
		}
	}

	/**
	 * Simply selects the home view to render by returning its name.
	 * 
	 * @throws PersonaException
	 * @throws ReclamoException
	 * @throws UnidadException
	 * @throws EdificioException
	 * @throws JsonProcessingException
	 */

	@GetMapping(value = "/edificios", produces = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody String getEdificios()
			throws EdificioException, UnidadException, ReclamoException, PersonaException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writerWithDefaultPrettyPrinter()
				.writeValueAsString(Controlador.getInstancia().getEdificios());
		return json;
	}

	@GetMapping(value = "/unidadesPorEdificio/{idEdificio}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody String getUnidadesPorEdificio(@PathVariable int idEdificio)
			throws EdificioException, UnidadException, ReclamoException, PersonaException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writerWithDefaultPrettyPrinter()
				.writeValueAsString(Controlador.getInstancia().getUnidadesPorEdificio(idEdificio));
		return json;
	}

	@GetMapping(value = "/habilitadosPorEdificio/{idEdificio}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody String getHabilitadosPorEdificio(@PathVariable int idEdificio)
			throws EdificioException, UnidadException, ReclamoException, PersonaException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writerWithDefaultPrettyPrinter()
				.writeValueAsString(Controlador.getInstancia().habilitadosPorEdificio(idEdificio));
		return json;
	}

	@GetMapping(value = "/dueniosPorEdificio/{idEdificio}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody String getDueniosPorEdificio(@PathVariable int idEdificio)
			throws EdificioException, UnidadException, ReclamoException, PersonaException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writerWithDefaultPrettyPrinter()
				.writeValueAsString(Controlador.getInstancia().dueniosPorEdificio(idEdificio));
		return json;
	}

	@GetMapping(value = "/inquilinosPorEdificio/{idEdificio}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody String getInquilinosPorEdificio(@PathVariable int idEdificio)
			throws EdificioException, UnidadException, ReclamoException, PersonaException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writerWithDefaultPrettyPrinter()
				.writeValueAsString(Controlador.getInstancia().inquilinosPorEdificio(idEdificio));
		return json;
	}

	@GetMapping(value = "/habitantesPorEdificio/{idEdificio}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody String getHabitantesPorEdificio(@PathVariable int idEdificio)
			throws EdificioException, UnidadException, ReclamoException, PersonaException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writerWithDefaultPrettyPrinter()
				.writeValueAsString(Controlador.getInstancia().habitantesPorEdificio(idEdificio));
		return json;
	}

	@GetMapping(value = "/dueniosPorUnidad/{idEdificio}/{piso}/{numero}", produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody String getDueniosPorUnidad(@PathVariable int idEdificio, @PathVariable String piso,
			@PathVariable String numero)
			throws EdificioException, UnidadException, ReclamoException, PersonaException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writerWithDefaultPrettyPrinter()
				.writeValueAsString(Controlador.getInstancia().dueniosPorUnidad(idEdificio, piso, numero));
		return json;
	}

	@GetMapping(value = "/inquilinosPorUnidad/{idEdificio}/{piso}/{numero}", produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody String getInquilinosPorUnidad(@PathVariable int idEdificio, @PathVariable String piso,
			@PathVariable String numero)
			throws EdificioException, UnidadException, ReclamoException, PersonaException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writerWithDefaultPrettyPrinter()
				.writeValueAsString(Controlador.getInstancia().inquilinosPorUnidad(idEdificio, piso, numero));
		return json;
	}

	@GetMapping(value = "/consultarReclamos", produces = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody String getReclamos()
			throws EdificioException, UnidadException, ReclamoException, PersonaException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writerWithDefaultPrettyPrinter()
				.writeValueAsString(Controlador.getInstancia().getReclamos());
		return json;
	}

	@GetMapping(value = "/reclamos/id/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody String getReclamoById(@PathVariable int id)
			throws EdificioException, UnidadException, ReclamoException, PersonaException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writerWithDefaultPrettyPrinter()
				.writeValueAsString(Controlador.getInstancia().getReclamosById(id));
		return json;
	}

	@GetMapping(value = "/reclamos/edificio/{edificioId}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody String getReclamosByEdifico(@PathVariable int edificioId)
			throws EdificioException, UnidadException, ReclamoException, PersonaException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writerWithDefaultPrettyPrinter()
				.writeValueAsString(Controlador.getInstancia().getReclamosByEdificio(edificioId));
		return json;
	}

	@GetMapping(value = "/reclamos/unidad/{idUnidad}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody String getReclamosByUnidad(@PathVariable int idUnidad)
			throws EdificioException, UnidadException, ReclamoException, PersonaException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writerWithDefaultPrettyPrinter()
				.writeValueAsString(Controlador.getInstancia().getReclamosByUnidad(idUnidad));
		return json;
	}

	@GetMapping(value = "/reclamos/persona/{documento}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody String getReclamoByPersona(@PathVariable String documento)
			throws EdificioException, UnidadException, ReclamoException, PersonaException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writerWithDefaultPrettyPrinter()
				.writeValueAsString(Controlador.getInstancia().getReclamosByPersona(documento));
		return json;
	}

	@GetMapping(value = "/reclamosPersona", produces = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody String getReclamoByPersonaDni()
			throws EdificioException, UnidadException, ReclamoException, PersonaException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writerWithDefaultPrettyPrinter()
				.writeValueAsString(Controlador.getInstancia().getReclamosByPersona(dni));
		return json;
	}

}
