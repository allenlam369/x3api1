package allen.met.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import allen.met.model.Uvi;
import allen.met.repository.UviRepository;

@Repository
@Service("UviService")
public class UviService {
	static String pattern = "yyyyMMdd_HHmm"; // for reading input data
	static SimpleDateFormat sdf = new SimpleDateFormat(pattern);
	static String pattern2 = "yyyyMMdd"; // for reading input data
	static SimpleDateFormat sdf2 = new SimpleDateFormat(pattern2);
	static String pattern3 = "yyyy-MM-dd HH:mm:ss"; // for Spring to parse datetime
	static SimpleDateFormat sdf3 = new SimpleDateFormat(pattern3);

	@PersistenceContext(unitName = "met")
	@Qualifier(value = "metEntityManagerFactory")
	private EntityManager em;

	@Autowired
	private UviRepository repo;

	public Optional<Uvi> findById(long id) {
		return repo.findById(id);
	}

	@SuppressWarnings("unchecked")
	public List<Uvi> findBetween(String d1, String d2) {
		Query q = em.createNamedQuery("Uvi.findBetween");
		try {
			Date date1 = sdf.parse(d1);
			Date date2 = sdf.parse(d2);

			q.setParameter(1, date1);
			q.setParameter(2, date2);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
		}

		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Uvi> findOnDate(String d1) {
		Query q = em.createNamedQuery("Uvi.findOnDate");
		try {
			Date date1 = sdf2.parse(d1);
			q.setParameter(1, date1);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
		}
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Uvi> twoDays() {
		Query q = em.createNamedQuery("Uvi.twoDays");
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Uvi> oneWeek() {
		Query q = em.createNamedQuery("Uvi.oneWeek");
		return q.getResultList();
	}

	@Transactional
	public Uvi save(Uvi u) {
		return repo.save(u);
	}

	public Uvi saveOrUpdate(Uvi u) {
		Query q = em.createNamedQuery("Uvi.findByDatetime");
		q.setParameter(1, u.getDatetime());

		try {
			// has an existing datetime, update the uvi value
			Uvi uu = (Uvi) q.getSingleResult();
			uu.setUvi(u.getUvi());
			return save(uu);
		} catch (NoResultException e) {
			// no existing datetime, new data
			return save(u);
		}
	}

}
