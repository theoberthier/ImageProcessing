package com.project.pdl;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.file.Files;

import com.project.pdl.classes.Image;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class ImageControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@BeforeAll
	public static void reset() {
  	// reset Image class static counter
  	ReflectionTestUtils.setField(Image.class, "count", Long.valueOf(0));
	}

	public MockMvc getMockMvc() {
		return mockMvc;
	}

	public void setMockMvc(MockMvc mockMvc) {
		this.mockMvc = mockMvc;
	}

	@Test
	@Order(1)
	public void getImageListShouldReturnSuccess() throws Exception {
		this.mockMvc.perform(get("/images")).andExpect(status().isOk());
	}

	@Test
	@Order(2)
	public void getImageShouldReturnNotFound() throws Exception {
		this.mockMvc.perform(get("/images/100")).andExpect(status().isNotFound());
	}

	@Test
	@Order(3)
	public void getImageShouldReturnSuccess() throws Exception {
		this.mockMvc.perform(get("/images/0")).andExpect(status().isAccepted());
	}

	@Test
	@Order(4)
	public void deleteImagesShouldReturnMethodNotAllowed() throws Exception {
		this.mockMvc.perform(delete("/images")).andExpect(status().isMethodNotAllowed());
	}

	@Test
	@Order(5)
	public void deleteImageShouldReturnNotFound() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.delete("/images/{id}", "100")).andExpect(status().isNotFound());
	}

	@Test
	@Order(6)
	public void deleteImageShouldReturnSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.delete("/images/{id}","0")).andExpect(status().isOk());
	}

	@Test
	@Order(7)
	public void createImageShouldReturnSuccess() throws Exception {
		final ClassPathResource imgFile = new ClassPathResource("images/paysage.jpg");
		MockMultipartFile multipart = new MockMultipartFile("file","paysage.jpg","image/jpeg",Files.readAllBytes(imgFile.getFile().toPath()));
		this.mockMvc.perform(MockMvcRequestBuilders.multipart("/images").file(multipart)).andExpect(status().isOk());
	}

	@Test
	@Order(8)
	public void createImageShouldReturnUnsupportedMediaType() throws Exception {
		final ClassPathResource imgFile = new ClassPathResource("images/td01.pdf");
		MockMultipartFile multipart = new MockMultipartFile("file","for_test.pdf","image/pdf",Files.readAllBytes(imgFile.getFile().toPath()));
		this.mockMvc.perform(MockMvcRequestBuilders.multipart("/images").file(multipart)).andExpect(status().isUnsupportedMediaType());
	}

	@Test
	@Order(9)
	public void applyAlgorithm() throws Exception {
		this.mockMvc.perform(get("/images/{id}?algorithm=luminosity&delta=10", 1)).andExpect(status().isAccepted());
		this.mockMvc.perform(get("/images/{id}?algorithm=luminosity&delta=10", 100)).andExpect(status().isNotFound());
		this.mockMvc.perform(get("/images/{id}?algorithm=", 1)).andExpect(status().isBadRequest());
		this.mockMvc.perform(get("/images/{id}?algorithm=blur&radius=10", 1)).andExpect(status().isBadRequest());
	}
}