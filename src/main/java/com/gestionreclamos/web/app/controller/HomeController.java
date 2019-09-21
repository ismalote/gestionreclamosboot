package com.gestionreclamos.web.app.controller;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uade.administracion.controlador.Controlador;
import com.uade.administracion.exceptions.EdificioException;
import com.uade.administracion.exceptions.PersonaException;
import com.uade.administracion.exceptions.ReclamoException;
import com.uade.administracion.exceptions.UnidadException;
import com.uade.administracion.modelo.UbicacionReclamo;
import com.uade.administracion.views.ReclamoView;

@Controller
public class HomeController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@GetMapping("/")
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);

		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);

		String formattedDate = dateFormat.format(date);

		model.addAttribute("serverTime", formattedDate);

		return "home";
	}

	@PostMapping("/upload")
	public String upload(Model model) {
		return "EL upload se hizo correctamente.";
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
		String json = mapper.writeValueAsString(Controlador.getInstancia().getEdificios());
		return json;
	}

	@GetMapping(value = "/unidadesPorEdificio/{idEdificio}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody String getUnidadesPorEdificio(@PathVariable int idEdificio)
			throws EdificioException, UnidadException, ReclamoException, PersonaException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(Controlador.getInstancia().getUnidadesPorEdificio(idEdificio));
		return json;
	}

	@GetMapping(value = "/habilitadosPorEdificio/{idEdificio}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody String getHabilitadosPorEdificio(@PathVariable int idEdificio)
			throws EdificioException, UnidadException, ReclamoException, PersonaException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(Controlador.getInstancia().habilitadosPorEdificio(idEdificio));
		return json;
	}

	@GetMapping(value = "/dueniosPorEdificio/{idEdificio}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody String getDueniosPorEdificio(@PathVariable int idEdificio)
			throws EdificioException, UnidadException, ReclamoException, PersonaException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(Controlador.getInstancia().dueniosPorEdificio(idEdificio));
		return json;
	}

	@GetMapping(value = "/inquilinosPorEdificio/{idEdificio}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody String getInquilinosPorEdificio(@PathVariable int idEdificio)
			throws EdificioException, UnidadException, ReclamoException, PersonaException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(Controlador.getInstancia().inquilinosPorEdificio(idEdificio));
		return json;
	}

	@GetMapping(value = "/habitantesPorEdificio/{idEdificio}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody String getHabitantesPorEdificio(@PathVariable int idEdificio)
			throws EdificioException, UnidadException, ReclamoException, PersonaException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(Controlador.getInstancia().habitantesPorEdificio(idEdificio));
		return json;
	}

	@GetMapping(value = "/dueniosPorUnidad/{idEdificio}/{piso}/{numero}", produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody String getDueniosPorUnidad(@PathVariable int idEdificio, @PathVariable String piso,
			@PathVariable String numero)
			throws EdificioException, UnidadException, ReclamoException, PersonaException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(Controlador.getInstancia().dueniosPorUnidad(idEdificio, piso, numero));
		return json;
	}

	@GetMapping(value = "/inquilinosPorUnidad/{idEdificio}/{piso}/{numero}", produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody String getInquilinosPorUnidad(@PathVariable int idEdificio, @PathVariable String piso,
			@PathVariable String numero)
			throws EdificioException, UnidadException, ReclamoException, PersonaException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper
				.writeValueAsString(Controlador.getInstancia().inquilinosPorUnidad(idEdificio, piso, numero));
		return json;
	}

	@GetMapping(value = "/reclamos", produces = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody String getReclamos()
			throws EdificioException, UnidadException, ReclamoException, PersonaException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(Controlador.getInstancia().getReclamos());
		return json;
	}

	@GetMapping(value = "/reclamos/id/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody String getReclamoById(@PathVariable int id)
			throws EdificioException, UnidadException, ReclamoException, PersonaException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(Controlador.getInstancia().getReclamosById(id));
		return json;
	}

	@GetMapping(value = "/reclamos/edificio/{edificioId}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody String getReclamosByEdifico(@PathVariable int edificioId)
			throws EdificioException, UnidadException, ReclamoException, PersonaException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(Controlador.getInstancia().getReclamosByEdificio(edificioId));
		return json;
	}

	@GetMapping(value = "/reclamos/unidad/{idUnidad}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody String getReclamosByUnidad(@PathVariable int idUnidad)
			throws EdificioException, UnidadException, ReclamoException, PersonaException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(Controlador.getInstancia().getReclamosByUnidad(idUnidad));
		return json;
	}

	@GetMapping(value = "/reclamos/persona/{documento}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody String getReclamoByPersona(@PathVariable String documento)
			throws EdificioException, UnidadException, ReclamoException, PersonaException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(Controlador.getInstancia().getReclamosByPersona(documento));
		return json;
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
			return mapper.writeValueAsString(reclamo);
		} catch (Exception e) {
			e.printStackTrace();
			return "Hubo un error al intentar crear el reclamo.";
		}
	}
}
