package com.project.pdl.classes;

public class Image {
    private static Long count = Long.valueOf(1);
    private Long id;
    private String name;
    private byte[] data;
    private String size;
    private String type;
    private boolean undo;
    private boolean redo;
  
    public Image(final String name, final byte[] data) {
      id = count++;
      this.name = name;
      this.data = data;
    }

    public Image(final String name, final byte[] data, final String size, final String type, final boolean undo, final boolean redo) {
      id = count++;
      this.name = name;
      this.data = data;
      this.size = size;
      this.type = type;
      this.undo = undo;
      this.redo = redo;
    }
  
    public long getId() {
      return id;
    }
  
    public String getName() {
      return name;
    }
  
    public void setName(final String name) {
      this.name = name;
    }
  
    public byte[] getData() {
      return data;
    }

    public void setData(byte[] data) {
      this.data = data;
    }

    public boolean getUndo() {
      return undo;
    }

    public void setUndo(boolean status) {
      this.undo = status;
    }

    public boolean getRedo() {
      return redo;
    }

    public void setRedo(boolean status) {
      this.redo = status;
    }

    public String getType() {
      return type;
    }

    public String getSize() {
      return size;
    }
    public void setSize(final String size) {
      this.size = size;
    }
    public void setType(final String type) {
      this.type = type;
    }
  }
  