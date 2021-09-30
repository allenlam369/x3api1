package allen.wqplis.controller;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import allen.wqplis.model.MarineLatest;
import allen.wqplis.repository.MarineLatestRepository;

@RestController
@RequestMapping("/wqplis")
public class MarineLatestController {
	@Autowired
	MarineLatestRepository repo;

//	@Autowired
//	MarineLatestService mService;

	@PersistenceContext(unitName = "wqplis")
	@Qualifier(value = "wqplisEntityManagerFactory")
	private EntityManager em;

	@GetMapping("/marineLatest")
	public List<MarineLatest> all() {
		return repo.findAll();
	}
}
