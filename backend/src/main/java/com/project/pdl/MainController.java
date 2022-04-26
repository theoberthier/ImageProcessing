package com.project.pdl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.project.pdl.classes.BlurFiltre;
import com.project.pdl.classes.ColorFiltre;
import com.project.pdl.classes.HistogramEqualization;
import com.project.pdl.classes.Image;
import com.project.pdl.classes.ImageDao;
import com.project.pdl.classes.Luminosity;
import com.project.pdl.classes.Mirror;
import com.project.pdl.classes.Negatif;
import com.project.pdl.classes.RidgeFilter;

import java.awt.image.BufferedImage;

import boofcv.io.image.ConvertBufferedImage;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.image.GrayU8;
import boofcv.struct.image.Planar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
public class MainController {
    @Autowired
    private ObjectMapper mapper;
    private final ImageDao imageDao;
    @Autowired
    public MainController(ImageDao imageDao) {
      this.imageDao = imageDao;
    }

    @RequestMapping(value = "/reset/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> reset(@PathVariable("id") long id) {
      Optional<Image> img = imageDao.retrieve(id);
      if(!img.isPresent()) return new ResponseEntity<String>("Image not found", HttpStatus.NOT_FOUND);
      imageDao.replaceImage(img.get().getName());
      return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/undo/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> undoImage(@PathVariable("id") long id) {
      return imageDao.undoImage(id) ? new ResponseEntity<>(HttpStatus.ACCEPTED) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/redo/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> redoImage(@PathVariable("id") long id) {
      return imageDao.redoImage(id) ? new ResponseEntity<>(HttpStatus.ACCEPTED) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/replace/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> saveImage(@PathVariable("id") long id) {
      return imageDao.saveImage(id) ? new ResponseEntity<>(HttpStatus.ACCEPTED) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/images/{id}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<?> getImage(
      @PathVariable("id") long id,
      @RequestParam(value="algorithm", required = false) String algorithm,
      @RequestParam(value="gain", required = false) String gain,
      @RequestParam(value="radius", required = false) String radius,
      @RequestParam(value="filter", required = false) String filter,
      @RequestParam(value="band", required = false) String band,
      @RequestParam(value="delta", required = false) String delta
    ) throws IOException {
      Optional<Image> img = imageDao.retrieve(id);
      if(!img.isPresent())
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

      if(!(algorithm == null)) {
        if(algorithm.equals("luminosity")) {
          if(delta.equals(null) || delta == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            
          String input = imageDao.tmp_path + img.get().getName();
          BufferedImage input_b = UtilImageIO.loadImage(input);
          Planar<GrayU8> input_i = ConvertBufferedImage.convertFromPlanar(input_b, null, true, GrayU8.class);
  
          String output = imageDao.tmp_path + img.get().getName();
          Planar<GrayU8> output_i = input_i.clone();
  
          Luminosity.apply(input_i, output_i, Integer.valueOf(delta));
          UtilImageIO.saveImage(output_i, output);
          Path output_f = Paths.get(output);
          byte[] fileContent = Files.readAllBytes(output_f);
          return new ResponseEntity<byte[]>(fileContent, HttpStatus.ACCEPTED);
        }
        else if(algorithm.equals("color")) {
          if(delta.equals(null) || delta == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    
          String input = imageDao.tmp_path + img.get().getName();
          BufferedImage input_b = UtilImageIO.loadImage(input);
          Planar<GrayU8> input_i = ConvertBufferedImage.convertFromPlanar(input_b, null, true, GrayU8.class);
  
          String output = imageDao.tmp_path + img.get().getName();
          Planar<GrayU8> output_i = input_i.clone();  
          
          ColorFiltre.apply(input_i, output_i, Integer.valueOf(delta));
          UtilImageIO.saveImage(output_i, output);
          Path output_f = Paths.get(output);
          byte[] fileContent = Files.readAllBytes(output_f);
          return new ResponseEntity<byte[]>(fileContent, HttpStatus.ACCEPTED);
        }
        else if(algorithm.equals("ridge")) {  
          String input = imageDao.tmp_path + img.get().getName();
          BufferedImage input_b = UtilImageIO.loadImage(input);
          Planar<GrayU8> input_i = ConvertBufferedImage.convertFromPlanar(input_b, null, true, GrayU8.class);
  
          String output = imageDao.tmp_path + img.get().getName();
          Planar<GrayU8> output_i = input_i.clone();  
          
          RidgeFilter.apply(input_i, output_i);
          UtilImageIO.saveImage(output_i, output);
          Path output_f = Paths.get(output);
          byte[] fileContent = Files.readAllBytes(output_f);
          return new ResponseEntity<byte[]>(fileContent, HttpStatus.ACCEPTED);
        }
        else if(algorithm.equals("histo")) {
          if(band.equals(null) || band == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    
          String input = imageDao.tmp_path + img.get().getName();
          BufferedImage input_b = UtilImageIO.loadImage(input);
          Planar<GrayU8> input_i = ConvertBufferedImage.convertFromPlanar(input_b, null, true, GrayU8.class);
  
          String output = imageDao.tmp_path + img.get().getName();
          Planar<GrayU8> output_i = input_i.clone();  
          
          HistogramEqualization.apply(input_i, output_i, Integer.valueOf(band));
          UtilImageIO.saveImage(output_i, output);
          Path output_f = Paths.get(output);
          byte[] fileContent = Files.readAllBytes(output_f);
          return new ResponseEntity<byte[]>(fileContent, HttpStatus.ACCEPTED);
        }
        else if(algorithm.equals("blur")) {
          if(radius.equals(null) || radius == null || filter.equals(null) || filter == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  
          String input = imageDao.tmp_path + img.get().getName();
          BufferedImage input_b = UtilImageIO.loadImage(input);
          Planar<GrayU8> input_i = ConvertBufferedImage.convertFromPlanar(input_b, null, true, GrayU8.class);
  
          String output = imageDao.tmp_path + img.get().getName();
          Planar<GrayU8> output_i = input_i.clone();  
          
          BlurFiltre.apply(input_i, output_i, Integer.valueOf(radius), Integer.valueOf(filter));
          UtilImageIO.saveImage(output_i, output);
          Path output_f = Paths.get(output);
          byte[] fileContent = Files.readAllBytes(output_f);
          return new ResponseEntity<byte[]>(fileContent, HttpStatus.ACCEPTED);
        }
        else if(algorithm.equals("mirror")) {  
          String input = imageDao.tmp_path + img.get().getName();
          BufferedImage input_b = UtilImageIO.loadImage(input);
          Planar<GrayU8> input_i = ConvertBufferedImage.convertFromPlanar(input_b, null, true, GrayU8.class);
  
          String output = imageDao.tmp_path + img.get().getName();
          Planar<GrayU8> output_i = input_i.clone();  
          
          Mirror.apply(input_i, output_i);
          UtilImageIO.saveImage(output_i, output);
          Path output_f = Paths.get(output);
          byte[] fileContent = Files.readAllBytes(output_f);
          return new ResponseEntity<byte[]>(fileContent, HttpStatus.ACCEPTED);
        }
        else if(algorithm.equals("negatif")) {
          String input = imageDao.tmp_path + img.get().getName();
          BufferedImage input_b = UtilImageIO.loadImage(input);
          Planar<GrayU8> input_i = ConvertBufferedImage.convertFromPlanar(input_b, null, true, GrayU8.class);
  
          String output = imageDao.tmp_path + img.get().getName();
          Planar<GrayU8> output_i = input_i.clone();  
          
          Negatif.apply(input_i, output_i);
          UtilImageIO.saveImage(output_i, output);
          Path output_f = Paths.get(output);
          byte[] fileContent = Files.readAllBytes(output_f);
          return new ResponseEntity<byte[]>(fileContent, HttpStatus.ACCEPTED);
        }
      }

      return new ResponseEntity<byte[]>(img.get().getData(), HttpStatus.ACCEPTED);
    }
  
    @RequestMapping(value = "/images/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteImage(@PathVariable("id") long id) {
      Image img = imageDao.retrieve(id).get();
      imageDao.delete(img);
      return new ResponseEntity<>(HttpStatus.OK);
    }
  
    @RequestMapping(value = "/images", method = RequestMethod.POST)
    public ResponseEntity<?> addImage(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) throws IOException {
      String type = ImageDao.getType(file.getOriginalFilename());
      if(type.equals("JPG") || type.equals("PNG") || type.equals("JPEG")) {
        Image img = new Image(file.getOriginalFilename(), file.getBytes());
        imageDao.create(img);
        return new ResponseEntity<>(HttpStatus.OK);
      }
      else return new ResponseEntity<>(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    // Route /algos who list in JSON parser all algorithms
    @RequestMapping(value = "/algos", method = RequestMethod.GET)
    public ArrayNode getAlgos() {
      ArrayNode nodes = mapper.createArrayNode();
      JsonNode node = mapper.createObjectNode();
      ((ObjectNode) node).put("id", "negatif");
      ((ObjectNode) node).put("name", "Negatif");
      nodes.add(node);
      node = mapper.createObjectNode();
      ((ObjectNode) node).put("id", "mirror");
      ((ObjectNode) node).put("name", "Mirror");
      nodes.add(node);
      node = mapper.createObjectNode();
      ((ObjectNode) node).put("id", "ridge");
      ((ObjectNode) node).put("name", "Ridge");
      nodes.add(node);
      return nodes;
    }

    @RequestMapping(value = "/images", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public ArrayNode getImageList() {
      ArrayNode nodes = mapper.createArrayNode();
      for(Image img:imageDao.retrieveAll()) {
        JsonNode node = mapper.createObjectNode();
        ((ObjectNode) node).put("id", img.getId());
        ((ObjectNode) node).put("name", img.getName());
        ((ObjectNode) node).put("type", img.getType());
        ((ObjectNode) node).put("size", img.getSize());
        ((ObjectNode) node).put("undo", img.getUndo());
        ((ObjectNode) node).put("redo", img.getRedo());
        nodes.add(node);
      }
      return nodes;
    }
  }