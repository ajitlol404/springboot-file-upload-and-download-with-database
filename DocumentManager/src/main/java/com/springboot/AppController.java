package com.springboot;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AppController {

	@Autowired
	private DocumentRepository repo;
	
	@GetMapping("/")
	public String viewHomePage(Model model)
	{
		List<Document> listDocs = repo.findAll();
		model.addAttribute("listDocs", listDocs);
		return "home";
	}
	
	@PostMapping("/upload")
	public String uploadFile(@RequestParam("document") MultipartFile multipartFile,RedirectAttributes redirectAttributes) throws IOException
	{
		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		Document document=new Document();
		document.setName(fileName);
		document.setContent(multipartFile.getBytes());
		document.setSize(multipartFile.getSize());
		document.setUploadTime(new Date());
		
		repo.save(document);
		
		redirectAttributes.addFlashAttribute("message", "File uploaded successfully.");
		
		return "redirect:/";
	}
	
	@GetMapping("/downloadFile")
	public void downloadFile(@RequestParam("id") long id,HttpServletResponse response) throws Exception
	{
		Optional<Document> result = repo.findById(id);
		if(!result.isPresent())
		{
			throw new Exception("Could not find document with ID : "+id);
		}
		
		Document document = result.get();
		
		response.setContentType("application/octet-stream");
		String headerKey="Content-Disposition";
		String headerValue="attachment; filename="+document.getName();
		
		response.setHeader(headerKey, headerValue);
		
		ServletOutputStream outputStream = response.getOutputStream();
		
		outputStream.write(document.getContent());
		outputStream.close();
	}
}
