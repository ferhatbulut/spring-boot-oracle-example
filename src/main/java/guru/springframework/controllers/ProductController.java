package guru.springframework.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.xml.crypto.Data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import guru.springframework.commands.ProductForm;
import guru.springframework.converters.ProductToProductForm;
import guru.springframework.domain.Product;
import guru.springframework.services.ProductService;

/**
 * Created by jt on 1/10/17.
 */
@Controller
public class ProductController {

	private ProductService productService;

	private ProductToProductForm productToProductForm;

	@Autowired
	public void setProductToProductForm(ProductToProductForm productToProductForm) {
		this.productToProductForm = productToProductForm;
	}

	@Autowired
	public void setProductService(ProductService productService) {
		this.productService = productService;
	}

	@RequestMapping("/auth/datas")
	@ResponseBody
	public ResponseEntity<MyResponseData> getAuthData(HttpServletRequest request,
			@RequestParam(value = "ID", defaultValue = "") String id) {
		String userAgent = request.getHeader("user-agent");
		MyResponseData myResponseData = new MyResponseData();
		myResponseData.setId("Auth / ID >>" + id);
		myResponseData.setUserAgent(userAgent);
		System.out.println(myResponseData.toString());
		return new ResponseEntity<MyResponseData>(myResponseData, HttpStatus.OK);
	}

	@RequestMapping("/nonauth/datas")
	@ResponseBody
	public ResponseEntity<MyResponseData> getNonAuthData(HttpServletRequest request,
			@RequestParam(value = "ID", defaultValue = "") String id) {
		String userAgent = request.getHeader("user-agent");
		MyResponseData myResponseData = new MyResponseData();
		myResponseData.setId("NonAuth / ID >>" + id);
		myResponseData.setUserAgent(userAgent);
		System.out.println(myResponseData.toString());
		return new ResponseEntity<MyResponseData>(myResponseData, HttpStatus.OK);
	}

	@RequestMapping("/")
	public String redirToList(@RequestBody String body, @RequestHeader HttpHeaders headers) {
		return "redirect:/product/list";
	}

	@RequestMapping({ "/product/list", "/product" })
	public String listProducts(Model model) {
		model.addAttribute("products", productService.listAll());
		return "product/list";
	}

	@RequestMapping("/product/show/{id}")
	public String getProduct(@PathVariable String id, Model model) {
		model.addAttribute("product", productService.getById(Long.valueOf(id)));
		return "product/show";
	}

	@RequestMapping("product/edit/{id}")
	public String edit(@PathVariable String id, Model model) {
		Product product = productService.getById(Long.valueOf(id));
		ProductForm productForm = productToProductForm.convert(product);

		model.addAttribute("productForm", productForm);
		return "product/productform";
	}

	@RequestMapping("/product/new")
	public String newProduct(Model model) {
		model.addAttribute("productForm", new ProductForm());
		return "product/productform";
	}

	@RequestMapping(value = "/product", method = RequestMethod.POST)
	public String saveOrUpdateProduct(@Valid ProductForm productForm, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return "product/productform";
		}

		Product savedProduct = productService.saveOrUpdateProductForm(productForm);

		return "redirect:/product/show/" + savedProduct.getId();
	}

	@RequestMapping("/product/delete/{id}")
	public String delete(@PathVariable String id) {
		productService.delete(Long.valueOf(id));
		return "redirect:/product/list";
	}

}

class MyResponseData {

	private String id;
	private String userAgent;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

}