package site.notion.dokuny.stock_dividend.web;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.notion.dokuny.stock_dividend.model.Company;
import site.notion.dokuny.stock_dividend.persist.entity.CompanyEntity;
import site.notion.dokuny.stock_dividend.service.CompanyService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/company")
public class CompanyController {

	private final CompanyService companyService;


	@GetMapping("/autocomplete")
	public ResponseEntity<List<String>> autocomplete(@RequestParam String keyword) {
		return ResponseEntity.ok(companyService.autocomplete(keyword));
	}

	@GetMapping
	@PreAuthorize("hasRole('READ')")
	public ResponseEntity<Page<Company>> searchCompany(final Pageable pageable) {
		return ResponseEntity.ok(companyService.getAllCompany(pageable));
	}

	@PostMapping
	@PreAuthorize("hasRole('WRITE')")
	public ResponseEntity<Company> addCompany(@RequestBody Company request) {
		String ticker = request.getTicker().trim();

		if (ObjectUtils.isEmpty(ticker)) {
			throw new RuntimeException("ticker is empty");
		}

		Company company = companyService.save(ticker);

		return ResponseEntity.ok(company);
	}

	@DeleteMapping("/{ticker}")
	@PreAuthorize("hasRole('WRITE')")
	public ResponseEntity<?> deleteCompany(@PathVariable String ticker) {
		return ResponseEntity.ok(companyService.deleteCompany(ticker));
	}
}
