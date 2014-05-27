package com.example.shufflezinho;

public class Music {
	private int id;
	private String name;
	private String fullpath;
	private String album_name;
	private int album_id;
	private String artist_name;
	private int artist_id;
	
	public Music(int id, String name, String fullpath, String album_name,
			String artist_name) {
		super();
		this.id = id;
		this.name = name;
		this.fullpath = fullpath;
		this.album_name = album_name;
		this.artist_name = artist_name;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFullpath() {
		return fullpath;
	}
	public void setFullpath(String fullpath) {
		this.fullpath = fullpath;
	}
	public String getAlbum_name() {
		return album_name;
	}
	public void setAlbum_name(String album_name) {
		this.album_name = album_name;
	}
	public int getAlbum_id() {
		return album_id;
	}
	public void setAlbum_id(int album_id) {
		this.album_id = album_id;
	}
	public String getArtist_name() {
		return artist_name;
	}
	public void setArtist_name(String artist_name) {
		this.artist_name = artist_name;
	}
	public int getArtist_id() {
		return artist_id;
	}
	public void setArtist_id(int artist_id) {
		this.artist_id = artist_id;
	}
	
	
}
