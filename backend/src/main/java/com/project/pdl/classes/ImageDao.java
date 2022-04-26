package com.project.pdl.classes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import java.awt.image.BufferedImage;
import boofcv.io.image.UtilImageIO;

import org.springframework.stereotype.Repository;

@Repository
public class ImageDao implements Dao<Image> {

  private final Map<Long, Image> images = new HashMap<>();

  public String path = "src/main/resources/images/home/";
  public String tmp_path = "src/main/resources/images/tmp/";
  public String undo_path = "src/main/resources/images/undo/";
  public String redo_path = "src/main/resources/images/redo/";

  public static String getType(String path) {
    String[] parts = path.split("\\.");
    if(parts.length > 0) {
      String type = parts[parts.length-1];
      return type.toUpperCase();
    }
    else return "NOT DEFINED";
  }

  // Function to replace image name in /images/home/ to /images/tmp/
  public void replaceImage(String name) {
    File file = new File(path + name);
    File file_tmp = new File(tmp_path + name);
    try {
      Files.copy(file.toPath(), file_tmp.toPath(), StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void browseFolder(final String path) {
    File folder = new File(path);
    for(File fileEntry : folder.listFiles()) {
      if(fileEntry.isDirectory()) browseFolder(fileEntry.toPath().toString());
      else {
        String type = getType(fileEntry.toPath().toString());
        if(type.equals("JPG") || type.equals("PNG") || type.equals("JPEG")) putImage(fileEntry);
      }
    }
  }

  public boolean undoImage(final long id) {
    Optional<Image> img = Optional.ofNullable(images.get(id));
    if(!img.isPresent()) return false;
    File file = new File(path + img.get().getName());
    if(!file.exists()) return false;

    File undo_file = new File(undo_path + img.get().getName());
    if(!undo_file.exists()) return false;

    try {
      file.renameTo(new File(redo_path + img.get().getName()));
      undo_file.renameTo(new File(path + img.get().getName()));

      byte[] data = Files.readAllBytes(Paths.get(path + img.get().getName()));
      img.get().setData(data);
      img.get().setUndo(false);
      img.get().setRedo(true);
    } catch (Exception e) {
      return false;
    }

    return true;
  } 

  public boolean redoImage(final long id) {
    Optional<Image> img = Optional.ofNullable(images.get(id));
    if(!img.isPresent()) return false;
    File file = new File(path + img.get().getName());
    if(!file.exists()) return false;

    File redo_file = new File(redo_path + img.get().getName());
    if(!redo_file.exists()) return false;

    try {
      file.renameTo(new File(undo_path + img.get().getName()));
      redo_file.renameTo(new File(path + img.get().getName()));

      byte[] data = Files.readAllBytes(Paths.get(path + img.get().getName()));
      img.get().setData(data);
      img.get().setUndo(true);
      img.get().setRedo(false);
    } catch (Exception e) {
      return false;
    }

    return true;
  }

  public boolean saveImage(final long id) {
    Optional<Image> img = Optional.ofNullable(images.get(id));
    if(!img.isPresent()) return false;
    File file = new File(path + img.get().getName());
    if(!file.exists()) return false;

    File tmp_file = new File(tmp_path + img.get().getName());
    if(!tmp_file.exists()) return false;

    try {
      file.renameTo(new File(undo_path+img.get().getName()));
      tmp_file.renameTo(new File(path + img.get().getName()));
      byte[] data = Files.readAllBytes(Paths.get(path + img.get().getName()));
      img.get().setData(data);
      img.get().setUndo(true);
    } catch (Exception e) {
      return false;
    }

    return true;
  }

  private void putImage(File fileEntry) {
    byte[] fileContent;
    try {
      fileContent = Files.readAllBytes(fileEntry.toPath());
      BufferedImage input = UtilImageIO.loadImage(fileEntry.toPath().toString());
      String size = input.getWidth() + "*" + input.getHeight() + "*3";
      boolean undo = false;
      boolean redo = false;
      File undof = new File(undo_path + fileEntry.getName());
      if(undof.exists()) undo = true;
      File redof = new File(redo_path + fileEntry.getName());
      if(redof.exists()) redo = true;
      Image img = new Image(fileEntry.getName(), fileContent, size, getType(fileEntry.toPath().toString()), undo, redo);
      images.put(img.getId(), img);
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }

  public ImageDao() {
    // Init images in /images/ folder
    browseFolder(path);
  }

  @Override
  public Optional<Image> retrieve(final long id) {
    return Optional.ofNullable(images.get(id));
  }

  @Override
  public List<Image> retrieveAll() {
    List<Image> imgs = new ArrayList<Image>();
    for(Map.Entry<Long, Image> entry:images.entrySet()) imgs.add(entry.getValue());
    return imgs;
  }

  @Override
  public void create(final Image img) {
    File file = new File(path + img.getName());

    FileOutputStream fos = null;
    try {
      fos = new FileOutputStream(file);
      fos.write(img.getData());
    }
    catch(FileNotFoundException e) {
      System.out.println("File not found");
    }
    catch(IOException ioe) {
      System.out.println("IO exception");
    }
    finally {
      try {
        if(fos != null) fos.close();
      }
      catch(IOException ioe) {
        System.out.println("IO exception");
      }
    }
    
    images.put(img.getId(), img);
    BufferedImage input = UtilImageIO.loadImage(path + img.getName());
    String size = input.getWidth() + "*" + input.getHeight() + "*3";
    img.setSize(size);
    String type = getType(img.getName());
    img.setType(type);
  }

  @Override
  public void update(final Image img, final String[] params) {
    // Not used
  }

  @Override
  public void delete(final Image img) {
    images.remove(img.getId());
  }
}
